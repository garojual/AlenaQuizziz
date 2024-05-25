package com.uniquindio.alena.controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PresentarExamenController implements Initializable {

    @FXML
    public AnchorPane rootPane;
    @FXML
    public VBox rootVBox;
    @FXML
    public ScrollPane rootScrollPane;

    private HashMap preguntas;

    public PresentarExamenController() {
    }

    private DataBaseConnection conexionDB; // Conexión a la base de datos

    public void setDatabaseConnection(DataBaseConnection conexionDB) {
        this.conexionDB = conexionDB;
    }

    @FXML
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Obtener preguntas y respuestas de la base de datos
//        String consulta = "SELECT p.id_pregunta, p.enunciado, p.tipo_pregunta, r.id_respuesta, r.enunciado_respuesta " +
//                "FROM pregunta p " +
//                "LEFT JOIN respuesta r ON p.id_pregunta = r.id_pregunta";

//        try {
//            DataBaseConnection dataBaseConnection = new DataBaseConnection();
//            Connection connection = dataBaseConnection.getConnection();
//
//            PreparedStatement statement = connection.prepareStatement(consulta);
//            ResultSet rs = statement.executeQuery();
//
//        } catch (SQLException e) {
//            e.printStackTrace();
            // Manejar la excepción de manera adecuada
//        }
        System.out.println("Prueba");
        rootVBox.setPrefWidth(600);
        rootVBox.setAlignment(Pos.TOP_CENTER);
        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearPreguntaMultipleRespuesta("2", "Enunciado2", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearMetodoVerdaderoFalso("3", "Enunciado3");
        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearMetodoVerdaderoFalso("3", "Enunciado3");
        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearPreguntaMultipleRespuesta("2", "Enunciado2", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearMetodoVerdaderoFalso("3", "Enunciado3");
        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
        crearMetodoVerdaderoFalso("31", "Enunciado3");
//        crearPreguntaMultipleRespuesta("2", "Enunciado2", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearMetodoVerdaderoFalso("3", "Enunciado3");
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearMetodoVerdaderoFalso("3", "Enunciado3");
    }

    private void crearPreguntaUnicaRespuesta(String numPreg, String enunciado, String res1, String res2, String res3, String res4){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);
        vBoxEnunciado.setPrefWidth(600);

        Label labelUnicaRespuesta = new Label(numPreg + ". " + enunciado);
        setEstiloLabel(labelUnicaRespuesta);
        vBoxEnunciado.getChildren().add(labelUnicaRespuesta);
        rootVBox.getChildren().add(vBoxEnunciado);

        VBox vBoxRespuestas = new VBox();
        vBoxRespuestas.setAlignment(Pos.CENTER_LEFT);
        setRespuestasUR(vBoxRespuestas, res1, res2, res3, res4);
        rootVBox.getChildren().add(vBoxRespuestas);
    }

    private void setEstiloLabel(Node node){
        node.setStyle("-fx-font: 13 arial;");
    }

    private void setCheckboxClickFunct(CheckBox node){
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                System.out.println(node.getText());
                }
        });
    }
    private void setRespuestasUR(VBox vBox, String res1, String res2, String res3, String res4){
        RadioButton respuesta1 = new RadioButton(res1);
        RadioButton respuesta2 = new RadioButton(res2);
        RadioButton respuesta3 = new RadioButton(res3);
        RadioButton respuesta4 = new RadioButton(res4);

        ToggleGroup grupoRespuestas = new ToggleGroup();
        respuesta1.setToggleGroup(grupoRespuestas);
        respuesta2.setToggleGroup(grupoRespuestas);
        respuesta3.setToggleGroup(grupoRespuestas);
        respuesta4.setToggleGroup(grupoRespuestas);

        vBox.getChildren().add(respuesta1);
        vBox.getChildren().add(respuesta2);
        vBox.getChildren().add(respuesta3);
        vBox.getChildren().add(respuesta4);
    }

    private  void crearPreguntaMultipleRespuesta(String numPreg, String enunciado, String res1, String res2, String res3, String res4){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);
        vBoxEnunciado.setPrefWidth(600);

        Label labelUnicaRespuesta = new Label(numPreg + ". " + enunciado);
        setEstiloLabel(labelUnicaRespuesta);
        vBoxEnunciado.getChildren().add(labelUnicaRespuesta);
        rootVBox.getChildren().add(vBoxEnunciado);

        VBox vBoxRespuestas = new VBox();
        vBoxRespuestas.setAlignment(Pos.CENTER_LEFT);
        setRespuestasMR(vBoxRespuestas, res1, res2, res3, res4);
        rootVBox.getChildren().add(vBoxRespuestas);
    }

    private void setRespuestasMR(VBox vBox, String res1, String res2, String res3, String res4) {
        CheckBox respuesta1 = new CheckBox(res1);
        setCheckboxClickFunct(respuesta1);
        CheckBox respuesta2 = new CheckBox(res2);
        setCheckboxClickFunct(respuesta2);
        CheckBox respuesta3 = new CheckBox(res3);
        setCheckboxClickFunct(respuesta3);
        CheckBox respuesta4 = new CheckBox(res4);
        setCheckboxClickFunct(respuesta4);

        vBox.getChildren().add(respuesta1);
        vBox.getChildren().add(respuesta2);
        vBox.getChildren().add(respuesta3);
        vBox.getChildren().add(respuesta4);
    }

    private void crearMetodoVerdaderoFalso(String numPreg, String enunciado){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);
        vBoxEnunciado.setPrefWidth(600);

        Label labelUnicaRespuesta = new Label(numPreg + ". " + enunciado);
        setEstiloLabel(labelUnicaRespuesta);
        vBoxEnunciado.getChildren().add(labelUnicaRespuesta);
        rootVBox.getChildren().add(vBoxEnunciado);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        setVerdaderFalso(hBox);
        rootVBox.getChildren().add(hBox);
    }

    private void setVerdaderFalso(HBox hBox) {
        RadioButton verdadero = new RadioButton("Verdadero");
        RadioButton falso = new RadioButton("Falso");

        ToggleGroup VFToogleGroup = new ToggleGroup();

        verdadero.setToggleGroup(VFToogleGroup);
        falso.setToggleGroup(VFToogleGroup);

        hBox.getChildren().add(verdadero);
        hBox.getChildren().add(falso);
    }
}

    // Obtener el contenedor de la pantalla
