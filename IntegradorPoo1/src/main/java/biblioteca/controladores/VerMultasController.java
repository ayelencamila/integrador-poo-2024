package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Multa;
import biblioteca.modelo.EstadoMulta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;

/**
 * Controlador para la vista de gestión de multas.
 * Permite al usuario visualizar las multas pendientes y marcarlas como pagadas.
 */
public class VerMultasController {

    /**
     * Tabla que muestra las multas pendientes.
     */
    @FXML
    private TableView<Multa> tablaMultas;

    /**
     * Columna que muestra el ID de la multa.
     */
    @FXML
    private TableColumn<Multa, Long> colId;

    /**
     * Columna que muestra los días de atraso.
     */
    @FXML
    private TableColumn<Multa, Integer> colDiasAtraso;

    /**
     * Columna que muestra el monto de la multa.
     */
    @FXML
    private TableColumn<Multa, Double> colMonto;

    /**
     * Columna que muestra el estado de la multa.
     */
    @FXML
    private TableColumn<Multa, EstadoMulta> colEstado;

    /**
     * Columna que muestra el nombre del miembro asociado a la multa.
     */
    @FXML
    private TableColumn<Multa, String> colMiembro;

    /**
     * Columna que muestra el título del libro asociado a la multa.
     */
    @FXML
    private TableColumn<Multa, String> colLibro;

    /**
     * Lista observable que contiene las multas mostradas en la tabla.
     */
    private ObservableList<Multa> datos;

    /**
     * Inicializa la vista configurando las columnas de la tabla y cargando las multas pendientes desde el sistema.
     */
    @FXML
    public void initialize() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getId()));
        colDiasAtraso.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getDiasAtraso()));
        colMonto.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getMonto()));
        colEstado.setCellValueFactory(cell -> new javafx.beans.property.SimpleObjectProperty<>(cell.getValue().getEstado()));

        colMiembro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getPrestamo().getMiembro().getApellido() + ", " + cell.getValue().getPrestamo().getMiembro().getNombre()
        ));

        colLibro.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(
            cell.getValue().getPrestamo().getCopiaLibro().getLibro().getTitulo()
        ));

        List<Multa> multasPendientes = App.getServicio().buscarMultasPendientes();
        datos = FXCollections.observableArrayList(multasPendientes);
        tablaMultas.setItems(datos);
    }

    /**
     * Marca la multa seleccionada como pagada y actualiza la tabla.
     * Si no hay una multa pendiente seleccionada, muestra una advertencia.
     */
    @FXML
    private void pagarMulta() {
        Multa seleccionada = tablaMultas.getSelectionModel().getSelectedItem();

        if (seleccionada != null && seleccionada.getEstado() == EstadoMulta.PENDIENTE) {
            seleccionada.setEstado(EstadoMulta.PAGADA);
            App.getServicio().guardarMulta(seleccionada);
            tablaMultas.refresh();

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Multa actualizada");
            alerta.setHeaderText(null);
            alerta.setContentText("La multa fue marcada como PAGADA.");
            alerta.showAndWait();
        } else {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Sin selección");
            alerta.setHeaderText(null);
            alerta.setContentText("Seleccioná una multa pendiente.");
            alerta.showAndWait();
        }
    }
}