package com.uniquindio.alena.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Random;
import java.util.ResourceBundle;

public class CrearPreguntaController implements Initializable {
    private DataBaseConnection databaseConnection;
    private ShowAlert showAlert = new ShowAlert();
    private static final String SQL_TEMAS = "SELECT NOMBRE_TEMA, ID_TEMA FROM tema";

    @FXML
    private ComboBox<String> tipoPregunta;

    @FXML
    private ComboBox<String> temaPregunta;

    @FXML
    private RadioButton rdbttnPrivada;

    @FXML
    private RadioButton rdbttnPublica;

    private SharedData sharedData = SharedData.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            databaseConnection = new DataBaseConnection();
            Connection connection = databaseConnection.getConnection();
            ObservableList<String> temas = getTemasFromVista(connection);
            temaPregunta.setItems(temas);
            ObservableList<String> tiposPregunta = FXCollections.observableArrayList(
                    "Unica respuesta",
                    "Verdadero/Falso",
                    "Multiple respuesta",
                    "Emparejar",
                    "Ordenar",
                    "Completar",
                    "Pregunta padre"
            );
            tipoPregunta.setItems(tiposPregunta);

            ToggleGroup ppToogleGroup = new ToggleGroup();
            rdbttnPublica.setToggleGroup(ppToogleGroup);
            addActionRadioButton(rdbttnPublica);
            rdbttnPrivada.setToggleGroup(ppToogleGroup);
            addActionRadioButton(rdbttnPrivada);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addActionRadioButton(RadioButton radioButton){
        radioButton.setOnAction(e ->{
            if(radioButton.getText().toString().equals("Privada")){
                sharedData.setSeleccionURCorrecta(2);
            }
            else {
                sharedData.setSeleccionURCorrecta(1);
            }
        });
    }

    private ObservableList<String> getTemasFromVista(Connection connection) throws SQLException {
        ObservableList<String> temas = FXCollections.observableArrayList();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQL_TEMAS);
        while (resultSet.next()) {
            String tema = resultSet.getString("nombre_tema");
            int id_pregunta = resultSet.getInt("ID_TEMA");
            temas.add(tema);
            sharedData.putSeleccion(tema,id_pregunta);
        }
        return temas;
    }

    @FXML
    private void onContinueButtonClicked(ActionEvent event) {
        String selectedTema = temaPregunta.getValue();
        String selectedTipoPregunta = tipoPregunta.getValue();

        if (selectedTema != null && selectedTipoPregunta != null) {
            SharedData sharedData = SharedData.getInstance();
            sharedData.setSelectedTemaPregunta(selectedTema);
            sharedData.setSelectedTipoPregunta(selectedTipoPregunta);
            sharedData.putSeleccion(selectedTipoPregunta, sharedData.getSeleccion(selectedTema));


            switch (selectedTipoPregunta){
                case "Unica respuesta":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_unica_respuesta.fxml"));
                    Parent root = loader.load();

                    // Mostrar la nueva pantalla
                    Stage stage = (Stage) tipoPregunta.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                    break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                case "Multiple respuesta":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_mult_resp.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "Verdadero/Falso":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_v_f.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "Emparejar":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_asociar.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "Ordenar":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_ordenar.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "Completar":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_completar.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                case "Pregunta padre":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_pregunta_padre.fxml"));
                        Parent root = loader.load();

                        // Mostrar la nueva pantalla
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new Scene(root));
                        stage.show();
                        break;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
            }

        } else {
            showAlert.error("Debe seleccionar un tema y un tipo de pregunta.");
        }
    }



    public void setDatabaseConnection(DataBaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }
}
