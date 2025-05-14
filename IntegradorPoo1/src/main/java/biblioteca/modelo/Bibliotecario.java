package biblioteca.modelo;

import javax.persistence.*;

/**
 * Clase que representa a un bibliotecario en el sistema.
 * Un bibliotecario es un tipo de usuario que tiene permisos para gestionar libros y generar multas.
 */
@Entity
public class Bibliotecario extends Usuario {

    /**
     * Constructor por defecto.
     */
    public Bibliotecario() {}

    /**
     * Registra un nuevo libro en el sistema.
     * @param libro El libro que se desea registrar.
     */
    public void registrarLibro(Libro libro) {
        // lógica de registrar libro
    }

    /**
     * Modifica la información de un libro existente en el sistema.
     * @param libro El libro que se desea modificar.
     */
    public void modificarLibro(Libro libro) {
        // lógica de modificar libro
    }

    /**
     * Elimina un libro del sistema.
     * @param libro El libro que se desea eliminar.
     */
    public void eliminarLibro(Libro libro) {
        // lógica de eliminar libro
    }

    /**
     * Genera una multa asociada a un préstamo.
     * @param prestamo El préstamo que generó la multa.
     * @return La multa generada.
     */
    public Multa generarMulta(Prestamo prestamo) {
        // lógica de generar multa
        return null;
    }
}

