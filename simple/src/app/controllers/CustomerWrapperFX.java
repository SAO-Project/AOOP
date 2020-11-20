package app.controllers;

import app.Customer;
import app.IBankDB;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Wrapper Class of customer. So it can be viewed in Javafx application.
 */
public class CustomerWrapperFX {
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty checkingNumber;
    private final SimpleIntegerProperty savingsNumber;
    private final SimpleIntegerProperty creditNumber;
    private final SimpleStringProperty fullName;
    private final Button button;

    public CustomerWrapperFX(Customer customer, IBankDB bankDB) {
        id = new SimpleIntegerProperty(customer.getId());
        checkingNumber =
                new SimpleIntegerProperty(customer.getChecking().getNumber());
        savingsNumber =
                new SimpleIntegerProperty(customer.getSavings().getNumber());
        creditNumber =
                new SimpleIntegerProperty(customer.getCredit().getNumber());
        fullName = new SimpleStringProperty(customer.getFullName());
        button = new Button("View Customer");
        createButton(customer, bankDB);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getCheckingNumber() {
        return checkingNumber.get();
    }

    public SimpleIntegerProperty checkingNumberProperty() {
        return checkingNumber;
    }

    public int getSavingsNumber() {
        return savingsNumber.get();
    }

    public SimpleIntegerProperty savingsNumberProperty() {
        return savingsNumber;
    }

    public int getCreditNumber() {
        return creditNumber.get();
    }

    public SimpleIntegerProperty creditNumberProperty() {
        return creditNumber;
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public int getChecking() {
        return checkingNumber.get();
    }

    public int getSavings() {
        return savingsNumber.get();
    }

    public int getCredit() {
        return creditNumber.get();
    }

    public Button getButton() {
        return button;
    }

    public String getFullName() {
        return fullName.get();
    }

    /**
     * Creates a button to display bank manager customer menu.
     *
     * @param customer Customer to display.
     * @param bankDB Contains all data.
     */
    public void createButton(Customer customer, IBankDB bankDB) {
        this.button.setOnAction(e -> {
            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource(RunBankController.BANK_MANAGER_CUSTOMER_MENU));
            try {
                Parent root = loader.load();

                BankManagerCustomerMenuController
                        bankManagerCustomerMenuController = loader.getController();
                bankManagerCustomerMenuController.setCustomer(customer);
                bankManagerCustomerMenuController.enterData(bankDB);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.showAndWait();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }
}
