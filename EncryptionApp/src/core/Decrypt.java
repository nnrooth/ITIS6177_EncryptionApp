package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import crypto.Symmetric;
import dropbox.Dropbox;

public class Decrypt {

	public static void run() {
		// Setup for encryption process
		System.out.printf("[+] TNO Decryption Tool\n");
		
		InputStream in = null;
		
		String fileName;
		String tempPath;
		String writePath;
		
		String digestWrite, cipherWrite, dataWrite;
		
		File digestFile, cipherFile, dataFile;
		File privateKeyFile; // File representing private key
		
		byte[] digest, keyBytes, ivBytes;
		
		String privateKeyDir, privateKey; byte[] privateKeyBytes, mdCipherBytes;
		
		int keySize;
		
		try {
			// Get file name from user
			System.out.printf("[.] File Name: ");
			fileName = BaseTools.getUserInput();
			tempPath = BaseTools.getDefaultTempDir();
			writePath = BaseTools.getDefaultDownloadDir();
			
			// Setup files for read/write
			cipherWrite = tempPath + fileName + ".ct";
			digestWrite = tempPath + fileName + ".md";
			dataWrite = writePath + fileName;
			
			cipherFile = new File(cipherWrite);
			digestFile = new File(digestWrite);
			dataFile = new File(dataWrite);
			
			
			// Start encryption process
			System.out.printf("[*] Attempting Decryption\n");
			
			// Check for key pair
			privateKeyDir = BaseTools.getDefaultKeyDir();
			privateKey = privateKeyDir + BaseTools.getDefaultKeyFileNames()[1];
			
			if (!new File(privateKey).exists()) {
				System.out.printf("[-] No key pair found!\n");
				KeyGen.run();
			}
			
			// Read in the private key
			privateKeyFile = new File(privateKey);
			privateKeyBytes = new byte[(int) privateKeyFile.length()];
			in = new FileInputStream(privateKeyFile);
			in.read(privateKeyBytes); in.close();
			
			// Download cipher of key to temp dir
			Dropbox.download(digestFile);
			
			// Read in cipher of key
			in = new FileInputStream(digestFile);
			mdCipherBytes = new byte[(int) digestFile.length()];
			in.read(mdCipherBytes); in.close();
			digestFile.delete();
			
			// Decrypt message digest with private key
			digest = Asymmetric.decrypt(mdCipherBytes, privateKeyBytes);
			keySize = 32 /* bytes */;
			 /* 128 bits */
			
			// Split message digest into key and iv
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
			
			// Download cipher file
			Dropbox.download(cipherFile);
			
			// Decrypt file
			Symmetric.decrypt(cipherFile, dataFile, keyBytes, ivBytes);
			
			cipherFile.delete();
			System.out.printf("[+] Download Complete\n");
		
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
