import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.ArrayList;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/1/20
 *
 * Contains all bank data.
 */
public class BankCustomerData implements IBankDB{
    private HashMap<String, Customer> nameToCustomer;
    private HashMap<Integer, Customer> idToCustomer;
    private HashMap<Integer, Customer> accountNumberToCustomer;
    private HashMap<Integer, Account> accountNumberToAccount;
    private ArrayList<Transaction> transactions;
    private HashMap<Customer, ArrayList<Transaction>> customerToTransactions;

    /**
     * Creates a object to contain all bank data.
     *
     */
    public BankCustomerData() {
        this.nameToCustomer = new HashMap<>();
        this.idToCustomer = new HashMap<>();
        this.transactions = new ArrayList<>();
        this.customerToTransactions = new HashMap<>();
    
        
        
        createAccountNumberToCustomerHashMap(this.nameToCustomer);
    }

    @Override
    /**
     * Assumes customer is valid.
     * Will not add.
     */
    public void addCustomer(Customer customer) {
        // TODO(Eddie): Remove this.
        if (containsCustomer(customer.getFullName())) {
            // TODO(Alex): Should we throw here if we try to add a customer
            //  that's already present. Its an edge case...
            System.out.println("Customer already active!");
            return;
        }
        this.nameToCustomer.put(customer.getFullName(), customer);
        this.idToCustomer.put(customer.getId(), customer);
        this.customerToTransactions.put(customer, new ArrayList<>());
        addCustomerToAccountNumberToCustomerMap(customer);
    }

    @Override
    public Collection<Customer> getCustomers() {
        return idToCustomer.values();
    }

    @Override
    public boolean containsCustomer(String fullName) {
        return this.nameToCustomer.containsKey(fullName);
    }

    @Override
    public Optional<Customer> getCustomer(String fullName) {
        if (nameToCustomer.containsKey(fullName)) {
            return Optional.of(nameToCustomer.get(fullName));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Customer> getCustomer(int id) {
        if (idToCustomer.containsKey(id)) {
            return Optional.of(idToCustomer.get(id));
        }
        return Optional.empty();
    }

    @Override
    public boolean containsChecking(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public boolean containsSavings(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public boolean containsCredit(int accountNumber) {
        return getAccount(accountNumber).isPresent();
    }

    @Override
    public boolean containsAccountNumber(int accountNumber) {
        return (containsSavings(accountNumber) ||
            containsChecking(accountNumber) ||
            containsCredit(accountNumber)
        );
    }

    @Override
    public Optional<Account> getAccount(int accountNumber) {
        if (accountNumberToAccount.containsKey(accountNumber)) {
            return Optional.of(accountNumberToAccount.get(accountNumber));
        }
        return Optional.empty();
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        Customer srcCustomer = transaction.getSrcCustomer();
        Customer destCustomer = transaction.getDestCustomer();
        
        if(srcCustomer != null){
            customerToTransactions.get(srcCustomer).add(transaction);
        }
    
        if(destCustomer != null){
            customerToTransactions.get(destCustomer).add(transaction);
        }
    }

    @Override
    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public Collection<Transaction> getTransactions(Customer customer) {
        return customerToTransactions.get(customer);
    }

    @Override
    public BankStatement getBankStatement(Customer customer) {
        return null;
    }

    /**
     * Creates class attribute accountNumberToCustomer.
     *
     * Adds all customers to upon object initialization.
     * @param idToCustomer Uses data to populate map.
     */
    public void createAccountNumberToCustomerHashMap(
            HashMap<String, Customer> idToCustomer) {
        this.accountNumberToCustomer = new HashMap<>();
        this.accountNumberToAccount = new HashMap<>();

        // Adds account number to map if account active...
        for (Customer customer : idToCustomer.values()) {
            addCustomerToAccountNumberToCustomerMap(customer);
        }
    }

    /**
     * Used to add customer to accountNumberToCustomerMap class attribute.
     *
     * @param customer Customer to add.
     */
    private void addCustomerToAccountNumberToCustomerMap(Customer customer) {
        if (customer.getChecking().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getChecking().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getChecking().getNumber(), customer.getChecking());
        }

        if (customer.getSavings().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getSavings().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getSavings().getNumber(), customer.getSavings());
        }

        if (customer.getCredit().getIsActive()) {
            this.accountNumberToCustomer.put(
                    customer.getCredit().getNumber(), customer);
            this.accountNumberToAccount.put(
                    customer.getCredit().getNumber(), customer.getCredit());
        }
    }

    public int getNextId() {
        return idToCustomer.values().size() +1;
    }
}
