/* $Id:$ */
/******************************************************************************
 |PasswordStoreHelperImplTest.java|  -  TODO description

 begin                : |09.01.2007|
 copyright            : (C) |2007| by |tmichel|
 email                : |development@thomas-michel.info|
 ******************************************************************************/
/******************************************************************************
 *                                                                            *
 *   This program is free software; you can redistribute it and/or modify     *
 *   it under the terms of the Lesser GNU General Public License as published *
 *   by the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                      *
 *                                                                            *
 ******************************************************************************/
package info.thomas_michel.projects.cryptman.io.test;

import info.thomas_michel.projects.cryptman.io.IPasswordStoreHelper;
import info.thomas_michel.projects.cryptman.io.PasswordStoreHelperImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

/**
 * @author tmichel
 *
 */
public class PasswordStoreHelperImplTest extends TestCase {

    final Properties properties = System.getProperties();

    String masterPasswordFilePath = "";

    String passwordFilePath = "";

    {
        init();
        masterPasswordFilePath = properties
                .getProperty("MasterPasswordFilePath");
        passwordFilePath = properties.getProperty("PasswordFilePath");
        final Logger logger = Logger.getLogger(this.getClass().getName());
        logger.log(Level.INFO, "masterFile:" + masterPasswordFilePath);
        logger.log(Level.INFO, "PasswortFile" + passwordFilePath);
        if (masterPasswordFilePath == null && passwordFilePath == null) {
            logger.log(Level.SEVERE,
                    "Bitte nur mit maven testen!> mvn -o clean test");
        }
    }

    private void init() {
        InputStream inStream = null;
        try {
            final File file = new File("target/test/properties.xml");
            inStream = new FileInputStream(file);
            properties.loadFromXML(inStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * Test method for 'info.thomas_michel.projects.cryptman.io.PasswordStoreHelperImpl.readMasterPassword(String)'
     */
    public void testReadMasterPassword() {
        final IPasswordStoreHelper passwordStoreHelper = new PasswordStoreHelperImpl();
        final char[] masterPassword = passwordStoreHelper
                .readMasterPassword(masterPasswordFilePath);
        final String passwordOfFile = new String(masterPassword);
        final String orgPassword = "Das ist das Masterpasswort";
        if (!orgPassword.equals(passwordOfFile)) {
            fail();
        }
    }

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
