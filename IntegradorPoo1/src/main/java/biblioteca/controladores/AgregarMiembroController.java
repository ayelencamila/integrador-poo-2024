package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AgregarMiembroController {

    @FXML
    private TextField campoNombre;

    @FXML
    private TextField campoApellido;

    @FXML
    private PasswordField campoClave;

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

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) campoNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
