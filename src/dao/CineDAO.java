package dao;
import modelo.Conexion;
import modelo.Pelicula;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class CineDAO {
    private Conexion conexion = new Conexion();

    public boolean registrarPelicula(Pelicula p) {
        String sql = "INSERT INTO peliculas (titulo, genero, duracion, clasificacion) VALUES (?, ?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getTitulo());
            ps.setString(2, p.getGenero());
            ps.setInt(3, p.getDuracion());
            ps.setString(4, p.getClasificacion());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al registrar película: " + e.getMessage());
            return false;
        }
    }

    public List<Pelicula> listarPeliculas() {
        List<Pelicula> lista = new ArrayList<>();
        String sql = "SELECT * FROM peliculas";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pelicula p = new Pelicula(rs.getInt("id"), rs.getString("titulo"), rs.getString("genero"), rs.getInt("duracion"), rs.getString("clasificacion"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar películas: " + e.getMessage());
        }
        return lista;
    }

    public boolean eliminarPelicula(int id) {
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al eliminar película (Posible dependencia en funciones): " + e.getMessage());
            return false;
        }
    }

    public DefaultTableModel obtenerCartelera() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID Función");
        modelo.addColumn("Película");
        modelo.addColumn("Sala");
        modelo.addColumn("Fecha/Hora");
        modelo.addColumn("Precio");
        
        String sql = "SELECT f.id, p.titulo, s.nombre, f.fecha_hora, f.precio FROM funciones f " +
                     "JOIN peliculas p ON f.id_pelicula = p.id " +
                     "JOIN salas s ON f.id_sala = s.id";
                     
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] fila = new Object[5];
                fila[0] = rs.getInt("id");
                fila[1] = rs.getString("titulo");
                fila[2] = rs.getString("nombre");
                fila[3] = rs.getString("fecha_hora");
                fila[4] = rs.getDouble("precio");
                modelo.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cartelera: " + e.getMessage());
        }
        return modelo;
    }

    public boolean asientoOcupado(int idFuncion, String asiento) {
        String sql = "SELECT COUNT(*) FROM reservas WHERE id_funcion = ? AND asiento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            ps.setString(2, asiento);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    public int registrarClienteSiNoExiste(String documento, String nombre, String correo) {
        String sqlBusqueda = "SELECT id FROM clientes WHERE documento = ?";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sqlBusqueda)) {
            ps.setString(1, documento);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
            
            String sqlInsert = "INSERT INTO clientes (documento, nombre, correo) VALUES (?, ?, ?)";
            try (PreparedStatement psIns = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
                psIns.setString(1, documento);
                psIns.setString(2, nombre);
                psIns.setString(3, correo);
                psIns.executeUpdate();
                ResultSet rsKey = psIns.getGeneratedKeys();
                if (rsKey.next()) return rsKey.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return -1;
    }

    public boolean registrarReserva(int idFuncion, int idCliente, String asiento) {
        String sql = "INSERT INTO reservas (id_funcion, id_cliente, asiento) VALUES (?, ?, ?)";
        try (Connection con = conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idFuncion);
            ps.setInt(2, idCliente);
            ps.setString(3, asiento);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.err.println("Error de BD al reservar: " + e.getMessage());
            return false;
        }
    }
}
