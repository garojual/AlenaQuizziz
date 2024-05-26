package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class CrearExamenController implements Initializable {

    private static final String SQL_TEMAS = "SELECT ID_TEMA,NOMBRE_TEMA FROM tema";
    private static final String SQL_CATEGORIAS = "SELECT ID_CATEGORIA, NOMBRE_CATEGORIA FROM categoria";


    @FXML
    private ComboBox<String> temasList;
    @FXML
    private TextField nomExamen;
    @FXML
    private TextField numPreguntas;
    @FXML
    private DatePicker fecha;
    @FXML
    private Spinner<Integer> horaInicio;
    @FXML
    private Spinner<Integer> minutosInicio;
    @FXML
    private Spinner<Integer> horaFin;
    @FXML
    private Spinner<Integer> minutosFin;
    @FXML
    private TextArea descripcion;
    @FXML
    private TextField numPregAlumno;
    @FXML
    private ComboBox<String> categoria;
    @FXML
    private Button continuar;



    private DataBaseConnection databaseConnection;
    private SharedData sharedData = SharedData.getInstance();
    private final ShowAlert showAlert = new ShowAlert();
    // Variable para controlar si se ha mostrado la advertencia
    private boolean advertenciaMostrada = false;

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

            ObservableList<String> categorias = getCategoriasFromDatabase(connection);
            categoria.setItems(categorias);

            SpinnerValueFactory<Integer> hourValueFactoryInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().getHour());
            SpinnerValueFactory<Integer> minuteValueFactoryInicio = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
            horaInicio.setValueFactory(hourValueFactoryInicio);
            minutosInicio.setValueFactory(minuteValueFactoryInicio);

            // Inicializar Spinners de hora y minuto para fecha de fin
            SpinnerValueFactory<Integer> hourValueFactoryFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, LocalTime.now().plusHours(1).getHour());
            SpinnerValueFactory<Integer> minuteValueFactoryFin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, LocalTime.now().getMinute());
            horaFin.setValueFactory(hourValueFactoryFin);
            minutosFin.setValueFactory(minuteValueFactoryFin);

            // Cerrar la conexión después de usarla
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción (mostrar un mensaje de error, registrarla, etc.)
        }
    }


    @FXML
    private void onTemaSelected() {
        String selectedTema = temasList.getValue();
        if (selectedTema != null) {
            sharedData.setSelectedTemaExamen(selectedTema);
        }
        else {
            showAlert.error("No puede crear un examen sin tema");
        }

    }

    @FXML
    private void onCategoriaSelected() {
        String selectedCategoria = categoria.getValue();
        if (selectedCategoria != null) {
            sharedData.setSelectedCategoria(selectedCategoria);
        }
        else {
            showAlert.error("No puede crear un examen sin tema");
        }

    }

    @FXML
    private void onContinueButtonClicked(ActionEvent event) {
        // Obtener los valores de los campos
        String nombreExamen = nomExamen.getText();
        String docenteId = sharedData.getDocenteId();
        System.out.println(docenteId);
        int numPregunta = Integer.parseInt(numPreguntas.getText());
        sharedData.setNumPreguntas(numPregunta);
        System.out.println(numPregunta);
        LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha.getValue(), LocalTime.of(horaInicio.getValue(), minutosInicio.getValue()));
        LocalDateTime fechaHoraFin = LocalDateTime.of(fecha.getValue(), LocalTime.of(horaFin.getValue(), minutosFin.getValue()));
        String desc = descripcion.getText();
        int numPregPorExamen = Integer.parseInt(numPregAlumno.getText());
        sharedData.setPregPorAlumno(numPregPorExamen);
        int temaId = sharedData.getTemasMapExam(temasList.getValue());
        sharedData.setTemasMapExam(temasList.getValue(),temaId);
        System.out.println(sharedData.getTemasMapExam(temasList.getValue()));
        System.out.println(temaId);
        int categoriaId = sharedData.getCategoriasMapExam(categoria.getValue());



        // Llamar a la función para insertar el examen y obtener el ID


        // Llamar a la función para insertar el examen y obtener el ID
        try {
            Connection connection = databaseConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall("{ ? = call CALCULARDIFERENCIA(?, ?) }");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, numPregunta);
            callableStatement.setInt(3, temaId);
            callableStatement.execute();

            int diferencia = callableStatement.getInt(1);

            // Mostrar la advertencia solo si no se ha mostrado antes
            if (diferencia < 0 && !advertenciaMostrada) {
                advertenciaMostrada = true;
                showAlert.advertencia("Faltan " + Math.abs(diferencia) + " preguntas en el banco para crear este examen. Deberá crear las preguntas faltantes.");

            } else {
                crearExamen(nombreExamen, numPregunta, fechaHoraInicio, fechaHoraFin, desc, numPregPorExamen, docenteId, temaId, categoriaId);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_examen_preguntas.fxml"));
                    Parent root = loader.load();

                    // Mostrar la nueva pantalla
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    private void crearExamen(String nombreExamen, int numPreguntas, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin, String descripcion, int numPregPorExamen, String docenteId, int temaId, int categoriaId) {
        try {
            // Establecer conexión con la base de datos

            Connection connection = databaseConnection.getConnection();

            // Llamar a la función para insertar el examen y obtener el ID
            CallableStatement callableStatement = connection.prepareCall("{ ? = call add_examen(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }");
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setString(2, nombreExamen);
            callableStatement.setInt(3, numPreguntas);
            callableStatement.setTimestamp(4, Timestamp.valueOf(fechaHoraInicio));
            callableStatement.setTimestamp(5, Timestamp.valueOf(fechaHoraFin));
            callableStatement.setString(6, descripcion);
            callableStatement.setInt(7, numPregPorExamen);
            callableStatement.setString(8, docenteId);
            callableStatement.setString(9, "en edicion");
            callableStatement.setInt(10, temaId);
            callableStatement.setInt(11, categoriaId);




            callableStatement.execute();

            int idExamen = callableStatement.getInt(1);
            sharedData.setIdExamen(idExamen);
            System.out.println(idExamen);

            // Cerrar la conexión
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert.error("Error al crear el examen: ");
        }
    }



    private ObservableList<String> getTemasFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> temas = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_TEMAS);
        while (resultSet.next()) {
            int id= resultSet.getInt("ID_TEMA");
            String tema = resultSet.getString("NOMBRE_TEMA");
            sharedData.setTemasMapExam(tema,id);
            System.out.println(sharedData.getTemasMapExam(tema));
            temas.add(tema);
        }
        return temas;
    }

    private ObservableList<String> getCategoriasFromDatabase(Connection connection) throws SQLException {
        ObservableList<String> categorias = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_CATEGORIAS);
        while (resultSet.next()) {
            int id= resultSet.getInt("ID_CATEGORIA");
            String categoria = resultSet.getString("NOMBRE_CATEGORIA");
            sharedData.setCategoriasMapExam(categoria,id);
            categorias.add(categoria);
        }
        return categorias;
    }



    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

}