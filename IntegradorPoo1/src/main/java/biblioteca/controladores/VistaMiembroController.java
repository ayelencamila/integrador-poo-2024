package biblioteca.controladores;

import java.util.stream.Collectors;

import biblioteca.App;
import biblioteca.modelo.Libro;
import biblioteca.modelo.Usuario;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.control.*;


public class VistaMiembroController {

    @FXML
    private Button botonCerrarSesion;

    @FXML
private TextField campoBusqueda;

@FXML
private ChoiceBox<String> tipoBusqueda;

@FXML
private TableView<Libro> tablaLibros;

@FXML
private TableColumn<Libro, String> colTitulo;

@FXML
private TableColumn<Libro, String> colAutor;

@FXML
private TableColumn<Libro, String> colCategoria;

@FXML
private Label mensajeError;

@FXML
private Label labelBienvenida;

@FXML
private void initialize() {
    tipoBusqueda.getItems().addAll("Título", "Autor", "Categoría");
    tipoBusqueda.setValue("Título");

    colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
    colAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
    colCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria()));

    tablaLibros.setItems(FXCollections.observableArrayList(App.getServicio().buscarTodosLibros()));
}

@FXML
private void handleBuscar() {
    String filtro = campoBusqueda.getText().trim().toLowerCase();
    String criterio = tipoBusqueda.getValue();

    if (filtro.isEmpty()) {
        mensajeError.setText("Ingrese un valor para buscar.");
        return;
    }

    List<Libro> todos = App.getServicio().buscarTodosLibros();
    List<Libro> filtrados;

    switch (criterio) {
        case "Autor":
            filtrados = todos.stream()
                .filter(l -> l.getAutor() != null && l.getAutor().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
            break;
        case "Categoría":
            filtrados = todos.stream()
                .filter(l -> l.getCategoria() != null && l.getCategoria().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
            break;
        default: // Título
            filtrados = todos.stream()
                .filter(l -> l.getTitulo() != null && l.getTitulo().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
    }

    if (filtrados.isEmpty()) {
        mensajeError.setText("No se encontraron resultados.");
    } else {
        mensajeError.setText("");
    }

    tablaLibros.setItems(FXCollections.observableArrayList(filtrados));
}
    @FXML
    private void handleRestablecer() {
        campoBusqueda.clear();
        tipoBusqueda.setValue("Título");
        mensajeError.setText("");
        tablaLibros.setItems(FXCollections.observableArrayList(App.getServicio().buscarTodosLibros()));
    }
    private Usuario usuario;
    public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
    labelBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
}

    @FXML
    private void cerrarSesion() {
        try {
            Stage actual = (Stage) botonCerrarSesion.getScene().getWindow();
            actual.close();

            FXMLLoader loader = new FXMLLoader(App.class.getResource("/biblioteca/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage loginStage = new Stage();
            loginStage.setTitle("Iniciar sesión");
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
