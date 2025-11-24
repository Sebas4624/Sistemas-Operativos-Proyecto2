package sistemas.operativos.proyecto2.simulator.process;

/**
 * Clase proceso del simulador.
 * @author Sebastián
 */
public class Process implements Comparable<Process> {
    private final String id;
    private final String name;
    private ProcessState currentState = ProcessState.READY;
    private int priority;
    private Integer startTime = null;
    private Integer finishTime = null;
    
    private final Element elementCRUD;
    private final CRUD crud;

    /**
     * Constructor.
     * @param id Identificador del proceso.
     * @param name Nombre del proceso.
     * @param priority Nivel de prioridad del proceso.
     */
    public Process(String id, String name, int priority, Element elementCRUD, CRUD crud) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.elementCRUD = elementCRUD;
        this.crud = crud;
    }
    
    public String id() { return id; }
    public String name() { return name; }
    public int priority() { return priority; }
    public void reducePriority() { if (priority > 1) priority--; }
    
    public String getElementName() { return this.elementCRUD.name; }
    public int getElementBlocks() { return this.elementCRUD.blocks; }
    public Element.Type getElementType() { return this.elementCRUD.type; }
    public String[] getElementPath() { return this.elementCRUD.path; }
    public CRUD getCRUD() { return this.crud; }
    
    public ProcessState currentState() { return currentState; }
    public boolean isFinished() {
        return currentState == ProcessState.FINISHED;
    }
    
    public boolean isBlocked() {
        return currentState == ProcessState.BLOCKED;
    }
    
    public boolean isRunning() {
        return currentState == ProcessState.RUNNING;
    }
    
    public boolean isReady() {
        return currentState == ProcessState.READY;
    }
    
    public void setFinished() {
        this.currentState = ProcessState.FINISHED;
    }
    
    public void setBlocked() {
        this.currentState = ProcessState.BLOCKED;
    }
    
    public void setRunning() {
        this.currentState = ProcessState.RUNNING;
    }
    
    public void setReady() {
        this.currentState = ProcessState.READY;
    }

    public Integer startTime() { return startTime; }
    public void setStartTime(int t) { if (startTime == null) startTime = t; }

    public Integer finishTime() { return finishTime; }
    public void setFinishTime(int t) { finishTime = t; }

    @Override
    public int compareTo(Process o) {
        //return Integer.compare(this.priority, o.priority());
        return Integer.compare(this.priority, o.priority());
    }

    @Override
    public String toString() {
        return "ID: " + this.id + ", Nombre: " + this.name + ", Prioridad: " + this.priority;
    }
}