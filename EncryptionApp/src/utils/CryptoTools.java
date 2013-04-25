package utils;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class CryptoTools {
	// Session variables
	private static final String DEFAULT_PROVIDER = "BC";
	
	private static final String DEFAULT_MESSAGEDIGEST_ALGORITHM = "SHA-512";
	
	private static final String DEFAULT_SYMMETRIC_ALGORITHM = "AES";
	private static final String DEFAULT_SYMMETRIC_MODE = "CTR";
	private static final String DEFAULT_SYMMETRIC_PADDING_STATE = "NoPadding";
	
	private static final String DEFAULT_ASYMMETRIC_ALGORITHM = "RSA";
	private static final String DEFAULT_ASYMMETRIC_MODE = "NONE";
	private static final String DEFAULT_ASYMMETRIC_PADDING_STATE = "PKCS1Padding";
	private static final int 	DEFAULT_ASYMMETRIC_KEY_SIZE = 2048;
	
	/**
	 * Returns a string containing the default provider
	 * 
	 * @return Default provider
	 */
	public static String getDefaultProvider() { return DEFAULT_PROVIDER; }
	
	/**
	 * Returns a MessageDigest object created using default values
	 * 
	 * @return Default MessageDigest Object
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static MessageDigest getDefaultMessageDigest() 
			throws NoSuchAlgorithmException, NoSuchProviderException {
		
		return MessageDigest.getInstance(DEFAULT_MESSAGEDIGEST_ALGORITHM, DEFAULT_PROVIDER);
	}
	
	/**
	 * Returns the default Cipher used for symmetric encryption
	 * 
	 * @return
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws NoSuchProviderException
	 */
	public static Cipher getDefaultSymmetricCipher()
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			NoSuchProviderException {
		
		String cipherMethod = String.format("%s/%s/%s",
				DEFAULT_SYMMETRIC_ALGORITHM,
				DEFAULT_SYMMETRIC_MODE,
				DEFAULT_SYMMETRIC_PADDING_STATE
			);
		
		return Cipher.getInstance(cipherMethod, DEFAULT_PROVIDER);
	}
	
	/**
	 * Returns the default cipher used for Asymmetric Encryption
	 * 
	 * @return
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws NoSuchPaddingException
	 */
	public static Cipher getDefaultAsymmetricCipher()
			throws NoSuchAlgorithmException, NoSuchProviderException,
			NoSuchPaddingException {
		
		String cipherMethod = String.format("%s/%s/%s",
				DEFAULT_ASYMMETRIC_ALGORITHM,
				DEFAULT_ASYMMETRIC_MODE,
				DEFAULT_ASYMMETRIC_PADDING_STATE
			);
		
		return Cipher.getInstance(cipherMethod, DEFAULT_PROVIDER);
	}

	/**
	 * Accepts a byte array representing an initial value
	 * Initializes the iv using standard algorithm
	 * 
	 * @param messageNumber
	 * @param ivBytes
	 * 
	 * @return Initialized IV byte array
	 */
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
	
	/**
	 * Convert bytes into a public key for asymmetric encryption/decryption
	 * 
	 * @param keyBytes Bytes for public key
	 * 
	 * @return Public key created from bytes
	 * 
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PublicKey getAsymmetricPublicKey(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		
		PublicKey pubKey = KeyFactory
				.getInstance(DEFAULT_ASYMMETRIC_ALGORITHM)
				.generatePublic(new X509EncodedKeySpec(keyBytes));
		
		return pubKey;
	}
	
	/**
	 * Convert bytes into a private key for asymmetric encryption/decryption
	 * 
	 * @param keyBytes Bytes for private key
	 * 
	 * @return Private key created from bytes
	 * 
	 * @throws InvalidKeySpecException
	 * @throws NoSuchAlgorithmException
	 */
	public static PrivateKey getAsymmetricPrivateKey(byte[] keyBytes)
			throws InvalidKeySpecException, NoSuchAlgorithmException {
		
		PrivateKey privKey = KeyFactory
				.getInstance(DEFAULT_ASYMMETRIC_ALGORITHM)
				.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
		
		return privKey;
	}
	
	/**
	 * Generate a new Asymmetric Key Pair
	 * 
	 * @return New Asymmetric Key Pair
	 * 
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static KeyPair newAsymmetricKeyPair()
			throws NoSuchAlgorithmException, NoSuchProviderException {
		
		SecureRandom random = new SecureRandom();
		KeyPairGenerator fact = KeyPairGenerator.getInstance(DEFAULT_ASYMMETRIC_ALGORITHM, DEFAULT_PROVIDER);
		fact.initialize(DEFAULT_ASYMMETRIC_KEY_SIZE, random);
		KeyPair keyPair = fact.generateKeyPair();
		
		return keyPair;
	}
	
	/**
	 * Create a new SecureRandom generator
	 * 
	 * @return New SecureRandom generator
	 */
	public static SecureRandom newSecureRandom() {
		SecureRandom random = new SecureRandom();
		
		return random;
	}
}
