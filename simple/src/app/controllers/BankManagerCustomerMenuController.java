package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Controller for the Bank Manager Customer Menu window.
 *
 * TODO(Edd1e234): Make customer must have transactions to create bank
 *      statement.
 */
public class BankManagerCustomerMenuController extends RunBankController {
    @FXML Button backButton;
    @FXML Button viewAccountButton;
    @FXML Button generateBankStatementButton;

    /**
     * Shows bank manager customers info.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical error in code.
     */
    public void viewAccount(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        displayMessage(customer.get().customerInfoString() +
                customer.get().getChecking().getString() + "\n" +
                customer.get().getSavings().getString() + "\n" +
                customer.get().getCredit().getString() + "\n");
    }

    /**
     * Writes a bank statement.
     *
     * @param actionEvent Not used.
     * @throws IOException If file not found, will display to user. Else if
     * thrown logical in code.
     */
    public void generateBankStatement(
            ActionEvent actionEvent) throws IOException {
        containsCustomer();
        bankDB.getBankStatement(
                customer.get()).ifPresent(
                        bankStatement ->
                                bankStatement.createBankStatement(bankDB));
        if (bankDB.getBankStatement(customer.get()).isPresent()) {
            AlertBox.display(SUCCESS, "File will create at end of session");
        } else {
            AlertBox.display(ERROR, "No transactions were recorded") ;
        }
    }

    /**
     * Back to the main menu.
     *
     * @param actionEvent Not used.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
