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
	
	public void process(String fileLine){
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
		}
	}
	
	private void pays(String[] actions){
		
		try{
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
			
		}catch(RuntimeException e){
			String log = e.getMessage() + " in transaction: " + String.join(", ", actions);
		}
	}
	
	private void transfers(String[] actions){
		try{
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
						amount, ""));
			
		}catch (NumberFormatException e){
			System.out.println("Please enter a number");
		} catch (RuntimeException e){
			String log = e.getMessage() + " in transaction: " + String.join(", ", actions);
		}
		
	}
	
	private void inquires(String[] actions){
		//Find customer
		System.out.println(actions[0]);
		System.out.println(bank.getCustomer(actions[0] + " " + actions[1]));
		Customer customer = bank.getCustomer(actions[0] + " " + actions[1]).orElseThrow();
		
		//Add transaction
		bank.addTransaction(
			new Transaction(
				customer,
				customer.getAccountByType(actions[2]),
				new NullCustomer(),
				new NullAccount(),
				0,
				"inquires"));
	}
	
	private void withdraws(String[] actions){
		try{
			//Find account and Customer
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
		}catch (NullPointerException e){
			String log = "Failed Transaction: ";
			for(int i = 0; i < actions.length; i++){
				log += actions[i];
			}
		}catch (RuntimeException e){
			String log = e.getMessage() + " in transaction: " + String.join(", ", actions);
		}
	}
	
	private void deposits(String[] actions){
		try{
			//Get customer and account to deposit
			Customer customer = bank.getCustomer(actions[4] + " " + actions[5]).orElseThrow();
			Account account = customer.getAccountByType(actions[6]);
			double amount = Double.parseDouble(actions[7]);
			account.deposit(Double.parseDouble(actions[7]));
			
			bank.addTransaction(
				new Transaction(
					customer,
					account,
					customer,
					account,
					amount,
					"deposits"));
			
		} catch (NullPointerException e){
			String log = e.getMessage() + " in transaction: " + String.join(", ", actions);
		} catch (RuntimeException e){
			String log = e.getMessage() + " in transaction: ";
			for(int i = 0; i < actions.length; i++){
				log += actions[i];
			}
		}
	}
}
