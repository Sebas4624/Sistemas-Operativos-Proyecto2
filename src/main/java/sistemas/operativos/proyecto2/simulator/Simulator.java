package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import java.util.function.Consumer;
import sistemas.operativos.proyecto2.file.FileMetadata;
import sistemas.operativos.proyecto2.file.Folder;
import sistemas.operativos.proyecto2.lib.LinkedList;
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
    
    private Consumer<Void> updateJTreeFunction;
    private Consumer<Void> updateJTableFunction;
    
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
                            
                            this.executeUpdateJTable();
                            this.executeUpdateJTree();
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
                            
                            this.executeUpdateJTable();
                            this.executeUpdateJTree();
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
                            
                            this.executeUpdateJTable();
                            this.executeUpdateJTree();
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
                            
                            this.executeUpdateJTable();
                            this.executeUpdateJTree();
                        }
                    }
                }
            }
            
            case Policy.SSTF -> {
                while (true) {
                    if(Thread.currentThread().isInterrupted()) {
                        return;
                    }
                    
                    if (this.sched.isActive()) {
                        this.sched.executeSSTF();
                        if (this.sched.getCurrentProcess() != null && this.sched.getCurrentProcess().isRunning()) {                        
                            this.executeCRUD();
                            this.sched.getCurrentProcess().setFinished();
                            
                            this.executeUpdateJTable();
                            this.executeUpdateJTree();
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
                switch(current.getElementType()) {
                    case Element.Type.FILE -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]);
                                
                                if (!this.getCurrentFolder().getName().equals(current.getElementPath()[i])) {
                                    this.createFolder(current.getElementPath()[i]);
                                    this.getFolder(current.getElementPath()[i]).writeFile(current.getElementName(), current.getElementBlocks());
                                } else {
                                    this.writeFile(current.getElementName(), current.getElementBlocks());
                                }
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                                
                                if (!this.getCurrentFolder().getName().equals(current.getElementPath()[i])) {
                                    this.createFolder(current.getElementPath()[i]);
                                    this.getFolder(current.getElementPath()[i]);
                                }
                            }
                        }

                        break;
                    }
                    case Element.Type.FOLDER -> {
                        for (int i = 0; i < current.getElementPath().length; i++) {
                            if (i == current.getElementPath().length - 1) {
                                this.getFolder(current.getElementPath()[i]);
                                
                                if (!this.getCurrentFolder().getName().equals(current.getElementPath()[i])) {
                                    this.createFolder(current.getElementPath()[i]);
                                    this.getFolder(current.getElementPath()[i]).writeFile(current.getElementName(), current.getElementBlocks());
                                } else {
                                    this.createFolder(current.getElementName());
                                }
                            } else {
                                this.getFolder(current.getElementPath()[i]);
                                
                                if (!this.getCurrentFolder().getName().equals(current.getElementPath()[i])) {
                                    this.createFolder(current.getElementPath()[i]);
                                    this.getFolder(current.getElementPath()[i]);
                                }
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
                            if (i == current.getElementPath().length - 1 && !"root".equals(current.getElementName())) {
                                Folder prevFolder = this.getCurrentFolder();
                                this.getFolder(current.getElementPath()[i]).modifyFolder(current.getElementName(), prevFolder);
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
                            if (i == current.getElementPath().length - 1 && !"root".equals(current.getElementName())) {
                                this.deleteFolder(current.getElementName());
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
    
    public synchronized void startSchedulerExecution() {
        if (this.sched.isActive()) {
            starter = new Thread(() -> {
                this.startScheduler();
            }, "Starter-Thread");
        }
        
        starter.start();
    }
    
    public synchronized void stopSchedulerExecution() {
        starter.interrupt();
        
        starter = new Thread(() -> {
            this.startScheduler();
        }, "Starter-Thread");
    }
    
    /*
     *   Simulator Methods
     */
    
    public synchronized Folder getCurrentFolder() {
        return this.currentFolder;
    }
    
    public synchronized void writeFile(String fileName, int blockSize) {
        if (!checkWritePermission("writeFile")) return;
        currentFolder.writeFile(fileName, blockSize, NUM_BLOCKS, blockFree);
    }
    
    public synchronized void modifyFile(String fileName, String newName) {
        if (!checkWritePermission("modifyFile")) return;
        
        String defName = newName;
        LinkedList<FileMetadata> filesTemp = currentFolder.getFiles();

        for (int j = 0; j < filesTemp.size(); j++) {
            if (filesTemp.get(j).getFileName().equals(newName)) {
                defName = newName + " (" + String.valueOf(System.currentTimeMillis()) + ")";
                break;
            }
        }
        
        try {
            currentFolder.getFile(fileName).setLastModifiedTime(System.currentTimeMillis());
            currentFolder.getFile(fileName).setFileName(defName);
        } catch (Exception e) {
            Printer.print("Archivo no encontrado");
        }
    }
    
    public synchronized void deleteFile(String fileName) {
        if (!checkWritePermission("deleteFile")) return;
        
        try {
            currentFolder.deleteFile(fileName, NUM_BLOCKS, blockFree);
        } catch (Exception e) {
            Printer.print("Archivo no encontrado");
        }
    }
    
    public synchronized void createFolder(String name) {
        if (!checkWritePermission("createFolder")) return;
        currentFolder.createFolder(name);
    }
    
    public synchronized Simulator getFolder(String name) {
        Folder newFolder = currentFolder.getFolder(name);
        if (newFolder != null) currentFolder = newFolder;
        return this;
    }
    
    public synchronized void modifyFolder(String name, Folder folder) {
        if (!checkWritePermission("modifyFolder")) return;
        
        String defName = name;
        LinkedList<Folder> foldersTemp = folder.getSubfolders();

        for (int j = 0; j < foldersTemp.size(); j++) {
            if (foldersTemp.get(j).getName().equals(name)) {
                defName = name + " (" + String.valueOf(System.currentTimeMillis()) + ")";
                break;
            }
        }
        
        try {
            currentFolder.setLastModifiedTime(System.currentTimeMillis());
            currentFolder.setName(defName);
        } catch (Exception e) {
            Printer.print("Archivo no encontrado");
        }
    }
    
    public synchronized void deleteFolder(String name) {
        if (!checkWritePermission("deleteFolder")) return;
        
        try {
            currentFolder.deleteFolder(name, NUM_BLOCKS, blockFree);
        } catch (Exception e) {
            Printer.print("Archivo no encontrado");
        }
    }
    
    public synchronized void resetRootFolder() {
        this.currentToRoot();
        currentFolder.deleteAllFiles(NUM_BLOCKS, blockFree);
        currentFolder.deleteAllFolders(NUM_BLOCKS, blockFree);
    }
    
    public synchronized void resetScheduler() {
        this.sched.resetSchedulerState();
    }
    
    /*
     *   Updates
     */
    
    // JTree
    
    public void setUpdateJTreeFunction(Consumer<Void> updateFunction) {
        this.updateJTreeFunction = updateFunction;
    }
    
    public void executeUpdateJTree() {
        if (updateJTreeFunction != null) {
            updateJTreeFunction.accept(null);
        }
    }
    
    // JTable
    
    public void setUpdateJTableFunction(Consumer<Void> updateFunction) {
        this.updateJTableFunction = updateFunction;
    }
    
    public void executeUpdateJTable() {
        if (updateJTableFunction != null) {
            updateJTableFunction.accept(null);
        }
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
    
    public int getNumBlocks() {
    return NUM_BLOCKS;
    }
    

}
