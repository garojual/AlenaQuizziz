package com.uniquindio.alena.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Stage;

import java.io.IOException;

public class ShowAlert {
    public void advertencia(String message) {
        Alert.AlertType alertType = Alert.AlertType.WARNING;
        Alert alert = new Alert(alertType);
        alert.setTitle("Advertencia");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void error(String message) {
        Alert.AlertType alertType = Alert.AlertType.ERROR;
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void mostrarAdvertencia(String rutaPantallaCancel, String rutaPantallaContinuar, Object controladorPantallaCancel, Object controladorPantallaContinuar, String advertencia) {
        Stage stage = new Stage();
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(stage);
        dialog.setTitle("Advertencia");
        dialog.setHeaderText(advertencia);

        ButtonType cancelButtonType = new ButtonType("Cancelar");
        ButtonType continueButtonType = new ButtonType("Continuar");

        dialog.getDialogPane().getButtonTypes().addAll(cancelButtonType, continueButtonType);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == cancelButtonType) {
                mostrarPantalla(rutaPantallaCancel, controladorPantallaCancel);
            } else if (buttonType == continueButtonType) {
                mostrarPantalla(rutaPantallaContinuar, controladorPantallaContinuar);
            }
            return null;
        });

        dialog.showAndWait();
    }

    public void mostrarPantalla(String rutaPantalla, Object controlador) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaPantalla));
            loader.setController(controlador);
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
