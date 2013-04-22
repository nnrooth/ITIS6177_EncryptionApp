// TODO convert for decryption

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
import crypto.Symmetric;
import utils.CryptoTools;
import utils.BaseTools;

public class Decrypt {

	public static void main(String[] args) {
		
		System.out.printf("[+] TNO Decryption Tool\n");
		
		String fip; // fip - File input path
		RandomAccessFile fin; // fin - Input file
		
		String fop; // fop - File output path
		String fon; // fon - File output name
		String cipherRead; String digestRead; String plainTextWrite;
		FileOutputStream fos; // fos - File output stream
		
		byte[] cipherText; byte[] digest;
		byte[] keyBytes; byte[] ivBytes; 
		byte[] plainText;
		
		String pikDir, pik; byte[] pikBytes; byte[] mdCipher;
		
		int keySize;
		
		System.out.printf("[.] Read Path: ");
		fip = BaseTools.getUserInput();
		
		System.out.printf("[*] Attempting Decryption\n");
		
		try {
			// Start decryption process
			
			pikDir = BaseTools.getDefaultKeyDir();
			pik = pikDir + BaseTools.getDefaultKeyFileNames()[1];
			
			// Check for key pair
			if (!new File(pik).exists()) {
				System.out.printf("[-] No key pair found!\n");
				KeyGen.main(null);
			}
			
			// Read in the private key
			fin = new RandomAccessFile(pik, "r");
			pikBytes = new byte[(int) fin.length()];
			fin.read(pikBytes);
			fin.close();
			
			// Read in cipher of file
			cipherRead = fip + ".ct";
			fin = new RandomAccessFile(cipherRead, "r");
			cipherText = new byte[(int) fin.length()];
			fin.read(cipherText);
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
			
			plainText = Symmetric.decrypt(cipherText, keyBytes, ivBytes);
			
			System.out.printf("[+] MD : %s\n", BaseTools.toHex(digest));
			System.out.printf("[+] Key: %s\n", BaseTools.toHex(keyBytes));
			System.out.printf("[+] IV : %s\n", BaseTools.toHex(ivBytes));
			
			System.out.printf("[.] Write Path: ");
			fop = BaseTools.getUserInput();
			new File(fop).mkdirs();
			
			fon = new File(fip).getName();
			plainTextWrite = fop + fon;
						
			fos = new FileOutputStream(plainTextWrite); fos.write(plainText); fos.close();
			
			System.out.printf("[+] Write Complete\n");
			
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
