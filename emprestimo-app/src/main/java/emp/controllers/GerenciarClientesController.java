package emp.controllers;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.dao.ClienteDAO;
import emp.models.Cliente;
import emp.services.ClienteService;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Controlador responsável pelas operações de gerenciamento de clientes.
 * 
 * Exibe os clientes em um TableView com colunas de informações (nome, CPF, telefone, e-mail e status)
 * e possibilita ações como visualizar, editar e excluir clientes.
 *
 * Além disso, permite a filtragem por termo de busca, status e data de cadastro.
 *
 * Opera em conjunto com o ClienteService para atualizar a lista compartilhada de clientes e utiliza
 * ClienteDAO para operações de banco de dados na tabela de clientes.
 */
public class GerenciarClientesController {

    /** Tabela que lista os Clientes. */
    @FXML
    private TableView<Cliente> tabelaClientes;

    /** Campo de texto para busca de clientes. */
    @FXML
    private TextField campoBusca;

    /** Combobox para selecionar o filtro por status (Todos, Ativo, Inativo). */
    @FXML
    private ComboBox<String> filtroStatus;

    /** DatePicker para filtrar clientes pela data de cadastro. */
    @FXML
    private DatePicker filtroData;

    /** Botão para abrir o formulário de novo cliente. */
    @FXML
    private Button botaoNovoCliente;

    /** DAO para executar operações no banco de dados referentes a Cliente. */
    private ClienteDAO clienteDAO = new ClienteDAO();

    /**
     * Método de inicialização do controlador.
     * Configura a tabela, os filtros e carrega os dados iniciais dos clientes.
     */
    @FXML
    private void initialize() {
        configurarTabela();
        configurarFiltros();
        carregarDadosIniciais();
    }

