package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class VerMiembrosController {

    @FXML
    private TableView<Miembro> tablaMiembros;
    @FXML
    private TableColumn<Miembro, Long> colId;
    @FXML
    private TableColumn<Miembro, String> colNombre;
    @FXML
    private TableColumn<Miembro, String> colApellido;
    @FXML
    private TableColumn<Miembro, EstadoUsuario> colEstado;

    private final Servicio servicio = App.getServicio();
    private List<Miembro> miembros;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getId()));
        colNombre.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getNombre()));
        colApellido.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getApellido()));
        colEstado.setCellValueFactory(d -> new javafx.beans.property.SimpleObjectProperty<>(d.getValue().getEstado()));

        miembros = servicio.buscarTodosMiembros();
        tablaMiembros.setItems(FXCollections.observableArrayList(miembros));
    }

    @FXML
    private void agregarMiembro() {
        abrirVentana("agregarMiembro.fxml", "Agregar Miembro");
        recargar();
    }

    @FXML
    private void modificarMiembro() {
        Miembro seleccionado = tablaMiembros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un miembro para modificar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("modificarMiembro.fxml"));
            Scene scene = new Scene(loader.load());

            ModificarMiembroController controller = loader.getController();
            controller.setMiembro(seleccionado);

            Stage stage = new Stage();
            stage.setTitle("Modificar Miembro");
            stage.setScene(scene);
            stage.showAndWait();

            recargar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void darDeBajaMiembro() {
        Miembro seleccionado = tablaMiembros.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un miembro para dar de baja.");
            return;
        }

        seleccionado.setEstado(EstadoUsuario.BAJA);
        servicio.actualizarMiembro(seleccionado);
        recargar();
    }

    private void recargar() {
    miembros = servicio.buscarTodosMiembros();
    tablaMiembros.getItems().clear();
    tablaMiembros.setItems(FXCollections.observableArrayList(miembros));
    }

    private void abrirVentana(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarAlerta(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Atención");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
