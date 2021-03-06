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
 * This file contains functions to manipulate complex type OsmsLicenseRequest
 */

#include "OsmsLicenseRequest.hpp"
#include <axis/AxisWrapperAPI.hpp>

/*
 * This static method serialize a OsmsLicenseRequest type of object
 */
int Axis_Serialize_OsmsLicenseRequest(OsmsLicenseRequest* param, IWrapperSoapSerializer* pSZ, bool bArray = false)
{
	if (bArray)
	{
		pSZ->serialize("<", Axis_TypeName_OsmsLicenseRequest, ">", NULL);
	}
	else
	{
		bool blnIsNewPrefix = false;
		const AxisChar* sPrefix = pSZ->getNamespacePrefix(Axis_URI_OsmsLicenseRequest, blnIsNewPrefix);
		if (!blnIsNewPrefix)
		{
			pSZ->serialize("<", Axis_TypeName_OsmsLicenseRequest, " xsi:type=\"", sPrefix, ":",
				Axis_TypeName_OsmsLicenseRequest, "\">", NULL);
		}
		else
		{
			pSZ->serialize("<", Axis_TypeName_OsmsLicenseRequest, " xsi:type=\"", sPrefix, ":",
				Axis_TypeName_OsmsLicenseRequest, "\" xmlns:", sPrefix, "=\"",
				Axis_URI_OsmsLicenseRequest, "\">", NULL);
		}
	}

	pSZ->serializeAsElement("contentId", (void*)(param->contentId), XSD_STRING);
	pSZ->serializeAsElement("rightsInfo", (void*)(param->rightsInfo), XSD_STRING);

	pSZ->serialize("</", Axis_TypeName_OsmsLicenseRequest, ">", NULL);
	return AXIS_SUCCESS;
}

/*
 * This static method deserialize a OsmsLicenseRequest type of object
 */
int Axis_DeSerialize_OsmsLicenseRequest(OsmsLicenseRequest* param, IWrapperSoapDeSerializer* pIWSDZ)
{
	param->contentId = pIWSDZ->getElementAsString("contentId",0);
	param->rightsInfo = pIWSDZ->getElementAsString("rightsInfo",0);
	return pIWSDZ->getStatus();
}
void* Axis_Create_OsmsLicenseRequest(OsmsLicenseRequest* pObj, bool bArray = false, int nSize=0)
{
	if (bArray && (nSize > 0))
	{
		if (pObj)
		{
			OsmsLicenseRequest* pNew = new OsmsLicenseRequest[nSize];
			memcpy(pNew, pObj, sizeof(OsmsLicenseRequest)*nSize/2);
			memset(pObj, 0, sizeof(OsmsLicenseRequest)*nSize/2);
			delete [] pObj;
			return pNew;
		}
		else
		{
			return new OsmsLicenseRequest[nSize];
		}
	}
	else
		return new OsmsLicenseRequest;
}

/*
 * This static method delete a OsmsLicenseRequest type of object
 */
void Axis_Delete_OsmsLicenseRequest(OsmsLicenseRequest* param, bool bArray = false, int nSize=0)
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
 * This static method gives the size of OsmsLicenseRequest type of object
 */
int Axis_GetSize_OsmsLicenseRequest()
{
	return sizeof(OsmsLicenseRequest);
}

OsmsLicenseRequest::OsmsLicenseRequest()
{
	/*do not allocate memory to any pointer members here
	 because deserializer will allocate memory anyway. */
	memset( &contentId, 0, sizeof( xsd__string));
	memset( &rightsInfo, 0, sizeof( xsd__string));
}

OsmsLicenseRequest::~OsmsLicenseRequest()
{
	/*delete any pointer and array members here*/
}
