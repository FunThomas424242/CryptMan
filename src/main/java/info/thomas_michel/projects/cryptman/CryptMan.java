/* $Id:$ */
/******************************************************************************
 |CryptMan.java|  -  TODO description

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
package info.thomas_michel.projects.cryptman;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Diese Klasse stellt eine Helper Klasse zur Ver- und Entschlüsselung dar.
 * 
 * 
 * @author tmichel
 * 
 */
public class CryptMan {

    public static ICipherFactory getCipherFactory(final String algorithm){
        return new CipherFactoryImpl(algorithm);
    }
    
    public static ICipherFactory getCipherFactory(){
        return new CipherFactoryImpl();
    }
    
    public static byte[] encryptText(final Cipher encCipher,
            final byte[] clearText) throws IllegalStateException,
            IllegalBlockSizeException, BadPaddingException {

        final byte[] encryptedText = encCipher.doFinal(clearText);
        return encryptedText;
    }

    public static byte[] dencryptText(final Cipher decCipher,
            final byte[] encryptedText) throws IllegalStateException,
            IllegalBlockSizeException, BadPaddingException {

        final byte[] clearText = decCipher.doFinal(encryptedText);
        return clearText;
    }




}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
