// TODO convert for decryption

package core;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.FileOutputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;

import crypto.Hash;
import crypto.Symmetric;
import utils.CryptoTools;
import utils.BaseTools;

public class Decrypt {

	public static void main(String[] args) {
		
		System.out.printf("[+] TNO Decryption Tool Reborn");
		Scanner scanIn = new Scanner(System.in);
		
		String fip; // fip - File input path
		RandomAccessFile fin; // fin - Input file
		
		String fop; // fop - File output path
		String fon; // fon - File output name
		String cipherWrite; String digestWrite;
		FileOutputStream fos; // fos - File output stream
		
		byte[] plainText; byte[] digest;
		byte[] keyBytes; byte[] ivBytes; 
		byte[] cipherText;
		
		int keySize;
		
		System.out.printf("[.] Read Path: ");
		fip = scanIn.nextLine();
		
		System.out.printf("[*] Attempting Decryption");
		
		try {
			
			fin = new RandomAccessFile(fip, "r");
			plainText = new byte[(int) fin.length()];
			fin.read(plainText);
			fin.close();
			
			digest = Hash.run(plainText);
			keySize = 32; // 256-bit key
			
			keyBytes = Arrays.copyOf(digest, keySize);
			ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
			
			cipherText = Symmetric.encrypt(plainText, ivBytes, keyBytes);
			
			System.out.printf("[+] MD : %s\n", BaseTools.toHex(digest));
			System.out.printf("[+] Key: %s\n", BaseTools.toHex(keyBytes));
			System.out.printf("[+] IV : %s\n", BaseTools.toHex(ivBytes));
			
			System.out.printf("[.] Write Path: ");
			fop = scanIn.nextLine();
			System.out.printf("[.] Write Name: ");
			fon = scanIn.nextLine();
			
			cipherWrite = fop + fon + ".ct";
			digestWrite = fop + fon + ".md";
			
			fos = new FileOutputStream(cipherWrite); fos.write(cipherText); fos.close();
			fos = new FileOutputStream(digestWrite); fos.write(digest); fos.close();
			
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
		} finally {
			scanIn.close();
		}
	}
}
