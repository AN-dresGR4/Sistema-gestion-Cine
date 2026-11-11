package controlador;
import dao.CineDAO;
import modelo.Pelicula;
import modelo.Sala;
import vista.DialogoAsientos;
import vista.VentanaPrincipal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class CineControlador implements ActionListener {
    private VentanaPrincipal vista;
    private CineDAO dao;

    public CineControlador(VentanaPrincipal vista, CineDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        this.vista.btnAct.addActionListener(this);
        this.vista.btnGuardarPeli.addActionListener(this);
        this.vista.btnEditarPeli.addActionListener(this);     // Nuevo Listener Editar Película
        this.vista.btnEliminarPeli.addActionListener(this);
        this.vista.btnGuardarFuncion.addActionListener(this);
        
        // Listeners Reservas
        this.vista.btnSeleccionarAsientos.addActionListener(this);
        this.vista.btnReservar.addActionListener(this);
        
        // Listeners Salas
        this.vista.btnGuardarSala.addActionListener(this);
        this.vista.btnEliminarSala.addActionListener(this);
        this.vista.btnCargarEditarSala.addActionListener(this);
        
        cargarDatos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAct) cargarDatos();
        else if (e.getSource() == vista.btnGuardarPeli) guardarPelicula();
        else if (e.getSource() == vista.btnEditarPeli) cargarPeliculaParaEditar(); // Acción Editar Película
        else if (e.getSource() == vista.btnEliminarPeli) eliminarPelicula();
        else if (e.getSource() == vista.btnGuardarFuncion) guardarFuncion();
        else if (e.getSource() == vista.btnSeleccionarAsientos) abrirMapaSillas();
        else if (e.getSource() == vista.btnReservar) procesarReserva();
        else if (e.getSource() == vista.btnGuardarSala) guardarSala();
        else if (e.getSource() == vista.btnEliminarSala) eliminarSala();
        else if (e.getSource() == vista.btnCargarEditarSala) cargarSalaParaEditar();
    }

    private void cargarDatos() {
        vista.tablaCartelera.setModel(dao.obtenerCartelera());
        
        List<Pelicula> peliculas = dao.listarPeliculas();
        DefaultTableModel modPelis = new DefaultTableModel();
        modPelis.addColumn("ID"); modPelis.addColumn("Título"); modPelis.addColumn("Género");
        modPelis.addColumn("Duración"); modPelis.addColumn("Clasificación");
        vista.cbPeliculas.removeAllItems();
        for (Pelicula p : peliculas) {
            modPelis.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero(), p.getDuracion(), p.getClasificacion()});
            vista.cbPeliculas.addItem(p.getId() + " - " + p.getTitulo());
        }
        vista.tablaPeliculas.setModel(modPelis);
        
        List<Sala> salas = dao.listarSalas();
        DefaultTableModel modSalas = new DefaultTableModel();
        modSalas.addColumn("ID"); modSalas.addColumn("Nombre de Sala"); modSalas.addColumn("Capacidad");
        vista.cbSalas.removeAllItems();
        for (Sala s : salas) {
            modSalas.addRow(new Object[]{s.getId(), s.getNombre(), s.getCapacidad()});
            vista.cbSalas.addItem(s.getId() + " - " + s.getNombre() + " (" + s.getCapacidad() + " Asientos)");
        }
        vista.tablaSalas.setModel(modSalas);
        
        vista.cbFunciones.removeAllItems();
        List<String> funciones = dao.obtenerFuncionesDropdown();
        for (String f : funciones) vista.cbFunciones.addItem(f);
    }

    // --- LÓGICA DE PELÍCULAS (GUARDAR / EDITAR / ELIMINAR) ---
    private void guardarPelicula() {
        if (vista.txtTit.getText().trim().isEmpty() || vista.txtGen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El título y género son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        try {
            int duracion = Integer.parseInt(vista.txtDur.getText());
            String titulo = vista.txtTit.getText();
            String genero = vista.txtGen.getText();
            String clasificacion = vista.txtClas.getText();
            String idTexto = vista.txtIdPeliEdit.getText();
            
            if (idTexto.isEmpty()) {
                // Crear nueva
                dao.registrarPelicula(new Pelicula(0, titulo, genero, duracion, clasificacion));
                JOptionPane.showMessageDialog(vista, "Película guardada con éxito.");
            } else {
                // Actualizar existente
                int id = Integer.parseInt(idTexto);
                dao.actualizarPelicula(new Pelicula(id, titulo, genero, duracion, clasificacion));
                JOptionPane.showMessageDialog(vista, "Película actualizada correctamente.");
            }
            
            // Limpiar campos
            vista.txtIdPeliEdit.setText(""); vista.txtTit.setText(""); 
            vista.txtGen.setText(""); vista.txtDur.setText(""); vista.txtClas.setText("");
            cargarDatos();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "La duración debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarPeliculaParaEditar() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una película de la tabla para editar.");
            return;
        }
        vista.txtIdPeliEdit.setText(vista.tablaPeliculas.getValueAt(fila, 0).toString());
        vista.txtTit.setText(vista.tablaPeliculas.getValueAt(fila, 1).toString());
        vista.txtGen.setText(vista.tablaPeliculas.getValueAt(fila, 2).toString());
        vista.txtDur.setText(vista.tablaPeliculas.getValueAt(fila, 3).toString());
        vista.txtClas.setText(vista.tablaPeliculas.getValueAt(fila, 4).toString());
    }

    private void eliminarPelicula() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una película."); return; }
        if (dao.eliminarPelicula((int) vista.tablaPeliculas.getValueAt(fila, 0))) {
            JOptionPane.showMessageDialog(vista, "Película eliminada."); 
            vista.txtIdPeliEdit.setText(""); vista.txtTit.setText(""); vista.txtGen.setText(""); vista.txtDur.setText(""); vista.txtClas.setText("");
            cargarDatos();
        } else JOptionPane.showMessageDialog(vista, "No se puede eliminar la película (está en uso en una función).", "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --- LÓGICA DE SALAS ---
    private void guardarSala() {
        if (vista.txtNomSala.getText().trim().isEmpty() || vista.txtCapSala.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El nombre y la capacidad son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        try {
            int capacidad = Integer.parseInt(vista.txtCapSala.getText());
            String nombre = vista.txtNomSala.getText();
            String idTexto = vista.txtIdSalaEdit.getText();
            
            if (idTexto.isEmpty()) dao.registrarSala(new Sala(0, nombre, capacidad));
            else dao.actualizarSala(new Sala(Integer.parseInt(idTexto), nombre, capacidad));
            
            vista.txtNomSala.setText(""); vista.txtCapSala.setText(""); vista.txtIdSalaEdit.setText("");
            cargarDatos();
            JOptionPane.showMessageDialog(vista, "Sala guardada/actualizada con éxito.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "La capacidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void cargarSalaParaEditar() {
        int fila = vista.tablaSalas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una sala de la tabla primero."); return; }
        vista.txtIdSalaEdit.setText(vista.tablaSalas.getValueAt(fila, 0).toString());
        vista.txtNomSala.setText(vista.tablaSalas.getValueAt(fila, 1).toString());
        vista.txtCapSala.setText(vista.tablaSalas.getValueAt(fila, 2).toString());
    }
    private void eliminarSala() {
        int fila = vista.tablaSalas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una sala de la tabla para eliminarla."); return; }
        if (dao.eliminarSala((int) vista.tablaSalas.getValueAt(fila, 0))) {
            JOptionPane.showMessageDialog(vista, "Sala eliminada."); vista.txtNomSala.setText(""); vista.txtCapSala.setText(""); vista.txtIdSalaEdit.setText(""); cargarDatos();
        } else JOptionPane.showMessageDialog(vista, "No se puede eliminar la sala (en uso).", "Error de Integridad", JOptionPane.ERROR_MESSAGE);
    }

    // --- LÓGICA DE FUNCIONES Y RESERVAS ---
    private void guardarFuncion() {
        if (vista.cbPeliculas.getSelectedItem() == null || vista.cbSalas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione una película y una sala.", "Error", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int idPeli = Integer.parseInt(((String) vista.cbPeliculas.getSelectedItem()).split(" - ")[0]);
            int idSala = Integer.parseInt(((String) vista.cbSalas.getSelectedItem()).split(" - ")[0]);
            
            if (dao.registrarFuncion(idPeli, idSala, vista.txtFecha.getText(), Double.parseDouble(vista.txtPrecio.getText()))) {
                JOptionPane.showMessageDialog(vista, "¡Función agregada a la cartelera con éxito!"); cargarDatos(); 
            } else JOptionPane.showMessageDialog(vista, "Error al guardar en la BD.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(vista, "El precio debe ser válido.", "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void abrirMapaSillas() {
        if (vista.cbFunciones.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione primero una Función.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFuncion = Integer.parseInt(((String) vista.cbFunciones.getSelectedItem()).split(" - ")[0]);
        int capacidadSala = dao.obtenerCapacidadSalaPorFuncion(idFuncion);
        List<String> ocupados = dao.obtenerAsientosOcupados(idFuncion);
        
        DialogoAsientos dialog = new DialogoAsientos(vista, capacidadSala, ocupados);
        dialog.setVisible(true); 
        
        List<String> seleccionados = dialog.getSeleccionados();
        if (!seleccionados.isEmpty()) {
            vista.txtAsientosSel.setText(seleccionados.toString()); 
        } else {
            vista.txtAsientosSel.setText("");
        }
    }

    private void procesarReserva() {
        if (vista.txtAsientosSel.getText().isEmpty() || vista.txtDoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete los datos y seleccione asientos en el mapa.", "Advertencia", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int idFuncion = Integer.parseInt(((String) vista.cbFunciones.getSelectedItem()).split(" - ")[0]);
            
            String sillasString = vista.txtAsientosSel.getText().replace("[", "").replace("]", "");
            List<String> listaAsientos = Arrays.asList(sillasString.split(", "));
            
            String doc = vista.txtDoc.getText();
            int idCliente = dao.registrarCliente(doc, vista.txtNom.getText(), vista.txtCor.getText());
            
            if (idCliente != -1 && dao.registrarReservaMultiple(idFuncion, idCliente, listaAsientos)) {
                JOptionPane.showMessageDialog(vista, "¡RESERVA EXITOSA!\nBoletos: " + listaAsientos.size() + "\nSillas: " + listaAsientos.toString(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.txtAsientosSel.setText(""); 
            } else {
                JOptionPane.showMessageDialog(vista, "No se pudo guardar la reserva. Quizás un asiento se ocupó mientras lo seleccionaba.", "Error de Sincronización", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(vista, "Error al procesar reserva.", "Error", JOptionPane.ERROR_MESSAGE); }
    }
}