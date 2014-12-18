package com.visenze.visearch.internal.util;


import com.visenze.visearch.ViSearchException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for generating authentication tokens for ViSearch APIs.
 */
public class AuthGenerator {

    /**
     * Generates a map of parameters for ViSearch APIs authentication.
     *
     * @param accessKey the access key of the ViSearch App
     * @param secretKey the secret key of the ViSearch App
     * @return the map of authentication parameters
     */
    public static Map<String, String> getAuthParams(String accessKey, String secretKey) throws ViSearchException {
        Map<String, String> authParams = new HashMap<String, String>();
        String nonce = generateNonce();
        long date = System.currentTimeMillis() / 1000L;
        StringBuilder sigStr = new StringBuilder();
        sigStr.append(secretKey);
        sigStr.append(nonce);
        sigStr.append(date);
        authParams.put("access_key", accessKey);
        authParams.put("nonce", nonce);
        authParams.put("date", String.valueOf(date));
        try {
            authParams.put("sig", hmacEncode(sigStr.toString(), secretKey));
        } catch (Exception e) {
            throw new ViSearchException("Exception in getAuthParams", e);
        }
        return authParams;
    }

    private static String generateNonce() {
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        return new String(Hex.encodeHex(bytes));
    }

    private static String hmacEncode(String baseString, String key) throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException,
            UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes());
        return new String(Hex.encodeHex(digest));
    }
}
