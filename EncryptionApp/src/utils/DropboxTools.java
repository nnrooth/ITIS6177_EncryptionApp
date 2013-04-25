package utils;

import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.WebAuthSession;

public class DropboxTools {

	// Initialize app keys
	private final static String APP_KEY = "1m4hi6opflwrbyz";
	private final static String APP_SECRET = "r06ysm5vs1c12bk";
	// Initialize access type
	private final static AccessType ACCESS_TYPE = AccessType.DROPBOX;
		
	public static AppKeyPair getKeyPair() {
		AppKeyPair appKeyPair = new AppKeyPair(APP_KEY, APP_SECRET);
		
		return appKeyPair;
	}
	
	public static WebAuthSession getAuthSession() {
		WebAuthSession webAuthSession = new WebAuthSession(getKeyPair(), ACCESS_TYPE);
		
		return webAuthSession;
	}
}
