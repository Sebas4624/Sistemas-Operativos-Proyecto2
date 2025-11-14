package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import sistemas.operativos.proyecto2.file.Folder;
import sistemas.operativos.proyecto2.utils.Printer;

/**
 *
 * @author sebas
 */
public class Simulator {
    final int NUM_BLOCKS;
    public boolean[] blockFree;
    public Folder rootFolder = new Folder("root");
    public Folder currentFolder = rootFolder;
    
    public Simulator() {
        this.NUM_BLOCKS = 128;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
    }
    
    public Simulator(int num) {
        this.NUM_BLOCKS = num;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
    }
    
    /*
     *   Simulator Methods
     */
    
    /* Relative */
    
    public void writeFile(String fileName, int blockSize) {
        currentFolder.writeFile(fileName, blockSize, NUM_BLOCKS, blockFree);
    }
    
    public void deleteFile(String fileName) {
        currentFolder.deleteFile(fileName, NUM_BLOCKS, blockFree);
    }
    
    public void createFolder(String name) {
        currentFolder.createFolder(name);
    }
    
    public Simulator getFolder(String name) {
        Folder newFolder = currentFolder.getFolder(name);
        if (newFolder != null) currentFolder = newFolder;
        return this;
    }
    
    public void deleteFolder(String name) {
        currentFolder.deleteFolder(name, NUM_BLOCKS, blockFree);
    }
    
    /* From root */
    
    public void writeFileFromRoot(String fileName, int blockSize) {
        currentFolder = rootFolder;
        currentFolder.writeFile(fileName, blockSize, NUM_BLOCKS, blockFree);
    }
    
    public void deleteFileFromRoot(String fileName) {
        currentFolder = rootFolder;
        currentFolder.deleteFile(fileName, NUM_BLOCKS, blockFree);
    }
    
    public void createFolderFromRoot(String name) {
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
        currentFolder = rootFolder;
        currentFolder.deleteFolder(name, NUM_BLOCKS, blockFree);
    }
}
