package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

import java.sql.*;
import java.util.ResourceBundle;

public class CrearExamenController implements Initializable {

    private static final String SQL_TEMAS = "SELECT NOMBRE_TEMA FROM tema";
    private static final String CALL_QUESTIONS_BY_TOPIC = "{ ? = call get_preguntas_por_tema(?) }";


    @FXML
    private ComboBox<String> temasList;


    private DataBaseConnection databaseConnection;

    public CrearExamenController() {
    }

    // Método initialize y updateQuestionListView...

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {
            // Obtener la conexión JDBC
            databaseConnection = new DataBaseConnection();
            Connection connection = databaseConnection.getConnection();

            ObservableList<String> temas = getTemasFromDatabase(connection);
            temasList.setItems(temas);

            // Cerrar la conexión después de usarla
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
        }
    }


    @FXML
    private void onTemaSelected(ActionEvent event) {
        String selectedTema = temasList.getValue();
        if (selectedTema != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_examen_preguntas.fxml"));
                Scene scene = new Scene(loader.load());

                AñadirPreguntaController secondScreenController = loader.getController();
                secondScreenController.setSelectedTema(selectedTema);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private ObservableList<String> getTemasFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> temas = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_TEMAS);
        while (resultSet.next()) {
            String tema = resultSet.getString("NOMBRE_TEMA");
            temas.add(tema);
        }
        return temas;
    }



    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

}