package emp.controllers;

import emp.App;
import emp.models.Customer;
import emp.models.Loan;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;

public class LoanRegistrationController {
    @FXML
    private ComboBox<Customer> customerCombo;
    @FXML
    private TextField amountField;
    @FXML
    private TextField interestRateField;
    @FXML
    private ComboBox<String> interestTypeCombo;
    @FXML
    private TextField termField;
    @FXML
    private ComboBox<String> termTypeCombo;
    @FXML
    private Label simulatedAmountLabel;
    @FXML
    private TableView<Loan> loansTable;
    @FXML
    private TableColumn<Loan, String> customerColumn;
    @FXML
    private TableColumn<Loan, Double> amountColumn;
    @FXML
    private TableColumn<Loan, Double> interestRateColumn;
    @FXML
    private TableColumn<Loan, String> termColumn;
    @FXML
    private TableColumn<Loan, Double> totalAmountColumn;
    @FXML
    private TableColumn<Loan, String> statusColumn;

    private ObservableList<Loan> loans = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        interestTypeCombo.setItems(FXCollections.observableArrayList("Simples", "Composto"));
        termTypeCombo.setItems(FXCollections.observableArrayList("Meses", "Semanas"));

        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer().getName()));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        interestRateColumn.setCellValueFactory(new PropertyValueFactory<>("interestRate"));
        termColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTerm() + " " +
                data.getValue().getTermType().toLowerCase()));
        totalAmountColumn.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loansTable.setItems(loans);
    }

    @FXML
    private void handleOpenSimulation() throws IOException {
        App.setRoot("loan-simulation");
    }

    @FXML
    private void handleSave() {
        if (validateFields()) {
            Loan loan = createLoanFromFields();
            loans.add(loan);
            clearFields();
            AlertHelper.showSuccessMessage("Empréstimo registrado com sucesso!");
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
        simulatedAmountLabel.setText("");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }

    private Loan createLoanFromFields() {
        return new Loan(
                customerCombo.getValue(),
                Double.parseDouble(amountField.getText()),
                Double.parseDouble(interestRateField.getText()),
                interestTypeCombo.getValue().toUpperCase(),
                Integer.parseInt(termField.getText()),
                termTypeCombo.getValue().toUpperCase());
    }

    private void clearFields() {
        customerCombo.setValue(null);
        amountField.clear();
        interestRateField.clear();
        interestTypeCombo.setValue(null);
        termField.clear();
        termTypeCombo.setValue(null);
    }

    private boolean validateFields() {
        if (customerCombo.getValue() == null ||
                amountField.getText().isEmpty() ||
                interestRateField.getText().isEmpty() ||
                interestTypeCombo.getValue() == null ||
                termField.getText().isEmpty() ||
                termTypeCombo.getValue() == null) {

            AlertHelper.showSuccessMessage("Por favor, preencha todos os campos!");
            return false;
        }

        try {
            Double.parseDouble(amountField.getText());
            Double.parseDouble(interestRateField.getText());
            Integer.parseInt(termField.getText());
        } catch (NumberFormatException e) {
            AlertHelper.showSuccessMessage("Por favor, insira valores numéricos válidos!");
            return false;
        }

        return true;
    }
}