package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Rack;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AgregarModificarRackController {

    @FXML
    private TextField campoDescripcion;

    private Rack rack;
    private Runnable onGuardar;

    public void setRack(Rack rack) {
        this.rack = rack;
        if (rack != null) {
            campoDescripcion.setText(rack.getDescripcion());
        }
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

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

    @FXML
    private void cancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) campoDescripcion.getScene().getWindow();
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
