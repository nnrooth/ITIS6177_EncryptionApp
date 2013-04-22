package core;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import crypto.Asymmetric;
import crypto.Hash;
import crypto.Symmetric;
import utils.CryptoTools;
import utils.BaseTools;

public class Encrypt {

	public static void main(String[] args) {
		// Setup for encryption process
		System.out.printf("[+] TNO Encryption Tool\n");
		
		String fip; // fip - File input path
		RandomAccessFile fin; // fin - Input file
		
		String fop; // fop - File output path
		String cipherWrite; String digestWrite;
		FileOutputStream fos; // fos - File output stream
		
		byte[] plainText; byte[] digest;
		byte[] keyBytes; byte[] ivBytes; 
		byte[] cipherText;
		
		String pukDir, puk; byte[] pukBytes; byte[] mdCipher;
		
		int keySize;
		
		System.out.printf("[.] Read Path: ");
		fip = BaseTools.getUserInput();
		
		System.out.printf("[*] Attempting Encryption\n");
		
		try {
			// Start encryption process
			
			pukDir = BaseTools.getDefaultKeyDir();
			puk = pukDir + BaseTools.getDefaultKeyFileNames()[0];
			
			// Check for key pair
			if (!new File(puk).exists()) {
				System.out.printf("[-] No key pair found!\n");
				KeyGen.main(null);
			}
			
			// Read in the public key
			fin = new RandomAccessFile(puk, "r");
			pukBytes = new byte[(int) fin.length()];
			fin.read(pukBytes);
			fin.close();
			
			// Read in the file
			fin = new RandomAccessFile(fip, "r");
			plainText = new byte[(int) fin.length()];
			fin.read(plainText);
			fin.close();
			
			// Create message digest of file
			digest = Hash.run(plainText);
			keySize = 32 /* bytes */;
					 /* 128 bits */
			
			// Encrypt message digest with public key
			mdCipher = Asymmetric.encrypt(digest, pukBytes);
			
			// Split message digest into key and iv
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
			
			// Encrypt file
			cipherText = Symmetric.encrypt(plainText, keyBytes, ivBytes);
			
			// Display results to user
			System.out.printf("[+] MD : %s\n", BaseTools.toHex(digest));
			System.out.printf("[+] Key: %s\n", BaseTools.toHex(keyBytes));
			System.out.printf("[+] IV : %s\n", BaseTools.toHex(ivBytes));
			
			// Get write path for file from user
			System.out.printf("[.] Write Path: ");
			fop = BaseTools.getUserInput();
			new File(fop).mkdirs();
			
			// Get the file's name
			String fileName = new File(fip).getName();
			cipherWrite = fop + fileName + ".ct";
			digestWrite = fop + fileName + ".md";
			
			// Write cipher of file and cipher of key
			fos = new FileOutputStream(cipherWrite); fos.write(cipherText); fos.close();
			fos = new FileOutputStream(digestWrite); fos.write(mdCipher); fos.close();
			
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
		} catch (ShortBufferException e) {
			System.out.printf("[-] Err: Short buffer error\n");
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
