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
	
	public static void main(String args[]) {
		
		System.out.printf("[+] TNO Key Generator Tool\n");
		
		String fop; // fop - File output path
		FileOutputStream fos; // fos - File output stream
		KeyPair keyPair;
		String puk, pik;
		byte[] publicKey, privateKey;
		
		try {
			keyPair = CryptoTools.newAsymmetricKeyPair();
			publicKey = keyPair.getPublic().getEncoded();
			privateKey = keyPair.getPrivate().getEncoded();
			
			fop = BaseTools.getDefaultKeyDir();
			puk = BaseTools.getDefaultKeyFileNames()[0];
			pik = BaseTools.getDefaultKeyFileNames()[1];
			
			File pukFile = new File(fop + puk);
			File pikFile = new File(fop + pik);
			
			if (!pukFile.exists() && !pikFile.exists()) {
				fos = new FileOutputStream(pukFile); fos.write(publicKey); fos.close();
				fos = new FileOutputStream(pikFile); fos.write(privateKey); fos.close();
				
				pukFile.setReadOnly();
				pikFile.setReadOnly();
				
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
