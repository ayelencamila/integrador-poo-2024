package biblioteca.controladores;

import biblioteca.controladores.VistaMiembroController;
import biblioteca.modelo.Usuario;
import biblioteca.modelo.EstadoUsuario;
import biblioteca.servicios.UsuarioServicio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Controlador para la pantalla de inicio de sesión (login) de la biblioteca.
 * Permite validar las credenciales del usuario y redirigirlo a la vista correspondiente según su rol.
 */
public class LoginController {

    @FXML private TextField campoUsuario;      // Campo para ingresar el ID de usuario
    @FXML private PasswordField campoClave;    // Campo para ingresar la clave
    @FXML private Label mensajeError;          // Label para mostrar mensajes de error

    private UsuarioServicio usuarioServicio = new UsuarioServicio(); // Servicio para validar usuarios

    /**
     * Maneja el evento de login al presionar el botón correspondiente.
     * Valida los campos, verifica las credenciales y redirige según el rol del usuario.
     * @param event Evento de acción generado por el botón de login.
     */
    @FXML
    public void handleLogin(ActionEvent event) {
        mensajeError.setText(""); // Limpia mensaje anterior

        String usuarioIngresado = campoUsuario.getText().trim();
        String clave = campoClave.getText();

        // Validación de campos vacíos
        if (usuarioIngresado.isEmpty() || clave.isEmpty()) {
            mensajeError.setText("Por favor, complete todos los campos.");
            return;
        }

        Long idUsuario;
        try {
            idUsuario = Long.parseLong(usuarioIngresado);
        } catch (NumberFormatException e) {
            mensajeError.setText("El ID debe ser numérico.");
            return;
        }

        Usuario usuario = usuarioServicio.validarLogin(idUsuario, clave);

        if (usuario != null && usuario.getEstado() == EstadoUsuario.ALTA) {
            try {
                Stage stage = (Stage) campoUsuario.getScene().getWindow();
                FXMLLoader loader;

                // Si el usuario es bibliotecario, carga la vista principal
                if ("BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
                    loader = new FXMLLoader(getClass().getResource("/biblioteca/principal.fxml"));
                    Parent root = loader.load();
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    // Si es miembro, carga la vista de miembro y pasa el usuario
                    loader = new FXMLLoader(getClass().getResource("/biblioteca/vistaMiembro.fxml"));
                    Parent root = loader.load();
                    VistaMiembroController controller = loader.getController();
                    controller.setUsuario(usuario);
                    stage.setScene(new Scene(root));
                    stage.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                mensajeError.setText("Error al cargar la vista.");
            }
        } else {
            mensajeError.setText("Usuario o clave incorrectos, o usuario inactivo.");
        }
    }
}