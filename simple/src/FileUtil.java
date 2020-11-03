

import java.io.BufferedReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 10/27/20
 *
 * Reads a file and builds hash-maps Names to Customers and Id's to customers.
 *
 * Runs operations related to file writing and reading.
 */
public class FileUtil {
    private static final String FAILURE_PARSE_MESSAGE = "Failed to parse ";
    private static final String REGEX = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";

    // Public for testing.
    static int FIRST_NAME = -1;
    static int LAST_NAME = -1;
    static int DOB = -1;
    static int ID = -1;
    static int ADDRESS = -1;
    static int PHONE_NUMBER = -1;
    static int CHECKING_ACCOUNT_NUMBER = -1;
    static int SAVING_ACCOUNT_NUMBER = -1;
    static int CREDIT_ACCOUNT_NUMBER = -1;
    static int CHECKING_STARTING_BALANCE = -1;
    static int SAVING_STARTING_BALANCE = -1;
    static int CREDIT_STATING_BALANCE = -1;
    static int CREDIT_MAX = -1;
    static int PASSWORD = -1;
    static int EMAIL = -1;

    /**
     * No Instance of the class should be allowed.
     */
    private FileUtil() {
    }

    /**
     * Goes through each line of customers.cvs. Each line is parsed, which
     * creates a Customer object. Customer class is stored in two separate hash
     * maps. These hash maps are returned via array.
     * <p>
     * If fails to parse error message will be thrown to the screen.
     *
     * @param pathToFile opens file according to this.
     * @return Object containing two hashmaps. One hash map is customer name to
     * customer object. The other is customer ID to customer object.
     * BankCustomerData object also contains the next ID available.
     */
    public static BankCustomerData readFile(String pathToFile) {
        HashMap<String, Customer> nameToCustomer = new HashMap<>();
        HashMap<Integer, Customer> idToCustomer = new HashMap<>();
        try {
            // Open file.
            BufferedReader  cvsReader =
                    new BufferedReader(new java.io.FileReader(pathToFile));
            String line;

            setEachIndex(cvsReader.readLine().split(","));
            while ((line = cvsReader.readLine()) != null) {
                Customer customer = parseLine(line.split(REGEX)).orElseThrow();

                nameToCustomer.put(Customer.getHashName(customer), customer);
                idToCustomer.put(customer.getId(), customer);
            }
        } catch (Exception e) {
            // Uncomment if debugging
            e.printStackTrace();
            System.out.println("Failed to read line");
        }
        return new BankCustomerData(nameToCustomer, idToCustomer);
    }

    /**
     * Will set variables to corresponding values in file.
     *
     * @param listOfValues First now cells ONLY.
     */
    public static void setEachIndex(String[] listOfValues) {
        for (int i = 0; i < listOfValues.length; i++) {
            switch (listOfValues[i]) {
                case "First Name":
                    FIRST_NAME = i;
                    break;
                case "Last Name":
                    LAST_NAME = i;
                    break;
                case "Date of Birth":
                    DOB = i;
                    break;
                case "Identification Number":
                    ID = i;
                    break;
                case "Password":
                    PASSWORD = i;
                    break;
                case "Email":
                    EMAIL = i;
                    break;
                case "Address":
                    ADDRESS = i;
                    break;
                case "Phone Number":
                    PHONE_NUMBER = i;
                    break;
                case "Savings Account Number":
                    SAVING_ACCOUNT_NUMBER = i;
                    break;
                case "Savings Starting Balance":
                    SAVING_STARTING_BALANCE = i;
                    break;
                case "Checking Account Number":
                    CHECKING_ACCOUNT_NUMBER = i;
                    break;
                case "Checking Starting Balance":
                    CHECKING_STARTING_BALANCE = i;
                    break;
                case "Credit Account Number":
                    CREDIT_ACCOUNT_NUMBER = i;
                    break;
                case "Credit Starting Balance":
                    CREDIT_STATING_BALANCE = i;
                    break;
                case "Credit Max":
                    CREDIT_MAX = i;
                    break;
                default:
                    System.out.println("ERROR at " + i);
                    System.out.println(listOfValues[i]);
            }
        }
    }

    /**
     * Parses line and retrieves customer object from {@param splitLine}.
     *
     * @param splitLine array split by commas.
     * @return Optional.empty if something goes wrong. Else return Optional with
     * Customer object intact.
     */
    public static Optional<Customer> parseLine(String[] splitLine) {
        // TODO(Edd1e234): Make this look cleaner.
        try {
            return Optional.of(
                    new Customer(
                            splitLine[FIRST_NAME],              /* =firstName */
                            splitLine[LAST_NAME],               /* =lastName */
                            splitLine[DOB],                     /* =DOB */
                            parseIdNum(splitLine[ID]).orElseThrow(), /* =id */
                            removeQuotes(splitLine[ADDRESS]),   /* =address */
                            splitLine[PHONE_NUMBER],            /* =phoneNumber */
                            splitLine[EMAIL],                   /* =email */
                            splitLine[PASSWORD],                /* =password*/
                            parseCheckingAccount(
                                    splitLine[CHECKING_ACCOUNT_NUMBER] /* =accountNumStr */,
                                    splitLine[CHECKING_STARTING_BALANCE] /* =accountBalanceStr */)
                                    .orElseThrow(), /* =checking */
                            parseCreditAccount(
                                    splitLine[CREDIT_ACCOUNT_NUMBER], /* =accountNumStr */
                                    splitLine[CREDIT_STATING_BALANCE], /* =accountBalanceStr */
                                    splitLine[CREDIT_MAX] /* =creditMax*/)
                                    .orElseThrow(),    /* =creditAccount */
                            parseSavingAccount(
                                    splitLine[SAVING_ACCOUNT_NUMBER], /* =accountNumStr */
                                    splitLine[SAVING_STARTING_BALANCE] /* =accountBalanceStr */)
                                    .orElseThrow()   /* =savingAccount */
                    ));
        } catch (Exception exception) {

            // If we return optional empty, something has gone wrong.
            return Optional.empty();
        }
    }

