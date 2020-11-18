package app.controllers;

import app.*;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * IOExceptions should not be caught, we want to know when the program crashes.
 */
public class MainMenuController extends RunBankController {
    @FXML Button customerLoginButton;
    @FXML Button createCustomerButton;
    @FXML Button bankManagerButton;
    @FXML Button transactionButton;
    @FXML Button exitButton;

    /**
     * Opens customer login and customer menu.
     *
     * @param actionEvent Not used.
     * @throws IOException Logical error in code.
     */
    public void CustomerLogin(ActionEvent actionEvent) throws IOException {
        customerLoginButton.getScene().getWindow().hide();

        // Move on to Login Customer menu.
        FXMLLoader loginLoader =
                new FXMLLoader(getClass().getResource(CUSTOMER_LOGIN));
        Parent customerLoginRoot = loginLoader.load();

        CustomerLoginController
                customerLoginController = loginLoader.getController();
        customerLoginController.enterData(bankDB);
        customerLoginController.setNeedsPassword(true);

        // Moves scene.
        Stage customerLoginStage = new Stage();
        customerLoginStage.setScene(new Scene(customerLoginRoot));
        customerLoginStage.showAndWait();

        Optional<Customer> customer = customerLoginController.getCustomer();
        if (customer.isEmpty()) {
            ((Stage) customerLoginButton.getScene().getWindow()).show();
            return;
        }

        // Generic move.
        moveScene(CUSTOMER_MENU, customer);
    }

    /**
     * Opens create customer window.
     *
     * @param actionEvent Not used.
     * @throws IOException Logical errors in code if thrown.
     */
    public void CreateCustomer(ActionEvent actionEvent) throws IOException {
        exit(exitButton);
        moveScene(CREATE_CUSTOMER, Optional.empty());
        System.out.println("CreateCustomerButton");
    }

    /**
     * Opens bank manager window.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical errors in code.
     */
    public void bankManagers(ActionEvent actionEvent) throws IOException {
        System.out.println("Bank manager");
        exit(exitButton);
        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(BANK_MANAGER_MENU));
        Parent root = loader.load();

        BankManagerMenuController bankManagerMenuController =
                loader.getController();
        bankManagerMenuController.enterData(this.bankDB);
        bankManagerMenuController.start();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * Opens transaction window.
     *
     * @param actionEvent Not used.
     * @throws IOException If thrown logical errors in code.
     */
    public void transaction(ActionEvent actionEvent) throws IOException {
        exit(exitButton);
        moveScene(TRANSACTION_MENU, Optional.empty());
    }

    /**
     * Exits gracefully from process.
     *
     * @param actionEvent Not used.
     */
    public void exit(ActionEvent actionEvent) {
        exit();
    }
}
