package app.controllers;

import app.FileUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/18/20
 *
 * First window to display.
 */
public class BankMenuController extends RunBankController {
    @FXML TextField textField;
    @FXML Button backButton;

    /**
     * Reads give file from textField.
     *
     * @param actionEvent Not used.
     */
    public void performRead(ActionEvent actionEvent) {
        if (textField.getText().length() == 0) {
            AlertBox.display(ERROR, "Please enter file name");
            return;
        }
        try {
            // Opens main menu.
            this.bankDB = FileUtil.readFileV2(textField.getText());
            exit(backButton);
            moveScene(MAIN_MENU, Optional.empty());
        } catch (IOException e) {
            AlertBox.display(ERROR, e.getMessage());
        }
    }

    /**
     * Exits bank.
     *
     * @param actionEvent Not used.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
