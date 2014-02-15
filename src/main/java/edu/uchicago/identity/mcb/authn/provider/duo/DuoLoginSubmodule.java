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

import com.duosecurity.DuoWeb;
import edu.internet2.middleware.assurance.mcb.authn.provider.MCBLoginServlet;
import edu.internet2.middleware.assurance.mcb.authn.provider.MCBSubmodule;
import edu.internet2.middleware.assurance.mcb.authn.provider.MCBUsernamePrincipal;
import edu.internet2.middleware.shibboleth.idp.authn.AuthenticationException;
import edu.internet2.middleware.shibboleth.idp.authn.LoginHandler;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import org.opensaml.xml.util.DatatypeHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This sub-module handles DUO authentication for the Multi-Context Broker
 * 
 * @author David Langenberg
 */
public class DuoLoginSubmodule implements MCBSubmodule{

	private final Logger log = LoggerFactory.getLogger(DuoLoginSubmodule.class);
	
	private String beanName = null;
	
	private String aKey;
	private String iKey;
	private String sKey;
	private String host;
	private String loginPage;
	
	/**
	 * Constructor
	 * @param aKey aKey from duo
	 * @param iKey iKey from duo
	 * @param sKey sKey from duo
	 * @param host host from duo
	 * @param loginPage velocity template containing DUO page
	 */
	public DuoLoginSubmodule(String aKey, String iKey, String sKey, String host, String loginPage){
		this.aKey = aKey;
		this.iKey = iKey;
		this.sKey = sKey;
		this.host = host;
		this.loginPage = loginPage;
		
		log.debug("Config: akey: {}, iKey: {}, sKey: {}, host: {}, login page: {}",aKey,iKey,sKey != null ? "XXXXXXXXXX" : sKey,host, loginPage);
	}
	
	/**
	 * Display the Duo login screen
	 * 
	 * @param servlet
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 * @throws LoginException 
	 */
	public boolean displayLogin(MCBLoginServlet servlet, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, LoginException {
		//this module must be invoked after a principal has already been established
		MCBUsernamePrincipal principal = (MCBUsernamePrincipal) request.getSession().getAttribute(LoginHandler.PRINCIPAL_KEY);
		log.debug("principal name is: {}", principal.getName());
		if(principal == null || principal.getName() == null || principal.getName().equals("") || principal.getName().equals("[principal]")){
			log.error("The DuoLoginSubmodule may not be invoked unless the user already has authenticated using another method.  No user principal detected.");
			return false;
		}
		
		log.debug("creating signed Duo request for principal: {}", principal);
		
		String req = DuoWeb.signRequest(iKey, sKey, aKey, principal.getName());
		log.debug("Duo request: {}", req);
		
		VelocityContext vCtx = new VelocityContext();
		vCtx.put("duoRequest", req);
		vCtx.put("duoHost",host);
		
		log.debug("Displaying Velocity Duo template [{}]",loginPage);
		servlet.doVelocity(request, response, loginPage, vCtx);
		
		return true;
	}

	/**
	 * Process the response from the Login Screen
	 * @param servlet
	 * @param request
	 * @param response
	 * @return
	 * @throws AuthenticationException
	 * @throws LoginException 
	 */
	public boolean processLogin(MCBLoginServlet servlet, HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, LoginException {
		String sig_response = DatatypeHelper.safeTrimOrNullString(request.getParameter("sig_response"));
		log.debug("Signed response from Duo is: {}", sig_response);
		
		String result = DuoWeb.verifyResponse(iKey, sKey, aKey, sig_response);
		log.debug("Result of the verification of the response from Duo is: {}",result);
		
		MCBUsernamePrincipal principal = (MCBUsernamePrincipal) request.getSession().getAttribute(LoginHandler.PRINCIPAL_KEY);
		
		if(result.equalsIgnoreCase(principal.getName())){
			return true;
		}else {
			principal.setFailedLogin("unable to verify Duo");
			return false;
		}
	}

	public void init() {
		log.info("Duo Login Submodule version {} initialized", getClass().getPackage().getImplementationVersion());
	}

	public String getBeanName() {
		return beanName;
	}

	public void setBeanName(String string) {
		beanName = string;
	}
	
}
