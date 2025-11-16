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
        Simulator sim = new Simulator();
        
        sim.writeFile("Test1", 2);
        sim.writeFile("Test2", 5);
        sim.writeFile("Test3", 3);
        sim.createFolder("Testeos");
        //sim.createFolder("Locos");
        //sim.deleteFolder("Locos");
        //sim.writeFile("Test4", 1284, "");
        
        //Printer.print(sim.rootFolder.toString());

        //sim.deleteFile("Test2");
        sim.getFolder("Testeos").writeFile("Test4", 3);
        sim.getFolder("Testeos").writeFile("Test5", 4);
        sim.getFolder("Testeos").writeFile("Test6", 1);
        
        //Printer.print(sim.rootFolder.toString());
        
        sim.getFolder("Testeos").deleteFile("Test4");
        
        Printer.print(sim.rootFolder.toString());
        Printer.print(Arrays.toString(sim.blockFree));
        
        sim.deleteFolderFromRoot("Testeos");
        
        //Printer.print(sim.rootFolder.toString());
        Printer.print(Arrays.toString(sim.blockFree));
        
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame frame = new MainFrame();
                frame.setVisible(true);
            }
        });
    }
}
