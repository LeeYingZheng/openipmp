package se.anatom.ejbca.ca.store.junit;

import java.util.Random;
import java.util.*;
import java.lang.Integer;
import java.io.*;

import java.security.cert.*;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import java.rmi.RemoteException;

import se.anatom.ejbca.ra.*;
import se.anatom.ejbca.ca.sign.*;
import se.anatom.ejbca.ca.store.*;
import se.anatom.ejbca.ca.crl.*;
import se.anatom.ejbca.util.*;
import se.anatom.ejbca.SecConst;

import org.apache.log4j.*;
import junit.framework.*;


/** Tests certificate store.
 *
 * @version $Id: TestCertificateData.java,v 1.1 2006/06/09 17:09:07 danijel Exp $
 */
public class TestCertificateData extends TestCase {

    static byte[] testcert = Base64.decode(
    ("MIICETCCAXqgAwIBAgIIEzy5vc2xpOIwDQYJKoZIhvcNAQEFBQAwLjEOMAwGA1UE"
    +"AxMFZWpiY2ExDzANBgNVBAoTBkFuYVRvbTELMAkGA1UEBhMCU0UwHhcNMDExMTE0"
    +"MTMxODU5WhcNMDMxMTE0MTMyODU5WjAsMQwwCgYDVQQDEwNmb28xDzANBgNVBAoT"
    +"BkFuYVRvbTELMAkGA1UEBhMCU0UwXDANBgkqhkiG9w0BAQEFAANLADBIAkEAqPX5"
    +"YOgT76Tz5uDOmegzA6RRdOFR7/nyWc8Wu4FnU6litDqo1wQCD9Pqtq6XzWJ1smD5"
    +"svNhscRcXPeiucisoQIDAQABo34wfDAPBgNVHRMBAf8EBTADAQEAMA8GA1UdDwEB"
    +"/wQFAwMHoAAwHQYDVR0OBBYEFMrdBFmXrmAtP65uHZmF2Jc3shB1MB8GA1UdIwQY"
    +"MBaAFHxNs2NoKyv7/ipWKfwRyGU6d6voMBgGA1UdEQQRMA+BDWZvb0BhbmF0b20u"
    +"c2UwDQYJKoZIhvcNAQEFBQADgYEAH6AqvzaReZFMvYudIY6lCT5shodNTyjZBT0/"
    +"kBMHp1csVVqJl80Ngr2QzKE55Xhok05i7q9oLcRSbnQ8ZfnTDa9lZaWiZzX7LxF/"
    +"5fd74ol2m/J2LvVglqH9VEINI4RE+HxrMFy8QMROYbsOhl8Jk9TOsuDeQjEtgodm"
    +"gY5ai2k=").getBytes());

    static Category cat = Category.getInstance( TestCertificateData.class.getName() );
    private static Context ctx;
    private static CertificateDataHome home;
    private static ICertificateStoreSessionHome storehome; 
    private static X509Certificate cert;

    public TestCertificateData(String name) {
        super(name);
    }
    protected void setUp() throws Exception {
        cat.debug(">setUp()");
        ctx = getInitialContext();
        Object obj = ctx.lookup("CertificateData");
        home = (CertificateDataHome) javax.rmi.PortableRemoteObject.narrow(obj, CertificateDataHome.class);
        Object obj2 = ctx.lookup("CertificateStoreSession");
        storehome = (ICertificateStoreSessionHome) javax.rmi.PortableRemoteObject.narrow(obj2, ICertificateStoreSessionHome.class);
        cert = CertTools.getCertfromByteArray(testcert);
        cat.debug("<setUp()");
    }
    protected void tearDown() throws Exception {
    }
    private Context getInitialContext() throws NamingException {
        cat.debug(">getInitialContext");
        Context ctx = new javax.naming.InitialContext();
        cat.debug("<getInitialContext");
        return ctx;
    }

