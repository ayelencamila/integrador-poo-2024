package biblioteca.controladores;

import biblioteca.modelo.Libro;
import biblioteca.App;
import biblioteca.servicios.Servicio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class verLibrosMiembrosController {

    @FXML private TextField campoBusqueda;
    @FXML private TableView<Libro> tablaLibros;
    @FXML private TableColumn<Libro, Long> colId;
    @FXML private TableColumn<Libro, String> colTitulo;
    @FXML private TableColumn<Libro, String> colAutor;
    @FXML private TableColumn<Libro, String> colCategoria;
    @FXML private TableColumn<Libro, String> colIsbn;
    @FXML private TableColumn<Libro, String> colEditorial;
    @FXML private TableColumn<Libro, String> colIdioma;

    private ObservableList<Libro> datos = FXCollections.observableArrayList();

    private Servicio servicio = App.getServicio();

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

    @FXML
    private void restablecerBusqueda() {
        campoBusqueda.clear();
        tablaLibros.setItems(datos);
    }
}