    /**
     * Configura as colunas da tabela de clientes e vincula a lista compartilhada proveniente do ClienteService.
     * 
     * As colunas configuradas são:
     * - Nome, CPF, Telefone, E-mail e Status.
     * - Uma coluna de Ações que disponibiliza os botões de visualizar, editar e excluir.
     */
    @SuppressWarnings("unchecked")
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
        colunaStatus.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().isAtivo() ? "Ativo" : "Inativo")
        );

        TableColumn<Cliente, Void> colunaAcoes = new TableColumn<>("Ações");
        colunaAcoes.setPrefWidth(120);
        colunaAcoes.setCellFactory(coluna -> new TableCell<>() {
            private final Button btnVisualizar = new Button();
            private final Button btnEditar = new Button();
            private final Button btnExcluir = new Button();
            private final HBox containerAcoes = new HBox(5);
            {
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

                btnVisualizar.setOnAction(evento -> {
                    Cliente cliente = getTableView().getItems().get(getIndex());
                    visualizarCliente(cliente);
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
            colunaNome, colunaCpf, colunaTelefone, colunaEmail, colunaStatus, colunaAcoes
        );

        // Liga o TableView à lista compartilhada do ClienteService
        tabelaClientes.setItems(ClienteService.getClientes());
    }

    /**
     * Configura o filtro de status, adicionando as opções "Todos", "Ativo" e "Inativo".
     */
    private void configurarFiltros() {
        filtroStatus.getItems().addAll("Todos", "Ativo", "Inativo");
        filtroStatus.setValue("Todos");
    }

    /**
     * Carrega os dados iniciais dos clientes atualizando a lista compartilhada.
     * Este método consulta o banco de dados pelas informações na tabela de clientes através do ClienteService.
     */
    private void carregarDadosIniciais() {
        try {
            ClienteService.atualizarClientes();
            System.out.println("Clientes carregados: " + ClienteService.getClientes().size());
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErro("Erro ao carregar clientes: " + e.getMessage());
        }
    }

    /**
     * Abre o formulário para adicionar um novo cliente.
     * Após o fechamento do diálogo, os dados são recarregados se a operação for confirmada.
     */
    @FXML
    private void adicionarCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/emp/views/FormularioCliente.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Novo Cliente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaClientes.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            FormularioClienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicado()) {
                carregarDadosIniciais();
            }
        } catch (IOException e) {
            mostrarErro("Erro ao abrir formulário de cliente: " + e.getMessage());
        }
    }

    /**
     * Realiza a busca de clientes aplicando os filtros de termo, status e data de cadastro.
     * Atualiza o TableView com os resultados filtrados.
     */
    @FXML
    private void buscarClientes() {
        try {
            String termoBusca = campoBusca.getText().toLowerCase();
            String statusSelecionado = filtroStatus.getValue();
            LocalDate dataSelecionada = filtroData.getValue();

            List<Cliente> todosClientes = new ClienteDAO().buscarTodos();
            tabelaClientes.setItems(FXCollections.observableArrayList(
                todosClientes.stream()
                    .filter(cliente -> 
                        (termoBusca.isEmpty() ||
                         cliente.getNome().toLowerCase().contains(termoBusca) ||
                         cliente.getCpf().contains(termoBusca)) &&
                        (statusSelecionado.equals("Todos") ||
                         (statusSelecionado.equals("Ativo") && cliente.isAtivo()) ||
                         (statusSelecionado.equals("Inativo") && !cliente.isAtivo())) &&
                        (dataSelecionada == null || cliente.getDataCadastro().equals(dataSelecionada))
                    )
                    .collect(Collectors.toList())
            ));
        } catch (SQLException e) {
            e.printStackTrace();
            mostrarErro("Erro ao buscar clientes: " + e.getMessage());
        }
    }

    /**
     * Abre o formulário para editar os dados do cliente selecionado.
     * Após a edição, a tabela é atualizada se houver confirmação.
     *
     * @param cliente Cliente a ser editado.
     */
    @FXML
    private void editarCliente(Cliente cliente) {
        if (cliente != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/emp/views/FormularioCliente.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.setTitle("Editar Cliente");
                dialogStage.initModality(Modality.WINDOW_MODAL);
                dialogStage.initOwner(tabelaClientes.getScene().getWindow());
                dialogStage.setScene(new Scene(root));

                FormularioClienteController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setCliente(cliente);

                dialogStage.showAndWait();

                if (controller.isOkClicado()) {
                    tabelaClientes.refresh();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mostrarErro("Erro ao abrir formulário de edição");
            }
        }
    }

    /**
     * Exclui o cliente selecionado, confirmando a operação pelo usuário.
     *
     * @param cliente Cliente a ser excluído.
     */
    @FXML
    private void excluirCliente(Cliente cliente) {
        if (cliente != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Excluir Cliente");
            alert.setContentText("Tem certeza que deseja excluir o cliente " + cliente.getNome() + "?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                try {
                    clienteDAO.excluir(cliente.getCpf());
                    ClienteService.atualizarClientes();
                    mostrarErro("Cliente excluído com sucesso!");
                } catch (SQLException e) {
                    mostrarErro("Erro ao excluir cliente do banco de dados: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Exibe uma janela de alerta com a mensagem de erro especificada.
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
     * Abre a janela de detalhes do cliente.
     *
     * @param cliente Cliente cujos detalhes serão exibidos.
     */
    private void visualizarCliente(Cliente cliente) {
        if (cliente != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/emp/views/DetalhesCliente.fxml"));
                Parent root = loader.load();

                Stage dialogStage = new Stage();
                dialogStage.initStyle(StageStyle.DECORATED);
                dialogStage.setTitle("Detalhes do Cliente - " + cliente.getNome());
                dialogStage.setScene(new Scene(root));
                dialogStage.setMaximized(true);
                dialogStage.setResizable(true);

                DetalhesClienteController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setCliente(cliente);

                dialogStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                mostrarErro("Erro ao abrir detalhes do cliente: " + e.getMessage());
            }
        }
    }
}