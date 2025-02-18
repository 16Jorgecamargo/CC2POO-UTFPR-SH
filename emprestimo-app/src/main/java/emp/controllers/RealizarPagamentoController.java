package emp.controllers;

import emp.dao.EmprestimoDAO;
import emp.dao.PagamentoDAO;
import emp.models.Emprestimo;
import emp.models.Pagamento;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controlador responsável por gerenciar a realização de pagamentos dos empréstimos.
 * 
 * Exibe uma tabela com empréstimos ativos e permite ao usuário selecionar um empréstimo,
 * configurar o número de parcelas a serem pagas e aplicar multas. O controlador
 * atualiza os valores, calcula o total a pagar e registra o pagamento no banco de dados.
 * 
 * Interage com os seguintes elementos FXML:
 * - TableView para exibir empréstimos e suas colunas (cliente, id, valores, vencimento, etc.)
 * - ListView para exibir o histórico de pagamento.
 * - Labels para exibição dos valores (valor inicial, juros, multa, total).
 * - Spinner para seleção do número de parcelas.
 * - ToggleGroup para seleção da forma de pagamento.
 */
public class RealizarPagamentoController {
    /** Tabela que exibe os empréstimos */
    @FXML private TableView<Emprestimo> tabelaEmprestimos;
    /** Coluna que exibe o nome do cliente do empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaCliente;
    /** Coluna que exibe o ID do empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaId;
    /** Coluna que exibe o valor original do empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaValorOriginal;
    /** Coluna que exibe o total de juros do empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaJuros;
    /** Coluna que exibe o valor de multas aplicadas no empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaMultas;
    /** Coluna que exibe o valor total a pagar (valor + juros + multa) */
    @FXML private TableColumn<Emprestimo, String> colunaTotal;
    /** Coluna que exibe a data de vencimento do empréstimo */
    @FXML private TableColumn<Emprestimo, String> colunaVencimento;
    /** Coluna que exibe a quantidade de parcelas pagas versus total parcelas */
    @FXML private TableColumn<Emprestimo, String> colunaParcelas;
    /** Coluna que exibe o valor restante com base no número de parcelas */
    @FXML private TableColumn<Emprestimo, String> colunaRestante;
    /** Coluna que exibe o valor de cada parcela */
    @FXML private TableColumn<Emprestimo, String> colunaValorParcela;
    
    /** ListView para exibir o histórico de pagamentos */
    @FXML private ListView<String> historicoList;
    
    /** Label que exibe o valor inicial do empréstimo */
    @FXML private Label valorInicialLabel;
    /** Label que exibe o total de juros */
    @FXML private Label jurosLabel;
    /** Label que exibe o valor da multa aplicada */
    @FXML private Label multaLabel;
    /** Label que exibe o total a pagar */
    @FXML private Label totalLabel;
    
    /** Spinner para selecionar o número de parcelas a pagar */
    @FXML private Spinner<Integer> parcelasSpinner;
    /** Label que exibe o valor calculado a ser pago com base no número de parcelas */
    @FXML private Label valorCalculadoLabel;
    /** Grupo de botões para seleção da forma de pagamento */
    @FXML private ToggleGroup formaPagamentoGroup;

    /** Armazena o valor atual da multa aplicada (inicialmente do atraso e acumulada via aplicarMulta) */
    private double multaAplicada = 0;

    /** Lista observável para os empréstimos exibidos na tabela */
    private ObservableList<Emprestimo> emprestimos = FXCollections.observableArrayList();
    /** Formato para exibir datas */
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    /** Formato para exibição de valores monetários */
    private NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    /**
     * Inicializa o controlador.
     * Configura a tabela de empréstimos, a listagem do histórico e carrega os dados iniciais.
     */
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

