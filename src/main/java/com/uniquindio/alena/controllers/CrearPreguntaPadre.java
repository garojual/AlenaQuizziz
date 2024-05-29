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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ResourceBundle;

public class CrearPreguntaPadre implements Initializable {

    @FXML
    ComboBox<Integer> numPreguntas;

    @FXML
    TextArea enunciadoTextArea;

    DataBaseConnection dataBaseConnection =new DataBaseConnection();

    SharedData sharedData = SharedData.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        crearActionComboBox(numPreguntas);

    }

    private void crearActionComboBox(ComboBox comboBox){
        ObservableList<Integer> num = FXCollections.observableArrayList(
                1,2,3,4,5
        );
        numPreguntas.setItems(num);
        comboBox.setOnAction(e -> {
            sharedData.setNumSubPreguntas((Integer)(comboBox.getSelectionModel().getSelectedItem()));
        });
    }

    @FXML
    private void aceptar (ActionEvent event){
        try {
            String ADD_QUESTION = "{ ? = call add_pregunta(?, ?, ?, ?, ?, ?, ?) }";
            Connection connection = dataBaseConnection.getConnection();
            CallableStatement callableStatement = connection.prepareCall(ADD_QUESTION);
            callableStatement.registerOutParameter(1, Types.INTEGER);
            callableStatement.setInt(2,sharedData.getSeleccion(sharedData.getSelectedTemaPregunta()));
            callableStatement.setNull(3,Types.INTEGER);
            callableStatement.setString(4,sharedData.getSelectedTipoPregunta());
            callableStatement.setInt(5,sharedData.getSeleccionURCorrecta());
            callableStatement.setString(6, enunciadoTextArea.getText());
            callableStatement.setString(7,sharedData.getDocenteId());
            callableStatement.setString(8,"finalizada");
            callableStatement.execute();
            int id= callableStatement.getInt(1);

            sharedData.setPadre(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/uniquindio/alena/crear_sub_preguntas.fxml"));
            Parent root = loader.load();

            // Mostrar la nueva pantalla
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
