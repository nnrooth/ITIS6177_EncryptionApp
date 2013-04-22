package utils;

import java.io.File;
import java.util.Scanner;

public class BaseTools {

	private static String digits = "01234567890abcdef";
	private static Scanner scanIn = null;

	public static String getUserInput() {
		if (scanIn == null) {
			scanIn = new Scanner(System.in);
		}
		
		return scanIn.nextLine();
	}
	
	public static String toHex(byte[] bytes, int length) {
		StringBuffer buffy = new StringBuffer();
		for (int n = 0; n < length; n++) {
			int v = bytes[n] & 0xff;
			buffy.append(digits.charAt(v >> 4));
			buffy.append(digits.charAt(v & 0xf));
		}
		
		return buffy.toString();
	}
	
	public static String toHex(byte[] bytes) {
		return toHex(bytes, bytes.length);
	}
	
	public static String getDefaultKeyDir() {
		String dir = System.getProperty("user.dir") + "\\.keys\\";
		File file = new File(dir); file.mkdir(); file = null;
		
		return dir;
	}
	
	public static String[] getDefaultKeyFileNames() {
		String[] keyFileNames = new String[] { ".puk", ".pik" };
		
		return keyFileNames;
	}
}
