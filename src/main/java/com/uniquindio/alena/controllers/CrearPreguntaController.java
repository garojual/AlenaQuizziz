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
import java.sql.*;
import java.util.ResourceBundle;

public class CrearPreguntaController implements Initializable {
    private DataBaseConnection databaseConnection;
    private static final String SQL_TEMAS = "SELECT NOMBRE_TEMA FROM tema";

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
            SharedData sharedData = SharedData.getInstance();
            sharedData.setSelectedTema(selectedTema);
            sharedData.setSelectedTipoPregunta(selectedTipoPregunta);
            sharedData.putSeleccion("tema", selectedTema);
            sharedData.putSeleccion("tipoPregunta", selectedTipoPregunta);

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_unica_respuesta.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de la nueva pantalla y pasarle el Singleton (si fuera necesario)
                CrearPreguntaUnicaRespuesta controller = loader.getController();

                // Mostrar la nueva pantalla
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
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
