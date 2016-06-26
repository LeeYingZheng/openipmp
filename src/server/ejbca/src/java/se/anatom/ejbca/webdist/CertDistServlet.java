
package se.anatom.ejbca.webdist;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;
import java.security.cert.*;
import java.math.BigInteger;

import javax.rmi.PortableRemoteObject;
import javax.naming.InitialContext;

import se.anatom.ejbca.util.Base64;

import org.apache.log4j.*;

import se.anatom.ejbca.ca.store.ICertificateStoreSession;
import se.anatom.ejbca.ca.store.ICertificateStoreSessionHome;
import se.anatom.ejbca.ca.sign.ISignSessionHome;
import se.anatom.ejbca.ca.sign.ISignSessionRemote;
import se.anatom.ejbca.ca.crl.RevokedCertInfo;
import se.anatom.ejbca.util.CertTools;

/**
 * Servlet used to distribute certificates and CRLs.<br>
 *
 * The servlet is called with method GET and syntax 'command=<command>'.
 * <p>The follwing commads are supported:<br>
 * <ul>
 * <li>crl - gets the latest CRL.
 * <li>lastcert - gets latest certificate of a user, takes argument 'subject=<subjectDN>'.
 * <li>listcerts - lists all certificates of a user, takes argument 'subject=<subjectDN>'.
 * <li>revoked - checks if a certificate is revoked, takes arguments 'subject=<subjectDN>&serno=<serial number>'.
 * <li>cacert - returns ca certificate in PEM-format
 * <li>nscacert - returns ca certificate for Netscape/Mozilla
 * <li>iecacert - returns ca certificate for Internet Explorer
 * </ul>
 * cacert, nscacert and iecacert also takes optional parameter level=<int 1,2,...>, where the level is
 * which ca certificate in a hierachy should be returned. 0=root (default), 1=sub to root etc.
 *
 * @version $Id: CertDistServlet.java,v 1.1 2006/06/09 17:09:07 danijel Exp $
 *
 */
public class CertDistServlet extends HttpServlet {

    static private Category cat = Category.getInstance( CertDistServlet.class.getName() );

    private static final String COMMAND_PROPERTY_NAME = "cmd";
    private static final String COMMAND_CRL = "crl";
    private static final String COMMAND_REVOKED = "revoked";
    private static final String COMMAND_CERT = "lastcert";
    private static final String COMMAND_LISTCERT = "listcerts";
    private static final String COMMAND_NSCACERT = "nscacert";
    private static final String COMMAND_IECACERT = "iecacert";
    private static final String COMMAND_CACERT = "cacert";

    private static final String SUBJECT_PROPERTY = "subject";
    private static final String ISSUER_PROPERTY = "issuer";
    private static final String SERNO_PROPERTY = "serno";
    private static final String LEVEL_PROPERTY = "level";

    private InitialContext ctx = null;
    ICertificateStoreSessionHome storehome = null;
    ISignSessionHome signhome = null;

