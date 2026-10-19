package cl.ipvg.georegistro;

public class Incidencia {
    private String titulo;
    private String descripcion;
    private String categoria;
    private double latitud;
    private double longitud;
    private long timestamp;

    // Constructor vacío requerido por Firebase
    public Incidencia() {}

    public Incidencia(String titulo, String descripcion, String categoria, double latitud, double longitud, long timestamp) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.timestamp = timestamp;
    }

    // Getters
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public String getCategoria() { return categoria; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
    public long getTimestamp() { return timestamp; }
}
