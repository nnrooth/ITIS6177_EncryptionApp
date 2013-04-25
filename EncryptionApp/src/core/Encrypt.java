package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import utils.BaseTools;
import utils.CryptoTools;
import crypto.Asymmetric;
import crypto.Hash;
import crypto.Symmetric;
import dropbox.Dropbox;

public class Encrypt {

	public static void run() {
		System.out.printf("[+] TNO Encryption Tool\n");
		
		// Setup for encryption process
		InputStream in = null; OutputStream out = null;
		
		String filePath; // Path of file to be encrypted
		
		String writePath, fileName;
		String cipherWrite; String digestWrite;
		String publicKeyDir, publicKey;
		
		byte[] publicKeyBytes, mdCipherBytes; // Byte arrays used for encrypting file digest
		byte[] digest, keyBytes, ivBytes; // Byte arrays used for encryption file
				
		File publicKeyFile; // File reprsenting public key
		File dataFile, cipherFile, digestFile;
		
		int keySize; // Key size to use for AES encryption
		
		try {
			// Get read path for file from user
			System.out.printf("[.] Read Path: ");
			filePath = BaseTools.getUserInput();
			writePath = BaseTools.getDefaultTempDir();
			
			// Get the file's name
			fileName = new File(filePath).getName();
			cipherWrite = writePath + fileName + ".ct";
			digestWrite = writePath + fileName + ".md";
			
			// Create new files for read/write
			dataFile = new File(filePath);
			cipherFile = new File(cipherWrite);
			digestFile = new File(digestWrite);
			
			// Start encryption process
			System.out.printf("[*] Attempting Encryption\n");
						
			publicKeyDir = BaseTools.getDefaultKeyDir();
			publicKey = publicKeyDir + BaseTools.getDefaultKeyFileNames()[0];
			
			// Check for public key
			publicKeyFile = new File(publicKey);
			if (!publicKeyFile.exists()) {
				System.out.printf("[-] No key pair found!\n");
				KeyGen.run();
			}
			
			// Read in the public key
			publicKeyBytes = new byte[(int) publicKeyFile.length()];
			in = new FileInputStream(publicKeyFile);
			in.read(publicKeyBytes); in.close();
			
			// Create message digest of file
			in = new FileInputStream(dataFile);
			digest = Hash.run(in); in.close();
			keySize = 32 /* bytes */;
					 /* 128 bits */
			
			// Encrypt message digest with public key
			mdCipherBytes = Asymmetric.encrypt(digest, publicKeyBytes); publicKeyBytes = null;
			
			// Split message digest into key and iv
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
						
			// Encrypt file and store temporarily
			Symmetric.encrypt(dataFile, cipherFile, keyBytes, ivBytes);
			
			// Store digest temporarily
			out = new FileOutputStream(digestFile);
			out.write(mdCipherBytes); out.close();

			// Pass files to Dropbox
			Dropbox.upload(cipherFile); cipherFile.delete();
			Dropbox.upload(digestFile); digestFile.delete();
			
			// Process is complete, notify user
			System.out.printf("[+] Write Complete\n");
		
			// Error handling
		} catch (FileNotFoundException e) {
			System.out.printf("[-] Err: File not found\n");
		} catch (IOException e) {
			System.out.printf("[-] Err: Read/Write error\n");
		} catch (NoSuchAlgorithmException e) {
			System.out.printf("[-] Err: Algorithm is unavailable\n");
		} catch (InvalidKeyException e) {
			System.out.printf("[-] Err: Key is invalid\n");
		} catch (InvalidAlgorithmParameterException e) {
			System.out.printf("[-] Err: Invalid algorithm parameter\n");
		} catch (IllegalBlockSizeException e) {
			System.out.printf("[-] Err: Illegal block size\n");
		} catch (BadPaddingException e) {
			System.out.printf("[-] Err: Bad padding exception\n");
		} catch (NoSuchProviderException e) {
			System.out.printf("[-] Err: Bouncy Castle is unavailable\n");
		} catch (NoSuchPaddingException e) {
			System.out.printf("[-] Err: No such padding error\n");
		} catch (Exception e) {
			System.out.printf("[-] Err: Generic error\n");
			e.printStackTrace();
		}
	}
}
