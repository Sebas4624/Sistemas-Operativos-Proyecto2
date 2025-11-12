package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import sistemas.operativos.proyecto2.file.FileMetadata;
import sistemas.operativos.proyecto2.lib.HashTable;
import sistemas.operativos.proyecto2.lib.LinkedList;
import sistemas.operativos.proyecto2.utils.Printer;

/**
 *
 * @author sebas
 */
public class Simulator {
    final int NUM_BLOCKS;
    public boolean[] blockFree;
    LinkedList<Integer> fileBlock = new LinkedList();
    public HashTable<FileMetadata> fileMeta;
    
    public Simulator() {
        this.NUM_BLOCKS = 128;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
        this.fileMeta = new HashTable((int) Math.ceil(NUM_BLOCKS / 4));
    }
    
    public Simulator(int num) {
        this.NUM_BLOCKS = num;
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
        this.fileMeta = new HashTable((int) Math.ceil(NUM_BLOCKS / 4));
    }
    
    /*
     *   Simulator Methods
     */
    
    public void writeFile(String fileName, int blockSize) {
        LinkedList<Integer> assignedBlocks = new LinkedList<>();
        boolean canCreateFile = false;
        
        for (int i = 0, j = 0; i < NUM_BLOCKS && j < NUM_BLOCKS; i++) {
            if (blockFree[i]) {
                j++;
            }
            if (j >= blockSize) {
                canCreateFile = true;
                break;
            }
        }
        
        if (canCreateFile) {    
            for (int i = 0, j = 0; i < NUM_BLOCKS && j < blockSize; i++) {
                if (blockFree[i]) {
                    blockFree[i] = false;
                    assignedBlocks.add(i);
                    j++;
                }
            }

            FileMetadata newFileMeta = new FileMetadata(fileName, blockSize, assignedBlocks);

            fileMeta.insert(newFileMeta, fileName);
        } else {
            Printer.print("Cannot create \"" + fileName + "\" due to insufficient space");
        }
    }
    
    public void deleteFile(String fileName) {
        LinkedList<FileMetadata> metadataList = fileMeta.getKeyList(fileName);
        FileMetadata metadata = null;
        
        for (int i = 0; i < metadataList.size(); i++) {
            FileMetadata res = metadataList.get(i);
            
            if (res != null && res.getFileName().equals(fileName)) {
                metadata = res;
                break;
            }
        }
        
        if (metadata != null) {
            for (int i = 0; i < metadata.getBlockIndices().size(); i++) {
                int index = metadata.getBlockIndices().get(i);
                if (!blockFree[index]) {
                    blockFree[index] = true;
                }
            }
        }
        
        fileMeta.remove(metadata, fileName);
    }
}
