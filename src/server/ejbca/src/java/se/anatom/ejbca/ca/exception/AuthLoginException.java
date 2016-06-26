package se.anatom.ejbca.ca.exception;

import se.anatom.ejbca.exception.EjbcaException;

/**
 * Authentication error due to wrong credentials of user object.
 * To authenticate a user the user must have valid credentials, i.e. password.
 *
 * @version $Id: AuthLoginException.java,v 1.1 2006/06/09 17:09:06 danijel Exp $
 */
public class AuthLoginException extends EjbcaException {

   /**
    * Constructor used to create exception with an errormessage.
    * Calls the same constructor in baseclass <code>Exception</code>.
    *
    * @param message Human redable error message, can not be NULL.
    */
   public AuthLoginException(String message) {
       super(message);
   }
}
