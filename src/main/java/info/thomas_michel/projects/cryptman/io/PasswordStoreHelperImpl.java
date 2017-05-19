/* $Id:$ */
/******************************************************************************
 |PasswordStoreHelperImpl.java|  -  TODO description

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.tools.ant.BuildException;

/**
 * @author tmichel
 *
 */
public class PasswordStoreHelperImpl implements IPasswordStoreHelper {

    public static final String STRING_COLON = ":";

    public static final char CHAR_COLON = ':';

    public static final char CHAR_CR = '\n';

    public static final char CHAR_COMMA = ',';

    protected static final Logger logger = Logger.getLogger(PasswordStoreHelperImpl.class
            .getName());

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IPasswordStoreHelper#readMasterPassword(java.lang.String)
     */
    public char[] readMasterPassword(final String keyFilePath) {
        final File keyFile = new File(keyFilePath);
        if (!keyFile.exists()) {
            throw new BuildException(
                    "Datei mit MasterPasswort nicht gefunden: " + keyFilePath);
        }
        char[] schluessel = new char[(int) keyFile.length()];
        try {
            final FileReader reader = new FileReader(keyFile);
            reader.read(schluessel);
            reader.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage());
        }
        return schluessel;

    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IPasswordStoreHelper#readPassword(java.lang.String, java.lang.String, java.lang.String)
     */
    public  byte[] readPassword(final String passwordFilePath,
            final String hostName, final String userName) throws IOException {

        // Array zur Rückgabe des Passwortes
        byte[] password = null;

        File passwordFile = null;
        FileReader reader = null;
        BufferedReader bufReader = null;
        try {
            passwordFile = new File(passwordFilePath);
            if (!passwordFile.exists()) {
                passwordFile.createNewFile();
            }
            reader = new FileReader(passwordFile);
            bufReader = new BufferedReader(reader);

            boolean weiter = true;
            String curHostName = null;
            String curUserName = null;
            // suchen des Passworteintrages
            while (weiter) {
                final String line = bufReader.readLine();
                // Abbruch am Ende
                if (line == null) {
                    weiter = false;
                    continue;
                }
                // Aktuelle Zeile auswerten
                final StringTokenizer tok = new StringTokenizer(line,
                        STRING_COLON, false);
                // HostName einlesen
                if (tok.hasMoreTokens()) {
                    curHostName = tok.nextToken();
                } else {
                    continue;
                }
                // UserName einlesen
                if (tok.hasMoreTokens()) {
                    curUserName = tok.nextToken();
                } else {
                    continue;
                }

                // Zugangsdaten gefunden?
                final boolean matchedEntry = userName.equals(curUserName)
                        && curHostName.equals(hostName);
                if (!matchedEntry) {
                    continue;
                }
                // Bei Übereinstimmung von HostName und UserName
                // Passwort einlesen
                if (tok.hasMoreTokens()) {
                    final String passwordParts = tok.nextToken();
                    password = convertDigitsToByteArray(passwordParts);
                } else {
                    continue;
                }
                // Nicht leeres Password gefunden?
                if (password != null && password.length > 0) {
                    weiter = false;
                }
            }// end while

            // Falls kein Passwort oder leeres Passwort gefunden, Array auf null setzen
            if (password == null || password.length < 1) {
                password = null;
            }

        } finally {
            if (bufReader != null) {
                bufReader.close();
            } else if (reader != null) {
                reader.close();
            }

        }// end finally

        return password;
    }

    /* (non-Javadoc)
     * @see info.thomas_michel.projects.cryptman.io.IPasswordStoreHelper#writePassword(java.lang.String, java.lang.String, java.lang.String, byte[])
     */
    public void writePassword(final String passwordFilePath,
            final String hostName, final String userName,
            final byte[] encryptedPassword) throws IOException {

        final File datei = new File(passwordFilePath);
        final String password = convertByteArrayToDigitList(encryptedPassword);

        final FileOutputStream outStream = new FileOutputStream(datei, true);
        final Writer writer = new OutputStreamWriter(outStream);
        writer.write(CHAR_CR);
        writer.write(CHAR_COLON);
        writer.write(hostName);
        writer.write(CHAR_COLON);
        writer.write(userName);
        writer.write(CHAR_COLON);
        writer.write(password);
        writer.write(CHAR_COLON);
        writer.flush();
        writer.close();
    }

    /**
     * Setzt eine komma getrennte Liste von Ziffern zu einem Byte Array zusammen.
     * Jede Ziffer wird zu einem Byte im Array.
     * 
     * @param digitList kommaseparierte Liste von Ziffern
     * @return byte[] Array von Bytes welche den Ziffern entsprechen
     */
    public static byte[] convertDigitsToByteArray(final String digitList) {
        return convertDigitsToByteArray(digitList, Character
                .toString(CHAR_COMMA));
    }

    /**
     * Setzt eine Separator getrennte Liste von Ziffern zu einem Byte Array zusammen.
     * Jede Ziffer wird zu einem Byte im Array.
     * Die zum separieren der Ziffern verwendeten Zeichen können als Parameter
     * übergeben werden.
     * 
     * @param digitList kommaseparierte Liste von Ziffern
     * @param separatorList Liste der Separator Zeichen (wie beim Tokenizer)
     * @return byte[] Array von Bytes welche den Ziffern entsprechen
     */
    public static byte[] convertDigitsToByteArray(final String digitList,
            final String separatorList) {
        final StringTokenizer tok = new StringTokenizer(digitList,
                separatorList);
        final byte[] text = new byte[tok.countTokens()];
        int i = 0;
        while (tok.hasMoreTokens()) {
            final String value = tok.nextToken();
            final int intValue = Integer.parseInt(value);
            text[i++] = (byte) intValue;
        }
        return text;
    }

    /**
     * Konvertiert ein ByteArray in eine Ziffernfolge
     * 
     * @param text Zu konvertierendes Array
     * @return Kommagetrennte Ziffernfolge 
     */
    public static String convertByteArrayToDigitList(final byte[] text) {
        return convertByteArrayToDigitList(text, CHAR_COMMA);
    }

    /**
     * Konvertiert ein ByteArray in eine Ziffernfolge. 
     * 
     * @param text Zu konvertierendes Array
     * @param separator Separator Zeichen zum Trennen der Ziffern.
     * @return Separator getrennte Ziffernfolge
     */
    public static String convertByteArrayToDigitList(final byte[] text,
            final char separator) {
        final StringBuffer digitList = new StringBuffer(4 * text.length);
        for (int i = 0; i < text.length; i++) {
            if (i > 0) {
                digitList.append(separator);
            }
            digitList.append((int) text[i]);
        }
        return digitList.toString();
    }

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
