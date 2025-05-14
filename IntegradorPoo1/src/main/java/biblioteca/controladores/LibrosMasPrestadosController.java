package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Prestamo;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de libros más prestados.
 * Permite visualizar un resumen de los libros más prestados en el sistema.
 */
public class LibrosMasPrestadosController {

    /**
     * Tabla que muestra el resumen de libros y la cantidad de veces que fueron prestados.
     */
    @FXML
    private TableView<Map.Entry<String, Long>> tablaResumen;

    /**
     * Columna que muestra el título del libro.
     */
    @FXML
    private TableColumn<Map.Entry<String, Long>, String> colTitulo;

    /**
     * Columna que muestra la cantidad de veces que el libro fue prestado.
     */
    @FXML
    private TableColumn<Map.Entry<String, Long>, Long> colCantidad;

    /**
     * Inicializa la vista configurando las columnas y cargando los datos de los libros más prestados.
     */
    @FXML
    public void initialize() {
        colTitulo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey()));
        colCantidad.setCellValueFactory(cellData -> new SimpleObjectProperty<Long>(cellData.getValue().getValue()));

        List<Prestamo> prestamos = App.getServicio().getRepositorio().buscarTodos(Prestamo.class);

        Map<String, Long> conteo = prestamos.stream()
                .map(p -> p.getCopiaLibro().getLibro().getTitulo())
                .collect(Collectors.groupingBy(titulo -> titulo, Collectors.counting()));

        List<Map.Entry<String, Long>> librosOrdenados = conteo.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());

        ObservableList<Map.Entry<String, Long>> datos = FXCollections.observableArrayList(librosOrdenados);
        tablaResumen.setItems(datos);
    }

    /**
     * Cierra la ventana actual de la vista de libros más prestados.
     */
    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaResumen.getScene().getWindow();
        stage.close();
    }
}