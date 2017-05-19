/* $Id:$ */
/******************************************************************************
 |PasswordTaskTest.java|  -  TODO description

 begin                : |08.01.2007|
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
package info.thomas_michel.projects.cryptman.ant.test;

import info.thomas_michel.projects.cryptman.ant.PasswordTask;
import info.thomas_michel.projects.lib.async.ThreadWaiter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;

/**
 * @author tmichel
 *
 */
public class PasswordTaskTest extends TestCase {

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
     * Test method for 'info.thomas_michel.projects.cryptman.ant.PasswordTask.execute()'
     */
    public void testExecute1() {
        // Das Passwort ist:
        //>Password1<

        final PasswordTask task = new PasswordTask();
        // Default settings by ant
        final Project antProject = new Project();
        task.setProject(antProject);
        task.setTaskName("testExecute");
        task.setDescription("Beschreibung");
        // user settings for test
        task.setHostName("maximed");
        task.setUserName("tmichel");
        task.setMasterPasswordFilePath(masterPasswordFilePath);
        task.setPasswordFilePath(passwordFilePath);
        task.setPasswordPropertyName("password1");
        task.setPrompt("Test1");
        task.execute();

    }

    public void testExecute2() {
        // Das Passwort ist:
        //>So ein Unsinn<

        final PasswordTask task = new PasswordTask();
        // Default settings by ant
        final Project antProject = new Project();
        task.setProject(antProject);
        task.setTaskName("testExecute");
        task.setDescription("Beschreibung");
        // user settings for test
        task.setHostName("maximed2");
        task.setUserName("tmichel");
        task.setMasterPasswordFilePath(masterPasswordFilePath);
        task.setPasswordFilePath(passwordFilePath);
        task.setPasswordPropertyName("password2");
        task.setPrompt("Test2");
        task.execute();

    }

    public void testExecute3() {
        // Das Passwort ist:
        //>Das gibt aber Ärger bei so einem Übel<

        final PasswordTask task = new PasswordTask();
        // Default settings by ant
        final Project antProject = new Project();
        task.setProject(antProject);
        task.setTaskName("testExecute");
        task.setDescription("Beschreibung");
        // user settings for test
        task.setHostName("maximed3");
        task.setUserName("tmichel");
        task.setMasterPasswordFilePath(masterPasswordFilePath);
        task.setPasswordFilePath(passwordFilePath);
        task.setPasswordPropertyName("password3");
        task.setPrompt("Test3");

        final ThreadInputSequenceEingabe1 runable = new ThreadInputSequenceEingabe1();
        final Thread userSimulationThread = new Thread(runable);
        userSimulationThread.start();
        task.execute();
        ThreadWaiter.waitForSubThread(userSimulationThread);

    }

    public void testExecute4() {
        final PasswordTask task = new PasswordTask();
        // Default settings by ant
        final Project antProject = new Project();
        task.setProject(antProject);
        task.setTaskName("testExecute");
        task.setDescription("Beschreibung");
        // user settings for test
        final Date heute = new Date();

        task.setHostName("maximedttest" + heute.getTime());
        task.setUserName("tmichel");
        task.setMasterPasswordFilePath(masterPasswordFilePath);
        task.setPasswordFilePath(passwordFilePath);
        task.setPasswordPropertyName("password4");
        task.setPrompt("Test4");
        final ThreadInputSequenceEingabeLeerUndAbbruch runable = new ThreadInputSequenceEingabeLeerUndAbbruch();
        final Thread userSimulationThread = new Thread(runable);
        try {
            userSimulationThread.start();
            task.execute();
            ThreadWaiter.waitForSubThread(userSimulationThread);
            fail();
        } catch (BuildException e) {
            // soll nach Abbruch erreicht werden
            ThreadWaiter.waitForSubThread(userSimulationThread);
        }

    }

    /**
     * Methode dient zum Debuggen in Eclipse
     * @param args
     */
    public static void main(String args) {
        final PasswordTask task = new PasswordTask();
        // Default settings by ant
        final Project antProject = new Project();
        task.setProject(antProject);
        task.setTaskName("testExecute");
        task.setDescription("Beschreibung");
        // user settings for test
        final Date heute = new Date();

        task.setHostName("maximedttest" + heute.getTime());
        task.setUserName("tmichel");
        task.setMasterPasswordFilePath("src/main/resources/master.txt");
        task.setPasswordFilePath("src/main/resources/passwort.txt");
        task.setPasswordPropertyName("password_main");
        task.setPrompt("Bitte Passwörter eingeben");
        try {
            task.execute();
            fail();
        } catch (BuildException e) {
            // soll nach Abbruch erreicht werden
        }

    }

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
