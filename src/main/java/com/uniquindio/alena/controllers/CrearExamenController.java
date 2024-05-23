package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CrearExamenController implements Initializable {

    private static final String SQL_PREGUNTAS_BANCO = "SELECT NOMBRE_TEMA, ENUNCIADO, TIPO_PREGUNTA FROM preguntas_publicas_por_tema";

    @FXML
    private ListView<String> questionListView;

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

            // Obtener las preguntas de la base de datos y actualizar el ListView
            ObservableList<String> questions = getQuestionsFromDatabase(connection);
            questionListView.setItems(questions);

            // Cerrar la conexión después de usarla
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
        }
    }

    private ObservableList<String> getQuestionsFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> questions = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_PREGUNTAS_BANCO);
        while (resultSet.next()) {
            String questionText = resultSet.getString("enunciado");
            questions.add(questionText);
        }
        return questions;
    }


    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}