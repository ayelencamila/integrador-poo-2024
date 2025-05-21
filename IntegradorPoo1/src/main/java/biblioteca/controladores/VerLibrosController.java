package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Libro;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controlador para la vista de visualización de libros.
 * Permite al usuario ver, buscar, modificar y eliminar libros registrados en el
 * sistema.
 */
public class VerLibrosController {

    /**
     * Tabla que muestra los libros registrados.
     */
    @FXML
    private TableView<Libro> tablaLibros;

    /**
     * Columna que muestra el ID del libro.
     */
    @FXML
    private TableColumn<Libro, Long> colId;

    /**
     * Columna que muestra el título del libro.
     */
    @FXML
    private TableColumn<Libro, String> colTitulo;

    /**
     * Columna que muestra el autor del libro.
     */
    @FXML
    private TableColumn<Libro, String> colAutor;

    /**
     * Columna que muestra la categoría del libro.
     */
    @FXML
    private TableColumn<Libro, String> colCategoria;

    /**
     * Columna que muestra el ISBN del libro.
     */
    @FXML
    private TableColumn<Libro, String> colIsbn;

    /**
     * Columna que muestra la editorial del libro.
     */
    @FXML
    private TableColumn<Libro, String> colEditorial;

    /**
     * Columna que muestra el idioma del libro.
     */
    @FXML
    private TableColumn<Libro, String> colIdioma;

    /**
     * Campo de texto para buscar libros por título.
     */
    @FXML
    private TextField campoBusqueda;

    /**
     * Lista observable que contiene los libros mostrados en la tabla.
     */
    private ObservableList<Libro> datos;

    /**
     * Inicializa la vista configurando las columnas de la tabla y cargando los
     * libros registrados en el sistema.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<Libro, Long>("id"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<Libro, String>("titulo"));
        colAutor.setCellValueFactory(new PropertyValueFactory<Libro, String>("autor"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<Libro, String>("categoria"));
        colIsbn.setCellValueFactory(new PropertyValueFactory<Libro, String>("isbn"));
        colEditorial.setCellValueFactory(new PropertyValueFactory<Libro, String>("editorial"));
        colIdioma.setCellValueFactory(new PropertyValueFactory<Libro, String>("idioma"));
        colPrecio.setCellValueFactory(
                data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getPrecioEstimado()).asObject());

        List<Libro> libros = App.getServicio().getRepositorio().buscarTodos(Libro.class);
        datos = FXCollections.observableArrayList(libros);
        tablaLibros.setItems(datos);
    }

    /**
     * Elimina el libro seleccionado de la tabla y del sistema.
     * Muestra una confirmación antes de eliminar.
     */
    @FXML
    private void eliminarLibro() {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION, "¿Eliminar el libro seleccionado?",
                    ButtonType.YES, ButtonType.NO);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);

            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    Servicio servicio = App.getServicio();
                    servicio.eliminarLibro(libroSeleccionado);
                    datos.remove(libroSeleccionado);

                    Alert exito = new Alert(Alert.AlertType.INFORMATION);
                    exito.setTitle("Éxito");
                    exito.setHeaderText(null);
                    exito.setContentText("Libro eliminado correctamente.");
                    exito.showAndWait();
                }
            });

        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Atención");
            alerta.setHeaderText(null);
            alerta.setContentText("Seleccioná un libro para eliminar.");
            alerta.showAndWait();
        }
    }

    /**
     * Abre una nueva ventana para modificar los datos del libro seleccionado.
     * Si no hay libro seleccionado, muestra una advertencia.
     */
    @FXML
    private void modificarLibro() {
        Libro libroSeleccionado = tablaLibros.getSelectionModel().getSelectedItem();

        if (libroSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(App.class.getResource("modificarLibro.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = new Stage();
                stage.setTitle("Modificar Libro");
                stage.setScene(scene);

                ModificarLibroController controller = loader.getController();
                controller.setLibro(libroSeleccionado);

                stage.showAndWait();

                tablaLibros.refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText(null);
            alert.setContentText("Seleccioná un libro para modificar.");
            alert.showAndWait();
        }
    }

    /**
     * Filtra los libros en la tabla según el texto ingresado en el campo de
     * búsqueda.
     * Si el campo está vacío, restaura la lista completa.
     */
    @FXML
    private void buscarLibro() {
        String texto = campoBusqueda.getText().toLowerCase().trim();

        if (texto.isEmpty()) {
            tablaLibros.setItems(datos); // 'datos' es la lista completa de libros
            return;
        }

        ObservableList<Libro> filtrados = FXCollections.observableArrayList();

        for (Libro libro : datos) {
            if ((libro.getTitulo() != null && libro.getTitulo().toLowerCase().contains(texto)) ||
                    (libro.getAutor() != null && libro.getAutor().toLowerCase().contains(texto)) ||
                    (libro.getCategoria() != null && libro.getCategoria().toLowerCase().contains(texto))) {
                filtrados.add(libro);
            }
        }

        tablaLibros.setItems(filtrados);
    }

    /**
     * Restaura la lista completa de libros y limpia el campo de búsqueda.
     */
    @FXML
    private void restablecerBusqueda() {
        campoBusqueda.clear();
        tablaLibros.setItems(datos);
    }

    /**
     * Columna que muestra el precio estimado del libro.
     */
    @FXML
    private TableColumn<Libro, Double> colPrecio;

}