    /**
     * Configura as colunas da tabela de empréstimos, definindo os cellValueFactory para
     * converter os dados em strings formatadas, utilizando formatação monetária e de datas.
     */
    private void configurarTabela() {
        colunaCliente.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getCliente().getNome()));
        colunaId.setCellValueFactory(data ->
            new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        colunaValorOriginal.setCellValueFactory(data ->
            new SimpleStringProperty(formatMoney(data.getValue().getValor())));
        colunaJuros.setCellValueFactory(data ->
            new SimpleStringProperty(formatMoney(data.getValue().getTotalJuros())));
        colunaMultas.setCellValueFactory(data ->
            new SimpleStringProperty(formatMoney(data.getValue().calcularJurosAtraso())));
        colunaTotal.setCellValueFactory(data -> {
            double valorOriginal = data.getValue().getValor();
            double juros = data.getValue().getTotalJuros();
            return new SimpleStringProperty(formatMoney(valorOriginal + juros + multaAplicada));
        });
        colunaVencimento.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getDataVencimento().format(dateFormatter)));
        colunaParcelas.setCellValueFactory(data ->
            new SimpleStringProperty(data.getValue().getParcelasPagas() + "/" + data.getValue().getPrazo()));
        colunaRestante.setCellValueFactory(data ->
            new SimpleStringProperty(formatMoney((data.getValue().getPrazo() - data.getValue().getParcelasPagas()) * data.getValue().getValorParcela())));
        colunaValorParcela.setCellValueFactory(data ->
            new SimpleStringProperty(formatMoney(data.getValue().getValorParcela())));

        tabelaEmprestimos.setItems(emprestimos);
        tabelaEmprestimos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldVal, newVal) -> {
                if (newVal != null) {
                    atualizarDetalhes(newVal);
                }
            });
    }

    /**
     * Formata um valor double para o padrão monetário brasileiro.
     *
     * @param value Valor a ser formatado.
     * @return String representando o valor formatado.
     */
    private String formatMoney(double value) {
        return currencyFormatter.format(value);
    }

    /**
     * Atualiza os detalhes dos valores exibidos ao selecionar um empréstimo.
     * Configura os labels com valores iniciais (valor, juros, multa) e configura
     * o Spinner para seleção de parcelas, atualizando o valor calculado.
     *
     * @param emprestimo Empréstimo selecionado.
     */
    private void atualizarDetalhes(Emprestimo emprestimo) {
        valorInicialLabel.setText(formatMoney(emprestimo.getValor()));
        jurosLabel.setText(formatMoney(emprestimo.getTotalJuros()));

        // Define a multa aplicada com o valor calculado de atraso inicialmente.
        multaAplicada = emprestimo.calcularJurosAtraso();
        multaLabel.setText(formatMoney(multaAplicada));

        double total = emprestimo.getTotalAPagar() + multaAplicada;
        totalLabel.setText(formatMoney(total));

        int parcelasRestantes = emprestimo.getPrazo() - emprestimo.getParcelasPagas();
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, parcelasRestantes, 1);
        parcelasSpinner.setValueFactory(valueFactory);

        valorCalculadoLabel.setText(formatMoney(emprestimo.getValorParcela() + multaAplicada));

        parcelasSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            double valorTotalSelecionado = newVal * emprestimo.getValorParcela() + multaAplicada;
            valorCalculadoLabel.setText(formatMoney(valorTotalSelecionado));
        });
    }

    /**
     * Configura o ListView de histórico, inicializando-o com uma lista vazia.
     */
    private void configurarHistorico() {
        historicoList.setItems(FXCollections.observableArrayList());
    }

    /**
     * Carrega os empréstimos ativos do banco de dados usando EmprestimoDAO.
     * Apenas os empréstimos cujo status não seja "Inativo" são adicionados.
     */
    @FXML
    public void carregarEmprestimos() {
        emprestimos.clear();
        EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
        try {
            List<Emprestimo> todosEmprestimos = emprestimoDAO.buscarTodos();
            List<Emprestimo> emprestimosAtivosList = todosEmprestimos.stream()
                .filter(emp -> !"Inativo".equalsIgnoreCase(emp.getStatus()))
                .collect(Collectors.toList());
            emprestimos.addAll(emprestimosAtivosList);
            tabelaEmprestimos.setItems(emprestimos);
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar empréstimos: " + e.getMessage());
        }
    }

    /**
     * Seleciona todas as parcelas restantes do empréstimo e atualiza o valor calculado.
     * Caso nenhum empréstimo esteja selecionado, exibe mensagem de alerta.
     */
    @FXML
    private void pagarTotal() {
        Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
        if (emprestimoSelecionado != null) {
            int parcelasRestantes = emprestimoSelecionado.getPrazo() - emprestimoSelecionado.getParcelasPagas();
            parcelasSpinner.getValueFactory().setValue(parcelasRestantes);
            double valorTotal = parcelasRestantes * emprestimoSelecionado.getValorParcela() + multaAplicada;
            valorCalculadoLabel.setText(formatMoney(valorTotal));
        } else {
            mostrarErro("Selecione um empréstimo primeiro");
        }
    }

    /**
     * Confirma o pagamento do empréstimo.
     * Calcula o valor a ser pago considerando o número de parcelas selecionadas e multa aplicada,
     * registra o pagamento via PagamentoDAO, atualiza o status do empréstimo, insere registro no histórico
     * e limpa os campos de entrada.
     */
    @FXML
    private void confirmarPagamento() {
        try {
            Emprestimo emprestimoSelecionado = tabelaEmprestimos.getSelectionModel().getSelectedItem();
            if (emprestimoSelecionado == null) {
                mostrarErro("Selecione um empréstimo primeiro");
                return;
            }
            int parcelasSelecionadas = parcelasSpinner.getValue();
            double valorPagamento = parcelasSelecionadas * emprestimoSelecionado.getValorParcela() + multaAplicada;
            
            RadioButton selectedRadioButton = (RadioButton) formaPagamentoGroup.getSelectedToggle();
            String formaPagamento = selectedRadioButton.getText();

            Pagamento pagamento = new Pagamento(emprestimoSelecionado, valorPagamento, formaPagamento);
            PagamentoDAO pagamentoDAO = new PagamentoDAO();
            try {
                pagamentoDAO.inserir(pagamento);
            } catch (SQLException e) {
                mostrarErro("Erro ao registrar o pagamento: " + e.getMessage());
                return;
            }

            int novasParcelasPagas = emprestimoSelecionado.getParcelasPagas() + parcelasSelecionadas;
            emprestimoSelecionado.setParcelasPagas(novasParcelasPagas);
            if (novasParcelasPagas >= emprestimoSelecionado.getPrazo()) {
                emprestimoSelecionado.setStatus("Inativo");
            }

            try {
                EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
                emprestimoDAO.atualizarEmprestimo(emprestimoSelecionado);
            } catch (SQLException e) {
                mostrarErro("Erro ao atualizar empréstimo: " + e.getMessage());
                System.err.println("Erro ao atualizar empréstimo: " + e.getMessage());
                return;
            }

            carregarEmprestimos();
            tabelaEmprestimos.refresh();
            historicoList.getItems().add(0, String.format("✅ Pagamento de %s realizado via %s",
                    currencyFormatter.format(valorPagamento), formaPagamento));

            mostrarSucesso("Pagamento realizado com sucesso!");
            limparCampos();
        } catch (NumberFormatException e) {
            mostrarErro("Valor de pagamento inválido");
        }
    }

    /**
     * Abre um diálogo para o usuário aplicar uma multa manual.
     * Soma o valor informado à multa aplicada, atualiza os labels correspondentes e o valor final a pagar.
     */
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
        dialog.showAndWait().ifPresent(input -> {
            try {
                String valorLimpo = input.replaceAll("[^\\d,\\.]", "");
                valorLimpo = valorLimpo.replace(".", "").replace(",", ".");
                double novaMulta = Double.parseDouble(valorLimpo);
                multaAplicada += novaMulta;
                multaLabel.setText(currencyFormatter.format(multaAplicada));
                double novoTotal = emprestimoSelecionado.getTotalAPagar() + multaAplicada;
                totalLabel.setText(currencyFormatter.format(novoTotal));
                int parcelasSelecionadas = parcelasSpinner.getValue();
                double valorTotalSelecionado = parcelasSelecionadas * emprestimoSelecionado.getValorParcela() + multaAplicada;
                valorCalculadoLabel.setText(currencyFormatter.format(valorTotalSelecionado));
                mostrarSucesso("Multa aplicada com sucesso!");
            } catch (NumberFormatException e) {
                mostrarErro("Valor inválido");
                System.err.println("Erro ao aplicar multa: " + e.getMessage());
            }
        });
    }

    /**
     * Exibe um alerta informativo de que a renegociação não está implementada.
     */
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

    /**
     * Exibe um alerta do tipo sucesso.
     *
     * @param mensagem Mensagem de sucesso a ser exibida.
     */
    private void mostrarSucesso(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de erro com a mensagem especificada.
     *
     * @param mensagem Mensagem de erro.
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Limpa os campos de entrada e reinicia os controles de pagamento.
     * Reinicia o Spinner de parcelas, o label do valor calculado e deseleciona o empréstimo.
     * Também reseta a multa aplicada.
     */
    private void limparCampos() {
        parcelasSpinner.getValueFactory().setValue(1);
        valorCalculadoLabel.setText(formatMoney(0));
        tabelaEmprestimos.getSelectionModel().clearSelection();
        multaAplicada = 0;
    }
}