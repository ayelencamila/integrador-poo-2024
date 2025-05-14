package biblioteca.modelo;

import javax.persistence.*;

/**
 * Representa una multa asociada a un préstamo en la biblioteca.
 * Una multa se genera cuando un miembro devuelve un libro con retraso.
 */
@Entity
public class Multa {

    /**
     * Identificador único de la multa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Monto de la multa en unidades monetarias.
     */
    private double monto;

    /**
     * Número de días de retraso que generaron la multa.
     */
    private int diasAtraso;

    /**
     * Estado actual de la multa (por ejemplo, PENDIENTE, PAGADA, etc.).
     */
    @Enumerated(EnumType.STRING)
    private EstadoMulta estado;

    /**
     * Préstamo asociado a esta multa.
     */
    @OneToOne
    @JoinColumn(name = "prestamo_id")
    private Prestamo prestamo;

    /**
     * Constructor por defecto.
     */
    public Multa() {}

    // Getters y setters

    /**
     * Obtiene el identificador único de la multa.
     * @return el identificador de la multa.
     */
    public Long getId() { return id; }

    /**
     * Establece el identificador único de la multa.
     * @param id el identificador de la multa.
     */
    public void setId(Long id) { this.id = id; }

    /**
     * Obtiene el monto de la multa.
     * @return el monto de la multa.
     */
    public double getMonto() { return monto; }

    /**
     * Establece el monto de la multa.
     * @param monto el monto de la multa.
     */
    public void setMonto(double monto) { this.monto = monto; }

    /**
     * Obtiene el número de días de retraso que generaron la multa.
     * @return los días de retraso.
     */
    public int getDiasAtraso() { return diasAtraso; }

    /**
     * Establece el número de días de retraso que generaron la multa.
     * @param diasAtraso los días de retraso.
     */
    public void setDiasAtraso(int diasAtraso) { this.diasAtraso = diasAtraso; }

    /**
     * Obtiene el estado actual de la multa.
     * @return el estado de la multa.
     */
    public EstadoMulta getEstado() { return estado; }

    /**
     * Establece el estado actual de la multa.
     * @param estado el estado de la multa.
     */
    public void setEstado(EstadoMulta estado) { this.estado = estado; }

    /**
     * Obtiene el préstamo asociado a esta multa.
     * @return el préstamo asociado.
     */
    public Prestamo getPrestamo() { return prestamo; }

    /**
     * Establece el préstamo asociado a esta multa.
     * @param prestamo el préstamo asociado.
     */
    public void setPrestamo(Prestamo prestamo) { this.prestamo = prestamo; }
}