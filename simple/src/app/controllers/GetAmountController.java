package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 */
public class GetAmountController extends RunBankController {
    Optional<Double> amount = Optional.empty();

    @FXML Label labelField;
    @FXML TextField amountTextField;
    @FXML Button backButton;

    /**
     * Displays message to the user.
     *
     * @param message Message to display to the user.
     */
    public void setMessage(String message) {
        labelField.setText(message);
    }

    /**
     * Gets amount.
     *
     * @return if empty, user did not enter amount.
     */
    public Optional<Double> getAmount() {
        if (amount.isPresent()) {
            if (amount.get() <= 0) {
                return Optional.empty();
            }
            return amount;
        }
        return amount;
    }

    /**
     * Parse amount user entered.
     *
     * @param actionEvent Not used.
     */
    public void enter(ActionEvent actionEvent) {
        double amount;
        try {
            amount = Double.parseDouble(amountTextField.getText());

            // Throw exception!
            if (amount < 0) {
                throw new RuntimeException();
            }

            this.amount = Optional.of(amount);
            System.out.println("Amount of money is " + amount);
            exit(backButton);
        } catch (Exception e) {
            AlertBox.display(ERROR, "Invalid");
        }
    }

    /**
     * Closes window.
     *
     * @param actionEvent Not used.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
