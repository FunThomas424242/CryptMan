/* $Id:$ */
/******************************************************************************
 |CipherFactoryImpl.java|  -  TODO description

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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

/**
 * Dient der Erzeugung von Cipher Instanzen. 
 * Die Namen der unterstützten Algorithmen sind:
 * <ul>
 *  <li>AES: Advanced Encryption Standard as specified by NIST in a draft FIPS. Based on the Rijndael algorithm by Joan Daemen and Vincent Rijmen, AES is a 128-bit block cipher supporting keys of 128, 192, and 256 bits.</li>
 *  <li>ARCFOUR/RC4: A stream cipher developed by Ron Rivest. For more information, see K. Kaukonen and R. Thayer, "A Stream Cipher Encryption Algorithm 'Arcfour'", Internet Draft (expired), draft-kaukonen-cipher-arcfour-03.txt.</li>
 *  <li>Blowfish: The block cipher designed by Bruce Schneier.</li>
 *  <li>DES: The Digital Encryption Standard as described in FIPS PUB 46-2.</li>
 *  <li>DESede: Triple DES Encryption (DES-EDE).</li>
 *  <li>ECIES (Elliptic Curve Integrated Encryption Scheme)</li>
 *  <li>RC2, RC4, and RC5: Variable-key-size encryption algorithms developed by Ron Rivest for RSA Data Security, Inc.</li>
 *  <li>RSA: The RSA encryption algorithm as defined in PKCS #1.</li> 
 *  <li>PBEWith<b>digest</b>And<b>encryption</b> or PBEWith<b>prf</b>And<b>encryption</b>
 *    Secret-key factory for use with PKCS #5 password-based encryption,
 *    where <br>
 *    <b>digest</b> is a message digest,<br>
 *    <b>prf</b> is a pseudo-random function, and <br>
 *    <b>encryption</b> is an encryption algorithm. 
 *    <br>
 *    Examples: <b>PBEWithMD5AndDES</b> (PKCS #5, v 1.5) and <b>PBEWithHmacSHA1AndDESede</b> (PKCS #5, v 2.0). <br/>
 *    Note: These both use only the low order 8 bits of each password character. 
 *    <br>
 *    <b>message digits</b>
 *    <ul>
 *     <li>MD2: The MD2 message digest algorithm as defined in RFC 1319.</li>
 *     <li>MD5: The MD5 message digest algorithm as defined in RFC 1321.</li>
 *     <li>SHA-1: The Secure Hash Algorithm, as defined in Secure Hash Standard, NIST FIPS 180-1.</li>
 *     <li>SHA-256, SHA-384, and SHA-512: New hash algorithms for which the draft Federal Information Processing Standard 180-2, Secure Hash Standard (SHS) is now available. SHA-256 is a 256-bit hash function intended to provide 128 bits of security against collision attacks, while SHA-512 is a 512-bit hash function intended to provide 256 bits of security. A 384-bit hash may be obtained by truncating the SHA-512 output.</li>
 *    </ul>
 *    <br>
 *    <b>encryption algorithms</b>
 *    <ul>
 *      <li>DES</li>
 *      <li>DESede</li>
 *      <li>HmacMD5: The HMAC-MD5 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).</li>
 *      <li>HmacSHA1: The HMAC-SHA1 keyed-hashing algorithm as defined in RFC 2104: "HMAC: Keyed-Hashing for Message Authentication" (February 1997).</li>
 *      <li>HmacSHA256: The HmacSHA256 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-256 as the message digest algorithm.</li>
 *      <li>HmacSHA384: The HmacSHA384 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-384 as the message digest algorithm.</li>
 *      <li>HmacSHA512: The HmacSHA512 algorithm as defined in RFC 2104 "HMAC: Keyed-Hashing for Message Authentication" (February 1997) with SHA-512 as the message digest algorithm.</li>
 *      <li>PBEWith<b>mac</b>: MAC for use with PKCS #5 v 2.0 password-based message authentication standard, where <b>mac</b> is a Message Authentication Code algorithm name. Example: PBEWithHmacSHA1</li>
 *      </ul>
 *   </li>
 * </ul>
 * 
 * @author tmichel
 *
 */
