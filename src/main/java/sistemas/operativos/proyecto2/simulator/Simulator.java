package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import sistemas.operativos.proyecto2.file.Folder;
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
        
    final int NUM_BLOCKS;
    public boolean[] blockFree;
    public Folder rootFolder = new Folder("root");
    public Folder currentFolder = rootFolder;
    private UserMode mode;
    
    public Simulator() {
        this.NUM_BLOCKS = 128;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
        this.mode = UserMode.ADMIN; 
    }
    
    public Simulator(int num) {
        this.NUM_BLOCKS = num;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
        this.mode = UserMode.ADMIN;
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
    
    public void deleteFolder(String name) {
        if (!checkWritePermission("deleteFolder")) return;
        currentFolder.deleteFolder(name, NUM_BLOCKS, blockFree);
    }
    
    /* From root */
    
    public void writeFileFromRoot(String fileName, int blockSize) {
        if (!checkWritePermission("writeFileFromRoot")) return;
        currentFolder = rootFolder;
        currentFolder.writeFile(fileName, blockSize, NUM_BLOCKS, blockFree);
    }
    
    public void deleteFileFromRoot(String fileName) {
        if (!checkWritePermission("deleteFileFromRoot")) return;
        currentFolder = rootFolder;
        currentFolder.deleteFile(fileName, NUM_BLOCKS, blockFree);
    }
    
    public void createFolderFromRoot(String name) {
        if (!checkWritePermission("createFolderFromRoot")) return;
        currentFolder = rootFolder;
        currentFolder.createFolder(name);
    }
    
    public Simulator getFolderFromRoot(String name) {
        currentFolder = rootFolder;
        Folder newFolder = currentFolder.getFolder(name);
        if (newFolder != null) currentFolder = newFolder;
        return this;
    }
    
    public void deleteFolderFromRoot(String name) {
        if (!checkWritePermission("deleteFolderFromRoot")) return;
        currentFolder = rootFolder;
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
