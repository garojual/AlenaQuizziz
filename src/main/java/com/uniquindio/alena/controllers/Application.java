package com.uniquindio.alena.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("/com/uniquindio/alena/inicio.fxml"));
        Parent root = fxmlLoader.load();

        // Obtiene la instancia del controlador
        InicioController  controller = fxmlLoader.getController();

        // Obtener la conexión a la base de datos
        DataBaseConnection connection = new DataBaseConnection();

        // Establece la conexión en el controlador
        controller.setDataBaseConnection(connection);

        Scene scene = new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        stage.setTitle("Alena");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}