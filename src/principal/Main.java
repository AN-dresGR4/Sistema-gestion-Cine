package principal;
import controlador.CineControlador;
import dao.CineDAO;
import vista.VentanaPrincipal;

public class Main {
    public static void main(String[] args) {
        VentanaPrincipal vista = new VentanaPrincipal();
        CineDAO dao = new CineDAO();
        new CineControlador(vista, dao);
        vista.setVisible(true);
    }
}
