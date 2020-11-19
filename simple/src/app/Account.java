package app;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Alex
 * @since 11/9
 * @version 1
 * <p>
 * This class is the abstract class for account storing
 * the common components of Credit Savings and Checking accounts.
 * <p/>
 */
public abstract class Account implements Printable {
	protected int number;
	protected double balance;
	protected Customer customer;
	protected Boolean isActive;
	protected AccountType accountType;
	
	/**
	 * Default constructor
	 */
	public Account(){
		this.isActive = false;
	}
	
	/**
	 * Abstract constructor use to initiate the attributes that the account types will inherit
	 * @param number unique account number that varies depending on the type of account
	 * @param balance the current amount of money in this account
	 */
	public Account(int number, double balance){
		this.number = number;
		this.balance = balance;
		this.isActive = true;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Getters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public int getNumber() {
		return number;
	}
	
	public double getBalance() {
		return balance;
	}
	
	public String getBalanceString(){
		return String.format("$%.2f", balance);
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public Boolean  getIsActive() {return isActive;}

	public AccountType getAccountType() {return accountType;}
	
	/**
	 * @author Alex
	 * gets a specific String depending on what type of account this instance is
	 * @return a string describing the account
	 */
	public String getAccountTypeStr() {
		switch (accountType) {
			case CHECKING:
				return "Checking account";
			case SAVINGS:
				return "Savings account";
			case CREDIT:
				return "Credit account";
		}
		return "FAIL FAIL";
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Setters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive  = isActive;
	}

	public void activateAccount(double amount, int number) {
		setBalance(amount);
		setNumber(number);
		setIsActive(true);
	}

	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Actuators
	 ----------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Puts money into the account and it could be as much as the user wants
	 * @param amount the amount of money the user wants to deposit into the account
	 */
	public void deposit(double amount)throws RuntimeException{
		// no negative deposits
		if(amount < 0){
			throw new RuntimeException("No negative numbers accepted");
		}
		
		balance += amount;
	}
	
	/**
	 * Takes out money from this account if amount is not bigger than current balance
	 * @param amount the amount of money the user wants to withdraw cannot be more than balance
	 */
	public void withdraw(double amount) throws RuntimeException{
		// no negative deposits
		if(amount < 0){
			throw new RuntimeException("No negative numbers accepted");
		}
		
		//not enough balance to withdraw
		if(amount > balance){
			throw new RuntimeException("Not enough funds to withdraw");
		}
		
		//withdraw amount from the balance
		balance -= amount;
	}
	
	/**
	 * Transfers money from this account to a selected account
	 * @author Alex
	 * @param account the account where the money is going to
	 * @param amount the amount of money that is being transfer
	 * @throws RuntimeException throws if transfer was not successful.
	 */
	public void transfer(Account account, double amount) throws RuntimeException{
		//Grab money from this account
		this.withdraw(amount);
		
		//Deposit the money into the other account
		try{
			account.deposit(amount);
		}catch(RuntimeException e){
			//undo withdraw
			this.deposit(amount);
			throw e;
		}
	}
	
	/**
	 * Prints formatted string into the console whenever it is needed to be print
	 */
	@Override
	public void print(){
		System.out.println(getString());
	}
	
	/**
	 * Retrieves account info.
	 * @return account info in string.
	 */
	@Override
	public String getString(){
		if (!isActive) {
			return "";
		}
		return (this.getClass().getSimpleName() + "\n" +
			"Account number: " + number + "\n" +
			"Account balance: " + String.format("$%.2f", balance) + "\n"
		);
	}
	
	/**
	 * Writes the formatted string into the Writer
	 * @param writer writes the file into any subclass of a writer such as files
	 * @throws IOException throws if file or output source is not found or has a problem.
	 */
	@Override
	public void write(Writer writer)throws IOException {
		writer.write(getString() + "\n");
	}
}