/**
 * @author Alex
 * @since 11/05/2020
 *
 * <p>
 * This class stores a transaction and it will return the transaction in a printable format for the user and
 * for the transaction
 * <p/>
 */
public class Transaction implements Printable {
	private Customer srcCustomer;
	private Account srcAccount;
	private Customer destCustomer;
	private Account destAccount;
	private double amount;
	private String action;
	private String date;
	
	/**
	 * Constructor with all the information for any kind of transaction
	 * @param srcCustomer Customer where money is coming from
	 * @param srcAccount Account where money is coming from
	 * @param destCustomer Customer where money is going to
	 * @param destAccount Account where money is going to
	 * @param amount amount of money that was transferred
	 * @param action what type of transaction occurred
	 */
	public Transaction(
		Customer srcCustomer,
		Account srcAccount,
		Customer destCustomer,
		Account destAccount,
		double amount,
		String action
	){
		this.srcCustomer = srcCustomer;
		this.srcAccount = srcAccount;
		this.destCustomer = destCustomer;
		this.destAccount = destAccount;
		this.amount = amount;
		this.action = action;
	}
	
	/*-----------------------------------------------------------------------------------------------------------------
	                                            Getters
	 ----------------------------------------------------------------------------------------------------------------*/
	
	public Account getDestAccount() {
		return destAccount;
	}
	
	public Account getSrcAccount() {
		return srcAccount;
	}
	
	public Customer getSrcCustomer() {
		return srcCustomer;
	}
	
	public Customer getDestCustomer() {
		return destCustomer;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getAction() {
		return action;
	}
	
	public String getDate() {
		return date;
	}
	
	/**
	 * prints the transaction converted into a String
	 * @throws RuntimeException if actions is not valid then throw an exception
	 */
	@Override
	public void print() throws RuntimeException{
		System.out.println(getString());
	}
	
	/**
	 * Formats the information of the String and makes it a readable sentence
	 * @return a String that gets the transaction information into a readable sentence
	 * @throws RuntimeException
	 */
	@Override
	public String getString() throws RuntimeException{
		switch (action){
			case "pays":
				return paysString();
			case "transfers":
				return transfersString();
			case "inquires":
				return inquiresString();
			case "withdraws":
				return withdrawsString();
			case "deposits":
				return depositsString();
			default:
				throw new RuntimeException(action + " is not a valid action");
		}
	}
	
	/**
	 * formats string into pay to person styled
	 * @return returns formatted string
	 */
	private String paysString(){
		return (srcCustomer.getFullName() + " paid " +
			getBalanceString() + " to " +
			destCustomer.getFullName()
		);
	}
	
	/**
	 * formats string into transfer from account to account format
	 * @return returns formatted string
	 */
	private String transfersString(){
		return ( srcCustomer.getFullName() + " transferred " +
			getBalanceString() + " from " +
			srcAccount.getClass().toString() + " to" +
			destAccount.getClass().toString()
		);
	}
	
	/**
	 * formats string into user inquires account format
	 * @return returns formatted string
	 */
	private String inquiresString(){
		return ( srcCustomer.getFullName() + " inquired their " +
			srcAccount.getClass().toString() + " account"
		);
	}
	
	/**
	 * formats string into user withdraws from account
	 * @return returns formatted string
	 */
	private String withdrawsString(){
		return ( srcCustomer.getFullName() + " withdrew " +
			getBalanceString() + " from " +
			srcAccount.getClass().toString() + " account"
		);
	}
	
	/**
	 * formats string into user deposits into account format
	 * @return returns formatted string
	 */
	private String depositsString(){
		return ( destCustomer.getFullName() + " deposited " +
			getBalanceString() + " into their " +
			destAccount.getClass().toString() + " account"
		);
	}
	
	/**
	 * gets the amount into a String format
	 * @return returns formatted amount
	 */
	private String getBalanceString(){
		return String.format("$%.2f", amount);
	}
}
