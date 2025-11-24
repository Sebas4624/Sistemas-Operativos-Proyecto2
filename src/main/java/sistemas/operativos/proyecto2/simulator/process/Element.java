package sistemas.operativos.proyecto2.simulator.process;

/**
 *
 * @author sebas
 */
public class Element {
    public enum Type {
        FILE,
        FOLDER
    }

    public String name;
    public int blocks;
    public Type type;
    public String[] path;

    public Element(String name, Type type, String[] path) {
        this.name = name;
        this.blocks = 0;
        this.type = type;
        this.path = path;
    }

    public Element(String name, int blocks, Type type, String[] path) {
        this.name = name;
        this.blocks = blocks;
        this.type = type;
        this.path = path;
    }
}
