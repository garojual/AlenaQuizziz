package com.uniquindio.alena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class CrearPreguntaMultipleRespuesta implements Initializable {


    private DataBaseConnection databaseConnection;

    public CrearPreguntaMultipleRespuesta() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private Map<String, String> seleccion;

    @FXML
    private TextArea enunciadoTextArea;



    public void setSeleccion(Map<String, String> seleccion) {
        this.seleccion = seleccion;
    }

    @FXML
    private void onSaveButtonClicked(ActionEvent event) {
        String enunciado = enunciadoTextArea.getText();
        String tema = seleccion.get("tema");
        String tipoPregunta = seleccion.get("tipoPregunta");

        if (enunciado != null && !enunciado.trim().isEmpty()) {
        }
    }

    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