    /**
     * Generates ID to be stored in customer.
     * <p>
     * Made public for tests.
     *
     * @param idStr raw string text, expected format is xxx-xx-xxx
     * @return Optional.empty() if something goes wrong removing "-". Else
     * returns int value.
     */
    public static Optional<Integer> parseIdNum(String idStr) {
        return parseInt(idStr, FAILURE_PARSE_MESSAGE + "Id.");
    }

    /**
     * Build customer checking account using account number and starting
     * balance. Will throw if anything goes wrong
     *
     * @param accountNumStr     Account number for checking account.
     * @param accountBalanceStr Starting balance.
     * @return Optional containing checking account.
     */
    public static Optional<Checking> parseCheckingAccount(
            String accountNumStr, String accountBalanceStr) {
        return Optional.of(new Checking(
                parseInt(
                        accountNumStr,
                        FAILURE_PARSE_MESSAGE
                                .concat("Checking account number."))
                        .orElseThrow(), /* =accountNumber*/
                parseDouble(
                        accountBalanceStr,
                        FAILURE_PARSE_MESSAGE.concat("Checking balance."))
                        .orElseThrow()  /* =accountBalance*/
        ));
    }

    /**
     * Build customer credit account using account number and starting balance.
     * Will throw if anything goes wrong
     *
     * @param accountNumStr     Account number for credit account.
     * @param accountBalanceStr Starting balance.
     * @return Optional containing credit account.
     */
    public static Optional<Credit>
    parseCreditAccount(String accountNumStr, String accountBalanceStr,
                       String creditMaxStr) {
        return Optional.of(new Credit(
                parseInt(
                        accountNumStr,
                        FAILURE_PARSE_MESSAGE.concat("Credit account number"))
                        .orElseThrow(), /* =accountBalance*/
                parseDouble(
                        accountBalanceStr,
                        FAILURE_PARSE_MESSAGE.concat("Credit balance"))
                        .orElseThrow(), /* =accountBalance*/
                parseInt(creditMaxStr,
                        FAILURE_PARSE_MESSAGE.concat("Credit max"))
                        .orElseThrow()
        ));
    }

    /**
     * Build customer saving account using account number and starting balance.
     * Will throw if anything goes wrong
     *
     * @param accountNumStr     Account number for saving account.
     * @param accountBalanceStr Starting balance.
     * @return Optional containing credit account.
     */
    public static Optional<Savings> parseSavingAccount(
            String accountNumStr, String accountBalanceStr) {

        return Optional.of(new Savings(
                parseInt(
                        accountNumStr,
                        FAILURE_PARSE_MESSAGE.concat("Saving account number"))
                        .orElseThrow(), /* =accountBalance */
                parseDouble(
                        accountBalanceStr,
                        FAILURE_PARSE_MESSAGE.concat("Savings balance"))
                        .orElseThrow() /* =accountBalance */
        ));
    }

    /**
     * Parse ints.
     *
     * @param line    int number to be parsed. Currently supports "1234" and
     *                "1-2-3-4".
     * @param message If {@param line} fails to parse, print message and fail
     *                quietly
     * @return Returns Optional.empty() if fails to parse. Else return parsed
     * value.
     */
    public static Optional<Integer> parseInt(String line, String message) {
        StringBuilder value = new StringBuilder();
        try {
            // Split line, combine nums
            Arrays.stream(line.split("-")).forEach(value::append);
            return Optional.of(Integer.valueOf(value.toString()));
        } catch (Exception exception) {
            // If something fails print message and fail quietly.
            System.out.println(message);
            return Optional.empty();
        }
    }

    /**
     * Parses double value.
     *
     * @param line    Assumes there will be no need to split.
     * @param message Error message to prompt to console if failure happens.
     * @return Returns Optional.empty() if fails to parse. Else return parsed
     * value.
     */
    public static Optional<Double> parseDouble(String line, String message) {
        try {
            return Optional.of(Double.valueOf(line));
        } catch (Exception exception) {
            System.out.println(message);
            return Optional.empty();
        }
    }

    /**
     * Remove quotation marks.
     * <p>
     * Expecting "str" -> str.
     *
     * @param str Expecting "str"
     * @return Str with removed quotation values.
     */
    private static String removeQuotes(String str) {
        return str.substring(1, str.length() - 1);
    }
}
