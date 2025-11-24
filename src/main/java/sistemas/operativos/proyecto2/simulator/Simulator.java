package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import sistemas.operativos.proyecto2.file.Folder;
import sistemas.operativos.proyecto2.simulator.config.Config;
import sistemas.operativos.proyecto2.simulator.config.Policy;
import sistemas.operativos.proyecto2.simulator.process.Process;
import sistemas.operativos.proyecto2.simulator.process.Element;
import sistemas.operativos.proyecto2.simulator.process.CRUD;
import sistemas.operativos.proyecto2.utils.Printer;

/**
 *
 * @author sebas
 */
public class Simulator {
    
    public enum UserMode {
        ADMIN,
        USER
    }
    
    public Config config;
    public Scheduler sched;
    
    private Thread starter;
    
    final int NUM_BLOCKS;
    public boolean[] blockFree;
    public Folder rootFolder = new Folder("root");
    public Folder currentFolder = rootFolder;
    private UserMode mode;
    
    public Simulator(Config config) {
        if (config.getNumBlocks() < 64) {
            this.NUM_BLOCKS = 64;
        } else if (config.getNumBlocks() > 1024) {
            this.NUM_BLOCKS = 1024;
        } else {
            this.NUM_BLOCKS = config.getNumBlocks();
        }
        
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
        this.mode = UserMode.ADMIN;
        
        this.sched = new Scheduler(config);
        this.config = config;
        
        this.starter = new Thread(() -> {
            this.startScheduler();
        }, "Starter-Thread");
    }
    
    /*
     *   Process Scheduler Manager Methods
     */
    
    public void startScheduler() {
        switch (config.getPolicy()) {
            
            case Policy.FIFO -> {
                while (true) {
                    if(Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    
                    if (this.sched.isActive()) {
                        this.sched.executeFIFO();
                        if (this.sched.getCurrentProcess() != null && this.sched.getCurrentProcess().isRunning()) {                        
                            this.executeCRUD();
                            this.sched.getCurrentProcess().setFinished();
                        }
                    }
                }
            }
            
            case Policy.LIFO -> {
                while (true) {
                    if(Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    
                    if (this.sched.isActive()) {
                        this.sched.executeLIFO();
                        if (this.sched.getCurrentProcess() != null && this.sched.getCurrentProcess().isRunning()) {                        
                            this.executeCRUD();
                            this.sched.getCurrentProcess().setFinished();
                        }
                    }
                }
            }
            
            case Policy.RANDOM -> {
                while (true) {
                    if(Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    
                    if (this.sched.isActive()) {
                        this.sched.executeRANDOM();
                        if (this.sched.getCurrentProcess() != null && this.sched.getCurrentProcess().isRunning()) {                        
                            this.executeCRUD();
                            this.sched.getCurrentProcess().setFinished();
                        }
                    }
                }
            }
            
            case Policy.PRI -> {
                while (true) {
                    if(Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    
                    if (this.sched.isActive()) {
                        this.sched.executePRI();
                        if (this.sched.getCurrentProcess() != null && this.sched.getCurrentProcess().isRunning()) {                        
                            this.executeCRUD();
                            this.sched.getCurrentProcess().setFinished();
                        }
                    }
                }
            }
            
        }
    }
    
    public void executeCRUD() {
        if (this.sched.getCurrentProcess() == null) return;
        
        Process current = this.sched.getCurrentProcess();
        this.currentToRoot();
        
        switch (this.sched.getCurrentProcess().getCRUD()) {
            
            case CRUD.CREATE -> {
                Printer.print("CREATE");
                switch(current.getElementType()) {
                    case Element.Type.FILE -> {
                        Printer.print("File");
                        Printer.print(Arrays.toString(current.getElementPath()));
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).writeFile(current.getElementName(), current.getElementBlocks());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                    case Element.Type.FOLDER -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).createFolder(current.getElementName());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                }
                
                break;
            }
            
            case CRUD.UPDATE -> {
                switch(current.getElementType()) {
                    case Element.Type.FILE -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).modifyFile(current.getElementPath()[i], current.getElementName());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                    case Element.Type.FOLDER -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).modifyFolder(current.getElementName());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                }
                
                break;
            }
            
            case CRUD.DELETE -> {
                switch(current.getElementType()) {
                    case Element.Type.FILE -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).deleteFile(current.getElementName());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                    case Element.Type.FOLDER -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]).deleteFolder(current.getElementName());
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                            }
                        }

                        break;
                    }
                }
                
                break;
            }
            
            default -> {
                
                break;
            }
            
        }
    }
    
    public void startSchedulerExecution() {
        if (this.sched.isActive()) {
            starter = new Thread(() -> {
                this.startScheduler();
            }, "Starter-Thread");
        }
        
        starter.start();
    }
    
    public void stopSchedulerExecution() {
        starter.interrupt();
        
        starter = new Thread(() -> {
            this.startScheduler();
        }, "Starter-Thread");
    }
    
    /*
     *   Simulator Methods
     */
    
    public Folder getCurrentFolder() {
        return this.currentFolder;
    }
    
    /* Relative */
    
    public void writeFile(String fileName, int blockSize) {
        if (!checkWritePermission("writeFile")) return;
        currentFolder.writeFile(fileName, blockSize, NUM_BLOCKS, blockFree);
    }
    
    public void modifyFile(String fileName, String newName) {
        if (!checkWritePermission("modifyFile")) return;
        currentFolder.getFile(fileName).setLastModifiedTime(System.currentTimeMillis());
        currentFolder.getFile(fileName).setFileName(newName);
    }
    
    public void deleteFile(String fileName) {
        if (!checkWritePermission("deleteFile")) return;
        currentFolder.deleteFile(fileName, NUM_BLOCKS, blockFree);
    }
    
    public void createFolder(String name) {
        if (!checkWritePermission("createFolder")) return;
        currentFolder.createFolder(name);
    }
    
    public Simulator getFolder(String name) {
        Folder newFolder = currentFolder.getFolder(name);
        if (newFolder != null) currentFolder = newFolder;
        return this;
    }
    
    public void modifyFolder(String name) {
        if (!checkWritePermission("modifyFolder")) return;
        currentFolder.setLastModifiedTime(System.currentTimeMillis());
        currentFolder.setName(name);
    }
    
    public void deleteFolder(String name) {
        if (!checkWritePermission("deleteFolder")) return;
        currentFolder.deleteFolder(name, NUM_BLOCKS, blockFree);
    }
    
    /*
     *   Misc
     */
    
    public void setMode(UserMode mode) {
        this.mode = mode;
    }

    public UserMode getMode() {
        return this.mode;
    }
    
    public void currentToRoot() {
        currentFolder = rootFolder;
    }

    private boolean isAdmin() {
        return this.mode == UserMode.ADMIN;
    }

    private boolean checkWritePermission(String operation) {
        if (!isAdmin()) {
            Printer.print("Operación \"" + operation + "\" no permitida en modo USUARIO (solo lectura).");
            return false;
        }
        return true;
    }

}
