package core;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import crypto.Asymmetric;
import crypto.Symmetric;
import dropbox.Dropbox;
import utils.CryptoTools;
import utils.BaseTools;

public class Decrypt {

	public static void main(String[] args) {
		// Setup for encryption process
		System.out.printf("[+] TNO Decryption Tool\n");
		
		InputStream in = null; OutputStream out = null;
		
		String fileName;
		String tempPath;
		String writePath;
		
		String digestWrite, cipherWrite, dataWrite;
		
		File digestFile, cipherFile, dataFile;
		RandomAccessFile fin; // fin - Input file
		
		byte[] digest, keyBytes, ivBytes;
		
		String pikDir, pik; byte[] pikBytes, mdCipher;
		
		int keySize;
		
		try {
			// Get file name from user
			System.out.printf("[.] File Name: ");
			fileName = BaseTools.getUserInput();
			tempPath = BaseTools.getDefaultTempDir();
			writePath = BaseTools.getDefaultDownloadDir();
			
			// Start encryption process
			System.out.printf("[*] Attempting Decryption\n");
			
			// Check for key pair
			pikDir = BaseTools.getDefaultKeyDir();
			pik = pikDir + BaseTools.getDefaultKeyFileNames()[1];
			
			if (!new File(pik).exists()) {
				System.out.printf("[-] No key pair found!\n");
				KeyGen.main(null);
			}
			
			// Read in the private key
			fin = new RandomAccessFile(pik, "r");
			pikBytes = new byte[(int) fin.length()];
			fin.read(pikBytes);
			fin.close();
			
			// Download cipher of key to temp dir
			digestWrite = tempPath + fileName + ".md";
			digestFile = new File(digestWrite);
			
			Dropbox.download(digestFile);
			
			// Read in cipher of key
			in = new FileInputStream(digestFile);
			mdCipher = new byte[(int) digestFile.length()];
			in.read(mdCipher); in.close();
			digestFile.delete();
			
			// Decrypt message digest with private key
			digest = Asymmetric.decrypt(mdCipher, pikBytes);
			keySize = 32 /* bytes */;
			 /* 128 bits */
			
			// Split message digest into key and iv
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
			
			dataWrite = writePath + fileName;
			out = new FileOutputStream(dataWrite);
			
			// Download cipher file
			cipherWrite = tempPath + fileName + ".ct";
			cipherFile = new File(cipherWrite);
			
			Dropbox.download(cipherFile);
			
			// Decrypt file
			in = new FileInputStream(cipherWrite);
			Symmetric.decrypt(in, out, keyBytes, ivBytes);
			in.close(); out.close();
			
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
