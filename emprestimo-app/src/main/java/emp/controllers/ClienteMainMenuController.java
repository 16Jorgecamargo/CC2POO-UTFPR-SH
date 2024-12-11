package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import java.io.IOException;

public class ClienteMainMenuController {

    @FXML
    private void handleLoanRequest() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Solicitação de Empréstimos");
    }

    @FXML
    private void handleLoanConsultation() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Consulta de Empréstimos");
    }

    @FXML
    private void handlePayment(){
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Pagamento de Empréstimos");
    }

    @FXML
    private void handlePaymentConsultation() {
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Consulta de Pagamentos");
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
