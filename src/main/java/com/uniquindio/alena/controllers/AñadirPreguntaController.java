package com.uniquindio.alena.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AñadirPreguntaController implements Initializable {

    private static final String SQL_PREGUNTAS_BANCO = "SELECT ID_PREGUNTA, ENUNCIADO FROM preguntas_publicas_por_tema";
    private String selectedTema;

    // Variable para almacenar el ID del examen actual
    private int examenIdExamen;

    private static final String CALL_ADD_QUESTION = "{ call add_preguntas_examen(?, ?) }";
    @FXML
    private Label temaLabel;

    @FXML
    private ListView<String> questionListView;

    @FXML
    private ListView<String> listaExamenActual;

    @FXML
    private ComboBox<Integer> porcentaje;

    private Map<String, Integer> preguntasMap;

    private DataBaseConnection databaseConnection;

    public AñadirPreguntaController() {
        preguntasMap = new HashMap<>();
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        try {

            ObservableList<Integer> opciones = FXCollections.observableArrayList(
                    10,20,30,40,50,60,70,80,90,100
            );
            porcentaje.setItems(opciones);
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

    public void setSelectedTema(String selectedTema) {
        this.selectedTema = selectedTema;
        temaLabel.setText(selectedTema); // Actualiza el label con el tema seleccionado
    }

    public void setExamenIdExamen(int examenIdExamen) {
        this.examenIdExamen = examenIdExamen;
    }

    private ObservableList<String> getQuestionsFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> questions = FXCollections.observableArrayList();
        // Si se ha seleccionado un tema, se obtienen las preguntas de ese tema.
        if (selectedTema != null) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_PREGUNTAS_BANCO);
            while (resultSet.next()) {
                int questionId = resultSet.getInt("ID_PREGUNTA");
                String questionText = resultSet.getString("ENUNCIADO");
                preguntasMap.put(questionText, questionId);
                questions.add(questionText);
            }
        }
        return questions;
    }

    @FXML
    private void onAddButtonClicked(ActionEvent event) {
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            // Añadir la pregunta seleccionada a la lista de preguntas del examen
            listaExamenActual.getItems().add(selectedQuestion);

            // Obtener el ID de la pregunta seleccionada
            int questionId = preguntasMap.get(selectedQuestion);

            // Lógica para añadir la pregunta al examen en la base de datos
            if (examenIdExamen != 0) {
                addQuestionToExam(questionId);
            }
        }
    }

    private void addQuestionToExam(int preguntaId) {
        try {
            Connection connection = databaseConnection.getConnection();

            // Preparar la llamada al procedimiento almacenado
            CallableStatement callableStatement = connection.prepareCall(CALL_ADD_QUESTION);
            callableStatement.setInt(1, examenIdExamen);
            callableStatement.setInt(2, preguntaId);

            // Ejecutar la llamada al procedimiento almacenado
            callableStatement.execute();

            // Cerrar la conexión después de usarla
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
        }
    }

    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
