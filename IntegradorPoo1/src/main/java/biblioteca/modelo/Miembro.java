package biblioteca.modelo;

import javax.persistence.*;
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
    @OneToMany(mappedBy = "miembro")
    private List<Prestamo> librosPrestados;

    /**
     * Constructor por defecto.
     */
    public Miembro() {}

    /**
     * Realiza el préstamo de una copia de un libro.
     * 
     * @param copia la copia del libro a prestar.
     * @return true si el préstamo se realizó con éxito, false en caso contrario.
     */
    public boolean prestarLibro(CopiaLibro copia) {
        // Aquí iría la lógica de préstamo
        return true;
    }

    /**
     * Realiza la devolución de una copia de un libro.
     * 
     * @param copia la copia del libro a devolver.
     * @return true si la devolución se realizó con éxito, false en caso contrario.
     */
    public boolean devolverLibro(CopiaLibro copia) {
        // Aquí iría la lógica de devolución
        return true;
    }

    /**
     * Obtiene la lista de libros prestados por el miembro.
     * 
     * @return la lista de préstamos asociados al miembro.
     */
    public List<Prestamo> getLibrosPrestados() {
        return librosPrestados;
    }

    /**
     * Establece la lista de libros prestados por el miembro.
     * 
     * @param librosPrestados la lista de préstamos asociados al miembro.
     */
    public void setLibrosPrestados(List<Prestamo> librosPrestados) {
        this.librosPrestados = librosPrestados;
    }
}