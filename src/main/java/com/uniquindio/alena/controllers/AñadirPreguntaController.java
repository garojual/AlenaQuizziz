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
import java.util.ResourceBundle;

public class AñadirPreguntaController implements Initializable {

    private static final String SQL_PREGUNTAS_BANCO = "SELECT NOMBRE_TEMA, ENUNCIADO, TIPO_PREGUNTA FROM preguntas_publicas_por_tema";
    private String selectedTema;

    // Variable para almacenar el ID del examen actual
    private int examenIdExamen;
    private static final String CALL_QUESTIONS_BY_TOPIC = "{ ? = call get_preguntas_por_tema(?) }";

    private static final String CALL_ADD_QUESTION = "{ call add_pregunta_examen(?, ?, ?) }";
    @FXML
    private Label temaLabel;



    @FXML
    private ListView<String> questionListView;

    @FXML
    private ListView<String> listaExamenActual;

    private DataBaseConnection databaseConnection;

    public AñadirPreguntaController() {
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
            CallableStatement callableStatement = connection.prepareCall(CALL_QUESTIONS_BY_TOPIC);
            callableStatement.registerOutParameter(1, Types.REF_CURSOR);
            callableStatement.setString(2, selectedTema);

            // Ejecutar la llamada al procedimiento almacenado
            callableStatement.execute();

            // Obtener el cursor devuelto por el procedimiento almacenado
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            while (resultSet.next()) {
                String questionText = resultSet.getString("ENUNCIADO");
                questions.add(questionText);
            }
            // Cerrar la conexión después de usarla
            connection.close();
        } else {
            // Si no se ha seleccionado un tema, se obtienen todas las preguntas.
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL_PREGUNTAS_BANCO);
            while (resultSet.next()) {
                String questionText = resultSet.getString("enunciado");
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

            // Eliminar la pregunta seleccionada de la lista de preguntas generales
            questionListView.getItems().remove(selectedQuestion);

            // Lógica para añadir la pregunta al examen en la base de datos
            if (selectedTema != null) {
                addQuestionToExam(selectedTema, selectedQuestion);
            }
        }
    }

    private void addQuestionToExam(String tema, String pregunta) {
        try {
            Connection connection = databaseConnection.getConnection();

            // Preparar la llamada al procedimiento almacenado
            CallableStatement callableStatement = connection.prepareCall(CALL_ADD_QUESTION);
            callableStatement.setString(1, tema);
            callableStatement.setString(2, pregunta);

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