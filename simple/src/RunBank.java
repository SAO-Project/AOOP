import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/2/20
 *
 * Runs Bank
 */
public class RunBank {
    private final static String EXIT = "exit";
    private final static String INVALID_INPUT = "Could not read input";
    final private static String SELECT_ONE = "Please select one";
    final private static String PATH_TO_FILE = "simple/Bank_4_users.csv";

    public static void main(String[] args) {
        IBankDB bankCustomerData = FileUtil.readFile(PATH_TO_FILE);
        userOrBankManager(bankCustomerData);
    
        //Write log file
        try {
            FileWriter myWriter = new FileWriter("transactions.txt");
        
            //First line of the output file
            myWriter.write("Disney Bank transaction file\n");
        
            //gets transaction from iterable
        
            for(Transaction transaction : bankCustomerData.getTransactions()){
                myWriter.write(transaction.getString() + "\n");
            }
        
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    /**
     * Runs main menu.
     *
     * @param bankCustomerData
     * @return
     */
    private static ArrayList<String> userOrBankManager(IBankDB bankCustomerData) {
        ArrayList<String> logs = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenuNewLine(new String[]{
                    "1. Customer",
                    "2. Create Customer",
                    "3. Bank Manager",
                    "4. Read Transaction File",
                    "5. Exit"
            });

            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        logs.addAll(accessCustomer(bankCustomerData));
                        break;
                    case 2:
                        logs.addAll(
                                attemptToAddCustomerToSystem(bankCustomerData));
                        break;
                    case 3:
                        bankManager(bankCustomerData);
                        break;
                    case 4:
                        TransactionFileMenu transactionFileMenu = new TransactionFileMenu(bankCustomerData, scanner);
                        transactionFileMenu.askForFileName();
                        break;
                    case 5:
                        return null;
                    default:
                        System.out.println(SELECT_ONE);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<String> accessCustomer(
            IBankDB bankCustomerData) {
        ArrayList<String> logs = new ArrayList<>();
        Optional<Customer> customer = getCustomer(bankCustomerData);
        Scanner scanner = new Scanner(System.in);

        if (customer.isEmpty()) {
            return logs;
        }
        
        while (true) {
            try {
                System.out.println("Hello " + customer.get().getFirstName() +
                        " " + customer.get().getLastName());

                System.out.println("Enter password");
                System.out.println("type -1 to exit");
                String password = scanner.nextLine();

                // Exits if users asks
                if (password.equals("-1")) {
                    return logs;
                }

                // Return if empty.
                if (!password.equals(customer.get().getPassword())) {
                    continue;
                }

                // Perform customer actions
                return customerMenu(customer.get(), bankCustomerData);
            } catch (Exception e) {
                System.out.println("Failed at accessingCustomer method");
            }
        }
    }

    public static ArrayList<String> customerMenu(
        Customer customer,
        IBankDB bankCustomerData
    ) {
        ArrayList<String> logs = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);

        // Asks user for option.
        while (true) {
            try {
                displayMenuNewLine(new String[] {
                        "Select prompt",
                        "1. Inquire Balance",
                        "2. Deposit Money",
                        "3. Withdraw money",
                        "4. Transfer Money",
                        "5. Pay Someone",
                        "6. Print Logs",
                        "7. View Bank Account",
                        "8. Activate account",
                        "9. Exit"
                });

                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        inquireBalance(customer).ifPresent(logs::add);
                        break;
                    case 2:
                        depositMoney(customer).ifPresent(logs::add); 
                        break;
                    case 3:
                        withdrawMoney(customer).ifPresent(logs::add);
                        break;
                    case 4:
                        transferMoney(customer).ifPresent(logs::add);
                        break;
                    case 5:
                        paySomeone(customer, bankCustomerData).ifPresent(logs::add);
                        break;
                    case 6:
                        System.out.println("Printing logs of current session");
                        logs.forEach(System.out::println);
                        break;
                    case 7:
                        printBankAccount(customer);
                        break;
                    case 8:
                        logs.addAll(activateAccounts(customer, bankCustomerData,
                                false));
                        break;
                    case 9:
                        return logs;
                    default:
                        System.out.println("Please select one");

                }
            } catch (Exception e) {
                // TODO(Edd1e234): Fill this...
            }
        }
    }

    /**
     * Run Bank menu. Can inquire about anyone's account.
     *
     * Search for any customer and print out there info. Search for any
     * account and print out that accounts info.
     *
     * @param bankDB Contains all customer accounts
     */
    public static void bankManager(IBankDB bankDB) {
        System.out.println("Welcome Manager!");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            displayMenuNewLine(new String[] {
                    "Bank Manager Menu",
                    "1. View Customer",
                    "2. Inquire account by number",
                    "3. Print all customers",
                    "4. Exit"
            });
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        getCustomer(bankDB).ifPresent(customer ->
                                bankManagerCustomerMenu(customer, bankDB));
                        break;
                    case 2:
                        System.out.println("Enter account number");

                        // Prints out account
                        bankDB.getAccount(Integer.parseInt(scanner.nextLine()))
                                .ifPresent(Account::accountStr);
                        break;
                    case 3:
                        // Prints out all customers
                        bankDB.getCustomers().forEach(customer ->
                                System.out.println(customer.toString()));
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println(SELECT_ONE);
                }
            } catch (Exception e) {
                System.out.println(INVALID_INPUT);
            }
        }
    }

    /**
     * Handles all actions having to do with bank manager and single customer.
     *
     * @param customer         Single customer bank manager has selected.
     * @param bankDB Contains all customer data. Used to retrieve all
     *                         bank statements.
     */
    public static void bankManagerCustomerMenu(
            Customer customer, IBankDB bankDB) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(customer.getFullName() + " customer account");
        while (true) {
            try {
                displayMenuNewLine(new String[] {
                        "1. View Accounts",
                        "2. Execute BankStatement",
                        "3. Exit"
                });
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        getAccount(customer)
                                .ifPresent(account ->
                                        System.out.println(
                                                account.accountStr()));
                        break;
                    case 2:
                        // Create BankStatement
                        break;
                    case 3:
                        return;
                    default:
                        System.out.println(SELECT_ONE);
                }
            } catch (Exception e) {
                System.out.println(INVALID_INPUT);
            }
        }
    }

    /**
     * Attempt to add a customer to the system.
     *
     * @param bankCustomerData
     * @return
     */
    private static ArrayList<String> attemptToAddCustomerToSystem(
            IBankDB bankCustomerData) {
        ArrayList<String> logs = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        String firstName;
        String lastName;
        String dob;
        String address;
        String phoneNumber;
        String email;

        // Get first name.
        do {
            System.out.println("Enter First Name");
            firstName = scanner.nextLine();
            if (firstName.toLowerCase().equals(EXIT)) {
                return logs;
            }

        } while (!nameValidator(firstName));

        // Get last name
        do {
            System.out.println("Enter Last Name");
            lastName = scanner.nextLine();
            if (lastName.toLowerCase().equals(EXIT)) {
                return logs;
            }
        } while (!nameValidator(lastName));

        // Get date of birth
        do {
            System.out.println("Enter Date of Birth DD/MM/YEAR");
            System.out.print("DD: ");
            String dd = scanner.nextLine();

            if (dd.toLowerCase().equals(EXIT)) {
                return logs;
            }

            System.out.print("MM: ");
            String mm = scanner.nextLine();


            System.out.print("YEAR: ");
            String year = scanner.nextLine();

            dob = dd + "/" + mm + "/" + year;

        } while (!dateOfBirthValidator(dob));

        do {
            System.out.println("Enter email");
            email = scanner.nextLine();

            if (email.toLowerCase().equals(EXIT)) {
                return logs;
            }
        } while (!emailValidator(email));

        // Get Address
        while (true) {
            System.out.println("Enter address");
            address = scanner.nextLine();

            if (address.toLowerCase().equals(EXIT)) {
                return logs;
            }

            System.out.println("Confirm address: " + "\n\t" + address);
            displayMenuNewLine(new String[]{
                    "1. Yes",
                    "2. No"
            });
            try {
                int answer = Integer.parseInt(scanner.nextLine());

                if (answer == 1) {
                    break;
                }
                // TODO(Edd1e234) Check this at some point!
                if (answer != 2) {
                    System.out.println(INVALID_INPUT);
                }
                System.out.println("Restarting address input");
            } catch (Exception exception) {
                System.out.println("Restarting address input");
            }
        }
        // Get Phone number
        while (true) {
            System.out.println("Enter Phone number (9xxxxxxxxx)");
            phoneNumber = scanner.nextLine();
            if (phoneNumber.toLowerCase().equals(EXIT)) {
                return new ArrayList<>();
            }
            if (phoneNumberValidator(phoneNumber)) {
                phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " +
                        phoneNumber.substring(3, 6) + "-"
                        + phoneNumber.substring(6, 10);
                break;
            } else {
                System.out.println("Failed to validate phone number\n" +
                        "Try again.");
            }
        }

        Customer customer = new Customer(
                firstName,
                lastName,
                dob,
                bankCustomerData.getNextId(),
                phoneNumber,
                email,
                address,
                generatePassword(firstName, lastName),
                new Checking(),
                new Credit(),
                new Savings());
        if (bankCustomerData.containsCustomer(customer.getFirstName())) {
            System.out.println("Customer already in system... EXISTING");
            return new ArrayList<>();
        }
        System.out.println(bankCustomerData.getNextId());
        logs.addAll(activateAccounts(customer, bankCustomerData,
                true));

        // Add customer
        bankCustomerData.addCustomer(customer);

        // Add customer to the system.
        logs.add(customer.getFullName() + " Added to the system");


        System.out.println("\nID: " + customer.getId());
        System.out.println("Password: " + customer.getPassword());
        return logs;
    }

    /**
     * Inquires balance for customer.
     * @param customer Retrieves account from customer.
     * @return Logs of action.
     */
    public static Optional<String> inquireBalance(Customer customer) {
        System.out.println("Select account to inquire");
        Optional<Account> account = getAccount(customer);
        if (account.isPresent()) {
            System.out.println(account.get().accountStr());
            return Optional.of(customer.getFullName() + " inquires " +
                    account.get().getAccountTypeStr() + " balance.");
        }
        System.out.println("Failed to locate");
        return Optional.empty();
    }

    /**
     * Deposits money into desired account.
     * @param customer Customer to deposit money into.
     * @return If empty failed to deposit, else successful.
     */
    public static Optional<String> depositMoney(Customer customer) {
        System.out.println("Select account deposit into");
        Optional<Account> account = getAccount(customer);
        if (account.isEmpty()) {
            return Optional.empty();
        }

        Optional<Double> amountOfMoneyToDeposit = getAmountOfMoney("Get " +
                "amount of money to deposit");
        if (amountOfMoneyToDeposit.isEmpty()) {
            return Optional.empty();
        }

        try {
            account.get().deposit(amountOfMoneyToDeposit.get());
            System.out.println("Success!");
            // TODO (Alex): Are you okay with this message
            return Optional.of(customer.getFullName() + " deposits money into "
                    + account.get().getAccountTypeStr());
        } catch (Exception e) {
            // Prints out failure message.
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Withdraws money from account
     * @param customer Customer to withdraw money from.
     * @return If empty failed to withdraw, else successful.
     */
    public static Optional<String> withdrawMoney(Customer customer) {
        System.out.println("Select account to withdraw from");
        Optional<Account> account = getAccount(customer);
        if (account.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Double> amountToWithdraw = 
                getAmountOfMoney("Enter amount to withdraw");
        if (account.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            account.get().withdraw(amountToWithdraw.get());
            System.out.println("Success!");
            return Optional.of(customer.getFullName() + " withdraws from "
                    + account.get().getAccountTypeStr());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<String> transferMoney(Customer customer) {
        System.out.println("Select account to transfer from");
        Optional<Account> sourceAccount = getAccount(customer);
        if (sourceAccount.isEmpty()) {
            return Optional.empty();
        }

        System.out.println("Select account to transfer to");
        Optional<Account> destAccount = getAccount(customer);
        if (destAccount.isEmpty()) {
            return Optional.empty();
        }

        if (destAccount.get().equals(sourceAccount.get())) {
            System.out.println("Failed... same account selected");
            return Optional.empty();
        }

        Optional<Double> amountToTransfer = getAmountOfMoney("Enter amount to" +
                " transfer");
        if (amountToTransfer.isEmpty()) {
            return Optional.empty();
        }

        try {
            customer.transfer(sourceAccount.get(), destAccount.get(),
                    amountToTransfer.get());
            System.out.println("Success!");
            return Optional.of(customer.getFullName() + " transfers from " +
                    sourceAccount.get().getAccountTypeStr() + " to " +
                    destAccount.get().getAccountTypeStr());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Transfers money
     * @param customer Customer to pay money from.
     * @param bankCustomerData Used to retrieve customer to pay money to.
     * @return If empty failed to pay.
     */
    public static Optional<String> paySomeone(
        Customer customer,
        IBankDB bankCustomerData
    ) {
        System.out.println("Select customer to pay");
        Optional<Customer> toCustomer = getCustomer(bankCustomerData);
        if (toCustomer.isEmpty()) {
            return Optional.empty();
        }

        Optional<Double> amountToTransfer = getAmountOfMoney("Enter amount of" +
                " money to transfer");
        if (amountToTransfer.isEmpty()) {
            return Optional.empty();
        }

        try {
            customer.paySomeone(toCustomer.get(), amountToTransfer.get());
            System.out.println("Success");
            return Optional.of(customer.getFullName() + " paid " +
                    toCustomer.get().getFullName());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Prompts user about getting customer info.
     *
     * @param bankCustomerData Contains all bank data.
     * @return If empty user did not wish to select customer account, else
     * contains customer.
     */
    public static Optional<Customer> getCustomer(IBankDB bankCustomerData) {
        Scanner scanner = new Scanner(System.in);
        Optional<Customer> customer;
        while (true) {
            displayMenuNewLine(new String[]{
                    "1. Enter Customer Id",
                    "2. Enter Full Name",
                    "3. Exit"
            });
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        System.out.println("Enter Id: ");
                        int customerId = Integer.parseInt(scanner.nextLine());

                        customer = bankCustomerData.getCustomer(customerId);

                        // Check if customer is present. If not prompt user
                        // again.
                        if (customer.isEmpty()) {
                            System.out.println("Did not find customer account");
                            break;
                        }
                        return customer;
                    case 2:
                        System.out.println("Enter: Name");

                        customer =
                                bankCustomerData.getCustomer(scanner.nextLine());

                        // Check if customer is present. If not prompt user
                        // again.
                        if (customer.isEmpty()) {
                            System.out.println("Did not find customer account");
                            break;
                        }
                        return customer;
                    case 3:
                        return Optional.empty();
                    default:
                        System.out.println("Please select one of the two.");
                }
            } catch (Exception exception) {
                // TODO(Edd1e234): Make this functionally work better.
                System.out.println("ERROR AT getCustomerFromHashMapV2" +
                        "(TRY AGAIN)");
            }
        }
    }

    /**
     * Finds and returns either Checking, Savings, or Credit account. Loops till
     * user finds the correct account or exits upon user request.
     *
     * @param customer Contains account to access.
     * @return Account user asks or any empty optional object.
     */
    public static Optional<Account> getAccount(Customer customer) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenuNewLine(new String[]{
                    "1. Checking Account",
                    "2. Saving Account",
                    "3. Credit Account",
                    "4. Type -1 to EXIT"
            });

            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        if (customer.getChecking().getIsActive()) {
                            return Optional.of(customer.getChecking());
                        }
                        System.out.println("You do not have an active Checking" +
                                " Account");
                        break;
                    case 2:
                        if (customer.getSavings().getIsActive()) {
                            return Optional.of(customer.getSavings());
                        }
                        System.out.println("You do not have an active Savings" +
                                " Account");
                        break;
                    case 3:
                        if (customer.getCredit().getIsActive()) {
                            System.out.println("Credit account...");
                            return Optional.of(customer.getCredit());
                        }
                        System.out.println("You do not have an active Credit" +
                                " Account");
                        break;
                    case -1:
                        return Optional.empty();
                    default:
                        System.out.println("Please select one. ");
                }
            } catch (Exception exception) {
                System.out.println("Could not read input.");
            }
        }
    }

    /**
     * Best way to parse money amounts across the platform.
     *
     * @param message if failure to parse print this.
     * @return Optional empty if user prefers to exit. Else return amount.
     */
    public static Optional<Double> getAmountOfMoney(String message) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayMenuNewLine(new String[]{
                    message,
                    "Type -1 to exit."
            });
            try {
                double amount = Double.parseDouble(scanner.nextLine());
                // If user wants to exit.
                if (amount == -1.0) {
                    return Optional.empty();
                }

                if (amount < 0) {
                    System.out.println("Needs to be positive value.");
                    continue;
                }
                return Optional.of(amount);
            } catch (Exception exception) {

                // TODO(Edd1e234): Clean this us, make error message generic.
                System.out.println("Could not read input please try again.");
            }
        }
    }

    // Util methods
    public static void displayMenuNewLine(String[] prompts) {
        Arrays.stream(prompts).forEach(System.out::println);
    }

    /**
     * Prints out Bank info for customer
     * @param customer
     */
    private static void printBankAccount(Customer customer) {
        System.out.println(customer.getFullName());
        System.out.println(customer.getId());

        if (customer.getChecking().getIsActive()) {
            System.out.println(customer.getChecking().accountStr());
        }

        if (customer.getSavings().getIsActive()) {
            System.out.println(customer.getSavings().accountStr());
        }

        if (customer.getCredit().getIsActive()) {
            System.out.println(customer.getSavings().accountStr());
        }
    }


    /**
     * Activate customers
     *
     * @param customer                             Creates account for this
     *                                             account.
     * @param bankCustomerData                     Contains customer data used
     *                                             to create a new account.
     * @param activateAtLeastOneAccountIsNecessary If this is activate at least
     *                                             one account. Will not allow
     *                                             the user to exit.
     * @return Returns logs containing all actions.
     */
    public static ArrayList<String> activateAccounts(
            Customer customer, IBankDB bankCustomerData,
            boolean activateAtLeastOneAccountIsNecessary) {

        ArrayList<String> logs = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        String alreadyActive = "Already Active";
        while (true) {
            displayMenuNewLine(new String[]{
                    "Activate accounts",
                    "1. Checking",
                    "2. Saving",
                    "3. Credit",
                    "4. Exit"
            });
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        // TODO(Edd1e234): Check this funtionality
                        if (customer.getChecking().getIsActive()) {
                            System.out.println(alreadyActive);
                            break;
                        }
                        // Prints and adds to logs action
                        setAccountInfo(
                                customer.getChecking(), bankCustomerData)
                                .ifPresent(System.out::println);
                        logs.add("Checking account was activated");
                        break;
                    case 2:
                        if (customer.getSavings().getIsActive()) {
                            System.out.println(alreadyActive);
                            break;
                        }
                        // Prints and adds to logs action
                        setAccountInfo(
                                customer.getSavings(), bankCustomerData)
                                .ifPresent(System.out::println);
                        logs.add("Saving Account was activated");
                        break;
                    case 3:
                        if (customer.getCredit().getIsActive()) {
                            System.out.println(alreadyActive);
                            break;
                        }
                        // Prints and adds to logs action
                        setCreditAccount(
                                customer.getCredit(), bankCustomerData)
                                .ifPresent(System.out::println);
                        logs.add("Credit Account was activated");
                        break;
                    case 4:
                        // In the case that at least one account
                        if (activateAtLeastOneAccountIsNecessary) {
                            if (customer.getChecking().getIsActive() ||
                                    customer.getSavings().getIsActive() ||
                                    customer.getCredit().getIsActive()) {
                                return logs;
                            }
                            System.out.println("Need to activate at least " +
                                    "one account!");
                            break;
                        }
                        return logs;
                    default:
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                System.out.println(INVALID_INPUT);
            }
        }
    }

    /**
     * Meant to only be used for checking and savings account.
     * <p>
     * Credit accounts are set separately.
     *
     * @param account          Needs to be Savings or Checking account.
     * @param bankCustomerData Used to validate the account number setting.
     */
    public static Optional<String> setAccountInfo(
            Account account, IBankDB bankCustomerData) {
        int accountNumberType = 1000;
        if (account.getAccountType() == AccountType.SAVINGS) {
            accountNumberType = 2000;
        }

        int accountNumber = generateAccountNumber(
                bankCustomerData, accountNumberType);

        // Set starting balance!
        // TODO(Edd1e234): Make sure this works properly
        Optional<Double> startingBalance;
        while (true) {
            try {
                // Change in accounts
                if (account.getAccountType() == AccountType.SAVINGS) {
                    startingBalance = getAmountOfMoney("Enter starting " +
                            "balance");
                } else {
                    startingBalance = getAmountOfMoney("Enter Checking " +
                            "balance");
                }
                if (startingBalance.isEmpty()) {
                    return Optional.empty();
                }
                break;
            } catch (Exception exception) {
                System.out.println(INVALID_INPUT);
            }
        }
        account.setBalance(startingBalance.get());
        account.setNumber(accountNumber);
        account.setIsActive(true);
        return Optional.of("Account activated\n");
    }

    /**
     * Credit is unique, needs to be settled differently.
     *
     * @param creditAccount    The actual credit account.
     * @param bankCustomerData Contains all customer data necessary.
     * @return Logging info.
     */
    public static Optional<String> setCreditAccount(
            Credit creditAccount, IBankDB
            bankCustomerData) {
        Scanner scanner = new Scanner(System.in);

        // Generates an account number necessary.
        int accountNumber =
                generateAccountNumber(bankCustomerData, 3000);

        // Generates starting balance!
        Optional<Double> startingBalance =
                getAmountOfMoney("Enter starting balance(Enter as a " +
                        "positive will convert to negative)");

        if (startingBalance.isEmpty()) {
            return Optional.empty();
        }

        int creditMax;
        while (true) {
            try {
                System.out.println("What is the credit max");
                creditMax = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception exception) {
                System.out.println(INVALID_INPUT);
            }
        }
        creditAccount.setCreditMax(creditMax);
        creditAccount.setNumber(accountNumber);
        creditAccount.setBalance(startingBalance.get() * -1);
        creditAccount.setIsActive(true);
        return Optional.of("Account activated\n");
    }

    /**
     * Generates account number.
     *
     * @param bankCustomerData Used to get next available account number and
     *                         check if the next available account number is not
     *                         already set.
     * @return New account number.
     */
    public static int generateAccountNumber(
            IBankDB bankCustomerData,
            int accountNumberType) {
        int accountNumber = accountNumberType + bankCustomerData.getNextId();
        if (bankCustomerData.containsAccountNumber(accountNumber)) {
            System.out.println("Something has gone wrong. Account logic" +
                    " has failed");
        }
        return accountNumber;
    }


    /**
     * Validates name.
     *
     * @param name Name to validate.
     * @return If valid.
     */
    public static boolean nameValidator(String name) {
        return Pattern.matches("[a-zA-Z]+", name);
    }

    /**
     * Validates DOB.
     *
     * @param dob DOB to validate.
     * @return If valid or not
     */
    public static boolean dateOfBirthValidator(String dob) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(dob);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * Validates phone number.
     *
     * @param phoneNumber Number to validate.
     * @return If phone number is valid.
     */
    private static boolean phoneNumberValidator(String phoneNumber) {
        return phoneNumber.charAt(0) == '9' && phoneNumber.length() == 10
                && Pattern.matches("[0-9]+", phoneNumber);
    }

    /**
     * Validates email.
     *
     * @param email email to validate.
     * @return If email is valid.
     */
    private static boolean emailValidator(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    private static String generatePassword(String firstName, String lastName) {
        return lastName + "*" + firstName + "!987";
    }
}
