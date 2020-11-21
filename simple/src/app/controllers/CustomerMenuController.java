package app.controllers;

import app.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Displays the customer menu.
 */
public class CustomerMenuController extends RunBankController {
    @FXML Button inquireBalanceButton;
    @FXML Button depositMoneyButton;
    @FXML Button withdrawButton;
    @FXML Button transferButton;
    @FXML Button paySomeoneButton;
    @FXML Button showLogsButton;
    @FXML Button viewBankAccountButton;
    @FXML Button activateAccountButton;
    @FXML Button backButton;
    @FXML Button exitButton;

    /**
     * Shows account based on user preference.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void inquireBalance(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        Optional<Account> account = getAccount("Get account to display");

        if (account.isEmpty()) {
            System.out.println("Failed to retrieve account.");
            return;
        }
        displayMessage(account.get().getString());
        bankDB.addTransaction(new Transaction(
                this.customer,
                account.orElseThrow(),
                new NullCustomer(),
                new NullAccount(),
                0,
                "inquires"));
        System.out.println("inquire balance");
    }

    /**
     * Deposits money. User chooses account.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void depositMoney(ActionEvent actionEvent) throws IOException{
        containsCustomer();
        System.out.println("deposit money");

        Optional<Account> account = getAccount("Get account to deposit into");
        if (account.isEmpty()) {
            System.out.println("Failed to retrieve account");
            return;
        }

        Optional<Double> amountToDeposit = getAmount("Enter amount to deposit");
        if (amountToDeposit.isEmpty()) {
            System.out.println("Failed to amount");
            return;
        }
        try {
            account.get().deposit(amountToDeposit.get());
            bankDB.addTransaction(
                    new Transaction(
                            new NullCustomer(),
                            new NullAccount(),
                            customer,
                            account.get(),
                            amountToDeposit.get(),
                            "deposits"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    /**
     * Withdraws money. User chooses account to withdraw from.
     *
     * @param actionEvent Not Used.
     * @throws IOException If thrown logical error in code.
     */
    public void withdrawMoney(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        System.out.println("withdraw");
        Optional<Account> account = getAccount("Get account to withdraw from");
        if (account.isEmpty()) {
            System.out.println("Failed to retrieve account");
            return;
        }

        Optional<Double> amountToWithdraw = getAmount("Amount to withdraw");
        if (amountToWithdraw.isEmpty()) {
            System.out.println("Failed to retrieve amount");
            return;
        }
        try {
            account.get().withdraw(amountToWithdraw.get());
            bankDB.addTransaction(new Transaction(
                    customer,
                    account.get(),
                    new NullCustomer(),
                    new NullAccount(),
                    amountToWithdraw.get(),
                    "withdraws"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    /**
     * Transfers money from account to account.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown something has gone wrong. Logical error
     * in code.
     */
    public void transferMoney(ActionEvent actionEvent) throws IOException{
        containsCustomer();
        System.out.println("transfer");
        Optional<Account> sourceAccount = getAccount("Get account to transfer" +
                " from");
        if (sourceAccount.isEmpty()) {
            System.out.println("Failed to retrieve source account");
            return;
        }

        Optional<Account> destAccount = getAccount("Get account to transfer " +
                "to");
        if (destAccount.isEmpty()) {
            System.out.println("Failed to retrieve des account");
            return;
        }

        if (destAccount.get().equals(sourceAccount.get())) {
            AlertBox.display(ERROR, "Selected same account failed...");
            return;
        }

        Optional<Double> amountToTransfer = getAmount("Get amount to transfer");
        if (amountToTransfer.isEmpty()) {
            System.out.println("Failed to retrieve amount");
            return;
        }

        try {
            customer.transfer(sourceAccount.get(), destAccount.get(),
                    amountToTransfer.get());
            bankDB.addTransaction(new Transaction(
                    customer,
                    sourceAccount.get(),
                    new NullCustomer(),
                    destAccount.get(),
                    amountToTransfer.get(),
                    "transfers"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    /**
     * User selects another user to pay.
     * User pays from checking account into other users checking account.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void payMoney(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        System.out.println("pay");
        if (!customer.getChecking().IsActive()) {
            AlertBox.display(ERROR, "Please activate checking account");
        }

        // TODO(Edd1e234): Translate this to its own method.
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(CUSTOMER_LOGIN));
        Parent root = loader.load();

        CustomerLoginController controller = loader.getController();
        controller.enterData(bankDB);
        controller.setNeedsPassword(false);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();

        Optional<Customer> destCustomer = controller.getCustomer();

        if (destCustomer.isEmpty()) {
            System.out.println("Keep Running");
            return;
        }

        Optional<Double> amount = getAmount("Enter amount to pay");
        if (amount.isEmpty()) {
            return;
        }

        try {
            customer.paySomeone(destCustomer.get(), amount.get());
            this.bankDB.addTransaction(
                    new Transaction(
                            customer,
                            customer.getChecking(),
                            destCustomer.get(),
                            destCustomer.get().getChecking(),
                            amount.get(),
                            "pays"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    /**
     * Shows all events in customer session.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void showLogs(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (bankDB.getTransactions(customer).isEmpty()) {
            AlertBox.display(ERROR, "No logs");
            return;
        }
        StringBuilder transactionsStr = new StringBuilder();

        // Gather all the strings.
        // TODO(Edd1e): Make this more efficient.
        for (Transaction transaction : bankDB.getTransactions(customer)) {
            System.out.println(transaction.getString());
            transactionsStr.append(transaction.getString()).append("\n");
        }

        displayMessage(transactionsStr.toString());
        System.out.println("show logs");
    }

    /**
     * Shows user their bank info.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void viewBankAccount(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        displayMessage(customer.customerInfoString() +
                customer.getChecking().getString() + "\n" +
                customer.getSavings().getString() + "\n" +
                customer.getCredit().getString() + "\n");
        System.out.println("view bank account");
    }

    /**
     * Runs window for user to activate an account.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void activateAccount(ActionEvent actionEvent) throws IOException {
        System.out.println("activate account");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(ACCOUNT));
        Parent root = fxmlLoader.load();

        GetAccountController getAccountController = fxmlLoader.getController();
        getAccountController.setCustomer(customer);
        getAccountController.setActivateAccount(true);
        getAccountController.setMessage("Click which account to activate");

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * Goes back to the main menu.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void back(ActionEvent actionEvent) throws IOException {
        exit(backButton);
        moveScene(MAIN_MENU, new NullCustomer());
    }

    /**
     * Exits program.
     *
     * @param actionEvent Not used.
     */
    public void exit(ActionEvent actionEvent) {
        exit();
    }
}
