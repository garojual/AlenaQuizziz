package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CrearSubPreguntas implements Initializable {

    @FXML
    VBox rootVBox;

    int numSubPreguntas;
    int numPreguntasCreadas;
    ShowAlert showAlert = new ShowAlert();
    SharedData sharedData = SharedData.getInstance();
    DataBaseConnection dataBaseConnection;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /*
        * Obtener el numero de preguntas de la pantalla anterior y asignarlo a numSubPreguntas
        * */

        for (int i = 0; i <sharedData.getNumSubPreguntas(); i++){
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
            Stage stage= (Stage) rootVBox.getScene().getWindow();
            stage.close();
            sharedData.setPadre(null);
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
                                    showAlert.error("El tipo de pregunta no puede ser nulo");
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
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_unica_respuesta.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                case "Múltiple respuesta":
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_mult_resp.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                case "Ordenar":
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_ordenar.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                case "Emparejar":
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_asociar.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                case "Completar":
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_completar.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                case "Verdadero/Falso":
                    try {
                        openNewWindow("/com/uniquindio/alena/crear_pregunta_v_f.fxml");
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
            }

        });

    }
    private void openNewWindow(String ruta) throws Exception {
        Stage newStage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
        AnchorPane vbox = loader.load();
        Scene scene = new Scene(vbox);
        newStage.setScene(scene);

        newStage.show();
    }

    public void setDatabaseConnection(DataBaseConnection connection) {
    }
}
