package emp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;

import emp.App;
import javafx.collections.FXCollections;

public class LoginController {

    @FXML
    private TextField userField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> userTypeCombo;

    @FXML
    private Label errorMessage;

    @FXML
    public void initialize() {
        // Inicializa o ComboBox com os tipos de usuário
        userTypeCombo.setItems(FXCollections.observableArrayList(
                "Cliente",
                "Agiota"));
        userTypeCombo.getSelectionModel().selectFirst();
        errorMessage.setText("");
    }

    @FXML
    private void handleLogin() throws IOException {
        String username = userField.getText();
        String password = passwordField.getText();
        String userType = userTypeCombo.getValue();

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.setText("Por favor, preencha todos os campos!");
            return;
        }

        // TODO logica de autenticação
        if (username.equals("1") && password.equals("1") && userType.equals("Agiota")) {
            App.setRoot("agiota-main-menu");
            return;

        } else if (username.equals("1") && password.equals("1") && userType.equals("Cliente")) {
            App.setRoot("cliente-main-menu");
            return;

        } else {
            errorMessage.setText("Usuário, senha ou tipo de usuário inválidos!");
        }
    }

    @FXML
    private void handleForgotPassword() {
        try {
            App.setRoot("forgot-password");
        } catch (IOException e) {
            errorMessage.setText("Erro ao abrir tela de recuperação de senha");
        }
    }

    @FXML
    private void handleCreateAccount() {
        try {
            App.setRoot("register");
        } catch (IOException e) {
            errorMessage.setText("Erro ao abrir tela de registro");
        }
    }
}