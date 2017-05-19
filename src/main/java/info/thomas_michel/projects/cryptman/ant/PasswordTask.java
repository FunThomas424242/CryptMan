/* $Id:$ */
/******************************************************************************
 |PasswordTask.java|  -  TODO description

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
package info.thomas_michel.projects.cryptman.ant;

import info.thomas_michel.projects.cryptman.CipherFactoryImpl;
import info.thomas_michel.projects.cryptman.CryptMan;
import info.thomas_michel.projects.cryptman.ICipherFactory;
import info.thomas_michel.projects.cryptman.gui.PasswordDialogImpl;
import info.thomas_michel.projects.cryptman.io.IDialog;
import info.thomas_michel.projects.cryptman.io.IPasswordStoreHelper;
import info.thomas_michel.projects.cryptman.io.PasswordStoreHelperImpl;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * @author tmichel
 *
 */
public class PasswordTask extends Task {

    /**
     * Eingabeprompt in der Shell bzw. Fenstertitel bei einer GUI
     */
    protected String prompt = "";

    protected String passwordPropertyName = null;

    /** 
     * Name des Hosts auf dem eingeloggt werden soll
     */
    protected String hostName = null;

    /**
     * Name des Nutzers der eingeloggt werden soll
     */
    protected String userName = null;

    /**
     * Pfad der Datei in welcher die Zugangsinformationen gespeichert werden:
     * server,user, password (verschlüsselt) 
     */
    protected String passwordFilePath = null;

    /**
     * Pfad der Datei in welcher das Masterpassword (passphrase) hinterlegt ist
     * mit der die Passwörter verschlüsselt werden.
     * (Diese Datei sollte an einem sicheren Ort liegen z.B.:
     * <ul>
     * <li> eigenes Profil unter Windows</li>
     * <li> Home Dir unter Unix</li>
     */
    protected String masterPasswordFilePath = null;

    /**
     * Name der Klasse welche den PassworteingabeDialog implementiert.
     */
    protected String passwordDialogClassName = PasswordDialogImpl.class
            .getName();

    /**
     * Name der Klasse welche den Zugriff auf den Passwortspeicher realisiert.
     * @see IPasswordStoreHelper 
     */
    protected String passwordStoreHelperClassName = PasswordStoreHelperImpl.class.getName();

    
    /**
     * Algorithm to encrypt the passwords
     */
    protected String encryptAlgorithm = CipherFactoryImpl.ALGORITHM_PBE_MD5_DES;
    
    
    
    
    /**
     * Konstruktor des Ant Task
     */
    public PasswordTask() {
        super();
    }

