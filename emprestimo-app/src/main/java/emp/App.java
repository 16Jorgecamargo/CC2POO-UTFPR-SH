package emp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Classe principal do aplicativo de Empréstimos.
 * Inicia a interface JavaFX e gerencia a troca de telas via FXML.
 */
public class App extends Application {
    private static Scene scene;
    private static Stage stage;

    // Dimensões das telas
    private static final double LOGIN_WIDTH = 400;
    private static final double LOGIN_HEIGHT = 600;

    /**
     * Inicia a aplicação carregando a tela de login.
     *
     * @param primaryStage palco principal da aplicação.
     * @throws IOException se ocorrer erro ao carregar o FXML.
     */
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

    /**
     * Define a nova raiz (tela) a partir de um arquivo FXML e ajusta dimensões conforme a tela.
     *
     * @param fxml nome do arquivo FXML (sem extensão).
     * @throws IOException se ocorrer erro no carregamento do arquivo.
     */
    public static void setRoot(String fxml) throws IOException {
        switch (fxml) {
            case "login":
                stage.setWidth(LOGIN_WIDTH);
                stage.setHeight(LOGIN_HEIGHT);
                stage.setResizable(false);
                break;
            case "dashboard":
                stage.setMaximized(true);
                stage.setResizable(true);
                break;
        }
        scene.setRoot(loadFXML(fxml));
        stage.centerOnScreen();
    }

    /**
     * Carrega o arquivo FXML informado.
     *
     * @param fxml nome do arquivo FXML (sem extensão).
     * @return Parent resultado do carregamento do FXML.
     * @throws IOException se ocorrer erro durante o carregamento.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("views/" + fxml + ".fxml"));
            return fxmlLoader.load();
        } catch (IOException e) {
            System.err.println("Erro ao carregar FXML: " + fxml);
            throw e;
        }
    }

    /**
     * Método principal que inicia o aplicativo.
     *
     * @param args argumentos de linha de comando.
     */
    public static void main(String[] args) {
        launch();
    }
}