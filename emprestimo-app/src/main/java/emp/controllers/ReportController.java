package emp.controllers;

import emp.App;
import emp.models.Customer;
import emp.models.Report;
import emp.utils.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.FileChooser;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public class ReportController {
    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;
    @FXML private ComboBox<String> reportType;
    @FXML private ComboBox<Customer> customerSelect;
    @FXML private TableView<Report> reportTable;
    @FXML private TableColumn<Report, LocalDate> dateColumn;
    @FXML private TableColumn<Report, String> customerColumn;
    @FXML private TableColumn<Report, String> descriptionColumn;
    @FXML private TableColumn<Report, Double> valueColumn;
    @FXML private TableColumn<Report, String> statusColumn;

    @FXML
    public void initialize() {
        // Initialize ComboBoxes
        reportType.setItems(FXCollections.observableArrayList(
            "Inadimplência", 
            "Histórico", 
            "Transações"
        ));
        
        // Setup table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private void handleGenerateReport() {
        if (!validateFields()) {
            AlertHelper.showSuccessMessage("Preencha os campos obrigatórios!");
            return;
        }
        
        // TODO: Implement report generation
        AlertHelper.showSuccessMessage("Relatório gerado com sucesso!");
    }

    @FXML
    private void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório PDF");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // TODO: Implement PDF export
            AlertHelper.showSuccessMessage("Relatório exportado com sucesso!");
        }
    }

    @FXML
    private void handleExportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salvar Relatório Excel");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            // TODO: Implement Excel export
            AlertHelper.showSuccessMessage("Relatório exportado com sucesso!");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        App.setRoot("agiota-main-menu");
    }

    private boolean validateFields() {
        return startDate.getValue() != null && 
               endDate.getValue() != null && 
               reportType.getValue() != null;
    }
}