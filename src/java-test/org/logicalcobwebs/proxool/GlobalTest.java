/*
 * This software is released under a licence similar to the Apache Software Licence.
 * See org.logicalcobwebs.proxool.package.html for details.
 * The latest version is available at http://proxool.sourceforge.net
 */
package org.logicalcobwebs.proxool;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.apache.log4j.xml.DOMConfigurator;
import org.logicalcobwebs.logging.Log;
import org.logicalcobwebs.logging.LogFactory;

/**
 * Provides a suite of all tests. And some utility methods for setting
 * up the logging.
 *
 * @version $Revision: 1.13 $, $Date: 2003/03/10 15:31:26 $
 * @author bill
 * @author $Author: billhorsman $ (current maintainer)
 * @since Proxool 0.5
 */
public class GlobalTest {

    private static final Log LOG = LogFactory.getLog(GlobalTest.class);

    private static boolean initialised;

    public static synchronized void globalSetup() throws ClassNotFoundException {
        if (!initialised) {
            String log4jPath = System.getProperty("log4jPath");
            if (log4jPath != null && log4jPath.length() > 0) {
                try {
                    DOMConfigurator.configureAndWatch(log4jPath);
                } catch (Exception e) {
                    LOG.debug("Can't configure logging using " + log4jPath);
                }
            }

            Class.forName(ProxoolDriver.class.getName());

            /* uncomment this if you want to turn on debug loggin to the console
            org.apache.log4j.BasicConfigurator.resetConfiguration();
            org.apache.log4j.BasicConfigurator.configure();
            */

            initialised = true;
        }
    }

    public static synchronized void globalTeardown(String alias) {
        ProxoolFacade.shutdown(alias + ":teardown", 500);
    }

    /**
     * Run all tests
     *
     * @return a composite test of all Proxool tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(org.logicalcobwebs.proxool.AllTests.suite());
        suite.addTest(org.logicalcobwebs.proxool.configuration.AllTests.suite());
        suite.addTest(org.logicalcobwebs.proxool.admin.AllTests.suite());
        suite.addTest(org.logicalcobwebs.proxool.util.AllTests.suite());

        // create a wrapper for global initialization code.
        TestSetup wrapper = new TestSetup(suite) {
            public void setUp() throws Exception {
                GlobalTest.globalSetup();
            }

            protected void tearDown() throws Exception {
                GlobalTest.globalTeardown("global");
            }
        };
        return wrapper;
    }
}


/*
 Revision history:
 $Log: GlobalTest.java,v $
 Revision 1.13  2003/03/10 15:31:26  billhorsman
 fixes

 Revision 1.12  2003/03/04 10:10:52  billhorsman
 loads ProxoolDriver

 Revision 1.11  2003/03/03 17:38:47  billhorsman
 leave shutdown to AbstractProxoolTest

 Revision 1.10  2003/03/03 11:12:04  billhorsman
 fixed licence

 Revision 1.9  2003/03/01 15:27:24  billhorsman
 checkstyle

 Revision 1.8  2003/02/19 23:36:50  billhorsman
 renamed monitor package to admin

 Revision 1.7  2003/02/10 00:14:33  chr32
 Added tests for AbstractListenerContainer.

 Revision 1.6  2003/02/07 15:10:11  billhorsman
 add admin tests

 Revision 1.5  2003/02/06 17:41:03  billhorsman
 now uses imported logging

 Revision 1.4  2003/01/31 16:38:05  billhorsman
 doc

 Revision 1.3  2002/12/18 03:15:03  chr32
 Added commented-out code that will make logging level DEBUG.

 Revision 1.2  2002/12/16 17:15:12  billhorsman
 fixes

 Revision 1.1  2002/12/16 17:05:25  billhorsman
 new test structure

 */