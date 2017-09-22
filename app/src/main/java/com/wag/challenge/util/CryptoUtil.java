package com.wag.challenge.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by PGomez on 9/21/2017.
 */

public class CryptoUtil {

    public static String getMd5(String aString){
        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        m.update(aString.getBytes(),0,aString.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