    /**
     * Hauptmethode welche aufgerufen wird wenn der Task läuft. 
     */
    public void execute() {
        final Project antProject = this.getProject();
        final String taskName = this.getTaskName();

        // Überprüfen der Property-Werte auf Gültigkeit
        if (this.hostName == null || this.userName == null
                || this.passwordFilePath == null
                || this.masterPasswordFilePath == null
                || this.passwordPropertyName == null) {
            // Beenden mit Fehlerkode
            throw new BuildException("Missing required parameters in task:"
                    + taskName + "\n passwordPropertyName="
                    + passwordPropertyName + "\n hostName=" + hostName
                    + "\n userName=" + userName + "\n passwordFilePath="
                    + passwordFilePath + "\n masterPasswordFilePath="
                    + masterPasswordFilePath);
        } else {
            try {
                final Class passwordStoreHelperClass = Class.forName(passwordStoreHelperClassName);
                final Object passwordStoreHelperObject = passwordStoreHelperClass.newInstance();
                final IPasswordStoreHelper passwordStoreHelper=(IPasswordStoreHelper)passwordStoreHelperObject;
                final char[] masterPassword = passwordStoreHelper
                        .readMasterPassword(this.masterPasswordFilePath);
                final ICipherFactory cipherFactory = CryptMan
                        .getCipherFactory(encryptAlgorithm);
                final Cipher encCipher = cipherFactory.getCipher(
                        Cipher.ENCRYPT_MODE, masterPassword);
                final Cipher decCipher = cipherFactory.getCipher(
                        Cipher.DECRYPT_MODE, masterPassword);
                // lesen Passwort aus Passwortdatei
                byte[] password = null;
                password = passwordStoreHelper.readPassword(this.passwordFilePath,
                        this.hostName, this.userName);

                // Passwort gefunden oder nicht?
                if (password != null) {
                    // Passwort gefunden -> Rückgabe als Property
                    final byte[] clearText = CryptMan.dencryptText(decCipher,
                            password);
                    antProject.setNewProperty(passwordPropertyName, new String(
                            clearText));
                } else {
                    // Passwort nicht gefunden -> Dialogeingabe + speichern
                    final Class dialogClass = Class
                            .forName(passwordDialogClassName);
                    final IDialog dialogObject = (IDialog) dialogClass
                            .newInstance();
                    final IDialog inputDialog = dialogObject.newDialog(
                            hostName, userName);
                    inputDialog.setPrompt(this.prompt);
                    inputDialog.setHostName(this.hostName);
                    inputDialog.setUserName(this.userName);
                    inputDialog.show();
                    if (inputDialog.isUserCanceled()) {
                        throw new BuildException(
                                "Skript Ausführung vom Nutzer abgebrochen, im Task: "
                                        + taskName);
                    }

                    final char[] readedPassword = inputDialog
                            .getReadedPassword();

                    // gültig?
                    if (readedPassword == null || readedPassword.length < 1) {
                        throw new BuildException("Ungültiges Passwort: "
                                + taskName);
                    }
                    final String passwordAsString = new String(readedPassword);
                    System.out.println("Passwort:" + passwordAsString);

                    try {
                        password = passwordAsString.getBytes();
                        final byte[] encryptedPassword = CryptMan.encryptText(
                                encCipher, password);
                        antProject.setNewProperty(passwordPropertyName,
                                passwordAsString);
                        passwordStoreHelper
                                .writePassword(this.passwordFilePath,
                                        this.hostName, this.userName,
                                        encryptedPassword);
                    } catch (IllegalStateException ex) {
                        throw new BuildException("Passphrase veraltet?"
                                + ex.getLocalizedMessage(), ex);
                    } catch (IllegalBlockSizeException ex) {
                        throw new BuildException("Passphrase veraltet?"
                                + ex.getLocalizedMessage(), ex);
                    } catch (BadPaddingException ex) {
                        throw new BuildException("Passphrase veraltet?"
                                + ex.getLocalizedMessage(), ex);
                    } catch (IOException ex) {
                        throw new BuildException("Passphrase veraltet?"
                                + ex.getLocalizedMessage(), ex);
                    }

                }
            } catch (IOException e) {
                throw new BuildException(e);
            } catch (InvalidKeyException e) {
                throw new BuildException("Passphrase veraltet?"
                        + e.getLocalizedMessage(), e);
            } catch (InvalidKeySpecException e) {
                throw new BuildException(
                        "Passphrase nicht ASCII oder veraltet?"
                                + e.getLocalizedMessage(), e);
            } catch (NoSuchAlgorithmException e) {
                throw new BuildException(e);
            } catch (NoSuchPaddingException e) {
                throw new BuildException(e);
            } catch (InvalidAlgorithmParameterException e) {
                throw new BuildException(e);
            } catch (IllegalStateException e) {
                throw new BuildException(e);
            } catch (IllegalBlockSizeException e) {
                throw new BuildException("Passphrase veraltet?"
                        + e.getLocalizedMessage(), e);
            } catch (BadPaddingException e) {
                throw new BuildException("Passphrase veraltet?"
                        + e.getLocalizedMessage(), e);
            } catch (ClassNotFoundException e) {
                throw new BuildException(
                        "Implementierungsklasse für den Passworteingabedialog nicht gefunden/kompatible: "
                                + e.getLocalizedMessage());
            } catch (InstantiationException e) {
                throw new BuildException(e);
            } catch (IllegalAccessException e) {
                throw new BuildException(e);
            }
        }
    }

    /**
     * @param hostName The hostName to set.
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @param masterPasswordFilePath The masterPasswordFilePath to set.
     */
    public void setMasterPasswordFilePath(String masterPasswordFilePath) {
        this.masterPasswordFilePath = masterPasswordFilePath;
    }

    /**
     * @param passwordFilePath The passwordFilePath to set.
     */
    public void setPasswordFilePath(String passwordFilePath) {
        this.passwordFilePath = passwordFilePath;
    }

    /**
     * @param passwordPropertyName The passwordPropertyName to set.
     */
    public void setPasswordPropertyName(String passwordPropertyName) {
        this.passwordPropertyName = passwordPropertyName;
    }

    /**
     * @param prompt The prompt to set.
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return Returns the passwordDialogClassName.
     */
    public String getPasswordDialogClassName() {
        return passwordDialogClassName;
    }

    /**
     * @param passwordDialogClassName The passwordDialogClassName to set.
     */
    public void setPasswordDialogClassName(String dialogImplClassName) {
        this.passwordDialogClassName = dialogImplClassName;
    }

    /**
     * @return Returns the encryptAlgorithm.
     */
    public String getEncryptAlgorithm() {
        return encryptAlgorithm;
    }

    /**
     * @param encryptAlgorithm The encryptAlgorithm to set.
     */
    public void setEncryptAlgorithm(String cryptoAlgorithm) {
        this.encryptAlgorithm = cryptoAlgorithm;
    }

    /**
     * @return Returns the passwordStoreHelperClassName.
     */
    public String getPasswordStoreHelperClassName() {
        return passwordStoreHelperClassName;
    }

    /**
     * @param passwordStoreHelperClassName The passwordStoreHelperClassName to set.
     */
    public void setPasswordStoreHelperClassName(String passwordStoreHelperClassName) {
        this.passwordStoreHelperClassName = passwordStoreHelperClassName;
    }
}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
