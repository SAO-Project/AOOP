package app.controllers;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 *
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
        if (RunBank.nameValidator(firstName)) {
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
        if (RunBank.nameValidator(lastName)) {
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
        if (RunBank.emailValidator(email)) {
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
        if (RunBank.phoneNumberValidator(phoneNumber)) {
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

    public void addToSystem(ActionEvent actionEvent) {
        try {
            Customer customer =
                    new Customer(
                            validateStr(firstName).orElseThrow(),
                            validateStr(lastName).orElseThrow(),
                            validateStr(dob).orElseThrow(),
                            bankDB.getNextId(),
                            validateStr(phoneNumber).orElseThrow(),
                            validateStr(email).orElseThrow(),
                            validateStr(address).orElseThrow(),
                            RunBank.generatePassword(firstName, lastName),
                            new Checking(),
                            new Credit(),
                            new Savings());
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ACCOUNT));
            Parent root = loader.load();

            GetAccountController getAccountController = loader.getController();
            getAccountController.setCustomer(Optional.of(customer));
            getAccountController.setMessage("Please activate at least " +
                    "the savings account account");
            getAccountController.setActivateAccount(true);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();

            if (validateCustomer(customer)) {
                displayMessage("Customer added\n"
                        + customer.customerInfoString());
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
        moveScene(MAIN_MENU, Optional.empty());
    }

    /**
     * Adds customer to the System.
     *
     * @param customer Customer to be added.
     * @return If True customer was added to the system. 
     */
    public Boolean validateCustomer(Customer customer) {
        if (bankDB.containsCustomer(customer.getFullName())) {
            return false;
        }

        if (customer.getSavings().getIsActive()) {
            bankDB.addCustomer(customer);
            return true;
        }
        return false;
    }

    /**
     * Validate a string. Make sure it is not length zero.
     *
     * @param str String to validate.
     * @return If empty() string is not valid.
     */
    public static Optional<String> validateStr(String str) {
        if (str == null) {
            return Optional.empty();
        }


        if (str.length() == 0) {
            return Optional.empty();
        }
        return Optional.of(str);
    }
}
