package biblioteca.modelo;

import javax.persistence.*;
import java.util.List;

/**
 * Representa una copia de un libro en la biblioteca.
 * Cada copia puede tener un estado, un tipo, y puede estar asociada a un libro, un rack y un préstamo.
 */
@Entity
public class CopiaLibro {

    /**
     * Identificador único de la copia del libro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Estado actual de la copia del libro (por ejemplo, DISPONIBLE, PRESTADO, etc.).
     */
    @Enumerated(EnumType.STRING)
    private EstadoCopia estado;

    /**
     * Tipo de la copia del libro (por ejemplo, ELECTRONICA, etc.).
     */
    @Enumerated(EnumType.STRING)
    private TipoCopia tipo;

    /**
     * Indica si la copia es de referencia (no puede ser prestada).
     */
    private boolean esReferencia;

    /**
     * Libro al que pertenece esta copia.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "libro_id")
    private Libro libro;


    /**
     * Rack donde se encuentra físicamente esta copia.
     */
    @ManyToOne
    private Rack rack;

    /**
     * Préstamo asociado a esta copia, si está prestada.
     */
    @OneToMany(mappedBy = "copiaLibro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Prestamo> prestamos;

    /**
     * Constructor por defecto.
     */
    public CopiaLibro() {}

    // Getters y setters

    /**
     * Obtiene el identificador único de la copia.
     * @return el identificador de la copia.
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único de la copia.
     * @param id el identificador de la copia.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el estado actual de la copia.
     * @return el estado de la copia.
     */
    public EstadoCopia getEstado() { return estado; }

    /**
     * Establece el estado actual de la copia.
     * @param estado el estado de la copia.
     */
    public void setEstado(EstadoCopia estado) { this.estado = estado; }

    /**
     * Obtiene el tipo de la copia.
     * @return el tipo de la copia.
     */
    public TipoCopia getTipo() { return tipo; }

    /**
     * Establece el tipo de la copia.
     * @param tipo el tipo de la copia.
     */
    public void setTipo(TipoCopia tipo) { this.tipo = tipo; }

    /**
     * Verifica si la copia es de referencia.
     * @return true si es de referencia, false en caso contrario.
     */
    public boolean isEsReferencia() { return esReferencia; }

    /**
     * Establece si la copia es de referencia.
     * @param esReferencia true si es de referencia, false en caso contrario.
     */
    public void setEsReferencia(boolean esReferencia) { this.esReferencia = esReferencia; }

    /**
     * Obtiene el libro al que pertenece esta copia.
     * @return el libro asociado.
     */
    public Libro getLibro() { return libro; }

    /**
     * Establece el libro al que pertenece esta copia.
     * @param libro el libro asociado.
     */
    public void setLibro(Libro libro) { this.libro = libro; }

    /**
     * Obtiene el rack donde se encuentra esta copia.
     * @return el rack asociado.
     */
    public Rack getRack() { return rack; }

    /**
     * Establece el rack donde se encuentra esta copia.
     * @param rack el rack asociado.
     */
    public void setRack(Rack rack) { this.rack = rack; }

    public List<Prestamo> getPrestamos() { return prestamos; }

    public void setPrestamos(List<Prestamo> prestamos) { this.prestamos = prestamos; }
}
