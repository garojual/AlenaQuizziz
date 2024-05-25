package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CrearPreguntaController implements Initializable {
    private DataBaseConnection databaseConnection;

    private static final String SQL_TEMAS = "SELECT NOMBRE_TEMA FROM tema";
    private static final String CALL_ADD_QUESTION = "{ call add_pregunta(?, ?, ?, ?, ?, ?, ?, ? }";


    private Map<String,String> seleccion =  new HashMap<>();


    @FXML
    private ComboBox<String> tipoPregunta;

    @FXML
    private ComboBox<String> temaPregunta;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            databaseConnection = new DataBaseConnection();
            Connection connection = databaseConnection.getConnection();
            ObservableList<String> temas = getTemasFromVista(connection);
            temaPregunta.setItems(temas);
            ObservableList<String> tiposPregunta = FXCollections.observableArrayList(
                    "Unica respuesta",
                    "Verdadero/Falso",
                    "Multiple respuesta",
                    "Emparejar",
                    "Ordenar",
                    "Completar"
            );
            tipoPregunta.setItems(tiposPregunta);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public CrearPreguntaController() {
    }

    private ObservableList<String> getTemasFromVista(Connection connection) throws SQLException {
        ObservableList<String> temas = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_TEMAS);
        while (resultSet.next()) {
            String tema = resultSet.getString("nombre_tema");
            temas.add(tema);
        }
        return temas;
    }

    @FXML
    private void onContinueButtonClicked(ActionEvent event) {
        String selectedTema = temaPregunta.getValue();
        String selectedTipoPregunta = tipoPregunta.getValue();

        if (selectedTema != null && selectedTipoPregunta != null) {
            // Guardar la información seleccionada en el Map
            seleccion.put("tema", selectedTema);
            seleccion.put("tipoPregunta", selectedTipoPregunta);

            // Cambiar a la siguiente pantalla
            try {
                switch (selectedTipoPregunta) {
                    case "Unica respuesta":
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_unica_respuesta.fxml"));
                            Parent root = loader.load();

                            // Obtener el controlador de la nueva pantalla y pasarle el Map de selección
                            CrearPreguntaUnicaRespuesta controller = loader.getController();
                            controller.setSeleccion(seleccion);

                            // Mostrar la nueva pantalla
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case "Verdadero/Falso":
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_v_f.fxml"));
                            Parent root = loader.load();

                            // Obtener el controlador de la nueva pantalla y pasarle el Map de selección
                            CrearPreguntaVF controller = loader.getController();
                            controller.setSeleccion(seleccion);

                            // Mostrar la nueva pantalla
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    case "Multiple respuesta":
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_mult_resp.fxml"));
                            Parent root = loader.load();

                            // Obtener el controlador de la nueva pantalla y pasarle el Map de selección
                            CrearPreguntaMultipleRespuesta controller = loader.getController();
                            controller.setSeleccion(seleccion);

                            // Mostrar la nueva pantalla
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                }

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Error al cargar la nueva pantalla: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert("Advertencia", "Debe seleccionar un tema y un tipo de pregunta.", Alert.AlertType.WARNING);
        }
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }


}
