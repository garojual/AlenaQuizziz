package com.uniquindio.alena.controllers;

import com.almasb.fxgl.entity.level.tiled.Layer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AsignarPorcentajeSubpreguntas implements Initializable {

    private Map<String, Integer> subPreguntasMap;

    @FXML
    private VBox rootVBox;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void crearMenuPorcentajes(int numPregunta){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Label laberPorcentaje = new Label("Porcentaje " + numPregunta);
        ComboBox comboBox = new ComboBox<>();

        vBox.getChildren().add(laberPorcentaje);
        vBox.getChildren().add(comboBox);

        rootVBox.getChildren().add(vBox);

    }
}
