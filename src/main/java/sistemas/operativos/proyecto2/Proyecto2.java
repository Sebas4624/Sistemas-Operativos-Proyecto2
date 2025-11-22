package sistemas.operativos.proyecto2;

import java.util.Arrays;
import sistemas.operativos.proyecto2.utils.*;
import sistemas.operativos.proyecto2.simulator.Simulator;

/**
 *
 * @author sebas
 */
public class Proyecto2 {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
