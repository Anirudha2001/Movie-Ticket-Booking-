package application.utils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class EncryptionDecryption {

	private static final String AES_ALGORITHM = "AES";
	private static final String SHA_256_ALGORITHM = "SHA-256";
	private static final String ENCRYPTION_TRANSFORMATION = "AES/ECB/PKCS5Padding";

	// Encrypts a string using AES with SHA-256
	public static String encrypt(String input) {
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
			SecretKeySpec secretKeySpec = generateSecretKeySpec();
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
			byte[] encryptedBytes = cipher.doFinal(input.getBytes());
			return bytesToHex(encryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Decrypts a string using AES with SHA-256
	public static String decrypt(String encryptedInput) {
		try {
			Cipher cipher = Cipher.getInstance(ENCRYPTION_TRANSFORMATION);
			SecretKeySpec secretKeySpec = generateSecretKeySpec();
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] encryptedBytes = hexToBytes(encryptedInput);
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			return new String(decryptedBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// Generates SecretKeySpec using SHA-256 hash of a hardcoded key
	private static SecretKeySpec generateSecretKeySpec() throws NoSuchAlgorithmException {
		String key = "YourHardcodedKey"; // Replace with your actual key
		MessageDigest digest = MessageDigest.getInstance(SHA_256_ALGORITHM);
		byte[] hash = digest.digest(key.getBytes());
		return new SecretKeySpec(hash, AES_ALGORITHM);
	}

	// Convert bytes to hexadecimal representation
	private static String bytesToHex(byte[] bytes) {
		StringBuilder result = new StringBuilder();
		for (byte b : bytes) {
			result.append(String.format("%02X", b));
		}
		return result.toString();
	}

	// Convert hexadecimal representation to bytes
	private static byte[] hexToBytes(String hexString) {
		int len = hexString.length();
		byte[] bytes = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
					+ Character.digit(hexString.charAt(i + 1), 16));
		}
		return bytes;
	}
}
