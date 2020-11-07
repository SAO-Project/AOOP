
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
		return (this.getClass().getName() + "\n" +
			"Account number: " + number + "\n" +
			"Account balance: " + String.format("$%.2f", balance)
		);
	}
}