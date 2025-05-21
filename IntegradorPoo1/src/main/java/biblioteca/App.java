package biblioteca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import biblioteca.repositorios.Repositorio;
import biblioteca.servicios.Servicio;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * App principal de la Biblioteca
 */
public class App extends Application {

    private static Scene scene;
    private static Servicio servicio;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Entrando al método start...");
        // Inicializa el EntityManagerFactory y Repositorio
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");
        servicio = new Servicio(new Repositorio(emf));

        // Generar automáticamente las multas por atraso
        servicio.generarMultas();

        try {
        System.out.println("Cargando login.fxml...");

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/biblioteca/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 300);

        stage.setScene(scene);
        stage.setTitle("Login - Sistema de Biblioteca");
        stage.show();
        primaryStage = stage;

        System.out.println("Login cargado correctamente.");
    } catch (Exception e) {
        System.out.println("❌ Error al cargar login.fxml");
        e.printStackTrace();
    }
    }
    public static Stage getStage() {
        return primaryStage;
    }
    
    public static Servicio getServicio() {
        return servicio;
    }
    private static Stage primaryStage;

    public static FXMLLoader setRoot(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}



