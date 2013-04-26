package crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import utils.CryptoTools;

public class Hash {

	/**
	 * The method accepts some data, and returns a message digest of data
	 * 
	 * @param data
	 *            Data to be hashed
	 * @return Hash of data
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 */
	public static byte[] run(byte[] data) throws NoSuchAlgorithmException,
			NoSuchProviderException {

		MessageDigest digest = CryptoTools.getDefaultMessageDigest();
		digest.update(data);
		return digest.digest();
	}

	public static byte[] run(InputStream in) throws NoSuchAlgorithmException,
			NoSuchProviderException, IOException {

		byte[] hash = null;
		MessageDigest digest = CryptoTools.getDefaultMessageDigest();
		in = new DigestInputStream(in, digest);

		byte[] buffer = new byte[1024];

		while ((in.read(buffer)) >= 0) {
		}
		hash = digest.digest();

		return hash;
	}

}
