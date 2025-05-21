package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Rack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class VerRacksController {

    @FXML
    private TableView<Rack> tablaRacks;

    @FXML
    private TableColumn<Rack, Long> colId;

    @FXML
    private TableColumn<Rack, String> colDescripcion;

    private ObservableList<Rack> datos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleLongProperty(cell.getValue().getId()).asObject());
        colDescripcion.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getDescripcion()));

        cargarRacks();
    }

    private void cargarRacks() {
        List<Rack> lista = App.getServicio().buscarTodosRacks();
        datos.setAll(lista);
        tablaRacks.setItems(datos);
    }

    @FXML
    private void agregarRack() {
        abrirFormulario(null);
    }

    @FXML
    private void modificarRack() {
        Rack seleccionado = tablaRacks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            abrirFormulario(seleccionado);
        } else {
            mostrarAlerta("Seleccioná un rack para modificar.");
        }
    }

    @FXML
    private void eliminarRack() {
        Rack seleccionado = tablaRacks.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar este rack?", ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmar");
            confirmacion.showAndWait().ifPresent(res -> {
                if (res == ButtonType.YES) {
                    App.getServicio().eliminarRack(seleccionado);
                    cargarRacks();
                }
            });
        } else {
            mostrarAlerta("Seleccioná un rack para eliminar.");
        }
    }

    private void abrirFormulario(Rack rack) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/biblioteca/agregarModificarRack.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(rack == null ? "Agregar Rack" : "Modificar Rack");
            stage.initModality(Modality.APPLICATION_MODAL);

            AgregarModificarRackController controller = loader.getController();
            controller.setRack(rack);
            controller.setOnGuardar(() -> cargarRacks());

            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Información");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
