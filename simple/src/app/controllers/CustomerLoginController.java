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

    // Do work before.
    public void setNeedsPassword(boolean needsPassword) {
        System.out.println(needsPassword);
        this.needsPassword = needsPassword;
    }

    public Optional<Customer> getCustomer() {
        return this.customer;
    }

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

    public void enterFullName(ActionEvent actionEvent) {
        System.out.println("Enter Full Name");
        this.customer = bankDB.getCustomer(enterFullNameTextField.getText());

        // Runs through checks
        determinePath();
    }

    public void password(ActionEvent actionEvent) throws IOException {
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
        if (needsPassword) {
            // Moves scene and closes window. Goes back to main menu
            moveScene(MAIN_MENU, this.customer);
        } else {
            // Just closes scene
            exit(backButton);
        }
        System.out.println("Back");
    }

    public void exitButton(ActionEvent actionEvent) {
        System.out.println("Exit");
        System.exit(0);
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

    // Customer must enter password before moving on.
    private void needsPassword() {
        if (customer.isPresent()) {
            passwordVbox.setVisible(true);
        } else {
            passwordVbox.setVisible(false);
        }
    }

    // Customer is found, moves on.
    private void doesNotNeedPassword() {
        if (customer.isPresent()) {
            exit(backButton);
        }
    }
}
