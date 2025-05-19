package biblioteca.controladores;

import biblioteca.App;
import biblioteca.modelo.Miembro;
import biblioteca.modelo.Multa;
import biblioteca.modelo.EstadoMulta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador para la vista de gestión de multas.
 * Permite visualizar, filtrar y actualizar el estado de las multas pendientes de los miembros.
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
     * Columna que muestra los días de atraso de la multa.
     */
    @FXML
    private TableColumn<Multa, Integer> colDiasAtraso;

    /**
     * Columna que muestra el monto de la multa.
     */
    @FXML
    private TableColumn<Multa, Double> colMonto;

    /**
     * Columna que muestra el estado de la multa (PENDIENTE o PAGADA).
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
     * ComboBox para filtrar las multas por miembro.
     */
    @FXML
    private ComboBox<Miembro> comboMiembros;

    /**
     * Etiqueta que muestra el total de multas listadas.
     */
    @FXML
    private Label lblTotal;

    /**
     * Etiqueta que muestra el monto total de las multas listadas.
     */
    @FXML
    private Label lblMontoTotal;

    /**
     * Etiqueta que muestra el monto promedio de las multas listadas.
     */
    @FXML
    private Label lblPromedio;

    /**
     * Etiqueta que muestra el monto mayor de las multas listadas.
     */
    @FXML
    private Label lblMayor;

    /**
     * Lista observable que contiene las multas mostradas en la tabla.
     */
    private ObservableList<Multa> datos;

    /**
     * Inicializa la vista configurando las columnas, cargando las multas pendientes y los miembros,
     * y mostrando las estadísticas de las multas.
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

        colDiasAtraso.setSortable(true);
        colMonto.setSortable(true);

        List<Multa> multasPendientes = App.getServicio().buscarMultasPendientes();
        datos = FXCollections.observableArrayList(multasPendientes);
        tablaMultas.setItems(datos);

        List<Miembro> miembros = App.getServicio().buscarTodosMiembros();
        comboMiembros.setItems(FXCollections.observableArrayList(miembros));
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

        actualizarEstadisticas();
    }

    /**
     * Marca la multa seleccionada como pagada y actualiza la tabla y las estadísticas.
     * Si no hay una multa pendiente seleccionada, muestra una advertencia.
     */
    @FXML
    private void pagarMulta() {
        Multa seleccionada = tablaMultas.getSelectionModel().getSelectedItem();

        if (seleccionada != null && seleccionada.getEstado() == EstadoMulta.PENDIENTE) {
            seleccionada.setEstado(EstadoMulta.PAGADA);
            App.getServicio().guardarMulta(seleccionada);
            tablaMultas.refresh();
            actualizarEstadisticas();

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

    /**
     * Filtra las multas mostradas en la tabla según el miembro seleccionado en el ComboBox.
     * Si no hay miembro seleccionado, no realiza ningún filtrado.
     */
    @FXML
    private void filtrarPorMiembro() {
        Miembro seleccionado = comboMiembros.getValue();
        if (seleccionado != null) {
            List<Multa> todas = App.getServicio().buscarMultasPendientes();
            List<Multa> filtradas = todas.stream()
                    .filter(m -> m.getPrestamo().getMiembro().equals(seleccionado))
                    .collect(Collectors.toList());
            datos.setAll(filtradas);
            actualizarEstadisticas();
        }
    }

    /**
     * Muestra todas las multas pendientes en la tabla y limpia la selección del ComboBox de miembros.
     */
    @FXML
    private void verTodasLasMultas() {
        List<Multa> todas = App.getServicio().buscarMultasPendientes();
        datos.setAll(todas);
        comboMiembros.getSelectionModel().clearSelection();
        actualizarEstadisticas();
    }

    /**
     * Actualiza las estadísticas de las multas mostradas en la tabla:
     * total de multas, monto total, promedio y monto mayor.
     */
    private void actualizarEstadisticas() {
        int total = datos.size();
        double suma = datos.stream().mapToDouble(Multa::getMonto).sum();
        double promedio = total > 0 ? suma / total : 0;
        double mayor = datos.stream().mapToDouble(Multa::getMonto).max().orElse(0);

        lblTotal.setText("Total: " + total);
        lblMontoTotal.setText("Monto total: $" + suma);
        lblPromedio.setText("Promedio: $" + String.format("%.2f", promedio));
        lblMayor.setText("Mayor: $" + mayor);
    }
}