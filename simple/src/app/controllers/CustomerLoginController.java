package app.controllers;

import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Can be created in two ways
 */
public class CustomerLoginController extends RunBankController {
    private boolean needsPassword;

    @FXML VBox passwordVbox;
    @FXML PasswordField passwordField;
    @FXML Button passwordButton;

    @FXML TextField enterIdTextField;
    @FXML Button enterIdButton;

    @FXML TextField enterFullNameTextField;
    @FXML Button enterFullNameButton;

    @FXML Button backButton;

    /**
     * Determines if password is needed to retrieve customer.
     *
     * @param needsPassword bool for password.
     */
    public void setNeedsPassword(boolean needsPassword) {
        System.out.println(needsPassword);
        this.needsPassword = needsPassword;
    }

    /**
     * Gets Customer.
     *
     * @return Customer from logging.
     */
    public Optional<Customer> getCustomer() {
        return this.customer;
    }

    /**
     * Enter full id. Attempts to retrieve customer.
     *
     * @param actionEvent Not used.
     */
    public void enterId(ActionEvent actionEvent) {
        System.out.println("Enter ID");
        int id;
        try {
            id = Integer.parseInt(enterIdTextField.getText());
        } catch (Exception e) {
            AlertBox.display("ID Error",
                    "Please enter numbers in text field");
            return;
        }
        this.customer = bankDB.getCustomer(id);

        // Runs through checks.
        determinePath();
    }

    /**
     * Enter full name text field.
     *
     * @param actionEvent Not used.
     */
    public void enterFullName(ActionEvent actionEvent) {
        System.out.println("Enter Full Name");
        this.customer = bankDB.getCustomer(enterFullNameTextField.getText());

        // Runs through checks
        determinePath();
    }

    /**
     * Checks password. Attempts to retrieve customer.
     *
     * @param actionEvent Not used.
     */
    public void password(ActionEvent actionEvent)  {
        System.out.println("Entering Password");
        if (customer.isEmpty()) {
            AlertBox.display(ERROR, "Please enter ID or Name first");

        } else if (customer.get().getPassword().equals(passwordField.getText())
                // Quick access
        || customer.get().getPassword().equals("m")) {
            System.out.println(
                    "Logging in Customer " + customer.get().getFullName());
            exit(backButton);
        } else {
            AlertBox.display(ERROR, "Password is wrong");
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        exit(backButton);
    }

    private void determinePath() {
        if (needsPassword) {
            System.out.println("Needs password");
            needsPassword();
        } else {
            System.out.println("Does not need password");
            doesNotNeedPassword();
        }
    }

    /**
     * Makes password portion visiable.
     */
    private void needsPassword() {
        passwordVbox.setVisible(customer.isPresent());
    }

    /**
     * Closes window if password is not needed.
     */
    private void doesNotNeedPassword() {
        if (customer.isPresent()) {
            exit(backButton);
        }
    }

    /**
     * Exits from process.
     *
     * @param actionEvent Not used.
     */
    public void exitButton(ActionEvent actionEvent) {
        exit();
    }
}
