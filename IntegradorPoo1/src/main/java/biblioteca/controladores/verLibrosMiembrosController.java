package biblioteca.controladores;

import biblioteca.modelo.Libro;
import biblioteca.App;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controlador para la ventana que permite a los miembros visualizar y buscar libros disponibles en la biblioteca.
 */
public class verLibrosMiembrosController {

    @FXML private TextField campoBusqueda; // Campo para ingresar el texto de búsqueda
    @FXML private TableView<Libro> tablaLibros; // Tabla para mostrar los libros
    @FXML private TableColumn<Libro, Long> colId; // Columna para el ID del libro
    @FXML private TableColumn<Libro, String> colTitulo; // Columna para el título del libro
    @FXML private TableColumn<Libro, String> colAutor; // Columna para el autor del libro
    @FXML private TableColumn<Libro, String> colCategoria; // Columna para la categoría del libro
    @FXML private TableColumn<Libro, String> colIsbn; // Columna para el ISBN del libro
    @FXML private TableColumn<Libro, String> colEditorial; // Columna para la editorial del libro
    @FXML private TableColumn<Libro, String> colIdioma; // Columna para el idioma del libro

    private ObservableList<Libro> datos = FXCollections.observableArrayList(); // Lista observable de libros

    private Servicio servicio = App.getServicio(); // Servicio para acceder a los datos

    /**
     * Inicializa la tabla de libros y carga todos los libros disponibles.
     * Configura las columnas de la tabla.
     */
    @FXML
    public void initialize() {
        // Configura las columnas
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleLongProperty(data.getValue().getId()).asObject());
        colTitulo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitulo()));
        colAutor.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getAutor()));
        colCategoria.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCategoria()));
        colIsbn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIsbn()));
        colEditorial.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEditorial()));
        colIdioma.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIdioma()));

        // Carga todos los libros al iniciar
        datos.setAll(servicio.obtenerTodosLosLibros());
        tablaLibros.setItems(datos);
    }

    /**
     * Busca libros según el texto ingresado en el campo de búsqueda.
     * Filtra por título, autor o categoría.
     */
    @FXML
    private void buscarLibro() {
        String texto = campoBusqueda.getText().toLowerCase().trim();

        if (texto.isEmpty()) {
            tablaLibros.setItems(datos);
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
     * Restablece la búsqueda y muestra todos los libros disponibles.
     */
    @FXML
    private void restablecerBusqueda() {
        campoBusqueda.clear();
        tablaLibros.setItems(datos);
    }
}