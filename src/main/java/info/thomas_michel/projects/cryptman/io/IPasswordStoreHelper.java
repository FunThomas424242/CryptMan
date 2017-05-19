/* $Id:$ */
/******************************************************************************
 |IPasswordStoreHelper.java|  -  TODO description

 begin                : |10.01.2007|
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
package info.thomas_michel.projects.cryptman.io;

import java.io.IOException;

/**
 * @author tmichel
 *
 */
public interface IPasswordStoreHelper {

    /**
     * Liest das Masterpasswort aus der Datei mit �bergebenem Pfad.
     * 
     * @param keyFilePath Pfad zur Datei mit Masterpasswort
     * @return Masterpasswort im Klartext
     */
    public abstract char[] readMasterPassword(final String keyFilePath);

    /**
     * Diese Methode sucht in einer Passwortdatei die Zugangsdaten f�r einen
     * User an einem Host. Wird die geforderte Host/User Kombination gefunden,
     * wird das zugeh�rige verschl�sselte Passwort ausgelesen und als ByteArray
     * zur�ckgegeben.
     * 
     * @param passwordFilePath Pfad zur Passwortdatei
     * @param hostName Name des Rechners f�r Login
     * @param userName Loginname des Zugangs
     * 
     * @return gefundenes Passwort (verschl�sselt) oder null falls nicht gefunden
     * 
     * @throws IOException bei fehlerhaften Dateizugriffen
     */
    public abstract byte[] readPassword(final String passwordFilePath,
            final String hostName, final String userName) throws IOException;

    /**
     * Schreibt einen neuen Datensatz mit Zugangsdaten in eine Passwortdatei.
     * Der Datensatz wird am Ende der Datei angef�gt.
     * 
     * @param passwordFilePath Pfad zur Passwortdatei
     * @param hostName Rechnername f�r Login
     * @param userName Loginname f�r Zugang
     * @param encryptedPassword Passwort (verschl�sselt) f�r Zugang
     * 
     * @throws IOException Fehler beim Zugriff auf Passwortdatei
     */
    public abstract void writePassword(final String passwordFilePath,
            final String hostName, final String userName,
            final byte[] encryptedPassword) throws IOException;

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
