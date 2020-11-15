package app.controllers;

import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Optional;

import java.io.IOException;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 *
 */
abstract class RunBankController {
    protected final static String ERROR = "ERROR";
    protected final static String SUCCESS = "Success";
    protected final static String OPERATION_SUCCESS = "Operation was " +
            "successful";
    protected final static String MAIN_MENU = "fxml/MainMenu.fxml";
    protected final static String CUSTOMER_LOGIN = "fxml/CustomerLogin.fxml";
    protected final static String CUSTOMER_MENU = "fxml/CustomerMenu.fxml";
    protected final static String GET_AMOUNT = "fxml/GetAmount.fxml";
    protected final static String ACCOUNT = "fxml/GetAccount.fxml";

    // Model
    protected IBankDB bankDB;
    protected Optional<Customer> customer = Optional.empty();

    /**
     * Moves from scene to scene.
     * @param fxmlFile Where the next scene is located. fxmlFile must have a
     *                 controller.
     * @param title Title of window.
     * @throws IOException If thrown something has gone wrong. Do not catch
     * for it.
     */
    private Parent createRoot(
            String fxmlFile, Optional<Customer> customer) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        RunBankController controller = loader.getController();
        controller.enterData(bankDB);
        controller.setCustomer(customer);
        return root;
    }

    /**
     * Instantly moves file. When it is not necessary to do additional work
     * when it comes to making a scene change
     * @param fxmlFile Where the next scene is located. fxmlFile must have a
     *                 controller.
     * @param con Controller of @param fxmlFile.
     * @param title Title of window.
     * @throws IOException If thrown something has gone wrong. Do not catch
     * for it.
     */
    public void moveScene(
            String fxmlFile, Optional<Customer> customer) throws IOException {
        moveScene(createRoot(fxmlFile, customer));
    }

    public void moveScene(Parent root) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    protected void setCustomer(Optional<Customer> customer) {
        this.customer = customer;
    }

    protected Optional<Customer> getCustomer() {
        return getCustomer();
    }

    protected boolean containsCustomer() throws IOException {
        if (customer.isPresent()) {
            return true;
        }
        throw new IOException("Customer was not set before hand");
    }

    public void enterData(IBankDB bankDB) {
        this.bankDB = bankDB;
    }

    protected Optional<Double> getAmount(String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GET_AMOUNT));
        Parent root = fxmlLoader.load();

        GetAmountController getAmountController = fxmlLoader.getController();
        getAmountController.setText(message);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return getAmountController.getAmount();
    }

    protected Optional<Account> getAccount() throws IOException {
        // TODO(Edd1e234): Add message feature
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource(ACCOUNT));
        Parent root = fxmlLoader.load();

        GetAccountController getAccountController = fxmlLoader.load();
        getAccountController.setCustomer(customer);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return getAccountController.getAccount();
    }

    // Exits from scene.
    protected void exit(Button button) {
        ((Stage) button.getScene().getWindow()).close();
    }
}
