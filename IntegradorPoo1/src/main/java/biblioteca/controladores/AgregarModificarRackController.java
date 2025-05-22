package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Rack;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de agregar o modificar un rack en la biblioteca.
 */
public class AgregarModificarRackController {

    @FXML
    private TextField campoDescripcion; // Campo para la descripción del rack

    private Rack rack; // Rack a modificar (si es edición)
    private Runnable onGuardar; // Acción a ejecutar después de guardar

    /**
     * Establece el rack a modificar. Si es nulo, se asume que se está agregando uno nuevo.
     * @param rack Rack a modificar o null para agregar uno nuevo.
     */
    public void setRack(Rack rack) {
        this.rack = rack;
        if (rack != null) {
            campoDescripcion.setText(rack.getDescripcion());
        }
    }

    /**
     * Establece una acción a ejecutar después de guardar (por ejemplo, refrescar una tabla).
     * @param onGuardar Acción a ejecutar.
     */
    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    /**
     * Guarda el rack en la base de datos. Si es un rack nuevo lo agrega, si existe lo modifica.
     * Valida que la descripción no esté vacía.
     */
    @FXML
    private void guardar() {
        String descripcion = campoDescripcion.getText().trim();

        if (descripcion.isEmpty()) {
            mostrarAlerta("La descripción no puede estar vacía.");
            return;
        }

        if (rack == null) {
            rack = new Rack();
            rack.setDescripcion(descripcion);
            App.getServicio().agregarRack(rack);
        } else {
            rack.setDescripcion(descripcion);
            App.getServicio().modificarRack(rack);
        }

        if (onGuardar != null) onGuardar.run();

        cerrar();
    }

    /**
     * Cancela la operación y cierra la ventana actual.
     */
    @FXML
    private void cancelar() {
        cerrar();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrar() {
        Stage stage = (Stage) campoDescripcion.getScene().getWindow();
        stage.close();
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