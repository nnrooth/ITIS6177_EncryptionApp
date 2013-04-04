package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class Hash {

	private final static String DEFAULT_ALGORITHM = "SHA-256";
	
	/**
	 * The method accepts some data, and returns a message digest of data
	 * 
	 * @param data Data to be hashed
	 * @return Hash of data
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static byte[] run(byte[] data) throws NoSuchAlgorithmException, NoSuchProviderException {
		MessageDigest digest = MessageDigest.getInstance(DEFAULT_ALGORITHM, "BC");
		digest.update(data);
		return digest.digest();
	}

}
