/*
 * $Id: QuickStartSession.java 458029 2005-11-08 15:55:17Z ehillenius $
 * $Revision: 458029 $ $Date: 2005-11-08 16:55:17 +0100 (Tue, 08 Nov 2005) $
 * 
 * ==============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package sk.stuba.fiit.ztpPortal.core;

import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebSession;

public final class CoreSession extends WebSession {

	public CoreSession(Request request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	
	private static final long serialVersionUID = 1L;
	private String loged;
	private String password;

	public synchronized String getLoged() {
		return loged;
	}

	public synchronized void setLoged(String loged) {
		this.loged = loged;
	}

	public synchronized String getPassword() {
		return password;
	}

	public synchronized void setPassword(String password) {
		this.password = password;
	}
	
	public static CoreSession get() {
		return (CoreSession) Session.get();
	}

}
