package crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import utils.CryptoTools;

public class Asymmetric {

	/**
	 * Encrypts a byte array using the key
	 * 
	 * @param cipherBytes
	 * @param keyBytes
	 * 
	 * @return
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeySpecException
	 */
	public static byte[] encrypt(byte[] dataBytes, byte[] keyBytes)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidKeySpecException	{
		
		byte[] cipherBytes = null;
		Cipher cipher = CryptoTools.getDefaultAsymmetricCipher();
		
		PublicKey pubKey = CryptoTools.getAsymmetricPublicKey(keyBytes);
				
		cipher.init(Cipher.ENCRYPT_MODE, pubKey, CryptoTools.newSecureRandom());
		cipherBytes = cipher.doFinal(dataBytes);
		
		return cipherBytes;
	}
	
	/**
	 * Decrypts a byte array using the key
	 * 
	 * @param cipherBytes
	 * @param keyBytes
	 * 
	 * @return
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws InvalidKeySpecException
	 */
	public static byte[] decrypt(byte[] cipherBytes, byte[] keyBytes)
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			InvalidKeySpecException	{
		
		byte[] dataBytes = null;
		Cipher cipher = CryptoTools.getDefaultAsymmetricCipher();
		
		PrivateKey privKey = CryptoTools.getAsymmetricPrivateKey(keyBytes);
		
		cipher.init(Cipher.DECRYPT_MODE, privKey, CryptoTools.newSecureRandom());
		dataBytes = cipher.doFinal(cipherBytes);
		
		return dataBytes;
	}

}
