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
import utils.CryptoTools;
import utils.BaseTools;

public class DecryptV2 {

	public static void main(String[] args) {
		// Setup for encryption process
		System.out.printf("[+] TNO Decryption Tool\n");
		
		InputStream in = null; OutputStream out = null;
		
		String fip; // fip - File input path
		RandomAccessFile fin; // fin - Input file
		
		String fop; // fop - File output path
		String fon; // fon - File output name
		String cipherRead, digestRead, plainTextWrite;
		
		byte[] digest, keyBytes, ivBytes;
		
		String pikDir, pik; byte[] pikBytes, mdCipher;
		
		int keySize;
		
		try {
			// Get read path for file from user
			System.out.printf("[.] Read Path: ");
			fip = BaseTools.getUserInput();
			
			// Get write path for file from user
			System.out.printf("[.] Write Path: ");
			fop = BaseTools.getUserInput();
			new File(fop).mkdirs();
			
			// Start encryption process
			System.out.printf("[*] Attempting Encryption\n");
			
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
			
			// Read in cipher of key
			digestRead = fip + ".md";
			fin = new RandomAccessFile(digestRead, "r");
			mdCipher = new byte[(int) fin.length()];
			fin.read(mdCipher);
			fin.close();
			
			// Decrypt message digest with private key
			digest = Asymmetric.decrypt(mdCipher, pikBytes);
			keySize = 32 /* bytes */;
			 /* 128 bits */
			
			// Split message digest into key and iv
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
			
			// Get the file's name
			fon = new File(fip).getName();
			plainTextWrite = fop + fon;
			
			out = new FileOutputStream(plainTextWrite);
						
			// Decrypt file
			cipherRead = fip + ".ct";
			in = new FileInputStream(cipherRead);
			Symmetric.decrypt(in, out, keyBytes, ivBytes);
			in.close(); out.close();
			
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
