package biblioteca.servicios;

import biblioteca.modelo.*;
import biblioteca.repositorios.Repositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio principal para la gestión de la biblioteca.
 * Provee métodos para manipular libros, miembros, copias, préstamos, racks y multas.
 */
public class Servicio {
    private final Repositorio repo;

    /**
     * Constructor del servicio.
     * @param repo Repositorio a utilizar para las operaciones de persistencia.
     */
    public Servicio(Repositorio repo) {
        this.repo = repo;
    }

    /**
     * Obtiene el repositorio asociado.
     * @return Repositorio utilizado por el servicio.
     */
    public Repositorio getRepositorio() {
        return repo;
    }

    // ========================= LIBROS =========================

    /**
     * Agrega un libro a la base de datos.
     * @param libro Libro a agregar.
     */
    public void agregarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.insertar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Busca y retorna todos los libros.
     * @return Lista de libros.
     */
    public List<Libro> buscarTodosLibros() {
        return repo.buscarTodos(Libro.class);
    }

    /**
     * Elimina un libro de la base de datos.
     * @param libro Libro a eliminar.
     */
    public void eliminarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.eliminar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Modifica los datos de un libro existente.
     * @param libro Libro a modificar.
     */
    public void modificarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.modificar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Obtiene todos los libros (alias de buscarTodosLibros).
     * @return Lista de libros.
     */
    public List<Libro> obtenerTodosLosLibros() {
        return buscarTodosLibros();
    }

    // ========================= MIEMBROS =========================

    /**
     * Busca y retorna todos los miembros.
     * @return Lista de miembros.
     */
    public List<Miembro> buscarTodosMiembros() {
        return repo.buscarTodos(Miembro.class);
    }

    /**
     * Busca un miembro por su ID.
     * @param id ID del miembro.
     * @return Miembro encontrado o null si no existe.
     */
    public Miembro buscarMiembroPorId(Long id) {
        return repo.buscarPorId(Miembro.class, id);
    }

    /**
     * Actualiza los datos de un miembro.
     * @param miembro Miembro a modificar.
     */
    public void actualizarMiembro(Miembro miembro) {
        repo.iniciarTransaccion();
        repo.modificar(miembro);
        repo.confirmarTransaccion();
    }

    // ========================= COPIAS DE LIBRO =========================

