package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
import biblioteca.modelo.Prestamo;
import biblioteca.servicios.Servicio;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de realizar préstamos.
 * Permite al usuario registrar un nuevo préstamo de un libro a un miembro.
 */
public class RealizarPrestamoController {

    /**
     * ComboBox para seleccionar el miembro que realiza el préstamo.
     */
    @FXML
    private ComboBox<Miembro> comboMiembros;

    /**
     * ComboBox para seleccionar la copia del libro a prestar.
     */
    @FXML
    private ComboBox<CopiaLibro> comboCopias;

    /**
     * Selector de fecha para la fecha de inicio del préstamo.
     */
    @FXML
    private DatePicker fechaInicio;

    /**
     * Selector de fecha para la fecha de vencimiento del préstamo.
     */
    @FXML
    private DatePicker fechaVencimiento;

    /**
     * Servicio de lógica de negocio para operaciones de biblioteca.
     */
    private Servicio servicio;

    /**
     * Inicializa la vista cargando los datos necesarios y configurando los controles.
     */
    @FXML
    public void initialize() {
        servicio = App.getServicio();
        cargarMiembrosActivos();
        cargarCopiasDisponibles();
        configurarFechasPorDefecto();
    }

    /**
     * Carga los miembros activos en el ComboBox correspondiente.
     * Si no hay miembros activos, muestra una alerta.
     */
    private void cargarMiembrosActivos() {
        try {
            List<Miembro> miembros = servicio.buscarTodosMiembros()
                    .stream()
                    .filter(m -> m.getEstado() == EstadoUsuario.ALTA)
                    .collect(Collectors.toList());

            if (miembros.isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Sin miembros", "No hay miembros activos disponibles.");
                return;
            }

            comboMiembros.getItems().setAll(miembros);

            comboMiembros.setConverter(new StringConverter<Miembro>() {
                @Override
                public String toString(Miembro miembro) {
                    return miembro != null ? miembro.getNombre() + " " + miembro.getApellido() : "";
                }

                @Override
                public Miembro fromString(String string) {
                    return null;
                }
            });
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar los miembros activos.");
            e.printStackTrace();
        }
    }

    /**
     * Carga las copias de libros disponibles en el ComboBox correspondiente.
     * Si no hay copias disponibles, muestra una alerta.
     */
    private void cargarCopiasDisponibles() {
        try {
            List<CopiaLibro> copiasDisponibles = servicio.buscarCopiasDisponibles();
            comboCopias.getItems().setAll(copiasDisponibles);
            comboCopias.setConverter(new StringConverter<CopiaLibro>() {
                @Override
                public String toString(CopiaLibro copia) {
                    return (copia != null && copia.getLibro() != null)
                            ? copia.getLibro().getTitulo() + " (" + copia.getTipo() + ")"
                            : "Copia sin libro asociado";
                }

                @Override
                public CopiaLibro fromString(String string) {
                    return null;
                }
            });

            if (copiasDisponibles.isEmpty()) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No hay copias disponibles para préstamo.");
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al cargar las copias disponibles.");
            e.printStackTrace();
        }
    }

    /**
     * Configura las fechas por defecto para el préstamo (hoy y dentro de 7 días).
     */
    private void configurarFechasPorDefecto() {
        fechaInicio.setValue(LocalDate.now());
        fechaVencimiento.setValue(LocalDate.now().plusDays(7));
    }

    /**
     * Registra un nuevo préstamo si los datos son válidos.
     * Muestra mensajes de éxito o error según corresponda.
     */
    @FXML
    private void registrarPrestamo() {
        try {
            Miembro miembro = comboMiembros.getValue();
            CopiaLibro copia = comboCopias.getValue();
            LocalDate inicio = fechaInicio.getValue();
            LocalDate vencimiento = fechaVencimiento.getValue();

            if (!validarDatos(miembro, copia, inicio, vencimiento)) {
                return;
            }

            Prestamo prestamo = new Prestamo();
            prestamo.setMiembro(miembro);
            prestamo.setCopiaLibro(copia);
            prestamo.setFechaInicio(inicio);
            prestamo.setFechaVencimiento(vencimiento);

            servicio.realizarPrestamo(prestamo);

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado correctamente.");
            cerrarVentana();
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el préstamo.");
            e.printStackTrace();
        }
    }

    /**
     * Valida los datos ingresados antes de registrar el préstamo.
     * Verifica que los campos no estén vacíos, que las fechas sean válidas y que el miembro no supere el límite de préstamos activos.
     *
     * @param miembro     El miembro seleccionado.
     * @param copia       La copia seleccionada.
     * @param inicio      La fecha de inicio del préstamo.
     * @param vencimiento La fecha de vencimiento del préstamo.
     * @return true si los datos son válidos, false en caso contrario.
     */
    private boolean validarDatos(Miembro miembro, CopiaLibro copia, LocalDate inicio, LocalDate vencimiento) {
        if (miembro == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Seleccioná un miembro.");
            return false;
        }

        if (copia == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Datos incompletos", "Seleccioná una copia disponible.");
            return false;
        }

        if (!vencimiento.isAfter(inicio)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fecha inválida", "La fecha de vencimiento debe ser posterior a la de inicio.");
            return false;
        }

        long prestamosActivos = servicio.contarPrestamosActivos(miembro);
        if (prestamosActivos >= 5) {
            mostrarAlerta(Alert.AlertType.WARNING, "Límite alcanzado", "El miembro ya tiene 5 préstamos activos.");
            return false;
        }

        return true;
    }

    /**
     * Muestra una alerta al usuario con el tipo, título y mensaje especificados.
     *
     * @param tipo    Tipo de alerta (INFORMATION, WARNING, ERROR, etc.)
     * @param titulo  Título de la alerta.
     * @param mensaje Mensaje a mostrar en la alerta.
     */
    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Cierra la ventana actual del formulario de préstamo.
     * Si no se puede cerrar, muestra una alerta de error.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) comboMiembros.getScene().getWindow();
        if (stage != null) {
            stage.close();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cerrar la ventana.");
        }
    }
}