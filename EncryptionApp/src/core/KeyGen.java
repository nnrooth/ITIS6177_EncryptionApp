package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import utils.BaseTools;
import utils.CryptoTools;

public class KeyGen {
	
	public static void run() {
		
		System.out.printf("[+] TNO Key Generator Tool\n");
		
		// Create method variables
		String writePath; // Path to store keys
		FileOutputStream out; // File output stream
		KeyPair keyPair; // RSA key pair object
		String publicKey, privateKey; // fileNames for RSA keys
		byte[] publicKeyBytes, privateKeyBytes; // RSA key bytes
		
		// Get default values for storing keys
		writePath = BaseTools.getDefaultKeyDir();
		publicKey = BaseTools.getDefaultKeyFileNames()[0];
		privateKey = BaseTools.getDefaultKeyFileNames()[1];
		
		// Create file objects for keys
		File publicKeyFile = new File(writePath + publicKey);
		File privateKeyFile = new File(writePath + privateKey);
		
		try {
			// Check for existing keys
			if (!publicKeyFile.exists() && !privateKeyFile.exists()) {
				// Generate keys
				keyPair = CryptoTools.newAsymmetricKeyPair();
				publicKeyBytes = keyPair.getPublic().getEncoded();
				privateKeyBytes = keyPair.getPrivate().getEncoded();
				
				// Write key files
				out = new FileOutputStream(publicKeyFile); out.write(publicKeyBytes); out.close();
				out = new FileOutputStream(privateKeyFile); out.write(privateKeyBytes); out.close();
				
				// Set key files as read only
				publicKeyFile.setReadOnly();
				privateKeyFile.setReadOnly();
				
				System.out.printf("[+] Key Pair Created\n");
			} else {
				System.out.printf("[-] Key Pair Exists!\n");
			}
			
		} catch (NoSuchAlgorithmException e) {
			
		} catch (NoSuchProviderException e) {
			
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			
		}
	}
}
