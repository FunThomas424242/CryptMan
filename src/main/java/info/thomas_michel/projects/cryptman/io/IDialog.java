/* $Id:$ */
/******************************************************************************
 |IDialog.java|  -  TODO description

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
package info.thomas_michel.projects.cryptman.io;


/**
 * @author tmichel
 *
 */
public interface IDialog {

    /**
     * Wird vom Ant Task als Factory Methode genutzt.
     * 
     * @param hostName Name des RemoteRechners f�r Login
     * @param userName Loginname auf RemoteRechner f�r Login
     * @return PasswortEingabedialog
     */
    IDialog newDialog(final String hostName, final String userName);

    /**
     * @param string Text f�r Shell Prompt oder GUI Titel 
     */
    void setPrompt(String string);

    /**
     * @param string Name des Rechners
     */
    void setHostName(String string);

    /**
     * @param string Name des Nutzers
     */
    void setUserName(String string);

    /**
     * Anzeigen des Dialogs bzw. Aktivierung der Shell Eingabe
     */
    void show();

    /**
     * @return Abfrage des Flags f�r Nutzerabbruch -> true = Abbruch der Eingabe
     */
    boolean isUserCanceled();

    /**
     * @param true = Abbruch der Eingabe
     */
    void setUserCanceled(boolean canceled);

    /**
     * Nach Eingabe des Passortes und beim Schliessen des Dialogs
     * wird �ber diese Methode das Passwort �bergeben.
     * Bei einer Swing GUI wird diese Methode gew�hnlich vom OKButtonListener
     * aufgerufen, sobald der Dialog geschlossen werden soll.
     * 
     * @param password Passwort im Klartext
     */
    void setReadedPassword(final char[] password);

    /**
     * Gibt das im Dialog eingegebene Passwort zur�ck (auch wenn Dialog bereits geschlossen).
     * 
     * @param password Passwort im Klartext
     */
    public char[] getReadedPassword();

    /**
     * Schliessen des Dialogs.
     */
    void close();

    /**
     * Gibt das aktuell eingegebene Passwort zur�ck. Ist null falls noch keine
     * Eingabe erfolgte.
     * 
     * @return Wert aus dem PasswortField (nur solange Dialog nicht geschlossen)
     */
    char[] getPassword();

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
