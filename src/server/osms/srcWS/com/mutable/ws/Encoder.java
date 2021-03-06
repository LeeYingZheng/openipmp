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

package com.mutable.ws;

import com.mutable.osms.server.app.osmsEncoderManager;
import com.mutable.osms.server.app.osmsGlobalObjectManager;

public class Encoder {
	public int  encode(String username, String password, String inFile, String outFile){
		osmsEncoderManager encoder = osmsGlobalObjectManager.getInstance().getEncoderManager();
		try {
			encoder.encode(username, password, inFile, outFile, null, null, null, null);
			return 0;
		} catch (Exception e) {
			return 1;
		}
	}
}
