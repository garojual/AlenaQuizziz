package com.uniquindio.alena.controllers;

import java.util.HashMap;
import java.util.Map;

public class SharedData {
    private static SharedData instance;
    private String selectedTema;
    private String selectedTipoPregunta;
    private final Map<String, String> seleccion;

    private SharedData() {
        seleccion = new HashMap<>();
    }

    public static synchronized SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public String getSelectedTema() {
        return selectedTema;
    }

    public void setSelectedTema(String selectedTema) {
        this.selectedTema = selectedTema;
    }

    public String getSelectedTipoPregunta() {
        return selectedTipoPregunta;
    }

    public void setSelectedTipoPregunta(String selectedTipoPregunta) {
        this.selectedTipoPregunta = selectedTipoPregunta;
    }

    public Map<String, String> getSeleccion() {
        return seleccion;
    }

    public void putSeleccion(String key, String value) {
        seleccion.put(key, value);
    }

    public String getSeleccion(String key) {
        return seleccion.get(key);
    }
}
