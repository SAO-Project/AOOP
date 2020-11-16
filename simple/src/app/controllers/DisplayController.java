package app.controllers;
import app.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.util.Optional;
import app.*;
import javafx.scene.Parent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.Scene;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.util.Optional;

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
     * @param message Message to display to the user.
     */
    public void setText(String message) {
        labelField.setText(message);
    }

    /**
     * Exit from the window.
     */
    public void back(ActionEvent actionEvent) {
        exit(backButton);
    }
}
