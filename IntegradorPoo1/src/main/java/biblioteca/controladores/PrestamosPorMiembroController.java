package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Miembro;
import biblioteca.modelo.Prestamo;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la ventana que muestra los préstamos activos de un miembro.
 */
public class PrestamosPorMiembroController {

    @FXML
    private TextField campoIdMiembro; // Campo para ingresar el ID del miembro
    @FXML
    private Button btnBuscar;         // Botón para buscar los préstamos
    @FXML
    private Label mensajeError;       // Label para mostrar mensajes de error

    @FXML
    private TableView<Prestamo> tablaPrestamos; // Tabla para mostrar los préstamos
    @FXML
    private TableColumn<Prestamo, Long> colIdPrestamo; // Columna para el ID del préstamo
    @FXML
    private TableColumn<Prestamo, String> colTituloLibro; // Columna para el título del libro
    @FXML
    private TableColumn<Prestamo, String> colFechaInicio; // Columna para la fecha de inicio
    @FXML
    private TableColumn<Prestamo, String> colFechaVencimiento; // Columna para la fecha de vencimiento

    private final Servicio servicio = App.getServicio(); // Servicio para acceder a la lógica de negocio
    private final ObservableList<Prestamo> datos = FXCollections.observableArrayList(); // Lista observable para la tabla

    /**
     * Inicializa la tabla y sus columnas, y configura el formato de fechas.
     */
    @FXML
    public void initialize() {
        colIdPrestamo.setCellValueFactory(
                data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());

        colTituloLibro.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getCopiaLibro().getLibro().getTitulo()));

        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        colFechaInicio.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFechaInicio().format(formato)));

        colFechaVencimiento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getFechaVencimiento().format(formato)));

        tablaPrestamos.setItems(datos);
    }

    /**
     * Busca los préstamos activos del miembro cuyo ID se ingresa en el campo correspondiente.
     * Valida el campo, muestra mensajes de error si corresponde y actualiza la tabla.
     */
    @FXML
    private void buscarPrestamos() {
        mensajeError.setText("");
        datos.clear();

        String texto = campoIdMiembro.getText().trim();

        if (texto.isEmpty()) {
            mensajeError.setText("Debe ingresar un ID.");
            return;
        }

        try {
            Long id = Long.parseLong(texto);
            Miembro miembro = servicio.buscarMiembroPorId(id);

            if (miembro == null) {
                mensajeError.setText("Miembro no encontrado.");
                return;
            }

            List<Prestamo> prestamosActivos = miembro.getLibrosPrestados()
                    .stream()
                    .filter(p -> p.getFechaDevolucion() == null)
                    .collect(Collectors.toList());

            if (prestamosActivos.isEmpty()) {
                mensajeError.setText("Este miembro no tiene préstamos activos.");
            }

            datos.setAll(prestamosActivos);

        } catch (NumberFormatException e) {
            mensajeError.setText("El ID debe ser numérico.");
        } catch (Exception e) {
            mensajeError.setText("Ocurrió un error al buscar los préstamos.");
            e.printStackTrace();
        }
    }
}