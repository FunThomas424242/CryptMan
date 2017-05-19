/* $Id:$ */
/******************************************************************************
 |PasswordDialogImpl.java|  -  TODO description

 begin                : |07.01.2007|
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
package info.thomas_michel.projects.cryptman.gui;

import info.thomas_michel.projects.cryptman.io.IDialog;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * @author tmichel
 *
 */
public class PasswordDialogImpl implements IDialog {

    protected final Logger logger = Logger.getLogger(this.getClass().getName());

    protected boolean userCanceled = false;

    protected JDialog container = new JDialog();

    protected JPanel titleArea = new JPanel();

    protected JPanel dataArea = new JPanel();

    protected JLabel hostNameFieldLabel = new JLabel();

    protected JLabel userNameFieldLabel = new JLabel();

    protected JLabel passwordFieldLabel = new JLabel();

    protected JTextField hostNameField = new JTextField();

    protected JTextField userNameField = new JTextField();

    protected JPasswordField passwordField = new JPasswordField();

    protected JButton okButton = new JButton();

    protected JButton cancelButton = new JButton();

    /**
     * Vom User über den Dialog eingebenes Passwort.
     * Wird zur Weitergabe an den ANT Task hier gespeichert.
     */
    private char[] readedPassword;

    /**
     * Wird vom PasswordTask benötigt um über Reflection eine Instanz
     * des Dialoges erstellen zu können.
     * @see newDialog
     */
    public PasswordDialogImpl() {
        
    }

    /**
     * Wird von der Methode newDialog aufgerufen um eine neue Instanz
     * des Passworteingabedialoges zu erzeugen.
     * @see newDialog
     * 
     * @param antProject aktuelles Ant Projekt
     * @param passwordPropertyName Name des Ant Properties zur Rückgabe des Passwortes
     * @param encCipher Cipher zum Verschlüsseln
     * @param passwordFilePath Pfad zur Passwortdatei
     * @param hostName Name des Remote Rechners für Login
     * @param userName LoginName für Zugang
     */
    protected PasswordDialogImpl(final String hostName, final String userName) {
        this();
        hostNameField.setName(GUIConstants.FIELD_HOST_NAME);
        userNameField.setName(GUIConstants.FIELD_USER_NAME);
        passwordField.setName(GUIConstants.FIELD_PASSWORD);
        cancelButton.addActionListener(new CancelButtonListener(this));
        cancelButton.setText(GUIConstants.LABEL_CANCEL_BUTTON);
        cancelButton.setMnemonic(KeyEvent.VK_ESCAPE);
        cancelButton.setName(GUIConstants.BUTTON_CANCEL);
        okButton.addActionListener(new OkButtonListener(this));
        okButton.setText(GUIConstants.LABEL_OK_BUTTON);
        okButton.setMnemonic(KeyEvent.VK_ENTER);
        okButton.setName(GUIConstants.BUTTON_OK);
       
        try {
            String erscheinung = null;
            erscheinung = UIManager.getCrossPlatformLookAndFeelClassName();
            try {
                UIManager.setLookAndFeel(erscheinung);
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getLocalizedMessage());
            }

            container.setLocationRelativeTo(null);
            container
                    .setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            container.setResizable(false);
            container.setModal(true);
            titleArea.setLayout(new GridLayout(0, 1));
            dataArea.setLayout(new GridLayout(0, 1));
            hostNameFieldLabel.setText(GUIConstants.LABEL_HOSTNAME);
            userNameFieldLabel.setText(GUIConstants.LABEL_USERNAME);
            passwordFieldLabel.setText(GUIConstants.LABEL_PASSWORT);
            hostNameField.setEditable(false);
            hostNameField.setFocusable(false);
            userNameField.setEditable(false);
            userNameField.setFocusable(false);

            titleArea.add(hostNameFieldLabel);
            titleArea.add(userNameFieldLabel);
            titleArea.add(passwordFieldLabel);
            titleArea.add(cancelButton);
            dataArea.add(hostNameField);
            dataArea.add(userNameField);
            dataArea.add(passwordField);
            dataArea.add(okButton);

            container.getContentPane().add(titleArea, BorderLayout.WEST);
            container.getContentPane().add(dataArea, BorderLayout.CENTER);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#newDialog(org.apache.tools.ant.Project, java.lang.String, javax.crypto.Cipher, java.lang.String, java.lang.String, java.lang.String)
     */
    public IDialog newDialog(final String hostName, final String userName) {
        return new PasswordDialogImpl(hostName, userName);
    }

    /**
     * Setzt den Fenstertitel des Passworteingabedialogs
     */
    public void setPrompt(String prompt) {
        this.container.setTitle(prompt);
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#setzeServerDatenFeld(java.lang.String)
     */
    public void setHostName(String hostName) {
        this.hostNameField.setText(hostName);
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#setzeBenutzerDatenFeld(java.lang.String)
     */
    public void setUserName(String userName) {
        this.userNameField.setText(userName);
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#isUserCanceled()
     */
    public boolean isUserCanceled() {
        return userCanceled;
    }

    public void close() {
        this.container.dispose();
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#getPassword()
     */
    public char[] getPassword() {
        return this.passwordField.getPassword();
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#anzeigen()
     */
    public void show() {
        container.pack();
        container.setSize(400, 100);
        titleArea.setSize(100, 100);
        dataArea.setSize(300, 100);
        userNameFieldLabel.setSize(100, 30);
        hostNameField.setSize(100, 30);
        passwordFieldLabel.setSize(100, 30);
        userNameField.setSize(300, 30);
        hostNameField.setSize(300, 30);
        passwordField.setSize(300, 30);
        container.setVisible(true);
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IDialog#setUserCanceled(boolean)
     */
    public void setUserCanceled(boolean canceled) {
        this.userCanceled = canceled;
    }

    /**
     * @see info.thomas_michel.projects.cryptman.io.IDialog#getReadedPassword(char[] password)
     * 
     * @return Gibt das eingegebene Passwort zurück (auch wenn der Dialog schon geschlossen wurde)
     */
    public char[] getReadedPassword() {
        return readedPassword;
    }

    /**
     * @see info.thomas_michel.projects.cryptman.io.IDialog#setReadedPassword(char[] password)
     * 
     * @param password Setzt das eingegebene Passwort (auch wenn der Dialog schon geschlossen wurde)
     */
    public void setReadedPassword(char[] readedPassword) {
        this.readedPassword = readedPassword;
    }

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
