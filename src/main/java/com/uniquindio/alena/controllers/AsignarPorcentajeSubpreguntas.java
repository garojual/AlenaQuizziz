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
import java.util.*;

public class AsignarPorcentajeSubpreguntas implements Initializable {

    private SharedData sharedData = SharedData.getInstance();
    private Map<Integer, Integer> pesoSubPreguntas;
    private int[] pesosComboBoxSubpreguntas;

    @FXML
    private VBox rootVBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        pesoSubPreguntas = new HashMap<>();
        pesosComboBoxSubpreguntas = new int[sharedData.getPreguntasHijasMapa().size()];


        for (int i=0; i <  sharedData.getPreguntasHijasMapa().size(); i++){
            crearMenuPorcentajes(i);
        }

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Button button = new Button("Aceptar");
        button.setOnAction(e -> {
            asignarPorcentajesPreguntas();
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
            pesoSubPreguntas.put(value, pesosComboBoxSubpreguntas[i]);
            i++;
        }
        System.out.println(Arrays.toString(pesosComboBoxSubpreguntas));


    }

    public void setDatabaseConnection(DataBaseConnection connection) {
    }
}
