import java.util.Collection;
import java.util.HashMap;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/1/20
 *
 * Contains all bank data.
 */
public class BankCustomerData implements IBankDB{
    private HashMap<String, Customer> hashNameToCustomer;
    private HashMap<Integer, Customer> idToCustomer;


    // TODO(Edd1e234): Create a accountToCustomer hashmap
    public BankCustomerData(HashMap<String, Customer> hashNameToCustomer,
                            HashMap<Integer, Customer> idToCustomer) {
        this.hashNameToCustomer = hashNameToCustomer;
        this.idToCustomer = idToCustomer;
    }


    @Override
    public void addCustomer(Customer customer) {

    }

    @Override
    public Collection<Customer> getCustomers() {
        return null;
    }

    @Override
    public boolean containsCustomer(String fullName) {
        return false;
    }

    @Override
    public Customer getCustomer(String fullName) {
        return null;
    }

    @Override
    public boolean containsChecking(int accountNumber) {
        return false;
    }

    @Override
    public boolean containsSavings(int accountNumber) {
        return false;
    }

    @Override
    public boolean containsCredit(int accountNumber) {
        return false;
    }

    @Override
    public Account getAccount(int accountNumber) {
        return null;
    }

    @Override
    public void addTransaction(Transaction transaction) {

    }

    @Override
    public Collection<Transaction> getTransactions() {
        return null;
    }

    @Override
    public Collection<Transaction> getTransactions(Customer customer) {
        return null;
    }

    @Override
    public BankStatement getBankStatement(Customer customer) {
        return null;
    }
}
