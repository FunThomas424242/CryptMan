/* $Id:$ */
/******************************************************************************
 |ICipherFactory.java|  -  TODO description

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
package info.thomas_michel.projects.cryptman;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

/**
 * @author tmichel
 *
 */
public interface ICipherFactory {

    public Cipher getCipher(final int mode, final char[] passphrase)
            throws InvalidKeySpecException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException;

    /**
     * Erzeugt eine Chipher mit welcher je nach gewählten Mode entweder
     * ver- oder entschlüsselt werden kann.
     * 
     * @param mode Cipher.ENCRYPT_MODE oder Cipher.DECRYPT_MODE
     * @param passphrase Passwort zur Verschlüsselung (muss in ASCII vorliegen)
     *  gegenbenenfalls über CryptMan.convertPassphraseToASCII wandeln.
     * @param salt Ein initialer Zufallswert
     * @param iterationCount Ein Iterationszähler für den Algorithmus
     * @return Cipher zum Ver- bzw. Entschlüsseln
     * 
     * @throws InvalidKeySpecException z.B. wenn das Passwort nicht ASCII ist
     * @throws NoSuchAlgorithmException z.B. wenn der Alg. nicht unterstützt wird
     * @throws NoSuchPaddingException 
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     */
    public Cipher getCipher(final int mode, final char[] passphraseRaw,
            final byte[] salt, final int iterationCount)
            throws InvalidKeySpecException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException;

}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
