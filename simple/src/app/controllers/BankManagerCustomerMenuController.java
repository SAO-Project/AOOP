package app.controllers;

import app.BankStatement;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Controller for the Bank Manager Customer Menu window.
 *
 * User
 */
public class BankManagerCustomerMenuController extends RunBankController {
    @FXML Button backButton;
    @FXML Button viewAccountButton;
    @FXML Button generateBankStatementButton;

    public void viewAccount(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        displayMessage(customer.get().customerInfoString());
    }

    public void generateBankStatement(
            ActionEvent actionEvent) throws IOException {
        containsCustomer();
        bankDB.getBankStatement(
                customer.get()).ifPresent(
                        bankStatement ->
                                bankStatement.createBankStatement(bankDB));
    }

    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
