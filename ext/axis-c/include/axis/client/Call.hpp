/*
 *   Copyright 2003-2004 The Apache Software Foundation.
// (c) Copyright IBM Corp. 2004, 2005 All Rights Reserved
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

 /**
 * @file Call.h
 *
 * This file Contains the Call class and equivalent C function tables
 * that all web service stubs generated by WSDL2Ws tool use to talk
 * to Axis Engine.
 *
 * @author Susantha Kumara (susantha@opensource.lk, skumara@virtusa.com)
 * @author Sanjaya Singharage (sanjayas@opensource.lk)
 * @author Samisa Abeysinghe (sabeysinghe@virtusa.com)
 */
 
/*
 * Revision 1.1  2004/05/24 samisa
 * Added accesser to transport 
 */
 
/*
 * Revision 1.2  2004/05/25 samisa
 * Added accesser to SOAP serializer 
 */

/*
 * Revision 1.3  2004/05/31 samisa
 * Added setProxy
 */

/* Call.h: interface for the Call class.*/


#if !defined(_CALL_H____OF_AXIS_INCLUDED_)
#define _CALL_H____OF_AXIS_INCLUDED_

#include "../GDefine.hpp"
#include "../TypeMapping.hpp"
#include "../AxisUserAPI.hpp"
#include "../WSDDDefines.hpp"
#include "../IHeaderBlock.hpp"

AXIS_CPP_NAMESPACE_START

class ClientAxisEngine;
class SOAPTransport;
class MessageData;
class SoapDeSerializer;
class SoapSerializer;

class STORAGE_CLASS_INFO CallBase
{
public:
    virtual void AXISCALL setSOAPVersion(SOAP_VERSION version)=0;
    virtual int AXISCALL setTransportProperty(AXIS_TRANSPORT_INFORMATION_TYPE
        type, const char* value)=0;
	virtual int AXISCALL setHandlerProperty(AxisChar* name, 
		void* value, int len)=0;
    virtual int AXISCALL setProtocol(AXIS_PROTOCOL_TYPE protocol)=0;
    virtual int AXISCALL initialize(PROVIDERTYPE nStyle)=0;
    virtual int AXISCALL invoke()=0;
    virtual int AXISCALL unInitialize()=0;

    /* Method that set the remote method name */
    virtual void AXISCALL setOperation(const char* pchOperation,
        const char* pchNamespace)=0;
    virtual int AXISCALL setEndpointURI(const char* pchEndpointURI)=0;

    /* Method for adding complex parameters */
    virtual void AXISCALL addCmplxParameter(void* pObject, void* pSZFunct,
        void* pDelFunct, const AxisChar* pName, const AxisChar* pNamespace)=0;
    /* Method for adding complex type array parameters */
    virtual void AXISCALL addCmplxArrayParameter(Axis_Array* pArray,
        void* pSZFunct, void* pDelFunct, void* pSizeFunct,
        const AxisChar* pName, const AxisChar* pNamespace)=0;
    /* Method for adding basic type array parameters */
    virtual void AXISCALL addBasicArrayParameter(Axis_Array* pArray,
        XSDTYPE nType, const char* pName)=0;
    /* Method for adding parameters of basic types */
    virtual void AXISCALL addParameter(void* pValue,const char* pName,
        XSDTYPE nType)=0;

