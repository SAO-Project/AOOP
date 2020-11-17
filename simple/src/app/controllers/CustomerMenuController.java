package app.controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.util.Collection;
import java.util.Optional;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Customer menu.
 * TODO(Edd1e234): impl text box that can display a string.
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
    @FXML Button exitButton;

    public void inquireBalance(ActionEvent actionEvent) throws IOException {
        Optional<Account> account = getAccount("Get account to display");

        if (account.isEmpty()) {
            System.out.println("Failed to retrieve account.");
            return;
        }

        displayMessage(account.get().getString());
        bankDB.addTransaction(new Transaction(
                this.customer,
                account,
                Optional.empty(),
                Optional.empty(),
                0,
                "inquires"));
        System.out.println("inquire balance");
    }

    public void depositMoney(ActionEvent actionEvent) throws IOException{
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
            bankDB.addTransaction(new Transaction(
                    customer,
                    account,
                    customer,
                    account,
                    amountToDeposit.get(),
                    "deposits"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    public void withdraw(ActionEvent actionEvent) throws IOException {
        System.out.println("withdraw");
        Optional<Account> account = getAccount("Get account to withdraw from");
        if (account.isEmpty()) {
            System.out.println("Failed to retrieve account");
            return;
        }

        Optional<Double> amountToWithdraw = getAmount("Amount to withdraw");
        if (amountToWithdraw.isEmpty()) {
            System.out.println("Failed to retrieve amount");
        }
        try {
            account.get().withdraw(amountToWithdraw.get());
            bankDB.addTransaction(new Transaction(
                    customer,
                    account,
                    Optional.empty(),
                    Optional.empty(),
                    amountToWithdraw.get(),
                    "withdraws"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    public void transfer(ActionEvent actionEvent) throws IOException{
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
            customer.get().transfer(sourceAccount.get(), destAccount.get(),
                    amountToTransfer.get());
            bankDB.addTransaction(new Transaction(
                    customer,
                    sourceAccount,
                    Optional.empty(),
                    destAccount,
                    amountToTransfer.get(),
                    "transfer"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    public void pay(ActionEvent actionEvent) throws IOException {
        System.out.println("pay");
        if (!customer.get().getChecking().getIsActive()) {
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
            customer.get().paySomeone(destCustomer.get(), amount.get());
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
            this.bankDB.addTransaction(
                    new Transaction(
                            customer,
                            Optional.of(customer.get().getChecking()),
                            destCustomer,
                            Optional.of(destCustomer.get().getChecking()),
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
     * @param actionEvent
     * @throws IOException
     */
    public void showLogs(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (bankDB.getTransactions(customer.get()).isEmpty()) {
            AlertBox.display(ERROR, "No logs");
            return;
        }
        String transactionsStr = "";

        // Gather all the strings.
        // TODO(Edd1e): Make this more efficient.
        for (Transaction transaction : bankDB.getTransactions(customer.get())) {
            System.out.println(transaction.getString());
            transactionsStr += transaction.getString() + "\n";
        }

        displayMessage(transactionsStr);
        System.out.println("show logs");
    }

    public void viewBankAccount(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        displayMessage(customer.get().customerInfoString());
        System.out.println("view bank account");
    }

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

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
