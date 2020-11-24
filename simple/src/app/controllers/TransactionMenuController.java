package app.controllers;

import app.ActionReader;
import app.InvalidTransaction;
import app.NullCustomer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 *
 * Controls menu to perfrom actions.
 */
public class TransactionMenuController extends RunBankController {
    @FXML Button backButton;
    @FXML TextField textField;

    /**
     * Reads transaction and process transaction file.
     *
     * @param actionEvent Not used.
     * @throws IOException IOException should not be thrown, only for file
     * retrieval.
     */
    public void performTransactions(
            ActionEvent actionEvent
    ) throws IOException {
        if (textField.getText().length() == 0) {
            AlertBox.display(ERROR, "Please enter file name");
            return;
        }
        try {
            readAndProcessTransactions();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            AlertBox.display(ERROR, "Failed to find file");
        }
    }

    private void readAndProcessTransactions() throws FileNotFoundException {
        File transactionFile = new File(textField.getText());
        Scanner fileScanner = new Scanner(transactionFile);

        ActionReader actionReader = new ActionReader(this.bankDB);
        fileScanner.next();

        // Process.
        while (fileScanner.hasNextLine()) {
            processTransactions(fileScanner, actionReader);
        }
        AlertBox.display(SUCCESS, "File was process");
    }

    private void processTransactions(Scanner fileScanner, ActionReader actionReader) {
        try{
            actionReader.process(fileScanner.nextLine());
        } catch (InvalidTransaction e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (RuntimeException e){
            System.out.println(e.getMessage());
        }
    }


    /**
     * Goes back to main menu.
     * @param actionEvent Not used.
     * @throws IOException Something has gone wrong.
     */
    public void back(ActionEvent actionEvent) throws IOException {
        exit(backButton);
        moveScene(MAIN_MENU, new NullCustomer());
    }
}
