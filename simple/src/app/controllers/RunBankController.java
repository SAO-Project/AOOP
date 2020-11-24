package app.controllers;

import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Optional;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Parent for all controllers.
 */
abstract class RunBankController {
    // fxml files
    protected final static String MAIN_MENU = "fxml/MainMenu.fxml";
    protected final static String CUSTOMER_LOGIN = "fxml/CustomerLogin.fxml";
    protected final static String CUSTOMER_MENU = "fxml/CustomerMenu.fxml";
    protected final static String GET_AMOUNT = "fxml/GetAmount.fxml";
    protected final static String ACCOUNT = "fxml/GetAccount.fxml";
    protected final static String DISPLAY = "fxml/Display.fxml";
    protected final static String BANK_MANAGER_MENU = "fxml/BankManagerMenu" +
            ".fxml";
    protected final static String CREATE_CUSTOMER = "fxml/CreateCustomerMenu" +
            ".fxml";
    public final static String BANK_MANAGER_CUSTOMER_MENU = "fxml" +
            "/BankManagerCustomerMenu.fxml";
    protected final static String TRANSACTION_MENU = "fxml/TransactionMenu" +
            ".fxml";

    // Messages
    protected final static String ERROR = "ERROR";
    protected final static String SUCCESS = "Success";
    protected final static String OPERATION_SUCCESS = "Operation was " +
            "successful";

    // Model
    protected IBankDB bankDB;
    protected Customer customer = new NullCustomer();

    /**
     * Moves from scene to scene.
     * @param fxmlFile Where the next scene is located. fxmlFile must have a
     *                 controller.
     * @param customer Contains customer
     * @throws IOException If thrown something has gone wrong. Do not catch
     * for it.
     */
    private Parent createRoot(
            String fxmlFile, Customer customer) throws IOException {
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
     * @throws IOException If thrown something has gone wrong. Do not catch
     * for it.
     */
    public void moveScene(
            String fxmlFile,
            Customer customer
    ) throws IOException {

        // TODO(Edd1e) Extract these if statements out, to another util class.
        if (fxmlFile.equals(MAIN_MENU)) {
            if (customer.getOptional().isPresent()) {
                throw new IOException("Customer is empty");
            }
        }

        if (customer.getOptional().isEmpty() && fxmlFile.equals(CUSTOMER_LOGIN)) {
            throw new IOException("while attempting to move to " + fxmlFile +
                    "Customer was empty!!!!");
        }
        moveScene(createRoot(fxmlFile, customer));
    }

    /**
     * Move scene based on root.
     * @param root contains the root of the scene.
     */
    public void moveScene(Parent root) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Set customer for the window.
     * @param customer sets class customer.
     */
    protected void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * Gets customer for the controller.
     *
     * @return Optional customer. 
     */
    protected Optional<Customer> getCustomer() {
        return customer.getOptional();
    }

    /**
     * Checks if the customer is not null.
     *
     * @return If true, controller contains a customer.
     */
    protected boolean containsCustomer() {
        return customer.getOptional().isPresent();
    }

    /**
     * Sets the model for the controller to retrieve data from.
     *
     * @param bankDB Contains all customer data.
     */
    public void enterData(IBankDB bankDB) {
        this.bankDB = bankDB;
    }

    /**
     * Opens an amount window.
     *
     * @param message Allows you to set a custom message.
     * @return If empty, user did not want to set amount after all.
     * @throws IOException Something has gone wrong if thrown.
     */
    protected Optional<Double> getAmount(String message) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(GET_AMOUNT));
        Parent root = fxmlLoader.load();

        GetAmountController getAmountController = fxmlLoader.getController();
        getAmountController.setMessage(message);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return getAmountController.getAmount();
    }

    /**
     * Opens account retrieval window. Customer should have already been set
     * prior.
     *
     * @return If account is empty, customer did not want to get an account.
     * @throws IOException Something has gone wrong.
     */
    protected Optional<Account> getAccount(String message) throws IOException {
        containsCustomer();
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource(ACCOUNT));
        Parent root = fxmlLoader.load();

        GetAccountController getAccountController = fxmlLoader.getController();
        getAccountController.setCustomer(customer);
        getAccountController.setMessage(message);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();

        return getAccountController.getAccount();
    }

    /**
     * Displays a large message.
     *
     * @param message Message to display.
     * @throws IOException If program fails, should throws.
     */
    protected void displayMessage(String message) throws IOException {
        FXMLLoader fxmlLoader =
                new FXMLLoader(getClass().getResource(DISPLAY));
        Parent root = fxmlLoader.load();

        DisplayController displayController = fxmlLoader.getController();
        System.out.println("Messages: ");
        System.out.println(message + "\n");
        displayController.setText(message);

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.showAndWait();
    }

    /**
     * Exits from window.
     *
     * @param button Source of scene.
     */
    protected void exit(Button button) {
        ((Stage) button.getScene().getWindow()).close();
    }

    /**
     * Upon program termination. Writes new balance.
     */
    protected void exit() {
        (new CSVFile(bankDB)).writeCSV();
        try{
            FileUtil.writeTransactions(bankDB.getTransactions());
        }catch(IOException e){
            AlertBox.display(ERROR, "There was an error printing the transactions");
        }
        System.exit(0);
    }
}
