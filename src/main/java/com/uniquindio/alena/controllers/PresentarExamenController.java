package com.uniquindio.alena.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PresentarExamenController implements Initializable {


    public PresentarExamenController() {
    }

    private DataBaseConnection conexionDB; // Conexión a la base de datos
    private ScrollPane root; // Contenedor principal de la pantalla

    public void setDatabaseConnection(DataBaseConnection conexionDB) {
        this.conexionDB = conexionDB;
    }

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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(new URL("../../resources/com/uniquindio/alena/presentar_examen.fxml"));


        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

    // Obtener el contenedor de la pantalla
