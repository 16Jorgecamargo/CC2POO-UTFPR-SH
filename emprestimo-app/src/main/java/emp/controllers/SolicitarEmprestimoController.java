package emp.controllers;

import emp.models.Cliente;
import emp.models.Emprestimo;
import emp.services.ClienteService;
import emp.dao.EmprestimoDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;
import java.sql.SQLException;

/**
 * Controlador responsável pela solicitação de um empréstimo.
 * 
 * Gerencia a interface para simulação e confirmação do empréstimo solicitado, permitindo
 * ao usuário inserir valores, selecionar prazo, taxa de juros e tipo de juros (simples ou composto).
 * Calcula parcelas, total a pagar e atualiza um gráfico comparativo do valor da parcela versus total a pagar.
 * 
 * Interage com os seguintes elementos FXML:
 * - TextField: campoValor (valor do empréstimo)
 * - Slider e Spinner: controle de prazo (número de meses)
 * - TextField: campoTaxa (taxa de juros)
 * - Labels: valorParcelaLabel, totalPagarLabel, totalJurosLabel para exibir resultados
 * - BarChart: graficoComparativo para exibir a simulação de parcelas em diversos prazos
 * - ComboBox: comboCliente que apresenta a lista dos clientes (obtida via ClienteService)
 * - RadioButtons: jurosSimples e jurosComposto (definem o tipo de juros)
 */
public class SolicitarEmprestimoController {
    /** Campo de texto para inserir o valor do empréstimo */
    @FXML private TextField campoValor;
    /** Slider para selecionar o prazo (opcional) */
    @FXML private Slider sliderPrazo;
    /** Label que exibe o prazo selecionado */
    @FXML private Label labelPrazo;
    /** Campo de texto para inserir a taxa de juros */
    @FXML private TextField campoTaxa;
    /** Label que exibe o valor calculado da parcela */
    @FXML private Label valorParcelaLabel;
    /** Label que exibe o total a pagar */
    @FXML private Label totalPagarLabel;
    /** Label que exibe o total de juros */
    @FXML private Label totalJurosLabel;
    /** BarChart que apresenta um comparativo entre valores de parcelas e totais para diferentes prazos */
    @FXML private BarChart<String, Number> graficoComparativo;
    /** ComboBox contendo os clientes (proveniente do ClienteService) */
    @FXML private ComboBox<Cliente> comboCliente;
    /** Label que exibe o status do cliente (Ativo/Inativo) */
    @FXML private Label statusCliente;
    /** Grupo de botões que define o tipo de juros (simples ou composto) */
    @FXML private ToggleGroup tipoJurosGroup;
    /** RadioButton que seleciona juros simples */
    @FXML private RadioButton jurosSimples;
    /** RadioButton que seleciona juros composto */
    @FXML private RadioButton jurosComposto;
    /** Spinner para selecionar o prazo em meses */
    @FXML private Spinner<Integer> spinnerPrazo;
    /** Eixo Y do gráfico comparativo */
    @FXML private NumberAxis yAxis;
    
