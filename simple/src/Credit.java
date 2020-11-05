/**
 * @author Alex Avila
 * @version 1.0
 * @since 9/28/20
 * <p>
 * A type of account where the user cannot withdraw and only can deposit until dept is pay off
 */
public class Credit extends Account{
	private int maxCredit;
	
	/**
	 * Default constructor
	 */
	public Credit(){
		super();
		this.accountType = AccountType.CREDIT;
	}
	
	/**
	 * Credit constructor with Account parameters
	 * @param number Account number of the credit card. Always starts with digit 3
	 * @param balance current balance of the credit card which is negative because it is dept
	 */
	public Credit(int number, double balance){
		super(number, balance);
		this.accountType  = AccountType.CREDIT;
	}
	
	/**
	 * This constructor accepts all the attributes used in PA3
	 * @param number Account number of the credit card. Always starts with digit 3
	 * @param balance current balance of the credit card which is negative because it is dept
	 * @param maxCredit the limit of money that the user can borrow.
	 */
	public Credit(int number, double balance, int maxCredit){
		super(number, balance);
		this.maxCredit = maxCredit;
		this.accountType = AccountType.CREDIT;
	}
	
	/**
	 * Does deposit to credit if it is successful then return false
	 * @param amount the amount of money the user wants to deposit into the account
	 */
	@Override
	public void deposit(double amount) throws RuntimeException{
		// not negative amount allowed
		if(amount < 0){
			throw new RuntimeException("No negative numbers allowed");
		}
		
		// cannot pay more than debt
		if(balance + amount > 0){
			throw new RuntimeException("Cannot pay more than debt");
		}
		
		// deposit money
		balance += amount;
	}
	
	/**
	 * Cannot withdraw from credit card
	 * @param amount the amount of money the user wants to withdraw cannot be more than balance
	 */
	@Override
	public void withdraw(double amount){
		throw new RuntimeException("No withdraw allowed from credit account");
	}

	public void setCreditMax(int maxCredit) {
		this.maxCredit = maxCredit;
	}
}