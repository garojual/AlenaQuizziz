package com.uniquindio.alena;

import java.sql.*;
import oracle.jdbc.driver.OracleDriver;

public class DataBaseConnection {

    public static void main(String[] args) {
        Connection jdbcConnection = null;
        try {
            // Cargar el controlador JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Establecer la conexión con la base de datos
            String url = "jdbc:oracle:thin:@localhost:1521:XE";
            String user = "BD2";
            String password = "1234";
            jdbcConnection = DriverManager.getConnection(url, user, password);

            if (jdbcConnection != null) {
                System.out.println("Conexión establecida con la base de datos Oracle.");
            } else {
                System.out.println("Fallo al establecer la conexión.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            // Cerrar la conexión
            if (jdbcConnection != null) {
                try {
                    jdbcConnection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
