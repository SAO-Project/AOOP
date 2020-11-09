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
 * @author Alex Avila
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
        IBankDB bankCustomerData = FileUtil.readFile(askForFileName());
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
     * @param bankCustomerData Contains all customer data.
     */
    private static void userOrBankManager(IBankDB bankCustomerData) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMenuNewLine(new String[]{
                "What would you like to do today?\n",
                "\t1. Sign in as Customer",
                "\t2. Create Customer",
                "\t3. Sign in as Bank Manager",
                "\t4. Read Transaction File",
                "\t5. Exit\n"
            });

            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        accessCustomer(bankCustomerData);
                        break;
                    case 2:
                        attemptToAddCustomerToSystem(bankCustomerData);
                        break;
                    case 3:
                        bankManager(bankCustomerData);
                        break;
                    case 4:
                        TransactionFileMenu transactionFileMenu =
                                new TransactionFileMenu(
                                        bankCustomerData, scanner);
                        transactionFileMenu.askForFileName();
                        break;
                    case 5:
                        CSVFile csvFile = new CSVFile(bankCustomerData);
                        csvFile.writeCSV();
                        return;
                    default:
                        System.out.println("Please enter a valid number");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please insert a valid option");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void accessCustomer(
        IBankDB bankCustomerData
    ) {
        Optional<Customer> customer = getCustomer(bankCustomerData);
        Scanner scanner = new Scanner(System.in);

        if (customer.isEmpty()) {
            return;
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
                    return;
                }

                // Return if empty.
                if (!password.equals(customer.get().getPassword())) {
                    continue;
                }

                // Perform customer actions
                customerMenu(customer.get(), bankCustomerData);
                return;
            } catch (Exception e) {
                System.out.println("Failed at accessingCustomer method");
            }
        }
    }

    public static void customerMenu(
        Customer customer,
        IBankDB bankCustomerData
    ) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        // Asks user for option.
        while (true) {
            try {
                displayMenuNewLine(new String[] {
                        "Select prompt\n",
                        "\t1. Inquire Balance",
                        "\t2. Deposit Money",
                        "\t3. Withdraw money",
                        "\t4. Transfer Money",
                        "\t5. Pay Someone",
                        "\t6. Print Logs",
                        "\t7. View Bank Account",
                        "\t8. Activate account",
                        "\t9. Exit\n"
                });

                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        inquireBalance(customer).ifPresent(transactions::add);
                        break;
                    case 2:
                        depositMoney(customer).ifPresent(transactions::add);
                        break;
                    case 3:
                        withdrawMoney(customer).ifPresent(transactions::add);
                        break;
                    case 4:
                        transferMoney(customer).ifPresent(transactions::add);
                        break;
                    case 5:
                        paySomeone(customer, bankCustomerData)
                                .ifPresent(transactions::add);
                        break;
                    case 6:
                        System.out.println("Logs for current session");
                        transactions.forEach(
                                transaction ->
                                        System.out.println(
                                                "\t" + transaction.getString()));
                        break;
                    case 7:
                        printBankAccount(customer);
                        break;
                    case 8:
                        activateAccounts(customer, bankCustomerData, false);
                        break;
                    case 9:
                        transactions.forEach(bankCustomerData::addTransaction);
                        return;
                    default:
                        System.out.println("Please select a valid option");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please select a valid option");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
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
                                .ifPresent(Account::getString);
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
                        getAccount(customer).ifPresent(Account::print);
                        break;
                    case 2:
                        // Creates a bankStatement
                        bankDB.getBankStatement(customer)
                                .ifPresent(
                                        bankStatement -> bankStatement
                                                .createBankStatement(bankDB));
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
     * @param bankCustomerData Contains customer data.
     */
    private static void attemptToAddCustomerToSystem(
        IBankDB bankCustomerData
    ) {
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
                return;
            }

        } while (!nameValidator(firstName));

        // Get last name
        do {
            System.out.println("Enter Last Name");
            lastName = scanner.nextLine();
            if (lastName.toLowerCase().equals(EXIT)) {
                return;
            }
        } while (!nameValidator(lastName));

        // Get date of birth
        do {
            System.out.println("Enter Date of Birth DD/MM/YEAR");
            System.out.print("DD: ");
            String dd = scanner.nextLine();

            if (dd.toLowerCase().equals(EXIT)) {
                return;
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
                return;
            }
        } while (!emailValidator(email));

        // Get Address
        while (true) {
            System.out.println("Enter address");
            address = scanner.nextLine();

            if (address.toLowerCase().equals(EXIT)) {
                return;
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
                return;
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
            return;
        }

        // Add customer
        bankCustomerData.addCustomer(customer);
        System.out.println("\nID: " + customer.getId());
        System.out.println("Password: " + customer.getPassword());
    }

    /**
     * Inquires balance for customer.
     * @param customer Retrieves account from customer.
     * @return Logs of action.
     */
    public static Optional<Transaction> inquireBalance(Customer customer) {
        System.out.println("Select account to inquire\n");
        Optional<Account> account = getAccount(customer);
        if (account.isPresent()) {
            account.ifPresent(Account::print);
            return Optional.of(
                    new Transaction(
                            Optional.of(customer),
                            account,
                            Optional.empty(),
                            Optional.empty(),
                            0,
                            "inquires"));
        }
        System.out.println("Failed to locate");
        return Optional.empty();
    }

    /**
     * Deposits money into desired account.
     * @param customer Customer to deposit money into.
     * @return If empty failed to deposit, else successful.
     */
    public static Optional<Transaction> depositMoney(Customer customer) {
        System.out.println("Select account deposit into\n");
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
            return Optional.of(
                    new Transaction(
                            Optional.of(customer),
                            account,
                            Optional.of(customer),
                            account,
                            amountOfMoneyToDeposit.get(),
                            "deposits"));
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
    public static Optional<Transaction> withdrawMoney(Customer customer) {
        System.out.println("Select account to withdraw from\n");
        Optional<Account> account = getAccount(customer);
        if (account.isEmpty()) {
            return Optional.empty();
        }
        
        Optional<Double> amountToWithdraw = 
                getAmountOfMoney("Enter amount to withdraw");
        if (amountToWithdraw.isEmpty()) {
            return Optional.empty();
        }
        
        try {
            account.get().withdraw(amountToWithdraw.get());
            System.out.println("Success!");
            return Optional.of(
                    new Transaction(
                            Optional.of(customer),
                            account,
                            Optional.empty(),
                            Optional.empty(),
                            amountToWithdraw.get(),
                            "withdraws"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public static Optional<Transaction> transferMoney(Customer customer) {
        System.out.println("Select account to transfer from\n");
        Optional<Account> sourceAccount = getAccount(customer);
        if (sourceAccount.isEmpty()) {
            return Optional.empty();
        }

        System.out.println("Select account to transfer to\n");
        Optional<Account> destAccount = getAccount(customer);
        if (destAccount.isEmpty()) {
            return Optional.empty();
        }

        if (destAccount.get().equals(sourceAccount.get())) {
            System.out.println("Failed... same account selected");
            return Optional.empty();
        }

        Optional<Double> amountToTransfer = getAmountOfMoney("Enter amount to transfer");
        if (amountToTransfer.isEmpty()) {
            return Optional.empty();
        }

        try {
            customer.transfer(sourceAccount.get(), destAccount.get(),
                    amountToTransfer.get());
            System.out.println("Success!");
            return Optional.of(
                    new Transaction(
                            Optional.of(customer),
                            sourceAccount,
                            Optional.empty(),
                            destAccount,
                            amountToTransfer.get(),
                            "transfers"));
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
    public static Optional<Transaction> paySomeone(
        Customer customer,
        IBankDB bankCustomerData) {
        if (!customer.getChecking().getIsActive()) {
            System.out.println("Please activate checking account");
            return Optional.empty();
        }

        System.out.println("Select customer to pay\n");
        Optional<Customer> destCustomer = getCustomer(bankCustomerData);
        if (destCustomer.isEmpty()) {
            return Optional.empty();
        }

        Optional<Double> amountToTransfer = getAmountOfMoney("Enter amount of" +
                " money to pay!");
        if (amountToTransfer.isEmpty()) {
            return Optional.empty();
        }

        try {
            customer.paySomeone(destCustomer.get(), amountToTransfer.get());
            System.out.println("Success");
            return Optional.of(
                    new Transaction(
                            Optional.of(customer),
                            Optional.of(customer.getChecking()),
                            destCustomer,
                            Optional.of(destCustomer.get().getChecking()),
                            amountToTransfer.get(),
                            "pays"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Prompts user about getting customer info.
     * @param bankCustomerData Contains all bank data.
     * @return If empty user did not wish to select customer account, else
     * contains customer.
     */
    public static Optional<Customer> getCustomer(IBankDB bankCustomerData) {
        Scanner scanner = new Scanner(System.in);
        Optional<Customer> customer;
        while (true) {
            displayMenuNewLine(new String[]{
                "Select sign in option\n",
                "\t1. Enter Customer Id",
                "\t2. Enter Full Name",
                "\t3. Exit\n"
            });
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        System.out.print("Enter Id: ");
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
                        System.out.print("Enter full name: ");

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
                        System.out.println("Please select one of the options.");
                }
            } catch (NumberFormatException  e){
                System.out.println("Please enter a valid option");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
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
                "\t1. Checking Account",
                "\t2. Saving Account",
                "\t3. Credit Account",
                "\t4. EXIT\n"
            });

            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        if (customer.getChecking().getIsActive()) {
                            return Optional.of(customer.getChecking());
                        }
                        System.out.println("You do not have an" +
                                " active Checking Account");
                        break;
                    case 2:
                        if (customer.getSavings().getIsActive()) {
                            return Optional.of(customer.getSavings());
                        }
                        System.out.println("You do not have an" +
                                " active Savings Account");
                        break;
                    case 3:
                        if (customer.getCredit().getIsActive()) {
                            return Optional.of(customer.getCredit());
                        }
                        System.out.println("You do not have an" +
                                " active Credit Account");
                        break;
                    case 4:
                        return Optional.empty();
                    default:
                        System.out.println("Please select a valid option. ");
                }
            } catch(NumberFormatException e){
                System.out.println("Invalid option. please try again");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
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
            } catch(NumberFormatException e){
                System.out.println("Invalid amount. please try again");
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Util methods
    public static void displayMenuNewLine(String[] prompts) {
        Arrays.stream(prompts).forEach(System.out::println);
    }

    /**
     * Prints out Bank info for customer
     * @param customer Contains bank info for user.
     */
    private static void printBankAccount(Customer customer) {
        System.out.println(customer.getFullName());
        System.out.println(customer.getId());

        if (customer.getChecking().getIsActive()) {
            customer.getChecking().print();
        }

        if (customer.getSavings().getIsActive()) {
            customer.getSavings().print();
        }

        if (customer.getCredit().getIsActive()) {
            customer.getCredit().print();
        }
    }


    /**
     * Activate customers
     *
     * @param customer                             Creates account for this
     *                                             account.
     * @param bankCustomerData                     Contains customer data used
     *                                             to create a new account.
     * @param activateAtLeastSavingsAccount         If this is activate at least
     *                                             one account. Will not allow
     *                                             the user to exit.
     */
    public static void activateAccounts(
        Customer customer, IBankDB bankCustomerData,
        boolean activateAtLeastSavingsAccount) {

        if (activateAtLeastSavingsAccount) {
            System.out.println("Please activate at least savings account");
        }

        Scanner scanner = new Scanner(System.in);
        String alreadyActive = "Already Active";
        while (true) {
            displayMenuNewLine(new String[]{
                "Activate accounts\n",
                "\t1. Checking",
                "\t2. Saving",
                "\t3. Credit",
                "\t4. Exit\n"
            });
            try {
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        if (customer.getChecking().getIsActive()) {
                            System.out.println(alreadyActive);
                            break;
                        }
                        // Prints and adds to logs action
                        setAccountInfo(
                                customer.getChecking(), bankCustomerData)
                                .ifPresent(System.out::println);
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
                        break;
                    case 4:
                        // In the case that at least one account
                        if (activateAtLeastSavingsAccount) {
                            if (customer.getSavings().getIsActive()) {
                                return;
                            }
                            System.out.println("Need to activate savings " +
                                    "account");
                            break;
                        }
                        return;
                    default:
                }
            } catch (Exception exception) {
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

    /**
     * Retrieves user input file.
     *
     * Method is not dynamic. If file is not valid program should fail, but
     * will fail gracefully.
     * @return Path to customer data.
     */
    private static String askForFileName() {
        Scanner scanner = new Scanner(System.in);
        displayMenuNewLine(new String[] {
                "\n\n",
                "If file is " + PATH_TO_FILE + " please type 1",
                "Else type in file name"
        });
        String input = scanner.nextLine();

        if (input.equals("1")) {
            return PATH_TO_FILE;
        }
        return input;
    }
}