public class CipherFactoryImpl implements ICipherFactory {

    public static final String ALGORITHM_PBE_MD5_DES = "PBEWithMD5AndDES";
    public static final String ALGORITHM_PBE_HMAC_SHA1_DESEDE = "PBEWithHmacSHA1AndDESede";


    private static final String DEFAULT_ALGORITHM = ALGORITHM_PBE_MD5_DES;

    protected String algorithm = null;

    public CipherFactoryImpl() {
        this(DEFAULT_ALGORITHM);
    }

    /**
     * Der Konstruktor erwartet einen Verschlüsselungsalgorithmus. Dieser wird
     * zur Erzeugung der Chiphren benutzt.
     * 
     * @see getChipher
     * @see ALGORITHM_PBE_MD5_DES
     * 
     * @param algorithm Verschlüsselungsalgorithmus
     * 
     */
    public CipherFactoryImpl(final String algorithm) {
        super();
        if (algorithm != null) {
            this.algorithm = algorithm;
        } else {
            this.algorithm = DEFAULT_ALGORITHM;
        }
    }

    public Cipher getCipher(final int mode, final char[] passphrase)
            throws InvalidKeySpecException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            InvalidAlgorithmParameterException {
        // Salt
        final byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21,
                (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

        return getCipher(mode, passphrase, salt, 20);
    }

    /**
     * Erzeugt eine Chipher mit welcher je nach gewählten Mode entweder
     * ver- oder entschlüsselt werden kann.
     * 
     * @param mode Cipher.ENCRYPT_MODE oder Cipher.DECRYPT_MODE
     * @param passphrase Passwort zur Verschlüsselung (wird in ASCII konvertiert)
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
            InvalidAlgorithmParameterException {
        PBEKeySpec pbeKeySpec;
        PBEParameterSpec pbeParamSpec;
        SecretKeyFactory keyFac;

        // Create PBE parameter set
        pbeParamSpec = new PBEParameterSpec(salt, iterationCount);

        // Prompt user for encryption password.
        // Collect user password as char array (using the
        // "readPasswd" method from above), and convert
        // it into a SecretKey object, using a PBE key
        // factory.
        final char[] passphrase = CipherFactoryImpl
                .convertPassphraseToASCII(passphraseRaw);
        pbeKeySpec = new PBEKeySpec(passphrase);
        keyFac = SecretKeyFactory.getInstance(algorithm);
        SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);

        // Create PBE Cipher
        Cipher pbeCipher = Cipher.getInstance(algorithm);

        // Initialize PBE Cipher with key and parameters
        pbeCipher.init(mode, pbeKey, pbeParamSpec);

        return pbeCipher;
    }

    public static char[] convertPassphraseToASCII(final char[] textRaw) {
        if (textRaw == null) {
            return new char[0];
        }

        final StringBuffer buf = new StringBuffer(textRaw.length * 2);
        for (int i = 0; i < textRaw.length; i++) {
            final char zeichen = textRaw[i];

            switch (zeichen) {
            case 'ä': {
                buf.append("ae");
                break;
            }
            case 'ü': {
                buf.append("ue");
                break;
            }
            case 'ö': {
                buf.append("oe");
                break;
            }
            case 'Ä': {
                buf.append("AE");
                break;
            }
            case 'Ü': {
                buf.append("UE");
                break;
            }
            case 'Ö': {
                buf.append("OE");
                break;
            }
            case 'ß': {
                buf.append("sz");
                break;
            }
            default: {
                if ("§`´°\n\t\b".indexOf(zeichen) > -1 || zeichen < ' '
                        || zeichen > 'z') {
                    buf.append("?");
                    continue;
                } else {
                    buf.append(zeichen);
                }
                break;
            }

            }
        }
        final String textASCII = buf.toString();
        return textASCII.toCharArray();
    }
}

/**
 * ChangeLog
 * 
 * $Revision:$
 * 
 */
