package vista;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public JTabbedPane pestanas;
    public JTable tablaCartelera, tablaPeliculas;
    public JButton btnActualizarCartelera, btnGuardarPelicula, btnEliminarPelicula, btnReservar;
    public JTextField txtTitulo, txtGenero, txtDuracion, txtClasificacion;
    public JTextField txtDocCliente, txtNomCliente, txtCorreoCliente, txtAsiento, txtIdFuncionReserva;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Cine - CineCampus");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        pestanas = new JTabbedPane();
        
        // Pestaña 1
        JPanel panelCartelera = new JPanel(new BorderLayout());
        tablaCartelera = new JTable();
        btnActualizarCartelera = new JButton("Actualizar Cartelera");
        panelCartelera.add(new JScrollPane(tablaCartelera), BorderLayout.CENTER);
        panelCartelera.add(btnActualizarCartelera, BorderLayout.SOUTH);
        pestanas.addTab("Inicio / Cartelera", panelCartelera);
        
        // Pestaña 2
        JPanel panelGestion = new JPanel(new BorderLayout());
        JPanel panelFormularioPelicula = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormularioPelicula.setBorder(BorderFactory.createTitledBorder("Gestión de Películas"));
        
        panelFormularioPelicula.add(new JLabel("Título:")); txtTitulo = new JTextField(); panelFormularioPelicula.add(txtTitulo);
        panelFormularioPelicula.add(new JLabel("Género:")); txtGenero = new JTextField(); panelFormularioPelicula.add(txtGenero);
        panelFormularioPelicula.add(new JLabel("Duración (min):")); txtDuracion = new JTextField(); panelFormularioPelicula.add(txtDuracion);
        panelFormularioPelicula.add(new JLabel("Clasificación:")); txtClasificacion = new JTextField(); panelFormularioPelicula.add(txtClasificacion);
        
        btnGuardarPelicula = new JButton("Guardar Película");
        btnEliminarPelicula = new JButton("Eliminar Seleccionada");
        JPanel panelBotonesGestion = new JPanel();
        panelBotonesGestion.add(btnGuardarPelicula);
        panelBotonesGestion.add(btnEliminarPelicula);
        
        JPanel panelSupGestion = new JPanel(new BorderLayout());
        panelSupGestion.add(panelFormularioPelicula, BorderLayout.CENTER);
        panelSupGestion.add(panelBotonesGestion, BorderLayout.SOUTH);
        
        tablaPeliculas = new JTable();
        panelGestion.add(panelSupGestion, BorderLayout.NORTH);
        panelGestion.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);
        pestanas.addTab("Gestión", panelGestion);
        
        // Pestaña 3
        JPanel panelReservas = new JPanel(new GridLayout(6, 2, 10, 10));
        panelReservas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelReservas.add(new JLabel("ID Función (Ver en Cartelera):")); txtIdFuncionReserva = new JTextField(); panelReservas.add(txtIdFuncionReserva);
        panelReservas.add(new JLabel("Asiento (Ej: A1, B4):")); txtAsiento = new JTextField(); panelReservas.add(txtAsiento);
        panelReservas.add(new JLabel("Documento Cliente:")); txtDocCliente = new JTextField(); panelReservas.add(txtDocCliente);
        panelReservas.add(new JLabel("Nombre Cliente:")); txtNomCliente = new JTextField(); panelReservas.add(txtNomCliente);
        panelReservas.add(new JLabel("Correo Cliente:")); txtCorreoCliente = new JTextField(); panelReservas.add(txtCorreoCliente);
        
        btnReservar = new JButton("Confirmar Reserva / Generar Entrada");
        btnReservar.setBackground(new Color(34, 139, 34));
        btnReservar.setForeground(Color.WHITE);
        panelReservas.add(new JLabel("")); 
        panelReservas.add(btnReservar);
        
        pestanas.addTab("Reservas / Entradas", panelReservas);
        add(pestanas);
    }
}
