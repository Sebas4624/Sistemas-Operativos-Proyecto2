package sistemas.operativos.proyecto2.file;

import sistemas.operativos.proyecto2.lib.HashTable;
import sistemas.operativos.proyecto2.lib.LinkedList;
import sistemas.operativos.proyecto2.utils.Printer;

/**
 *
 * @author sebas
 */
public class Folder {
    private String name;
    final private HashTable<Folder> subfolders;
    final private HashTable<FileMetadata> files;
    final private long creationTime;
    private long lastModifiedTime;

    public Folder(String name) {
        this.name = name;
        this.subfolders = new HashTable(16);
        this.files = new HashTable(16);
        this.creationTime = System.currentTimeMillis();
        this.lastModifiedTime = this.creationTime;
    }
    
    public void createFolder(String name) {
        Folder newFolder = new Folder(name);
        
        subfolders.insert(newFolder, name);
    }
    
    public Folder getFolder(String name) {
        LinkedList<Folder> resList = this.subfolders.getAllValues();
        Folder selected = this;
        
        for (int i = 0; i < resList.size(); i++) {
            Folder res = resList.get(i);
            
            if (res != null && res.getName().equals(name)) {
                selected = res;
                break;
            }
        }
        
        return selected;
    }
    
    public void deleteFolder(String name, int NUM_BLOCKS, boolean[] blockFree) {
        LinkedList<Folder> resList = this.subfolders.getAllValues();
        
        for (int i = 0; i < resList.size(); i++) {
            Folder res = resList.get(i);
            
            if (res != null && res.getName().equals(name)) {
                res.deleteAllFiles(NUM_BLOCKS, blockFree);
                res.deleteAllFolders(NUM_BLOCKS, blockFree);
                subfolders.remove(res, name);
                break;
            }
        }
    }
    
    public void deleteAllFolders(int NUM_BLOCKS, boolean[] blockFree) {
        for (int i = 0; i < subfolders.tSize; i++) {
            LinkedList<Folder> folderList = subfolders.list[i];
            
            for (int j = 0; j < folderList.size(); j++) {
                Folder res = folderList.get(j);
                String folderName;

                if (res != null) {
                    folderName = res.getName();
                    res.deleteAllFiles(NUM_BLOCKS, blockFree);
                    res.deleteAllFolders(NUM_BLOCKS, blockFree);
                    subfolders.remove(res, folderName);
                }
            }
        }
    }
    
    public void writeFile(String fileName, int blockSize, int NUM_BLOCKS, boolean[] blockFree) {
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

            files.insert(newFileMeta, fileName);
        } else {
            Printer.print("Cannot create \"" + fileName + "\" due to insufficient space");
        }
    }
    
    public void deleteFile(String fileName, int NUM_BLOCKS, boolean[] blockFree) {
        LinkedList<FileMetadata> metadataList = files.getKeyList(fileName);
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
        
        files.remove(metadata, fileName);
    }
    
    public void deleteAllFiles(int NUM_BLOCKS, boolean[] blockFree) { 
        for (int i = 0; i < files.list.length; i++) {
            LinkedList<FileMetadata> metadataList = files.list[i];
            
            for (int j = 0; j < metadataList.size(); j++) {
                FileMetadata res = metadataList.get(j);
                String fileName;
            
                if (res != null) {
                    fileName = res.getFileName();
                    for (int k = 0; k < res.getBlockIndices().size(); k++) {
                        int index = res.getBlockIndices().get(k);
                        if (!blockFree[index]) {
                            blockFree[index] = true;
                        }
                    }
                    
                    files.remove(res, fileName);
                }
            }
        }
    }
    
    /*
     *   Setters & Getters
     */
    
    public String getName() { return this.name; }
    public HashTable getSubfolderList() { return this.subfolders; }
    public HashTable getFilesList() { return this.files; }
    public long getCreationTime() { return this.creationTime; }
    public long getLastModifiedTime() { return this.lastModifiedTime; }
    
    public LinkedList<Folder> getSubfolders() {
        return this.subfolders.getAllValues();
    }
    
    public LinkedList<FileMetadata> getFiles() {
        return this.files.getAllValues();
    }
    
    public FileMetadata getFile(String name) {
        LinkedList<FileMetadata> list = this.files.getAllValues();
        
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getFileName() == null ? name == null : list.get(i).getFileName().equals(name)) {
                return list.get(i);
            }
        }
        
        return null;
    }
            
    public void setName(String newName) {
        this.name = newName;
    }
    
    public void setLastModifiedTime(long newTime) {
        this.lastModifiedTime = newTime;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name);
        sb.append('\n');
        sb.append(this.name).append(" Subfolders");
        sb.append('\n');
        sb.append(this.subfolders.toString());
        sb.append('\n');
        sb.append(this.name).append(" Files");
        sb.append('\n');
        sb.append(this.files.toString());
        return sb.toString();
    }
}
