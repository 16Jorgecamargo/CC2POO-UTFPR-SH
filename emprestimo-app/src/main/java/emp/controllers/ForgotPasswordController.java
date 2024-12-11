package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.io.IOException;

public class ForgotPasswordController {
    @FXML
    private TextField userField;
    
    @FXML
    private Label errorMessage;

    @FXML
    private void handleRecover() {
        String username = userField.getText();

        if (username.isEmpty()) {
            errorMessage.setText("Por favor, informe o nome de usuário!");
            return;
        }

        String password;
        switch (username.toLowerCase()) {
            case "user":
                password = "\tuser";
                break;
            case "agiota":
                password = "\tagiota";
                break;
            case "dev":
                password = "\tdev";
                break;
            default:
                password = null;
                break;
        }

        if (password != null) {
            AlertHelper.showSuccessMessage("A senha do usuário " + username + " é: " + password);
            try {
                App.setRoot("login");
            } catch (IOException e) {
                errorMessage.setText("Erro ao voltar para tela de login");
            }
        } else {
            errorMessage.setText("Usuário não encontrado!");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("login");
    }
}
