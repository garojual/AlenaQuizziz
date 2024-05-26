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
    import javafx.scene.control.ComboBox;
    import javafx.scene.control.Label;
    import javafx.scene.control.ListView;
    import javafx.stage.Stage;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.*;
    import java.util.HashMap;
    import java.util.Map;
    import java.util.ResourceBundle;

    import static oracle.jdbc.OracleTypes.*;

    public class AñadirPreguntaController implements Initializable {

        private SharedData sharedData = SharedData.getInstance();
        private ShowAlert showAlert = new ShowAlert();

        private static final String SQL_PREGUNTAS_BANCO = "{ ? = calL GET_PREGUNTAS_POR_TEMA(?) }";


        // Variable para almacenar el ID del examen actual
        private int examenIdExamen= sharedData.getIdExamen() ;

        private static final String CALL_ADD_QUESTION = "{ call add_preguntas_examen(?, ?, ?) }";
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
        private String selectedTema;

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

                selectedTema= sharedData.getSelectedTemaExamen();
                System.out.println(selectedTema);
                System.out.println(sharedData.getTemasMapExam(selectedTema));

                // Obtener las preguntas de la base de datos y actualizar el ListView
                ObservableList<String> questions = getQuestionsFromDatabase(connection);
                questionListView.setItems(questions);

                temaLabel.setText(sharedData.getSelectedTemaExamen());

                // Cerrar la conexión después de usarla
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
            }
        }




        private ObservableList<String> getQuestionsFromDatabase(Connection connection) throws SQLException {
            ObservableList<String> questions = FXCollections.observableArrayList();
            // Si se ha seleccionado un tema, se obtienen las preguntas de ese tema.
            if (selectedTema != null) {
                CallableStatement callableStatement = connection.prepareCall(SQL_PREGUNTAS_BANCO);
                callableStatement.registerOutParameter(1, REF_CURSOR);
                callableStatement.setString(2,selectedTema);

                // Ejecutar la llamada a la función SQL
                callableStatement.execute();
                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);

                // Procesar el resultado y almacenar las preguntas en la lista
                while (resultSet.next()) {
                    String questionText = resultSet.getString("ENUNCIADO");
                    int questionId = resultSet.getInt("ID_PREGUNTA");
                    preguntasMap.put(questionText, questionId);
                    questions.add(questionText);

                }
                resultSet.close();
                callableStatement.close();
            }
            return questions;
        }

        @FXML
        private void onAddButtonClicked(ActionEvent event) {
            String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
            Integer selectedOption = porcentaje.getSelectionModel().getSelectedItem();
            if (selectedOption == null){
                showAlert.advertencia("Debe seleccionar un porcentaje para la pregunta");
            }
            if (selectedQuestion != null && selectedOption != null) {
                // Añadir la pregunta seleccionada a la lista de preguntas del examen
                listaExamenActual.getItems().add(selectedQuestion);

                // Obtener el ID de la pregunta seleccionada
                int questionId = preguntasMap.get(selectedQuestion);

                // Lógica para añadir la pregunta al examen en la base de datos
                if (examenIdExamen != 0) {
                    addQuestionToExam(questionId,selectedOption);
                }
            }
        }

        @FXML
        private void OnCreatedButtonClicked(ActionEvent event){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_tema.fxml"));
                Parent root = loader.load();
                // Mostrar la nueva pantalla
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert.error("Error al cargar la nueva pantalla: " );
            }
        }

        @FXML
        private void onFinalizar(ActionEvent event){

        }

        private void addQuestionToExam(int preguntaId, int opcion) {
            try {
                Connection connection = databaseConnection.getConnection();

                // Preparar la llamada al procedimiento almacenado
                CallableStatement callableStatement = connection.prepareCall(CALL_ADD_QUESTION);
                callableStatement.setInt(1, examenIdExamen);
                callableStatement.setInt(2, preguntaId);
                callableStatement.setInt(3,opcion);

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
