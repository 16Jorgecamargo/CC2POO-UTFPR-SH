package emp.controllers;

import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.App;
import emp.models.Dashboard;
import emp.dao.DashboardDAO;
import emp.dao.GraficoDAO;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Controlador principal do dashboard do sistema de empréstimos.
 * Gerencia a exibição de estatísticas, gráficos e navegação entre diferentes telas.
 * 
 * Este controlador interage com as seguintes views FXML:
 * - Dashboard principal
 * - Tela de solicitação de empréstimo
 * - Tela de pagamento
 * - Tela de gerenciamento de clientes
 */
public class DashboardController {
    /** Botão para acessar o gerenciamento de clientes */
    @FXML private Button botaoGerenciarClientes;
    
    /** Ícone do botão de gerenciamento */
    @FXML private FontIcon iconeGerenciar;
    
    /** Container para a tela de pagamento */
    @FXML private VBox conteudoPagamento;
    
    /** Controlador da tela de realizar pagamento */
    private RealizarPagamentoController realizarPagamentoController;
    
    /** Botão para realizar pagamento */
    @FXML private Button botaoRealizarPagamento;
    
    /** Indica se a tela de gerenciamento está ativa */
    @FXML private boolean telaGerenciamentoAtiva = false;
    
    /** Indica se a tela de pagamento está ativa */
    private boolean telaPagamentoAtiva = false;
    
    /** Indica se a tela de solicitação está ativa */
    private boolean telaSolicitacaoAtiva = false;

    /** Container para a tela de solicitação de empréstimo */
    @FXML private VBox conteudoEmprestimo;
    
    /** Botão para solicitar empréstimo */
    @FXML private Button botaoSolicitarEmprestimo;

    /** Grupo de botões de alternância para selecionar o período */
    @FXML private ToggleGroup periodoGroup;
    
    /** Label para exibir o total de empréstimos */
    @FXML private Label totalEmprestimosLabel;
    
    /** Conteúdo principal do dashboard */
    @FXML private Node conteudoPrincipal;
    
    /** Conteúdo do dashboard */
    @FXML private VBox dashboardContent;
    
    /** Label para exibir o total emprestado */
    @FXML private Label totalEmprestadoLabel;
    
    /** Label para exibir o saldo devedor */
    @FXML private Label saldoDevedorLabel;
    
    /** Label para exibir o número de empréstimos ativos */
    @FXML private Label emprestimosAtivosLabel;
    
    /** Label para exibir os pagamentos do mês */
    @FXML private Label pagamentosMesLabel;
    
    /** Gráfico de pizza para status dos empréstimos */
    @FXML private PieChart statusEmprestimoChart;
    
    /** Gráfico de linha para evolução dos pagamentos */
    @FXML private LineChart<String, Number> evolucaoPagamentosChart;
    
    /** Lista de alertas */
    @FXML private ListView<String> listaAlertas;
    
    /** Conteúdo do dashboard */
    @FXML private VBox conteudoDashboard;
    
    /** Conteúdo de gerenciamento */
    @FXML private VBox conteudoGerenciamento;

    /** Controlador da tela de solicitação de empréstimo */
    @FXML SolicitarEmprestimoController solicitarEmprestimoController;

