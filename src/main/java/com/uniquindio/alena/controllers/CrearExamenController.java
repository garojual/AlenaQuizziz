package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CrearExamenController implements Initializable {

    private static final String SQL_PREGUNTAS_BANCO = "SELECT NOMBRE_TEMA, ENUNCIADO, TIPO_PREGUNTA FROM preguntas_publicas_por_tema";
    private static final String SQL_TEMAS = "SELECT NOMBRE_TEMA FROM tema";
    private static final String CALL_QUESTIONS_BY_TOPIC = "{ ? = call get_preguntas_por_tema(?) }";

    private static final String CALL_ADD_QUESTION = "{ call add_pregunta_examen(?, ?, ?) }";

    @FXML
    private ListView<String> questionListView;

    @FXML
    private ComboBox<String> temasList;

    @FXML
    private ListView<String> listaExamenActual;

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

            ObservableList<String> temas = getTemasFromDatabase(connection);
            temasList.setItems(temas);

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

    @FXML
    private void onTemaSelected() {
        String selectedTema = temasList.getValue();
        if (selectedTema != null) {
            updateQuestionListView(selectedTema);
        }
    }

    private void updateQuestionListView(String tema) {
        try {
            Connection connection = databaseConnection.getConnection();

            // Preparar la llamada al procedimiento almacenado
            CallableStatement callableStatement = connection.prepareCall(CALL_QUESTIONS_BY_TOPIC);
            callableStatement.registerOutParameter(1, Types.REF_CURSOR);
            callableStatement.setString(2, tema);

            // Ejecutar la llamada al procedimiento almacenado
            callableStatement.execute();

            // Obtener el cursor devuelto por el procedimiento almacenado
            ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

            // Procesar el cursor y actualizar la ListView
            ObservableList<String> questions = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String questionText = resultSet.getString("ENUNCIADO");
                questions.add(questionText);
            }
            questionListView.setItems(questions);

            // Cerrar la conexión después de usarla
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
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

    @FXML
    private void onAddButtonClicked(ActionEvent event) {
        String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            // Añadir la pregunta seleccionada a la lista de preguntas del examen
            listaExamenActual.getItems().add(selectedQuestion);

            // Eliminar la pregunta seleccionada de la lista de preguntas generales
            questionListView.getItems().remove(selectedQuestion);

            // Lógica para añadir la pregunta al examen en la base de datos
            String selectedTema = temasList.getValue();
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