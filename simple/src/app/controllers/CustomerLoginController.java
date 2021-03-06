package app.controllers;

import app.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

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
        return this.customer.getOptional();
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
        this.customer = bankDB.getCustomer(id).orElseThrow();

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
        this.customer = bankDB.getCustomer(enterFullNameTextField.getText()).orElseThrow();

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
        if (customer.getOptional().isEmpty()) {
            AlertBox.display(ERROR, "Please enter ID or Name first");

        } else if (customer.getPassword().equals(passwordField.getText())
                // Quick access
        || customer.getPassword().equals("m")) {
            System.out.println(
                    "Logging in Customer " + customer.getFullName());
            exit(backButton);
        } else {
            AlertBox.display(ERROR, "Password is wrong");
        }
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
        passwordVbox.setVisible(customer.getOptional().isPresent());
    }

    /**
     * Closes window if password is not needed.
     */
    private void doesNotNeedPassword() {
        if (customer.getOptional().isPresent()) {
            exit(backButton);
        }
    }

    /**
     * Back to the main menu.
     *
     * @param actionEvent Not used.;
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
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
