package app.controllers;

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
 * TODO(Edd1e234): impl activate account portion
 */
public class GetAccountController extends RunBankController {
    private Optional<Account> account = Optional.empty();
    private boolean activateAccount = false;

    @FXML Button checkingButton;
    @FXML Button savingsButton;
    @FXML Button creditButton;
    @FXML Button backButton;

    public Optional<Account> getAccount() {
        return account;
    }

    public void checking(ActionEvent actionEvent) throws IOException {
        if (containsCustomer()) {
            if (!activateAccount) {
                if (this.customer.get().getChecking().getIsActive()) {
                    this.account = Optional.of(this.customer.get().getChecking());
                    exit(checkingButton);
                } else {
                    AlertBox.display(ERROR, "Checking was not active");
                }
            }
        }
    }

    public void savings(ActionEvent actionEvent) throws IOException {
        if (containsCustomer()) {
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
        if (containsCustomer()) {
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
