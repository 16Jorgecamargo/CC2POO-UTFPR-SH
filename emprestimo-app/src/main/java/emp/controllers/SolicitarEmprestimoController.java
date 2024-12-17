package emp.controllers;

import emp.models.Cliente;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.chart.XYChart;
import javafx.collections.FXCollections;
import javafx.scene.chart.NumberAxis;
import javafx.collections.ObservableList;

import java.text.NumberFormat;
import java.util.Locale;

public class SolicitarEmprestimoController {
    @FXML
    private TextField campoValor;
    @FXML
    private Slider sliderPrazo;
    @FXML
    private Label labelPrazo;
    @FXML
    private TextField campoTaxa;
    @FXML
    private Label valorParcelaLabel;
    @FXML
    private Label totalPagarLabel;
    @FXML
    private Label totalJurosLabel;
    @FXML
    private BarChart<String, Number> graficoComparativo;
    @FXML
    private ComboBox<Cliente> comboCliente;
    @FXML
    private Label statusCliente;
    @FXML
    private ToggleGroup tipoJurosGroup;
    @FXML
    private RadioButton jurosSimples;
    @FXML
    private RadioButton jurosComposto;
    @FXML
    private Spinner<Integer> spinnerPrazo;
    @FXML
    private NumberAxis yAxis;

    private final double TAXA_JUROS = 0.0199; // 1.99% ao mês
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Configurar combo de clientes
        comboCliente.setItems(clientes);
        comboCliente.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                atualizarStatusCliente(newVal);
            }
        });

        // Configurar spinner de prazo
        SpinnerValueFactory.IntegerSpinnerValueFactory prazoFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                1, 48, 12);
        spinnerPrazo.setValueFactory(prazoFactory);

        // Configurar campo de taxa com valor inicial
        campoTaxa.setText("1,99");

        // Setar juros composto como padrão
        jurosComposto.setSelected(true);

        // Carregar clientes de exemplo
        carregarClientes();

        // Configurar listeners para tipo de juros
        if (tipoJurosGroup != null) {
            tipoJurosGroup.selectedToggleProperty().addListener((obs, old, newVal) -> {
                if (campoValor.getText() != null && !campoValor.getText().isEmpty()) {
                    simular();
                }
            });
        }

        // Configurar campo valor para aceitar apenas números e vírgula
        campoValor.textProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal.matches("\\d*[,.]?\\d*")) {
                campoValor.setText(oldVal);
            }
        });

        // Configurar labels iniciais
        valorParcelaLabel.setText("R$ 0,00");
        totalPagarLabel.setText("R$ 0,00");
        totalJurosLabel.setText("R$ 0,00");
    }

    private void carregarClientes() {
        // Aqui você carregaria os clientes do seu banco de dados
        clientes.addAll(
                new Cliente("João Silva", "123.456.789-00", "(47) 99999-9999", "joao@email.com"),
                new Cliente("Maria Santos", "987.654.321-00", "(47) 98888-8888", "maria@email.com"));
    }

    private void atualizarStatusCliente(Cliente cliente) {
        if (cliente.isAtivo()) {
            statusCliente.setText("(Ativo)");
            statusCliente.setStyle("-fx-text-fill: #28a745;");
        } else {
            statusCliente.setText("(Inativo)");
            statusCliente.setStyle("-fx-text-fill: #dc3545;");
        }
    }

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
                // Juros Simples: P = (V * (1 + i * n)) / n
                totalPagar = valor * (1 + taxa * prazo);
                parcela = totalPagar / prazo;
            } else {
                // Juros Compostos: P = V * (i * (1 + i)^n) / ((1 + i)^n - 1)
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

    private double calcularParcela(double valor, int prazo, double taxa) {
        return valor * (taxa * Math.pow(1 + taxa, prazo)) / (Math.pow(1 + taxa, prazo) - 1);
    }

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
            
            Tooltip parcelaTooltip = new Tooltip(currencyFormat.format(parcela));
            Tooltip.install(parcelaData.getNode(), parcelaTooltip);
            
            Tooltip totalTooltip = new Tooltip(currencyFormat.format(total));
            Tooltip.install(totalData.getNode(), totalTooltip);
            
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

    @FXML
    private void visualizarContrato() {
        // Implementar visualização do contrato
        mostrarInfo("Visualização do contrato em desenvolvimento");
    }

    @FXML
    private void confirmarSolicitacao() {
        Cliente cliente = comboCliente.getValue();
        if (cliente != null && !cliente.isAtivo()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Ativar Cliente");
            alert.setHeaderText("Cliente Inativo");
            alert.setContentText("O cliente está inativo. Deseja ativá-lo para prosseguir com a solicitação?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                cliente.setAtivo(true);
                atualizarStatusCliente(cliente);
                // Aqui você atualizaria os gráficos do dashboard
            }
        }
        // Continuar com a solicitação...
        mostrarInfo("Solicitação confirmada com sucesso!");
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}