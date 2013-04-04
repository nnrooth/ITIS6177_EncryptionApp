package utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class CryptoTools {

	public static String getDefaultEncryptionMode() { return DEFAULT_ENCRYPTION_MODE; }
	
	public static Cipher getDefaultCipherInstance() throws Exception {
		String cipherMethod = String.format("%s/%s/%s",
				DEFAULT_ENCRYPTION_ALGORITHM,
				DEFAULT_ENCRYPTION_MODE,
				DEFAULT_PADDING_STATE);
		
		return Cipher.getInstance(cipherMethod);
	}
	
	public static IvParameterSpec getAesIv(byte[] ivBytes) {
		return IvParameterSpec(ivBytes);
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
	
	public static SecretKey getAesKey(byte[] keyBytes) {
		return SecretKeySpec(keyBytes, "AES");
	}
}
