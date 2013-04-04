package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import utils.CryptoTools;

public class Hash {

	/**
	 * The method accepts some data, and returns a message digest of data
	 * 
	 * @param data Data to be hashed
	 * @return Hash of data
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static byte[] run(byte[] data)
			throws NoSuchAlgorithmException, NoSuchProviderException {
		
		MessageDigest digest = CryptoTools.getDefaultMessageDigest();
		digest.update(data);
		return digest.digest();
	}

}
