package app.controllers;

import app.Account;
import app.NullAccount;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/15/20
 *
 */
public class GetAccountController extends RunBankController {
    private Account account = new NullAccount();
    private boolean activateAccount = false;

    @FXML Label labelField;
    @FXML Button checkingButton;
    @FXML Button savingsButton;
    @FXML Button creditButton;
    @FXML Button backButton;

    /**
     * Sets message to display to the user.
     *
     * @param message Message to display to the user.
     */
    public void setMessage(String message) {
        labelField.setText(message);
    }

    /**
     * Determines if at least one account must be activated.
     *
     * @param activateAccount Unlocks functionality to activate accounts.
     */
    public void setActivateAccount(boolean activateAccount) {
        this.activateAccount = activateAccount;
    }

    /**
     * Only used in circumstances to retrieve an account.
     *
     * @return If empty account was picked.
     */
    public Optional<Account> getAccount() {
        return account.getOptional();
    }

    /**
     * Activates checking account or retrieves it.
     *
     * @param actionEvent Not used.
     * @throws IOException Customer was not set if thrown.
     */
    public void checking(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.getChecking().getIsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> this.customer.setChecking(balance));
            }
        } else {
            if (this.customer.getChecking().getIsActive()) {
                this.account = this.customer.getChecking();
                exit(checkingButton);
            } else {
                AlertBox.display(ERROR, "Checking was not active");
            }
        }
    }

    /**
     * Activates savings account or retrieves it.
     *
     * @param actionEvent Not used.
     * @throws IOException Customer was not set if thrown.
     */
    public void savings(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.getSavings().getIsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> this.customer.setSavings(balance));
            }
        } else {
            if (this.customer.getSavings().getIsActive()) {
                this.account = this.customer.getSavings();
                exit(checkingButton);
            } else {
                throw new IOException("Savings was not active for this " +
                        "account " + this.customer.getFullName());
            }
        }
    }

    /**
     * Activates credit account or retrieves it.
     *
     * @param actionEvent Not used.
     * @throws IOException Customer was not set if thrown.
     */
    public void credit(ActionEvent actionEvent) throws IOException {
        containsCustomer();
        if (activateAccount) {
            if (this.customer.getCredit().getIsActive()) {
                AlertBox.display(ERROR, "Already active");
            } else {
                // TODO(Edd1e): Make box for credit max.
                getAmount("Enter amount to start with").ifPresent(balance ->
                        this.customer.setCredit(balance, 5000));
            }
        } else {
            if (this.customer.getCredit().getIsActive()) {
                this.account = this.customer.getCredit();
                exit(checkingButton);
            } else {
                AlertBox.display(ERROR, "Inactive");
            }
        }
    }

    /**
     * Goes back to previous menu.
     *
     * @param actionEvent Not used.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
