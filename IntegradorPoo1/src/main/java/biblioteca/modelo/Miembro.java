package biblioteca.modelo;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa un miembro de la biblioteca que extiende la funcionalidad de un usuario.
 * Un miembro puede realizar préstamos y devoluciones de libros.
 */
@Entity
public class Miembro extends Usuario {

    /**
     * Lista de préstamos asociados al miembro.
     */
    @OneToMany(mappedBy = "miembro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prestamo> librosPrestados;

    public Miembro() {}

    /**
     * Realiza el préstamo de una copia de un libro.
     * 
     * @param copia la copia del libro a prestar.
     * @return true si el préstamo se realizó con éxito, false en caso contrario.
     */
    public boolean prestarLibro(CopiaLibro copia) {
        if (this.getEstado() != EstadoUsuario.ALTA) {
            return false;
        }

        long prestamosActivos = librosPrestados.stream()
            .filter(p -> p.getFechaDevolucion() == null)
            .count();

        if (prestamosActivos >= 5) {
            return false;
        }

        if (copia.getEstado() != EstadoCopia.DISPONIBLE || copia.isEsReferencia()) {
            return false;
        }

        Prestamo prestamo = new Prestamo();
        prestamo.setMiembro(this);
        prestamo.setCopiaLibro(copia);
        prestamo.setFechaInicio(LocalDate.now());
        prestamo.setFechaVencimiento(LocalDate.now().plusDays(10));

        copia.setEstado(EstadoCopia.PRESTADA);

        librosPrestados.add(prestamo);
        return true;
    }

    /**
     * Realiza la devolución de una copia de un libro.
     * 
     * @param copia la copia del libro a devolver.
     * @return true si la devolución se realizó con éxito, false en caso contrario.
     */
    public boolean devolverLibro(CopiaLibro copia) {
        Prestamo prestamo = librosPrestados.stream()
            .filter(p -> p.getCopiaLibro().equals(copia) && p.getFechaDevolucion() == null)
            .findFirst()
            .orElse(null);

        if (prestamo == null) {
            return false;
        }

        LocalDate hoy = LocalDate.now();
        prestamo.setFechaDevolucion(hoy);

        copia.setEstado(EstadoCopia.DISPONIBLE);

        if (hoy.isAfter(prestamo.getFechaVencimiento())) {
            int diasAtraso = (int) java.time.temporal.ChronoUnit.DAYS.between(
                prestamo.getFechaVencimiento(), hoy);

            double monto = diasAtraso * 100.0;

            Multa multa = new Multa();
            multa.setDiasAtraso(diasAtraso);
            multa.setMonto(monto);
            multa.setEstado(EstadoMulta.PENDIENTE);
            multa.setPrestamo(prestamo);

            prestamo.setMulta(multa);
        }

        return true;
    }

    public List<Prestamo> getLibrosPrestados() {
        return librosPrestados;
    }

    public void setLibrosPrestados(List<Prestamo> librosPrestados) {
        this.librosPrestados = librosPrestados;
    }
}