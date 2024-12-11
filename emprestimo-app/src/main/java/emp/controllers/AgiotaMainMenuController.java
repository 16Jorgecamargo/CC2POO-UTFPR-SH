package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import java.io.IOException;

public class AgiotaMainMenuController {

    @FXML
    private void handleCustomerRegistration() throws IOException {
        App.setRoot("customer-registration");
    }

    @FXML
    private void handleLoanRegistration() throws IOException {
        App.setRoot("loan-registration");
    }

    @FXML
    private void handlePaymentMonitoring() throws IOException {
        App.setRoot("payment-monitoring");
    }

    @FXML
    private void handleReports() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Relatórios");
    }

    @FXML
    private void handleControlPanel() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Painel de Controle");
    }

    @FXML
    private void handleLoanSimulation() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Simulação de Empréstimos");
    }

    @FXML
    private void handleSettings() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Configurações");
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("login");
    }
}