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
 *
 */
public class MainMenuController extends RunBankController {
    @FXML Button customerLoginButton;
    @FXML Button createCustomerButton;
    @FXML Button bankManagerButton;
    @FXML Button transactionButton;
    @FXML Button exitButton;

    public void CustomerLogin(ActionEvent actionEvent) throws IOException {
        exit(customerLoginButton);

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
            return;
        }

        // Move on to customer menu.
        FXMLLoader customerMenuLoader =
                new FXMLLoader(getClass().getResource(CUSTOMER_MENU));
        Parent customerMenuRoot = customerMenuLoader.load();

        CustomerMenuController
                customerMenuController = customerMenuLoader.getController();
        customerMenuController.enterData(bankDB);
        customerMenuController.setCustomer(customer.get());

        Stage customerMenuStage = new Stage();
        customerMenuStage.setScene(new Scene(customerMenuRoot));
        customerMenuStage.show();
        System.out.println("Customer has been logged in");
    }

    public void CreateCustomer(ActionEvent actionEvent) {
        System.out.println("CreateCustomerButton");
    }

    public void bankManagers(ActionEvent actionEvent) {
        System.out.println("Bank Manager");
    }

    public void transaction(ActionEvent actionEvent) {
        System.out.println("Transaction Button");
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("Exit");
        System.exit(0);
    }
}
