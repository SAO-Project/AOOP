package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 *
 * Runs first bank menu.
 */
public class RunBankV2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Displays first window.
     *
     * @param stage Scene for the first window to be shown.
     * @throws Exception Logical error if thrown.
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("controllers/fxml/BankMenu.fxml"));
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
