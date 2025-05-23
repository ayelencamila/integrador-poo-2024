package biblioteca.controladores;

import biblioteca.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Controlador principal de la aplicación.
 * Proporciona métodos para navegar entre las diferentes vistas de la
 * biblioteca.
 */
public class PrincipalController {


    /**
     * Abre la vista para ver la lista de libros registrados.
     */
    @FXML
    private void verLibros() {
        abrirVista("verLibros.fxml", "Lista de Libros");
    }

    /**
     * Abre la vista para ver la lista de copias.
     */
    @FXML
    private void verCopias() {
        abrirVista("verCopias.fxml", "Gestión de Copias");
    }

    /**
     * Abre la vista para gestionar las multas.
     */
    @FXML
    private void verMultas() {
        abrirVista("verMultas.fxml", "Gestión de Multas");
    }

    /**
     * Abre la vista para gestionar los préstamos.
     */
    @FXML
    private void verPrestamos() {
        abrirVista("verPrestamos.fxml", "Gestión de Préstamos");
    }

    /**
     * Abre la vista para ver el informe de los libros más prestados.
     */
    @FXML
    private void verMasPrestados() {
        abrirVista("librosMasPrestados.fxml", "Libros más prestados");
    }

    /**
     * Abre la vista para registrar un nuevo préstamo.
     */
    @FXML
    private void abrirRegistroPrestamo() {
        abrirVista("realizarPrestamo.fxml", "Registrar Préstamo");
    }

    @FXML
    private void abrirPrestamosPorMiembro() {
        abrirVista("prestamosPorMiembro.fxml", "Préstamos por Miembro");
    }
    @FXML
    private void verMiembros() {
        abrirVista("verMiembros.fxml", "Miembros");
    }
    @FXML private Button botonCerrarSesion;

    @FXML
    private void cerrarSesion() {
        try {
            Stage actual = (Stage) botonCerrarSesion.getScene().getWindow();
            actual.close();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("/biblioteca/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar sesión");
            loginStage.setScene(scene);
            loginStage.show();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }   
    @FXML
    private void verRacks() {
        abrirVista("verRacks.fxml", "Gestión de Racks");
    }


    /**
     * Método genérico para abrir una vista.
     * Carga un archivo FXML y lo muestra en una nueva ventana.
     *
     * @param recurso El nombre del archivo FXML de la vista.
     * @param titulo  El título de la ventana.
     */
    private void abrirVista(String recurso, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(recurso));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}