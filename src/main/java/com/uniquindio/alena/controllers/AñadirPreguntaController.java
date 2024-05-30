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
    import javafx.scene.layout.AnchorPane;
    import javafx.stage.Stage;
    import oracle.jdbc.OracleTypes;

    import java.io.IOException;
    import java.net.URL;
    import java.sql.*;
    import java.util.*;

    import static oracle.jdbc.OracleTypes.*;

    public class AñadirPreguntaController implements Initializable {

        private SharedData sharedData = SharedData.getInstance();
        private ShowAlert showAlert = new ShowAlert();

        private static final String SQL_PREGUNTAS_BANCO = "{ ? = call GET_PREGUNTAS_POR_TEMA(?) }";


        // Variable para almacenar el ID del examen actual
        private int examenIdExamen= sharedData.getIdExamen() ;

        private static final String CALL_ADD_QUESTION = "{ call add_pregunta_examen(?, ?, ?) }";
        @FXML
        private Label temaLabel;

        @FXML
        private ListView<String> questionListView;

        @FXML
        private ListView<String> listaExamenActual;

        @FXML
        private ListView<String> listaSubPreguntas;

        @FXML
        private ComboBox<Integer> porcentaje;


        private DataBaseConnection databaseConnection;
        private String selectedTema;
        int numPreguntas=0;

        public AñadirPreguntaController() {
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
                    sharedData.setPreguntasMap(questionText,questionId);
                    String PREGUNTA_HIJA = "{ ? = call es_pregunta_hija(?) }";
                    CallableStatement callableStatement1 = connection.prepareCall(PREGUNTA_HIJA);
                    callableStatement1.registerOutParameter(1,Types.VARCHAR);
                    callableStatement1.setInt(2,questionId);
                    callableStatement1.execute();
                    if(callableStatement1.getString(1).equals("FALSE") ){
                        questions.add(questionText);
                        questionListView.setItems(questions);
                    }

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
            String selectedQuestionType;
            try {
                String QUESTION_TYPE = "{ ? = call get_tipo(?) }";
                Connection connection=databaseConnection.getConnection();
                CallableStatement callableStatement = connection.prepareCall(QUESTION_TYPE);
                callableStatement.registerOutParameter(1, Types.VARCHAR);
                callableStatement.setInt(2,sharedData.getPreguntasMap(selectedQuestion));
                callableStatement.execute();
                selectedQuestionType = callableStatement.getString(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (selectedOption == null){
                showAlert.error("Debe seleccionar un porcentaje para la pregunta");
            }
            if(numPreguntas > sharedData.getNumPreguntas()){
                showAlert.error("Ya agregó todas las preguntas del examen");
            }
            if (selectedQuestion != null && selectedOption != null && numPreguntas < sharedData.getNumPreguntas()) {
                System.out.println(selectedQuestionType);
                if(!selectedQuestionType.equals("Pregunta padre")) {
                    // Añadir la pregunta seleccionada a la lista de preguntas del examen
                    listaExamenActual.getItems().add(selectedQuestion);
                    numPreguntas += 1;
                    questionListView.getItems().remove(selectedQuestion);

                    // Obtener el ID de la pregunta seleccionada
                    int questionId = sharedData.getPreguntasMap(selectedQuestion);

                    // Lógica para añadir la pregunta al examen en la base de datos
                    if (examenIdExamen != 0) {
                        addQuestionToExam(questionId, selectedOption);
                    }
                    listaSubPreguntas.getItems().clear();
                }
                else{
                    try {
                        int questionId = sharedData.getPreguntasMap(selectedQuestion);
                        addQuestionToExam(questionId,selectedOption);
                        int i = 0;
                        openNewWindow();
                        listaExamenActual.getItems().add(selectedQuestion);
                        numPreguntas += 1;
                        questionListView.getItems().remove(selectedQuestion);
                        listaSubPreguntas.getItems().clear();

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        private void openNewWindow() throws Exception {
            Stage newStage = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/asignar_porcentaje_subpreguntas.fxml"));
            AnchorPane vbox = loader.load();
            Scene scene = new Scene(vbox);
            newStage.setScene(scene);

            newStage.show();
        }

        @FXML
        private void onListViewClicked(){
            try {
                String selectedQuestion = questionListView.getSelectionModel().getSelectedItem();
                if (selectedQuestion != null) {
                    // Obtener el ID de la pregunta seleccionada
                    int questionId = sharedData.getPreguntasMap(selectedQuestion);
                    System.out.println(questionId);
                    String PREGUNTAS_HIJAS = "{ ? = call get_preguntas_hijas(?) }";
                    Connection connection=databaseConnection.getConnection();
                    CallableStatement callableStatement = connection.prepareCall(PREGUNTAS_HIJAS);
                    callableStatement.registerOutParameter(1, REF_CURSOR);
                    callableStatement.setInt(2,questionId);
                    // Ejecuta la llamada a la función
                    callableStatement.execute();

                    // Obtiene el cursor devuelto por la función
                    try (ResultSet rs = (ResultSet) callableStatement.getObject(1)) {
                        // Procesa los resultados del cursor
                        while (rs.next()) {
                            int idPregunta = rs.getInt("ID_PREGUNTA");
                            String enunciado = rs.getString("ENUNCIADO");
                            System.out.println(enunciado);
                            sharedData.setPreguntasHijasMap(enunciado,idPregunta);
                        }
                    }
                    sharedData.getPreguntasHijasMapa().forEach((key, value) -> {
                        listaSubPreguntas.getItems().add(key);
                    });
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
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
            List<Integer> examenesAlumno = obtenerExamenesAlumno(sharedData.getIdExamen());
            try {
                Connection connection = databaseConnection.getConnection();
                String CALL_FINALIZAR_EXAMEN = "{ call finalizar_examen (?) }";
                CallableStatement callableStatement = connection.prepareCall(CALL_FINALIZAR_EXAMEN);
                callableStatement.setInt(1,examenIdExamen);
                System.out.println(examenIdExamen);
                System.out.println(sharedData.getIdCurso());
                callableStatement.execute();
                callableStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

            CallableStatement callableStatement1 = null;


            // Llamar a la función CALL_EXAMEN_ALUMNO para cada examen de alumno obtenido
            for (Integer idExamenAlumno : examenesAlumno) {
                try {
                    Connection connection = databaseConnection.getConnection();
                    String CALL_EXAMEN_ALUMNO = "{ call generar_examenes_alumno_proc (?, ?, ?) }";
                    CallableStatement callableStatement = connection.prepareCall(CALL_EXAMEN_ALUMNO);
                    callableStatement.setString(1, sharedData.getDocenteId());
                    callableStatement.setInt(2, sharedData.getIdCurso());
                    callableStatement.setInt(3, idExamenAlumno);
                    callableStatement.execute();
                    System.out.println("funciona");
                    callableStatement.close();
                    connection.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }


            try {
                String CALL_PREGUNTAS_ALUMNO = "{ call procesar_examen_alumno (?) }";
                Connection connection = databaseConnection.getConnection();
                CallableStatement callableStatement2 = connection.prepareCall(CALL_PREGUNTAS_ALUMNO);
                callableStatement2.setInt(1, sharedData.getIdExamen());
                callableStatement2.execute();
                System.out.println("funciona x2");
                callableStatement2.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/inicio.fxml"));
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
            }
        }

        public void setDatabaseConnection(DataBaseConnection databaseConnection) {
            this.databaseConnection = databaseConnection;
        }

        private List<Integer> obtenerExamenesAlumno(int idExamen) {
            List<Integer> examenesAlumno = new ArrayList<>();
            try {
                Connection connection = databaseConnection.getConnection();
                String CALL_OBTENER_EXAMENES_ALUMNO = "{ ? = call obtener_examenes_alumno(?) }";
                CallableStatement callableStatement = connection.prepareCall(CALL_OBTENER_EXAMENES_ALUMNO);
                callableStatement.registerOutParameter(1, CURSOR);
                callableStatement.setInt(2,idExamen);
                callableStatement.execute();
                ResultSet resultSet = (ResultSet) callableStatement.getObject(1);
                while (resultSet.next()) {
                    int idExamenAlumno = resultSet.getInt("id_examen");
                    examenesAlumno.add(idExamenAlumno);
                }
                resultSet.close();
                callableStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return examenesAlumno;
        }
    }