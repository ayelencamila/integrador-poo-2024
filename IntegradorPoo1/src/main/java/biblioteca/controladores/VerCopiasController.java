package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Prestamo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para la ventana que permite visualizar, agregar, modificar y eliminar copias de libros.
 */
public class VerCopiasController {

    @FXML
    private ComboBox<Libro> comboLibros; // ComboBox para seleccionar un libro
    @FXML
    private TableView<CopiaLibro> tablaCopias; // Tabla para mostrar las copias del libro seleccionado
    @FXML
    private TableColumn<CopiaLibro, Long> colId; // Columna para el ID de la copia
    @FXML
    private TableColumn<CopiaLibro, String> colTipo; // Columna para el tipo de copia
    @FXML
    private TableColumn<CopiaLibro, String> colEstado; // Columna para el estado de la copia
    @FXML
    private TableColumn<CopiaLibro, String> colRack; // Columna para el rack de la copia
    @FXML
    private TableColumn<CopiaLibro, String> colPrestadoA; // Columna para el nombre del miembro que tiene la copia prestada

    private ObservableList<CopiaLibro> datosCopias; // Lista observable para la tabla de copias

    /**
     * Inicializa la tabla y el ComboBox de libros.
     * Configura las columnas y carga los libros disponibles.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());

        colTipo.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo().toString()));

        colEstado.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado().toString()));

        colRack.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRack().getDescripcion()));

        colPrestadoA.setCellValueFactory(cellData -> {
            CopiaLibro copia = cellData.getValue();

            Prestamo activo = copia.getPrestamos().stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .findFirst()
                .orElse(null);

            if (activo != null) {
                var miembro = activo.getMiembro();
                return new javafx.beans.property.SimpleStringProperty(
                    miembro.getNombre() + " " + miembro.getApellido()
                );
            } else {
                return new javafx.beans.property.SimpleStringProperty("—");
            }
        });

        List<Libro> libros = App.getServicio().buscarTodosLibros();
        comboLibros.setItems(FXCollections.observableArrayList(libros));
    }

    /**
     * Carga las copias del libro seleccionado en la tabla.
     * Muestra una alerta si no se selecciona ningún libro.
     */
    @FXML
    private void cargarCopias() {
        Libro seleccionado = comboLibros.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            List<CopiaLibro> copias = App.getServicio().buscarCopiasPorLibro(seleccionado);
            datosCopias = FXCollections.observableArrayList(copias);
            tablaCopias.setItems(datosCopias);
        } else {
            mostrarAlerta("Debe seleccionar un libro.");
        }
    }

    /**
     * Abre la ventana para agregar una nueva copia al libro seleccionado.
     * Muestra una alerta si no se selecciona ningún libro.
     */
    @FXML
    private void agregarCopia() {
        Libro libroSeleccionado = comboLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un libro para asociar la copia.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("agregarCopia.fxml"));
            Scene scene = new Scene(loader.load());

            AgregarCopiaController controller = loader.getController();
            controller.setLibro(libroSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Agregar Copia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarCopias();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de agregar copia.");
        }
    }

    /**
     * Abre la ventana para modificar el estado de la copia seleccionada.
     * Muestra una alerta si no se selecciona ninguna copia.
     */
    @FXML
    private void modificarCopia() {
        CopiaLibro seleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una copia.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("modificarCopia.fxml"));
            Scene scene = new Scene(loader.load());

            ModificarCopiaController controller = loader.getController();
            controller.setCopia(seleccionada);

            Stage stage = new Stage();
            stage.setTitle("Modificar Estado de Copia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarCopias();
            tablaCopias.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana.");
        }
    }

    /**
     * Elimina la copia seleccionada si no está prestada.
     * Solicita confirmación antes de eliminar y muestra alertas según corresponda.
     */
    @FXML
    private void eliminarCopia() {
        CopiaLibro seleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una copia para eliminar.");
            return;
        }

        if (seleccionada.getEstado() == EstadoCopia.PRESTADA) {
            mostrarAlerta("No se puede eliminar una copia que está prestada.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro que querés eliminar esta copia?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                App.getServicio().getRepositorio().iniciarTransaccion();
                App.getServicio().getRepositorio().eliminar(seleccionada);
                App.getServicio().getRepositorio().confirmarTransaccion();

                mostrarAlerta("Copia eliminada correctamente.");
                cargarCopias();
            }
        });
    }

    /**
     * Muestra una alerta informativa al usuario.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}