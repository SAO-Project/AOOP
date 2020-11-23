package app;

/**
 * @author Alex
 * @since 11/09
 * @version 1
 * This class handles the Transaction actions csv file
 * the actions are handle here and this uses dependency
 * inversion by only depending on an abstract interface database
 */
public class ActionReader {
	final private IBankDB bank;
	
	public ActionReader(IBankDB bank){
		this.bank = bank;
	}
	
	public void process(String fileLine) throws RuntimeException, InvalidTransaction{
		String[] actions = fileLine.split(",");
		switch (actions[3]){
			case "pays":
				pays(actions);
				break;
			case "transfers":
				transfers(actions);
				break;
			case "inquires":
				inquires(actions);
				break;
			case "withdraws":
				withdraws(actions);
				break;
			case "deposits":
				deposits(actions);
				break;
			default:
				throw new InvalidTransaction(actions[3] + " is not a valid transaction");
		}
	}
	
	private void pays(String[] actions) throws RuntimeException{
		//Find source user and account
		Customer srcCustomer = bank.getCustomer(actions[0] + " " + actions[1]).orElseThrow();
		System.out.println(srcCustomer);
		Account srcAccount = srcCustomer.getAccountByType(actions[2]);

		//Find dest user and account
		Customer destCustomer = bank.getCustomer(actions[4] + " " + actions[5]).orElseThrow();
		Account destAccount = destCustomer.getAccountByType(actions[6]);

		double amount = Double.parseDouble(actions[7]);
		srcAccount.transfer(destAccount, amount);

		bank.addTransaction(
				new Transaction(
						srcCustomer,
						srcAccount,
						destCustomer,
						destAccount,
						amount, "pays"));
	}
	
	private void transfers(String[] actions) throws RuntimeException{
		//Find source user and account
		Customer srcCustomer = bank.getCustomer(actions[0] + " " + actions[1]).orElseThrow();
		Account srcAccount = srcCustomer.getAccountByType(actions[2]);

		//Find dest user and account
		Customer destCustomer = bank.getCustomer(actions[4] + " " + actions[5]).orElseThrow();
		Account destAccount = destCustomer.getAccountByType(actions[6]);

		double amount = Double.parseDouble(actions[7]);
		srcAccount.transfer(destAccount, amount);

		bank.addTransaction(
				new Transaction(
						srcCustomer,
						srcAccount,
						destCustomer,
						destAccount,
						amount,
						"transfers"));
	}
	
	private void inquires(String[] actions) throws RuntimeException{
		System.out.println(actions[0]);
		System.out.println(bank.getCustomer(actions[0] + " " + actions[1]));
		Customer customer = bank.getCustomer(actions[0] + " " + actions[1]).orElseThrow();

		bank.addTransaction(
				new Transaction(
						customer,
						customer.getAccountByType(actions[2]),
						new NullCustomer(),
						new NullAccount(),
						0,
						"inquires"));
	}
	
	private void withdraws(String[] actions) throws RuntimeException{
		Customer customer = bank.getCustomer(actions[0] + " " + actions[1]).orElseThrow();
		Account account = customer.getAccountByType(actions[2]);
		double amount = Double.parseDouble(actions[7]);
		account.withdraw(amount);

		bank.addTransaction(
				new Transaction(
						customer,
						account,
						new NullCustomer(),
						new NullAccount(),
						amount,
						"withdraws"));
	}
	
	private void deposits(String[] actions) throws RuntimeException{
		Customer customer = bank.getCustomer(actions[4] + " " + actions[5]).orElseThrow();
		Account account = customer.getAccountByType(actions[6]);
		double amount = Double.parseDouble(actions[7]);
		account.deposit(Double.parseDouble(actions[7]));

		bank.addTransaction(
				new Transaction(
						new NullCustomer(),
						new NullAccount(),
						customer,
						account,
						amount,
						"deposits"));
	}
}