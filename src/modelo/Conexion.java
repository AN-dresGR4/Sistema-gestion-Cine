package modelo;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private final String URL = "jdbc:mysql://localhost:3306/cine_db";
    private final String USER = "root"; 
    private final String PASSWORD = ""; 
    private Connection con;

    public Connection getConexion() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            return con;
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}
