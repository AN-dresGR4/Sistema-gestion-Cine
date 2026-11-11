package vista;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    public JTabbedPane pestanas;
    public JTable tablaCartelera, tablaPeliculas, tablaSalas, tablaReservas;
    
    // Botones Películas
    public JButton btnGuardarPeli, btnEliminarPeli, btnEditarPeli;
    
    // Botones Cartelera y Funciones
    public JButton btnAct, btnGuardarFuncion, btnEditarFuncion, btnEliminarFuncion;
    public JTextField txtIdFuncionEdit; // Campo oculto para editar función
    
    // Botones Salas
    public JButton btnGuardarSala, btnEliminarSala, btnCargarEditarSala;
    
    // Botones Reservas
    public JButton btnSeleccionarAsientos, btnReservar, btnBuscarCliente;
    public JButton btnEditarReserva, btnEliminarReserva, btnCargarReservaEdit;
    public JTextField txtIdReservaEdit; // Campo oculto para editar reserva
    
    // Campos Película
    public JTextField txtIdPeliEdit, txtTit, txtGen, txtDur, txtClas;
    
    // Campos Función
    public JComboBox<String> cbPeliculas; 
    public JComboBox<String> cbSalas; 
    public JTextField txtFecha, txtPrecio;
    
    // Campos Reserva
    public JComboBox<String> cbFunciones; 
    public JTextField txtAsientosSel, txtDoc, txtNom, txtCor;
    
    // Campos Sala
    public JTextField txtIdSalaEdit, txtNomSala, txtCapSala;

    public VentanaPrincipal() {
        setTitle("Sistema de Gestión de Cine");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        pestanas = new JTabbedPane();
        
        // --- PESTAÑA 1: Cartelera (Actualizada con Editar y Eliminar) ---
        JPanel p1 = new JPanel(new BorderLayout());
        tablaCartelera = new JTable();
        
        JPanel panelBotonesCartelera = new JPanel();
        btnAct = new JButton("Actualizar Cartelera");
        btnEditarFuncion = new JButton("Cargar Función para Editar");
        btnEliminarFuncion = new JButton("Eliminar Función");
        txtIdFuncionEdit = new JTextField(); txtIdFuncionEdit.setVisible(false); // Oculto
        
        panelBotonesCartelera.add(btnAct);
        panelBotonesCartelera.add(btnEditarFuncion);
        panelBotonesCartelera.add(btnEliminarFuncion);
        
        p1.add(new JScrollPane(tablaCartelera), BorderLayout.CENTER);
        p1.add(panelBotonesCartelera, BorderLayout.SOUTH);
        
        // --- PESTAÑA 2: Películas y Funciones ---
        JPanel p2 = new JPanel(new BorderLayout());
        JPanel panelFormularios = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel fPeli = new JPanel(new GridLayout(7, 2, 5, 5));
        fPeli.setBorder(BorderFactory.createTitledBorder("1. Nueva / Editar Película"));
        txtIdPeliEdit = new JTextField(); txtIdPeliEdit.setEditable(false);
        txtTit = new JTextField(); txtGen = new JTextField(); txtDur = new JTextField(); txtClas = new JTextField();
        btnGuardarPeli = new JButton("Guardar"); btnEditarPeli = new JButton("Editar"); btnEliminarPeli = new JButton("Eliminar");
        fPeli.add(new JLabel("ID:")); fPeli.add(txtIdPeliEdit);
        fPeli.add(new JLabel("Título:")); fPeli.add(txtTit);
        fPeli.add(new JLabel("Género:")); fPeli.add(txtGen);
        fPeli.add(new JLabel("Duración (min):")); fPeli.add(txtDur);
        fPeli.add(new JLabel("Clasificación:")); fPeli.add(txtClas);
        fPeli.add(btnGuardarPeli); fPeli.add(btnEditarPeli);
        fPeli.add(btnEliminarPeli); fPeli.add(new JLabel(""));
        
        JPanel fFunc = new JPanel(new GridLayout(7, 2, 5, 5)); // Ajustado a 7 filas
        fFunc.setBorder(BorderFactory.createTitledBorder("2. Programar / Editar Función"));
        cbPeliculas = new JComboBox<>(); cbSalas = new JComboBox<>();
        txtFecha = new JTextField("2026-11-10 15:00:00"); txtPrecio = new JTextField("10000");
        btnGuardarFuncion = new JButton("Guardar / Actualizar Función");
        
        fFunc.add(new JLabel("Película:")); fFunc.add(cbPeliculas);
        fFunc.add(new JLabel("Sala:")); fFunc.add(cbSalas);
        fFunc.add(new JLabel("Fecha/Hora:")); fFunc.add(txtFecha);
        fFunc.add(new JLabel("Precio:")); fFunc.add(txtPrecio);
        fFunc.add(new JLabel("")); fFunc.add(btnGuardarFuncion);
        
        panelFormularios.add(fPeli); panelFormularios.add(fFunc);
        tablaPeliculas = new JTable();
        p2.add(panelFormularios, BorderLayout.NORTH);
        p2.add(new JScrollPane(tablaPeliculas), BorderLayout.CENTER);

        // --- PESTAÑA 3: Salas ---
        JPanel p4 = new JPanel(new BorderLayout());
        JPanel fSala = new JPanel(new GridLayout(5, 2, 5, 5));
        fSala.setBorder(BorderFactory.createTitledBorder("Administración de Salas"));
        txtIdSalaEdit = new JTextField(); txtIdSalaEdit.setEditable(false);
        txtNomSala = new JTextField(); txtCapSala = new JTextField();
        btnGuardarSala = new JButton("Guardar"); btnEliminarSala = new JButton("Eliminar"); btnCargarEditarSala = new JButton("Editar");
        fSala.add(new JLabel("ID:")); fSala.add(txtIdSalaEdit);
        fSala.add(new JLabel("Nombre:")); fSala.add(txtNomSala);
        fSala.add(new JLabel("Capacidad:")); fSala.add(txtCapSala);
        fSala.add(btnGuardarSala); fSala.add(new JLabel("")); 
        fSala.add(btnCargarEditarSala); fSala.add(btnEliminarSala);
        tablaSalas = new JTable();
        p4.add(fSala, BorderLayout.NORTH);
        p4.add(new JScrollPane(tablaSalas), BorderLayout.CENTER);
        
        // --- PESTAÑA 4: Crear Reservas ---
        JPanel p3 = new JPanel(new GridLayout(7, 2, 10, 10));
        p3.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        cbFunciones = new JComboBox<>(); 
        
        JPanel panelAsientos = new JPanel(new BorderLayout(5, 0));
        txtAsientosSel = new JTextField(); txtAsientosSel.setEditable(false); 
        btnSeleccionarAsientos = new JButton("Mapa de Sillas");
        panelAsientos.add(txtAsientosSel, BorderLayout.CENTER);
        panelAsientos.add(btnSeleccionarAsientos, BorderLayout.EAST);
        
        // Panel con botón Buscar Cliente
        JPanel panelDoc = new JPanel(new BorderLayout(5, 0));
        txtDoc = new JTextField();
        btnBuscarCliente = new JButton("Buscar");
        panelDoc.add(txtDoc, BorderLayout.CENTER);
        panelDoc.add(btnBuscarCliente, BorderLayout.EAST);
        
        txtNom = new JTextField(); txtCor = new JTextField(); 
        btnReservar = new JButton("Confirmar Reserva");
        btnReservar.setBackground(new Color(34, 139, 34)); btnReservar.setForeground(Color.WHITE);
        
        p3.add(new JLabel("Función:")); p3.add(cbFunciones);
        p3.add(new JLabel("Asientos:")); p3.add(panelAsientos);
        p3.add(new JLabel("Documento Cliente:")); p3.add(panelDoc); // Añadido el botón buscar
        p3.add(new JLabel("Nombre Cliente:")); p3.add(txtNom);
        p3.add(new JLabel("Correo Cliente:")); p3.add(txtCor);
        p3.add(new JLabel("")); p3.add(btnReservar);

        // --- PESTAÑA 5: Ver, Editar y Eliminar Reservas (NUEVA) ---
        JPanel p5 = new JPanel(new BorderLayout());
        JPanel panelSupRes = new JPanel(new GridLayout(2, 2, 5, 5));
        panelSupRes.setBorder(BorderFactory.createTitledBorder("Gestión de Reservas Existentes"));
        
        txtIdReservaEdit = new JTextField(); txtIdReservaEdit.setVisible(false);
        btnCargarReservaEdit = new JButton("Cargar Reserva para Editar Asiento");
        btnEliminarReserva = new JButton("Eliminar Reserva Seleccionada");
        btnEliminarReserva.setBackground(new Color(220, 53, 69)); btnEliminarReserva.setForeground(Color.WHITE);
        
        panelSupRes.add(btnCargarReservaEdit); panelSupRes.add(btnEliminarReserva);
        
        tablaReservas = new JTable();
        p5.add(panelSupRes, BorderLayout.NORTH);
        p5.add(new JScrollPane(tablaReservas), BorderLayout.CENTER);
        
        // Añadir pestañas al panel principal
        pestanas.addTab("Cartelera", p1);
        pestanas.addTab("Películas y Funciones", p2);
        pestanas.addTab("Salas", p4);
        pestanas.addTab("Hacer Reserva", p3);
        pestanas.addTab("Ver / Editar Reservas", p5); // Nueva pestaña
        add(pestanas);
    }
}