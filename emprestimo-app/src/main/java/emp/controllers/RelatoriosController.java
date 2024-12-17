package emp.controllers;

import emp.models.Cliente;
import emp.models.Emprestimo;
import emp.models.Relatorio;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

public class RelatoriosController {
    @FXML
    private ComboBox<String> tipoRelatorio;
    @FXML
    private ComboBox<String> selectPeriodo;
    @FXML
    private ComboBox<Cliente> selectCliente;
    @FXML
    private ComboBox<String> selectStatus;
    @FXML
    private TextField valorMinimo;
    @FXML
    private TextField valorMaximo;
    @FXML
    private HBox datePickers;
    @FXML
    private DatePicker dataInicio;
    @FXML
    private DatePicker dataFim;
    @FXML
    private PieChart graficoStatus;
    @FXML
    private BarChart<String, Number> graficoPagamentos;
    @FXML
    private TableView<Emprestimo> tabelaDados;
    @FXML
    private TextArea campoAnotacoes;

    private final NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private ObservableList<Emprestimo> emprestimos = FXCollections.observableArrayList();
    private Relatorio relatorioAtual;

    @FXML
    private void initialize() {
        tipoRelatorio.getItems().addAll(
                "Por Cliente",
                "Por Empréstimo",
                "Financeiros Gerais");

        selectPeriodo.getItems().addAll(
                "Mês Atual",
                "Trimestre",
                "Semestre",
                "Ano",
                "Período Personalizado");

        selectPeriodo.valueProperty().addListener((obs, old, newVal) -> {
            datePickers.setVisible("Período Personalizado".equals(newVal));
        });

        selectStatus.getItems().addAll("Todos", "Ativos", "Atrasados", "Quitados");
        selectStatus.setValue("Todos");
    }

    private void configurarComboBoxes() {
        selectPeriodo.getSelectionModel().selectedItemProperty().addListener((obs, old, newVal) -> {
            datePickers.setVisible(newVal.equals("Período Personalizado"));
        });

        selectStatus.getItems().addAll("Todos", "Ativos", "Atrasados", "Quitados");
        selectStatus.setValue("Todos");
    }

    private void configurarListeners() {
        tipoRelatorio.getItems().addAll("Por Cliente", "Por Empréstimo", "Financeiro");
        tipoRelatorio.setValue("Financeiro");

        selectPeriodo.getItems().addAll("Mês Atual", "Trimestre", "Semestre", "Ano", "Período Personalizado");
        selectPeriodo.setValue("Mês Atual");
    }

    private void configurarTabela() {
        TableColumn<Emprestimo, String> colunaId = new TableColumn<>("ID");
        colunaId.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));

        TableColumn<Emprestimo, String> colunaCliente = new TableColumn<>("Cliente");
        colunaCliente.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCliente().getNome()));

        TableColumn<Emprestimo, String> colunaValor = new TableColumn<>("Valor Original");
        colunaValor.setCellValueFactory(
                data -> new SimpleStringProperty(currencyFormatter.format(data.getValue().getValor())));

        TableColumn<Emprestimo, String> colunaSaldo = new TableColumn<>("Saldo Atual");
        colunaSaldo.setCellValueFactory(data -> new SimpleStringProperty(currencyFormatter.format(
                data.getValue().getTotalAPagar() -
                        (data.getValue().getValorParcela() * data.getValue().getParcelasPagas()))));

        tabelaDados.getColumns().setAll(colunaId, colunaCliente, colunaValor, colunaSaldo);
    }

    @FXML
    private void gerarRelatorio() {
        Relatorio.TipoRelatorio tipo;
        switch (tipoRelatorio.getValue()) {
            case "Por Cliente":
                tipo = Relatorio.TipoRelatorio.POR_CLIENTE;
                break;
            case "Por Empréstimo":
                tipo = Relatorio.TipoRelatorio.POR_EMPRESTIMO;
                break;
            default:
                tipo = Relatorio.TipoRelatorio.FINANCEIRO;
                break;
        }

        Relatorio.Periodo periodo;
        switch (selectPeriodo.getValue()) {
            case "Trimestre":
                periodo = Relatorio.Periodo.TRIMESTRE;
                break;
            case "Semestre":
                periodo = Relatorio.Periodo.SEMESTRE;
                break;
            case "Ano":
                periodo = Relatorio.Periodo.ANO;
                break;
            case "Período Personalizado":
                periodo = Relatorio.Periodo.PERSONALIZADO;
                break;
            default:
                periodo = Relatorio.Periodo.MES_ATUAL;
                break;
        }

        relatorioAtual = new Relatorio(tipo, periodo);
        if (periodo == Relatorio.Periodo.PERSONALIZADO) {
            relatorioAtual.setDataInicio(dataInicio.getValue());
            relatorioAtual.setDataFim(dataFim.getValue());
        }

        atualizarVisualizacoes();
    }

    private void atualizarVisualizacoes() {
        atualizarGraficoPizza();
        atualizarGraficoPagamentos();
        atualizarTabela();
    }

    private void atualizarGraficoPizza() {
        graficoStatus.getData().clear();
        var statusCount = relatorioAtual.contarPorStatus();

        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        statusCount.forEach((status, count) -> {
            pieData.add(new PieChart.Data(status + " (" + count + ")", count));
        });

        graficoStatus.setData(pieData);
    }

    private void atualizarGraficoPagamentos() {
        graficoPagamentos.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pagamentos");

        // Adicionar dados de exemplo
        series.getData().add(new XYChart.Data<>("Jan", 5000));
        series.getData().add(new XYChart.Data<>("Fev", 7000));
        series.getData().add(new XYChart.Data<>("Mar", 6000));

        graficoPagamentos.getData().add(series);
    }

    @FXML
    private void exportarPDF() {
        // Implementar exportação PDF
        mostrarInfo("Exportação para PDF em desenvolvimento");
    }

    @FXML
    private void exportarExcel() {
        // Implementar exportação Excel
        mostrarInfo("Exportação para Excel em desenvolvimento");
    }

    private void mostrarInfo(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void atualizarTabela() {
        if (relatorioAtual != null) {
            emprestimos = FXCollections.observableArrayList(relatorioAtual.getEmprestimos());
            tabelaDados.setItems(emprestimos);
        }
    }
}