package emp.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertHelper {
    public static void showSuccessMessage(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}