package biblioteca.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Representa un libro en la biblioteca.
 * Un libro contiene información básica como título, autor, categoría, ISBN, editorial e idioma.
 * Además, puede tener múltiples copias asociadas.
 */
@Entity
public class Libro implements Serializable {

    /**
     * Identificador único del libro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Título del libro.
     */
    private String titulo;

    /**
     * Autor del libro.
     */
    private String autor;

    /**
     * Categoría o género del libro.
     */
    private String categoria;

    /**
     * Código ISBN del libro.
     */
    private String isbn;

    /**
     * Editorial que publicó el libro.
     */
    private String editorial;

    /**
     * Idioma en el que está escrito el libro.
     */
    private String idioma;

    /**
     * Lista de copias asociadas a este libro.
     */
    @OneToMany(mappedBy = "libro")
    private List<CopiaLibro> copias;

    /**
     * Constructor por defecto.
     */
    public Libro() {}

    // Getters y setters

    /**
     * Obtiene el identificador único del libro.
     * @return el identificador del libro.
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del libro.
     * @param id el identificador del libro.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el título del libro.
     * @return el título del libro.
     */
    public String getTitulo() { return titulo; }

    /**
     * Establece el título del libro.
     * @param titulo el título del libro.
     */
    public void setTitulo(String titulo) { this.titulo = titulo; }

    /**
     * Obtiene el autor del libro.
     * @return el autor del libro.
     */
    public String getAutor() { return autor; }

    /**
     * Establece el autor del libro.
     * @param autor el autor del libro.
     */
    public void setAutor(String autor) { this.autor = autor; }

    /**
     * Obtiene la categoría del libro.
     * @return la categoría del libro.
     */
    public String getCategoria() { return categoria; }

    /**
     * Establece la categoría del libro.
     * @param categoria la categoría del libro.
     */
    public void setCategoria(String categoria) { this.categoria = categoria; }

    /**
     * Obtiene el código ISBN del libro.
     * @return el código ISBN del libro.
     */
    public String getIsbn() { return isbn; }

    /**
     * Establece el código ISBN del libro.
     * @param isbn el código ISBN del libro.
     */
    public void setIsbn(String isbn) { this.isbn = isbn; }

    /**
     * Obtiene la editorial del libro.
     * @return la editorial del libro.
     */
    public String getEditorial() { return editorial; }

    /**
     * Establece la editorial del libro.
     * @param editorial la editorial del libro.
     */
    public void setEditorial(String editorial) { this.editorial = editorial; }

    /**
     * Obtiene el idioma del libro.
     * @return el idioma del libro.
     */
    public String getIdioma() { return idioma; }

    /**
     * Establece el idioma del libro.
     * @param idioma el idioma del libro.
     */
    public void setIdioma(String idioma) { this.idioma = idioma; }

    /**
     * Obtiene la lista de copias asociadas al libro.
     * @return la lista de copias del libro.
     */
    public List<CopiaLibro> getCopias() { return copias; }

    /**
     * Establece la lista de copias asociadas al libro.
     * @param copias la lista de copias del libro.
     */
    public void setCopias(List<CopiaLibro> copias) { this.copias = copias; }
    @Override
    public String toString() {
        return titulo + " - " + autor;
    }

}