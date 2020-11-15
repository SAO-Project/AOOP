package app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Alex Avila
 * @since 11/01/20
 * @version 1
 * This class is the menu of when the user ask to read a file.
 */
public class TransactionFileMenu {
	private Scanner scanner;
	private ActionReader actionReader;
	
	/**
	 * Constructor.
	 * @param bank bank interface dependency
	 * @param scanner Scanner to read from dependency
	 */
	public TransactionFileMenu(IBankDB bank, Scanner scanner){
		this.scanner = scanner;
		actionReader = new ActionReader(bank);
	}
	
	/**
	 * Ask for the file name
	 */
	public void askForFileName(){
		System.out.println("Type -1 to exit");
		System.out.print("Enter file name: ");

		String fileName = scanner.nextLine();

		if (fileName.equals("-1")) {
			return;
		}

		File transactionFile =
				new File(System.getProperty("user.dir") + "\\" + fileName);
		Scanner fileScanner;
		try{
			fileScanner = new Scanner(transactionFile);
		}catch (FileNotFoundException e){
			System.out.println("file not found please try again");
			askForFileName();
			return;
		}
		
		fileScanner.nextLine();
		while(fileScanner.hasNextLine()){
			actionReader.process(fileScanner.nextLine());
		}
	}
}
