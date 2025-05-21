package biblioteca.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Representa un libro en la biblioteca.
 * Un libro contiene información básica como título, autor, categoría, ISBN,
 * editorial e idioma.
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
     * Precio estimado del libro para cálculo de multas.
     */
    private double precioEstimado;

    /**
     * Constructor por defecto.
     */
    public Libro() {
    }

    // Getters y setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public List<CopiaLibro> getCopias() {
        return copias;
    }

    public void setCopias(List<CopiaLibro> copias) {
        this.copias = copias;
    }

    public double getPrecioEstimado() {
        return precioEstimado;
    }

    public void setPrecioEstimado(double precioEstimado) {
        this.precioEstimado = precioEstimado;
    }

    @Override
    public String toString() {
        return titulo + " - " + autor;
    }
}
