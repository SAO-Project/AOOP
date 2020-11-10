import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TransactionFileMenu {
	private Scanner scanner;
	private ActionReader actionReader;
	
	public TransactionFileMenu(IBankDB bank, Scanner scanner){
		this.scanner = scanner;
		actionReader = new ActionReader(bank);
	}
	
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
