package emp.controllers;

import emp.App;
//import emp.utils.AlertHelper;
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
    private void handleReports() throws IOException {
        App.setRoot("report");
    }

    @FXML
    private void handleControlPanel() throws IOException {
        App.setRoot("control-panel");
    }

    @FXML
    private void handleSettings() throws IOException {
        App.setRoot("settings");
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("login");
    }
}