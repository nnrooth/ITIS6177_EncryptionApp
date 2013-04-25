package crypto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
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
	 * Encrypts a stream and outputs ciphertext as a stream. Uses 1024 byte buffer.
	 * 
	 * @param in
	 * @param out
	 * @param keyBytes
	 * @param ivBytes
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 */
	public static void encrypt(File dataFile, File cipherFile, byte[] keyBytes, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		
		FileInputStream in = null;
		CipherOutputStream out = null;
		
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(CryptoTools.initIvBytes(1, ivBytes));
		
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		cipher.init(Cipher.ENCRYPT_MODE,  key, ivSpec);
		
		in = new FileInputStream(dataFile);
		out = new CipherOutputStream(new FileOutputStream(cipherFile), cipher);
		
		int bytesRead = 0;
		byte[] buffer = new byte[1024];

		while ((bytesRead = in.read(buffer)) >= 0) {
			out.write(buffer, 0, bytesRead);
		}
		
		in.close(); out.close();
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
	
	/**
	 * Decrypts a stream and outputs plaintext as a stream. Uses 1024 byte buffer.
	 * 
	 * @param in
	 * @param out
	 * @param keyBytes
	 * @param ivBytes
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchProviderException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IOException
	 */
	public static void decrypt(File cipherFile, File dataFile, byte[] keyBytes, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		
		CipherInputStream in = null;
		FileOutputStream out = null;
		
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(CryptoTools.initIvBytes(1, ivBytes));
		
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		cipher.init(Cipher.DECRYPT_MODE,  key, ivSpec);
		
		in = new CipherInputStream(new FileInputStream(cipherFile), cipher);
		out = new FileOutputStream(dataFile);
		
		int bytesRead = 0;
		byte[] buffer = new byte[1024];

		while ((bytesRead = in.read(buffer)) >= 0) {
			out.write(buffer, 0, bytesRead);
		}
		
		in.close(); out.close();
	}
	
	public static void decrypt(InputStream in, File dataFile, byte[] keyBytes, byte[] ivBytes)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException, InvalidKeyException,
			InvalidAlgorithmParameterException, IOException {
		
		CipherInputStream cIn = null;
		FileOutputStream  out = null;
		
		SecretKeySpec key = CryptoTools.getSymmetricKey(keyBytes);
		IvParameterSpec ivSpec = new IvParameterSpec(CryptoTools.initIvBytes(1, ivBytes));
		
		Cipher cipher = CryptoTools.getDefaultSymmetricCipher();
		cipher.init(Cipher.DECRYPT_MODE,  key, ivSpec);
		
		cIn = new CipherInputStream(in, cipher);
		out = new FileOutputStream(dataFile);
		
		int bytesRead = 0;
		byte[] buffer = new byte[1024];

		while ((bytesRead = cIn.read(buffer)) >= 0) {
			out.write(buffer, 0, bytesRead);
		}
		
		cIn.close(); in.close(); out.close();
	}
}
