package dao;
import modelo.Conexion;
import modelo.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CineDAO {
    private Conexion conexion = new Conexion();

    // -- 1. PELÍCULAS --
    public boolean registrarPelicula(Pelicula p) {
        String sql = "INSERT INTO peliculas (titulo, genero, duracion, clasificacion) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo()); ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion()); ps.setString(4, p.getClasificacion());
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    public List<Pelicula> listarPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getInt("duracion"), rs.getString("clasificacion")));
            }
        } catch (SQLException e) { }
        return lista;
    }

    public boolean eliminarPelicula(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    // -- 2. CARTELERA Y FUNCIONES --
    public boolean registrarFuncion(int idPelicula, int idSala, String fechaHora, double precio) {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula); ps.setInt(2, idSala);
            ps.setString(3, fechaHora); ps.setDouble(4, precio);
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    public DefaultTableModel obtenerCartelera() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Función"); modelo.addColumn("Película"); 
        modelo.addColumn("Sala"); modelo.addColumn("Horario"); modelo.addColumn("Precio");
        
        String sql = "SELECT f.id, p.titulo, s.nombre, f.fecha_hora, f.precio " +
                     "FROM funciones f JOIN peliculas p ON f.id_pelicula = p.id JOIN salas s ON f.id_sala = s.id";
        
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt("id"), rs.getString("titulo"), rs.getString("nombre"), rs.getString("fecha_hora"), rs.getDouble("precio")});
            }
        } catch (SQLException e) { }
        return modelo;
    }
    
    // MÉTODO NUEVO PARA LLENAR EL SELECT (COMBOBOX)
    public List<String> obtenerFuncionesDropdown() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT f.id, p.titulo, f.fecha_hora FROM funciones f JOIN peliculas p ON f.id_pelicula = p.id";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // Formato: "ID - Título de película (Fecha)"
                lista.add(rs.getInt("id") + " - " + rs.getString("titulo") + " (" + rs.getString("fecha_hora") + ")");
            }
        } catch (SQLException e) { }
        return lista;
    }

    // -- 3. RESERVAS Y CLIENTES --
    public boolean asientoOcupado(int idFuncion, String asiento) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE id_funcion = ? AND asiento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion); ps.setString(2, asiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { }
        return false;
    }

    public int registrarCliente(String doc, String nom, String cor) {
        String sqlBusqueda = "SELECT id FROM clientes WHERE documento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlBusqueda)) {
            ps.setString(1, doc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id"); 
            
            String sqlInsert = "INSERT INTO clientes (documento, nombre, correo) VALUES (?, ?, ?)";
            try (PreparedStatement psIns = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psIns.setString(1, doc); psIns.setString(2, nom); psIns.setString(3, cor);
                psIns.executeUpdate();
                ResultSet rsKey = psIns.getGeneratedKeys();
                if (rsKey.next()) return rsKey.getInt(1);
            }
        } catch (SQLException e) { }
        return -1;
    }

    public boolean registrarReserva(int func, int cli, String as) {
        String sql = "INSERT INTO reservas (id_funcion, id_cliente, asiento) VALUES (?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, func); ps.setInt(2, cli); ps.setString(3, as);
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
}