    /* Methods used by stubs to get a deserialized value of an XML element
     * as basic types
     */
    virtual xsd__int * AXISCALL getElementAsInt(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__boolean * AXISCALL getElementAsBoolean(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedInt * AXISCALL getElementAsUnsignedInt(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__short * AXISCALL getElementAsShort(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedShort * AXISCALL getElementAsUnsignedShort(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__byte * AXISCALL getElementAsByte(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedByte * AXISCALL getElementAsUnsignedByte(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__long * AXISCALL getElementAsLong(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__integer * AXISCALL getElementAsInteger(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedLong * AXISCALL getElementAsUnsignedLong(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__float * AXISCALL getElementAsFloat(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__double * AXISCALL getElementAsDouble(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__decimal * AXISCALL getElementAsDecimal(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__string AXISCALL getElementAsString(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__anyURI AXISCALL getElementAsAnyURI(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__QName AXISCALL getElementAsQName(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__hexBinary * AXISCALL getElementAsHexBinary(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__base64Binary * AXISCALL getElementAsBase64Binary(const
        AxisChar* pName, const AxisChar* pNamespace)=0;
    virtual xsd__dateTime * AXISCALL getElementAsDateTime(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__date * AXISCALL getElementAsDate(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__time * AXISCALL getElementAsTime(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__duration * AXISCALL getElementAsDuration(const AxisChar* pName,
        const AxisChar* pNamespace)=0;

    /* Methods used by stubs to get a deserialized value of XML attribute
     * as basic types
     */
    virtual xsd__int * AXISCALL getAttributeAsInt(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__boolean * AXISCALL getAttributeAsBoolean(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedInt * AXISCALL getAttributeAsUnsignedInt(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__short * AXISCALL getAttributeAsShort(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedShort * AXISCALL getAttributeAsUnsignedShort(const
        AxisChar* pName, const AxisChar* pNamespace)=0;
    virtual xsd__byte * AXISCALL getAttributeAsByte(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedByte * AXISCALL getAttributeAsUnsignedByte(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__long * AXISCALL getAttributeAsLong(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__integer * AXISCALL getAttributeAsInteger(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__unsignedLong * AXISCALL getAttributeAsUnsignedLong(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__float * AXISCALL getAttributeAsFloat(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__double * AXISCALL getAttributeAsDouble(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__decimal * AXISCALL getAttributeAsDecimal(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__string AXISCALL getAttributeAsString(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__anyURI AXISCALL getAttributeAsAnyURI(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__QName AXISCALL getAttributeAsQName(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__hexBinary * AXISCALL getAttributeAsHexBinary(const AxisChar*
        pName, const AxisChar* pNamespace)=0;
    virtual xsd__base64Binary * AXISCALL getAttributeAsBase64Binary(const
        AxisChar* pName, const AxisChar* pNamespace)=0;
    virtual xsd__dateTime * AXISCALL getAttributeAsDateTime(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__date * AXISCALL getAttributeAsDate(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__time * AXISCALL getAttributeAsTime(const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    virtual xsd__duration * AXISCALL getAttributeAsDuration(const AxisChar* pName,
        const AxisChar* pNamespace)=0;

    /* Method used by stubs to get a deserialized value of complex types */
    virtual void* AXISCALL getCmplxObject(void* pDZFunct, void* pCreFunct,
        void* pDelFunct, const AxisChar* pName, const AxisChar* pNamespace)=0;
    /* Method used by stubs to get a deserialized Array of complex types */
    virtual Axis_Array AXISCALL getCmplxArray(void* pDZFunct, void* pCreFunct,
        void* pDelFunct, void* pSizeFunct, const AxisChar* pName,
        const AxisChar* pNamespace)=0;
    /* Method used by stubs to get a deserialized Array of basic types */
    virtual Axis_Array AXISCALL getBasicArray(XSDTYPE nType,
        const AxisChar* pName, const AxisChar* pNamespace)=0;

    virtual int AXISCALL checkMessage(const AxisChar* pName,
        const AxisChar* pNamespace)=0;

    virtual void* AXISCALL checkFault(const AxisChar* pName,
        const AxisChar* pNamespace)=0;

    /* Minimal error check */
    virtual int AXISCALL getStatus()=0;

    virtual AnyType* AXISCALL getAnyObject()=0;
    virtual int AXISCALL addAnyObject(AnyType* pAnyObject)=0;
    virtual const AxisChar* AXISCALL getNamespacePrefix
        (const AxisChar* pNamespace)=0;

    virtual IHeaderBlock* AXISCALL createHeaderBlock(AxisChar *pachLocalName,
        AxisChar *pachUri)=0;

    virtual IHeaderBlock* AXISCALL createHeaderBlock(AxisChar *pachLocalName,
        AxisChar *pachUri, AxisChar *pachPrefix)=0;

    static bool bInitialized;
};

/* A separate call class object should be used by each thread */
class STORAGE_CLASS_INFO Call : public CallBase
{
public:
    Call();
    virtual ~Call();
    void AXISCALL setSOAPVersion(SOAP_VERSION version);
    int AXISCALL setTransportProperty(AXIS_TRANSPORT_INFORMATION_TYPE type,
        const char* value);
	const char* AXISCALL getTransportProperty(const char *key, bool response=true);
	int AXISCALL setHandlerProperty(AxisChar* name, void* value, int len);
	/**
	 * set the protocol that the underlying transport will use. 
	 * If there is not transport set then the transport protocol is stored locally until there is a transport.
	 * 
	 * @param protocol the protocol that you want. Allowed values are  defined in GDefine.hpp AXIS_PROTOCOL_TYPE
	 * @return AXIS_SUCCESS if the protocol was set correctly in the underlying transport or, if there is no transport then the value was stored safely.
	 */
    int AXISCALL setProtocol(AXIS_PROTOCOL_TYPE protocol);
    /**
     * Get the protocol that the transport is or will use.
     * @return the transport protocol being used.
     */
    AXIS_PROTOCOL_TYPE AXISCALL getProtocol();
    int AXISCALL unInitialize();
    int AXISCALL initialize(PROVIDERTYPE nStyle);
    int AXISCALL invoke();

    /**
     * Sets an Attribute to the SOAPMethod, using the given Attribute data.
     * You must ensure the prefix has a valid namespace declared, otherwise an
     * invalid SOAP message will be produced.
     * It is safer to use setSOAPMethodAttribute(const AxisChar *pLocalname, const AxisChar *pPrefix, const AxisChar* pUri, const AxisChar *pValue)
     * 
     * @param pLocalname The local name of the Attribute.
     * @param pPrefix The prefix of the Attribute.
     * @param pValue The value of the Attribute.
     */
    void setSOAPMethodAttribute(const AxisChar *pLocalname, const AxisChar *pPrefix, const AxisChar *pValue);

    /**
     * Sets an Attribute to the SOAPMethod, using the given Attribute data.
     *
     * @param pLocalname The local name of the Attribute.
     * @param pPrefix The prefix of the Attribute.
     * @param pUri The namespace uri of the Attribute.
     * @param pValue The value of the Attribute.
     */
    void setSOAPMethodAttribute(const AxisChar *pLocalname, const AxisChar *pPrefix, const AxisChar* pUri, const AxisChar *pValue);

    /* Method for adding complex parameters */
    void AXISCALL addCmplxParameter(void* pObject, void* pSZFunct,
        void* pDelFunct, const AxisChar* pName, const AxisChar* pNamespace);
    /* Method for adding complex type array parameters */
    void AXISCALL addCmplxArrayParameter(Axis_Array* pArray, void* pSZFunct,
        void* pDelFunct, void* pSizeFunct, const AxisChar* pName,
        const AxisChar* pNamespace);
    /* Method for adding basic type array parameters */
    void AXISCALL addBasicArrayParameter(Axis_Array* pArray, XSDTYPE nType,
        const AxisChar* pName);
    /* Method for adding parameters of basic types */
    void AXISCALL addParameter(void* pValue,const char* pchName,
        XSDTYPE nType);

    /* Method that set the remote method name */
    void AXISCALL setOperation(const char* pchOperation,
        const char* pchNamespace);
    int AXISCALL setEndpointURI(const char* pchEndpointURI);
public:
    IHeaderBlock* AXISCALL createHeaderBlock(AxisChar *pachLocalName,
        AxisChar *pachUri);
    IHeaderBlock* AXISCALL createHeaderBlock(AxisChar *pachLocalName,
        AxisChar *pachUri, AxisChar *pachPrefix);
    IHeaderBlock* createHeaderBlock();
    /* Methods used by stubs to get a deserialized value of XML element
     * as basic types
     */
    xsd__int * AXISCALL getElementAsInt(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__boolean * AXISCALL getElementAsBoolean(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedInt * AXISCALL getElementAsUnsignedInt(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__short * AXISCALL getElementAsShort(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedShort * AXISCALL getElementAsUnsignedShort(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__byte * AXISCALL getElementAsByte(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedByte * AXISCALL getElementAsUnsignedByte(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__long * AXISCALL getElementAsLong(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__integer * AXISCALL getElementAsInteger(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedLong * AXISCALL getElementAsUnsignedLong(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__float * AXISCALL getElementAsFloat(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__double * AXISCALL getElementAsDouble(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__decimal * AXISCALL getElementAsDecimal(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__string AXISCALL getElementAsString(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__anyURI AXISCALL getElementAsAnyURI(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__QName AXISCALL getElementAsQName(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__hexBinary * AXISCALL getElementAsHexBinary(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__base64Binary * AXISCALL getElementAsBase64Binary(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__dateTime * AXISCALL getElementAsDateTime(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__date * AXISCALL getElementAsDate(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__time * AXISCALL getElementAsTime(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__duration * AXISCALL getElementAsDuration(const AxisChar* pName,
        const AxisChar* pNamespace);

    /* Methods used by stubs to get a deserialized value of a XML attribute
     * as basic types
     */
    xsd__int * AXISCALL getAttributeAsInt(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__boolean * AXISCALL getAttributeAsBoolean(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedInt * AXISCALL getAttributeAsUnsignedInt(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__short * AXISCALL getAttributeAsShort(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedShort * AXISCALL getAttributeAsUnsignedShort(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__byte * AXISCALL getAttributeAsByte(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedByte * AXISCALL getAttributeAsUnsignedByte(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__long * AXISCALL getAttributeAsLong(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__integer * AXISCALL getAttributeAsInteger(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__unsignedLong * AXISCALL getAttributeAsUnsignedLong(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__float * AXISCALL getAttributeAsFloat(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__double * AXISCALL getAttributeAsDouble(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__decimal * AXISCALL getAttributeAsDecimal(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__string AXISCALL getAttributeAsString(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__anyURI AXISCALL getAttributeAsAnyURI(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__QName AXISCALL getAttributeAsQName(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__hexBinary * AXISCALL getAttributeAsHexBinary(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__base64Binary * AXISCALL getAttributeAsBase64Binary(const AxisChar*
        pName, const AxisChar* pNamespace);
    xsd__dateTime * AXISCALL getAttributeAsDateTime(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__date * AXISCALL getAttributeAsDate(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__time * AXISCALL getAttributeAsTime(const AxisChar* pName,
        const AxisChar* pNamespace);
    xsd__duration * AXISCALL getAttributeAsDuration(const AxisChar* pName,
        const AxisChar* pNamespace);

    /* Method used by stubs to get a deserialized value of complex types */
    void* AXISCALL getCmplxObject(void* pDZFunct, void* pCreFunct,
        void* pDelFunct, const AxisChar* pName, const AxisChar* pNamespace);
    /* Method used by stubs to get a deserialized Array of complex types */
    Axis_Array AXISCALL getCmplxArray(void* pDZFunct, void* pCreFunct,
        void* pDelFunct, void* pSizeFunct, const AxisChar* pName,
        const AxisChar* pNamespace);
    /* Method used by stubs to get a deserialized Array of basic types */
    Axis_Array AXISCALL getBasicArray(XSDTYPE nType, const AxisChar* pName,
        const AxisChar* pNamespace);

    int AXISCALL checkMessage(const AxisChar* pName,
        const AxisChar* pNamespace);

    void* AXISCALL checkFault(const AxisChar* pName,
        const AxisChar* pNamespace);

    int AXISCALL getStatus();

    SOAPTransport* getTransport() { return m_pTransport; }
    SoapSerializer* getSOAPSerializer() { return (SoapSerializer*)m_pIWSSZ; }

  /**
    * Set proxy server and port for transport.
    *
    * @param pcProxyHost Host name of proxy server
    * @param uiProxyPort Port of proxy server
    */
    void setProxy(const char* pcProxyHost, unsigned int uiProxyPort);

    AnyType* AXISCALL getAnyObject();
    int AXISCALL addAnyObject(AnyType* pAnyObject);

	/**
	 * Returns the prefix for a previously defined namespace. If the 
	 * namespace has not previously been associated with a prefix, it
	 * creates a new prefix, which is unique and returns that. It will
	 * only return prefixes for user-defined namespaces, so passing a 
	 * standard namespace will cause a new prefix to be created.
	 * 
	 * @param pNamespace the namespace to look for
	 * @return the prefix for this namespace
	 */
    const AxisChar* AXISCALL getNamespacePrefix(const AxisChar* pNamespace);
	
	/**
	 * Returns a complex fault as an XML string 
	 */
	const xsd__string getFaultAsXMLString();

private:
    int openConnection();
    void closeConnection();
    int makeArray();

private:
    ClientAxisEngine* m_pAxisEngine;

#ifdef WIN32
  #pragma warning (disable : 4251)
#endif
	list<void*> m_handlerProperties;

#ifdef WIN32
  #pragma warning (default : 4251)
#endif

    /*
       Following are pointers to relevant objects of the ClientAxisEngine
       instance. So they do not belong to this object and are not created
       or deleted
     */
    SoapSerializer* m_pIWSSZ;
    SoapDeSerializer* m_pIWSDZ;
    char* m_pcEndPointUri;
    AXIS_PROTOCOL_TYPE m_nTransportType;
    /*
       Transport object
     */
    SOAPTransport* m_pTransport;

    /* Minimal error check */
    int m_nStatus;
  /**
    * Proxy server name.
    */
    string m_strProxyHost;
  /**
    * Proxy server port.
    */
    unsigned int m_uiProxyPort;
  /**
    * Use Proxy or not?
    */
    bool m_bUseProxy;

  /**
    * To track if the initialize was called/matched with an uninitialize
    */
    bool m_bCallInitialized;
  
    // Samisa m_pchSessionID was misssing and there was a compile error due to this
    char* m_pchSessionID;

};
AXIS_CPP_NAMESPACE_END

#endif
