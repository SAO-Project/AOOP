package app.controllers;

import app.Account;
import app.AccountType;
import app.Customer;
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
 * Controls retrieving and activating accounts.
 */
public class GetAccountController extends RunBankController {
    private static final int NO_CREDIT_MAX = -1;
    private static final int DEFAULT_CREDIT_MAX = 5000;

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
        if (!containsCustomer()){
            throw new IOException("Customer does not exist");
        }
        if (activateAccount) {
            if (this.customer.getChecking().IsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> activateAccount(
                                this.customer, AccountType.CHECKING, balance));
            }
        } else {
            if (this.customer.getChecking().IsActive()) {
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
        if (!containsCustomer()){
            throw new IOException("Customer does not exist");
        }
        if (activateAccount) {
            if (this.customer.getSavings().IsActive()) {
                AlertBox.display(ERROR, "Account is active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> activateAccount(
                                this.customer, AccountType.SAVINGS, balance));
            }
        } else {
            if (this.customer.getSavings().IsActive()) {
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
        if (!containsCustomer()){
            throw new IOException("Customer does not exist");
        }
        if (activateAccount) {
            if (this.customer.getCredit().IsActive()) {
                AlertBox.display(ERROR, "Already active");
            } else {
                getAmount("Enter amount to start with").ifPresent(
                        balance -> activateAccount(
                                this.customer, AccountType.CREDIT, balance));
            }
        } else {
            if (this.customer.getCredit().IsActive()) {
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

    /**
     * Activates a given account.
     *
     * @param customer The customer to activate account.
     * @param accountType Which account to activate.
     * @param balance The amount of money to activate the account with.
     */
    private void activateAccount(
            Customer customer, AccountType accountType, double balance) {
        switch (accountType) {
            case SAVINGS:
                customer.setSavings(balance);
                break;
            case CHECKING:
                customer.setChecking(balance);
                break;
            case CREDIT:
                try {
                    Optional<Double> creditMax = getAmount("Enter Credit Max ");
                    if (creditMax.isPresent()) {
                        customer.setCredit(balance,
                                (int) Math.round(creditMax.get()));
                        break;
                    }
                    AlertBox.display(ERROR, "Failed to activate account");
                } catch (Exception e) {
                    AlertBox.display(ERROR, "Operation Failed exception " +
                            "thrown");
                }
                return;
                default:
                    AlertBox.display(ERROR, "IN ACTIVATE ACCOUNT IN " +
                            "getAccountController AND DEFAULT OPTION WAS SET");
                    return;
        }
        AlertBox.display(SUCCESS, "Successfully added");
    }
}
