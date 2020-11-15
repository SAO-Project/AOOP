package app.controllers;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
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

    public void setText(String message) {
        labelField.setText(message);
    }

    public Optional<Double> getAmount() {
        return amount;
    }

    public void enter(ActionEvent actionEvent) {
        double amount;
        try {
            amount = Double.parseDouble(amountTextField.getText());

            // Throw exception!
            if (amount < 0) {
                throw new RuntimeException();
            }

            this.amount = Optional.of(amount);
        } catch (Exception e) {
            AlertBox.display(ERROR, "Invalid");
        }
    }

    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
