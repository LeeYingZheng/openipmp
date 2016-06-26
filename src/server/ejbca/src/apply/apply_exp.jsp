<%@ page language="Java" import="javax.naming.*,javax.rmi.*,java.util.*,java.security.cert.*,se.anatom.ejbca.ca.sign.*"%>

<HTML>
<HEAD>
<TITLE>EJBCA IE certificate enroll</TITLE>

 <object
   classid="clsid:43F8F289-7A20-11D0-8F06-00C04FC295E1"
   id="encoder">
 </object>
<SCRIPT LANGUAGE=VBSCRIPT>
<!--
   Function GetProviderList()

   Dim CspList, cspIndex, ProviderName
   On Error Resume Next

   count = 0
   base = 0
   enhanced = 0
   CspList = ""
   ProviderName = ""

   For ProvType = 0 to 13
      cspIndex = 0
      encoder.ProviderType = ProvType
      ProviderName = encoder.enumProviders(cspIndex,0)

      while ProviderName <> ""
         Set oOption = document.createElement("OPTION")
         oOption.text = ProviderName
         oOption.value = ProvType
         Document.CertReqForm.CspProvider.add(oOption)
         if ProviderName = "Microsoft Base Cryptographic Provider v1.0" Then
            base = count
         end if
         if ProviderName = "Microsoft Enhanced Cryptographic Provider v1.0" Then
            enhanced = count
         end if
         cspIndex = cspIndex +1
         ProviderName = ""
         ProviderName = encoder.enumProviders(cspIndex,0)
         count = count + 1
      wend
   Next
   Document.CertReqForm.CspProvider.selectedIndex = base
   if enhanced then
      Document.CertReqForm.CspProvider.selectedIndex = enhanced
   end if
   End Function
-->
</SCRIPT>
</HEAD>
<BODY onLoad="GetProviderList()" bgcolor="#ffffff" link="black" vlink="black" alink="black">
<center>
<FONT face=arial size="3"><strong>EJBCA IE Certificate Enrollment
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
        out.println("<li><a href=\"/webdist/certdist?cmd=iecacert&level=0\">Root CA</a></li>");
        if (chain.length > 1) {
            for (int i=chain.length-2;i>=0;i--) {
                out.println("<li><a href=\"/webdist/certdist?cmd=iecacert&level="+i+"\">CA</a></li>");
            }
        }
    }
} catch(Exception ex) {
    ex.printStackTrace();
}                                             
%>
<HR>
<FORM NAME="CertReqForm" ACTION="/apply/certreq" ENCTYPE=x-www-form-encoded METHOD=POST>
 Please give your username and password, then click OK to fetch your certificate.<BR>

        Username: <input type=text size=10 name=user value="foo"><br>
        Password: <input type=text size=10 name=password value="foo123"><br>

    <P>Please choose the CSP you wish to use from the list below (the default is probably good):</P>
    <SELECT NAME="CspProvider">
    </SELECT></P>

    <INPUT TYPE="hidden" NAME="pkcs10" VALUE="">

<INPUT type="button" value="OK" name="GenReq">

</FORM>

<SCRIPT LANGUAGE=VBS>
    Function CSR(keyflags)
       CSR = ""
       szName          = "CN=6AEK347fw8vWE424"
       encoder.HashAlgorithm = "MD5"
       err.clear
       On Error Resume Next
       set options = document.all.CspProvider.options
       index = options.selectedIndex
       encoder.providerName = options(index).text
       tmpProviderType = options(index).value
       encoder.providerType = tmpProviderType
       encoder.KeySpec = 2
       if tmpProviderType < 2 Then
          encoder.KeySpec = 1
       end if
       encoder.GenKeyFlags = &h04000001 OR keyflags
       CSR = encoder.createPKCS10(szName, "")
       if len(CSR)<>0 then Exit Function
       encoder.GenKeyFlags = &h04000000 OR keyflags
       CSR = encoder.createPKCS10(szName, "")
       if len(CSR)<>0 then Exit Function
       if encoder.providerName = "Microsoft Enhanced Cryptographic Provider v1.0" Then
          if MsgBox("1024-bit key generation failed. Would you like to try 512 instead?", vbOkCancel)=vbOk Then
             encoder.providerName = "Microsoft Base Cryptographic Provider v1.0"
          else
             Exit Function
          end if
       end if
       encoder.GenKeyFlags = 1 OR keyflags
       CSR = encoder.createPKCS10(szName, "")
       if len(CSR)<>0 then Exit Function
       encoder.GenKeyFlags = keyflags
       CSR = encoder.createPKCS10(szName, "")
       if len(CSR)<>0 then Exit Function
       encoder.GenKeyFlags = 0
       CSR = encoder.createPKCS10(szName, "")
    End Function

    Sub GenReq_OnClick
       Dim TheForm
       Set TheForm = Document.CertReqForm
       err.clear
       result = CSR(2)
       if len(result)=0 Then
          result = MsgBox("Unable to generate PKCS#10 certificate request.", 0, "Alert")
          Exit Sub
       end if
       TheForm.pkcs10.Value = result
       TheForm.Submit
       Exit Sub
    End Sub
</SCRIPT>

</BODY>
</HTML>
