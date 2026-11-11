package dao;
import modelo.Conexion;
import modelo.Pelicula;
import modelo.Reserva;
import modelo.Sala;
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
    public boolean actualizarPelicula(Pelicula p) {
        String sql = "UPDATE peliculas SET titulo = ?, genero = ?, duracion = ?, clasificacion = ? WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo()); ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion()); ps.setString(4, p.getClasificacion()); ps.setInt(5, p.getId());
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public List<Pelicula> listarPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getInt("duracion"), rs.getString("clasificacion")));
        } catch (SQLException e) { } return lista;
    }
    public boolean eliminarPelicula(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    // -- 2. SALAS --
    public boolean registrarSala(Sala s) {
        String sql = "INSERT INTO salas (nombre, capacidad) VALUES (?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre()); ps.setInt(2, s.getCapacidad());
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public boolean actualizarSala(Sala s) {
        String sql = "UPDATE salas SET nombre = ?, capacidad = ? WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, s.getNombre()); ps.setInt(2, s.getCapacidad()); ps.setInt(3, s.getId());
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public List<Sala> listarSalas() {
        List<Sala> lista = new ArrayList<>();
        String sql = "SELECT * FROM salas";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(new Sala(rs.getInt("id"), rs.getString("nombre"), rs.getInt("capacidad")));
        } catch (SQLException e) { } return lista;
    }
    public boolean eliminarSala(int id) {
        String sql = "DELETE FROM salas WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id); ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    // -- 3. CARTELERA Y FUNCIONES --
    public boolean registrarFuncion(int idPelicula, int idSala, String fechaHora, double precio) {
        String sql = "INSERT INTO funciones (id_pelicula, id_sala, fecha_hora, precio) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula); ps.setInt(2, idSala); ps.setString(3, fechaHora); ps.setDouble(4, precio);
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public boolean actualizarFuncion(int idFuncion, int idPelicula, int idSala, String fechaHora, double precio) {
        String sql = "UPDATE funciones SET id_pelicula = ?, id_sala = ?, fecha_hora = ?, precio = ? WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPelicula); ps.setInt(2, idSala); ps.setString(3, fechaHora); ps.setDouble(4, precio); ps.setInt(5, idFuncion);
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public boolean eliminarFuncion(int idFuncion) {
        String sql = "DELETE FROM funciones WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion); ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }
    public DefaultTableModel obtenerCartelera() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Función"); modelo.addColumn("Película"); 
        modelo.addColumn("Sala"); modelo.addColumn("Horario"); modelo.addColumn("Precio");
        String sql = "SELECT f.id, p.titulo, s.nombre, f.fecha_hora, f.precio FROM funciones f JOIN peliculas p ON f.id_pelicula = p.id JOIN salas s ON f.id_sala = s.id";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) modelo.addRow(new Object[]{rs.getInt("id"), rs.getString("titulo"), rs.getString("nombre"), rs.getString("fecha_hora"), rs.getDouble("precio")});
        } catch (SQLException e) { } return modelo;
    }
    public List<String> obtenerFuncionesDropdown() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT f.id, p.titulo, f.fecha_hora FROM funciones f JOIN peliculas p ON f.id_pelicula = p.id";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) lista.add(rs.getInt("id") + " - " + rs.getString("titulo") + " (" + rs.getString("fecha_hora") + ")");
        } catch (SQLException e) { } return lista;
    }
    public int obtenerCapacidadSalaPorFuncion(int idFuncion) {
        String sql = "SELECT s.capacidad FROM funciones f JOIN salas s ON f.id_sala = s.id WHERE f.id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getInt("capacidad"); }
        } catch (SQLException e) {}
        return 30; 
    }

    // -- 4. CLIENTES Y BÚSQUEDA AUTOMÁTICA --
    public String[] obtenerDatosCliente(String documento) {
        String sql = "SELECT nombre, correo FROM clientes WHERE documento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, documento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new String[]{rs.getString("nombre"), rs.getString("correo")};
            }
        } catch (SQLException e) {}
        return null;
    }
    public int registrarCliente(String doc, String nom, String cor) {
        String sqlBusqueda = "SELECT id FROM clientes WHERE documento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlBusqueda)) {
            ps.setString(1, doc); ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id"); 
            String sqlInsert = "INSERT INTO clientes (documento, nombre, correo) VALUES (?, ?, ?)";
            try (PreparedStatement psIns = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psIns.setString(1, doc); psIns.setString(2, nom); psIns.setString(3, cor);
                psIns.executeUpdate(); ResultSet rsKey = psIns.getGeneratedKeys();
                if (rsKey.next()) return rsKey.getInt(1);
            }
        } catch (SQLException e) { } return -1;
    }

    // -- 5. RESERVAS (ARRAY Y MAPA) --
    public List<String> obtenerAsientosOcupados(int idFuncion) {
        List<String> ocupados = new ArrayList<>();
        String sql = "SELECT asiento FROM reservas WHERE id_funcion = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) ocupados.add(rs.getString("asiento"));
            }
        } catch (SQLException e) {}
        return ocupados;
    }

    public List<Reserva> listarTodasLasReservas() {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id, r.id_funcion, p.titulo, c.documento, c.nombre, r.asiento " +
                     "FROM reservas r " +
                     "JOIN funciones f ON r.id_funcion = f.id " +
                     "JOIN peliculas p ON f.id_pelicula = p.id " +
                     "JOIN clientes c ON r.id_cliente = c.id";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Reserva(rs.getInt("id"), rs.getInt("id_funcion"), rs.getString("titulo"), rs.getString("documento"), rs.getString("nombre"), rs.getString("asiento")));
            }
        } catch (SQLException e) {}
        return lista;
    }

    public boolean actualizarAsientoReserva(int idReserva, String nuevoAsiento) {
        String sql = "UPDATE reservas SET asiento = ? WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoAsiento); ps.setInt(2, idReserva);
            ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    public boolean eliminarReserva(int idReserva) {
        String sql = "DELETE FROM reservas WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idReserva); ps.execute(); return true;
        } catch (SQLException e) { return false; }
    }

    public boolean registrarReservaMultiple(int func, int cli, List<String> asientos) {
        String sql = "INSERT INTO reservas (id_funcion, id_cliente, asiento) VALUES (?, ?, ?)";
        try (Connection con = conexion.getConexion()) {
            con.setAutoCommit(false); // Transacción segura
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (String as : asientos) {
                    ps.setInt(1, func); ps.setInt(2, cli); ps.setString(3, as);
                    ps.executeUpdate();
                }
                con.commit(); return true;
            } catch (SQLException ex) {
                con.rollback(); return false;
            }
        } catch (SQLException e) { return false; }
    }
}