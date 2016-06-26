package se.anatom.ejbca.ra.junit;

import java.util.Random;
import java.util.*;
import java.lang.Integer;

import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import java.rmi.RemoteException;

import se.anatom.ejbca.ra.*;
import se.anatom.ejbca.util.*;
import se.anatom.ejbca.SecConst;

import org.apache.log4j.*;
import junit.framework.*;


/** Tests the UserData entity bean and some parts of UserAdminSession.
 *
 * @version $Id: TestUserData.java,v 1.1 2006/06/09 17:09:07 danijel Exp $
 */
public class TestUserData extends TestCase {

    static Category cat = Category.getInstance( TestUserData.class.getName() );
    private static Context ctx;
    private static UserDataHome home;
    private static String username;
    private static String username1;
    private static String pwd;
    private static String pwd1;

    public TestUserData(String name) {
        super(name);
    }
    protected void setUp() throws Exception {
        cat.debug(">setUp()");
        ctx = getInitialContext();
        Object obj = ctx.lookup("UserData");
        home = (UserDataHome) javax.rmi.PortableRemoteObject.narrow(obj, UserDataHome.class);

        cat.debug("<setUp()");
    }
    protected void tearDown() throws Exception {
    }
    private Context getInitialContext() throws NamingException {
        cat.debug(">getInitialContext");
        Context ctx = new javax.naming.InitialContext();
        cat.debug("<getInitialContext");
        return ctx;
    }
    private String genRandomUserName() throws Exception {

        // Gen random user
        Random rand = new Random(new Date().getTime()+4711);

        String username = "";
        for (int i = 0; i < 6; i++) {
            int randint = rand.nextInt(9);
            username += (new Integer(randint)).toString();
        }
        cat.debug("Generated random username: username =" + username);
        return username;
    } // genRandomUserName
    private String genRandomPwd() throws Exception {

        // Gen random pwd
        Random rand = new Random(new Date().getTime()+4812);

        String password = "";
        for (int i = 0; i < 8; i++) {
            int randint = rand.nextInt(9);
            password += (new Integer(randint)).toString();
        }
        cat.debug("Generated random pwd: password=" + password);
        return password;
    } // genRandomPwd

    public void test01CreateNewUser() throws Exception {
        cat.debug(">test01CreateNewUser()");
        UserData data1=null;
        username = genRandomUserName();
        pwd = genRandomPwd();
        data1 = home.create(username, pwd, "C=SE, O=AnaTom, CN="+username);
        assertNotNull("Error creating", data1);
        cat.debug("created it!");
        cat.debug("<test01CreateNewUser()");
    }
    public void test02LookupAndChangeUser() throws Exception {
        cat.debug(">test02LookupAndChangeUser()");
        UserDataPK pk = new UserDataPK();
        pk.username = username;
        cat.debug("pk="+ pk);

        UserData data2 = home.findByPrimaryKey(pk);
        cat.debug("found by key! ="+ data2);
        cat.debug("username="+data2.getUsername());
        assertTrue( "wrong username", data2.getUsername().equals(username) );
        cat.debug("subject="+data2.getSubjectDN());
        assertTrue( "wrong DN", data2.getSubjectDN().indexOf(username) != -1);
        cat.debug("email="+data2.getSubjectEmail());
        assertNull( "wrong email", data2.getSubjectEmail());
        cat.debug("status="+data2.getStatus());
        assertTrue( "wrong status", data2.getStatus() == UserData.STATUS_NEW );
        cat.debug("type="+data2.getType());
        assertTrue( "wrong type", data2.getType() == SecConst.USER_INVALID);
        cat.debug("password foo123 returned " + data2.comparePassword("foo123"));
        assertTrue( "wrong pwd (foo123 works)", data2.comparePassword("foo123")==false);
        cat.debug("password "+pwd+" returned " + data2.comparePassword(pwd));
        assertTrue( "wrong pwd "+pwd, data2.comparePassword(pwd));

        data2.setStatus(UserData.STATUS_GENERATED);
        data2.setType(SecConst.USER_ENDUSER);
        data2.setPassword("foo123");
        data2.setSubjectEmail(username+"@anatom.se");
        cat.debug("Changed it");
        cat.debug("<test02LookupAndChangeUser()");
    }
    public void test03LookupChangedUser() throws Exception {
        cat.debug(">test03LookupChangedUser()");
        UserDataPK pk = new UserDataPK();
        pk.username = username;
        UserData data = home.findByPrimaryKey(pk);
        cat.debug("found by key! ="+ data);
        cat.debug("username="+data.getUsername());
        assertTrue( "wrong username", data.getUsername().equals(username) );
        cat.debug("subject="+data.getSubjectDN());
        assertTrue( "wrong DN", data.getSubjectDN().indexOf(username)!=-1 );
        cat.debug("email="+data.getSubjectEmail());
        assertNotNull("Email should not be null now.", data.getSubjectEmail());
        assertTrue( "wrong email", data.getSubjectEmail().equals(username+"@anatom.se"));
        cat.debug("status="+data.getStatus());
        assertTrue( "wrong status", data.getStatus() == UserData.STATUS_GENERATED );
        cat.debug("type="+data.getType());
        assertTrue( "wrong type", data.getType() == SecConst.USER_ENDUSER);
        cat.debug("password foo123 returned " + data.comparePassword("foo123"));
        assertTrue( "wrong pwd foo123", data.comparePassword("foo123"));
        cat.debug("password "+pwd+" returned " + data.comparePassword(pwd));
        assertTrue( "wrong pwd ("+pwd+" works)", data.comparePassword(pwd)==false);
        cat.debug("<test03LookupChangedUser()");
    }
    public void test04CreateNewUser() throws Exception {
        cat.debug(">test04CreateNewUser()");
        UserData data4=null;
        username1 = genRandomUserName();
        pwd1 = genRandomPwd();
        data4 = home.create(username1, pwd1, "C=SE, O=AnaTom, CN="+username);
        assertNotNull("Error creating", data4);
        cat.debug("created it again!");
        cat.debug("<test04CreateNewUser()");
    }
    public void test05ListNewUser() throws Exception {
        cat.debug(">test05ListNewUser()");
        Object obj1 = ctx.lookup("UserAdminSession");
        IUserAdminSessionHome adminhome = (IUserAdminSessionHome) javax.rmi.PortableRemoteObject.narrow(obj1, IUserAdminSessionHome.class);
        IUserAdminSession admin = adminhome.create();

        Collection coll = admin.findAllUsersByStatus(UserData.STATUS_NEW);
        Iterator iter = coll.iterator();
        while (iter.hasNext())
        {
            UserAdminData data = (UserAdminData)iter.next();
            cat.debug("New user: "+data.getUsername()+", "+data.getDN()+", "+data.getEmail()+", "+data.getStatus()+", "+data.getType());
            admin.setUserStatus(data.getUsername(), UserData.STATUS_GENERATED);
        }
        Collection coll1 = admin.findAllUsersByStatus(UserData.STATUS_NEW);
        assertTrue("found NEW users though there should be none!", coll1.isEmpty());
        cat.debug("<test05ListNewUser()");
    }
    public void test06RemoveUser() throws Exception {
        cat.debug(">test06RemoveUser()");
        UserDataPK pk = new UserDataPK();
        pk.username = username;
        home.remove(pk);
        pk.username = username1;
        home.remove(pk);
        cat.debug("Removed it!");
        cat.debug("<test06RemoveUser()");
    }


}
