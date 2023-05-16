package se.ifmo.lab07.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityManager {
    private static final String PEPPER = "lkfjsflk";

    private static final String ALGORITHM = "sha-384";

    public static String generateHash(String password, String salt) throws NoSuchAlgorithmException {
        var md = MessageDigest.getInstance(ALGORITHM);
        var bytes = md.digest((PEPPER + password + salt).getBytes());
        var bi = new BigInteger(1, bytes);
        return String.format("%x", bi);
    }

    public static boolean checkPasswordHash(String hash, String password, String salt) throws NoSuchAlgorithmException {
        return hash.equals(generateHash(password, salt));
    }
}
