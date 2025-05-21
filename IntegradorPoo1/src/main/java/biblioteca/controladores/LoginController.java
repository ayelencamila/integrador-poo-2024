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

public class LoginController {

    @FXML private TextField campoUsuario;
    @FXML private PasswordField campoClave;
    @FXML private Label mensajeError;

    private UsuarioServicio usuarioServicio = new UsuarioServicio();

    @FXML
    public void handleLogin(ActionEvent event) {
        mensajeError.setText(""); // Limpia mensaje anterior

        String usuarioIngresado = campoUsuario.getText().trim();
        String clave = campoClave.getText();

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

                if ("BIBLIOTECARIO".equalsIgnoreCase(usuario.getRol())) {
                    loader = new FXMLLoader(getClass().getResource("/biblioteca/principal.fxml"));
                    Parent root = loader.load();  // Cargamos vista
                    stage.setScene(new Scene(root));
                    stage.show();
                } else {
                    loader = new FXMLLoader(getClass().getResource("/biblioteca/vistaMiembro.fxml"));
                    Parent root = loader.load();  // Cargamos vista
                    // Solo aquí hacemos el cast
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