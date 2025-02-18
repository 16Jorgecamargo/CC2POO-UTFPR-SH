package emp.controllers;

import emp.dao.EmprestimoDAO;
import emp.models.Cliente;
import emp.models.Emprestimo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.beans.property.SimpleStringProperty;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador responsável por exibir e manipular os detalhes de um Cliente.
 * Exibe informações pessoais, financeiras, o histórico de empréstimos e permite
 * realizar ações como renegociação de empréstimos e envio de lembretes.
 *
 * Interage com os seguintes elementos FXML:
 * - Labels (nome, CPF, status, endereço, telefone, email, data de cadastro, financeiro)
 * - Tabela de empréstimos e suas colunas (ID, valor, prazo, data de vencimento)
 * - Gráficos (BarChart para pagamentos e LineChart para tendências)
 * - ListView para notificações
 *
 * Utiliza o EmprestimoDAO para buscar os dados dos empréstimos a partir do banco de dados.
 */
public class DetalhesClienteController {
    /** Label que exibe o nome do cliente */
    @FXML private Label nomeClienteLabel;
    /** Label que exibe o CPF do cliente */
    @FXML private Label cpfClienteLabel;
    /** Label que exibe o status (ativo/inativo) do cliente */
    @FXML private Label statusClienteLabel;
    /** Label que exibe o endereço do cliente */
    @FXML private Label enderecoLabel;
    /** Label que exibe o telefone do cliente */
    @FXML private Label telefoneLabel;
    /** Label que exibe o email do cliente */
    @FXML private Label emailLabel;
    /** Label que exibe a data de cadastro do cliente */
    @FXML private Label dataCadastroLabel;
    /** Label que exibe o valor total emprestado */
    @FXML private Label valorTotalLabel;
    /** Label que exibe o total pago pelo cliente */
    @FXML private Label totalPagoLabel;
    /** Label que exibe o saldo restante do cliente */
    @FXML private Label saldoRestanteLabel;
    
    /** Tabela para exibir o histórico de empréstimos do cliente */
    @FXML private TableView<EmprestimoRow> tabelaEmprestimos;
    /** Coluna da tabela que exibe o ID do empréstimo */
    @FXML private TableColumn<EmprestimoRow, String> colunaId;
    /** Coluna da tabela que exibe o valor do empréstimo */
    @FXML private TableColumn<EmprestimoRow, String> colunaValor;
    /** Coluna da tabela que exibe o prazo do empréstimo */
    @FXML private TableColumn<EmprestimoRow, String> colunaPrazo;
    /** Coluna da tabela que exibe a data de vencimento do empréstimo */
    @FXML private TableColumn<EmprestimoRow, String> colunaDataVencimento;
    
    /** Gráfico em barras para exibição de pagamentos */
    @FXML private BarChart<String, Number> graficoPagamentos;
    /** Gráfico em linha para exibição de tendências */
    @FXML private LineChart<String, Number> graficoTendencia;
    /** Lista de notificações relacionadas ao cliente */
    @FXML private ListView<String> listaNotificacoes;

    /** Estágio atual do diálogo */
    @SuppressWarnings("unused")
    private Stage dialogStage;
    /** Cliente cujo detalhes serão exibidos */
    private Cliente cliente;
    /** DAO para acesso aos dados dos empréstimos */
    private final EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

