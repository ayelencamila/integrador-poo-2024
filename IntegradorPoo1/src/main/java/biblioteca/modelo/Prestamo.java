package biblioteca.modelo;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Representa un préstamo en la biblioteca.
 * Un préstamo está asociado a un miembro, una copia de un libro y puede incluir una multa si hay retraso en la devolución.
 */
@Entity
public class Prestamo {

    /**
     * Identificador único del préstamo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Fecha en la que se inició el préstamo.
     */
    private LocalDate fechaInicio;

    /**
     * Fecha en la que el préstamo debe ser devuelto.
     */
    private LocalDate fechaVencimiento;

    /**
     * Fecha en la que se devolvió el préstamo.
     */
    private LocalDate fechaDevolucion;

    /**
     * Miembro que realizó el préstamo.
     */
    @ManyToOne
    private Miembro miembro;

    /**
     * Copia del libro asociada al préstamo.
     */
    @ManyToOne
    private CopiaLibro copiaLibro;

    /**
     * Multa asociada al préstamo, si aplica.
     */
    @OneToOne(mappedBy = "prestamo", cascade = CascadeType.ALL)
    private Multa multa;

    /**
     * Constructor por defecto.
     */
    public Prestamo() {}

    // Getters y setters

    /**
     * Obtiene el identificador único del préstamo.
     * @return el identificador del préstamo.
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único del préstamo.
     * @param id el identificador del préstamo.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene la fecha de inicio del préstamo.
     * @return la fecha de inicio.
     */
    public LocalDate getFechaInicio() { return fechaInicio; }

    /**
     * Establece la fecha de inicio del préstamo.
     * @param fechaInicio la fecha de inicio.
     */
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    /**
     * Obtiene la fecha de vencimiento del préstamo.
     * @return la fecha de vencimiento.
     */
    public LocalDate getFechaVencimiento() { return fechaVencimiento; }

    /**
     * Establece la fecha de vencimiento del préstamo.
     * @param fechaVencimiento la fecha de vencimiento.
     */
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    /**
     * Obtiene la fecha de devolución del préstamo.
     * @return la fecha de devolución.
     */
    public LocalDate getFechaDevolucion() { return fechaDevolucion; }

    /**
     * Establece la fecha de devolución del préstamo.
     * @param fechaDevolucion la fecha de devolución.
     */
    public void setFechaDevolucion(LocalDate fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    /**
     * Obtiene el miembro que realizó el préstamo.
     * @return el miembro asociado.
     */
    public Miembro getMiembro() { return miembro; }

    /**
     * Establece el miembro que realizó el préstamo.
     * @param miembro el miembro asociado.
     */
    public void setMiembro(Miembro miembro) { this.miembro = miembro; }

    /**
     * Obtiene la copia del libro asociada al préstamo.
     * @return la copia del libro.
     */
    public CopiaLibro getCopiaLibro() { return copiaLibro; }

    /**
     * Establece la copia del libro asociada al préstamo.
     * @param copiaLibro la copia del libro.
     */
    public void setCopiaLibro(CopiaLibro copiaLibro) { this.copiaLibro = copiaLibro; }

    /**
     * Obtiene la multa asociada al préstamo, si aplica.
     * @return la multa asociada.
     */
    public Multa getMulta() { return multa; }

    /**
     * Establece la multa asociada al préstamo.
     * @param multa la multa asociada.
     */
    public void setMulta(Multa multa) { this.multa = multa; }
}



