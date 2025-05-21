package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.Libro;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class VerCopiasController {

    @FXML
    private ComboBox<Libro> comboLibros;
    @FXML
    private TableView<CopiaLibro> tablaCopias;
    @FXML
    private TableColumn<CopiaLibro, Long> colId;
    @FXML
    private TableColumn<CopiaLibro, String> colTipo;
    @FXML
    private TableColumn<CopiaLibro, String> colEstado;
    @FXML
    private TableColumn<CopiaLibro, String> colRack;
    @FXML
    private TableColumn<CopiaLibro, String> colPrestadoA;

    private ObservableList<CopiaLibro> datosCopias;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleLongProperty(cellData.getValue().getId()).asObject());

        colTipo.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo().toString()));

        colEstado.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEstado().toString()));

        colRack.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getRack().getDescripcion()));

        colPrestadoA.setCellValueFactory(cellData -> {
    CopiaLibro copia = cellData.getValue();
        if (copia.getEstado() == EstadoCopia.PRESTADA && copia.getPrestamo() != null) {
            var miembro = copia.getPrestamo().getMiembro();
            return new javafx.beans.property.SimpleStringProperty(
                miembro.getNombre() + " " + miembro.getApellido()
            );
        } else {
            return new javafx.beans.property.SimpleStringProperty("—");
        }
});

        List<Libro> libros = App.getServicio().buscarTodosLibros();
        comboLibros.setItems(FXCollections.observableArrayList(libros));
    }

    @FXML
    private void cargarCopias() {
        Libro seleccionado = comboLibros.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            List<CopiaLibro> copias = App.getServicio().buscarCopiasPorLibro(seleccionado);
            datosCopias = FXCollections.observableArrayList(copias);
            tablaCopias.setItems(datosCopias);
        } else {
            mostrarAlerta("Debe seleccionar un libro.");
        }
    }

    @FXML
    private void agregarCopia() {
        Libro libroSeleccionado = comboLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado == null) {
            mostrarAlerta("Debe seleccionar un libro para asociar la copia.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("agregarCopia.fxml"));
            Scene scene = new Scene(loader.load());

            AgregarCopiaController controller = loader.getController();
            controller.setLibro(libroSeleccionado);

            Stage stage = new Stage();
            stage.setTitle("Agregar Copia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarCopias();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana de agregar copia.");
        }
    }

    @FXML
    private void modificarCopia() {
        CopiaLibro seleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una copia.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("modificarCopia.fxml"));
            Scene scene = new Scene(loader.load());

            ModificarCopiaController controller = loader.getController();
            controller.setCopia(seleccionada);

            Stage stage = new Stage();
            stage.setTitle("Modificar Estado de Copia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarCopias();
            tablaCopias.refresh();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("No se pudo abrir la ventana.");
        }
    }

    @FXML
    private void eliminarCopia() {
        CopiaLibro seleccionada = tablaCopias.getSelectionModel().getSelectedItem();

        if (seleccionada == null) {
            mostrarAlerta("Debe seleccionar una copia para eliminar.");
            return;
        }

        if (seleccionada.getEstado() == EstadoCopia.PRESTADA) {
            mostrarAlerta("No se puede eliminar una copia que está prestada.");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Estás seguro que querés eliminar esta copia?");

        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                App.getServicio().getRepositorio().iniciarTransaccion();
                App.getServicio().getRepositorio().eliminar(seleccionada);
                App.getServicio().getRepositorio().confirmarTransaccion();

                mostrarAlerta("Copia eliminada correctamente.");
                cargarCopias();
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Aviso");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
