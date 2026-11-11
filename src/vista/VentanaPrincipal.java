package vista;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public JTabbedPane pestanas;
    public JTable tablaCartelera, tablaPeliculas;
    
    // Botones
    public JButton btnAct, btnGuardar, btnEliminar, btnReservar, btnGuardarFuncion;
    
    // Campos Película
    public JTextField txtTit, txtGen, txtDur, txtClas;
    
    // Campos Función (AQUÍ CAMBIAMOS EL TXT POR EL COMBOBOX)
    public JComboBox<String> cbPeliculas; 
    public JTextField txtIdSala, txtFecha, txtPrecio;
    
    // Campos Reserva
    public JComboBox<String> cbFunciones; 
    public JTextField txtAsi, txtDoc, txtNom, txtCor;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Cine");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        pestanas = new JTabbedPane();
        
        // --- PESTAÑA 1: Cartelera ---
        JPanel p1 = new JPanel(new BorderLayout());
        tablaCartelera = new JTable();
        btnAct = new JButton("Actualizar Cartelera");
        p1.add(new JScrollPane(tablaCartelera), BorderLayout.CENTER);
        p1.add(btnAct, BorderLayout.SOUTH);
        
        // --- PESTAÑA 2: Gestión (Películas y Funciones) ---
        JPanel p2 = new JPanel(new BorderLayout());
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel fPeli = new JPanel(new GridLayout(6, 2, 5, 5));
        fPeli.setBorder(BorderFactory.createTitledBorder("1. Nueva Película"));
        txtTit = new JTextField(); txtGen = new JTextField(); 
        txtDur = new JTextField(); txtClas = new JTextField();
        btnGuardar = new JButton("Guardar Película"); 
        btnEliminar = new JButton("Eliminar Película");
        fPeli.add(new JLabel("Título:")); fPeli.add(txtTit);
        fPeli.add(new JLabel("Género:")); fPeli.add(txtGen);
        fPeli.add(new JLabel("Duración (min):")); fPeli.add(txtDur);
        fPeli.add(new JLabel("Clasificación:")); fPeli.add(txtClas);
        fPeli.add(btnGuardar); fPeli.add(btnEliminar);
        
        JPanel fFunc = new JPanel(new GridLayout(6, 2, 5, 5));
        fFunc.setBorder(BorderFactory.createTitledBorder("2. Programar Función (Cartelera)"));
        
        cbPeliculas = new JComboBox<>(); // Inicializamos el select de películas
        txtIdSala = new JTextField(); 
        txtFecha = new JTextField("2026-11-10 15:00:00"); txtPrecio = new JTextField("10000");
        btnGuardarFuncion = new JButton("Agregar a Cartelera");
        
        fFunc.add(new JLabel("Seleccionar Película:")); fFunc.add(cbPeliculas);
        fFunc.add(new JLabel("ID Sala (1 o 2):")); fFunc.add(txtIdSala);
        fFunc.add(new JLabel("Fecha/Hora:")); fFunc.add(txtFecha);
        fFunc.add(new JLabel("Precio entrada:")); fFunc.add(txtPrecio);
        fFunc.add(new JLabel("")); fFunc.add(btnGuardarFuncion);
        
        panelFormularios.add(fPeli); panelFormularios.add(fFunc);
        
        tablaPeliculas = new JTable();
        p2.add(panelFormularios, BorderLayout.NORTH);
        p2.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);
        
        // --- PESTAÑA 3: Reservas ---
        JPanel p3 = new JPanel(new GridLayout(7, 2, 10, 10));
        p3.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        cbFunciones = new JComboBox<>(); 
        txtAsi = new JTextField(); txtDoc = new JTextField(); 
        txtNom = new JTextField(); txtCor = new JTextField(); 
        btnReservar = new JButton("Confirmar Reserva / Generar Entrada");
        btnReservar.setBackground(new Color(34, 139, 34));
        btnReservar.setForeground(Color.WHITE);
        
        p3.add(new JLabel("Seleccionar Función:")); p3.add(cbFunciones);
        p3.add(new JLabel("Asiento (Ej: A1, B4):")); p3.add(txtAsi);
        p3.add(new JLabel("Documento Cliente:")); p3.add(txtDoc);
        p3.add(new JLabel("Nombre Cliente:")); p3.add(txtNom);
        p3.add(new JLabel("Correo Cliente:")); p3.add(txtCor);
        p3.add(new JLabel("")); p3.add(btnReservar);
        
        pestanas.addTab("Inicio / Cartelera", p1);
        pestanas.addTab("Gestión de Catálogo", p2);
        pestanas.addTab("Reservas / Entradas", p3);
        add(pestanas);
    }
}