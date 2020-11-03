import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import java.util.Arrays;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/2/20
 *
 * Runs Bank
 */
public class RunBank {
    final private static String SELECT_ONE = "Please select one";
    final private static String PATH_TO_FILE = "simple/Bank_4_users.csv";

    public static void main(String[] args) {
        IBankDB bankCustomerData = FileUtil.readFile(PATH_TO_FILE);
        userOrBankManager(bankCustomerData);
    }

    /**
     * Runs main menu.
     *
     * @param bankCustomer
     * @return
     */
    private static ArrayList<String> userOrBankManager(IBankDB bankCustomer) {
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
                        logs.addAll(accessCustomer(bankCustomer));
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        TransactionFileMenu transactionFileMenu = new TransactionFileMenu(bankCustomer, scanner);
                        transactionFileMenu.askForFileName();
                        break;
                    case 5:
                        return null;
                    default:
                        System.out.println(SELECT_ONE);
                }
            } catch (Exception e) {
                System.out.println("Exception caught");
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
                        break;
                    case 8:
                        break;
                    case 9:
                        return logs;
                    default:

                }
            } catch (Exception e) {
                // TODO(Edd1e234): Fill this...
            }
        }
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
}
