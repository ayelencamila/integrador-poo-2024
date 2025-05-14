package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.Prestamo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador para la vista de gestión de préstamos.
 * Permite al usuario visualizar los préstamos registrados y registrar devoluciones.
 */
public class VerPrestamosController {

    @FXML private TableView<Prestamo> tablaPrestamos;
    @FXML private TableColumn<Prestamo, Long> colId;
    @FXML private TableColumn<Prestamo, String> colMiembro;
    @FXML private TableColumn<Prestamo, String> colLibro;
    @FXML private TableColumn<Prestamo, LocalDate> colInicio;
    @FXML private TableColumn<Prestamo, LocalDate> colVencimiento;
    @FXML private TableColumn<Prestamo, LocalDate> colDevolucion;
    @FXML private TableColumn<Prestamo, String> colEstado;

    private ObservableList<Prestamo> datos;

    /**
     * Método que se ejecuta al inicializar la vista.
     * Configura las columnas de la tabla y carga los préstamos registrados desde el sistema.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));
        colMiembro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getMiembro().getApellido() + ", " + cell.getValue().getMiembro().getNombre()
        ));
        colLibro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getCopiaLibro().getLibro().getTitulo()
        ));
        colInicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaInicio()));
        colVencimiento.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaVencimiento()));
        colDevolucion.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaDevolucion()));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getFechaDevolucion() == null ? "Activo" : "Devuelto"
        ));

        // Cargar los préstamos registrados
        List<Prestamo> prestamos = App.getServicio().getRepositorio().buscarTodos(Prestamo.class);
        datos = FXCollections.observableArrayList(prestamos);
        tablaPrestamos.setItems(datos);
    }

    /**
     * Método que se ejecuta al presionar el botón de devolver.
     * Registra la devolución del préstamo seleccionado y actualiza la tabla.
     */
    @FXML
    private void devolver() {
        Prestamo prestamo = tablaPrestamos.getSelectionModel().getSelectedItem();

        if (prestamo == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Seleccioná un préstamo.");
            return;
        }

        if (prestamo.getFechaDevolucion() != null) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Este préstamo ya fue devuelto.");
            return;
        }

        // Registrar la devolución
        prestamo.setFechaDevolucion(LocalDate.now());

        // Cambiar el estado de la copia
        CopiaLibro copia = prestamo.getCopiaLibro();
        copia.setEstado(EstadoCopia.DISPONIBLE);

        // Guardar los cambios en el sistema
        App.getServicio().registrarDevolucion(prestamo, copia);

        // Actualizar la tabla
        tablaPrestamos.refresh();

        mostrarAlerta(Alert.AlertType.INFORMATION, "Devolución registrada correctamente.");
    }

    /**
     * Muestra una alerta con el mensaje especificado.
     * @param tipo El tipo de alerta (INFORMATION, WARNING, etc.).
     * @param mensaje El mensaje que se mostrará en la alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}