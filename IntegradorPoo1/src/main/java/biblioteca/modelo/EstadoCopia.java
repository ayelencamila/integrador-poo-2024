package biblioteca.modelo;

/**
 * Enumeración que representa los posibles estados de una copia de libro en el sistema de biblioteca.
 * 
 * DISPONIBLE: La copia está disponible para ser prestada.
 * PRESTADA: La copia se encuentra actualmente prestada a un usuario.
 * PERDIDA: La copia ha sido marcada como perdida.
 */
public enum EstadoCopia {
    DISPONIBLE,
    PRESTADA,
    PERDIDA
}