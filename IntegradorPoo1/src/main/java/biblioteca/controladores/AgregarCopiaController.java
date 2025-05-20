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

public class AgregarCopiaController {

    @FXML
    private ComboBox<TipoCopia> comboTipo;

    @FXML
    private ComboBox<EstadoCopia> comboEstado;

    @FXML
    private ComboBox<Rack> comboRack;

    private Libro libroSeleccionado;

    public void setLibro(Libro libro) {
        this.libroSeleccionado = libro;
    }

    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList(TipoCopia.values()));
        comboEstado.setItems(FXCollections.observableArrayList(EstadoCopia.values()));

        // Cargar racks desde base
        List<Rack> racks = App.getServicio().getRepositorio().buscarTodos(Rack.class);
        comboRack.setItems(FXCollections.observableArrayList(racks));
    }

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

    @FXML
    private void cancelar() {
        Stage stage = (Stage) comboTipo.getScene().getWindow();
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
