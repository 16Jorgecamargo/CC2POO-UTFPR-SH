package emp.controllers;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.models.Cliente;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;
import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;

public class GerenciarClientesController {
    @FXML
    private TableView<Cliente> tabelaClientes;

    @FXML
    private TextField campoBusca;

    @FXML
    private ComboBox<String> filtroStatus;

    @FXML
    private DatePicker filtroData;

    @FXML
    private Button botaoNovoCliente;
    

    private ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        configurarTabela();
        configurarFiltros();
        carregarDadosIniciais();
    }

    private void configurarTabela() {
        TableColumn<Cliente, String> colunaNome = new TableColumn<>("Nome");
        colunaNome.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNome()));

        TableColumn<Cliente, String> colunaCpf = new TableColumn<>("CPF");
        colunaCpf.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCpf()));

        TableColumn<Cliente, String> colunaTelefone = new TableColumn<>("Telefone");
        colunaTelefone.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefone()));

        TableColumn<Cliente, String> colunaEmail = new TableColumn<>("E-mail");
        colunaEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));

        TableColumn<Cliente, String> colunaStatus = new TableColumn<>("Status");
        colunaStatus
                .setCellValueFactory(data -> new SimpleStringProperty(data.getValue().isAtivo() ? "Ativo" : "Inativo"));

        TableColumn<Cliente, Void> colunaAcoes = new TableColumn<>("Ações");
        colunaAcoes.setPrefWidth(120);
        colunaAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btnVisualizar = new Button();
            private final Button btnEditar = new Button();
            private final Button btnExcluir = new Button();
            private final HBox containerAcoes = new HBox(5);

            {
                // Configurar ícones
                FontIcon iconeVisualizar = new FontIcon("fas-eye");
                iconeVisualizar.setIconSize(16);
                btnVisualizar.setGraphic(iconeVisualizar);
                btnVisualizar.getStyleClass().add("botao-acao");

                FontIcon iconeEditar = new FontIcon("fas-pencil-alt");
                iconeEditar.setIconSize(16);
                btnEditar.setGraphic(iconeEditar);
                btnEditar.getStyleClass().add("botao-acao");

                FontIcon iconeExcluir = new FontIcon("fas-trash");
                iconeExcluir.setIconSize(16);
                btnExcluir.setGraphic(iconeExcluir);
                btnExcluir.getStyleClass().add("botao-acao");

                // Configurar eventos
                btnVisualizar.setOnAction(evento -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    visualizarDetalhes();
                });

                btnEditar.setOnAction(evento -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    editarCliente(cliente);
                });

                btnExcluir.setOnAction(evento -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    excluirCliente(cliente);
                });

                containerAcoes.setAlignment(Pos.CENTER);
                containerAcoes.getChildren().addAll(btnVisualizar, btnEditar, btnExcluir);
            }

            @Override
            protected void updateItem(Void item, boolean vazio) {
                super.updateItem(item, vazio);
                setGraphic(vazio ? null : containerAcoes);
            }
        });

        tabelaClientes.getColumns().setAll(
                new TableColumn[] {colunaNome, colunaCpf, colunaTelefone, colunaEmail, colunaStatus, colunaAcoes});

        // Dados de exemplo
        clientes.add(new Cliente("João Silva", "123.456.789-00", "(47) 99999-9999", "joao@email.com"));
        clientes.add(new Cliente("Maria Santos", "987.654.321-00", "(47) 98888-8888", "maria@email.com"));
        tabelaClientes.setItems(clientes);
    }

    private void carregarDadosIniciais() {
        // TODO: Implement loading initial data from your data source
        // For now, we'll add some sample data
        clientes.add(new Cliente("John Doe", "123.456.789-00", "47 99999-9999", "john@email.com"));
        tabelaClientes.setItems(clientes);
    }

    private void configurarFiltros() {
        filtroStatus.getItems().addAll("Todos", "Ativo", "Inativo");
        filtroStatus.setValue("Todos");
    }

    @FXML
    private void adicionarCliente() {
        // Implementar lógica de adicionar
    }

    @FXML
    private void editarCliente(Cliente cliente) {
        if (cliente != null) {
            // Implementar lógica de edição
        }
    }

    @FXML
    private void excluirCliente(Cliente cliente) {
        if (cliente != null) {
            // Implementar lógica de exclusão
        }
    }

    @FXML
    private void visualizarDetalhes() {
        Cliente clienteSelecionado = tabelaClientes.getSelectionModel().getSelectedItem();
        if (clienteSelecionado != null) {
            // Implementar lógica de visualização
        }
    }
}
