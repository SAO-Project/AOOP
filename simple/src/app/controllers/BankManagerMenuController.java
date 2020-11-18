package app.controllers;

import app.controllers.fxml.CustomerWrapperFX;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * @author Edd1e234
 * @version 1.0
 * @since 11/17/20
 */
public class BankManagerMenuController extends RunBankController {
    @FXML TextField search; 
    @FXML TableView<CustomerWrapperFX> table;
    @FXML TableColumn<CustomerWrapperFX, Integer> idColumn;
    @FXML TableColumn<CustomerWrapperFX, Integer> checkingColumn;
    @FXML TableColumn<CustomerWrapperFX, Integer> creditColumn;
    @FXML TableColumn<CustomerWrapperFX, Integer> savingsColumn;
    @FXML TableColumn<CustomerWrapperFX, String> nameColumn;

    // Contains all the data.
    private final ObservableList<CustomerWrapperFX> customerList =
            FXCollections.observableArrayList();

    /**
     * Builds table view
     */
    public void start() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        checkingColumn.setCellValueFactory(new PropertyValueFactory<>(
                "checkingNumber"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>(
                "creditNumber"));
        savingsColumn.setCellValueFactory(new PropertyValueFactory<>(
                "savingsNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>(
                "fullName"));
        bankDB.getCustomers().forEach(
                customer -> customerList.add(new CustomerWrapperFX(customer)));
        
        FilteredList<CustomerWrapperFX> filteredList =
                new FilteredList<>(customerList, b -> true);
        search.textProperty().addListener((observable, oldValues, newValues) -> {
            filteredList.setPredicate(customer -> {
                // Searches for values here.
                if (newValues == null || newValues.isEmpty()) {
                    return true; 
                }
                
                String lowerCaseFilter = newValues.toLowerCase();
                
                if (customer.getFullName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(customer.getId()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(customer.getChecking()).contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(customer.getSavings()).contains(lowerCaseFilter)) {
                    return true;
                } else return String.valueOf(customer.getChecking()).contains(lowerCaseFilter);
            });
        });
        SortedList<CustomerWrapperFX> sortedList =
                new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public void back(ActionEvent actionEvent) {
    }
}
