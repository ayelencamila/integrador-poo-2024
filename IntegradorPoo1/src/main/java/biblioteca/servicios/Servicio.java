package biblioteca.servicios;

import biblioteca.modelo.Libro;
import biblioteca.repositorios.Repositorio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import biblioteca.modelo.Miembro;
import biblioteca.modelo.Multa;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.Prestamo;
import biblioteca.modelo.EstadoCopia;
import biblioteca.modelo.EstadoMulta;
import java.util.stream.Collectors;

/**
 * Clase que representa los servicios principales de la biblioteca.
 * Proporciona métodos para gestionar libros, préstamos, copias y multas.
 */
public class Servicio {
    private final Repositorio repo;

    /**
     * Constructor que inicializa el servicio con un repositorio.
     * @param repo El repositorio utilizado para interactuar con la base de datos.
     */
    public Servicio(Repositorio repo) {
        this.repo = repo;
    }

    /**
     * Agrega un nuevo libro al sistema.
     * @param libro El libro que se desea agregar.
     */
    public void agregarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.insertar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Obtiene el repositorio asociado al servicio.
     * @return El repositorio.
     */
    public Repositorio getRepositorio() {
        return repo;
    }

    /**
     * Busca todos los libros registrados en el sistema.
     * @return Una lista de libros.
     */
    public List<Libro> buscarTodosLibros() {
        return repo.buscarTodos(Libro.class);
    }

    /**
     * Elimina un libro del sistema.
     * @param libro El libro que se desea eliminar.
     */
    public void eliminarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.eliminar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Modifica la información de un libro existente.
     * @param libro El libro que se desea modificar.
     */
    public void modificarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.modificar(libro);
        repo.confirmarTransaccion();
    }

    /**
     * Busca todos los miembros registrados en el sistema.
     * @return Una lista de miembros.
     */
    public List<Miembro> buscarTodosMiembros() {
        return repo.buscarTodos(Miembro.class);
    }

    /**
     * Busca todas las copias de libros disponibles.
     * @return Una lista de copias disponibles.
     */
    public List<CopiaLibro> buscarCopiasDisponibles() {
    try {
        List<CopiaLibro> copias = repo.buscarCopiasDisponiblesConLibros();
        if (copias.isEmpty()) {
            System.out.println("⚠️ No hay copias disponibles.");
        } else {
            System.out.println("✅ Se encontraron copias disponibles: " + copias.size());
        }
        return copias;
    } catch (Exception e) {
        System.err.println("❌ Error al buscar copias: " + e.getMessage());
        return new ArrayList<>();
    }
    }
    public List<CopiaLibro> obtenerUnaCopiaPorLibroYTipo() {
    List<CopiaLibro> disponibles = buscarCopiasDisponibles();

    return disponibles.stream()
        .collect(Collectors.toMap(
            c -> c.getLibro().getId() + "-" + c.getTipo(), // clave única
            c -> c,
            (c1, c2) -> c1 // quedate con uno
        ))
        .values()
        .stream()
        .collect(Collectors.toList());
    }
  

    /**
     * Cuenta los préstamos activos de un miembro.
     * @param miembro El miembro cuyos préstamos se desean contar.
     * @return El número de préstamos activos.
     */
    public long contarPrestamosActivos(Miembro miembro) {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        return prestamos.stream()
                .filter(p -> p.getMiembro().equals(miembro) && p.getFechaDevolucion() == null)
                .count();
    }

    /**
     * Realiza un préstamo de un libro.
     * @param prestamo El préstamo que se desea realizar.
     */
    public void realizarPrestamo(Prestamo prestamo) {
        repo.iniciarTransaccion();

        // Cambiar estado de la copia
        CopiaLibro copia = prestamo.getCopiaLibro();
        copia.setEstado(EstadoCopia.PRESTADA);
        repo.modificar(copia);

        // Guardar el préstamo
        repo.insertar(prestamo);

        repo.confirmarTransaccion();
    }

    /**
     * Genera multas para los préstamos vencidos.
     */
    public void generarMultas() {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        LocalDate hoy = LocalDate.now();

        for (Prestamo p : prestamos) {
            if (p.getFechaDevolucion() == null && p.getFechaVencimiento().isBefore(hoy)) {
                // Verificamos si ya existe una multa para ese préstamo
                if (p.getMulta() == null) {
                    int diasAtraso = (int) p.getFechaVencimiento().until(hoy).toTotalMonths() * 30 + p.getFechaVencimiento().until(hoy).getDays();
                    double monto = diasAtraso * 100; // Por ejemplo, $100 por día

                    Multa multa = new Multa();
                    multa.setDiasAtraso(diasAtraso);
                    multa.setMonto(monto);
                    multa.setEstado(EstadoMulta.PENDIENTE);
                    multa.setPrestamo(p);

                    repo.iniciarTransaccion();
                    repo.insertar(multa);
                    repo.confirmarTransaccion();
                }
            }
        }
    }

    /**
     * Busca todas las multas pendientes de pago.
     * @return Una lista de multas pendientes.
     */
    public List<Multa> buscarMultasPendientes() {
        return repo.buscarTodos(Multa.class)
                .stream()
                .filter(m -> m.getEstado() == EstadoMulta.PENDIENTE)
                .collect(Collectors.toList());
    }

    /**
     * Guarda los cambios realizados en una multa.
     * @param multa La multa que se desea guardar.
     */
    public void guardarMulta(Multa multa) {
        repo.iniciarTransaccion();
        repo.modificar(multa);
        repo.confirmarTransaccion();
    }

    /**
     * Busca todos los préstamos que no han sido devueltos.
     * @return Una lista de préstamos sin devolución.
     */
    public List<Prestamo> buscarPrestamosSinDevolucion() {
        return repo.buscarTodos(Prestamo.class)
                   .stream()
                   .filter(p -> p.getFechaDevolucion() == null)
                   .collect(Collectors.toList());
    }

    /**
     * Registra la devolución de un préstamo y actualiza el estado de la copia.
     * @param prestamo El préstamo que se devuelve.
     * @param copia La copia del libro que se devuelve.
     */
    public void registrarDevolucion(Prestamo prestamo, CopiaLibro copia) {
        repo.iniciarTransaccion();

        // Actualizar el préstamo y la copia
        repo.modificar(prestamo);
        repo.modificar(copia);

        repo.confirmarTransaccion();
    }
}
