package app;

import app.controllers.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/14/20
 */
public class RunBankV2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        IBankDB bankCustomerData = FileUtil.readFile(RunBank.askForFileName());

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("controllers/fxml/MainMenu.fxml"));
        Parent root = loader.load();
        MainMenuController mainMenuController = loader.getController();

        // Send data to scene
        mainMenuController.enterData(bankCustomerData);

        stage.setTitle("Main Menu");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
