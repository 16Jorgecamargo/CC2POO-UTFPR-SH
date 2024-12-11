package emp.controllers;

import emp.App;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import emp.models.User;
import javafx.scene.layout.HBox;

public class SettingsController {
    @FXML private CheckBox emailNotificationCheck;
    @FXML private CheckBox smsNotificationCheck;
    @FXML private CheckBox whatsappNotificationCheck;
    @FXML private TextArea latePaymentMsgArea;
    @FXML private TextArea dueDateMsgArea;
    @FXML private CheckBox autoBackupCheck;
    @FXML private ComboBox<String> backupFrequencyCombo;
    @FXML private TextField backupDirField;
    @FXML private TableView<User> usersTable;
    @FXML private TableColumn<User, String> usernameColumn;
    @FXML private TableColumn<User, String> userTypeColumn;
    @FXML private TableColumn<User, String> statusColumn;
    @FXML private TableColumn<User, Void> actionsColumn;

    @FXML
    public void initialize() {
        // Configurar ComboBox de frequência de backup
        backupFrequencyCombo.setItems(FXCollections.observableArrayList(
            "Diário", "Semanal", "Mensal"
        ));
        backupFrequencyCombo.getSelectionModel().selectFirst();

        // Configurar mensagens padrão
        latePaymentMsgArea.setText("Prezado cliente, seu pagamento está atrasado...");
        dueDateMsgArea.setText("Prezado cliente, seu pagamento vence em breve...");

        // Configurar colunas da tabela de usuários
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        userTypeColumn.setCellValueFactory(new PropertyValueFactory<>("userType"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Editar");
            private final Button blockButton = new Button("Bloquear");
            private final HBox buttons = new HBox(5, editButton, blockButton);

            {
                editButton.setOnAction(e -> handleEditUser(getTableRow().getItem()));
                blockButton.setOnAction(e -> handleBlockUser(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    @FXML
    private void handleSelectBackupDir() {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Selecionar Diretório de Backup");
        File dir = dirChooser.showDialog(null);
        if (dir != null) {
            backupDirField.setText(dir.getAbsolutePath());
        }
    }

    @FXML
    private void handleExportBackup() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar Backup");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Arquivo de Texto", "*.txt")
        );
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // TODO: Implementar exportação
            AlertHelper.showSuccessMessage("Backup exportado com sucesso!");
        }
    }

    @FXML
    private void handleImportBackup() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Importar Backup");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Arquivo de Texto", "*.txt")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            // TODO: Implementar importação
            AlertHelper.showSuccessMessage("Backup importado com sucesso!");
        }
    }

    @FXML
    private void handleSaveSettings() {
        // TODO: Implementar salvamento das configurações
        AlertHelper.showSuccessMessage("Configurações salvas com sucesso!");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }

    private void handleEditUser(User user) {
        // TODO: Implementar edição de usuário
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Editar Usuário");
    }

    private void handleBlockUser(User user) {
        // TODO: Implementar bloqueio de usuário
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Bloquear Usuário");
    }
}
