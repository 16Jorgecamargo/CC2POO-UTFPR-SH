package emp.controllers;

import emp.models.Cliente;
import emp.models.Emprestimo;
import emp.models.Pagamento;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class RealizarPagamentoController {
    @FXML
    private TableView<Emprestimo> tabelaEmprestimos;
    @FXML
    private TableColumn<Emprestimo, String> colunaCliente;
    @FXML
    private TableColumn<Emprestimo, String> colunaId;
    @FXML
    private TableColumn<Emprestimo, String> colunaValorOriginal;
    @FXML
    private TableColumn<Emprestimo, String> colunaJuros;
    @FXML
    private TableColumn<Emprestimo, String> colunaTotal;
    @FXML
    private TableColumn<Emprestimo, String> colunaVencimento;
    @FXML
    private TableColumn<Emprestimo, String> colunaParcelas;

    @FXML
    private ListView<String> historicoList;
    @FXML
    private Label valorInicialLabel;
    @FXML
    private Label jurosLabel;
    @FXML
    private Label multaLabel;
    @FXML
    private Label totalLabel;
    @FXML
    private TextField valorPagamentoField;
    @FXML
    private ToggleGroup formaPagamentoGroup;

    private ObservableList<Emprestimo> emprestimos = FXCollections.observableArrayList();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    @FXML
    private void initialize() {
        if (tabelaEmprestimos != null) {
            configurarTabela();
        }
        if (historicoList != null) {
            configurarHistorico();
        }
        carregarEmprestimos();
    }

    private void configurarTabela() {
        colunaCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNome()));
        colunaId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colunaValorOriginal
                .setCellValueFactory(data -> new SimpleStringProperty(formatMoney(data.getValue().getValor())));
        colunaJuros.setCellValueFactory(data -> new SimpleStringProperty(formatMoney(data.getValue().getTotalJuros())));
        colunaTotal
                .setCellValueFactory(data -> new SimpleStringProperty(formatMoney(data.getValue().getTotalAPagar())));
        colunaVencimento.setCellValueFactory(
                data -> new SimpleStringProperty(data.getValue().getDataVencimento().format(dateFormatter)));
        colunaParcelas.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getParcelasPagas() + "/" + data.getValue().getPrazo()));

        tabelaEmprestimos.setItems(emprestimos);

        tabelaEmprestimos.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        atualizarDetalhes(newVal);
                    }
                });
    }

    private String formatMoney(double value) {
        return currencyFormatter.format(value);
    }

    private void atualizarDetalhes(Emprestimo emprestimo) {
        valorInicialLabel.setText(formatMoney(emprestimo.getValor()));
        jurosLabel.setText(formatMoney(emprestimo.getTotalJuros()));

        double multa = emprestimo.calcularJurosAtraso();
        multaLabel.setText(formatMoney(multa));

        double total = emprestimo.getTotalAPagar() + multa;
        totalLabel.setText(formatMoney(total));

        valorPagamentoField.setText(formatMoney(total).replace("R$", "").trim());
    }

    private void configurarHistorico() {
        historicoList.setItems(FXCollections.observableArrayList());
    }

    private void carregarEmprestimos() {
        emprestimos.clear();

        // Sample data
        Cliente cliente1 = new Cliente("João Silva", "123.456.789-00", "(47) 99999-9999", "joao@email.com");
        Cliente cliente2 = new Cliente("Maria Santos", "987.654.321-00", "(47) 98888-8888", "maria@email.com");
        Cliente cliente3 = new Cliente("Pedro Oliveira", "456.789.123-00", "(47) 97777-7777", "pedro@email.com");

        Emprestimo emp1 = new Emprestimo(5000.0, 12, 0.0199, cliente1);
        emp1.setId(1);
        emp1.setDataVencimento(LocalDate.now().plusDays(15));

        Emprestimo emp2 = new Emprestimo(10000.0, 24, 0.0189, cliente2);
        emp2.setId(2);
        emp2.setDataVencimento(LocalDate.now().plusDays(5));

        Emprestimo emp3 = new Emprestimo(15000.0, 36, 0.0179, cliente3);
        emp3.setId(3);
        emp3.setDataVencimento(LocalDate.now().minusDays(10));

        emprestimos.addAll(emp1, emp2, emp3);
        tabelaEmprestimos.setItems(emprestimos);
    }

    @FXML
    private void preencherValorTotal() {
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (emprestimoSelecionado != null) {
            double total = emprestimoSelecionado.getTotalAPagar();
            valorPagamentoField.setText(currencyFormatter.format(total).replace("R$", "").trim());
        } else {
            mostrarErro("Selecione um empréstimo primeiro");
        }
    }

    @FXML
    private void confirmarPagamento() {
        try {
            Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
            if (emprestimoSelecionado == null) {
                mostrarErro("Selecione um empréstimo primeiro");
                return;
            }

            String valorText = valorPagamentoField.getText().replace(".", "").replace(",", ".");
            double valorPagamento = Double.parseDouble(valorText);

            RadioButton selectedRadioButton = (RadioButton) formaPagamentoGroup.getSelectedToggle();
            String formaPagamento = selectedRadioButton.getText();

            // Criar e registrar o pagamento
            Pagamento pagamento = new Pagamento(emprestimoSelecionado, valorPagamento, formaPagamento);

            // Atualizar histórico
            historicoList.getItems().add(0, String.format("✅ Pagamento de %s realizado via %s",
                    currencyFormatter.format(valorPagamento), formaPagamento));

            // Atualizar empréstimo
            tabelaEmprestimos.refresh();

            mostrarSucesso("Pagamento realizado com sucesso!");
            limparCampos();
        } catch (NumberFormatException e) {
            mostrarErro("Valor de pagamento inválido");
        }
    }

    @FXML
    private void aplicarMulta() {
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (emprestimoSelecionado == null) {
            mostrarErro("Selecione um empréstimo primeiro");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Aplicar Multa");
        dialog.setHeaderText("Digite o valor da multa a ser aplicada");
        dialog.setContentText("Valor (R$):");

        dialog.showAndWait().ifPresent(valor -> {
            try {
                double valorMulta = Double.parseDouble(valor.replace(",", "."));
                double totalAtual = Double.parseDouble(totalLabel.getText()
                        .replace("R$", "").replace(".", "").replace(",", ".").trim());

                double novoTotal = totalAtual + valorMulta;

                multaLabel.setText(currencyFormatter.format(valorMulta));
                totalLabel.setText(currencyFormatter.format(novoTotal));

                // Atualizar campo de valor
                valorPagamentoField.setText(currencyFormatter.format(novoTotal)
                        .replace("R$", "").trim());

                mostrarSucesso("Multa aplicada com sucesso!");

            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido");
            }
        });
    }

    @FXML
    private void renegociar() {
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (emprestimoSelecionado == null) {
            mostrarErro("Selecione um empréstimo primeiro");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Renegociação");
        alert.setHeaderText("Funcionalidade em desenvolvimento");
        alert.setContentText("A renegociação será implementada em uma futura atualização.");
        alert.showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void limparCampos() {
        valorPagamentoField.clear();
        tabelaEmprestimos.getSelectionModel().clearSelection();
    }
}