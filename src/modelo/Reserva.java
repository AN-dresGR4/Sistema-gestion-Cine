/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Andres
 */
public class Reserva {
    private int id;
    private int idFuncion;
    private String pelicula;
    private String documentoCliente;
    private String nombreCliente;
    private String asiento;

    public Reserva(int id, int idFuncion, String pelicula, String documentoCliente, String nombreCliente, String asiento) {
        this.id = id;
        this.idFuncion = idFuncion;
        this.pelicula = pelicula;
        this.documentoCliente = documentoCliente;
        this.nombreCliente = nombreCliente;
        this.asiento = asiento;
    }

    public int getId() { return id; }
    public int getIdFuncion() { return idFuncion; }
    public String getPelicula() { return pelicula; }
    public String getDocumentoCliente() { return documentoCliente; }
    public String getNombreCliente() { return nombreCliente; }
    public String getAsiento() { return asiento; }
}
