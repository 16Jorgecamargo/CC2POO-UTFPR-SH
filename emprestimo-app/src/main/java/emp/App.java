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
    
    // Dimensões das telas
    private static final double LOGIN_WIDTH = 400;
    private static final double LOGIN_HEIGHT = 600;
    private static final double DASHBOARD_WIDTH = 1920;
    private static final double DASHBOARD_HEIGHT = 1080;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        scene = new Scene(loadFXML("login"), LOGIN_WIDTH, LOGIN_HEIGHT);
        stage.setTitle("Sistema de Empréstimos");
        stage.setMinWidth(LOGIN_WIDTH);
        stage.setMinHeight(LOGIN_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        // Ajusta dimensões baseado na tela
        switch (fxml) {
            case "login":
                stage.setWidth(LOGIN_WIDTH);
                stage.setHeight(LOGIN_HEIGHT);
                stage.setResizable(false);
                break;
            case "dashboard":
            case "gerenciarClientes":
                stage.setWidth(DASHBOARD_WIDTH);
                stage.setHeight(DASHBOARD_HEIGHT);
                stage.setResizable(true);
                break;
        }
        
        // Carrega nova tela e centraliza
        scene.setRoot(loadFXML(fxml));
        stage.centerOnScreen();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML: " + fxml);
            throw e;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}