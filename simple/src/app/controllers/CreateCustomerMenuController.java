package app.controllers;

import app.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Controls window to add a new customer to the system.
 */
public class CreateCustomerMenuController extends RunBankController {
    private String firstName;
    private String lastName;
    private String email;
    private String dob;
    private String phoneNumber;
    private String address;

    @FXML TextField firstNameTextField;
    @FXML Button addFirstNameButton;

    @FXML TextField lastNameTextField;
    @FXML Button addLastNameButton;

    @FXML DatePicker dobTextField;
    @FXML Button addDOBButton;

    @FXML TextField emailTextField;
    @FXML Button addEmailButton;

    @FXML TextField phoneNumberTextField;
    @FXML Button addPhoneNumberButton;

    @FXML TextField addressTextField;
    @FXML Button addAddressButton;

    @FXML Button addToSystemButton;
    @FXML Button backButton;



    /**
     * Records users first name.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setFirstName(ActionEvent actionEvent) {
        String firstName = firstNameTextField.getText();
        if (firstName.length() == 0) {
            AlertBox.display(ERROR, "text field is empty");
            return;
        }
        if (Validator.isNameValid(firstName)) {
            this.firstName = firstName;
            return;
        }
        AlertBox.display(ERROR, "Failed to validate name");
    }

    /**
     * Records users last name.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setLastName(ActionEvent actionEvent) {
        String lastName = lastNameTextField.getText();
        if (lastName.length() == 0) {
            AlertBox.display(ERROR,"text field is empty");
            return;
        }
        if (Validator.isNameValid(lastName)) {
            this.lastName = lastName;
            return;
        }
        AlertBox.display(ERROR, "Failed to validate name");
    }

    /**
     * Records users date of birth.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setDOB(ActionEvent actionEvent) {
        String dob =
                dobTextField.getValue().format(DateTimeFormatter.ofPattern(
                        "dd/MM/yyyy"));
        if (dob.length() == 0) {
            AlertBox.display(ERROR, "Empty ");
            return;
        }
        this.dob = dob;
    }

    /**
     * Records users email.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setEmail(ActionEvent actionEvent) {
        String email = emailTextField.getText();
        if (email.length() == 0) {
            AlertBox.display(ERROR, "Email is empty");
            return;
        }
        if (Validator.isEmailValid(email)) {
            this.email = email;
            return;
        }
        AlertBox.display(ERROR, "Email validator failed to retrieve email");
    }

    /**
     * Records users phone number.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setPhoneNumber(ActionEvent actionEvent) {
        String phoneNumber = phoneNumberTextField.getText();
        if (phoneNumber.length() == 0) {
            AlertBox.display(ERROR, "Phone number text field empty");
            return;
        }
        if (Validator.isPhoneNumberValid(phoneNumber)) {
            phoneNumber = "(" + phoneNumber.substring(0, 3) + ") " +
                    phoneNumber.substring(3, 6) + "-"
                    + phoneNumber.substring(6, 10);
            this.phoneNumber = phoneNumber;
            return;
        }
        AlertBox.display(ERROR, "Failed to validate phone number");
    }

    /**
     * Records users address.
     *
     * @param actionEvent When user clicks on button.
     */
    public void setAddress(ActionEvent actionEvent) {
        String address = addressTextField.getText();
        if (address.length() == 0) {
            AlertBox.display(ERROR, "Please add address");
            return;
        }
        this.address = address;
    }

    /**
     * Attempts to add customer to system. Customer must activate account
     * before being added.
     *
     * @param actionEvent Not Used
     */
    public void addToSystem(ActionEvent actionEvent) {
        try {
            Customer newCustomer =
                    new Customer(
                            Validator.validateStr(firstName).orElseThrow(),
                            Validator.validateStr(lastName).orElseThrow(),
                            Validator.validateStr(dob).orElseThrow(),
                            bankDB.getNextId(),
                            Validator.validateStr(address).orElseThrow(),
                            Validator.validateStr(phoneNumber).orElseThrow(),
                            Validator.validateStr(email).orElseThrow(),
                            CustomerUtil.generatePassword(firstName, lastName),
                            new Checking(),
                            new Credit(),
                            new Savings());

            if (bankDB.containsCustomer(newCustomer.getFullName())) {
                AlertBox.display(ERROR, "Customer name is already in " +
                        "system");
                return;
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(ACCOUNT));
            Parent root = loader.load();

            GetAccountController getAccountController = loader.getController();
            getAccountController.setCustomer(newCustomer);
            getAccountController.setMessage("Please activate at least " +
                    "the savings account account");
            getAccountController.setActivateAccount(true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Adds customer to system.
            if (newCustomer.getSavings().IsActive()) {
                bankDB.addCustomer(newCustomer);
                displayMessage("Customer added\n"
                        + newCustomer.customerInfoString());
            } else {
                AlertBox.display(ERROR, "Failed to add to the system");
            }

        } catch (NoSuchElementException e) {
            AlertBox.display(ERROR, "Please fill out all of the fields");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes the user back to the main menu.
     *
     * @throws IOException If thrown something has gone wrong.
     */
    public void back(ActionEvent actionEvent) throws IOException {
        exit(backButton);
        moveScene(MAIN_MENU, new NullCustomer());
    }
}
