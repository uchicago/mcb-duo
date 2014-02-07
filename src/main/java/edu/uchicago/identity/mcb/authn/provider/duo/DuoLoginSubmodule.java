/*
 * Copyright 2014 The University of Chicago.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package edu.uchicago.identity.mcb.authn.provider.duo;

import edu.internet2.middleware.assurance.mcb.authn.provider.MCBLoginServlet;
import edu.internet2.middleware.assurance.mcb.authn.provider.MCBSubmodule;
import edu.internet2.middleware.shibboleth.idp.authn.AuthenticationException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author David Langenberg
 */
public class DuoLoginSubmodule implements MCBSubmodule{

	
	
	public boolean displayLogin(MCBLoginServlet servlet, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, LoginException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public boolean processLogin(MCBLoginServlet servlet, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, LoginException {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void init() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public String getBeanName() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	public void setBeanName(String string) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
}
