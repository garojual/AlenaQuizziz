package com.uniquindio.alena.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearPreguntaUnicaRespuesta implements Initializable {

    @FXML
    private Label temaLabel;

    @FXML
    private Label tipoPreguntaLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SharedData sharedData = SharedData.getInstance();
        String selectedTema = sharedData.getSelectedTema();
        String selectedTipoPregunta = sharedData.getSelectedTipoPregunta();

        temaLabel.setText(selectedTema);
        tipoPreguntaLabel.setText(selectedTipoPregunta);
    }
}
