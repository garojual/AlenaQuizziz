package com.uniquindio.alena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CrearPreguntaMultipleRespuesta implements Initializable {

    private DataBaseConnection databaseConnection = new DataBaseConnection();
    SharedData sharedData = SharedData.getInstance();
    @FXML
    private Label temaLabel;

    @FXML
    private TextArea enunciado;

    @FXML
    private TextField respuesta1;
    @FXML
    private TextField respuesta2;
    @FXML
    private TextField respuesta3;
    @FXML
    private TextField respuesta4;

    @FXML
    private CheckBox CheckBox1;
    @FXML
    private CheckBox CheckBox2;
    @FXML
    private CheckBox CheckBox3;
    @FXML
    private CheckBox CheckBox4;

    private List<CheckBox> checkBoxes;
    private List<TextField> textFields;
    private ArrayList<String> seleccion = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicializamos listas de CheckBoxes y TextFields
        checkBoxes = List.of(CheckBox1, CheckBox2, CheckBox3, CheckBox4);
        textFields = List.of(respuesta1, respuesta2, respuesta3, respuesta4);

        temaLabel.setText(sharedData.getSelectedTemaPregunta());

        // Agregar acciones a los CheckBoxes
        for (CheckBox checkBox : checkBoxes) {
            addActionCheckBox(checkBox);
        }
    }

    @FXML
    private void crear(ActionEvent event) {
        String enunciadoText = enunciado.getText();
        String tema = temaLabel.getText();
        String tipo = "Multiple respuesta";
        String estado = "Finalizada";
        int id_pregunta;

        try {
            String ADD_QUESTION = "{ ? = call add_pregunta(?, ?, ?, ?, ?, ?, ?) }";
            Connection connection = databaseConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall(ADD_QUESTION);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2, sharedData.getSeleccion(tema));

            if (sharedData.isPadre() != null) {
                callableStatement.setInt(3, sharedData.isPadre());
            } else {
                callableStatement.setNull(3, Types.INTEGER);
            }

            callableStatement.setString(4, tipo);
            callableStatement.setInt(5, sharedData.getSeleccionURCorrecta());
            callableStatement.setString(6, enunciadoText);
            callableStatement.setString(7, sharedData.getDocenteId());
            callableStatement.setString(8, estado);
            callableStatement.execute();
            id_pregunta = callableStatement.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < textFields.size(); i++) {
            try {
                String respuestaText = textFields.get(i).getText();
                CheckBox checkBox = checkBoxes.get(i);
                String ADD_RESPUESTA = "{ call ADD_RESPUESTA(?,?,?) }";
                Connection connection1 = databaseConnection.getConnection();
                CallableStatement callableStatement1 = connection1.prepareCall(ADD_RESPUESTA);
                callableStatement1.setString(1, respuestaText);
                callableStatement1.setString(2, checkBox.isSelected() ? "correcta" : "incorrecta");
                callableStatement1.setInt(3, id_pregunta);
                callableStatement1.execute();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
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

    private void addActionCheckBox(CheckBox checkBox) {
        checkBox.setOnAction(e -> {
            seleccion.add(checkBox.getId().toString());
        });
    }

    @FXML
    private void cancelar(ActionEvent event) {
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

    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
