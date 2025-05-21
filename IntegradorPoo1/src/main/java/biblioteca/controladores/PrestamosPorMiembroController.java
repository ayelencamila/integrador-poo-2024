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

public class PrestamosPorMiembroController {

    @FXML
    private TextField campoIdMiembro;
    @FXML
    private Button btnBuscar;
    @FXML
    private Label mensajeError;

    @FXML
    private TableView<Prestamo> tablaPrestamos;
    @FXML
    private TableColumn<Prestamo, Long> colIdPrestamo;
    @FXML
    private TableColumn<Prestamo, String> colTituloLibro;
    @FXML
    private TableColumn<Prestamo, String> colFechaInicio;
    @FXML
    private TableColumn<Prestamo, String> colFechaVencimiento;

    private final Servicio servicio = App.getServicio();
    private final ObservableList<Prestamo> datos = FXCollections.observableArrayList();

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
