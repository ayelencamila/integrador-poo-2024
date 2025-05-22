package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para la ventana que permite visualizar, agregar, modificar y dar de baja miembros de la biblioteca.
 */
public class VerMiembrosController {

    @FXML
    private TableView<Miembro> tablaMiembros; // Tabla para mostrar los miembros
    @FXML
    private TableColumn<Miembro, Long> colId; // Columna para el ID del miembro
    @FXML
    private TableColumn<Miembro, String> colNombre; // Columna para el nombre del miembro
    @FXML
    private TableColumn<Miembro, String> colApellido; // Columna para el apellido del miembro
    @FXML
    private TableColumn<Miembro, EstadoUsuario> colEstado; // Columna para el estado del miembro

    private final Servicio servicio = App.getServicio(); // Servicio para acceder a los datos
    private List<Miembro> miembros; // Lista de miembros

    /**
     * Inicializa la tabla de miembros y carga todos los miembros existentes.
     * Configura las columnas de la tabla.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colApellido.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellido()));
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getEstado()));

        miembros = servicio.buscarTodosMiembros();
        tablaMiembros.setItems(FXCollections.observableArrayList(miembros));
    }

    /**
     * Abre la ventana para agregar un nuevo miembro y recarga la tabla al finalizar.
     */
    @FXML
    private void agregarMiembro() {
        abrirVentana("agregarMiembro.fxml", "Agregar Miembro");
        recargar();
    }

    /**
     * Abre la ventana para modificar el miembro seleccionado.
     * Muestra una alerta si no hay ningún miembro seleccionado.
     */
    @FXML
    private void modificarMiembro() {
        Miembro seleccionado = tablaMiembros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un miembro para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("modificarMiembro.fxml"));
            Scene scene = new Scene(loader.load());

            ModificarMiembroController controller = loader.getController();
            controller.setMiembro(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Miembro");
            stage.setScene(scene);
            stage.showAndWait();

            recargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Da de baja al miembro seleccionado cambiando su estado.
     * Muestra una alerta si no hay ningún miembro seleccionado.
     */
    @FXML
    private void darDeBajaMiembro() {
        Miembro seleccionado = tablaMiembros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un miembro para dar de baja.");
            return;
        }

        seleccionado.setEstado(EstadoUsuario.BAJA);
        servicio.actualizarMiembro(seleccionado);
        recargar();
    }

    /**
     * Recarga la tabla de miembros con los datos actualizados.
     */
    private void recargar() {
        miembros = servicio.buscarTodosMiembros();
        tablaMiembros.getItems().clear();
        tablaMiembros.setItems(FXCollections.observableArrayList(miembros));
    }

    /**
     * Abre una ventana secundaria con el FXML y título especificados.
     * @param fxml Nombre del archivo FXML.
     * @param titulo Título de la ventana.
     */
    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta de advertencia con el mensaje recibido.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}