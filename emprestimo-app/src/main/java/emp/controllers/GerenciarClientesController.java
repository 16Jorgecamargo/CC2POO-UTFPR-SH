package emp.controllers;

import java.io.IOException;
import java.time.LocalDate;

import org.kordamp.ikonli.javafx.FontIcon;

import emp.models.Cliente;
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
                new TableColumn[] { colunaNome, colunaCpf, colunaTelefone, colunaEmail, colunaStatus, colunaAcoes });

        // Dados de exemplo
        clientes.add(new Cliente("João Silva", "123.456.789-00", "(47) 99999-9999", "joao@email.com"));
        clientes.add(new Cliente("João pedro", "123.456.789-00", "(47) 99999-9999", "joao@email.com"));
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/emp/views/FormularioCliente.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Novo Cliente");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tabelaClientes.getScene().getWindow());

            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            FormularioClienteController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCliente(null);

            dialogStage.showAndWait();

            if (controller.isOkClicado()) {
                Cliente novoCliente = controller.getCliente();
                clientes.add(novoCliente);
                tabelaClientes.refresh();
            }

        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir formulário de cliente");
        }
    }

    @FXML
    private void buscarClientes() {
        String termoBusca = campoBusca.getText().toLowerCase();
        String statusSelecionado = filtroStatus.getValue();
        LocalDate dataSelecionada = filtroData.getValue();

        ObservableList<Cliente> clientesFiltrados = clientes.filtered(cliente -> {
            boolean correspondeTermoBusca = termoBusca.isEmpty() ||
                    cliente.getNome().toLowerCase().contains(termoBusca) ||
                    cliente.getCpf().contains(termoBusca);

            boolean correspondeStatus = statusSelecionado.equals("Todos") ||
                    (statusSelecionado.equals("Ativo") && cliente.isAtivo()) ||
                    (statusSelecionado.equals("Inativo") && !cliente.isAtivo());

            boolean correspondeData = dataSelecionada == null ||
                    cliente.getDataCadastro().equals(dataSelecionada);

            return correspondeTermoBusca && correspondeStatus && correspondeData;
        });

        tabelaClientes.setItems(clientesFiltrados);
    }

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

                Scene scene = new Scene(root);
                dialogStage.setScene(scene);

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

    @FXML
    private void excluirCliente(Cliente cliente) {
        if (cliente != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmar Exclusão");
            alert.setHeaderText("Excluir Cliente");
            alert.setContentText("Tem certeza que deseja excluir o cliente " + cliente.getNome() + "?");

            if (alert.showAndWait().get() == ButtonType.OK) {
                clientes.remove(cliente);
                tabelaClientes.refresh();
            }
        }
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setContentText(mensagem);
        alert.showAndWait();
    }

    private void visualizarCliente(Cliente cliente) {
        if (cliente != null) {
            try {
                // Carregar o FXML
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/emp/views/DetalhesCliente.fxml"));
                Parent root = loader.load();

                // Criar o Stage
                Stage dialogStage = new Stage();
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                dialogStage.initStyle(StageStyle.DECORATED);
                dialogStage.setTitle("Detalhes do Cliente");
                dialogStage.setResizable(true);

                // Definir dimensões
                dialogStage.setWidth(1920);
                dialogStage.setHeight(1080);
                dialogStage.setMinWidth(1280);
                dialogStage.setMinHeight(720);

                // Definir o owner (janela pai)
                Scene currentScene = tabelaClientes.getScene();
                if (currentScene != null && currentScene.getWindow() != null) {
                    dialogStage.initOwner(currentScene.getWindow());
                }

                // Configurar a cena
                Scene scene = new Scene(root, 1920, 1080);
                dialogStage.setScene(scene);

                // Configurar o controller
                DetalhesClienteController controller = loader.getController();
                controller.setDialogStage(dialogStage);
                controller.setCliente(cliente);

                // Centralizar e mostrar
                dialogStage.centerOnScreen();
                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao abrir detalhes do cliente");
                alert.setContentText("Não foi possível carregar a tela de detalhes.");
                alert.showAndWait();
            }
        }
    }
}
