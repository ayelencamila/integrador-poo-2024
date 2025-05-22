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

/**
 * Controlador para la vista de miembro.
 * Permite buscar libros por título, autor o categoría, mostrar todos los libros,
 * mostrar un mensaje de bienvenida y cerrar sesión.
 */
public class VistaMiembroController {

    @FXML
    private Button botonCerrarSesion; // Botón para cerrar sesión

    @FXML
    private TextField campoBusqueda; // Campo de texto para ingresar el filtro de búsqueda

    @FXML
    private ChoiceBox<String> tipoBusqueda; // ChoiceBox para seleccionar el tipo de búsqueda

    @FXML
    private TableView<Libro> tablaLibros; // Tabla para mostrar los libros

    @FXML
    private TableColumn<Libro, String> colTitulo; // Columna para el título del libro

    @FXML
    private TableColumn<Libro, String> colAutor; // Columna para el autor del libro

    @FXML
    private TableColumn<Libro, String> colCategoria; // Columna para la categoría del libro

    @FXML
    private Label mensajeError; // Label para mostrar mensajes de error

    @FXML
    private Label labelBienvenida; // Label para mostrar el mensaje de bienvenida

    private Usuario usuario; // Usuario actual

    /**
     * Inicializa la vista, configurando los controles y cargando los libros.
     * Configura los tipos de búsqueda y las columnas de la tabla.
     */
    @FXML
    private void initialize() {
        tipoBusqueda.getItems().addAll("Título", "Autor", "Categoría");
        tipoBusqueda.setValue("Título");

        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));
        colAutor.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor()));
        colCategoria.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoria()));

        tablaLibros.setItems(FXCollections.observableArrayList(App.getServicio().buscarTodosLibros()));
    }

    /**
     * Busca libros según el filtro y el criterio seleccionados.
     * Muestra un mensaje si no se encuentran resultados o si el filtro está vacío.
     */
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

    /**
     * Restablece la búsqueda, limpia el campo de búsqueda y muestra todos los libros.
     */
    @FXML
    private void handleRestablecer() {
        campoBusqueda.clear();
        tipoBusqueda.setValue("Título");
        mensajeError.setText("");
        tablaLibros.setItems(FXCollections.observableArrayList(App.getServicio().buscarTodosLibros()));
    }

    /**
     * Establece el usuario actual y actualiza el mensaje de bienvenida.
     * @param usuario Usuario que ha iniciado sesión.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        labelBienvenida.setText("Bienvenido, " + usuario.getNombre() + " " + usuario.getApellido());
    }

    /**
     * Cierra la sesión y vuelve a la pantalla de login.
     */
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