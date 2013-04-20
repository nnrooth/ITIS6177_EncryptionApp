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

	/**
	 * Encrypt passed plaintext
	 * 
	 * @param dataBytes
	 * @param keyBytes
	 * @param ivBytes
	 * 
	 * @return Ciphertext of encrypted plaintext
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws ShortBufferException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
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
	
	/**
	 * Decrypt passed ciphertext
	 * 
	 * @param cipherBytes
	 * @param keyBytes
	 * @param ivBytes
	 * 
	 * @return Plaintext decrypted from ciphertext
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws ShortBufferException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static byte[] decrypt(byte[] cipherBytes, byte[] keyBytes, byte[] ivBytes)
				throws NoSuchAlgorithmException, NoSuchProviderException,
				NoSuchPaddingException, ShortBufferException,
				IllegalBlockSizeException, BadPaddingException,
				InvalidKeyException, InvalidAlgorithmParameterException {
		
		byte[] dataBytes = new byte[cipherBytes.length];
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		
		cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
		
		int ctLength = cipher.update(cipherBytes, 0, cipherBytes.length, dataBytes, 0);
		ctLength += cipher.doFinal(dataBytes, ctLength);
		
		return dataBytes;
	}

}
