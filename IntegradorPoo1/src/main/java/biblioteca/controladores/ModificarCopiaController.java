package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controlador para la ventana de modificación del estado de una copia de libro.
 */
public class ModificarCopiaController {

    @FXML
    private ComboBox<EstadoCopia> comboEstado; // ComboBox para seleccionar el nuevo estado

    private CopiaLibro copia; // Copia de libro a modificar

    /**
     * Establece la copia de libro a modificar y actualiza el ComboBox con su estado actual.
     * @param copia CopiaLibro a modificar.
     */
    public void setCopia(CopiaLibro copia) {
        this.copia = copia;
        comboEstado.setValue(copia.getEstado());
    }

    /**
     * Inicializa el ComboBox con los posibles estados de la copia.
     */
    @FXML
    public void initialize() {
        comboEstado.setItems(FXCollections.observableArrayList(EstadoCopia.values()));
    }

    /**
     * Guarda el nuevo estado de la copia en la base de datos.
     * Valida que se haya seleccionado un estado antes de guardar.
     */
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
        Stage stage = (Stage) comboEstado.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta informativa al usuario.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
