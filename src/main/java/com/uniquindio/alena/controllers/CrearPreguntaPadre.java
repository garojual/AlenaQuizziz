package com.uniquindio.alena.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import org.w3c.dom.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearPreguntaPadre implements Initializable {

    @FXML
    ComboBox<Integer> numPreguntas;

    @FXML
    TextArea enunciadoTextArea;

    SharedData sharedData = SharedData.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    private void crearActionComboBox(ComboBox comboBox){
        comboBox.setOnAction(e -> {
            sharedData.setNumSubPreguntas((Integer)(comboBox.getSelectionModel().getSelectedItem()));
        });
    }
}
