package crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import utils.CryptoTools;

public class Symmetric {

	// TODO Comment on this method
	public static byte[] encrypt(byte[] dataBytes, byte[] keyBytes, byte[] ivBytes)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, ShortBufferException,
				IllegalBlockSizeException, BadPaddingException,
				InvalidKeyException, InvalidAlgorithmParameterException {
		
		byte[] cipherBytes = new byte[dataBytes.length];
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		
		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		
		int ctLength = cipher.update(dataBytes, 0, dataBytes.length, cipherBytes, 0);
		ctLength += cipher.doFinal(cipherBytes, ctLength);
		
		return cipherBytes;
	}
	
	public static byte[] decrypt(byte[] dataBytes, byte[] keyBytes, byte[] ivBytes)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, ShortBufferException,
				IllegalBlockSizeException, BadPaddingException,
				InvalidKeyException, InvalidAlgorithmParameterException {
		
		byte[] cipherBytes = new byte[dataBytes.length];
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		
		int ptLength = cipher.update(dataBytes, 0, dataBytes.length, cipherBytes, 0);
		ptLength += cipher.doFinal(cipherBytes, ptLength);
		
		return cipherBytes;
	}

}
