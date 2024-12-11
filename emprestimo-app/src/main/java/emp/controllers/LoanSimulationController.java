package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import java.io.IOException;

public class LoanSimulationController {
    @FXML private TextField amountField;
    @FXML private TextField interestRateField;
    @FXML private TextField termField;
    @FXML private ComboBox<String> termTypeCombo;
    @FXML private ComboBox<String> interestTypeCombo;
    @FXML private Label resultAmountLabel;
    @FXML private Label resultInterestLabel;
    @FXML private Label resultTotalLabel;
    @FXML private Label resultInstallmentLabel;

    @FXML
    public void initialize() {
        termTypeCombo.setItems(FXCollections.observableArrayList("Meses", "Semanas"));
        interestTypeCombo.setItems(FXCollections.observableArrayList("Simples", "Composto"));
        
        termTypeCombo.getSelectionModel().selectFirst();
        interestTypeCombo.getSelectionModel().selectFirst();
    }

    @FXML
    private void handleSimulate() {
        if (!validateFields()) return;

        try {
            double amount = Double.parseDouble(amountField.getText());
            double interestRate = Double.parseDouble(interestRateField.getText());
            int term = Integer.parseInt(termField.getText());
            boolean isCompound = interestTypeCombo.getValue().equals("Composto");

            double totalAmount;
            if (isCompound) {
                totalAmount = amount * Math.pow(1 + (interestRate/100), term);
            } else {
                totalAmount = amount * (1 + (interestRate/100 * term));
            }

            double totalInterest = totalAmount - amount;
            double installmentValue = totalAmount / term;

            resultAmountLabel.setText(String.format("R$ %.2f", amount));
            resultInterestLabel.setText(String.format("R$ %.2f", totalInterest));
            resultTotalLabel.setText(String.format("R$ %.2f", totalAmount));
            resultInstallmentLabel.setText(String.format("R$ %.2f", installmentValue));

        } catch (NumberFormatException e) {
            AlertHelper.showSuccessMessage("Por favor, insira valores numéricos válidos!");
        }
    }

    @FXML
    private void handleClear() {
        amountField.clear();
        interestRateField.clear();
        termField.clear();
        termTypeCombo.getSelectionModel().selectFirst();
        interestTypeCombo.getSelectionModel().selectFirst();
        
        resultAmountLabel.setText("R$ 0,00");
        resultInterestLabel.setText("R$ 0,00");
        resultTotalLabel.setText("R$ 0,00");
        resultInstallmentLabel.setText("R$ 0,00");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("loan-registration");
    }

    private boolean validateFields() {
        if (amountField.getText().isEmpty() || 
            interestRateField.getText().isEmpty() || 
            termField.getText().isEmpty()) {
            AlertHelper.showSuccessMessage("Por favor, preencha todos os campos!");
            return false;
        }
        return true;
    }
}