    /**
     * Inicializa o controlador do dashboard.
     * Carrega as sub-views necessárias e configura os componentes iniciais.
     * 
     * @throws IOException se houver erro no carregamento dos arquivos FXML
     */
    @FXML
    private void initialize() {
        try {
            FXMLLoader loaderSolicitar = new FXMLLoader(getClass().getResource("/emp/views/SolicitarEmprestimo.fxml"));
            Parent emprestimoRoot = loaderSolicitar.load();
            solicitarEmprestimoController = loaderSolicitar.getController();
            conteudoEmprestimo.getChildren().setAll(emprestimoRoot);
            
            FXMLLoader loaderPagamento = new FXMLLoader(getClass().getResource("/emp/views/RealizarPagamento.fxml"));
            Parent pagamentoRoot = loaderPagamento.load();
            realizarPagamentoController = loaderPagamento.getController();
            conteudoPagamento.getChildren().setAll(pagamentoRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }

        configurarPeriodoSelector();
        configurarAlertas();
        carregarDadosMes();

        // Inicializa os conteúdos como invisíveis
        if (conteudoEmprestimo != null) {
            conteudoEmprestimo.setVisible(false);
        }
        if (conteudoPrincipal != null) {
            conteudoPrincipal.setVisible(false);
        }
    }

    /**
     * Configura o seletor de período para filtrar os dados do dashboard.
     * Adiciona listeners para atualizar os gráficos quando um novo período é selecionado.
     */
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

    /**
     * Carrega os dados do dashboard para o período de hoje.
     */
    private void carregarDadosHoje() {
        carregarDashboard();
        atualizarGraficosPeriodo("Hoje");
    }

    /**
     * Carrega os dados do dashboard para o período da semana.
     */
    private void carregarDadosSemana() {
        carregarDashboard();
        atualizarGraficosPeriodo("Semana");
    }

    /**
     * Carrega os dados do dashboard para o período do mês.
     */
    private void carregarDadosMes() {
        carregarDashboard();
        atualizarGraficosPeriodo("Mês");
    }

    /**
     * Carrega os dados do dashboard para o período do ano.
     */
    private void carregarDadosAno() {
        carregarDashboard();
        atualizarGraficosPeriodo("Ano");
    }

    /**
     * Carrega os dados do dashboard para todos os períodos.
     */
    private void carregarDadosTudo() {
        carregarDashboard();
        atualizarGraficosPeriodo("Total");
    }

    /**
     * Atualiza os gráficos do dashboard com base no período selecionado.
     * 
     * @param periodo String indicando o período ("Hoje", "Semana", "Mês", "Ano", "Total")
     */
    private void atualizarGraficosPeriodo(String periodo) {
        // Obter contagem de status reais
        GraficoDAO graficoDAO = new GraficoDAO();
        Map<String, Integer> statusContagem;
        try {
            statusContagem = graficoDAO.getContagemStatusEmprestimos();
        } catch (SQLException ex) {
            System.err.println("Erro ao carregar contagem de status: " + ex.getMessage());
            return;
        }

        // Exemplo: considerar apenas os status relevantes, ajustando nomes conforme seu sistema.
        int empEmDia = statusContagem.getOrDefault("Em dia", 0);
        int empAtrasados = statusContagem.getOrDefault("Atrasado", 0);
        int empQuitados = statusContagem.getOrDefault("Quitado", 0);
        int total = empEmDia + empAtrasados + empQuitados;
        
        // Atualiza gráfico de pizza
        statusEmprestimoChart.getData().clear();
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data(String.format("Em dia (%d%%)\n%d empréstimos", total > 0 ? (empEmDia * 100) / total : 0, empEmDia), empEmDia),
                new PieChart.Data(String.format("Atrasados (%d%%)\n%d empréstimos", total > 0 ? (empAtrasados * 100) / total : 0, empAtrasados), empAtrasados),
                new PieChart.Data(String.format("Quitados (%d%%)\n%d empréstimos", total > 0 ? (empQuitados * 100) / total : 0, empQuitados), empQuitados)
        );
        statusEmprestimoChart.setData(pieData);
        
        // Mantém o estilo dos labels
        pieData.forEach(data -> {
            final Node node = data.getNode();
            node.setStyle("-fx-pie-label-visible: true; -fx-pie-label-fill: white;");
        });

        // Atualiza o gráfico de linha (conforme sua lógica)
        evolucaoPagamentosChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Pagamentos - " + periodo);
        try {
            if (periodo.equals("Hoje")) {
                List<Double> pagamentosHora = graficoDAO.getPagamentosPorHora();
                for (int i = 0; i < pagamentosHora.size(); i++) {
                    series.getData().add(new XYChart.Data<>(i + "h", pagamentosHora.get(i)));
                }
            } else {
                List<Double> pagamentosMes = graficoDAO.getPagamentosPorMes();
                String[] meses = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
                for (int i = 0; i < pagamentosMes.size(); i++) {
                    series.getData().add(new XYChart.Data<>(meses[i], pagamentosMes.get(i)));
                }
            }
        } catch (SQLException ex) {
            System.err.println("Erro ao carregar dados do gráfico de linha: " + ex.getMessage());
        }
        evolucaoPagamentosChart.getData().add(series);
    }

