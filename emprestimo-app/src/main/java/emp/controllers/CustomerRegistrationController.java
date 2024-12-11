package emp.controllers;

import emp.App;
import emp.models.Customer;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.HBox;
import java.io.IOException;

public class CustomerRegistrationController {
    @FXML private TextField nameField;
    @FXML private TextField cpfField;
    @FXML private TextField addressField;
    @FXML private TextArea creditNoteField;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameColumn;
    @FXML private TableColumn<Customer, String> cpfColumn;
    @FXML private TableColumn<Customer, String> addressColumn;
    @FXML private TableColumn<Customer, String> creditNoteColumn;
    @FXML private TableColumn<Customer, Void> actionsColumn;

    private ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        cpfColumn.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        creditNoteColumn.setCellValueFactory(new PropertyValueFactory<>("creditNote"));
        customerTable.setItems(customers);
        setupActionsColumn();
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("Editar");
            private final Button deleteButton = new Button("Excluir");
            private final HBox buttons = new HBox(5, editButton, deleteButton);

            {
                editButton.setOnAction(e -> handleEdit(getTableRow().getItem()));
                deleteButton.setOnAction(e -> handleDelete(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttons);
            }
        });
    }

    @FXML
    private void handleSave() {
        if (validateFields()) {
            Customer customer = new Customer(
                nameField.getText(),
                cpfField.getText(),
                addressField.getText(),
                creditNoteField.getText()
            );
            customers.add(customer);
            clearFields();
            AlertHelper.showSuccessMessage("Cliente cadastrado com sucesso!");
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }

    private void handleEdit(Customer customer) {
        nameField.setText(customer.getName());
        cpfField.setText(customer.getCpf());
        addressField.setText(customer.getAddress());
        creditNoteField.setText(customer.getCreditNote());
        customers.remove(customer);
    }

    private void handleDelete(Customer customer) {
        customers.remove(customer);
    }

    private void clearFields() {
        nameField.clear();
        cpfField.clear();
        addressField.clear();
        creditNoteField.clear();
    }

    private boolean validateFields() {
        if (nameField.getText().isEmpty() || cpfField.getText().isEmpty() || 
            addressField.getText().isEmpty()) {
            AlertHelper.showSuccessMessage("Por favor, preencha todos os campos obrigatórios!");
            return false;
        }
        return true;
    }
}
