import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hangman {
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";

	public static void main(String[] args) throws InterruptedException, ArrayIndexOutOfBoundsException {

		System.out.println("Welcome to Hangman!");
		Thread.sleep(1000);
		System.out.println("===============================================================");
		System.out.println("                           Rules                          ");
		System.out.println("---------------------------------------------------------------");
		System.out.println("1. Pick a word that is between 3~20 characters.");
		System.out.println("2. The word may only contain characters from A~Z and 1~9.");
		System.out.println("3. For spaces please type \"_\". ex: Hello_World");
		System.out.println("4. Your word should not contain special characters. ex: %^&#");
		System.out.println("5. Have Fun!");
		System.out.println("===============================================================");
		Thread.sleep(1000);
		String keepplaying = "Y";
		while (keepplaying.equals("Y")) {
			@SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
			String word = "A";// the word other players have to guess
			int chances = 8;// the amount of chances other players have
			String guess;// the word the player guesses
			boolean correctletter;
			boolean specialword = false;
			boolean specialchar = false;
			boolean exception;
			boolean correct = false;
			int charscorrect = 0;
			int charscorrect2 = 0;
			int spaces = 0;
			int counterz = 0;
			boolean firstspaces = true;
			int numfirstspaces = 0;
			String[] letter = new String[20];
			String[] checkletter = new String[20];// used to check if any
													// special letters
			String[] charguesses = new String[36];
			boolean[] guessedwords = new boolean[20];
			String choice;
			do {
				System.out.println("Would you like to play against the Cpu or a real person?");
				System.out.println("========================================================");
				System.out.println("\tA. Person");
				System.out.println("\tB. CPU");
				System.out.println("========================================================");
				choice = scanner.next().toUpperCase();
			} while ("BA".indexOf(choice) != 0 && "BA".indexOf(choice) != 1);
			// =====================================================================================================================================//
			if (choice.equals("B")) {
				Random random = new Random();
				int which = random.nextInt(18);
				String wordlist[] = new String[] { "strengths", "browser", "rhythmic", "camp_fire", "jukebox",
						"computer", "taskbar", "snowfight", "orphan", "months", "car", "depths", "compact", "muscles",
						"cat", "pillow", "heart", "stretchmarks", "mountain_bike", "earth" };
				word = wordlist[which];
				try {
					for (int x = 0; x < word.length(); x++) {
						letter[x] = String.valueOf(word.charAt(x));
						Pattern p = Pattern.compile("[^A-Za-z0-9]");
						Matcher m = p.matcher(letter[x]);
						if (specialword == false && !letter[x].equals("_")) {
							specialword = m.matches();
						}
						if (letter[x].equals("_") && firstspaces == false && specialword == false) {
							spaces++;
						}
					}
				} catch (ArrayIndexOutOfBoundsException e) {

				}
			}
			// =====================================================================================================================================//
			if (choice.equals("A")) {
				do {
					exception = false;
					specialword = false;
					spaces = 0;
					System.out.println("Please write a word.");
					word = scanner.next();
					try {

						for (int x = 0; x < word.length(); x++) {
							checkletter[x] = String.valueOf(word.charAt(x));
							Pattern p = Pattern.compile("[^A-Za-z0-9]");
							Matcher m = p.matcher(checkletter[x]);
							if (specialword == false && !checkletter[x].equals("_")) {
								specialword = m.matches();
								firstspaces = false;
							}
							if (checkletter[x].equals("_") && firstspaces == false && specialword == false) {
								spaces++;
							} else if (checkletter[x].equals("_") && specialword == false && firstspaces == true) {
								++numfirstspaces;
							}
						}
						for (int y = 0; y < word.length(); y++) {
							try {
								letter[y] = String.valueOf(word.charAt(y + numfirstspaces));
								System.out.println(letter[y]);
							} catch (StringIndexOutOfBoundsException e) {
							}
						}

						if (specialword == true)
							System.out.println("Your word has special characters in it!");
						else if ((word.length() - spaces - numfirstspaces) < 3) {
							System.out.println("The word is less than 3 characters!");
							exception = true;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						if (specialword == true)
							System.out.println("Your word has special characters in it!");
						else
							System.out.println("The word is over 20 characters!");
						exception = true;
					}
				} while (exception == true || specialword == true && choice.equals("A"));
			}
			// =====================================================================================================================================//
			System.out.println("Now... Time for magic...");
			System.out.print("\033[H\033[2J");
			Thread.sleep(1000);
			PrintingWord(word, spaces, letter, guessedwords, numfirstspaces, charguesses);// prints
																				// out
			// word
			System.out.println();
			while (correct == false && charscorrect2 < (word.length() - spaces - numfirstspaces) && chances > 0) {
				correctletter = false;
				charscorrect = 0;
				do {
					System.out.println("Please guess a character from the word.(A~Z, 1~9)");
					guess = scanner.next().toUpperCase();
					try {
							if (guess.length() != 1) {
								System.out.println("You can enter one character at a time!");
								specialchar = true;
							} else {
								Pattern p = Pattern.compile("[\\p{Alpha}]*[\\p{Punct}][\\p{Alpha}]*");
								Matcher m = p.matcher(guess);
								specialchar = m.matches();
								if (specialchar == true)
									System.out.println("That character is not A~Z or 1~9!");
							}
							for (int a = 0; a < charguesses.length; a++) {
							if (charguesses[a].equals(guess)) {
								System.out.println("You already guessed that character!");
								specialchar = true;
							}
						}
					} catch (NullPointerException e) {
						
					}

				} while (specialchar == true);
				try{
				charguesses[counterz] = guess;
				Arrays.sort(charguesses);
				++counterz;
				}catch(NullPointerException e){}

				for (int z = 0; z < word.length() - numfirstspaces; z++) {
					if (guess.equals(letter[z].toUpperCase()) && guessedwords[z] == false) {
						guessedwords[z] = true;
						correctletter = true;
						++charscorrect;
						++charscorrect2;
					}
				}
				if (correctletter == false) {
					--chances;
					System.out.printf(ANSI_RED + "Incorrect! You have %d chances left%n" + ANSI_RESET, chances);
					DisplayHangman(chances);
				} else {
					System.out.printf(ANSI_GREEN + "You guessed %d characters right!%n" + ANSI_RESET, charscorrect);
				}
				PrintingWord(word, spaces, letter, guessedwords, numfirstspaces, charguesses);
				if (charscorrect2 >= (word.length() - spaces - numfirstspaces))
					System.out.println(ANSI_GREEN + "You won the game!" + ANSI_RESET);
				else if (chances <= 0) {
					System.out.print(ANSI_RED + "You lost the game: The word was: " + ANSI_RESET);
					for (int y = 0; y < word.length(); y++)
						if (!letter[y].equals("_"))
							System.out.print(letter[y]);
				}
				System.out.println();
				++counterz;
			}
			do {
				System.out.println("Would you like to countinue playing? Y/N");
				keepplaying = scanner.next().toUpperCase();
			} while ("NY".indexOf(keepplaying) == -1);
			Thread.sleep(1000);
		}
	}

	private static void PrintingWord(String word, int spaces, String[] letter, boolean guessedwords[], int numfirstspaces, String charguesses[]) {
		System.out.println("========================");

		for (int y = 0; y < word.length() - numfirstspaces; y++) {
			if (letter[y].equals("_")) {
				System.out.print("  ");
			} else {
				if (guessedwords[y] == false)
					System.out.print("_");
				else
					System.out.print(letter[y]);
			}
		}
		System.out.println(" - " + (word.length() - spaces - numfirstspaces) + " characters");
		System.out.println("========================");
		try{
			System.out.println("Guessed Characters: ");
		for (int a = 0; a < charguesses.length; a++) {
			if(charguesses[a]!=null)
		System.out.print(charguesses[a].toUpperCase() + ", ");
		}
		System.out.println();
		}catch(NullPointerException e){}
	}

	private static void DisplayHangman(int chances) throws InterruptedException {
		System.out.print("Chances: ");
		for (int x = 0; x < chances; x++)
			System.out.print("√ ");
		System.out.println();
		Thread.sleep(1000);
		System.out.println(ANSI_RED + "		  Hangman:   " + ANSI_RESET);
		if (chances <= 7)
			System.out.println(ANSI_RED + "		 _________     " + ANSI_RESET);
		if (chances <= 6)
			System.out.println(ANSI_RED + "		|         |    " + ANSI_RESET);
		if (chances <= 5)
			System.out.println(ANSI_RED + "		|         0    " + ANSI_RESET);
		if (chances <= 4)
			System.out.println(ANSI_RED + "		|         |    " + ANSI_RESET);
		if (chances <= 3)
			System.out.println(ANSI_RED + "		|        /|\\  " + ANSI_RESET);
		if (chances <= 2)
			System.out.println(ANSI_RED + "		|        / \\  " + ANSI_RESET);
		if (chances <= 1)
			System.out.println(ANSI_RED + "		|              " + ANSI_RESET);
		if (chances <= 0)
			System.out.println(ANSI_RED + "		|              " + ANSI_RESET);
		Thread.sleep(2000);
		System.out.print("\033[H\033[2J");
	}
}
