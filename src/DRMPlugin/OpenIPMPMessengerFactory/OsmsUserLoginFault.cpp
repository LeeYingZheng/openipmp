/*
 * Copyright 2003-2004 The Apache Software Foundation.

 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file was auto-generated by the Axis C++ Web Service Generator (WSDL2Ws)
 * This file contains functions to manipulate complex type OsmsUserLoginFault
 */

#include "OsmsUserLoginFault.hpp"
#include <axis/AxisWrapperAPI.hpp>

/*
 * This static method serialize a OsmsUserLoginFault type of object
 */
int Axis_Serialize_OsmsUserLoginFault(OsmsUserLoginFault* param, IWrapperSoapSerializer* pSZ, bool bArray = false)
{
	if (bArray)
	{
		pSZ->serialize("<", Axis_TypeName_OsmsUserLoginFault, ">", NULL);
	}
	else
	{
		bool blnIsNewPrefix = false;
		const AxisChar* sPrefix = pSZ->getNamespacePrefix(Axis_URI_OsmsUserLoginFault, blnIsNewPrefix);
		if (!blnIsNewPrefix)
		{
			pSZ->serialize("<", Axis_TypeName_OsmsUserLoginFault, " xsi:type=\"", sPrefix, ":",
				Axis_TypeName_OsmsUserLoginFault, "\">", NULL);
		}
		else
		{
			pSZ->serialize("<", Axis_TypeName_OsmsUserLoginFault, " xsi:type=\"", sPrefix, ":",
				Axis_TypeName_OsmsUserLoginFault, "\" xmlns:", sPrefix, "=\"",
				Axis_URI_OsmsUserLoginFault, "\">", NULL);
		}
	}

	pSZ->serializeAsElement("errorCode", (void*)&(param->errorCode), XSD_INT);
	pSZ->serializeAsElement("responseString", (void*)(param->responseString), XSD_STRING);

	pSZ->serialize("</", Axis_TypeName_OsmsUserLoginFault, ">", NULL);
	return AXIS_SUCCESS;
}

/*
 * This static method deserialize a OsmsUserLoginFault type of object
 */
int Axis_DeSerialize_OsmsUserLoginFault(OsmsUserLoginFault* param, IWrapperSoapDeSerializer* pIWSDZ)
{
	xsd__int* p_errorCode = (pIWSDZ->getElementAsInt("errorCode",0));
	param->errorCode = *p_errorCode;
	delete p_errorCode;
	param->responseString = pIWSDZ->getElementAsString("responseString",0);
	return pIWSDZ->getStatus();
}
void* Axis_Create_OsmsUserLoginFault(OsmsUserLoginFault* pObj, bool bArray = false, int nSize=0)
{
	if (bArray && (nSize > 0))
	{
		if (pObj)
		{
			OsmsUserLoginFault* pNew = new OsmsUserLoginFault[nSize];
			memcpy(pNew, pObj, sizeof(OsmsUserLoginFault)*nSize/2);
			memset(pObj, 0, sizeof(OsmsUserLoginFault)*nSize/2);
			delete [] pObj;
			return pNew;
		}
		else
		{
			return new OsmsUserLoginFault[nSize];
		}
	}
	else
		return new OsmsUserLoginFault;
}

/*
 * This static method delete a OsmsUserLoginFault type of object
 */
void Axis_Delete_OsmsUserLoginFault(OsmsUserLoginFault* param, bool bArray = false, int nSize=0)
{
	if (bArray)
	{
		delete [] param;
	}
	else
	{
		delete param;
	}
}
/*
 * This static method gives the size of OsmsUserLoginFault type of object
 */
int Axis_GetSize_OsmsUserLoginFault()
{
	return sizeof(OsmsUserLoginFault);
}

OsmsUserLoginFault::OsmsUserLoginFault()
{
	/*do not allocate memory to any pointer members here
	 because deserializer will allocate memory anyway. */
	memset( &errorCode, 0, sizeof( xsd__int));
	memset( &responseString, 0, sizeof( xsd__string));
}

OsmsUserLoginFault::~OsmsUserLoginFault() throw ()
{
	/*delete any pointer and array members here*/
}
