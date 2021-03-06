/*
 * LICENSE AND COPYRIGHT INFORMATION:
 *
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is OpenIPMP.
 *
 * The Initial Developer of the Original Code is Mutable, Inc.
 * Portions created by Mutable, Inc. are
 * Copyright (C) Mutable, Inc. 2002-2006.  All Rights Reserved.
 * 
 *
 */


 /**
  * <p>Title: </p>
  * <p>Description: </p>
  * @version 1.0
  */
package com.mutable.osms.server.db.bean;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

import java.io.Serializable;
import java.util.Date;

public class  osmsDvDoiTitleBean  implements Serializable
{
	//-------------------------------------
	// properties
	//-------------------------------------

	//int TITLE_ID
	private Integer m_TitleId;
	public Integer getTitleId ()
	{return m_TitleId;}
	public void setTitleId (Integer pTitleId)
	{m_TitleId = pTitleId;}

	//int SEQUENCE
	private Integer m_Sequence;
	public Integer getSequence ()
	{return m_Sequence;}
	public void setSequence (Integer pSequence)
	{m_Sequence = pSequence;}

	//varchar TITLE_TEXT
	private String m_TitleText;
	public String getTitleText ()
	{return m_TitleText;}
	public void setTitleText (String pTitleText)
	{m_TitleText = pTitleText;}

	//varchar LANGUAGE
	private String m_Language;
	public String getLanguage ()
	{return m_Language;}
	public void setLanguage (String pLanguage)
	{m_Language = pLanguage;}

	//char MAINTENANCE_FLAG
	private String m_MaintenanceFlag;
	public String getMaintenanceFlag ()
	{return m_MaintenanceFlag;}
	public void setMaintenanceFlag (String pMaintenanceFlag)
	{m_MaintenanceFlag = pMaintenanceFlag;}

	//varchar LAST_UPDATED_BY
	private String m_LastUpdatedBy;
	public String getLastUpdatedBy ()
	{return m_LastUpdatedBy;}
	public void setLastUpdatedBy (String pLastUpdatedBy)
	{m_LastUpdatedBy = pLastUpdatedBy;}

	//datetime LAST_UPDATED_ON
	private Timestamp m_LastUpdatedOn;
	public Timestamp getLastUpdatedOn ()
	{return m_LastUpdatedOn;}
	public void setLastUpdatedOn (Timestamp pLastUpdatedOn)
	{m_LastUpdatedOn = pLastUpdatedOn;}

	//varchar CREATED_BY
	private String m_CreatedBy;
	public String getCreatedBy ()
	{return m_CreatedBy;}
	public void setCreatedBy (String pCreatedBy)
	{m_CreatedBy = pCreatedBy;}

	//datetime CREATED_ON
	private Timestamp m_CreatedOn;
	public Timestamp getCreatedOn ()
	{return m_CreatedOn;}
	public void setCreatedOn (Timestamp pCreatedOn)
	{m_CreatedOn = pCreatedOn;}


	//-------------------------------------
	// copyPropertiesTo method
	//-------------------------------------
	public void copyPropertiesTo (osmsDvDoiTitleBean target)
	{

		target.setTitleId (getTitleId());
		target.setSequence (getSequence());
		target.setTitleText (getTitleText());
		target.setLanguage (getLanguage());
		target.setMaintenanceFlag (getMaintenanceFlag());
		target.setLastUpdatedBy (getLastUpdatedBy());
		target.setLastUpdatedOn (getLastUpdatedOn());
		target.setCreatedBy (getCreatedBy());
		target.setCreatedOn (getCreatedOn());
	}


/*
	//-------------------------------------
	// equals method
	//-------------------------------------
	public boolean equals (Object pObj)
	{
    	if (!(pObj instanceof osmsDvDoiTitleBean)) return false;
	    osmsDvDoiTitleBean obj = (osmsDvDoiTitleBean) pObj;


		if (this.getTitleId () != obj.getTitleId () &&
			(this.getTitleId () == null ||
			obj.getTitleId () == null ||
			this.getTitleId ().equals (obj.getTitleId ()))) return false;

		if (this.getSequence () != obj.getSequence () &&
			(this.getSequence () == null ||
			obj.getSequence () == null ||
			this.getSequence ().equals (obj.getSequence ()))) return false;

		if (this.getTitleText () != obj.getTitleText () &&
			(this.getTitleText () == null ||
			obj.getTitleText () == null ||
			this.getTitleText ().equals (obj.getTitleText ()))) return false;

		if (this.getLanguage () != obj.getLanguage () &&
			(this.getLanguage () == null ||
			obj.getLanguage () == null ||
			this.getLanguage ().equals (obj.getLanguage ()))) return false;

		if (this.getMaintenanceFlag () != obj.getMaintenanceFlag () &&
			(this.getMaintenanceFlag () == null ||
			obj.getMaintenanceFlag () == null ||
			this.getMaintenanceFlag ().equals (obj.getMaintenanceFlag ()))) return false;

		if (this.getLastUpdatedBy () != obj.getLastUpdatedBy () &&
			(this.getLastUpdatedBy () == null ||
			obj.getLastUpdatedBy () == null ||
			this.getLastUpdatedBy ().equals (obj.getLastUpdatedBy ()))) return false;

		if (this.getLastUpdatedOn () != obj.getLastUpdatedOn () &&
			(this.getLastUpdatedOn () == null ||
			obj.getLastUpdatedOn () == null ||
			this.getLastUpdatedOn ().equals (obj.getLastUpdatedOn ()))) return false;

		if (this.getCreatedBy () != obj.getCreatedBy () &&
			(this.getCreatedBy () == null ||
			obj.getCreatedBy () == null ||
			this.getCreatedBy ().equals (obj.getCreatedBy ()))) return false;

		if (this.getCreatedOn () != obj.getCreatedOn () &&
			(this.getCreatedOn () == null ||
			obj.getCreatedOn () == null ||
			this.getCreatedOn ().equals (obj.getCreatedOn ()))) return false;

	 	return true;
	}
*/	


	//-------------------------------------
	// print method
	//-------------------------------------
	public void print()
	{

		System.out.println("TitleId = "+this.getTitleId());
		System.out.println("Sequence = "+this.getSequence());
		System.out.println("TitleText = "+this.getTitleText());
		System.out.println("Language = "+this.getLanguage());
		System.out.println("MaintenanceFlag = "+this.getMaintenanceFlag());
		System.out.println("LastUpdatedBy = "+this.getLastUpdatedBy());
		System.out.println("LastUpdatedOn = "+this.getLastUpdatedOn());
		System.out.println("CreatedBy = "+this.getCreatedBy());
		System.out.println("CreatedOn = "+this.getCreatedOn());
	}
	protected Time ConvertObject2Time(Object obj)
	{
		return (Time)obj;
	}

	
}