    /**
     * Inicializa o controlador.
     * Configura as colunas da tabela de empréstimos com seus respectivos cellValueFactory e cellFactory,
     * habilitando a quebra de linha para cada célula.
     */
    @FXML
    public void initialize() {
        // Configura os cellValueFactory das colunas para converter os dados em String.
        colunaId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
        colunaValor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValor()));
        colunaPrazo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrazo()));
        colunaDataVencimento.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataVencimento()));
        
        // Configura os cellFactory para habilitar a quebra de linha nos textos.
        colunaId.setCellFactory(col -> createWrapCell());
        colunaValor.setCellFactory(col -> createWrapCell());
        colunaPrazo.setCellFactory(col -> createWrapCell());
        colunaDataVencimento.setCellFactory(col -> createWrapCell());
    }

    /**
     * Define o estágio do diálogo.
     * 
     * @param dialogStage o estágio a ser definido
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Define o cliente para o qual os detalhes serão exibidos e preenche os dados na interface.
     * 
     * @param cliente objeto Cliente contendo os dados a serem exibidos
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        preencherDadosCliente();
    }

    /**
     * Preenche os dados do cliente na interface.
     * Atualiza os Labels com as informações pessoais e financeiras, 
     * carrega o histórico de empréstimos, gráficos e notificações.
     */
    private void preencherDadosCliente() {
        if (cliente != null) {
            nomeClienteLabel.setText(cliente.getNome());
            cpfClienteLabel.setText("CPF: " + cliente.getCpf());
            statusClienteLabel.setText("Status: " + (cliente.isAtivo() ? "Ativo" : "Inativo"));
            telefoneLabel.setText(cliente.getTelefone());
            emailLabel.setText(cliente.getEmail());
            enderecoLabel.setText(cliente.getEndereco());
            dataCadastroLabel.setText(cliente.getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            carregarDadosFinanceiros();
            carregarHistoricoEmprestimos();
            carregarGraficos();
            carregarNotificacoes();
        }
    }

    /**
     * Fecha a janela de detalhes do cliente.
     */
    @FXML
    private void fecharDetalhes() {
        Stage stage = (Stage) nomeClienteLabel.getScene().getWindow();
        stage.close();
    }
    
    /**
     * Executa a lógica de renegociação do empréstimo.
     * Método a ser implementado conforme a regra de negócio.
     */
    @FXML
    private void renegociarEmprestimo() {
        // ...implementar lógica de renegociação, se necessário...
    }
    
    /**
     * Executa a lógica de envio de lembrete para o cliente.
     * Método a ser implementado conforme a regra de negócio.
     */
    @FXML
    private void enviarLembrete() {
        // ...implementar lógica de envio de lembrete...
    }
    
    /**
     * Carrega os dados financeiros do cliente.
     * Exibe o valor total emprestado, o total pago e o saldo restante.
     */
    private void carregarDadosFinanceiros() {
        if (cliente != null) {
            valorTotalLabel.setText(String.format("R$ %.2f", cliente.getTotalEmprestado()));
            totalPagoLabel.setText(String.format("R$ %.2f", cliente.getTotalPago()));
            saldoRestanteLabel.setText(String.format("R$ %.2f", cliente.getTotalEmprestado() - cliente.getTotalPago()));
        }
    }
    
    /**
     * Carrega o histórico de empréstimos do cliente.
     * Busca os empréstimos do cliente através do EmprestimoDAO e popula a tabela.
     */
    private void carregarHistoricoEmprestimos() {
        try {
            List<Emprestimo> emprestimos = emprestimoDAO.buscarPorCliente(cliente.getCpf());
            List<EmprestimoRow> rows = emprestimos.stream()
                .map(emp -> new EmprestimoRow(emp))
                .collect(Collectors.toList());
            tabelaEmprestimos.setItems(FXCollections.observableArrayList(rows));
        } catch (SQLException e) {
            mostrarErro("Erro ao carregar empréstimos: " + e.getMessage());
        }
    }
    
    /**
     * Carrega os dados para os gráficos.
     * Método a ser implementado se houver necessidade de exibir gráficos financeiros relacionados ao cliente.
     */
    private void carregarGraficos() {
        // ...implementar carregamento dos gráficos, se necessário...
    }
    
    /**
     * Carrega as notificações relacionadas ao cliente.
     * Método a ser implementado se houver necessidade de exibir notificações.
     */
    private void carregarNotificacoes() {
        // ...implementar carregamento das notificações, se necessário...
    }
    
    /**
     * Classe auxiliar que representa uma linha na tabela de empréstimos.
     */
    public static class EmprestimoRow {
        private final Emprestimo emprestimo;
        
        /**
         * Construtor que inicializa a linha com um objeto Emprestimo.
         * 
         * @param emprestimo objeto Emprestimo que representa os dados desta linha
         */
        public EmprestimoRow(Emprestimo emprestimo) {
            this.emprestimo = emprestimo;
        }
        
        /**
         * Retorna o ID do empréstimo.
         * 
         * @return ID convertido para String
         */
        public String getId() {
            return String.valueOf(emprestimo.getId());
        }
        
        /**
         * Retorna o valor do empréstimo, formatado como moeda.
         * 
         * @return valor formatado (ex: "R$ 1000.00")
         */
        public String getValor() {
            return String.format("R$ %.2f", emprestimo.getValor());
        }
        
        /**
         * Retorna o prazo do empréstimo, em meses.
         * 
         * @return prazo concatenado com " meses"
         */
        public String getPrazo() {
            return emprestimo.getPrazo() + " meses";
        }
        
        /**
         * Retorna a data de vencimento do empréstimo, formatada.
         * 
         * @return data formatada conforme "dd/MM/yyyy"
         */
        public String getDataVencimento() {
            return emprestimo.getDataVencimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }
    }
    
    /**
     * Exibe uma janela de alerta com a mensagem de erro especificada.
     * 
     * @param mensagem texto do erro a ser exibido
     */
    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    /**
     * Cria uma célula customizada para a tabela que permite quebra de linha no texto.
     * 
     * @return um TableCell configurado para encapsular e ajustar o texto adequadamente
     */
    private TableCell<EmprestimoRow, String> createWrapCell() {
        return new TableCell<EmprestimoRow, String>() {
            private final Label label = new Label();
            {
                label.setWrapText(true);
                // Se desejar alinhar o texto, pode configurar: label.setStyle("-fx-alignment: CENTER-LEFT;");
                setGraphic(label);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    label.setText(null);
                } else {
                    label.setText(item);
                    // Ajusta a largura do label para igualar à largura da coluna.
                    label.setPrefWidth(getTableColumn().getWidth());
                }
            }
        };
    }
}