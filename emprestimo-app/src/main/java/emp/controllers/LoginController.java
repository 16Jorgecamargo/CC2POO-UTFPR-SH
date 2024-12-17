package emp.controllers;

import emp.App;
import emp.models.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML
    private TextField campoUsuario;

    @FXML
    private PasswordField campoSenha;

    @FXML
    private CheckBox lembrarMe;

    @FXML
    private Label mensagemErro;

    private Usuario usuarioRoot;

    @FXML
    private void initialize() {
        mensagemErro.setVisible(false);
        usuarioRoot = new Usuario("1", "admin@sistema.com", "1");
    }

    @FXML
    private void fazerLogin() {
        String usuario = campoUsuario.getText();
        String senha = campoSenha.getText();

        if (validarCredenciais(usuario, senha)) {
            try {
                App.setRoot("dashboard");
            } catch (Exception e) {
                exibirErro("Erro ao carregar dashboard");
                e.printStackTrace();
            }
        } else {
            exibirErro("Usuário ou senha inválidos");
        }
    }

    @FXML
    private void recuperarSenha() {
        exibirErro("Link de recuperação enviado para o email cadastrado");
    }

    @FXML
    private void criarConta() {
        exibirErro("Funcionalidade em desenvolvimento");
    }

    private boolean validarCredenciais(String usuario, String senha) {
        return (usuario.equals(usuarioRoot.getNomeUsuario()) ||
                usuario.equals(usuarioRoot.getEmail())) &&
                usuarioRoot.autenticar(senha);
    }

    private void exibirErro(String mensagem) {
        mensagemErro.setText(mensagem);
        mensagemErro.setVisible(true);
    }
}