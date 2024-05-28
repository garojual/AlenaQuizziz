package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaMultipleRespuesta("2", "Enunciado2", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearMetodoVerdaderoFalso("3", "Enunciado3", "1", "2");
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearMetodoVerdaderoFalso("3", "Enunciado3","1", "2");
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaMultipleRespuesta("2", "Enunciado2", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearMetodoVerdaderoFalso("3", "Enunciado3","1", "2");
//        crearPreguntaUnicaRespuesta("1", "Enunciado1", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaOrdenar("4","Enunciado4","Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaAsociar("5", "Enunciado5", "subEnunciado1", "subEnunciado2", "subEnunciado3", "subEnunciado4", "Respuesta1", "Respuesta2", "Respuesta3", "Respuesta4");
//        crearPreguntaCompletar("6", "Enunciado______");

        //Traerse a como de lugar e ignorando todos los acuerdos de derechos humanos en Ginebra, las preguntas.
        SharedData sharedData = SharedData.getInstance();
        Map<String, Integer> preguntas = sharedData.getPreguntasMapa();


    }

    private void crearPreguntaUnicaRespuesta(String numPreg, String enunciado, String res1, String res2, String res3,
                                             String res4){
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

    private  void crearPreguntaMultipleRespuesta(String numPreg, String enunciado, String res1, String res2,
                                                 String res3, String res4){
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

    private void crearMetodoVerdaderoFalso(String numPreg, String enunciado, String idVerdadero, String idFalso){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);
        vBoxEnunciado.setPrefWidth(600);

        Label labelUnicaRespuesta = new Label(numPreg + ". " + enunciado);
        setEstiloLabel(labelUnicaRespuesta);
        vBoxEnunciado.getChildren().add(labelUnicaRespuesta);
        rootVBox.getChildren().add(vBoxEnunciado);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        setVerdaderFalso(hBox, idVerdadero, idFalso);
        rootVBox.getChildren().add(hBox);
    }

    private void setVerdaderFalso(HBox hBox, String idVerdadero, String idFalso) {
        RadioButton verdadero = new RadioButton("Verdadero");
        verdadero.setId(idVerdadero);
        RadioButton falso = new RadioButton("Falso");
        falso.setId(idFalso);

        ToggleGroup VFToogleGroup = new ToggleGroup();

        verdadero.setToggleGroup(VFToogleGroup);
        falso.setToggleGroup(VFToogleGroup);

        hBox.getChildren().add(verdadero);
        hBox.getChildren().add(falso);
    }

    private void crearPreguntaOrdenar(String numPreg, String enunciado, String res1, String res2, String res3, String res4){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);

        Label labelOrdenar = new Label(numPreg + ". " + enunciado);
        labelOrdenar.setId("10");

        Label label1 = new Label(res1);
        label1.setId("1");
        Label label2 = new Label(res2);
        label2.setId("2");
        Label label3 = new Label(res3);
        label3.setId("3");
        Label label4 = new Label(res4);
        label4.setId("4");

        ObservableList<Integer> opciones = FXCollections.observableArrayList(
                1,2,3,4
        );
        ComboBox comboBox1 = new ComboBox<>();
        comboBox1.setItems(opciones);
        comboBox1.setId("1");
        asignarActionOrdenar(comboBox1);

        ComboBox comboBox2 = new ComboBox<>();
        comboBox2.setItems(opciones);
        comboBox2.setId("2");
        asignarActionOrdenar(comboBox2);

        ComboBox comboBox3 = new ComboBox<>();
        comboBox3.setItems(opciones);
        comboBox3.setId("3");
        asignarActionOrdenar(comboBox3);

        ComboBox comboBox4 = new ComboBox<>();
        comboBox4.setItems(opciones);
        comboBox4.setId("4");
        asignarActionOrdenar(comboBox4);

        vBoxEnunciado.getChildren().add(labelOrdenar);
        vBoxEnunciado.getChildren().add(label1);
        vBoxEnunciado.getChildren().add(comboBox1);
        vBoxEnunciado.getChildren().add(label2);
        vBoxEnunciado.getChildren().add(comboBox2);
        vBoxEnunciado.getChildren().add(label3);
        vBoxEnunciado.getChildren().add(comboBox3);
        vBoxEnunciado.getChildren().add(label4);
        vBoxEnunciado.getChildren().add(comboBox4);

        rootVBox.getChildren().add(vBoxEnunciado);

    }

    private void asignarActionOrdenar(ComboBox comboBox){
        comboBox.setOnAction(e->{
            VBox vBoxAux = (VBox) comboBox.getParent();

            assert vBoxAux != null;
            for (Node node: vBoxAux.getChildren()) {
                if (node instanceof Label){
                    if(node.getId().equals(comboBox.getId())){
                        String enunciado = ((Label) node).getText();
                        String orden = String.valueOf(comboBox.getSelectionModel().getSelectedItem());

                        System.out.println(enunciado + " " + orden);

                        //guardar en hashmap y subirlo a las respuestas del usuario, suerte :D

                    }
                }
            }
        });
    }

    private void crearPreguntaAsociar(String numPreg, String enunciadoPrincipal, String enun1, String enun2, String enun3,
                                      String enun4, String res1, String res2, String res3, String res4){
        VBox vBoxEnunciado = new VBox();
        vBoxEnunciado.setAlignment(Pos.CENTER);

        Label labelAsociar = new Label(numPreg + ". " + enunciadoPrincipal);
        labelAsociar.setId("10");

        ObservableList<String> opciones = FXCollections.observableArrayList(
                enun1, enun2, enun3, enun4
        );

        Label label1 = new Label(res1);
        Label label2 = new Label(res2);
        Label label3 = new Label(res3);
        Label label4 = new Label(res4);

        ComboBox comboBox1 = new ComboBox<>();
        comboBox1.setItems(opciones);
        addActionAsociar(comboBox1);

        ComboBox comboBox2 = new ComboBox<>();
        comboBox2.setItems(opciones);
        addActionAsociar(comboBox2);

        ComboBox comboBox3 = new ComboBox<>();
        comboBox3.setItems(opciones);
        addActionAsociar(comboBox3);

        ComboBox comboBox4 = new ComboBox<>();
        comboBox4.setItems(opciones);
        addActionAsociar(comboBox4);

        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        HBox hBox3 = new HBox();
        HBox hBox4 = new HBox();

        hBox1.getChildren().add(label1);
        hBox1.getChildren().add(comboBox1);

        hBox2.getChildren().add(label2);
        hBox2.getChildren().add(comboBox2);

        hBox3.getChildren().add(label3);
        hBox3.getChildren().add(comboBox3);

        hBox4.getChildren().add(label4);
        hBox4.getChildren().add(comboBox4);

        vBoxEnunciado.getChildren().add(labelAsociar);
        vBoxEnunciado.getChildren().add(hBox1);
        vBoxEnunciado.getChildren().add(hBox2);
        vBoxEnunciado.getChildren().add(hBox3);
        vBoxEnunciado.getChildren().add(hBox4);

        rootVBox.getChildren().add(vBoxEnunciado);

    }

    private void addActionAsociar(ComboBox comboBox){
        comboBox.setOnAction(e ->{
            HBox hBoxAux = (HBox) comboBox.getParent();

            assert hBoxAux != null;
            for (Node node: hBoxAux.getChildren()) {
                if(node instanceof Label){
                    String enunciado = ((Label) node).getText();
                    String par = String.valueOf(comboBox.getSelectionModel().getSelectedItem());

                    System.out.println(enunciado + " " + par);
                }
            }
        });
    }

    private void crearPreguntaCompletar(String numPreg, String enunciadoPrincipal){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label(numPreg + ". " + enunciadoPrincipal);
        TextField textField = new TextField();
        Button button = new Button("Guardar");

        vBox.getChildren().add(label);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(button);
        addActionClick(button, textField);

        rootVBox.getChildren().add(vBox);
    }

    private void addActionClick(Button button, TextField textField){
        button.setOnAction(e ->{
            String respuesta = textField.getText();
            System.out.println(respuesta);
        });
    }

    private void crearPreguntaPadre(String numPregunta, String enunciadoPrincipal, HashMap<Integer, String> subPreguntasMapa, ArrayList<String> tiposSubPreguntasaMapa){
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);

        Label label = new Label(numPregunta + ". " + enunciadoPrincipal);
        int i = 0;

        vBox.getChildren().add(label);
        rootVBox.getChildren().add(vBox);

        for(Map.Entry<Integer,String> entry: subPreguntasMapa.entrySet()){
            String value = entry.getValue();
            String tipo = tiposSubPreguntasaMapa.get(i);


            switch (tipo){
                case "Única respuesta":
                    //Obtener los enunciados y respuestas hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearPreguntaUnicaRespuesta("", "Enunciado 6.1", "Res1", "Res2", "Res3", "Res4");

                case "Múltiple respuesta":
                    //Obtener los enunciados y respuestas hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearPreguntaMultipleRespuesta("","Enunciado 6.2", "Res1", "Res2", "Res3", "Res4");

                case "Ordenar":
                    //Obtener los enunciados y respuestas hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearPreguntaOrdenar("", "Enunciado 6.3", "Res1", "Res2", "Res3", "Res4");

                case "Emparejar":
                    //Obtener los enunciados y respuestas hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearPreguntaAsociar("", "Enunciado 6.4", "Enum1", "Enum2",
                            "Enum3", "Enum4", "Res1", "Res2", "Res3", "Res4");

                case "Completar":
                    //Obtener  el enunciado hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearPreguntaCompletar("", "Enunciado 6.5");

                case "Verdadero/Falso":
                    //Obtener los enunciados y respuestas hasta el momento iran quemados, el unico que ira como vacio es numPreg

                    crearMetodoVerdaderoFalso("", "Enunciado 6.6", "id001", "id002");
            }

            i++;
        }
    }

}

    // Obtener el contenedor de la pantalla
