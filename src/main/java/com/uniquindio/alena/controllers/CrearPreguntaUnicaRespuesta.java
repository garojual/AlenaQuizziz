package com.uniquindio.alena.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearPreguntaUnicaRespuesta implements Initializable {
    public CrearPreguntaUnicaRespuesta() {
    }

    SharedData sharedData = SharedData.getInstance();
    @FXML
    private Label temaLabel;

    @FXML
    private Label tipoPreguntaLabel;

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
    private RadioButton radioBiuton1;

    @FXML
    private RadioButton radioButon2;

    @FXML
    private RadioButton radioButon3;

    @FXML
    private RadioButton redioButon4;

    private ToggleGroup toggleGroup;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        toggleGroup = new ToggleGroup();
        radioBiuton1.setToggleGroup(toggleGroup);
        radioButon2.setToggleGroup(toggleGroup);
        radioButon3.setToggleGroup(toggleGroup);
        redioButon4.setToggleGroup(toggleGroup);

        String selectedTema = sharedData.getSelectedTemaPregunta();
        String selectedTipoPregunta = sharedData.getSelectedTipoPregunta();

        temaLabel.setText(selectedTema);
        tipoPreguntaLabel.setText(selectedTipoPregunta);
    }

    @FXML
    private void crear(ActionEvent event) {
        String enunciadoText = enunciado.getText();
        String tema = temaLabel.getText();
        String tipo= "Unica respuesta";
        String estado= "Finalizada";


    }

    @FXML
    private void cancelar(ActionEvent event) {

    }
}
