package app.controllers.fxml;

import app.Customer;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Wrapper Class of customer. So it can be viewed in Javafx application. 
 */
public class CustomerWrapperFX {
    private final Customer customer;
    private final SimpleIntegerProperty id;
    private final SimpleIntegerProperty checkingNumber;
    private final SimpleIntegerProperty savingNumber;
    private final SimpleIntegerProperty creditNumber;
    private final SimpleStringProperty fullName;

    public CustomerWrapperFX(Customer customer) {
        this.customer = customer;
        id = new SimpleIntegerProperty(customer.getId());
        checkingNumber =
                new SimpleIntegerProperty(customer.getChecking().getNumber());
        savingNumber =
                new SimpleIntegerProperty(customer.getSavings().getNumber());
        creditNumber =
                new SimpleIntegerProperty(customer.getCredit().getNumber());
        fullName = new SimpleStringProperty(customer.getFullName());
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleIntegerProperty checkingNumberProperty() {
        return checkingNumber;
    }

    public SimpleIntegerProperty savingNumberProperty() {
        return savingNumber;
    }

    public SimpleIntegerProperty creditNumberProperty() {
        return creditNumber;
    }

    public String getFullName() {
        return fullName.get();
    }
}
