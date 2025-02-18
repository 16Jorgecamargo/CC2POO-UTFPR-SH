package emp.controllers;

import emp.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import emp.dao.ClienteDAO;

import java.sql.SQLException;

/**
 * Controlador para o formulário de cadastro ou edição de Cliente.
 * Responsável por exibir os campos de entrada e salvar os dados do cliente no banco de dados.
 *
 * Interage com os seguintes elementos FXML:
 * - Label do título do formulário.
 * - TextFields para nome, CPF, telefone, email e endereço.
 * - CheckBox para indicar se o cliente está ativo.
 *
 * Utiliza ClienteDAO para persistir os dados na tabela de clientes.
 */
public class FormularioClienteController {
    /** Label que exibe o título do formulário */
    @FXML private Label tituloFormulario;
    /** Campo de texto para o nome do cliente */
    @FXML private TextField campoNome;
    /** Campo de texto para o CPF do cliente */
    @FXML private TextField campoCPF;
    /** Campo de texto para o telefone do cliente */
    @FXML private TextField campoTelefone;
    /** Campo de texto para o email do cliente */
    @FXML private TextField campoEmail;
    /** CheckBox que indica se o cliente está ativo */
    @FXML private CheckBox campoAtivo;
    /** Campo de texto para o endereço do cliente */
    @FXML private TextField campoEndereco;

    /** Estágio da janela do formulário */
    private Stage dialogStage;
    /** Cliente sendo criado ou editado */
    private Cliente cliente;
    /** Indica se o usuário confirmou a operação de salvar */
    private boolean okClicado = false;
    /** DAO responsável pelas operações de banco de dados relacionadas a Cliente */
    private ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Inicializa o controlador.
     * Esse método é chamado automaticamente após o carregamento do FXML.
     */
    @FXML
    private void initialize() {
    }

    /**
     * Define o estágio (janela) do diálogo.
     *
     * @param dialogStage Estágio a ser associado ao formulário.
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Configura o cliente a ser editado e preenche os campos com os dados atuais.
     * Se o cliente for nulo, configura o formulário para a criação de novo cliente.
     *
     * @param cliente Objeto Cliente com os dados a serem editados ou nulo para novo cadastro.
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;

        if (cliente != null) {
            // Modo edição
            tituloFormulario.setText("Editar Cliente");
            campoNome.setText(cliente.getNome());
            campoCPF.setText(cliente.getCpf());
            campoTelefone.setText(cliente.getTelefone());
            campoEmail.setText(cliente.getEmail());
            campoAtivo.setSelected(cliente.isAtivo());
            campoEndereco.setText(cliente.getEndereco());
        } else {
            // Modo novo cliente
            tituloFormulario.setText("Novo Cliente");
        }
    }

    /**
     * Retorna o cliente atual.
     *
     * @return Objeto Cliente atualmente manipulado.
     */
    public Cliente getCliente() {
        return cliente;

    }

    /**
     * Retorna se o usuário confirmou a operação de salvar.
     *
     * @return true se o usuário clicou em salvar, false caso contrário.
     */
    public boolean isOkClicado() {
        return okClicado;
    }

    /**
     * Salva as informações do cliente.
     * Valida os campos de entrada e utiliza ClienteDAO para inserir ou atualizar o cliente no banco de dados.
     * Exibe uma mensagem de alerta em caso de erro na operação.
     */
    @FXML
    private void salvar() {
        if (validarEntradas()) {
            try {
                if (cliente == null) {
                    cliente = new Cliente(
                        campoNome.getText(),
                        campoCPF.getText(),
                        campoTelefone.getText(),
                        campoEmail.getText()
                    );
                    cliente.setEndereco(campoEndereco.getText());
                    cliente.setAtivo(true);
                    clienteDAO.inserir(cliente);
                } else {
                    cliente.setNome(campoNome.getText());
                    cliente.setTelefone(campoTelefone.getText());
                    cliente.setEmail(campoEmail.getText());
                    cliente.setEndereco(campoEndereco.getText());
                    cliente.setAtivo(campoAtivo.isSelected());
                    clienteDAO.atualizar(cliente);
                }

                okClicado = true;
                dialogStage.close();

            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao salvar cliente");
                alert.setContentText("Ocorreu um erro ao salvar o cliente: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Cancela a operação e fecha o formulário.
     */
    @FXML
    private void cancelar() {
        dialogStage.close();
    }

    /**
     * Valida os campos de entrada do formulário.
     * Verifica se os campos obrigatórios (nome, CPF, telefone e email) estão preenchidos.
     * Em caso de erro, exibe um alerta informando os campos incorretos.
     *
     * @return true se todos os campos obrigatórios estiverem válidos, false caso contrário.
     */
    private boolean validarEntradas() {
        String mensagemErro = "";

        if (campoNome.getText() == null || campoNome.getText().trim().isEmpty()) {
            mensagemErro += "Nome inválido!\n";
        }
        if (campoCPF.getText() == null || campoCPF.getText().trim().isEmpty()) {
            mensagemErro += "CPF inválido!\n";
        }
        if (campoTelefone.getText() == null || campoTelefone.getText().trim().isEmpty()) {
            mensagemErro += "Telefone inválido!\n";
        }
        if (campoEmail.getText() == null || campoEmail.getText().trim().isEmpty()) {
            mensagemErro += "E-mail inválido!\n";
        }

        if (mensagemErro.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Campos Inválidos");
            alert.setHeaderText("Por favor, corrija os campos inválidos");
            alert.setContentText(mensagemErro);
            alert.showAndWait();
            return false;
        }
    }
}

