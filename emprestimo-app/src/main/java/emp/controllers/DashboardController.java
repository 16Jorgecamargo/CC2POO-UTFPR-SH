package emp.controllers;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.App;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DashboardController {
    @FXML
    private Button botaoGerenciarClientes;
    @FXML
    private FontIcon iconeGerenciar;
    @FXML
    private VBox conteudoPagamento;
    @FXML
    private Button botaoRealizarPagamento;
    @FXML
    private VBox conteudoRelatorios;
    @FXML
    private Button botaoRelatorios;
    private boolean telaRelatoriosAtiva = false;
    private boolean telaGerenciamentoAtiva = false;
    private boolean telaPagamentoAtiva = false;
    private boolean telaSolicitacaoAtiva = false;

    @FXML
    private VBox conteudoEmprestimo;
    @FXML
    private Button botaoSolicitarEmprestimo;

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

        //conteúdo principal comece invisível
        if (conteudoEmprestimo != null) {
            conteudoEmprestimo.setVisible(false);
        }
        if (conteudoRelatorios != null) {
            conteudoRelatorios.setVisible(false);
        }
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
        listaAlertas.setCellFactory(lv -> new ListCell<String>() {
            private final Button btnRemover = new Button();
            private final HBox hbox = new HBox();
            private final Label label = new Label();

            {
                FontIcon removeIcon = new FontIcon("fas-times");
                removeIcon.setIconSize(12);
                btnRemover.setGraphic(removeIcon);
                btnRemover.getStyleClass().add("botao-remover-alerta");

                label.getStyleClass().add("texto-alerta");

                hbox.setAlignment(Pos.CENTER_LEFT);
                hbox.setSpacing(10);
                HBox.setHgrow(label, Priority.ALWAYS);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                hbox.getChildren().addAll(label, spacer, btnRemover);

                btnRemover.setOnAction(event -> {
                    String item = getItem();
                    if (item != null) {
                        listaAlertas.getItems().remove(item);
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    label.setText(item);
                    setGraphic(hbox);
                    setStyle("-fx-text-fill: #dc3545;");
                }
            }
        });

        // Adicionar os alertas
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
    private void alternarTelaSolicitacao() {
        if (!telaSolicitacaoAtiva) {
            ocultarTodasTelas();
            if (conteudoEmprestimo != null) {
                conteudoEmprestimo.setVisible(true);
            }
            configurarBotao(botaoSolicitarEmprestimo, "Voltar", "fas-arrow-left", true);
            telaSolicitacaoAtiva = true;
        } else {
            voltarDashboard();
        }
    }

    @FXML
    private void alternarTelaPagamento() {
        if (!telaPagamentoAtiva) {
            ocultarTodasTelas();
            if (conteudoPagamento != null) {
                conteudoPagamento.setVisible(true);
            }
            configurarBotao(botaoRealizarPagamento, "Voltar", "fas-arrow-left", true);
            telaPagamentoAtiva = true;
        } else {
            voltarDashboard();
        }
    }

    @FXML
    private void alternarTelaGerenciamento() {
        if (!telaGerenciamentoAtiva) {
            ocultarTodasTelas();
            if (conteudoGerenciamento != null) {
                conteudoGerenciamento.setVisible(true);
            }
            configurarBotao(botaoGerenciarClientes, "Voltar", "fas-arrow-left", true);
            telaGerenciamentoAtiva = true;
        } else {
            voltarDashboard();
        }
    }

    @FXML
    private void alternarTelaRelatorios() {
        if (!telaRelatoriosAtiva) {
            ocultarTodasTelas();
            if (conteudoRelatorios != null) {
                conteudoRelatorios.setVisible(true);
            }
            configurarBotao(botaoRelatorios, "Voltar", "fas-arrow-left", true);
            telaRelatoriosAtiva = true;
        } else {
            voltarDashboard();
        }
    }

    private void ocultarTodasTelas() {
        if (conteudoDashboard != null) {
            conteudoDashboard.setVisible(false);
        }
        if (conteudoEmprestimo != null) {
            conteudoEmprestimo.setVisible(false);
        }
        if (conteudoPagamento != null) {
            conteudoPagamento.setVisible(false);
        }
        if (conteudoGerenciamento != null) {
            conteudoGerenciamento.setVisible(false);
        }
        if (conteudoRelatorios != null) {
            conteudoRelatorios.setVisible(false);
        }
        if (conteudoPrincipal != null) {
            conteudoPrincipal.setVisible(false);
        }
    }

    private void voltarDashboard() {
        ocultarTodasTelas();
        if (conteudoDashboard != null) {
            conteudoDashboard.setVisible(true);
        }
        if (conteudoPrincipal != null) {
            conteudoPrincipal.setVisible(true);
        }
        resetarEstados();
        resetarBotoes();
    }

    @FXML
    private void sair() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetarEstados() {
        telaSolicitacaoAtiva = false;
        telaPagamentoAtiva = false;
        telaGerenciamentoAtiva = false;
        telaRelatoriosAtiva = false;
    }

    private void configurarBotao(Button botao, String texto, String icone, boolean voltar) {
        if (botao != null) {
            botao.setText(texto);
            if (voltar) {
                botao.getStyleClass().add("voltar");
            } else {
                botao.getStyleClass().remove("voltar");
            }
            botao.setGraphic(new FontIcon(icone));
        }
    }

    private void resetarBotoes() {
        if (botaoSolicitarEmprestimo != null) {
            botaoSolicitarEmprestimo.setText("Solicitar Empréstimo");
            botaoSolicitarEmprestimo.getStyleClass().remove("voltar");
            botaoSolicitarEmprestimo.setGraphic(new FontIcon("fas-hand-holding-usd"));
        }

        if (botaoRealizarPagamento != null) {
            botaoRealizarPagamento.setText("Realizar Pagamento");
            botaoRealizarPagamento.getStyleClass().remove("voltar");
            botaoRealizarPagamento.setGraphic(new FontIcon("fas-money-bill-wave"));
        }

        if (botaoGerenciarClientes != null) {
            botaoGerenciarClientes.setText("Gerenciar Clientes");
            botaoGerenciarClientes.getStyleClass().remove("voltar");
            botaoGerenciarClientes.setGraphic(new FontIcon("fas-users"));
        }

        if (botaoRelatorios != null) {
            botaoRelatorios.setText("Gerar Relatórios");
            botaoRelatorios.getStyleClass().remove("voltar");
            botaoRelatorios.setGraphic(new FontIcon("fas-chart-line"));
        }
    }
}