    public void test01CreateNewCert() throws Exception {
        cat.debug(">test01CreateNewCert()");
        X509Certificate cert = CertTools.getCertfromByteArray(testcert);
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        cat.debug("keyed it! ="+ pk);

        CertificateData data1=null;
        try {
            data1 = home.create(cert);
            assertNotNull("Failed to create", data1);
            cat.debug("created it!");
        } catch (javax.ejb.DuplicateKeyException e) {
            home.remove(pk);
            cat.debug("Removed it!");
            data1 = home.create(cert);
            assertNotNull("Failed to create", data1);
            cat.debug("created it!");
            return;
        }
        cat.debug("<test01CreateNewCert()");
    }
    public void test02FindAndChange() throws Exception {
        cat.debug(">test02FindAndChange()");
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        CertificateData data2 = home.findByPrimaryKey(pk);
        assertNotNull("Failed to find cert", data2);
        cat.debug("found by key! ="+ data2);
        cat.debug("fp="+data2.getFingerprint());
        cat.debug("issuer="+data2.getIssuerDN());
        cat.debug("subject="+data2.getSubjectDN());
        cat.debug("cafp="+data2.getCAFingerprint());
        cat.debug("status="+data2.getStatus());
        cat.debug("type="+data2.getType());
        cat.debug("serno="+data2.getSerialNumber());
        cat.debug("expiredate="+data2.getExpireDate());
        cat.debug("revocationdate="+data2.getRevocationDate());
        cat.debug("revocationreason="+data2.getRevocationReason());

        data2.setCAFingerprint("12345");
        data2.setStatus(CertificateData.CERT_REVOKED);
        data2.setType(SecConst.USER_ENDUSER);
        data2.setRevocationDate(new Date());
        data2.setRevocationReason(CRLData.REASON_KEYCOMPROMISE);
        cat.debug("Changed it");
       cat.debug("<test02FindAndChange()");
    }

    public void test03listAndRevoke() throws Exception {
        cat.debug(">test03listAndRevoke()");
        ICertificateStoreSessionRemote store = storehome.create();
        String[] certfps = store.listAllCertificates();
        assertNotNull("failed to list certs", certfps);
        assertTrue("failed to list certs", certfps.length != 0);
        cat.debug("List certs:");
        for (int i=0;i< certfps.length;i++)
            cat.debug(certfps[i]);

        // Revoke all certs
        for (int i=0; i<certfps.length;i++) {
            CertificateDataPK revpk = new CertificateDataPK();
            revpk.fp = certfps[i];
            CertificateData rev = home.findByPrimaryKey(revpk);
            if (rev.getStatus() != CertificateData.CERT_REVOKED) {
                rev.setStatus(CertificateData.CERT_REVOKED);
                rev.setRevocationDate(new Date());
                cat.debug("Revoked cert "+certfps[i]);
            }
        }
        cat.debug("<test03listAndRevoke()");
    }
    public void test04CheckRevoked() throws Exception {
        cat.debug(">test04CheckRevoked()");
        ICertificateStoreSessionRemote store = storehome.create();
        String[] certfps = store.listAllCertificates();
        assertNotNull("failed to list certs", certfps);
        assertTrue("failed to list certs", certfps.length != 0);
        // Verify that all certs are revoked
        for (int i=0; i<certfps.length;i++) {
            CertificateDataPK revpk = new CertificateDataPK();
            revpk.fp = certfps[i];
            CertificateData rev = home.findByPrimaryKey(revpk);
            assertTrue(rev.getStatus() == CertificateData.CERT_REVOKED);
            }
        cat.debug("<test04CheckRevoked()");
    }
    public void test05FindAgain() throws Exception {
        cat.debug(">test05FindAgain()");
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        CertificateData data3 = home.findByPrimaryKey(pk);
        assertNotNull("Failed to find cert", data3);
        cat.debug("found by key! ="+ data3);
        cat.debug("fp="+data3.getFingerprint());
        cat.debug("issuer="+data3.getIssuerDN());
        cat.debug("subject="+data3.getSubjectDN());
        cat.debug("cafp="+data3.getCAFingerprint());
        assertNotNull("wrong CAFingerprint", data3.getCAFingerprint());
        cat.debug("status="+data3.getStatus());
        assertTrue("wrong status", data3.getStatus() == CertificateData.CERT_REVOKED);
        cat.debug("type="+data3.getType());
        assertTrue("wrong type", (data3.getType() & SecConst.USER_ENDUSER) == SecConst.USER_ENDUSER);
        cat.debug("serno="+data3.getSerialNumber());
        cat.debug("expiredate="+data3.getExpireDate());
        cat.debug("revocationdate="+data3.getRevocationDate());
        cat.debug("revocationreason="+data3.getRevocationReason());
        assertTrue("wrong reason", (data3.getRevocationReason() & CRLData.REASON_KEYCOMPROMISE) == CRLData.REASON_KEYCOMPROMISE);

        cat.debug("Looking for cert with DN="+cert.getSubjectDN().toString());
        ICertificateStoreSessionRemote store = storehome.create();
        Certificate[] certs = store.findCertificatesBySubject(cert.getSubjectDN().toString());
        for (int i=0;i<certs.length;i++) {
            X509Certificate xcert = (X509Certificate)certs[i];
            cat.debug(xcert.getSubjectDN().toString()+" - "+xcert.getSerialNumber().toString());
            //cat.debug(certs[i].toString());
        }
        cat.debug("<test05FindAgain()");
    }
    public void test06FindByExpireTime() throws Exception {
        cat.debug(">test06FindByExpireTime()");
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        CertificateData data = home.findByPrimaryKey(pk);
        assertNotNull("Failed to find cert", data);
        cat.debug("expiredate="+data.getExpireDate());

        // Seconds in a year
        long yearmillis = 365*24*60*60*1000;
        long findDateSecs = data.getExpireDate().getTime() - (yearmillis*100);
        Date findDate = new Date(findDateSecs);
        
        ICertificateStoreSessionRemote store = storehome.create();
        cat.debug("1. Looking for cert with expireDate="+findDate);
        Certificate[] certs = store.findCertificatesByExpireTime(findDate);
        cat.debug("findCertificatesByExpireTime returned "+ certs.length+" certs.");
        assertTrue("No certs should have expired before this date", certs.length == 0);
        findDateSecs = data.getExpireDate().getTime() + 10000;
        findDate = new Date(findDateSecs);
        cat.debug("2. Looking for cert with expireDate="+findDate);
        certs = store.findCertificatesByExpireTime(findDate);
        cat.debug("findCertificatesByExpireTime returned "+ certs.length+" certs.");
        assertTrue("Some certs should have expired before this date", certs.length != 0);
        for (int i=0;i<certs.length;i++) {
            Date retDate = ((X509Certificate)certs[i]).getNotAfter();
            cat.debug(retDate);
            assertTrue("This cert is not expired by the specified Date.", retDate.getTime() < findDate.getTime()); 
        }
        cat.debug("<test06FindByExpireTime()");
    }

