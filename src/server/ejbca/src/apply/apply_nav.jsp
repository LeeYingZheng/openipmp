<%@ page language="Java" import="javax.naming.*,javax.rmi.*,java.util.*,java.security.cert.*,se.anatom.ejbca.ca.sign.*"%>

<HTML>
<HEAD><TITLE>EJBCA Mozilla Certificate enroll</TITLE></HEAD>
<BODY bgcolor="#ffffff" link="black" vlink="black" alink="black">

<center>
<FONT face=arial size="3"><strong>EJBCA Mozilla Certificate Enrollment
</strong></FONT>
</center>

<HR>
Welcome to certificate enrollment. <BR>
If you haven't done so already, you must first install the CA certificate(s) in your browser.

<P>Install CA certificates:

<%
try  {
    InitialContext ctx = new InitialContext();
    ISignSessionHome home = home = (ISignSessionHome) PortableRemoteObject.narrow(
            ctx.lookup("RSASignSession"), ISignSessionHome.class );
    ISignSession ss = home.create();
    Certificate[] chain = ss.getCertificateChain();
    if (chain.length == 0) {
        out.println("No CA certificates exist");
    } else {
        out.println("<li><a href=\"/webdist/certdist?cmd=nscacert&level=0\">Root CA</a></li>");
        if (chain.length > 1) {
            for (int i=chain.length-2;i>=0;i--) {
                out.println("<li><a href=\"/webdist/certdist?cmd=nscacert&level="+i+"\">CA</a></li>");
            }
        }
    }
} catch(Exception ex) {
    ex.printStackTrace();
}                                             
%>
<HR>
<FORM ACTION="/apply/certreq" ENCTYPE=x-www-form-encoded METHOD="POST">

Please give your username and password, then click OK to fetch your certificate.<BR>

Username
	<INPUT NAME=user TYPE=text SIZE=10 VALUE="foo"><br>
Password
	<INPUT NAME=password TYPE=text SIZE=10 VALUE="foo123"><br>
Key length
	<KEYGEN TYPE="hidden" NAME="keygen" VALUE="challenge">

<INPUT type="submit" value="OK">

</FORM>
</BODY>
</HTML>
