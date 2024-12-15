package emp.controllers;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardController {
    @FXML
    private Button botaoGerenciarClientes;
    @FXML
    private FontIcon iconeGerenciar;
    private boolean telaGerenciamentoAtiva = false;
    @FXML
    private ToggleGroup periodoGroup;
    @FXML
    private Label totalEmprestimosLabel;
    @FXML
    private Node conteudoPrincipal;
    @FXML
    private VBox dashboardContent;
    @FXML
    private Label totalEmprestadoLabel;
    @FXML
    private Label saldoDevedorLabel;
    @FXML
    private Label emprestimosAtivosLabel;
    @FXML
    private Label pagamentosMesLabel;
    @FXML
    private PieChart statusEmprestimoChart;
    @FXML
    private LineChart<String, Number> evolucaoPagamentosChart;
    @FXML
    private ListView<String> listaAlertas;
    @FXML
    private VBox conteudoDashboard;
    @FXML
    private VBox conteudoGerenciamento;

    @FXML
    private void initialize() {
        configurarPeriodoSelector();
        configurarAlertas();
        carregarDadosMes();

        // Garante que o conteúdo principal comece invisível
        if (conteudoPrincipal != null) {
            conteudoPrincipal.setVisible(false);
        }
    }

    private void configurarPeriodoSelector() {
        periodoGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton selected = (RadioButton) newVal;
                switch (selected.getText()) {
                    case "Hoje":
                        carregarDadosHoje();
                        break;
                    case "Semana":
                        carregarDadosSemana();
                        break;
                    case "Mês":
                        carregarDadosMes();
                        break;
                    case "Ano":
                        carregarDadosAno();
                        break;
                    case "Tudo":
                        carregarDadosTudo();
                        break;
                }
            }
        });
    }

    private void carregarDadosHoje() {
        totalEmprestimosLabel.setText("3");
        totalEmprestadoLabel.setText("R$ 10.000,00");
        saldoDevedorLabel.setText("R$ 5.000,00");
        emprestimosAtivosLabel.setText("5");
        pagamentosMesLabel.setText("R$ 1.000,00");
        atualizarGraficosPeriodo("Hoje");
    }

    private void carregarDadosSemana() {
        totalEmprestimosLabel.setText("8");
        totalEmprestadoLabel.setText("R$ 50.000,00");
        saldoDevedorLabel.setText("R$ 25.000,00");
        emprestimosAtivosLabel.setText("10");
        pagamentosMesLabel.setText("R$ 5.000,00");
        atualizarGraficosPeriodo("Semana");
    }

    private void carregarDadosMes() {
        totalEmprestimosLabel.setText("15");
        totalEmprestadoLabel.setText("R$ 100.000,00");
        saldoDevedorLabel.setText("R$ 50.000,00");
        emprestimosAtivosLabel.setText("15");
        pagamentosMesLabel.setText("R$ 10.000,00");
        atualizarGraficosPeriodo("Mês");
    }

    private void carregarDadosAno() {
        totalEmprestimosLabel.setText("50");
        totalEmprestadoLabel.setText("R$ 1.000.000,00");
        saldoDevedorLabel.setText("R$ 500.000,00");
        emprestimosAtivosLabel.setText("50");
        pagamentosMesLabel.setText("R$ 100.000,00");
        atualizarGraficosPeriodo("Ano");
    }

    private void carregarDadosTudo() {
        totalEmprestimosLabel.setText("100");
        totalEmprestadoLabel.setText("R$ 5.000.000,00");
        saldoDevedorLabel.setText("R$ 2.500.000,00");
        emprestimosAtivosLabel.setText("100");
        pagamentosMesLabel.setText("R$ 500.000,00");
        atualizarGraficosPeriodo("Total");
    }

    private void atualizarGraficosPeriodo(String periodo) {
        // Valores baseados no período
        int empEmDia = periodo.equals("Hoje") ? 3 : 60;
        int empAtrasados = periodo.equals("Hoje") ? 1 : 20;
        int empQuitados = periodo.equals("Hoje") ? 1 : 20;
        int total = empEmDia + empAtrasados + empQuitados;

        // Atualiza gráfico de pizza
        statusEmprestimoChart.getData().clear();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data(String.format("Em dia (%d%%)\n%d empréstimos",
                        (empEmDia * 100) / total, empEmDia), empEmDia),
                new PieChart.Data(String.format("Atrasados (%d%%)\n%d empréstimos",
                        (empAtrasados * 100) / total, empAtrasados), empAtrasados),
                new PieChart.Data(String.format("Quitados (%d%%)\n%d empréstimos",
                        (empQuitados * 100) / total, empQuitados), empQuitados));
        statusEmprestimoChart.setData(pieData);

        pieData.forEach(data -> {
            final Node node = data.getNode();
            node.setStyle("-fx-pie-label-visible: true; -fx-pie-label-fill: white;");
        });

        // Atualiza gráfico de linha
        evolucaoPagamentosChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pagamentos - " + periodo);

        if (periodo.equals("Hoje")) {
            for (int i = 0; i < 24; i++) {
                series.getData().add(new XYChart.Data<>(i + "h", Math.random() * 1000));
            }
        } else {
            String[] labels = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun" };
            double baseValue = periodo.equals("Ano") ? 100000 : 10000;
            for (String label : labels) {
                series.getData().add(new XYChart.Data<>(label, Math.random() * baseValue));
            }
        }

        evolucaoPagamentosChart.getData().add(series);
    }

    private void configurarAlertas() {
        listaAlertas.getItems().addAll(
                "⚠️ 3 contratos com pagamento atrasado",
                "🔔 5 pagamentos vencem esta semana",
                "❗ Cliente João da Silva - pagamento atrasado há 5 dias",
                "⚠️ 2 clientes com mais de 3 parcelas atrasadas",
                "🔔 Novo cliente aguardando aprovação de empréstimo",
                "❗ Limite de crédito atingido para cliente Maria Santos",
                "⚠️ Relatório mensal pendente de geração",
                "🔔 3 contratos próximos do vencimento",
                "❗ Sistema detectou possível fraude em 2 cadastros",
                "⚠️ Backup automático não realizado há 2 dias");
    }

    @FXML
    private BorderPane root;

    private boolean isDarkMode = false;

    @FXML
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            root.getStyleClass().add("dark-theme");
        } else {
            root.getStyleClass().remove("dark-theme");
        }
    }

    @FXML
    private void alternarTelaGerenciamento() {
        if (conteudoDashboard != null && conteudoGerenciamento != null) {
            telaGerenciamentoAtiva = !telaGerenciamentoAtiva;

            if (telaGerenciamentoAtiva) {
                // Mudando para tela de gerenciamento
                conteudoDashboard.setVisible(false);
                conteudoGerenciamento.setVisible(true);
                botaoGerenciarClientes.setText("Voltar");
                iconeGerenciar.setIconLiteral("fas-arrow-left");
            } else {
                // Voltando para dashboard
                conteudoDashboard.setVisible(true);
                conteudoGerenciamento.setVisible(false);
                botaoGerenciarClientes.setText("Gerenciar Clientes");
                iconeGerenciar.setIconLiteral("fas-users");
            }
        }
    }

    @FXML
    private void voltarDashboard() {
        if (dashboardContent != null && conteudoPrincipal != null) {
            dashboardContent.setVisible(true);
            conteudoPrincipal.setVisible(false);
        }
    }

    @FXML
    private void sair() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}