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

public class Encrypt {

	public static void main(String[] args) {
			System.out.printf("[+] TNO Encryption Tool Reborn");
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
			
			System.out.printf("[*] Attempting Encryption");
			try {
				fin = new RandomAccessFile(fip, "r");
				plainText = new byte[(int) fin.length()];
				fin.read(plainText);
				fin.close();
				
				digest = Hash.hashBytes(plainText);
				keySize = 32; // 256-bit key
				
				keyBytes = Arrays.copyOf(digest, keySize);
				ivBytes = CryptoTools.initIvBytes(1, Arrays.copyOfRange(digest, keySize, keySize + 16));
				
				cipherText = Symmetric.encryptAesCtr(plainText, ivBytes, keyBytes);
				
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
				
			} catch (IOException e) {
				
			} catch (NoSuchAlgorithmException e) {
				
			} catch (InvalidKeyException e) {
				
			} catch (InvalidAlgorithmParameterException e) {
				
			} catch (IllegalBlockSizeException e) {
				
			} catch (ShortBufferException e) {
				
			} catch (BadPaddingException e) {
				
			} catch (NoSuchProviderException e) {
				
			} catch (NoSuchPaddingException e) {
				
			} catch (Exception e) {
				
			} finally {
				scanIn.close();
			}
	}

}
