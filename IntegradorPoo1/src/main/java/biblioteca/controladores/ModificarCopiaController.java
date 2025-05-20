package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ModificarCopiaController {

    @FXML
    private ComboBox<EstadoCopia> comboEstado;

    private CopiaLibro copia;

    public void setCopia(CopiaLibro copia) {
        this.copia = copia;
        comboEstado.setValue(copia.getEstado());
    }

    @FXML
    public void initialize() {
        comboEstado.setItems(FXCollections.observableArrayList(EstadoCopia.values()));
    }

    @FXML
    private void guardar() {
        EstadoCopia nuevoEstado = comboEstado.getValue();
        if (nuevoEstado == null) {
            mostrarAlerta("Debe seleccionar un estado.");
            return;
        }

        copia.setEstado(nuevoEstado);
        App.getServicio().getRepositorio().iniciarTransaccion();
        App.getServicio().getRepositorio().modificar(copia);
        App.getServicio().getRepositorio().confirmarTransaccion();

        mostrarAlerta("Estado modificado correctamente.");
        cerrarVentana();
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) comboEstado.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}

