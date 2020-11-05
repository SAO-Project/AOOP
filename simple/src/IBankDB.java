import java.util.Collection;
import java.util.Optional;

/**
 * @author Alex Avila
 * @version 1.0
 * @since 10/10/20
 * <p>
 * This interface was mostly implemented to be able to abstract the storage implementation and only cares
 * about getting the right information and being able to update and create this information.
 * <p/>
 */
public interface IBankDB {
	
	/**
	 * add a customer to the customer hash table which key should be their full name
	 * @param customer
	 */
	void addCustomer(Customer customer);
	
	/**
	 * gets the collection of customers by taking tha values of the customer hash tables.
	 * @return returns a list of customers
	 */
	Collection<Customer> getCustomers();
	
	/**
	 * Checks if the bank contains a customer of a certain name
	 * @param fullName name of the seachee customer
	 * @return returns whether if the customer is in the bank or not
	 */
	boolean containsCustomer(String fullName);
	
	/**
	 * get a customer using their respective full name if the name is in the bank
	 * @param fullName the name of the Customer
	 * @return returns the Customer of the name fullName
	 */
	Optional<Customer> getCustomer(String fullName);

	/**
	 * Get a customer using their respective
	 * @param id
	 * @return
	 */
	Optional<Customer> getCustomer(int id);
	
	/**
	 * checks if the bank contains a Checkings account
	 * @param accountNumber uses account number to query account
	 * @return returns the account of certain account number
	 */
	boolean containsChecking(int accountNumber);
	
	/**
	 * checks if the bank contains a Saving account
	 * @param accountNumber uses account number to query account
	 * @return returns the account of certain account number
	 */
	boolean containsSavings(int accountNumber);
	
	/**
	 * checks if the bank contains a Credit account
	 * @param accountNumber uses account number to query account
	 * @return returns the account of certain account number
	 */
	boolean containsCredit(int accountNumber);

	/**
	 * Checks if the bank contains this account.
	 * @param accountNumber uses account number to query account.
	 * @return returns the account of certain account number.
	 */
	boolean containsAccountNumber(int accountNumber);

	/**
	 * gets account using its account number
	 * @param accountNumber queries account using account number
	 * @return returns the respective account of the account number
	 */
	Optional<Account> getAccount(int accountNumber);
	
	/**
	 * adds a transactions that was made when the users does a transaction
	 * @param transaction the transaction information message of the Customer to be added
	 */
	void addTransaction(Transaction transaction);
	
	/**
	 * get all the transactions made by all the Customers in the bank
	 * @return return a list of all the transactions
	 */
	Collection<Transaction> getTransactions();
	
	/**
	 * get all the transactions of a certain customer
	 * @param customer the customer to get the transactions from
	 * @return return a list of all customer transactions
	 */
	Collection<Transaction> getTransactions(Customer customer);
	
	/**
	 * get the bank statement of an specific customer
	 * @param customer the bankStatemet's customer
	 * @return return the bank statement of the user.
	 */
	BankStatement getBankStatement(Customer customer);
}
