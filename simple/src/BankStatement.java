import java.io.Writer;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;

/**
 * @author Eddie Garcia
 * @author Alex Avila
 *
 * Bank statement contains customer info and starting balance at the begining
 * of the session.
 */
public class BankStatement {
    final private Customer customer;
    final private String checkingStartingStr;
    final private String savingsStartingStr;
    final private String creditStartingStr;

    /**
     * Default constructor.
     * @param customer Customer in which the bank statement will be based on.
     */
    public BankStatement(Customer customer) {
        this.customer = customer;
        this.checkingStartingStr = customer.getChecking().getString();
        this.savingsStartingStr = customer.getSavings().getString();
        this.creditStartingStr = customer.getCredit().getString();
    }

    /**
     * Writes all Customer data to a file.
     * Writes starting balance of every account.
     * @param bankDB
     */
    public void createBankStatement(IBankDB bankDB) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
        String fileName = customer.getFullName() + " BankStatement.txt";

        try {
            Writer bankStatementWriter = new FileWriter(fileName);
            bankStatementWriter.write("Minor Bank\n\n");
            bankStatementWriter.write(customer.customerInfoString() + "\n");
            bankStatementWriter.write(
                    "Date accessed: " +
                            dateFormat.format(
                                    new Date(System.currentTimeMillis())) +
                            "\n\n");
            bankStatementWriter.write("Starting accounts: \n\n");
            bankStatementWriter.write(checkingStartingStr + "\n");
            bankStatementWriter.write(savingsStartingStr  + "\n");
            bankStatementWriter.write(creditStartingStr  + "\n\n");
            bankStatementWriter.write("All Transactions during " +
                    "session: \n\n");
            for (Transaction transaction : bankDB.getTransactions(customer)) {
                transaction.write(bankStatementWriter);
            }
            bankStatementWriter.write("\n\n" +
                    customer.getChecking().getString() + "\n");
            bankStatementWriter.write(
                    customer.getSavings().getString() + "\n");
            bankStatementWriter.write(
                    customer.getCredit().getString() + "\n");
            bankStatementWriter.close();
            System.out.println("Bank Statement Created!");
        } catch (Exception e) {
            System.out.println("At Writing Bank Statement");
        }
    }
}
