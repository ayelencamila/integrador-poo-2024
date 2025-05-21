package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.Miembro;
import biblioteca.servicios.Servicio;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.format.DateTimeFormatter;

public class QuienTieneCopiaController {

    @FXML
    private TextField campoIdCopia;
    @FXML
    private Label mensaje;
    @FXML
    private TableView<Prestamo> tablaResultado;
    @FXML
    private TableColumn<Prestamo, String> colNombre;
    @FXML
    private TableColumn<Prestamo, String> colFecha;

    private final Servicio servicio = App.getServicio();

    @FXML
    public void initialize() {
        colNombre.setCellValueFactory(data -> {
            Miembro m = data.getValue().getMiembro();
            String nombre = (m.getNombre() != null ? m.getNombre() : "") +
                    (m.getApellido() != null ? " " + m.getApellido() : "");
            return new javafx.beans.property.SimpleStringProperty(nombre.trim());
        });

        colFecha.setCellValueFactory(data -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return new javafx.beans.property.SimpleStringProperty(
                    data.getValue().getFechaInicio().format(formatter));
        });
    }

    @FXML
    public void buscarPrestamo() {
        mensaje.setText("");
        tablaResultado.setVisible(false);
        tablaResultado.getItems().clear();

        String texto = campoIdCopia.getText().trim();
        if (texto.isEmpty()) {
            mensaje.setText("Debe ingresar un ID.");
            return;
        }

        try {
            Long id = Long.parseLong(texto);
            CopiaLibro copia = servicio.getRepositorio().buscarPorId(CopiaLibro.class, id);

            if (copia == null) {
                mensaje.setText("No se encontró la copia.");
                return;
            }

            Prestamo prestamo = copia.getPrestamo();
            if (prestamo == null || prestamo.getFechaDevolucion() != null) {
                mensaje.setText("La copia no está prestada actualmente.");
                return;
            }

            tablaResultado.getItems().add(prestamo);
            tablaResultado.setVisible(true);

        } catch (NumberFormatException e) {
            mensaje.setText("El ID debe ser numérico.");
        } catch (Exception e) {
            mensaje.setText("Ocurrió un error.");
            e.printStackTrace();
        }
    }
}