    public void test07FindByIssuerAndSerno() throws Exception {
        cat.debug(">test07FindByIssuerAndSerno()");
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        CertificateData data3 = home.findByPrimaryKey(pk);
        assertNotNull("Failed to find cert", data3);

        cat.debug("Looking for cert with DN:"+cert.getIssuerDN().toString()+" and serno "+cert.getSerialNumber());
        ICertificateStoreSessionRemote store = storehome.create();
        Certificate fcert = store.findCertificateByIssuerAndSerno(cert.getIssuerDN().toString(), cert.getSerialNumber());
        assertNotNull("Cant find by issuer and serno", fcert);
        //cat.debug(fcert.toString());
        cat.debug("<test07FindByIssuerAndSerno()");
    }

    public void test08IsRevoked() throws Exception {
        cat.debug(">test08IsRevoked()");
        CertificateDataPK pk = new CertificateDataPK();
        pk.fp = CertTools.getFingerprintAsString(cert);
        CertificateData data3 = home.findByPrimaryKey(pk);
        assertNotNull("Failed to find cert", data3);
        cat.debug("found by key! ="+ data3);
        cat.debug("fp="+data3.getFingerprint());
        cat.debug("issuer="+data3.getIssuerDN());
        cat.debug("subject="+data3.getSubjectDN());
        cat.debug("cafp="+data3.getCAFingerprint());
        assertNotNull("wrong CAFingerprint", data3.getCAFingerprint());
        cat.debug("status="+data3.getStatus());
        assertTrue("wrong status", data3.getStatus() == CertificateData.CERT_REVOKED);
        cat.debug("type="+data3.getType());
        assertTrue("wrong type", (data3.getType() & SecConst.USER_ENDUSER) == SecConst.USER_ENDUSER);
        cat.debug("serno="+data3.getSerialNumber());
        cat.debug("expiredate="+data3.getExpireDate());
        cat.debug("revocationdate="+data3.getRevocationDate());
        cat.debug("revocationreason="+data3.getRevocationReason());
        assertTrue("wrong reason", (data3.getRevocationReason() & CRLData.REASON_KEYCOMPROMISE) == CRLData.REASON_KEYCOMPROMISE);

        cat.debug("Checking if cert is revoked DN:'"+cert.getIssuerDN().toString()+"', serno:'"+cert.getSerialNumber().toString()+"'.");
        ICertificateStoreSessionRemote store = storehome.create();
        RevokedCertInfo revinfo = store.isRevoked(cert.getIssuerDN().toString(), cert.getSerialNumber());
        assertNotNull("Certificate not revoked, it should be!", revinfo);
        assertTrue("Wrong revocationDate!", revinfo.getRevocationDate().getTime() == data3.getRevocationDate().getTime());
        assertTrue("Wrong reason!", revinfo.getReason() == data3.getRevocationReason());
        home.remove(pk);
        cat.debug("Removed it!");
        cat.debug("<test08IsRevoked()");
    }

}

