package com.uniquindio.alena.controllers;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.FlowPane;

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
    private VBox root; // Contenedor principal de la pantalla

    public void setDatabaseConnection(DataBaseConnection conexionDB) {
        this.conexionDB = conexionDB;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root = new VBox(10);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        // Obtener preguntas y respuestas de la base de datos
        String consulta = "SELECT p.id_pregunta, p.enunciado, p.tipo_pregunta, r.id_respuesta, r.enunciado_respuesta " +
                "FROM pregunta p " +
                "LEFT JOIN respuesta r ON p.id_pregunta = r.id_pregunta";

        try  {
            DataBaseConnection dataBaseConnection = new DataBaseConnection();
            Connection connection = dataBaseConnection.getConnection();

            PreparedStatement statement= connection.prepareStatement(consulta);
            ResultSet rs=statement.executeQuery();

            Map<Integer, List<String>> respuestasPorPregunta = new HashMap<>();

            while (rs.next()) {
                int idPregunta = rs.getInt("id_pregunta");
                String enunciadoPregunta = rs.getString("enunciado");
                String tipoPregunta = rs.getString("tipo_pregunta");

                // Obtener respuestas asociadas
                int idRespuesta = rs.getInt("id_respuesta");
                String enunciadoRespuesta = rs.getString("enunciado_respuesta");
                if (idRespuesta != 0) {
                    respuestasPorPregunta.computeIfAbsent(idPregunta, k -> new ArrayList<>())
                            .add(enunciadoRespuesta);
                }

                // Crear control UI para la pregunta
                Control controlPregunta = crearControlPregunta(idPregunta, enunciadoPregunta, tipoPregunta, respuestasPorPregunta.getOrDefault(idPregunta, new ArrayList<>()));
                root.getChildren().add(controlPregunta);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción de manera adecuada
        }
    }

    // Obtener el contenedor de la pantalla
    public VBox getRoot() {
        return root;
    }

    // Método para crear el control UI para cada pregunta
    private Control crearControlPregunta(int idPregunta, String enunciadoPregunta, String tipoPregunta, List<String> respuestas) {
        TitledPane titledPane = new TitledPane();
        titledPane.setText("Pregunta " + idPregunta);

        Label enunciadoLabel = new Label(enunciadoPregunta);
        enunciadoLabel.setWrapText(true);

        switch (tipoPregunta) {
            case "verdadero/falso":
                titledPane.setContent(crearControlVerdaderoFalso(enunciadoLabel));
                break;
            case "unica_respuesta":
                titledPane.setContent(crearControlUnicaRespuesta(enunciadoLabel, respuestas));
                break;
            case "multiple_respuesta":
                titledPane.setContent(crearControlMultipleRespuesta(enunciadoLabel, respuestas));
                break;
            case "emparejar":
                titledPane.setContent(crearControlEmparejar(enunciadoLabel, respuestas));
                break;
            case "ordenar":
                titledPane.setContent(crearControlOrdenar(enunciadoLabel, respuestas));
                break;
            case "completar":
                titledPane.setContent(crearControlCompletar(enunciadoLabel));
                break;
            default:
                titledPane.setContent(new Label("Tipo de pregunta no reconocido"));
        }

        return titledPane;
    }

    // Métodos para crear los controles UI para cada tipo de pregunta (devuelven VBox)
    private VBox crearControlVerdaderoFalso(Label enunciadoLabel) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        ToggleGroup grupo = new ToggleGroup();

        RadioButton verdadero = new RadioButton("Verdadero");
        verdadero.setToggleGroup(grupo);

        RadioButton falso = new RadioButton("Falso");
        falso.setToggleGroup(grupo);

        HBox opciones = new HBox(10);
        opciones.getChildren().addAll(verdadero, falso);
        contenedor.getChildren().add(opciones);

        return contenedor;
    }

    private VBox crearControlUnicaRespuesta(Label enunciadoLabel, List<String> respuestas) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        ToggleGroup grupo = new ToggleGroup();

        for (String respuesta : respuestas) {
            RadioButton opcion = new RadioButton(respuesta);
            opcion.setToggleGroup(grupo);
            contenedor.getChildren().add(opcion);
        }

        return contenedor;
    }

    private VBox crearControlMultipleRespuesta(Label enunciadoLabel, List<String> respuestas) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        for (String respuesta : respuestas) {
            CheckBox opcion = new CheckBox(respuesta);
            contenedor.getChildren().add(opcion);
        }

        return contenedor;
    }

    private VBox crearControlEmparejar(Label enunciadoLabel, List<String> respuestas) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Asegúrate de que el número de opciones sea par para el emparejamiento
        if (respuestas.size() % 2 != 0) {
            respuestas.add("Opción adicional"); // Agrega una opción adicional para completar el par
        }

        for (int i = 0; i < respuestas.size(); i += 2) {
            Label opcion1 = new Label(respuestas.get(i));
            Label opcion2 = new Label(respuestas.get(i + 1));

            grid.add(opcion1, 0, i / 2);
            grid.add(opcion2, 1, i / 2);
        }

        contenedor.getChildren().add(grid);
        return contenedor;
    }

    private VBox crearControlOrdenar(Label enunciadoLabel, List<String> respuestas) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        FlowPane opciones = new FlowPane();
        opciones.setHgap(10);
        opciones.setVgap(10);

        for (String respuesta : respuestas) {
            Label opcion = new Label(respuesta);
            opciones.getChildren().add(opcion);
        }

        contenedor.getChildren().add(opciones);
        return contenedor;
    }

    private VBox crearControlCompletar(Label enunciadoLabel) {
        VBox contenedor = new VBox(10);
        contenedor.getChildren().add(enunciadoLabel);

        TextField campoTexto = new TextField();
        contenedor.getChildren().add(campoTexto);

        return contenedor;
    }
}