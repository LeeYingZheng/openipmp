
package se.anatom.ejbca.ca.auth.junit;

import org.apache.log4j.*;
import junit.framework.*;

/**
 *
 * @version $Id: TestRunner.java,v 1.1 2006/06/09 17:09:06 danijel Exp $
 */
public class TestRunner extends Object {

    static Category cat = Category.getInstance( TestRunner.class.getName() );

    public static void main (String[] args) {
        BasicConfigurator.configure();
        junit.textui.TestRunner.run (suite());
    }

    private void cleanUp() {
        cat.debug(">cleanUp()");
        cat.debug("<cleanUp()");
    }

    public static Test suite ( ) {
        cat.debug(">suite()");

        TestSuite suite = new TestSuite();
        suite.addTest( new TestSuite( TestAuthenticationSession.class ));

        cat.debug("<suite()");
        return suite;
    }
}

