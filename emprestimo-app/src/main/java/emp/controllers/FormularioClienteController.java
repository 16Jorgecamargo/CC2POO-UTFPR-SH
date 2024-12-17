package emp.controllers;

import emp.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class FormularioClienteController {
    @FXML
    private Label tituloFormulario;
    @FXML
    private TextField campoNome;
    @FXML
    private TextField campoCPF;
    @FXML
    private TextField campoTelefone;
    @FXML
    private TextField campoEmail;
    @FXML
    private CheckBox campoAtivo;
    @FXML
    private TextField campoEndereco;

    private Stage dialogStage;
    private Cliente cliente;
    private boolean okClicado = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

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

    public Cliente getCliente() {
        return cliente;

    }

    public boolean isOkClicado() {
        return okClicado;
    }

    @FXML
    private void salvar() {
        if (validarEntradas()) {
            if (cliente == null) {
                cliente = new Cliente(
                        campoNome.getText(),
                        campoCPF.getText(),
                        campoTelefone.getText(),
                        campoEmail.getText());
                        cliente.setEndereco(campoEndereco.getText());
            } else {
                cliente.setNome(campoNome.getText());
                cliente.setCpf(campoCPF.getText());
                cliente.setTelefone(campoTelefone.getText());
                cliente.setEmail(campoEmail.getText());
                cliente.setEndereco(campoEndereco.getText());
                cliente.setAtivo(campoAtivo.isSelected());
                
            }

            okClicado = true;
            dialogStage.close();
        }
    }

    @FXML
    private void cancelar() {
        dialogStage.close();
    }

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

