package biblioteca.servicios;

import biblioteca.modelo.*;
import biblioteca.repositorios.Repositorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Servicio {
    private final Repositorio repo;

    public Servicio(Repositorio repo) {
        this.repo = repo;
    }

    public Repositorio getRepositorio() {
        return repo;
    }

    public void agregarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.insertar(libro);
        repo.confirmarTransaccion();
    }

    public List<Libro> buscarTodosLibros() {
        return repo.buscarTodos(Libro.class);
    }

    public void eliminarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.eliminar(libro);
        repo.confirmarTransaccion();
    }

    public void modificarLibro(Libro libro) {
        repo.iniciarTransaccion();
        repo.modificar(libro);
        repo.confirmarTransaccion();
    }

    public List<Miembro> buscarTodosMiembros() {
        return repo.buscarTodos(Miembro.class);
    }

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
                        c -> c.getLibro().getId() + "-" + c.getTipo(),
                        c -> c,
                        (c1, c2) -> c1
                ))
                .values()
                .stream()
                .collect(Collectors.toList());
    }
    
    public List<CopiaLibro> buscarCopiasPorLibro(Libro libro) {
        return repo.buscarCopiasPorLibro(libro);
    }

    public long contarPrestamosActivos(Miembro miembro) {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        return prestamos.stream()
                .filter(p -> p.getMiembro().equals(miembro) && p.getFechaDevolucion() == null)
                .count();
    }

    public void realizarPrestamo(Prestamo prestamo) {
        repo.iniciarTransaccion();
        CopiaLibro copia = prestamo.getCopiaLibro();
        copia.setEstado(EstadoCopia.PRESTADA);
        repo.modificar(copia);
        repo.insertar(prestamo);
        repo.confirmarTransaccion();
    }

    public void actualizarMiembro(Miembro miembro) {
        repo.iniciarTransaccion();
        repo.modificar(miembro);
        repo.confirmarTransaccion();
    }

    public void generarMultas() {
        List<Prestamo> prestamos = repo.buscarTodos(Prestamo.class);
        LocalDate hoy = LocalDate.now();

        for (Prestamo p : prestamos) {
            if (p.getFechaDevolucion() == null && p.getFechaVencimiento().isBefore(hoy)) {
                if (p.getMulta() == null) {
                    int diasAtraso = (int) java.time.temporal.ChronoUnit.DAYS.between(p.getFechaVencimiento(), hoy);
                    double monto = diasAtraso * 100;

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

    public List<Multa> buscarMultasPendientes() {
        return repo.buscarTodos(Multa.class)
                .stream()
                .filter(m -> m.getEstado() == EstadoMulta.PENDIENTE)
                .collect(Collectors.toList());
    }

    public void guardarMulta(Multa multa) {
        repo.iniciarTransaccion();
        repo.modificar(multa);
        repo.confirmarTransaccion();
    }

    public List<Prestamo> buscarPrestamosSinDevolucion() {
        return repo.buscarTodos(Prestamo.class)
                .stream()
                .filter(p -> p.getFechaDevolucion() == null)
                .collect(Collectors.toList());
    }

    public void registrarDevolucion(Prestamo prestamo, CopiaLibro copia) {
        repo.iniciarTransaccion();
        repo.modificar(prestamo);
        repo.modificar(copia);
        repo.confirmarTransaccion();
    }

    public List<Libro> obtenerTodosLosLibros() {
        return repo.buscarTodos(Libro.class);
    }
}

