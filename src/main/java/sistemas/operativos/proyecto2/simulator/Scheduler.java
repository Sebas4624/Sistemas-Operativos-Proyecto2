package sistemas.operativos.proyecto2.simulator;

import sistemas.operativos.proyecto2.lib.LinkedList;
import sistemas.operativos.proyecto2.lib.Queue;
import sistemas.operativos.proyecto2.simulator.process.Process;
import sistemas.operativos.proyecto2.simulator.config.Config;
import sistemas.operativos.proyecto2.simulator.process.CRUD;
import sistemas.operativos.proyecto2.simulator.process.Element;
import sistemas.operativos.proyecto2.utils.Printer;

/**
 *
 * @author sebas
 */
public class Scheduler {
    public Config config;
    
    private Queue<Process> readyQueue;
    private Queue<Process> finishedQueue;
    private Process currentProcess = null;
    
    public Scheduler(Config config) {
        this.readyQueue = new Queue();
        this.finishedQueue = new Queue();
        this.config = config;
    }
    
    /*
     *   Crear proceso de E/S
     */
    
    public void createProcess(String name, int priority, Element elementCRUD, CRUD crud) {
        String id = java.time.LocalTime.now().toString();
        
        Process process = new Process(id, name, priority, elementCRUD, crud);

        readyQueue.enqueue(process);
    }
    
    /*
     *   <------------------------>
     *   Políticas de Planificación
     *   <------------------------>
     */
    
    //   Función principal
    
    private void execute(int value) {
        if (currentProcess == null) {
            scheduleNextProcess(value);
        }
        
        // TODO: Execute CRUD operation from process
        
        if (currentProcess != null) {
            if (!currentProcess.isFinished()) {
                currentProcess.setRunning();
            } else if (currentProcess.isFinished()) {
                finishedQueue.enqueue(currentProcess);
                
                currentProcess = null;
            }
        
        
            // 4. Esperar según la duración del ciclo configurada
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    //   Politica de planificación FIFO (First-In, First-Out)
    
    public synchronized void executeFIFO() {
        execute(0);
    }
    
    //   Politica de planificación LIFO (Last-In, First-Out)
    
    public synchronized void executeLIFO() {
        execute(1);
    }
    
    //   Politica de planificación RANDOM (Aleatorio)
    
    public synchronized void executeRANDOM() {
        execute(2);
    }
    
    //   Politica de planificación PRI (Prioridad)
    
    public synchronized void executePRI() {
        execute(3);
    }
    
    //   Politica de planificación SSTF (Shortest Servicer Time First)
    
    public synchronized void executeSSTF() {
        execute(4);
    }
    
    /*
     *   Planificar procesos
     */
    
    private synchronized void scheduleNextProcess(int mode) {
        switch (mode) {
            // FIFO
            case 0 -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.dequeue();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
            
            // LIFO
            case 1 -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.dequeueReverse();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
            
            // RANDOM
            case 2 -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.pollRand();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
            
            // Priority
            case 3 -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.pollPriority();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
            
            // SSTF
            case 4 -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.pollPath();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
            
            // Default: FIFO
            default -> {
                if (!readyQueue.isEmpty()) {
                    currentProcess = readyQueue.dequeue();
                } else {
                    currentProcess = null;
                }

                if (currentProcess != null) {
                    currentProcess.setRunning();
                }
                
                break;
            }
        }
    }
    
    /*
     *   Misc
     */
    
    public boolean isActive() {
        return !readyQueue.isEmpty() || currentProcess != null;
    }

    public Process getCurrentProcess() {
        return this.currentProcess;
    }

    public LinkedList<Process> getReadyQueue() {
        return this.readyQueue.toLinkedList();
    }

    public LinkedList<Process> getFinishedQueue() {
        return this.readyQueue.toLinkedList();
    }

    public String[] getReadyList() {
        LinkedList<Process> lList = this.readyQueue.toLinkedList();
        String[] list = new String[lList.size()];
        
        for (int i = 0; i < list.length; i++) {
            list[i] = lList.get(i).name();
        }
        
        return list;
    }

    public String[] getFinishedList() {
        LinkedList<Process> lList = this.finishedQueue.toLinkedList();
        String[] list = new String[lList.size()];
        
        for (int i = 0; i < list.length; i++) {
            list[i] = lList.get(i).name();
        }
        
        return list;
    }
    
    public void resetSchedulerState() {
        this.readyQueue = new Queue();
        this.finishedQueue = new Queue();
        this.currentProcess = null;
    }
}
