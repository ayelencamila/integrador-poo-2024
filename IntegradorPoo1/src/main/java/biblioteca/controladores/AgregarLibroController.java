package biblioteca.controladores;

import biblioteca.modelo.Libro;
import biblioteca.App;
import biblioteca.repositorios.Repositorio;
import biblioteca.servicios.Servicio;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * Controlador para la vista de agregar un nuevo libro.
 * Permite al usuario ingresar los datos de un libro y guardarlo en el sistema.
 */
public class AgregarLibroController {

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtEditorial;
    @FXML private TextField txtIdioma;

    /**
     * Método que se ejecuta al presionar el botón de guardar.
     * Crea un nuevo libro con los datos ingresados y lo guarda en el sistema.
     */
    @FXML
    private void guardarLibro() {
        try {
            String titulo = txtTitulo.getText().trim();
            String autor = txtAutor.getText().trim();
            String isbn = txtIsbn.getText().trim();

            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Campos obligatorios", "El título, autor e ISBN no pueden estar vacíos.");
                return;
            }

            Repositorio repo = App.getServicio().getRepositorio();
            Servicio servicio = new Servicio(repo);

            boolean duplicado = false;
            for (Libro existente : servicio.buscarTodosLibros()) {
                if (existente.getTitulo().equalsIgnoreCase(titulo)
                        && existente.getAutor().equalsIgnoreCase(autor)
                        && existente.getIsbn().equalsIgnoreCase(isbn)) {
                    duplicado = true;
                    break;
                }
            }

            if (duplicado) {
                mostrarAlerta(Alert.AlertType.WARNING, "Duplicado", "El libro ya está registrado.");
                return;
            }

            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setCategoria(txtCategoria.getText().trim());
            libro.setIsbn(isbn);
            libro.setEditorial(txtEditorial.getText().trim());
            libro.setIdioma(txtIdioma.getText().trim());

            servicio.agregarLibro(libro);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Libro guardado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al guardar el libro.");
        }
    }

    /**
     * Muestra una alerta al usuario con el tipo, título y mensaje especificados.
     *
     * @param tipo    Tipo de alerta (INFORMATION, WARNING, ERROR, etc.)
     * @param titulo  Título de la alerta
     * @param mensaje Mensaje a mostrar en la alerta
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}