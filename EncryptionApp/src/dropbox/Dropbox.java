package dropbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import utils.BaseTools;
import utils.DropboxTools;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxInputStream;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.WebAuthSession;

public class Dropbox {
	
	private static DropboxAPI<WebAuthSession> dbApi = null;
	private static AccessTokenPair accessTokens = null;
	
	private final static String DEFAULT_PATH = "/xxxEncryption/";
	
	public static boolean connected() {
		boolean connected = (accessTokens != null);
		
		return connected;
	}
	
	private static void saveSession() {
		String tokenDir = BaseTools.getDefaultTokenDir();
		File keyFile = new File(tokenDir + ".key");
		File secretFile = new File(tokenDir + ".secret");
		
		FileOutputStream out = null;
		byte[] keyBytes, secretBytes;
		if (!keyFile.exists()) {
			try {
				out = new FileOutputStream(keyFile);
				keyBytes = accessTokens.key.getBytes("UTF-8");
				out.write(keyBytes); out.close();
				keyFile.setReadOnly();
			} catch (Exception e) {}
		}
		
		if (!secretFile.exists()) {
			try {
				out = new FileOutputStream(secretFile);
				secretBytes = accessTokens.secret.getBytes("UTF-8");;
				out.write(secretBytes); out.close();
				secretFile.setReadOnly();
			} catch (Exception e) {}
		}
				
	}
	
	private static boolean loadSession() {
		boolean loaded = false;
		
		String tokenDir = BaseTools.getDefaultTokenDir();
		File keyFile = new File(tokenDir + ".key");
		File secretFile = new File(tokenDir + ".secret");
		
		FileInputStream in = null;
		byte[] keyBytes, secretBytes;
		String key, secret;
		
		if (keyFile.exists() && secretFile.exists()) {
			try {
				keyBytes = new byte[(int) keyFile.length()];
				in = new FileInputStream(keyFile);
				in.read(keyBytes); in.close();
				key = new String(keyBytes, "UTF-8");
				
				secretBytes = new byte[(int) secretFile.length()];
				in = new FileInputStream(secretFile);
				in.read(secretBytes); in.close();
				secret = new String(secretBytes, "UTF-8");
				
				accessTokens = new AccessTokenPair(key, secret);
				
				WebAuthSession session = DropboxTools.getAuthSession();
				dbApi = new DropboxAPI<WebAuthSession>(session);
				dbApi.getSession().setAccessTokenPair(accessTokens);
				
				loaded = true;
			} catch (Exception e) {}
		}
		
		return loaded;
	}
	
	public static void connect() {
		if (dbApi == null && accessTokens == null) {
			if (!loadSession()) {
				WebAuthSession session = DropboxTools.getAuthSession();
				dbApi = new DropboxAPI<WebAuthSession>(session);
				
				try {
					WebAuthSession.WebAuthInfo info = session.getAuthInfo();
					System.out.printf("[1] Go to: %s\n", info.url);
					System.out.printf("[2] Allow access to this app\n");
					System.out.printf("[3] Press ENTER\n");
					
					try {
						while (System.in.read() != '\n') {}
					} catch (IOException e) {}
				
					session.retrieveWebAccessToken(info.requestTokenPair);
					accessTokens = session.getAccessTokenPair();
					dbApi.getSession().setAccessTokenPair(accessTokens);
					saveSession();
					
					System.out.printf("[+] Link Established\n");
				} catch (DropboxException e) {
					System.out.printf("[-] Unable to Establish Link\n");
				}
			} else {
				System.out.printf("[+] Session Loaded, Link Established\n");
			}
		} else {
			System.out.printf("[-] Link Already Established\n");
		}
	}
	
	public static DropboxInputStream getDownloadStream(File file)
			throws DropboxException {
		
		DropboxInputStream inputStream = null;
		String readPath = DEFAULT_PATH + file.getName();
		
		inputStream = dbApi.getFileStream(readPath, null);
		
		return inputStream;
	}
	
	public static void download(File file) {
		if (!connected()) {
			connect();
		}
		
		FileOutputStream outputStream = null;
		String readPath = DEFAULT_PATH + file.getName();
		
		try {
			outputStream = new FileOutputStream(file);
			dbApi.getFile(readPath, null, outputStream, null);
		} catch (DropboxUnlinkedException e) {
			System.out.printf("[-] Err: Dropbox Link is not established\n");
		} catch (DropboxException e) {
			System.out.printf("[-] Err: Failed downloading %s from dropbox\n", file.getName());
		} catch (FileNotFoundException e) {
			System.out.printf("[-] Err: Unable to locate remote file %s\n", file.getName());
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {}
			}
		}
	}
	
	public static void upload(File file) {
		if (!connected()) {
			connect();
		}
		
		FileInputStream inputStream = null;
		String writePath = DEFAULT_PATH + file.getName();
		long fileLength = file.length();
		
		try {
			inputStream = new FileInputStream(file);
			Entry newEntry = dbApi.putFile(
					writePath,
					inputStream,
					fileLength,
					null, null
				);
			System.out.printf("[+] File's Rev: %s\n", newEntry.rev);
			
		} catch (DropboxUnlinkedException e) {
			System.out.printf("[-] Err: Dropbox Link is not established\n");
		} catch (DropboxException e) {
			System.out.printf("[-] Err: Failed uploading %s to dropbox\n", file.getName());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.printf("[-] Err: Unable to locate local file %s\n", file.getName());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {}
			}
		}
	}
		
}
