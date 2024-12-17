package emp.controllers;

import emp.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.time.format.DateTimeFormatter;

public class DetalhesClienteController {
    @FXML private Label nomeClienteLabel;
    @FXML private Label cpfClienteLabel;
    @FXML private Label statusClienteLabel;
    @FXML private Label enderecoLabel;
    @FXML private Label telefoneLabel;
    @FXML private Label emailLabel;
    @FXML private Label dataCadastroLabel;
    @FXML private Label valorTotalLabel;
    @FXML private Label totalPagoLabel;
    @FXML private Label saldoRestanteLabel;
    @FXML private TableView<EmprestimoRow> tabelaEmprestimos;
    @FXML private BarChart<String, Number> graficoPagamentos;
    @FXML private LineChart<String, Number> graficoTendencia;
    @FXML private ListView<String> listaNotificacoes;

    private Stage dialogStage;
    private Cliente cliente;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        preencherDadosCliente();
    }

    private void preencherDadosCliente() {
        if (cliente != null) {
            nomeClienteLabel.setText(cliente.getNome());
            cpfClienteLabel.setText("CPF: " + cliente.getCpf());
            statusClienteLabel.setText("Status: " + (cliente.isAtivo() ? "Ativo" : "Inativo"));
            telefoneLabel.setText(cliente.getTelefone());
            emailLabel.setText(cliente.getEmail());
            enderecoLabel.setText(cliente.getEndereco());
            dataCadastroLabel.setText(cliente.getDataCadastro()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            carregarDadosFinanceiros();
            carregarHistoricoEmprestimos();
            carregarGraficos();
            carregarNotificacoes();
        }
    }

    @FXML
    private void fecharDetalhes() {
        dialogStage.close();
    }

    @FXML
    private void renegociarEmprestimo() {
        // Implementar lógica de renegociação
    }

    @FXML
    private void enviarLembrete() {
        // Implementar lógica de envio de lembrete
    }

    // Classes auxiliares e métodos de carregamento de dados
    private void carregarDadosFinanceiros() {
        if (cliente != null) {
            valorTotalLabel.setText(String.format("R$ %.2f", cliente.getTotalEmprestado()));
            totalPagoLabel.setText(String.format("R$ %.2f", cliente.getTotalPago()));
            saldoRestanteLabel.setText(String.format("R$ %.2f", 
                cliente.getTotalEmprestado() - cliente.getTotalPago()));
        }
    }

    private void carregarHistoricoEmprestimos() {
        // Implementar carregamento do histórico
    }

    private void carregarGraficos() {
        // Implementar carregamento dos gráficos
    }

    private void carregarNotificacoes() {
        // Implementar carregamento das notificações
    }

    // Classe para representar uma linha na tabela de empréstimos
    public static class EmprestimoRow {
        // Implementar propriedades da linha
    }
}