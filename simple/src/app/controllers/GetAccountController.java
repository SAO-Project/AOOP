package app.controllers;

import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/15/20
 *
 */
public class GetAccountController extends RunBankController {
    private Optional<Account> account = Optional.empty();
    private boolean activateAccount = false;

    @FXML Label labelField;
    @FXML Button checkingButton;
    @FXML Button savingsButton;
    @FXML Button creditButton;
    @FXML Button backButton;

    public void setMessage(String message) {
        labelField.setText(message);
    }

    public void setActivateAccount(boolean activateAccount) {
        this.activateAccount = activateAccount;
    }

    public Optional<Account> getAccount() {
        return account;
    }

    public void checking(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.get().getChecking().getIsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> this.customer.get().setChecking(balance));
            }
        } else {
            if (this.customer.get().getChecking().getIsActive()) {
                this.account = Optional.of(this.customer.get().getChecking());
                exit(checkingButton);
            } else {
                AlertBox.display(ERROR, "Checking was not active");
            }
        }
    }

    public void savings(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.get().getSavings().getIsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> this.customer.get().setSavings(balance));
            }
        } else {
            if (this.customer.get().getSavings().getIsActive()) {
                this.account = Optional.of(this.customer.get().getSavings());
                exit(checkingButton);
            } else {
                throw new IOException("Savings was not active for this " +
                        "account " + this.customer.get().getFullName());
            }
        }
    }

    public void credit(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.get().getCredit().getIsActive()) {
                AlertBox.display(ERROR, "Already active");
            } else {
                // TODO(Edd1e): Make box for credit max.
                getAmount("Enter amount to start with").ifPresent(balance ->
                        this.customer.get().setCredit(balance, 5000));
            }
        } else {
            if (this.customer.get().getCredit().getIsActive()) {
                this.account = Optional.of(this.customer.get().getCredit());
                exit(checkingButton);
            } else {
                AlertBox.display(ERROR, "Inactive");
            }
        }
    }

    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