    /** Formata os valores monetários para o padrão brasileiro */
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    /** DAO para operações de empréstimo na tabela de empréstimos */
    private EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    /**
     * Método de inicialização.
     * Configura o ComboBox com a lista compartilhada de clientes, atualiza o status do cliente,
     * configura o Spinner para prazo, define valores iniciais e listeners para simulação do empréstimo.
     */
    @FXML
    private void initialize() {
        // Configura o ComboBox com a ObservableList de ClienteService
        comboCliente.setItems(ClienteService.getClientes());
        carregarClientes();
        
        // Atualiza o status do cliente quando a seleção muda
        comboCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarStatusCliente(newVal);
            }
        });
        
        // Configura o Spinner de prazo: de 1 a 48 meses, valor inicial 12
        SpinnerValueFactory.IntegerSpinnerValueFactory prazoFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 48, 12);
        spinnerPrazo.setValueFactory(prazoFactory);

        // Define valor inicial para a taxa de juros
        campoTaxa.setText("1,99");

        // Seleciona juros composto por padrão
        jurosComposto.setSelected(true);

        // Quando o tipo de juros mudar, refaz a simulação se valor informado
        if (tipoJurosGroup != null) {
            tipoJurosGroup.selectedToggleProperty().addListener((obs, old, newVal) -> {
                if (campoValor.getText() != null && !campoValor.getText().isEmpty()) {
                    simular();
                }
            });
        }

        // Restringe o campo valor para aceitar somente números e vírgula
        campoValor.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*[,.]?\\d*")) {
                campoValor.setText(oldVal);
            }
        });
        
        // Inicializa os labels dos resultados
        valorParcelaLabel.setText("R$ 0,00");
        totalPagarLabel.setText("R$ 0,00");
        totalJurosLabel.setText("R$ 0,00");
    }
    
    /**
     * Atualiza a lista de clientes chamando o ClienteService para atualizar os dados.
     */
    public void carregarClientes() {
        try {
            ClienteService.atualizarClientes();
            comboCliente.setItems(ClienteService.getClientes());
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Atualiza o status do cliente na interface, mudando o texto e cor do label.
     *
     * @param cliente Cliente selecionado.
     */
    private void atualizarStatusCliente(Cliente cliente) {
        if (cliente.isAtivo()) {
            statusCliente.setText("(Ativo)");
            statusCliente.setStyle("-fx-text-fill: #28a745;");
        } else {
            statusCliente.setText("(Inativo)");
            statusCliente.setStyle("-fx-text-fill: #dc3545;");
        }
    }
    
    /**
     * Realiza a simulação do empréstimo.
     * Obtém os valores do formulário, calcula a parcela e total a pagar com base no tipo de juros selecionado,
     * atualiza os labels com o resultado e atualiza o gráfico comparativo.
     */
    @FXML
    private void simular() {
        try {
            Cliente cliente = comboCliente.getValue();
            if (cliente == null) {
                mostrarErro("Por favor, selecione um cliente");
                return;
            }
            double valor = Double.parseDouble(campoValor.getText().replace(".", "").replace(",", "."));
            int prazo = spinnerPrazo.getValue();
            double taxa = Double.parseDouble(campoTaxa.getText().replace(",", ".")) / 100;
            double parcela;
            double totalPagar;
    
            if (jurosSimples.isSelected()) {
                totalPagar = valor * (1 + taxa * prazo);
                parcela = totalPagar / prazo;
            } else {
                parcela = calcularParcela(valor, prazo, taxa);
                totalPagar = parcela * prazo;
            }
    
            double totalJuros = totalPagar - valor;
    
            valorParcelaLabel.setText(currencyFormat.format(parcela));
            totalPagarLabel.setText(currencyFormat.format(totalPagar));
            totalJurosLabel.setText(currencyFormat.format(totalJuros));
    
            atualizarGrafico(valor, taxa);
    
        } catch (NumberFormatException e) {
            mostrarErro("Por favor, insira valores válidos");
        }
    }
    
    /**
     * Limpa os campos do formulário e reseta os valores dos resultados e gráfico.
     */
    @FXML
    private void limpar() {
        comboCliente.setValue(null);
        campoValor.setText("");
        spinnerPrazo.getValueFactory().setValue(12);
        campoTaxa.setText("1,99");
        jurosComposto.setSelected(true);
    
        valorParcelaLabel.setText("R$ 0,00");
        totalPagarLabel.setText("R$ 0,00");
        totalJurosLabel.setText("R$ 0,00");
    
        statusCliente.setText("");
    
        graficoComparativo.getData().clear();
    }
    
    /**
     * Calcula o valor da parcela utilizando a fórmula de juros compostos.
     *
     * @param valor Valor do empréstimo.
     * @param prazo Número de meses.
     * @param taxa Taxa de juros mensal (em decimal).
     * @return Valor da parcela.
     */
    private double calcularParcela(double valor, int prazo, double taxa) {
        return valor * (taxa * Math.pow(1 + taxa, prazo)) / (Math.pow(1 + taxa, prazo) - 1);
    }
    
    /**
     * Atualiza o gráfico comparativo com as simulações para diferentes prazos.
     *
     * @param valor Valor do empréstimo.
     * @param taxa Taxa de juros mensal (em decimal).
     */
    @SuppressWarnings("unchecked")
    private void atualizarGrafico(double valor, double taxa) {
        graficoComparativo.getData().clear();
    
        XYChart.Series<String, Number> seriesParcelas = new XYChart.Series<>();
        seriesParcelas.setName("Valor da Parcela");
    
        XYChart.Series<String, Number> seriesTotais = new XYChart.Series<>();
        seriesTotais.setName("Total a Pagar");
    
        int[] prazos = {12, 24, 36, 48};
        for (int prazo : prazos) {
            double parcela;
            double total;
    
            if (jurosSimples.isSelected()) {
                total = valor * (1 + taxa * prazo);
                parcela = total / prazo;
            } else {
                parcela = calcularParcela(valor, prazo, taxa);
                total = parcela * prazo;
            }
    
            XYChart.Data<String, Number> parcelaData = new XYChart.Data<>(prazo + "x", parcela);
            XYChart.Data<String, Number> totalData = new XYChart.Data<>(prazo + "x", total);
    
            seriesParcelas.getData().add(parcelaData);
            seriesTotais.getData().add(totalData);
        }
    
        graficoComparativo.getData().addAll(seriesParcelas, seriesTotais);
    
        for (XYChart.Series<String, Number> series : graficoComparativo.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                Node node = data.getNode();
                if (node != null) {
                    Tooltip tooltip = new Tooltip(currencyFormat.format(data.getYValue()));
                    Tooltip.install(node, tooltip);
                }
            }
        }
    }
    
    /**
     * Abre uma janela informativa para visualizar o contrato.
     * Atualmente, a funcionalidade está em desenvolvimento.
     */
    @FXML
    private void visualizarContrato() {
        mostrarInfo("Visualização do contrato em desenvolvimento");
    }
    
    /**
     * Confirma a solicitação do empréstimo, criando um objeto Emprestimo, definindo a data de vencimento,
     * inserindo-o no banco de dados e informando o usuário sobre o sucesso da operação.
     */
    @FXML
    private void confirmarSolicitacao() {
        try {
            Cliente cliente = comboCliente.getValue();
            if (cliente == null) {
                mostrarErro("Selecione um cliente");
                return;
            }
    
            double valor = Double.parseDouble(campoValor.getText().replace(".", "").replace(",", "."));
            int prazo = spinnerPrazo.getValue();
            double taxa = Double.parseDouble(campoTaxa.getText().replace(",", ".")) / 100;
    
            Emprestimo emprestimo = new Emprestimo(valor, prazo, taxa, cliente);
    
            // Define a data de vencimento como a data atual acrescida do prazo em meses
            LocalDate dataVencimento = LocalDate.now().plusMonths(prazo);
            emprestimo.setDataVencimento(dataVencimento);
    
            emprestimoDAO.inserir(emprestimo);
    
            mostrarInfo("Empréstimo solicitado com sucesso!");
            limpar();
    
        } catch (SQLException e) {
            mostrarErro("Erro ao solicitar empréstimo: " + e.getMessage());
            System.err.println("Erro ao solicitar empréstimo: " + e.getMessage());
        } catch (NumberFormatException e) {
            mostrarErro("Valores inválidos");
        }
    }
    
    /**
     * Exibe uma mensagem de erro em um diálogo de alerta.
     *
     * @param mensagem Mensagem de erro a ser exibida.
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
    
    /**
     * Exibe uma mensagem informativa em um diálogo de alerta.
     *
     * @param mensagem Mensagem a ser exibida.
     */
    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}