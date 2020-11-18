package app.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/16/20
 *
 * Displays text.
 */
public class DisplayController extends RunBankController {
    @FXML Label labelField;
    @FXML Button backButton;

    /**
     * Sets message to display to the user.
     *
     * @param message Message to display to the user.
     */
    public void setText(String message) {
        labelField.setText(message);
    }

    /**
     * Exit from the window.
     *
     * @param actionEvent Not used.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
