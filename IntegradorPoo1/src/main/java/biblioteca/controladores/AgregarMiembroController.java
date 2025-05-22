package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de agregar un nuevo miembro a la biblioteca.
 */
public class AgregarMiembroController {

    @FXML
    private TextField campoNombre; // Campo para el nombre del miembro

    @FXML
    private TextField campoApellido; // Campo para el apellido del miembro

    @FXML
    private PasswordField campoClave; // Campo para la clave del miembro

    /**
     * Guarda el nuevo miembro en la base de datos.
     * Valida que todos los campos estén completos antes de guardar.
     * Muestra un mensaje de éxito con el ID generado.
     */
    @FXML
    private void guardar() {
        if (campoNombre.getText().isEmpty() ||
            campoApellido.getText().isEmpty() ||
            campoClave.getText().isEmpty()) {

            mostrarAlerta("Todos los campos deben estar completos.");
            return;
        }

        Miembro nuevo = new Miembro();
        nuevo.setNombre(campoNombre.getText());
        nuevo.setApellido(campoApellido.getText());
        nuevo.setClave(campoClave.getText());
        nuevo.setEstado(EstadoUsuario.ALTA);

        App.getServicio().getRepositorio().iniciarTransaccion();
        App.getServicio().getRepositorio().insertar(nuevo);
        App.getServicio().getRepositorio().confirmarTransaccion();

        Alert exito = new Alert(Alert.AlertType.INFORMATION);
        exito.setTitle("Miembro creado");
        exito.setHeaderText(null);
        exito.setContentText("El miembro fue registrado con ID: " + nuevo.getId());
        exito.showAndWait();

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
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta de advertencia con el mensaje recibido.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}