package app.controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.util.Optional;

import java.io.IOException;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Customer menu.
 * TODO(Edd1e234): impl text box that can display a string.
 */
public class CustomerMenuController extends RunBankController {
    private Customer customer;

    @FXML Button inquireBalanceButton;
    @FXML Button depositMoneyButton;
    @FXML Button withdrawButton;
    @FXML Button transferButton;
    @FXML Button paySomeoneButton;
    @FXML Button showLogsButton;
    @FXML Button viewBankAccountButton;
    @FXML Button activateAccountButton;
    @FXML Button exitButton;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void inquireBalance(ActionEvent actionEvent) throws IOException {
        Optional<Account> account = getAccount();

        if (account.isPresent()) {
            System.out.println("Account retrieved... display");
        }

        System.out.println("inquire balance");
    }

    public void depositMoney(ActionEvent actionEvent) {
        System.out.println("deposit money");
    }

    public void withdraw(ActionEvent actionEvent) {
        System.out.println("withdraw");
    }

    public void transfer(ActionEvent actionEvent) {
        System.out.println("transfer");
    }

    public void pay(ActionEvent actionEvent) throws IOException {
        System.out.println("pay");
        if (!customer.getChecking().getIsActive()) {
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
        }

        Optional<Double> amount = getAmount("Enter amount to pay");
        if (amount.isEmpty()) {
            return;
        }

        try {
            customer.paySomeone(destCustomer.get(), amount.get());
            // TODO(Edd1e234): Create method for Success Box!

            this.bankDB.addTransaction(
                    new Transaction(
                            Optional.of(customer),
                            Optional.of(customer.getChecking()),
                            destCustomer,
                            Optional.of(destCustomer.get().getChecking()),
                            amount.get(),
                            "pays"));
            AlertBox.display(SUCCESS, OPERATION_SUCCESS);
        } catch (Exception e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    public void showLogs(ActionEvent actionEvent) {
        System.out.println("show logs");
    }

    public void viewBankAccount(ActionEvent actionEvent) {
        System.out.println("view bank account");
    }

    public void activateAccount(ActionEvent actionEvent) {
        System.out.println("activate account");
    }

    public void exit(ActionEvent actionEvent) {
        System.exit(0);
    }
}
