package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.Miembro;
import biblioteca.modelo.Prestamo;
import biblioteca.servicios.Servicio;
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

    /**
     * Tabla que muestra los préstamos activos.
     */
    @FXML
    private TableView<Prestamo> tablaPrestamos;

    /**
     * Columna que muestra el ID del préstamo.
     */
    @FXML
    private TableColumn<Prestamo, Long> colId;

    /**
     * Columna que muestra el nombre del miembro.
     */
    @FXML
    private TableColumn<Prestamo, String> colMiembro;

    /**
     * Columna que muestra el título del libro.
     */
    @FXML
    private TableColumn<Prestamo, String> colLibro;

    /**
     * Columna que muestra la fecha de inicio del préstamo.
     */
    @FXML
    private TableColumn<Prestamo, LocalDate> colInicio;

    /**
     * Columna que muestra la fecha de vencimiento del préstamo.
     */
    @FXML
    private TableColumn<Prestamo, LocalDate> colVencimiento;

    /**
     * Lista observable de préstamos activos.
     */
    private ObservableList<Prestamo> datos;

    /**
     * Servicio de lógica de negocio para operaciones de biblioteca.
     */
    private Servicio servicio;

    /**
     * Inicializa la vista configurando las columnas de la tabla y cargando los préstamos activos.
     */
    @FXML
    public void initialize() {
        servicio = App.getServicio();

        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));
        colMiembro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getMiembro().getApellido() + ", " + cell.getValue().getMiembro().getNombre()
        ));
        colLibro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
                cell.getValue().getCopiaLibro().getLibro().getTitulo()
        ));
        colInicio.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaInicio()));
        colVencimiento.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getFechaVencimiento()));

        List<Prestamo> prestamosActivos = servicio.buscarPrestamosSinDevolucion();
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
            Miembro miembro = prestamo.getMiembro();
            CopiaLibro copia = prestamo.getCopiaLibro();

            boolean exito = miembro.devolverLibro(copia);

            if (exito) {
                servicio.actualizarMiembro(miembro);
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
                alerta.setContentText("No se pudo realizar la devolución. Verificá el estado del préstamo o la copia.");
                alerta.showAndWait();
            }
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(null);
            alerta.setContentText("Seleccioná un préstamo activo.");
            alerta.showAndWait();
        }
    }
}