    /**
     * Busca todas las copias disponibles de libros.
     * @return Lista de copias disponibles.
     */
    public List<CopiaLibro> buscarCopiasDisponibles() {
        try {
            return repo.buscarCopiasDisponiblesConLibros();
        } catch (Exception e) {
            // Manejo de error: retorna lista vacía si ocurre un error
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene una copia disponible por cada libro y tipo.
     * @return Lista de copias únicas por libro y tipo.
     */
    public List<CopiaLibro> obtenerUnaCopiaPorLibroYTipo() {
        List<CopiaLibro> disponibles = buscarCopiasDisponibles();
        return disponibles.stream()
                .collect(Collectors.toMap(
                        c -> c.getLibro().getId() + "-" + c.getTipo(),
                        c -> c,
                        (c1, c2) -> c1))
                .values()
                .stream()
                .collect(Collectors.toList());
    }

    /**
     * Busca todas las copias de un libro específico.
     * @param libro Libro a buscar.
     * @return Lista de copias del libro.
     */
    public List<CopiaLibro> buscarCopiasPorLibro(Libro libro) {
        return repo.buscarCopiasPorLibro(libro);
    }

    // ========================= PRÉSTAMOS =========================

    /**
     * Cuenta la cantidad de préstamos activos de un miembro.
     * @param miembro Miembro a consultar.
     * @return Cantidad de préstamos activos.
     */
    public long contarPrestamosActivos(Miembro miembro) {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        return prestamos.stream()
                .filter(p -> p.getMiembro().equals(miembro) && p.getFechaDevolucion() == null)
                .count();
    }

    /**
     * Realiza un préstamo y actualiza el estado de la copia.
     * @param prestamo Préstamo a registrar.
     */
    public void realizarPrestamo(Prestamo prestamo) {
        repo.iniciarTransaccion();
        CopiaLibro copia = prestamo.getCopiaLibro();
        copia.setEstado(EstadoCopia.PRESTADA);
        repo.modificar(copia);
        repo.insertar(prestamo);
        repo.confirmarTransaccion();
    }

    /**
     * Busca todos los préstamos sin devolución.
     * @return Lista de préstamos sin devolución.
     */
    public List<Prestamo> buscarPrestamosSinDevolucion() {
        return repo.buscarTodos(Prestamo.class)
                .stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }

    /**
     * Registra la devolución de un préstamo y actualiza la copia.
     * @param prestamo Préstamo devuelto.
     * @param copia Copia devuelta.
     */
    public void registrarDevolucion(Prestamo prestamo, CopiaLibro copia) {
        repo.iniciarTransaccion();
        repo.modificar(prestamo);
        repo.modificar(copia);
        repo.confirmarTransaccion();
    }

    // ========================= MULTAS =========================

    /**
     * Genera multas para préstamos vencidos sin devolución y sin multa previa.
     * Si el libro no tiene precio estimado, se usa 100.0 por defecto.
     */
    public void generarMultas() {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        LocalDate hoy = LocalDate.now();

        for (Prestamo p : prestamos) {
            if (p.getFechaDevolucion() == null && p.getMulta() == null) {
                int diasAtraso = (int) java.time.temporal.ChronoUnit.DAYS.between(p.getFechaVencimiento(), hoy);
                if (diasAtraso > 0) {
                    Double precioEstimado = Optional.ofNullable(
                        p.getCopiaLibro().getLibro().getPrecioEstimado()
                    ).orElse(100.0);

                    double monto = diasAtraso * precioEstimado;

                    Multa multa = new Multa();
                    multa.setDiasAtraso(diasAtraso);
                    multa.setMonto(monto);
                    multa.setEstado(EstadoMulta.PENDIENTE);
                    multa.setPrestamo(p);

                    p.setMulta(multa);
                    repo.iniciarTransaccion();
                    repo.persistir(multa);
                    repo.confirmarTransaccion();
                }
            }
        }
    }

    /**
     * Busca todas las multas pendientes.
     * @return Lista de multas pendientes.
     */
    public List<Multa> buscarMultasPendientes() {
        return repo.buscarTodos(Multa.class)
                .stream()
                .filter(m -> m.getEstado() == EstadoMulta.PENDIENTE)
                .collect(Collectors.toList());
    }

    /**
     * Guarda o actualiza una multa.
     * @param multa Multa a guardar.
     */
    public void guardarMulta(Multa multa) {
        repo.iniciarTransaccion();
        repo.modificar(multa);
        repo.confirmarTransaccion();
    }

    // ========================= RACKS =========================

    /**
     * Busca todos los racks.
     * @return Lista de racks.
     */
    public List<Rack> buscarTodosRacks() {
        return repo.buscarTodos(Rack.class);
    }

    /**
     * Agrega un rack a la base de datos.
     * @param rack Rack a agregar.
     */
    public void agregarRack(Rack rack) {
        repo.iniciarTransaccion();
        repo.insertar(rack);
        repo.confirmarTransaccion();
    }

    /**
     * Modifica los datos de un rack existente.
     * @param rack Rack a modificar.
     */
    public void modificarRack(Rack rack) {
        repo.iniciarTransaccion();
        repo.modificar(rack);
        repo.confirmarTransaccion();
    }

    /**
     * Elimina un rack de la base de datos.
     * @param rack Rack a eliminar.
     */
    public void eliminarRack(Rack rack) {
        repo.iniciarTransaccion();
        repo.eliminar(rack);
        repo.confirmarTransaccion();
    }
}