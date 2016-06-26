
package se.anatom.ejbca.ca.auth;

import java.rmi.*;

import javax.naming.*;
import javax.rmi.*;
import javax.ejb.*;

import se.anatom.ejbca.BaseSessionBean;
import se.anatom.ejbca.util.CertTools;
import se.anatom.ejbca.SecConst;

/**
 * Approves all authentication requests that contain a DN as the username, password is ignored and 
 * the username is returned as DN.
 * Useful for demo purposes to give out certificates to anyone.
 *
 * @version $Id: NullAuthenticationSessionBean.java,v 1.1 2006/06/09 17:09:06 danijel Exp $
 */
public class NullAuthenticationSessionBean extends BaseSessionBean implements IAuthenticationSession {

    /**
     * Default create for SessionBean without any creation Arguments.
     * @throws CreateException if bean instance can't be created
     */
    public void ejbCreate () throws CreateException {
        debug(">ejbCreate()");
        debug("<ejbCreate()");
    }

   /**
    * Implements IAuthenticationSession::authenticateUser.
    * Implements a mechanism that does no real authentication. Returns the username as DN is the username
    * contains a DN. Only returns entities of type USER_ENDUSER.
    * STATUS_NEW, STATUS_FAILED or STATUS_INPROCESS.
    */
    public UserAuthData authenticateUser(String username, String password) throws RemoteException {
        debug(">authenticateUser("+username+", hiddenpwd)");
        try {
            // Does the username contain a DN?
            String dn = CertTools.stringToBCDNString(username);
            if (dn != null) {
                String email = CertTools.getPartFromDN(dn, "EmailAddress");
                info("NULL-Authenticated user with DN='"+dn+"' and email='"+email+"'.");
                UserAuthData ret = new UserAuthData(username, dn, email, SecConst.USER_ENDUSER);
                debug("<authenticateUser("+username+", hiddenpwd)");
                return ret;
            } else {
                error("User "+username+" does not contain a DN.");
                throw new SecurityException("User "+username+" does not contain a DN.");
            }
        } catch (Exception e) {
            throw new EJBException(e.toString());
        }
    } //authenticateUser

   /**
    * Implements IAuthenticationSession::finishUser.
    * Does nothing...
    */
    public void finishUser(String username, String password) throws RemoteException {
        debug(">finishUser("+username+", hiddenpwd)");
        debug("<finishUser("+username+", hiddenpwd)");
    } //finishUser
}
