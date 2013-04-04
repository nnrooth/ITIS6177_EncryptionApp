package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTools {

	private static final String DEFAULT_PROVIDER = "BC";
	
	private static final String DEFAULT_MESSAGEDIGEST_ALGORITHM = "SHA-512";
	
	private static final String DEFAULT_SYMMETRIC_ALGORITHM = "AES";
	private static final String DEFAULT_SYMMETRIC_MODE = "CTR";
	private static final String DEFAULT_SYMMETRIC_PADDING_STATE = "NoPadding";
	
	public static String getDefaultProvider() { return DEFAULT_PROVIDER; }
	
	public static MessageDigest getDefaultMessageDigest() 
			throws NoSuchAlgorithmException, NoSuchProviderException {
		
		return MessageDigest.getInstance(DEFAULT_MESSAGEDIGEST_ALGORITHM, DEFAULT_PROVIDER);
	}
	
	public static Cipher getDefaultSymmetricCipherInstance()
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException {
		
		String cipherMethod = String.format("%s/%s/%s",
				DEFAULT_SYMMETRIC_ALGORITHM,
				DEFAULT_SYMMETRIC_MODE,
				DEFAULT_SYMMETRIC_PADDING_STATE);
		
		return Cipher.getInstance(cipherMethod, DEFAULT_PROVIDER);
	}

	public static byte[] initIvBytes(int messageNumber, byte[] ivBytes) {
		ivBytes[0] = (byte) (messageNumber >> 24);
		ivBytes[1] = (byte) (messageNumber >> 16);
		ivBytes[2] = (byte) (messageNumber >> 8);
		ivBytes[3] = (byte) (messageNumber >> 0);
		
		for (int i = 0; i < 7; i++) {
			ivBytes[8 + i] = 0;
		}
		
		ivBytes[15] = 1;
		
		return ivBytes;
	}
	
	public static SecretKeySpec getSymmetricKey(byte[] keyBytes) {
		return new SecretKeySpec(keyBytes, DEFAULT_SYMMETRIC_ALGORITHM);
	}
}
