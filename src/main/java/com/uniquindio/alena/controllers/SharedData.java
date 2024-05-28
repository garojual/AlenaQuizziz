    package com.uniquindio.alena.controllers;

    import com.dlsc.formsfx.model.validators.IntegerRangeValidator;

    import java.util.HashMap;
    import java.util.Map;

    //Shared data
    public class SharedData {
        private static SharedData instance;
        private String selectedTemaPregunta;
        private String selectedTipoPregunta;
        private String selectedTemaExamen;
        private String selectedCategoria;
        private int numPreguntas;
        private int pregPorAlumno;
        private final Map<String, String> seleccion;
        private Map<String, Integer> temasMapExam = new HashMap<>();
        private Map<String, Integer> categoriasMapExam = new HashMap<>();
        private Map<String,Integer> preguntasMap= new HashMap<>();
        private Map<String,Integer> preguntasHijasMap = new HashMap<>();
        private String docenteId = "1202589320";
        private int idCurso= 1;
        private String alumnoId;
        private int idExamen;
        private int numSubPreguntas;
        private boolean isPadre = false;
        private int seleccionURCorrecta;


        private SharedData() {
            seleccion = new HashMap<>();
        }

        public int getSeleccionURCorrecta() {
            return seleccionURCorrecta;
        }

        public void setSeleccionURCorrecta(int seleccionURCorrecta) {
            this.seleccionURCorrecta = seleccionURCorrecta;
        }

        public static synchronized SharedData getInstance() {
            if (instance == null) {
                instance = new SharedData();
            }
            return instance;
        }

        public int getNumSubPreguntas() {
            return numSubPreguntas;
        }



        public void setNumSubPreguntas(int numSubPreguntas) {
            this.numSubPreguntas = numSubPreguntas;
        }

        public String getSelectedTemaPregunta() {
            return selectedTemaPregunta;
        }

        public void setSelectedTemaPregunta(String selectedTemaPregunta) {
            this.selectedTemaPregunta = selectedTemaPregunta;
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

        public String getSelectedTemaExamen() {
            return selectedTemaExamen;
        }

        public void setSelectedTemaExamen(String selectedTemaExamen) {
            this.selectedTemaExamen = selectedTemaExamen;
        }

        public Integer getTemasMapExam(String key) {
            return temasMapExam.get(key);
        }

        public void setTemasMapExam(String tema, Integer id) {
            this.temasMapExam.put(tema,id);
        }

        public int getCategoriasMapExam(String key) {
            return categoriasMapExam.get(key);
        }

        public void setCategoriasMapExam(String categoria,Integer id) {
            this.categoriasMapExam.put(categoria,id);
        }

        public String getDocenteId() {
            return docenteId;
        }


        public String getAlumnoId() {
            return alumnoId;
        }

        public int getIdExamen() {
            return idExamen;
        }

        public void setIdExamen(int idExamen) {
            this.idExamen = idExamen;
        }

        public String getSelectedCategoria() {
            return selectedCategoria;
        }

        public void setSelectedCategoria(String selectedCategoria) {
            this.selectedCategoria = selectedCategoria;
        }

        public int getNumPreguntas() {
            return numPreguntas;
        }

        public void setNumPreguntas(int numPreguntas) {
            this.numPreguntas = numPreguntas;
        }

        public int getPregPorAlumno() {
            return pregPorAlumno;
        }

        public void setPregPorAlumno(int pregPorAlumno) {
            this.pregPorAlumno = pregPorAlumno;
        }

        public int getIdCurso() {
            return idCurso;
        }

        public void setIdCurso(int idCurso) {
            this.idCurso = idCurso;
        }

        public Integer getPreguntasMap(String key) {
            return preguntasMap.get(key);
        }

        public void setPreguntasMap(String key, Integer value) {
            this.preguntasMap.put(key,value);
        }

        public void setAlumnoId(String alumnoId) {
            this.alumnoId = alumnoId;
        }

        public Map<String, Integer> getPreguntasHijasMap() {
            return preguntasHijasMap;
        }

        public void setPreguntasHijasMap(String key, Integer value) {
            this.preguntasHijasMap.put(key,value);
        }

        public Map<String, Integer> getPreguntasHijasMapa() {
            return preguntasHijasMap;
        }

        public void setPreguntasHijasMapa(Map<String, Integer> preguntasHijasMapa) {
            this.preguntasHijasMap = preguntasHijasMapa;
        }

        public boolean isPadre() {
            return isPadre;
        }

        public void setPadre(boolean padre) {
            isPadre = padre;
        }

        public Map<String, Integer> getPreguntasMapa(){
            return preguntasMap;
        }
    }
