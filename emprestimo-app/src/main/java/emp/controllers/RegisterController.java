package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField userField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private PasswordField passwordField;
    
    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Label errorMessage;

    @FXML
    private RadioButton userRadio;
    
    @FXML
    private RadioButton agiotaRadio;
    
    @FXML
    private ToggleGroup userType;

    @FXML
    private void handleRegister() {
        String username = userField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorMessage.setText("Por favor, preencha todos os campos!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorMessage.setText("As senhas não coincidem!");
            return;
        }

        try {            
            AlertHelper.showSuccessMessage("Cadastro realizado com sucesso!");
            App.setRoot("login");
        } catch (IOException e) {
            errorMessage.setText("Erro ao finalizar cadastro");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("login");
    }
}
