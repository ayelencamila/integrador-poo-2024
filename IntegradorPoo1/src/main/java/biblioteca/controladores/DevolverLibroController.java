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
 * Controlador para la vista de devolución de libros.
 * Permite al usuario registrar la devolución de libros prestados.
 */
public class DevolverLibroController {

    @FXML
    private TableView<Prestamo> tablaPrestamos;

    @FXML
    private TableColumn<Prestamo, Long> colId;

    @FXML
    private TableColumn<Prestamo, String> colMiembro;

    @FXML
    private TableColumn<Prestamo, String> colLibro;

    @FXML
    private TableColumn<Prestamo, LocalDate> colInicio;

    @FXML
    private TableColumn<Prestamo, LocalDate> colVencimiento;

    private ObservableList<Prestamo> datos;

    /**
     * Inicializa la vista configurando las columnas de la tabla y cargando los préstamos activos.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<Long>(cell.getValue().getId()));
        colMiembro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getMiembro().getApellido() + ", " + cell.getValue().getMiembro().getNombre()
        ));
        colLibro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getCopiaLibro().getLibro().getTitulo()
        ));
        colInicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<LocalDate>(cell.getValue().getFechaInicio()));
        colVencimiento.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<LocalDate>(cell.getValue().getFechaVencimiento()));

        List<Prestamo> prestamosActivos = App.getServicio().buscarPrestamosSinDevolucion();
        datos = FXCollections.observableArrayList(prestamosActivos);
        tablaPrestamos.setItems(datos);
    }

    /**
     * Registra la devolución del libro seleccionado y actualiza la tabla.
     * Si no hay préstamo seleccionado, muestra una advertencia.
     */
    @FXML
    private void devolver() {
        Prestamo prestamo = tablaPrestamos.getSelectionModel().getSelectedItem();

        if (prestamo != null) {
            prestamo.setFechaDevolucion(LocalDate.now());

            CopiaLibro copia = prestamo.getCopiaLibro();
            copia.setEstado(EstadoCopia.DISPONIBLE);

            App.getServicio().registrarDevolucion(prestamo, copia);

            datos.remove(prestamo);
            tablaPrestamos.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Devolución registrada");
            alert.setHeaderText(null);
            alert.setContentText("El libro fue devuelto correctamente.");
            alert.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Seleccioná un préstamo activo.");
            alerta.showAndWait();
        }
    }
}
