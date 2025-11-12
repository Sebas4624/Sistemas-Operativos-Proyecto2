package sistemas.operativos.proyecto2;

import sistemas.operativos.proyecto2.utils.*;
import sistemas.operativos.proyecto2.lib.*;

/**
 *
 * @author sebas
 */
public class Proyecto2 {

    public static void main(String[] args) {
        HashTable<Integer> test = new HashTable(5);
        
        test.insert(46, "first");
        test.insert(12, "first");
        test.insert(92, "first");
        test.insert(37, "first");
        test.insert(20, "second");
        test.insert(10, "second");
        test.insert(22, "third");
        test.insert(52, "third");
        test.insert(11, "four");
        test.insert(45, "four");
        test.insert(61, "four");
        
        Printer.print(test.toString());
    }
}
