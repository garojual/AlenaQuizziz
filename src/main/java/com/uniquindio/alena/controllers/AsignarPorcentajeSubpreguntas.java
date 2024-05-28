package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class AsignarPorcentajeSubpreguntas implements Initializable {

    private SharedData sharedData = SharedData.getInstance();

    private int[] pesosComboBoxSubpreguntas;
    private DataBaseConnection dataBaseConnection = new DataBaseConnection();


    @FXML
    private VBox rootVBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        pesosComboBoxSubpreguntas = new int[sharedData.getPreguntasHijasMapa().size()];


        for (int i=0; i <  sharedData.getPreguntasHijasMapa().size(); i++){
            crearMenuPorcentajes(i);
        }

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Button button = new Button("Aceptar");
        button.setOnAction(e -> {
            asignarPorcentajesPreguntas();
            int i = 0;
            for(Integer value : sharedData.getPreguntasHijasMapa().values()){
                addQuestionToExam(value,sharedData.getPesoSubPreguntas(value),dataBaseConnection);
                i++;
            }
            Stage stage= (Stage) rootVBox.getScene().getWindow();
            stage.close();

        });

        vBox.getChildren().add(button);

        rootVBox.getChildren().add(vBox);
    }

    private void crearMenuPorcentajes(int numPregunta){
        ObservableList<Integer> opciones = FXCollections.observableArrayList(
                10,20,30,40,50,60,70,80,90,100
        );

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Label laberPorcentaje = new Label("Porcentaje Pregunta " + (numPregunta + 1));

        ComboBox comboBox = new ComboBox<>();
        comboBox.setItems(opciones);
        comboBox.setId(String.valueOf(numPregunta));
        asignarAccionComboBox(comboBox);

        vBox.getChildren().add(laberPorcentaje);
        vBox.getChildren().add(comboBox);

        rootVBox.getChildren().add(vBox);


    }

    private void asignarAccionComboBox(ComboBox comboBox) {
        comboBox.setOnAction(e -> {
            int aux = Integer.valueOf(comboBox.getId());
            System.out.println(comboBox.getValue() + " id: " + aux);
            pesosComboBoxSubpreguntas[aux] = (int) comboBox.getValue();
            System.out.println(Arrays.toString(pesosComboBoxSubpreguntas));
        });
    }

    private void asignarPorcentajesPreguntas() {
        int i = 0;
        for(Integer value : sharedData.getPreguntasHijasMapa().values()){
            sharedData.setPesoSubPreguntas(value, pesosComboBoxSubpreguntas[i]);
            i++;
        }
        System.out.println(Arrays.toString(pesosComboBoxSubpreguntas));


    }

    private void addQuestionToExam(int preguntaId, int opcion, DataBaseConnection databaseConnection) {
        try {

            Connection connection = databaseConnection.getConnection();
            String CALL_ADD_QUESTION = "{ call add_pregunta_examen(?, ?, ?) }";
            // Preparar la llamada al procedimiento almacenado
            CallableStatement callableStatement = connection.prepareCall(CALL_ADD_QUESTION);
            callableStatement.setInt(1, sharedData.getIdExamen());
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

    public void setDatabaseConnection(DataBaseConnection connection) {
    }
}
