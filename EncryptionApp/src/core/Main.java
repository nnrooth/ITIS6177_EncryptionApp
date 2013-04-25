package core;

import java.io.IOException;

import dropbox.Dropbox;
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
		RUN, ENCRYPT, DECRYPT, KEYGEN, CONNECT, HELP, CLEAR, STOP
	}
	
	private static State getState(String arg) {
		State state;
		
		if (arg.isEmpty())
			state = State.RUN;
		else if (arg.toLowerCase().equals("clr") || arg.toLowerCase().equals("clear"))
			state = State.CLEAR;
		else if (arg.equals(String.valueOf(1)))
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
		else if (arg.equals(String.valueOf(4)))
			state = State.CONNECT;
		else if (arg.toLowerCase().equals("connect"))
			state = State.CONNECT;
		else if (arg.equals(String.valueOf(0)))
			state = State.HELP;
		else if (arg.toLowerCase().equals("help") || arg.toLowerCase().equals("info")
			  || arg.toLowerCase().equals("question") || arg.equals("?"))
			state = State.HELP;
		else if (arg.toLowerCase().equals("stop") || arg.toLowerCase().equals("quit")
			  || arg.toLowerCase().equals("end")  || arg.toLowerCase().equals("kill")
			  || arg.toLowerCase().equals("exit") || arg.equals("*"))
			state = State.STOP;
		else
			state = State.RUN;
		
		return state;
	}
	
	private static void doState(State state) {
		switch (state) {
		
		case CLEAR:
			try {
				Runtime.getRuntime().exec("cls");
			} catch (IOException e) {
				System.out.printf("[-] Unable to clear screen\n");
			}
			break;
		
		case ENCRYPT:
			Encrypt.run();
			break;
			
		case DECRYPT:
			Decrypt.run();
			break;
			
		case KEYGEN:
			KeyGen.run();
			break;
			
		case CONNECT:
			Dropbox.connect();
			break;
			
		case HELP:
			displayHelp();
			break;
		
		case STOP:
			System.out.printf("[+] Goodbye\n");
			break;
		
		default:
			System.out.printf("[+] %s\n", tag);
			break;
		}
	}

	private static void displayHelp() {
		System.out.printf(
				"[0] Help/Info\n" +	
				"[1] Encrypt\n" +
				"[2] Decrypt\n" +
				"[3] Generate RSA Key Pair\n" +
				"[4] Establish Session with Dropbox\n" +
				"[*] Quit\n"
			);
	}
	
}
