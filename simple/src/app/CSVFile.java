package app;

import java.io.FileWriter;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/9/20
 *
 * Writes to CSV file at the termination of the program.
 *
 * Contains all customer info.
 */
public class CSVFile {
    final private IBankDB bankDB;

    // Default constructor
    public CSVFile(IBankDB bank) {
        this.bankDB = bank;
    }

    /**
     * Writes all customer info to output file.
     */
    public void writeCSV() {
        String firstLine = "First Name,Last Name,Date of Birth," +
                "IdentificationNumber,Address,Phone Number,Email, Password," +
                "app.Checking app.Account Number,app.Savings app.Account Number,app.Credit " +
                "app.Account Number,app.Checking Balance,app.Savings Balance, app.Credit " +
                "Balance, app.Credit Max";

        try {
            FileWriter writer = new FileWriter("New_balance.csv");
            writer.write(firstLine + System.lineSeparator());
            String nextLine;

            for (Customer customer : this.bankDB.getCustomers()) {
                nextLine = customer.getFirstName() + ","
                        + customer.getLastName() + ","
                        + customer.getDob() + ","
                        + customer.getId() + ","
                        + "\"" + customer.getAddress() + "\","
                        + customer.getPhone() + ","
                        + customer.getEmail() + ","
                        + customer.getPassword() + ","
                        + customer.getChecking().getNumber() + ","
                        + customer.getSavings().getNumber() + ","
                        + customer.getCredit().getNumber() + ","
                        + customer.getChecking().getBalanceString() + ","
                        + customer.getSavings().getBalanceString() + ","
                        + customer.getCredit().getBalanceString() + ","
                        + customer.getCredit().getMax();
                writer.write(nextLine + System.lineSeparator());
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Failed to write output");
            e.printStackTrace();
        }
    }
}