    public void init(ServletConfig config) throws ServletException {
    super.init(config);
        try {

            // Get EJB context and home interfaces
            ctx = new InitialContext();
            storehome = (ICertificateStoreSessionHome) PortableRemoteObject.narrow(
            ctx.lookup("CertificateStoreSession"), ICertificateStoreSessionHome.class );
            signhome = (ISignSessionHome) PortableRemoteObject.narrow(ctx.lookup("RSASignSession"), ISignSessionHome.class );
        } catch( Exception e ) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws IOException {
        cat.debug(">doPost()");
        res.setContentType("text/html");
        res.getOutputStream().println("The certificate/CRL distribution servlet only handles GET method.");
        cat.debug("<doPost()");
    } //doPost

    public void doGet(HttpServletRequest req,  HttpServletResponse res) throws java.io.IOException, ServletException {
        cat.debug(">doGet()");

        String command;
        // Keep this for logging.
        String remoteAddr = req.getRemoteAddr();
        command = req.getParameter(COMMAND_PROPERTY_NAME);
        if (command == null)
            command = "";
        if (command.equalsIgnoreCase(COMMAND_CRL)) {
            try {
                ICertificateStoreSession store = storehome.create();
                byte[] crl = store.getLastCRL();
                X509CRL x509crl = CertTools.getCRLfromByteArray(crl);
                String dn = x509crl.getIssuerDN().toString();
                String filename = CertTools.getPartFromDN(dn,"CN")+".crl";
                res.setHeader("Content-disposition", "attachment; filename=" +  filename);
                res.setContentType("application/octet-stream");
                res.setContentLength(crl.length);
                res.getOutputStream().write(crl);
                cat.info("Sent latest CRL to client at " + remoteAddr);
            } catch (Exception e) {
                PrintStream ps = new PrintStream(res.getOutputStream());
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Error getting latest CRL.");
                e.printStackTrace(ps);
                cat.error("Error sending latest CRL to " + remoteAddr);
                cat.error(e);
                return;
            }
        } else if (command.equalsIgnoreCase(COMMAND_CERT) || command.equalsIgnoreCase(COMMAND_LISTCERT)) {
            String dn = req.getParameter(SUBJECT_PROPERTY);
            if (dn == null) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Usage command=cert?subject=<subjectdn>.");
                cat.debug("Bad request, no 'dn' arg to 'lastcert' or 'listcert' command.");
                return;
            }
            try {
                cat.debug("Looking for certificates for '"+dn+"'.");
                ICertificateStoreSession store = storehome.create();
                Certificate[] certs = store.findCertificatesBySubject(dn);
                int latestcertno = -1;
                if (command.equalsIgnoreCase(COMMAND_CERT)) {
                    long maxdate = 0;
                    for (int i=0;i<certs.length;i++) {
                        if (i == 0) {
                            maxdate = ((X509Certificate)certs[i]).getNotBefore().getTime();
                            latestcertno = 0;
                        }
                        else if ( ((X509Certificate)certs[i]).getNotBefore().getTime() > maxdate ) {
                            maxdate = ((X509Certificate)certs[i]).getNotBefore().getTime();
                            latestcertno = i;
                        }
                    }
                    if (latestcertno > -1) {
                        byte[] cert = certs[latestcertno].getEncoded();
                        String filename = CertTools.getPartFromDN(dn,"CN")+".cer";
                        res.setHeader("Content-disposition", "attachment; filename=" +  filename);
                        res.setContentType("application/octet-stream");
                        res.setContentLength(cert.length);
                        res.getOutputStream().write(cert);
                        cat.info("Sent latest certificate for '"+dn+"' to client at " + remoteAddr);
                        
                    } else {
                        res.sendError(HttpServletResponse.SC_NOT_FOUND, "No certificate found for requested subject '"+dn+"'.");
                        cat.debug("No certificate found for '"+dn+"'.");
                    }
                }
                if (command.equalsIgnoreCase(COMMAND_LISTCERT)) {
                    res.setContentType("text/html");
                    PrintWriter pout = new PrintWriter(res.getOutputStream());
                    pout.println("<html><head><title>Certificates for "+dn+"</title></head>");
                    pout.println("<body><p>");
                    for (int i=0;i<certs.length;i++) {
                        Date notBefore = ((X509Certificate)certs[i]).getNotBefore();
                        Date notAfter = ((X509Certificate)certs[i]).getNotAfter();
                        String subject = ((X509Certificate)certs[i]).getSubjectDN().toString();
                        String issuer = ((X509Certificate)certs[i]).getIssuerDN().toString();
                        BigInteger serno = ((X509Certificate)certs[i]).getSerialNumber();
                        pout.println("<pre>Subject:"+subject);
                        pout.println("Issuer:"+issuer);
                        pout.println("NotBefore:"+notBefore.toString());
                        pout.println("NotAfter:"+notAfter.toString());
                        pout.println("Serial number:"+serno.toString());
                        pout.println("</pre>");
                        pout.println("<a href=\"certdist?cmd=revoked&issuer="+issuer+"&serno="+serno.toString()+"\">Check if certificate is revoked</a>");
                        pout.println("<hr>");
                        
                    }
                    if (certs.length == 0) {
                        pout.println("No certificates exists for '"+dn+"'.");
                    }
                    pout.println("</body></html>");
                    pout.close();
                }
            } catch (Exception e) {
                PrintStream ps = new PrintStream(res.getOutputStream());
                e.printStackTrace(ps);
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Error getting certificates.");
                cat.error("Error getting certificates for '"+dn+"' for "+remoteAddr);
                cat.error(e);
                return;
            }
        } else if (command.equalsIgnoreCase(COMMAND_NSCACERT) || command.equalsIgnoreCase(COMMAND_IECACERT) || command.equalsIgnoreCase(COMMAND_CACERT) ) {
            String lev = req.getParameter(LEVEL_PROPERTY);
            int level = 0;
            if (lev != null)
                level = Integer.parseInt(lev);
            // Root CA is level 0, next below root level 1 etc etc
            try {
                ISignSessionRemote ss = signhome.create();
                Certificate[] chain = ss.getCertificateChain();
                // chain.length-1 is last cert in chain (root CA)
                if ( (chain.length-1-level) < 0 ) {
                    PrintStream ps = new PrintStream(res.getOutputStream());
                    ps.println("No CA certificate of level "+level+"exist.");
                    cat.error("No CA certificate of level "+level+"exist.");
                    return;
                }
                X509Certificate cacert = (X509Certificate)chain[chain.length-1-level];
                byte[] enccert = cacert.getEncoded();
                if (command.equalsIgnoreCase(COMMAND_NSCACERT)) {
                    res.setContentType("application/x-x509-ca-cert");
                    res.setContentLength(enccert.length);
                    res.getOutputStream().write(enccert);
                    cat.debug("Sent CA cert to NS client, len="+enccert.length+".");
                } else if (command.equalsIgnoreCase(COMMAND_IECACERT)) {
                    res.setHeader("Content-disposition", "attachment; filename=ca.crt");
                    res.setContentType("application/octet-stream");
                    res.setContentLength(enccert.length);
                    res.getOutputStream().write(enccert);
                    cat.debug("Sent CA cert to IE client, len="+enccert.length+".");
                } else if (command.equalsIgnoreCase(COMMAND_CACERT)) {
                    byte[] b64cert = Base64.encode(enccert);
                    String out = "-----BEGIN CERTIFICATE-----\n";
                    out += new String(b64cert);
                    out += "\n-----END CERTIFICATE-----\n";
                    res.setHeader("Content-disposition", "attachment; filename=ca.pem");
                    res.setContentType("application/octet-stream");
                    res.setContentLength(out.length());
                    res.getOutputStream().write(out.getBytes());
                    cat.debug("Sent CA cert to client, len="+out.length()+".");
                } else {
                    res.setContentType("text/plain");
                    res.getOutputStream().println("Commands="+COMMAND_NSCACERT+" || "+COMMAND_IECACERT+" || "+COMMAND_CACERT);
                    return;
                }
            } catch (Exception e) {
                PrintStream ps = new PrintStream(res.getOutputStream());
                e.printStackTrace(ps);
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Error getting CA certificates.");
                cat.error("Error getting CA certificates.");
                cat.error(e);
                return;
            }            
        } else if (command.equalsIgnoreCase(COMMAND_REVOKED)) {
            String dn = req.getParameter(ISSUER_PROPERTY);
            if (dn == null) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Usage command=revoked?issuer=<subjectdn>&serno=<serianlnumber>.");
                cat.debug("Bad request, no 'issuer' arg to 'revoked' command.");
                return;
            }
            String serno = req.getParameter(SERNO_PROPERTY);
            if (serno == null) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Usage command=revoked?issuer=<subjectdn>&serno=<serianlnumber>.");
                cat.debug("Bad request, no 'serno' arg to 'revoked' command.");
                return;
            }
            cat.debug("Looking for certificate for '"+dn+"' and serno='"+serno+"'.");
            try {
                ICertificateStoreSession store = storehome.create();
                RevokedCertInfo revinfo = store.isRevoked(dn, new BigInteger(serno));
                res.setContentType("text/html");
                PrintWriter pout = new PrintWriter(res.getOutputStream());
                pout.println("<html><head><title>Check revocation</title></head>");
                pout.println("<body><p>");
                if (revinfo != null) {
                    pout.println("<h1>REVOKED</h1>");
                    pout.println("Certificate with issuer '"+dn+"' and serial number '"+serno+"' is revoked");
                    pout.println("RevocationDate is '"+revinfo.getRevocationDate()+"' and reason '"+revinfo.getReason()+"'.");
                } else {
                    pout.println("<h1>NOT REVOKED</h1>");
                    pout.println("Certificate with issuer '"+dn+"' and serial number '"+serno+"' is NOT revoked");
                }
                pout.println("</body></html>");
                pout.close();
            } catch (Exception e) {
                PrintStream ps = new PrintStream(res.getOutputStream());
                e.printStackTrace(ps);
                res.sendError(HttpServletResponse.SC_NOT_FOUND, "Error checking revocation.");
                cat.error("Error checking revocation for '"+dn+"' with serno '"+serno+"'.");
                cat.error(e);
                return;
            }                
        } else {
            res.setContentType("text/plain");
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Commands=lastcert | listcerts | crl | revoked");
            return;
        }

    } // doGet

    /**
     * Prints debug info back to browser client
     **/
    private class Debug {
        final private ByteArrayOutputStream buffer;
        final private PrintStream printer;
        Debug( ){
            buffer=new ByteArrayOutputStream();
            printer=new PrintStream(buffer);

            print("<html>");
            print("<body>");
            print("<head>");

            String title = "Certificate/CRL distribution servlet";
            print("<title>" + title + "</title>");
            print("</head>");
            print("<body bgcolor=\"white\">");

            print("<h2>" + title + "</h2>");
        }

        void printDebugInfo(OutputStream out) throws IOException {
            print("</body>");
            print("</html>");
            out.write(buffer.toByteArray());
        }

        void print(Object o) {
            printer.println(o);
        }
        void printInsertLineBreaks( byte[] bA ) throws Exception {
            BufferedReader br=new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(bA)) );
            while ( true ){
                String line=br.readLine();
                if (line==null)
                    break;
                print(line.toString()+"<br>");
            }
        }
        void takeCareOfException(Throwable t ) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            print("<h4>Exception:</h4>");
            try {
                printInsertLineBreaks( baos.toByteArray() );
            } catch (Exception e) {
                e.printStackTrace(printer);
            }
        }
    }
}
