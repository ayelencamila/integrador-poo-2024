package biblioteca.modelo;

import javax.persistence.*;
import java.util.List;

/**
 * Representa un rack en la biblioteca.
 * Un rack es un lugar físico donde se almacenan copias de libros.
 */
@Entity
public class Rack {

    /**
     * Identificador único del rack.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Descripción del rack (por ejemplo, ubicación o características).
     */
    private String descripcion;

    /**
     * Lista de copias de libros almacenadas en este rack.
     */
    @OneToMany(mappedBy = "rack")
    private List<CopiaLibro> copias;

    /**
     * Constructor por defecto.
     */
    public Rack() {}

    /**
     * Obtiene el identificador único del rack.
     * 
     * @return el identificador del rack.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del rack.
     * 
     * @param id el identificador del rack.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene la descripción del rack.
     * 
     * @return la descripción del rack.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del rack.
     * 
     * @param descripcion la descripción del rack.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la lista de copias de libros almacenadas en este rack.
     * 
     * @return la lista de copias de libros.
     */
    public List<CopiaLibro> getCopias() {
        return copias;
    }

    /**
     * Establece la lista de copias de libros almacenadas en este rack.
     * 
     * @param copias la lista de copias de libros.
     */
    public void setCopias(List<CopiaLibro> copias) {
        this.copias = copias;
    }
    @Override
    public String toString() {
        return descripcion;
    }

}