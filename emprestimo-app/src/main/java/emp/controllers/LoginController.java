package emp.controllers;

import emp.App;
import emp.models.Usuario;
import emp.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador responsável pelo processo de autenticação do usuário.
 * 
 * Gerencia a interface de login, validando o preenchimento dos campos, autenticando
 * o usuário através do UsuarioDAO e redirecionando para o dashboard em caso de sucesso.
 * Também fornece funcionalidades para recuperação de senha e criação de nova conta.
 *
 * Interage com os seguintes elementos FXML:
 * - TextField para usuário (campoUsuario)
 * - PasswordField para senha (campoSenha)
 * - CheckBox para lembrar o usuário (lembrarMe)
 * - Label para exibição de mensagens de erro (mensagemErro)
 */
public class LoginController {
    /** Campo de texto para inserir o usuário (geralmente o e-mail). */
    @FXML
    private TextField campoUsuario;

    /** Campo de senha para inserir a senha do usuário. */
    @FXML
    private PasswordField campoSenha;

    /** CheckBox para definir se o usuário deseja permanecer logado. */
    @FXML
    private CheckBox lembrarMe;

    /** Label para exibir mensagens de erro ocorridas no login. */
    @FXML
    private Label mensagemErro;

    /** DAO para realizar operações de autenticação na tabela de usuários do banco de dados. */
    private UsuarioDAO usuarioDAO;

    /**
     * Método de inicialização chamado automaticamente após o carregamento do FXML.
     * Inicializa a visibilidade da mensagem de erro e instância o UsuarioDAO.
     */
    @FXML
    private void initialize() {
        mensagemErro.setVisible(false);
        usuarioDAO = new UsuarioDAO();
    }

    /**
     * Tenta autenticar o usuário utilizando os dados inseridos.
     * Caso os campos estejam vazios ou a autenticação falhe, exibe uma mensagem de erro.
     * Se a autenticação for bem-sucedida, redireciona para a tela do dashboard.
     */
    @FXML
    private void fazerLogin() {
        String email = campoUsuario.getText();
        String senha = campoSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            exibirErro("Preencha todos os campos");
            return;
        }

        try {
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                // TODO: Armazenar usuário logado em sessão
                App.setRoot("dashboard");
            } else {
                exibirErro("Usuário ou senha inválidos");
            }
        } catch (Exception e) {
            exibirErro("Erro ao realizar login: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Simula o envio de um link para recuperação de senha.
     * Em ambiente real, este método deve implementar o envio de email para recuperação.
     */
    @FXML
    private void recuperarSenha() {
        exibirErro("Link de recuperação enviado para o email cadastrado");
    }

    /**
     * Redireciona o usuário para a tela de cadastro para a criação de uma nova conta.
     */
    @FXML
    private void criarConta() {
        try {
            App.setRoot("cadastro");
        } catch (Exception e) {
            exibirErro("Erro ao abrir tela de cadastro");
            e.printStackTrace();
        }
    }

    /**
     * Exibe uma mensagem de erro na interface.
     * 
     * @param mensagem A mensagem de erro a ser exibida.
     */
    private void exibirErro(String mensagem) {
        mensagemErro.setText(mensagem);
        mensagemErro.setVisible(true);
    }
}