    /**
     * Configura a lista de alertas do sistema.
     * Personaliza a exibição dos alertas e adiciona botões de remoção para cada item.
     */
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

    /** Container principal do layout */
    @FXML private BorderPane root;

    /** Indica se o modo escuro está ativado */
    private boolean isDarkMode = false;

    /**
     * Alterna entre o modo claro e escuro da interface.
     */
    @FXML
    private void toggleDarkMode() {
        isDarkMode = !isDarkMode;
        if (isDarkMode) {
            root.getStyleClass().add("dark-theme");
        } else {
            root.getStyleClass().remove("dark-theme");
        }
    }

    /**
     * Exibe a tela de solicitação de empréstimo e carrega os dados necessários.
     */
    @FXML
    private void alternarTelaSolicitacao() {
        ocultarTodasTelas();
        if (conteudoEmprestimo != null) {
            conteudoEmprestimo.setVisible(true);
            solicitarEmprestimoController.carregarClientes();
        }
    }

    /**
     * Exibe a tela de pagamento e carrega os dados necessários.
     */
    @FXML
    private void alternarTelaPagamento() {
        ocultarTodasTelas();
        if (conteudoPagamento != null) {
            conteudoPagamento.setVisible(true);
            if (realizarPagamentoController != null) {
                realizarPagamentoController.carregarEmprestimos();
            }
        }
    }

    /**
     * Exibe a tela de gerenciamento de clientes.
     */
    @FXML
    private void alternarTelaGerenciamento() {
        ocultarTodasTelas();
        if (conteudoGerenciamento != null) {
            conteudoGerenciamento.setVisible(true);
        }
    }

    /**
     * Exibe a tela principal do dashboard.
     */
    @FXML
    private void alternarTelaDashboard() {
        ocultarTodasTelas();
        if (conteudoDashboard != null) {
            conteudoDashboard.setVisible(true);
        }
        carregarDashboard();
        resetarEstados();
    }

    /**
     * Oculta todas as telas do sistema.
     * Utilizado antes de exibir uma nova tela.
     */
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
        if (conteudoPrincipal != null) {
            conteudoPrincipal.setVisible(false);
        }
    }

    /**
     * Retorna para a tela principal do dashboard.
     * Reseta todos os estados e configurações dos botões.
     */
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

    /**
     * Realiza o logout do sistema e retorna para a tela de login.
     * 
     * @throws IOException se houver erro ao carregar a tela de login
     */
    @FXML
    private void sair() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Reseta os estados das telas.
     */
    private void resetarEstados() {
        telaSolicitacaoAtiva = false;
        telaPagamentoAtiva = false;
        telaGerenciamentoAtiva = false;
    }

    /**
     * Configura o botão com texto, ícone e estilo apropriado.
     * 
     * @param botao o botão a ser configurado
     * @param texto o texto a ser exibido no botão
     * @param icone o ícone a ser exibido no botão
     * @param voltar indica se o botão é de voltar
     */
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

    /**
     * Reseta os botões para seus estados iniciais.
     */
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

    }

    /**
     * Carrega os dados do dashboard a partir do banco de dados.
     * Atualiza os labels com informações sobre empréstimos e pagamentos.
     * 
     * Consulta as tabelas:
     * - emprestimos (para total emprestado e ativos)
     * - pagamentos (para saldo devedor e pagamentos do mês)
     */
    private void carregarDashboard() {
        try {
            Dashboard daoDashboard = new DashboardDAO().getDashboardData();
            totalEmprestadoLabel.setText(formatMoney(daoDashboard.getTotalEmprestado()));
            saldoDevedorLabel.setText(formatMoney(daoDashboard.getSaldoDevedor()));
            emprestimosAtivosLabel.setText(String.valueOf(daoDashboard.getEmprestimosAtivos()));
            pagamentosMesLabel.setText(formatMoney(daoDashboard.getPagamentosMes()));
        } catch (SQLException e) {
            // Trate ou registre o erro conforme necessário
            System.err.println("Erro ao carregar dados do dashboard: " + e.getMessage());
        }
    }

    /**
     * Formata um valor monetário para exibição.
     * 
     * @param value o valor a ser formatado
     * @return uma string formatada representando o valor monetário
     */
    private String formatMoney(double value) {
        return String.format("R$ %.2f", value);
    }
}