/**
 * @author Alex Avila
 * @version 1.0
 * @since 9/27/2020
 * <p>
 * Is a person who has an ID and owns three different type of accounts for the bank
 */
public class Customer extends Person{
	private String password;
	private String email;
	private int id;
	private Checking checking;
	private Savings savings;
	private Credit credit;

	public Customer(){
		super();
	}
	
	/**
	 * @param firstName Customer's first name inherited from Person class
	 * @param lastName Customer's last name inherited from Person class
	 * @param dob Customer's date of birth as a String inherited from Person class
	 * @param address Customer's address inherited from Person class
	 * @param phone Customer's phone number inherited from Person class
	 * @param id Customer's identification number
	 * @param checking Customer's checkings account which is a Checking instance
	 * @param savings Customer's savings account which is a Savings instance
	 * @param credit Customer's credit account which is a Credit instance
	 */
	public Customer(
		String firstName,
		String lastName,
		String dob,
		String address,
		String phone,
		int id,
		Checking checking,
		Savings savings,
		Credit credit)
	{
		super(firstName, lastName, dob, address, phone);
		this.id = id;
		this.checking = checking;
		this.savings = savings;
		this.credit = credit;
	}
	
	public Customer(
		String firstName,
		String lastName,
		String dob,
		String address,
		String phone,
		int id,
		int checkingAccountNumber,
		double checkingAmount,
		int savingsAccountNumber,
		double savingsAmount,
		int creditAccountNumber,
		double creditAmount,
		int maxCredit
	) {
		super(firstName, lastName, dob, address, phone);
		this.id = id;
		this.checking = new Checking(
			checkingAccountNumber,
			checkingAmount
		);
		this.checking.setCustomer(this);
		this.savings = new Savings(
			savingsAccountNumber,
			savingsAmount
		);
		this.savings.setCustomer(this);
		this.credit = new Credit(
			creditAccountNumber,
			creditAmount,
			maxCredit
		);
		this.credit.setCustomer(this);
	}

	/**
	 * Used for file util.
	 */
	public Customer(String firstName, String lastName, String dob, int id,
					String address, String phoneNumber, String email,
					String password, Checking checkingAccount,
					Credit creditAccount, Savings savingsAccount) {
		super(firstName, lastName, dob, address, phoneNumber);
		this.id = id;
		this.checking = checkingAccount;
		this.credit = creditAccount;
		this.savings = savingsAccount;
		this.password = password;
		this.email = email;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Getters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public int getId() {
		return id;
	}
	
	public Checking getChecking() {
		return checking;
	}
	
	public Savings getSavings() {
		return savings;
	}
	
	public Credit getCredit() {
		return credit;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return firstName + " " + lastName;
	}

	/*-----------------------------------------------------------------------------------------------------------------
	                                            Setters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setChecking(Checking cheking) {
		this.checking = cheking;
	}
	
	public void setSavings(Savings savings) {
		this.savings = savings;
	}
	
	public void setCredit(Credit credit) {
		this.credit = credit;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Transfer
	 ----------------------------------------------------------------------------------------------------------------*/
	
	/**
	 * Transfers money determined by amount from source to dest account
	 * @param source Account where money is coming from to pay the destination account
	 * @param dest Account where money if going to from the source account
	 * @param amount the amount of money that is being transfer between both accounts
	 */
	public void transfer(Account source, Account dest, double amount) throws RuntimeException{
		source.transfer(dest, amount);
	}
	
	/**
	 * transfer money from this customer's checking account and sends it to the dest customer checking
	 * account
	 * @param dest Destination account that money will be going to
	 * @param amount Amount of money that is going to be transfer
	 */
	public void paySomeone(Customer dest, double amount) throws RuntimeException{
		//get checking account from customer
		transfer(checking, dest.getChecking(), amount);
	}
	
	Account getAccountByType(String type) throws RuntimeException{
		switch(type){
			case "Savings":
				return savings;
			case "Checking":
				return checking;
			case "Credit":
				return credit;
			default:
				throw new RuntimeException("not a valid account type");
		}
	}
	
	/**
	 * Formats the user information into csv style
	 * @return String with the Customers information formatted to be a line of the CSV file
	 */
	public String toCsvLine(){
		
		String checkingNumber = "";
		String checkingBalance = "";
		if(checking != null){
			checkingNumber = checking.getNumber() + "";
			checkingBalance = String.format("%.2f", checking.getBalance()) + "";
		}
		
		String savingsNumber = "";
		String savingsBalance = "";
		if(savings != null){
			savingsNumber =  savings.getNumber() + "";
			savingsBalance = String.format("%.2f", savings.getBalance()) + "";
		}
		
		String creditNumber = "";
		String creditBalance = "";
		if(credit != null){
			creditNumber =  credit.getNumber() + "";
			creditBalance = String.format("%.2f", credit.getBalance()) + "";
		}
		
		return firstName + "," + lastName + "," + dob + "," + id + "," +
			address + "," + phone + "," + checkingNumber + "," + savingsNumber + "," +
			creditNumber + "," + checkingBalance + "," + savingsBalance + "," +
			creditBalance;
	}

	public String customerInfoString() {
		return  "Costumer Information:\n" +
				"Full Name: " + firstName + " " + lastName + "\n" +
				"Date of birth: " + dob + "\n" +
				"Address: " + address + "\n" +
				"Phone: " + phone + "\n" +
				"ID: " + id + "\n";
	}
	
	/**
	 * converts information of the Customer into a formatted String
	 * @return returns a String with all the Customer's information
	 */
	@Override
	public String toString() {
		//Convert Checking to String
		String checkingString;
		try{
			checkingString = "Checking:\n" +
				"\tAccount Number: " + checking.getNumber() + "\n" +
				"\tBalance: $" + String.format("%.2f", checking.getBalance()) + "\n";
		}catch(NullPointerException e){
			checkingString = "";
		}
		
		//Convert Savings to String
		String savingsString;
		try{
			savingsString = "Savings:\n" +
				"\tAccount Number: " + savings.getNumber() + "\n" +
				"\tBalance: $" + String.format("%.2f", savings.getBalance()) + "\n";
		}catch(NullPointerException e){
			savingsString = "";
		}
		
		//Convert Checking to String
		String creditString;
		try{
			creditString = "Credit:\n" +
				"\tAccount Number: " + credit.getNumber() + "\n" +
				"\tBalance: $" + String.format("%.2f", credit.getBalance()) + "\n";
		}catch(NullPointerException e){
			creditString = "";
		}

		return customerInfoString() +
			checkingString +
			savingsString +
			creditString;
	}
}