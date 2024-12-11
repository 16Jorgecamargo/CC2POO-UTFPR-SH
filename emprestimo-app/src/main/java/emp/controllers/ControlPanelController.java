package emp.controllers;

import emp.App;
//import emp.models.Loan;
//import emp.models.Payment;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Arrays;

public class ControlPanelController {
    @FXML private Label activeClientsLabel;
    @FXML private Label activeLoansLabel;
    @FXML private Label latePaymentsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label receivableAmountLabel;
    
    @FXML private PieChart defaultRateChart;
    @FXML private BarChart<String, Number> loanAmountChart;
    @FXML private LineChart<String, Number> accumulatedInterestChart;
    @FXML private StackedBarChart<String, Number> loanDistributionChart;
    
    @FXML private ListView<String> alertsList;

    @FXML
    public void initialize() {
        setupDefaultRateChart();
        setupLoanAmountChart();
        setupAccumulatedInterestChart();
        setupLoanDistributionChart();
        setupAlertsList();
        updateKPIs();
    }

    private void setupDefaultRateChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("30 dias", 35),
            new PieChart.Data("60 dias", 25),
            new PieChart.Data("90+ dias", 40)
        );
        defaultRateChart.setData(pieChartData);
    }

    private void setupLoanAmountChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Valor Total");
        series.getData().add(new XYChart.Data<>("Jan", 50000));
        series.getData().add(new XYChart.Data<>("Fev", 65000));
        series.getData().add(new XYChart.Data<>("Mar", 45000));
        loanAmountChart.getData().add(series);
    }

    private void setupAccumulatedInterestChart() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Juros");
        series.getData().add(new XYChart.Data<>("Jan", 5000));
        series.getData().add(new XYChart.Data<>("Fev", 8000));
        series.getData().add(new XYChart.Data<>("Mar", 12000));
        accumulatedInterestChart.getData().add(series);
    }

    private void setupLoanDistributionChart() {
        XYChart.Series<String, Number> simples = new XYChart.Series<>();
        simples.setName("Simples");
        simples.getData().add(new XYChart.Data<>("2024", 30000));

        XYChart.Series<String, Number> composto = new XYChart.Series<>();
        composto.setName("Composto");
        composto.getData().add(new XYChart.Data<>("2024", 70000));

        ObservableList<XYChart.Series<String, Number>> data = FXCollections.observableArrayList();
        data.add(simples);
        data.add(composto);
        loanDistributionChart.setData(data);
    }

    private void setupAlertsList() {
        ObservableList<String> alerts = FXCollections.observableArrayList(
            "⚠️ 5 clientes com mais de 90 dias de atraso",
            "📅 10 empréstimos vencem esta semana",
            "👤 3 novos clientes registrados nos últimos 7 dias"
        );
        alertsList.setItems(alerts);
    }

    private void updateKPIs() {
        activeClientsLabel.setText("45");
        activeLoansLabel.setText("60");
        latePaymentsLabel.setText("15");
        totalRevenueLabel.setText("R$ 25.000,00");
        receivableAmountLabel.setText("R$ 150.000,00");
    }

    @FXML
    private void handleNotifyDefaulters() {
        AlertHelper.showSuccessMessage("Notificações enviadas para clientes inadimplentes!");
    }

    @FXML
    private void handleRefreshData() {
        updateKPIs();
        AlertHelper.showSuccessMessage("Dados atualizados com sucesso!");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }
}