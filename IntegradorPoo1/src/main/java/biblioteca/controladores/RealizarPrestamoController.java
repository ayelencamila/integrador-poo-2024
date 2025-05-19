package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.CopiaLibro;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.modelo.Miembro;
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

    @FXML
    private ComboBox<Miembro> comboMiembros;

    @FXML
    private ComboBox<CopiaLibro> comboCopias;

    @FXML
    private DatePicker fechaInicio;

    @FXML
    private DatePicker fechaVencimiento;

    private Servicio servicio;

    @FXML
    public void initialize() {
        servicio = App.getServicio();
        cargarMiembrosActivos();
        cargarCopiasDisponibles();
        configurarFechasPorDefecto();
    }

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

    private void configurarFechasPorDefecto() {
        fechaInicio.setValue(LocalDate.now());
        fechaVencimiento.setValue(LocalDate.now().plusDays(7));
    }

    @FXML
    private void registrarPrestamo() {
        try {
            Miembro miembro = comboMiembros.getValue();
            CopiaLibro copia = comboCopias.getValue();

            if (!validarDatos(miembro, copia, fechaInicio.getValue(), fechaVencimiento.getValue())) {
                return;
            }

            boolean exito = miembro.prestarLibro(copia);

            if (exito) {
                servicio.actualizarMiembro(miembro);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Préstamo registrado correctamente.");
                cerrarVentana();
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No se pudo realizar el préstamo", "Verificá las condiciones del miembro o la copia.");
            }

        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Ocurrió un error al registrar el préstamo.");
            e.printStackTrace();
        }
    }

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

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) comboMiembros.getScene().getWindow();
        if (stage != null) {
            stage.close();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo cerrar la ventana.");
        }
    }
}