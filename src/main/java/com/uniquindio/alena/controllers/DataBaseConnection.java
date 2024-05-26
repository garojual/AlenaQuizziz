package com.uniquindio.alena.controllers;

import java.sql.*;

public class DataBaseConnection {

    public Connection getConnection() throws SQLException {
        Connection jdbcConnection = null;
        // Establecer la conexión con la base de datos
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String user = "sys as sysdba";
        String password = "1234";
        try {
            // Cargar el controlador JDBC
            Class.forName("oracle.jdbc.driver.OracleDriver");
            // Obtener la conexión y asignarla a jdbcConnection
            jdbcConnection = DriverManager.getConnection(url, user, password);

            if (jdbcConnection != null) {
                System.out.println("Conexión establecida con la base de datos Oracle.");
            } else {
                System.out.println("Fallo al establecer la conexión.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return jdbcConnection; // Devolver la conexión establecida
    }
}
