package app;

import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

/**
 * @author Alex
 * @since 11/05/2020
 *
 *
 * // TODO(Alex): Can you remove the getClass() call, replace it with a more
 * getAccount type call?
 * This class stores a transaction and it will return the transaction in a printable format for the user and
 * for the transaction
 */
public class Transaction implements Printable {
	private final Customer srcCustomer;
	private final Account srcAccount;
	private final Customer destCustomer;
	private final Account destAccount;
	private final double amount;
	private final String action;
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
	
	public Optional<Account> getDestAccount() {
		return destAccount.getOptional();
	}
	
	public Optional<Account> getSrcAccount() {
		return srcAccount.getOptional();
	}
	
	public Optional<Customer> getSrcCustomer() {
		return srcCustomer.getOptional();
	}
	
	public Optional<Customer> getDestCustomer() {
		return destCustomer.getOptional();
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
			srcAccount.getClass().getSimpleName() + " to" +
			destAccount.getClass().getSimpleName()
		);
	}
	
	/**
	 * formats string into user inquires account format
	 * @return returns formatted string
	 */
	private String inquiresString(){
		return (srcCustomer.getFullName() + " inquired their " +
			srcAccount.getClass().getSimpleName() + " account"
		);
	}
	
	/**
	 * formats string into user withdraws from account
	 * @return returns formatted string
	 */
	private String withdrawsString(){
		return ( srcCustomer.getFullName() + " withdrew " +
			getBalanceString() + " from " +
			srcAccount.getClass().getSimpleName() + " account"
		);
	}
	
	/**
	 * formats string into user deposits into account format
	 * @return returns formatted string
	 */
	private String depositsString(){
		return ( destCustomer.getFullName() + " deposited " +
			getBalanceString() + " into their " +
			destAccount.getClass().getSimpleName() + " account"
		);
	}
	
	/**
	 * gets the amount into a String format
	 * @return returns formatted amount
	 */
	private String getBalanceString(){
		return String.format("$%.2f", amount);
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
	 * Writes formatted string into the given writer
	 * @param writer the writer in which the string is going to
	 * @throws IOException throws an exception if there is a problem where is going to be written.
	 */
	@Override
	public void write(Writer writer) throws IOException {
		writer.write(getString() + "\n");
	}
}
