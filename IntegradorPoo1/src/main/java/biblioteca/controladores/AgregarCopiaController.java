package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Rack;
import biblioteca.modelo.TipoCopia;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para la ventana de agregar una nueva copia de libro.
 */
public class AgregarCopiaController {

    @FXML
    private ComboBox<TipoCopia> comboTipo; // ComboBox para seleccionar el tipo de copia

    @FXML
    private ComboBox<EstadoCopia> comboEstado; // ComboBox para seleccionar el estado de la copia

    @FXML
    private ComboBox<Rack> comboRack; // ComboBox para seleccionar el rack donde se ubicará la copia

    private Libro libroSeleccionado; // Libro al que pertenece la copia

    /**
     * Establece el libro seleccionado al que se le agregará la copia.
     * @param libro Libro seleccionado.
     */
    public void setLibro(Libro libro) {
        this.libroSeleccionado = libro;
    }

    /**
     * Inicializa los ComboBox con los valores posibles y carga los racks desde la base de datos.
     */
    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(TipoCopia.values()));
        comboEstado.setItems(FXCollections.observableArrayList(EstadoCopia.values()));

        // Cargar racks desde base de datos
        List<Rack> racks = App.getServicio().getRepositorio().buscarTodos(Rack.class);
        comboRack.setItems(FXCollections.observableArrayList(racks));
    }

    /**
     * Guarda la nueva copia del libro en la base de datos.
     * Valida que todos los campos estén completos antes de guardar.
     */
    @FXML
    private void guardarCopia() {
        TipoCopia tipo = comboTipo.getValue();
        EstadoCopia estado = comboEstado.getValue();
        Rack rack = comboRack.getValue();

        if (tipo == null || estado == null || rack == null || libroSeleccionado == null) {
            mostrarAlerta("Debe completar todos los campos.");
            return;
        }

        CopiaLibro copia = new CopiaLibro();
        copia.setTipo(tipo);
        copia.setEstado(estado);
        copia.setRack(rack);
        copia.setLibro(libroSeleccionado);

        App.getServicio().getRepositorio().iniciarTransaccion();
        App.getServicio().getRepositorio().insertar(copia);
        App.getServicio().getRepositorio().confirmarTransaccion();

        mostrarAlerta("Copia agregada correctamente.");

        Stage stage = (Stage) comboTipo.getScene().getWindow();
        stage.close();
    }

    /**
     * Cancela la operación y cierra la ventana actual.
     */
    @FXML
    private void cancelar() {
        Stage stage = (Stage) comboTipo.getScene().getWindow();
        stage.close();
    }

    /**
     * Muestra una alerta informativa al usuario.
     * @param mensaje Mensaje a mostrar.
     */
    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}