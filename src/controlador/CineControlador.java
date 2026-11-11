package controlador;
import dao.CineDAO;
import modelo.Pelicula;
import modelo.Reserva;
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
        this.vista.btnEditarPeli.addActionListener(this);     
        this.vista.btnEliminarPeli.addActionListener(this);
        this.vista.btnGuardarFuncion.addActionListener(this);
        this.vista.btnEditarFuncion.addActionListener(this);   
        this.vista.btnEliminarFuncion.addActionListener(this); 
        
        // Listeners Reservas
        this.vista.btnSeleccionarAsientos.addActionListener(this);
        this.vista.btnBuscarCliente.addActionListener(this);  
        this.vista.btnReservar.addActionListener(this);
        this.vista.btnCargarReservaEdit.addActionListener(this);
        this.vista.btnEliminarReserva.addActionListener(this);
        
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
        else if (e.getSource() == vista.btnEditarPeli) cargarPeliculaParaEditar(); 
        else if (e.getSource() == vista.btnEliminarPeli) eliminarPelicula();
        else if (e.getSource() == vista.btnGuardarFuncion) guardarOActualizarFuncion();
        else if (e.getSource() == vista.btnEditarFuncion) cargarFuncionParaEditar();
        else if (e.getSource() == vista.btnEliminarFuncion) eliminarFuncion();
        else if (e.getSource() == vista.btnBuscarCliente) buscarCliente(); 
        else if (e.getSource() == vista.btnSeleccionarAsientos) abrirMapaSillas();
        else if (e.getSource() == vista.btnReservar) procesarReserva();
        else if (e.getSource() == vista.btnCargarReservaEdit) editarReservaAsiento();
        else if (e.getSource() == vista.btnEliminarReserva) eliminarReserva();
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

        List<Reserva> reservas = dao.listarTodasLasReservas();
        DefaultTableModel modReservas = new DefaultTableModel();
        modReservas.addColumn("ID Reserva"); modReservas.addColumn("Película"); 
        modReservas.addColumn("Documento Cliente"); modReservas.addColumn("Nombre Cliente"); modReservas.addColumn("Asiento");
        for (Reserva r : reservas) {
            modReservas.addRow(new Object[]{r.getId(), r.getPelicula(), r.getDocumentoCliente(), r.getNombreCliente(), r.getAsiento()});
        }
        vista.tablaReservas.setModel(modReservas);
    }

    private void buscarCliente() {
        String documento = vista.txtDoc.getText().trim();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Ingrese un número de documento.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String[] datos = dao.obtenerDatosCliente(documento);
        if (datos != null) {
            vista.txtNom.setText(datos[0]);
            vista.txtCor.setText(datos[1]);
            JOptionPane.showMessageDialog(vista, "¡Cliente encontrado y autocompletado!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(vista, "No se encontró ningún cliente con ese documento.", "Nuevo Cliente", JOptionPane.INFORMATION_MESSAGE);
            vista.txtNom.setText(""); vista.txtCor.setText("");
        }
    }

    private void guardarOActualizarFuncion() {
        if (vista.cbPeliculas.getSelectedItem() == null || vista.cbSalas.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione película y sala.", "Error", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int idPeli = Integer.parseInt(((String) vista.cbPeliculas.getSelectedItem()).split(" - ")[0]);
            int idSala = Integer.parseInt(((String) vista.cbSalas.getSelectedItem()).split(" - ")[0]);
            String fecha = vista.txtFecha.getText();
            double precio = Double.parseDouble(vista.txtPrecio.getText());
            String idFuncTexto = vista.txtIdFuncionEdit.getText();

            if (idFuncTexto.isEmpty()) {
                dao.registrarFuncion(idPeli, idSala, fecha, precio);
                JOptionPane.showMessageDialog(vista, "¡Función agregada a la cartelera!");
            } else {
                int idFunc = Integer.parseInt(idFuncTexto);
                dao.actualizarFuncion(idFunc, idPeli, idSala, fecha, precio);
                JOptionPane.showMessageDialog(vista, "¡Función actualizada con éxito!");
                vista.txtIdFuncionEdit.setText("");
            }
            cargarDatos();
        } catch (NumberFormatException ex) { 
            JOptionPane.showMessageDialog(vista, "Verifique que el precio sea numérico.", "Error", JOptionPane.ERROR_MESSAGE); 
        }
    }

    private void cargarFuncionParaEditar() {
        int fila = vista.tablaCartelera.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una función."); return; }
        vista.txtIdFuncionEdit.setText(vista.tablaCartelera.getValueAt(fila, 0).toString());
        vista.txtFecha.setText(vista.tablaCartelera.getValueAt(fila, 3).toString());
        vista.txtPrecio.setText(vista.tablaCartelera.getValueAt(fila, 4).toString());
        JOptionPane.showMessageDialog(vista, "Función cargada. Modifique los datos y presione 'Guardar / Actualizar Función'.");
        vista.pestanas.setSelectedIndex(1); 
    }

    private void eliminarFuncion() {
        int fila = vista.tablaCartelera.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una función."); return; }
        if (dao.eliminarFuncion((int) vista.tablaCartelera.getValueAt(fila, 0))) {
            JOptionPane.showMessageDialog(vista, "Función eliminada."); cargarDatos();
        } else {
            JOptionPane.showMessageDialog(vista, "No se puede eliminar porque tiene reservas activas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarReservaAsiento() {
        int fila = vista.tablaReservas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una reserva."); return; }
        int idReserva = (int) vista.tablaReservas.getValueAt(fila, 0);
        String nuevoAsiento = JOptionPane.showInputDialog(vista, "Ingrese el nuevo código de asiento (Ej: B2):");
        if (nuevoAsiento != null && !nuevoAsiento.trim().isEmpty()) {
            if (dao.actualizarAsientoReserva(idReserva, nuevoAsiento.toUpperCase())) {
                JOptionPane.showMessageDialog(vista, "Asiento actualizado con éxito."); cargarDatos();
            } else JOptionPane.showMessageDialog(vista, "Error al actualizar asiento.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarReserva() {
        int fila = vista.tablaReservas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una reserva."); return; }
        if (dao.eliminarReserva((int) vista.tablaReservas.getValueAt(fila, 0))) {
            JOptionPane.showMessageDialog(vista, "Reserva cancelada."); cargarDatos();
        } else JOptionPane.showMessageDialog(vista, "Error al eliminar reserva.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void guardarPelicula() {
        if (vista.txtTit.getText().trim().isEmpty() || vista.txtGen.getText().trim().isEmpty()) { return; }
        try {
            int duracion = Integer.parseInt(vista.txtDur.getText());
            String idTexto = vista.txtIdPeliEdit.getText();
            if (idTexto.isEmpty()) dao.registrarPelicula(new Pelicula(0, vista.txtTit.getText(), vista.txtGen.getText(), duracion, vista.txtClas.getText()));
            else dao.actualizarPelicula(new Pelicula(Integer.parseInt(idTexto), vista.txtTit.getText(), vista.txtGen.getText(), duracion, vista.txtClas.getText()));
            vista.txtIdPeliEdit.setText(""); vista.txtTit.setText(""); vista.txtGen.setText(""); vista.txtDur.setText(""); vista.txtClas.setText("");
            cargarDatos(); JOptionPane.showMessageDialog(vista, "Película guardada.");
        } catch (NumberFormatException ex) {}
    }
    private void cargarPeliculaParaEditar() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) return;
        vista.txtIdPeliEdit.setText(vista.tablaPeliculas.getValueAt(fila, 0).toString());
        vista.txtTit.setText(vista.tablaPeliculas.getValueAt(fila, 1).toString());
        vista.txtGen.setText(vista.tablaPeliculas.getValueAt(fila, 2).toString());
        vista.txtDur.setText(vista.tablaPeliculas.getValueAt(fila, 3).toString());
        vista.txtClas.setText(vista.tablaPeliculas.getValueAt(fila, 4).toString());
    }
    private void eliminarPelicula() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) return;
        dao.eliminarPelicula((int) vista.tablaPeliculas.getValueAt(fila, 0)); cargarDatos();
    }
    private void guardarSala() {
        if (vista.txtNomSala.getText().trim().isEmpty() || vista.txtCapSala.getText().trim().isEmpty()) { return; }
        try {
            int cap = Integer.parseInt(vista.txtCapSala.getText());
            String idTexto = vista.txtIdSalaEdit.getText();
            if (idTexto.isEmpty()) dao.registrarSala(new Sala(0, vista.txtNomSala.getText(), cap));
            else dao.actualizarSala(new Sala(Integer.parseInt(idTexto), vista.txtNomSala.getText(), cap));
            vista.txtNomSala.setText(""); vista.txtCapSala.setText(""); vista.txtIdSalaEdit.setText(""); cargarDatos();
        } catch (NumberFormatException ex) {}
    }
    private void cargarSalaParaEditar() {
        int fila = vista.tablaSalas.getSelectedRow();
        if (fila == -1) return;
        vista.txtIdSalaEdit.setText(vista.tablaSalas.getValueAt(fila, 0).toString());
        vista.txtNomSala.setText(vista.tablaSalas.getValueAt(fila, 1).toString());
        vista.txtCapSala.setText(vista.tablaSalas.getValueAt(fila, 2).toString());
    }
    private void eliminarSala() {
        int fila = vista.tablaSalas.getSelectedRow();
        if (fila == -1) return;
        dao.eliminarSala((int) vista.tablaSalas.getValueAt(fila, 0)); cargarDatos();
    }

    // --- MAPA INTERACTIVO Y ARRAY DE ASIENTOS ---
    private void abrirMapaSillas() {
        if (vista.cbFunciones.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(vista, "Seleccione primero una Función.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idFunc = Integer.parseInt(((String) vista.cbFunciones.getSelectedItem()).split(" - ")[0]);
        int capacidadSala = dao.obtenerCapacidadSalaPorFuncion(idFunc);
        List<String> ocupados = dao.obtenerAsientosOcupados(idFunc);
        
        // Abre el mapa interactivo pasando capacidad y ocupados en rojo
        DialogoAsientos dialog = new DialogoAsientos(vista, capacidadSala, ocupados);
        dialog.setVisible(true); 
        
        List<String> seleccionados = dialog.getSeleccionados();
        if (!seleccionados.isEmpty()) {
            vista.txtAsientosSel.setText(seleccionados.toString()); // Genera formato array [A1, A2]
        } else {
            vista.txtAsientosSel.setText("");
        }
    }

    private void procesarReserva() {
        if (vista.txtAsientosSel.getText().isEmpty() || vista.txtDoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Complete el documento y seleccione asientos en el mapa.", "Advertencia", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            int idFunc = Integer.parseInt(((String) vista.cbFunciones.getSelectedItem()).split(" - ")[0]);
            
            // Convierte de nuevo el String de texto plano a formato Array de la BD
            String sillasString = vista.txtAsientosSel.getText().replace("[", "").replace("]", "");
            List<String> listaAsientos = Arrays.asList(sillasString.split(", "));
            
            int idCliente = dao.registrarCliente(vista.txtDoc.getText(), vista.txtNom.getText(), vista.txtCor.getText());
            
            if (idCliente != -1 && dao.registrarReservaMultiple(idFunc, idCliente, listaAsientos)) {
                // Mensaje de confirmación detallado exacto que tenías
                JOptionPane.showMessageDialog(vista, "¡RESERVA EXITOSA!\nBoletos: " + listaAsientos.size() + "\nSillas asignadas: " + listaAsientos.toString(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.txtAsientosSel.setText(""); 
                vista.txtDoc.setText(""); vista.txtNom.setText(""); vista.txtCor.setText("");
                cargarDatos();
            } else {
                JOptionPane.showMessageDialog(vista, "Error al reservar. Es posible que un asiento se haya ocupado recientemente.", "Conflicto", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(vista, "Error al procesar la reserva.", "Error", JOptionPane.ERROR_MESSAGE); }
    }
}