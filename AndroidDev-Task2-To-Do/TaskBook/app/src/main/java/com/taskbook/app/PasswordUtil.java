package com.taskbook.app;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Basic password hashing without external libraries:
 * SHA-256 over (salt + password), with a random salt per user.
 * Plain-text passwords are never stored.
 */
public class PasswordUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    /** Creates a new random 16-byte salt, hex-encoded. */
    public static String generateSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return toHex(salt);
    }

    /** Returns the hex-encoded SHA-256 hash of salt + password. */
    public static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            return toHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to exist on every Android device
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /** Constant-time comparison of two hashes. */
    public static boolean matches(String password, String salt, String expectedHash) {
        byte[] a = hash(password, salt).getBytes(StandardCharsets.UTF_8);
        byte[] b = expectedHash.getBytes(StandardCharsets.UTF_8);
        return MessageDigest.isEqual(a, b);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
