package emp.controllers;

import emp.App;
import emp.models.Payment;
import emp.models.Customer;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import java.io.IOException;
import java.time.LocalDate;

public class PaymentMonitoringController {
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private ComboBox<String> statusFilter;
    @FXML
    private TableView<Payment> paymentsTable;
    @FXML
    private TableColumn<Payment, String> customerColumn;
    @FXML
    private TableColumn<Payment, Double> amountColumn;
    @FXML
    private TableColumn<Payment, LocalDate> dateColumn;
    @FXML
    private TableColumn<Payment, String> statusColumn;
    @FXML
    private Label totalPaymentsLabel;
    @FXML
    private Label latePaymentsLabel;
    @FXML
    private Label totalLateLabel;

    private ObservableList<Payment> payments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar ComboBox
        statusFilter.setItems(FXCollections.observableArrayList("Todos", "Em dia", "Atrasado"));
        statusFilter.getSelectionModel().selectFirst();

        // Configurar colunas da tabela
        customerColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCustomer().getName()));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        paymentsTable.setItems(payments);

        // Dados de exemplo
        Customer customer1 = new Customer("João Silva", "123.456.789-00", "Rua A", "Bom pagador");
        Customer customer2 = new Customer("Maria Santos", "987.654.321-00", "Rua B", "Histórico regular");

        payments.addAll(
                new Payment(customer1, 500.0, LocalDate.now().minusDays(5)),
                new Payment(customer2, 1000.0, LocalDate.now().minusDays(15)));

        updateStatistics();
    }

    @FXML
    private void handleFilter() {
        ObservableList<Payment> filteredPayments = FXCollections.observableArrayList(payments);

        // Filter by date
        if (startDate.getValue() != null && endDate.getValue() != null) {
            filteredPayments.removeIf(payment -> payment.getDate().isBefore(startDate.getValue()) ||
                    payment.getDate().isAfter(endDate.getValue()));
        }

        // Filter by status
        if (statusFilter.getValue() != null && !statusFilter.getValue().equals("Todos")) {
            String selectedStatus = statusFilter.getValue();
            filteredPayments.removeIf(payment -> !payment.getStatus().equals(selectedStatus));
        }

        // Update table and statistics
        paymentsTable.setItems(filteredPayments);
        updateStatistics();
    }

    @FXML
    private void handleRegisterPayment() {
        // !TODO Implementar registro de pagamento
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Registrar Pagamento");
    }

    @FXML
    private void handleNotifyCustomer() {
        // !TODO Implementar notificação
        AlertHelper.showSuccessMessage("Função em desenvolvimento: Notificar Cliente");
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }

    private void updateStatistics() {
        double total = payments.stream().mapToDouble(Payment::getAmount).sum();
        long lateCount = payments.stream().filter(p -> p.getStatus().equals("Atrasado")).count();
        double totalLate = payments.stream()
                .filter(p -> p.getStatus().equals("Atrasado"))
                .mapToDouble(Payment::getAmount)
                .sum();

        totalPaymentsLabel.setText(String.format("R$ %.2f", total));
        latePaymentsLabel.setText(String.valueOf(lateCount));
        totalLateLabel.setText(String.format("R$ %.2f", totalLate));
    }
}
