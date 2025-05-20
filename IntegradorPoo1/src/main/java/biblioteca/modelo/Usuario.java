package biblioteca.modelo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Representa un usuario genérico en la biblioteca.
 * Esta clase es abstracta y sirve como base para diferentes tipos de usuarios.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Usuario implements Serializable {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del usuario.
     */
    private String nombre;

    /**
     * Apellido del usuario.
     */
    private String apellido;

    /**
     * Contraseña del usuario.
     */
    private String clave;

    /**
     * Estado actual del usuario (por ejemplo, ALTA, BAJA, etc.).
     */
    @Enumerated(EnumType.STRING)
    private EstadoUsuario estado;

    /**
     * Rol del usuario (por ejemplo, BIBLIOTECARIO o MIEMBRO).
     */
    private String rol;

    /**
     * Constructor por defecto.
     */
    public Usuario() {}

    /**
     * Constructor con parámetros para inicializar un usuario.
     *
     * @param nombre Nombre del usuario.
     * @param apellido Apellido del usuario.
     * @param clave Contraseña del usuario.
     * @param estado Estado actual del usuario.
     * @param rol Rol del usuario.
     */
    public Usuario(String nombre, String apellido, String clave, EstadoUsuario estado, String rol) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.clave = clave;
        this.estado = estado;
        this.rol = rol;
    }

    /**
     * Busca libros por título.
     *
     * @param titulo Título del libro a buscar.
     * @return Lista de libros que coinciden con el título.
     */
    public List<Libro> buscarLibro(String titulo) {
        return null;
    }

    /**
     * Busca libros por autor.
     *
     * @param autor Autor del libro a buscar.
     * @return Lista de libros que coinciden con el autor.
     */
    public List<Libro> buscarLibroPorAutor(String autor) {
        return null;
    }

    /**
     * Busca libros por categoría.
     *
     * @param categoria Categoría del libro a buscar.
     * @return Lista de libros que coinciden con la categoría.
     */
    public List<Libro> buscarLibroPorCategoria(String categoria) {
        return null;
    }

    /**
     * Obtiene el identificador único del usuario.
     *
     * @return el identificador del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     *
     * @param id el identificador del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del usuario.
     *
     * @return el nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del usuario.
     *
     * @param nombre el nombre del usuario.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del usuario.
     *
     * @return el apellido del usuario.
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del usuario.
     *
     * @param apellido el apellido del usuario.
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene la contraseña del usuario.
     *
     * @return la contraseña del usuario.
     */
    public String getClave() {
        return clave;
    }

    /**
     * Establece la contraseña del usuario.
     *
     * @param clave la contraseña del usuario.
     */
    public void setClave(String clave) {
        this.clave = clave;
    }

    /**
     * Obtiene el estado actual del usuario.
     *
     * @return el estado del usuario.
     */
    public EstadoUsuario getEstado() {
        return estado;
    }

    /**
     * Establece el estado actual del usuario.
     *
     * @param estado el estado del usuario.
     */
    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el rol del usuario.
     *
     * @return el rol del usuario.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     *
     * @param rol el rol del usuario.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }
}
