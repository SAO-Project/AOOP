package app.controllers;

import app.Customer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;
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

    public CustomerWrapperFX(Customer customer) {
        id = new SimpleIntegerProperty(customer.getId());
        checkingNumber =
                new SimpleIntegerProperty(customer.getChecking().getNumber());
        savingsNumber =
                new SimpleIntegerProperty(customer.getSavings().getNumber());
        creditNumber =
                new SimpleIntegerProperty(customer.getCredit().getNumber());
        fullName = new SimpleStringProperty(customer.getFullName());
        button = new Button("View Customer");
        button.setOnAction(e -> {
            System.out.println("Here bro");
        });
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
}
