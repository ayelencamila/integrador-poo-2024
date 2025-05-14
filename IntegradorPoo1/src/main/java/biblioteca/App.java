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
        // Inicializa el EntityManagerFactory y Repositorio
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("bibliotecaPU");
        servicio = new Servicio(new Repositorio(emf));

        // Generar automáticamente las multas por atraso
        servicio.generarMultas();  // ← esta es la línea nueva

        // Cargar el archivo principal.fxml
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("principal.fxml"));
        scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setScene(scene);
        stage.setTitle("Sistema de Biblioteca");
        stage.show();
    }

    public static Servicio getServicio() {
        return servicio;
    }

    public static FXMLLoader setRoot(String fxml) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        scene.setRoot(fxmlLoader.load());
        return fxmlLoader;
    }

    public static void main(String[] args) {
        launch();
    }
}



