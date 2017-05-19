/* $Id:$ */
/******************************************************************************
 |CryptManTest.java|  -  TODO description

 begin                : |06.01.2007|
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
package info.thomas_michel.projects.cryptman.test;

import info.thomas_michel.projects.cryptman.CryptMan;
import info.thomas_michel.projects.cryptman.ICipherFactory;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;

import junit.framework.TestCase;

/**
 * @author tmichel
 *
 */
public class CryptManTest extends TestCase {

    private static final String DEFAULT_CHARSET = "UTF-8";



    public void testCase1() {
        final String passphrase = "Password1";
        final String clearText = "test1";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase2() {
        final String passphrase = "Password1";
        final String clearText = "Test2";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase3() {
        final String passphrase = "Password1";
        final String clearText = "Test";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase4() {
        final String passphrase = "Password1";
        final String clearText = "Buble";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase5() {
        final String passphrase = "tes";
        final String clearText = "37Lumos8";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase6() {
        final String passphrase = "test";
        final String clearText = "password";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase7() {
        final String passphrase = "Password3483Password3439384738Password";
        final String clearText = "password";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase8() {
        final String passphrase = "Passwäord3483Passüöword3439384738Password";
        final String clearText = "passwäüo rd";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase9() {
        final String passphrase = "Passworm d3483Password3439384738Password";
        final String clearText = "passwo zßürd";
        checkWithPassphraseAndText(passphrase, clearText);
    }
    
    public void testCase10() {
        final String passphrase = "password ÜÄÖÜieß kdfuer8374&/()!\"§$%&&()=? d3483Password3439384738Password#'.,;:_-+*´`ß?}][{^^°~=";
        final String clearText = "passwo zßürd Das ist ein superlanger Text bis zum nächsten Mal\n werden wir mal sehen ob das alles so geht";
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase11() {
        final String passphrase = "password ÜÄÖÜieß kdfuer8374&/()!\"§$%&&()=? d3483Password3439384738Password#'.,;:_-+*´`ß?}][{^^°~=";
        final String clearText = "passwo zßürd Das ist ein superlanger Text bis zum nächsten Mal\n werden wir mal sehen ob das alles so geht"+
        passphrase;
        checkWithPassphraseAndText(passphrase, clearText);
    }

    public void testCase12() {
        final String passphrase = "password ÜÄÖÜieß kdfuer8374&/()!\"§$%&&()=? d3483Password3439384738Password#'.,;:_-+*´`ß?}][{^^°~=";
        final String clearText = passphrase;
        checkWithPassphraseAndText(passphrase, clearText);
    }

    
    /**
     * @param passphrase
     * @param clearText
     * @throws UnsupportedEncodingException 
     */
    private void checkWithPassphraseAndText(final String passphrase,
            final String clearText) {
        final char[] passphraseAsChars = passphrase.toCharArray();

        try {
            byte[] encryptedText = null;
            /*verschlüsseln*/{
                final ICipherFactory factory = CryptMan.getCipherFactory();
                final Cipher encCipher = factory.getCipher(
                        Cipher.ENCRYPT_MODE, passphraseAsChars);
                encryptedText = CryptMan.encryptText(encCipher, clearText
                        .getBytes(DEFAULT_CHARSET));
            }/**/
            /*entschlüsseln*/{
                final ICipherFactory factory = CryptMan.getCipherFactory();
                final Cipher decCipher = factory.getCipher(
                        Cipher.DECRYPT_MODE, passphraseAsChars);
                final byte[] clearTextAsBytes = CryptMan.encryptText(decCipher,
                        encryptedText);
                final String clearText2 = new String(clearTextAsBytes,
                        DEFAULT_CHARSET);
                assertEquals(clearText, clearText2);
            }/**/
        } catch (Exception e) {
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
