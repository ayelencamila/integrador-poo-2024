package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModificarMiembroController {

    @FXML private TextField campoId;
    @FXML private TextField campoNombre;
    @FXML private TextField campoApellido;
    @FXML private PasswordField campoClave;
    @FXML private ComboBox<EstadoUsuario> comboEstado;

    private Miembro miembro;

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
        campoId.setText(String.valueOf(miembro.getId()));
        campoNombre.setText(miembro.getNombre());
        campoApellido.setText(miembro.getApellido());
        campoClave.setText(miembro.getClave());
        comboEstado.getItems().setAll(EstadoUsuario.values());
        comboEstado.setValue(miembro.getEstado());
    }

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

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) campoId.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
