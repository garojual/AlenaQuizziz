package com.uniquindio.alena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Map;
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

    public CrearPreguntaMultipleRespuesta() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            addActionRadioButton(CheckBox2);
            addActionRadioButton(CheckBox3);
            addActionRadioButton(CheckBox1);
            addActionRadioButton(CheckBox4);
            temaLabel.setText(sharedData.getSelectedTemaPregunta());
    }

    private ArrayList<String> seleccion = new ArrayList<>();



    @FXML
    private void crear(ActionEvent event) {
        String enunciadoText = enunciado.getText();
        String tema = temaLabel.getText();
        String tipo= "Multiple respuesta";
        String estado= "Finalizada";
        int id_pregunta;

        try {
            String ADD_QUESTION = "{ ? = call add_pregunta(?, ?, ?, ?, ?, ?, ?) }";
            Connection connection = databaseConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall(ADD_QUESTION);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2,sharedData.getSeleccion(tema));
            if (sharedData.isPadre()!=null){
                callableStatement.setInt(3,sharedData.isPadre());
            }
            else {
                callableStatement.setNull(3,Types.INTEGER);
            }
            callableStatement.setString(4,tipo);
            callableStatement.setInt(5,sharedData.getSeleccionURCorrecta());
            callableStatement.setString(6,enunciadoText);
            callableStatement.setString(7, sharedData.getDocenteId());
            callableStatement.setString(8,estado);
            callableStatement.execute();
            id_pregunta= callableStatement.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i=1; i<=4;i++){
            try {
                String respuesta = "respuesta" + i;
                String boton = "CheckBox" + i;
                String ADD_RESPUESTA = "{ call ADD_RESPUESTA(?,?,?) }";
                Connection connection1 = databaseConnection.getConnection();
                CallableStatement callableStatement1 = connection1.prepareCall(ADD_RESPUESTA);
                callableStatement1.setString(1,respuesta);
                if(boton.equals(seleccion.get(i))){
                    callableStatement1.setString(2,"correcta");
                }
                else {
                    callableStatement1.setString(2,"incorrecta");
                }
                callableStatement1.setInt(3,id_pregunta);
                callableStatement1.execute();


            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void addActionRadioButton(CheckBox radioButton){
        radioButton.setOnAction(e ->{
            seleccion.add(radioButton.getId().toString());
        });
    }

    @FXML
    private void cancelar(){

    }



    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
