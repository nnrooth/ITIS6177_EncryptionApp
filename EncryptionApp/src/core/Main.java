package core;

import utils.BaseTools;

public class Main {

	private static String tag = "Trust No One";
	
	public static void main(String[] args) {
		State state;
		String input;
		
		System.out.printf("[+] TNO Encryption/Decryption Tool\n");
		
		do {
			System.out.printf("\n[.] Instructions: ");
			input = BaseTools.getUserInput();
			System.out.println();
			
			state = getState(input);
			
			try {
				doState(state);
			} catch (Exception e) {
				System.out.printf("[-] Err: An error has occurred\n");
			}

		} while (state != State.STOP);
	}
	
	private enum State {
		RUN, ENCRYPT, DECRYPT, KEYGEN, STOP
	}
	
	private static State getState(String arg) {
		State state;
		
		if (arg.equals(String.valueOf(1)))
			state = State.ENCRYPT;
		else if (arg.toLowerCase().equals("encrypt"))
			state = State.ENCRYPT;
		else if (arg.equals(String.valueOf(2)))
			state = State.DECRYPT;
		else if (arg.toLowerCase().equals("decrypt"))
			state = State.DECRYPT;
		else if (arg.equals(String.valueOf(3)))
			state = State.KEYGEN;
		else if (arg.toLowerCase().equals("keygen"))
			state = State.KEYGEN;
		else
			state = State.STOP;
		
		return state;
	}
	
	private static void doState(State state) {
		switch (state) {
		
		case ENCRYPT:
			Encrypt.main(null);
			break;
			
		case DECRYPT:
			Decrypt.main(null);
			break;
			
		case KEYGEN:
			KeyGen.main(null);
			break;
		
		case STOP:
			System.out.printf("[+] Goodbye\n");
			break;
		
		default:
			System.out.printf("[+] %s\n", tag);
			break;
		}
	}

}
