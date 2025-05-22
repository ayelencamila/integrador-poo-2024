package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de modificación de datos de un miembro de la biblioteca.
 */
public class ModificarMiembroController {

    @FXML private TextField campoId;         // Campo para mostrar el ID del miembro
    @FXML private TextField campoNombre;     // Campo para el nombre del miembro
    @FXML private TextField campoApellido;   // Campo para el apellido del miembro
    @FXML private PasswordField campoClave;  // Campo para la clave del miembro
    @FXML private ComboBox<EstadoUsuario> comboEstado; // ComboBox para el estado del miembro

    private Miembro miembro; // Miembro a modificar

    /**
     * Establece el miembro a modificar y carga sus datos en los campos del formulario.
     * @param miembro Miembro a modificar.
     */
    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
        campoId.setText(String.valueOf(miembro.getId()));
        campoNombre.setText(miembro.getNombre());
        campoApellido.setText(miembro.getApellido());
        campoClave.setText(miembro.getClave());
        comboEstado.getItems().setAll(EstadoUsuario.values());
        comboEstado.setValue(miembro.getEstado());
    }

    /**
     * Guarda los cambios realizados en el miembro.
     * Valida que todos los campos estén completos antes de guardar.
     */
    @FXML
    private void guardar() {
        if (campoNombre.getText().isEmpty() || campoApellido.getText().isEmpty() || campoClave.getText().isEmpty()) {
            mostrarAlerta("Todos los campos deben estar completos.");
            return;
        }

        miembro.setNombre(campoNombre.getText());
        miembro.setApellido(campoApellido.getText());
        miembro.setClave(campoClave.getText());
        miembro.setEstado(comboEstado.getValue());

        App.getServicio().actualizarMiembro(miembro);
        cerrarVentana();
    }

    /**
     * Cancela la operación y cierra la ventana actual.
     */
    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    /**
     * Cierra la ventana actual.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) campoId.getScene().getWindow();
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