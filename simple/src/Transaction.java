import java.util.Optional;

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
	private Optional<Customer> srcCustomer;
	private Optional<Account> srcAccount;
	private Optional<Customer> destCustomer;
	private Optional<Account> destAccount;
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
		Optional<Customer> srcCustomer,
		Optional<Account> srcAccount,
		Optional<Customer> destCustomer,
		Optional<Account> destAccount,
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
	
	public Optional<Account> getDestAccount() {
		return destAccount;
	}
	
	public Optional<Account> getSrcAccount() {
		return srcAccount;
	}
	
	public Optional<Customer> getSrcCustomer() {
		return srcCustomer;
	}
	
	public Optional<Customer> getDestCustomer() {
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
	public String getString(){
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
				return action + " is not a valid action";
		}
	}
	
	/**
	 * formats string into pay to person styled
	 * @return returns formatted string
	 */
	private String paysString(){
		return (srcCustomer.orElseThrow().getFullName() + " paid " +
			getBalanceString() + " to " +
			destCustomer.orElseThrow().getFullName()
		);
	}
	
	/**
	 * formats string into transfer from account to account format
	 * @return returns formatted string
	 */
	private String transfersString(){
		return ( srcCustomer.orElseThrow().getFullName() + " transferred " +
			getBalanceString() + " from " +
			srcAccount.orElseThrow().getClass().getName() + " to" +
			destAccount.orElseThrow().getClass().getName()
		);
	}
	
	/**
	 * formats string into user inquires account format
	 * @return returns formatted string
	 */
	private String inquiresString(){
		return (srcCustomer.orElseThrow().getFullName() + " inquired their " +
			srcAccount.orElseThrow().getClass().getName() + " account"
		);
	}
	
	/**
	 * formats string into user withdraws from account
	 * @return returns formatted string
	 */
	private String withdrawsString(){
		return ( srcCustomer.orElseThrow().getFullName() + " withdrew " +
			getBalanceString() + " from " +
			srcAccount.orElseThrow().getClass().getName() + " account"
		);
	}
	
	/**
	 * formats string into user deposits into account format
	 * @return returns formatted string
	 */
	private String depositsString(){
		return ( destCustomer.orElseThrow().getFullName() + " deposited " +
			getBalanceString() + " into their " +
			destAccount.orElseThrow().getClass().getName() + " account"
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
