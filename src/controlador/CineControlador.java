package controlador;
import dao.CineDAO;
import modelo.Pelicula;
import vista.VentanaPrincipal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CineControlador implements ActionListener {
    private VentanaPrincipal vista;
    private CineDAO dao;

    public CineControlador(VentanaPrincipal vista, CineDAO dao) {
        this.vista = vista;
        this.dao = dao;
        
        this.vista.btnAct.addActionListener(this);
        this.vista.btnGuardar.addActionListener(this);
        this.vista.btnEliminar.addActionListener(this);
        this.vista.btnGuardarFuncion.addActionListener(this);
        this.vista.btnReservar.addActionListener(this);
        
        cargarDatos();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnAct) {
            cargarDatos();
        } else if (e.getSource() == vista.btnGuardar) {
            guardarPelicula();
        } else if (e.getSource() == vista.btnEliminar) {
            eliminarPelicula();
        } else if (e.getSource() == vista.btnGuardarFuncion) {
            guardarFuncion();
        } else if (e.getSource() == vista.btnReservar) {
            procesarReserva();
        }
    }

    private void cargarDatos() {
        // 1. Cargar Cartelera
        vista.tablaCartelera.setModel(dao.obtenerCartelera());
        
        // 2. Cargar Películas (En la Tabla y en el nuevo ComboBox)
        List<Pelicula> lista = dao.listarPeliculas();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID"); modelo.addColumn("Título"); modelo.addColumn("Género");
        modelo.addColumn("Duración"); modelo.addColumn("Clasificación");
        
        vista.cbPeliculas.removeAllItems(); // Limpiamos el select
        
        for (Pelicula p : lista) {
            // Llenar tabla
            modelo.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero(), p.getDuracion(), p.getClasificacion()});
            // Llenar select (formato: "ID - Título")
            vista.cbPeliculas.addItem(p.getId() + " - " + p.getTitulo());
        }
        vista.tablaPeliculas.setModel(modelo);
        
        // 3. LLENAR EL COMBOBOX DE FUNCIONES
        vista.cbFunciones.removeAllItems();
        List<String> funciones = dao.obtenerFuncionesDropdown();
        for (String f : funciones) {
            vista.cbFunciones.addItem(f);
        }
    }

    private void guardarPelicula() {
        if (vista.txtTit.getText().trim().isEmpty() || vista.txtGen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "El título y género son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            int duracion = Integer.parseInt(vista.txtDur.getText());
            Pelicula p = new Pelicula(0, vista.txtTit.getText(), vista.txtGen.getText(), duracion, vista.txtClas.getText());
            if (dao.registrarPelicula(p)) {
                JOptionPane.showMessageDialog(vista, "Película guardada correctamente.");
                vista.txtTit.setText(""); vista.txtGen.setText(""); vista.txtDur.setText(""); vista.txtClas.setText("");
                cargarDatos(); // Recarga la tabla y el ComboBox automáticamente
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar película.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "La duración debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPelicula() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista, "Seleccione una película de la tabla para eliminarla.");
            return;
        }
        int id = (int) vista.tablaPeliculas.getValueAt(fila, 0);
        if (dao.eliminarPelicula(id)) {
            JOptionPane.showMessageDialog(vista, "Película eliminada.");
            cargarDatos();
        } else {
            JOptionPane.showMessageDialog(vista, "No se puede eliminar. Verifique que no esté en la cartelera.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarFuncion() {
        if (vista.cbPeliculas.getSelectedItem() == null || vista.txtIdSala.getText().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Asegúrese de seleccionar una película y llenar la sala.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            // Extraer el ID de la película del ComboBox ("ID - Título")
            String seleccion = (String) vista.cbPeliculas.getSelectedItem();
            int idPeli = Integer.parseInt(seleccion.split(" - ")[0]);
            
            int idSala = Integer.parseInt(vista.txtIdSala.getText());
            String fecha = vista.txtFecha.getText();
            double precio = Double.parseDouble(vista.txtPrecio.getText());
            
            if (dao.registrarFuncion(idPeli, idSala, fecha, precio)) {
                JOptionPane.showMessageDialog(vista, "¡Función agregada a la cartelera con éxito!");
                cargarDatos(); 
            } else {
                JOptionPane.showMessageDialog(vista, "Error en BD. Revisa que el ID Sala exista (1 o 2).", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "La Sala y el precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarReserva() {
        if (vista.cbFunciones.getSelectedItem() == null || vista.txtAsi.getText().trim().isEmpty() || vista.txtDoc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor complete todos los datos de la reserva.", "Datos faltantes", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String seleccion = (String) vista.cbFunciones.getSelectedItem();
            int idFuncion = Integer.parseInt(seleccion.split(" - ")[0]);
            
            String asiento = vista.txtAsi.getText().toUpperCase();
            
            if (dao.asientoOcupado(idFuncion, asiento)) {
                JOptionPane.showMessageDialog(vista, "¡Lo sentimos! El asiento " + asiento + " ya está reservado para esta función.", "Asiento Ocupado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String doc = vista.txtDoc.getText();
            int idCliente = dao.registrarCliente(doc, vista.txtNom.getText(), vista.txtCor.getText());
            
            if (idCliente == -1) {
                JOptionPane.showMessageDialog(vista, "Fallo al procesar el cliente en BD.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (dao.registrarReserva(idFuncion, idCliente, asiento)) {
                JOptionPane.showMessageDialog(vista, "¡RESERVA EXITOSA!\nEntrada generada para el asiento: " + asiento + "\nDocumento: " + doc, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                vista.txtAsi.setText("");
            } else {
                JOptionPane.showMessageDialog(vista, "Error al guardar la reserva.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(vista, "Ocurrió un error al procesar la reserva.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        }
    }
}