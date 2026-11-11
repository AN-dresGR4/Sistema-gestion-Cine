package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class DialogoAsientos extends JDialog {
    private List<String> seleccionados = new ArrayList<>();

    public DialogoAsientos(JFrame parent, int capacidadSala, List<String> ocupados) {
        super(parent, "Selección Interactiva de Asientos (Capacidad: " + capacidadSala + ")", true);
        setSize(750, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        JLabel lblPantalla = new JLabel("P A N T A L L A", SwingConstants.CENTER);
        lblPantalla.setOpaque(true);
        lblPantalla.setBackground(new Color(50, 50, 50));
        lblPantalla.setForeground(Color.WHITE);
        lblPantalla.setFont(new Font("Arial", Font.BOLD, 20));
        lblPantalla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(lblPantalla, BorderLayout.NORTH);

        int columnas = 10;
        int filasTotales = (int) Math.ceil((double) capacidadSala / columnas);
        String[] letrasFilas = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        JPanel panelSillas = new JPanel(new GridLayout(filasTotales, columnas, 8, 8));
        panelSillas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        int contadorSillas = 0;
        for (int f = 0; f < filasTotales && contadorSillas < capacidadSala; f++) {
            String letraFila = letrasFilas[f];
            for (int c = 1; c <= columnas && contadorSillas < capacidadSala; c++) {
                contadorSillas++;
                String nombreAsiento = letraFila + c;
                
                JToggleButton btn = new JToggleButton(nombreAsiento);
                btn.setFont(new Font("Arial", Font.BOLD, 11));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

                // Pinta de ROJO si el asiento ya está en el array de ocupados de la BD
                if (ocupados != null && ocupados.contains(nombreAsiento)) {
                    btn.setBackground(new Color(220, 53, 69)); // Rojo Ocupado
                    btn.setForeground(Color.WHITE);
                    btn.setEnabled(false);
                } else {
                    btn.setBackground(new Color(40, 167, 69)); // Verde Libre
                    btn.setForeground(Color.WHITE);
                    
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (btn.isSelected()) {
                                btn.setBackground(new Color(255, 193, 7)); // Amarillo (Seleccionado)
                                btn.setForeground(Color.BLACK);
                                seleccionados.add(nombreAsiento);
                            } else {
                                btn.setBackground(new Color(40, 167, 69)); // Vuelve a verde
                                btn.setForeground(Color.WHITE);
                                seleccionados.remove(nombreAsiento);
                            }
                        }
                    });
                }
                panelSillas.add(btn);
            }
        }
        
        add(new JScrollPane(panelSillas), BorderLayout.CENTER);

        JButton btnConfirmar = new JButton("Confirmar Selección");
        btnConfirmar.setFont(new Font("Arial", Font.BOLD, 14));
        btnConfirmar.addActionListener(e -> dispose());
        add(btnConfirmar, BorderLayout.SOUTH);
    }

    public List<String> getSeleccionados() {
        return seleccionados;
    }
}