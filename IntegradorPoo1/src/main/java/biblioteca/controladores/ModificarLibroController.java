package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Libro;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la vista de modificación de libros.
 * Permite al usuario editar los datos de un libro existente y guardar los cambios en el sistema.
 */
public class ModificarLibroController {

    /**
     * Libro que se está modificando.
     */
    private Libro libro;

    @FXML private TextField txtTitulo;
    @FXML private TextField txtAutor;
    @FXML private TextField txtCategoria;
    @FXML private TextField txtIsbn;
    @FXML private TextField txtEditorial;
    @FXML private TextField txtIdioma;

    /**
     * Establece el libro que se desea modificar y carga sus datos en los campos de texto.
     * 
     * @param libro El libro que se desea modificar.
     */
    public void setLibro(Libro libro) {
        this.libro = libro;
        txtTitulo.setText(libro.getTitulo());
        txtAutor.setText(libro.getAutor());
        txtCategoria.setText(libro.getCategoria());
        txtIsbn.setText(libro.getIsbn());
        txtEditorial.setText(libro.getEditorial());
        txtIdioma.setText(libro.getIdioma());
    }

    /**
     * Método que se ejecuta al presionar el botón de guardar.
     * Actualiza los datos del libro con la información ingresada y guarda los cambios en el sistema.
     */
    @FXML
    private void guardarCambios() {
        if (txtTitulo.getText().trim().isEmpty() ||
            txtAutor.getText().trim().isEmpty() ||
            txtCategoria.getText().trim().isEmpty() ||
            txtIsbn.getText().trim().isEmpty() ||
            txtEditorial.getText().trim().isEmpty() ||
            txtIdioma.getText().trim().isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos incompletos");
            alerta.setHeaderText(null);
            alerta.setContentText("Todos los campos deben estar completos.");
            alerta.showAndWait();
            return;
        }

        libro.setTitulo(txtTitulo.getText());
        libro.setAutor(txtAutor.getText());
        libro.setCategoria(txtCategoria.getText());
        libro.setIsbn(txtIsbn.getText());
        libro.setEditorial(txtEditorial.getText());
        libro.setIdioma(txtIdioma.getText());

        App.getServicio().modificarLibro(libro);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText("El libro fue modificado con éxito.");
        alert.showAndWait();

        Stage stage = (Stage) txtTitulo.getScene().getWindow();
        stage.close();
    }
}