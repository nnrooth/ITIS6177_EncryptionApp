package utils;

public class BaseTools {

	private static String digits = "01234567890abcdef";
	
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

}
