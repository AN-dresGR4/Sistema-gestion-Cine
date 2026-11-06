package modelo;
public class Pelicula {
    private int id;
    private String titulo;
    private String genero;
    private int duracion;
    private String clasificacion;

    public Pelicula() {}
    public Pelicula(int id, String titulo, String genero, int duracion, String clasificacion) {
        this.id = id; this.titulo = titulo; this.genero = genero; this.duracion = duracion; this.clasificacion = clasificacion;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }
    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
    @Override
    public String toString() { return titulo; }
}
