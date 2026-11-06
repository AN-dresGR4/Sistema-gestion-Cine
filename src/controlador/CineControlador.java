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
        
        this.vista.btnActualizarCartelera.addActionListener(this);
        this.vista.btnGuardarPelicula.addActionListener(this);
        this.vista.btnEliminarPelicula.addActionListener(this);
        this.vista.btnReservar.addActionListener(this);
        
        cargarCartelera();
        cargarPeliculas();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.btnActualizarCartelera) cargarCartelera();
        else if (e.getSource() == vista.btnGuardarPelicula) guardarPelicula();
        else if (e.getSource() == vista.btnEliminarPelicula) eliminarPelicula();
        else if (e.getSource() == vista.btnReservar) procesarReserva();
    }

    private void cargarCartelera() { vista.tablaCartelera.setModel(dao.obtenerCartelera()); }

    private void cargarPeliculas() {
        List<Pelicula> lista = dao.listarPeliculas();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID"); modelo.addColumn("Título"); modelo.addColumn("Género");
        modelo.addColumn("Duración"); modelo.addColumn("Clasificación");
        for (Pelicula p : lista) {
            modelo.addRow(new Object[]{p.getId(), p.getTitulo(), p.getGenero(), p.getDuracion(), p.getClasificacion()});
        }
        vista.tablaPeliculas.setModel(modelo);
    }

    private void guardarPelicula() {
        if (vista.txtTitulo.getText().trim().isEmpty() || vista.txtGenero.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Los campos Título y Género son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE); return;
        }
        try {
            int duracion = Integer.parseInt(vista.txtDuracion.getText());
            Pelicula p = new Pelicula(0, vista.txtTitulo.getText(), vista.txtGenero.getText(), duracion, vista.txtClasificacion.getText());
            if (dao.registrarPelicula(p)) {
                JOptionPane.showMessageDialog(vista, "Película guardada correctamente.");
                cargarPeliculas();
                vista.txtTitulo.setText(""); vista.txtGenero.setText(""); vista.txtDuracion.setText(""); vista.txtClasificacion.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "La duración debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarPelicula() {
        int fila = vista.tablaPeliculas.getSelectedRow();
        if (fila == -1) { JOptionPane.showMessageDialog(vista, "Seleccione una película."); return; }
        int id = (int) vista.tablaPeliculas.getValueAt(fila, 0);
        if (dao.eliminarPelicula(id)) {
            JOptionPane.showMessageDialog(vista, "Película eliminada.");
            cargarPeliculas(); cargarCartelera();
        } else {
            JOptionPane.showMessageDialog(vista, "No se puede eliminar por dependencias.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void procesarReserva() {
        try {
            int idFuncion = Integer.parseInt(vista.txtIdFuncionReserva.getText());
            String asiento = vista.txtAsiento.getText().toUpperCase();
            if (dao.asientoOcupado(idFuncion, asiento)) {
                JOptionPane.showMessageDialog(vista, "¡Asiento " + asiento + " ocupado!"); return;
            }
            
            String doc = vista.txtDocCliente.getText();
            int idCliente = dao.registrarClienteSiNoExiste(doc, vista.txtNomCliente.getText(), vista.txtCorreoCliente.getText());
            
            if (idCliente != -1 && dao.registrarReserva(idFuncion, idCliente, asiento)) {
                JOptionPane.showMessageDialog(vista, "¡RESERVA CONFIRMADA para " + doc + " en asiento " + asiento + "!");
                vista.txtAsiento.setText("");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Faltan datos o ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
