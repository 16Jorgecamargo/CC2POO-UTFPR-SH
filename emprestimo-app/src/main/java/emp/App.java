package emp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static final double LOGIN_WIDTH = 400;
    private static final double LOGIN_HEIGHT = 600;
    private static final double DASHBOARD_WIDTH = 1920;
    private static final double DASHBOARD_HEIGHT = 1080;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        scene = new Scene(loadFXML("login"), LOGIN_WIDTH, LOGIN_HEIGHT);
        scene.getStylesheets().add(getClass().getResource("styles/global.css").toExternalForm());
        stage.setTitle("Sistema de Empréstimos");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        try {
            System.out.println("Tentando carregar: " + fxml);
            Parent root = loadFXML(fxml);
            System.out.println("FXML carregado com sucesso");
            
            if (fxml.equals("dashboard")) {
                stage.setWidth(DASHBOARD_WIDTH);
                stage.setHeight(DASHBOARD_HEIGHT);
            } else if (fxml.equals("login")) {
                stage.setWidth(LOGIN_WIDTH);
                stage.setHeight(LOGIN_HEIGHT);
            }
            
            stage.centerOnScreen();
            scene.setRoot(root);
        } catch (Exception e) {
            System.err.println("Erro ao carregar FXML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String resource = "views/" + fxml + ".fxml";
        System.out.println("Procurando recurso: " + resource);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resource));
        if (fxmlLoader.getLocation() == null) {
            throw new IOException("Recurso não encontrado: " + resource);
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}