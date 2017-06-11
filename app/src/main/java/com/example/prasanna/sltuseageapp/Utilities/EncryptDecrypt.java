package com.example.prasanna.sltuseageapp.Utilities;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

/**
 * Created by prasanna on 6/11/17.
 */

public class EncryptDecrypt {
    private static final String key = "THISISASLTUSAGEMETER@43234";

    public static String decrypt(String value){
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(key);
        return decryptor.decrypt(value);
    }

    public static String encrypt(String value){
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(key);
            String encryptedPassword = encryptor.encrypt(value);
            return encryptedPassword;
    }
}
