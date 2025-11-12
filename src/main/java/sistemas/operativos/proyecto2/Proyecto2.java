package sistemas.operativos.proyecto2;

import java.util.Arrays;
import sistemas.operativos.proyecto2.utils.*;
import sistemas.operativos.proyecto2.lib.*;
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
        sim.writeFile("Test4", 1284);
        
        Printer.print(sim.fileMeta.toString());
        Printer.print(Arrays.toString(sim.blockFree));

        sim.deleteFile("Test2");

        Printer.print(sim.fileMeta.toString());
        Printer.print(Arrays.toString(sim.blockFree));
    }
}
