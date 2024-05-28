package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearSubPreguntas implements Initializable {

    @FXML
    VBox rootVBox;

    int numSubPreguntas;
    int numPreguntasCreadas;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
        * Obtener el numero de preguntas de la pantalla anterior y asignarlo a numSubPreguntas
        * */

        for (int i = 0; i < 5; i++){
            crearMenuTipoPreguntas(i);
        }

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        Button button = new Button("Finalizar");
        hBox.getChildren().add(button);
        rootVBox.getChildren().add(hBox);
    }

    private void addActionButtonFinalizar(Button button){
        button.setOnAction(e ->{
            //Guardar preguntas y cerrar escena cueste lo que cueste
        });
    }

    private void crearMenuTipoPreguntas(int numPregunta){
        ObservableList<String> tiposPregunta = FXCollections.observableArrayList(
                "Unica respuesta",
                "Verdadero/Falso",
                "Multiple respuesta",
                "Emparejar",
                "Ordenar",
                "Completar"
        );

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label("Pregunta " + (numPregunta + 1));
        ComboBox comboBox = new ComboBox();
        comboBox.setItems(tiposPregunta);

        comboBox.setId(String.valueOf(numPregunta));
        Button button = new Button("Crear Pregunta");
        button.setId(String.valueOf(numPregunta));
        setActionButton(button);

        hBox.getChildren().add(label);
        hBox.getChildren().add(comboBox);
        hBox.getChildren().add(button);

        rootVBox.getChildren().add(hBox);

    }

    private void setActionButton(Button button){
        button.setOnAction(e -> {
            String idButton = button.getId();
            String seleccionTipoPregunta = null;
            //Obten todos los objetos de la escena
            for (int i = 0; i < rootVBox.getChildren().size(); i++) {
                //Verifica si el objeto es un comboBox
                if(rootVBox.getChildren().get(i) instanceof HBox) {
                    HBox node = (HBox) rootVBox.getChildren().get(i);
                    for (int j = 0; j < node.getChildren().size(); j++) {
                        if (node.getChildren().get(j) instanceof ComboBox<?>) {
                            //Compara el id del comboBox con el del boton, cada par tiene el mismo id
                            if (node.getChildren().get(j).getId().equals(button.getId())) {
                                if (((ComboBox<?>) node.getChildren().get(j)).getSelectionModel().getSelectedItem() != null) {
                                    seleccionTipoPregunta = (String) (((ComboBox<?>) node.getChildren().get(j)).
                                            getSelectionModel().getSelectedItem());
                                    System.out.println(seleccionTipoPregunta);

                                    break;
                                } else {
                                    //levantar error, se debe seleccionar un tipo de pregunta
                                    System.out.println("El tipo de pregunta es nulo");
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            switch (seleccionTipoPregunta){
                case "Única respuesta":
                    //Llamar a la pantalla de creacion de unica respuesta sin cerrar esta

                case "Múltiple respuesta":
                    //Llamar a la pantalla de creacion de mutliple respuesta sin cerrar esta

                case "Ordenar":
                    //Llamar a la pantalla de creacion de ordenar sin cerrar esta

                case "Emparejar":
                    //Llamar a la pantalla de creacion de Emparejar sin cerrar esta

                case "Completar":
                    //Llamar a la pantalla de creacion de Completar sin cerrar esta

                case "Verdadero/Falso":
                    //Llamar a la pantalla de creacion de Verdadero/Falso sin cerrar esta
            }

        });
    }

    public void setDatabaseConnection(DataBaseConnection connection) {
    }
}
