package app.controllers;

import app.IBankDB;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 *
 */
abstract class RunBankController {
    protected Button button;
    protected final static String ERROR = "ERROR";
    protected final static String MAIN_MENU = "fxml/MainMenu.fxml";
    protected final static String CUSTOMER_LOGIN = "fxml/CustomerLogin.fxml";
    protected final static String CUSTOMER_MENU = "fxml/CustomerMenu.fxml";

    // Model
    IBankDB bankDB;

    public void enterData(IBankDB bankDB) {
        this.bankDB = bankDB;
    }

    /**
     * Moves from scene to scene.
     * @param fxmlFile Where the next scene is located. fxmlFile must have a
     *                 controller.
     * @param title Title of window.
     * @throws IOException If thrown something has gone wrong. Do not catch
     * for it.
     */
    private Parent createRoot(
            String fxmlFile,
            RunBankController controller,
            String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();

        controller = loader.getController();
        controller.enterData(bankDB);
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
    public void moveScene(String fxmlFile,
                          RunBankController con,
                          String title) throws IOException {
        moveScene(createRoot(fxmlFile, con, title));
    }

    public void moveScene(Parent root) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Exits from scene.
    protected void exit(Button button) {
        ((Stage) button.getScene().getWindow()).close();
    }
}
