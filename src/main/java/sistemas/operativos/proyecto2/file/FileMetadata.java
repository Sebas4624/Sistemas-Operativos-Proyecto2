package sistemas.operativos.proyecto2.file;

import sistemas.operativos.proyecto2.lib.LinkedList;

/**
 *
 * @author sebas
 */
public class FileMetadata {
    private String fileName;
    private int fileSize;
    private LinkedList<Integer> blockIndices;
    final private long creationTime;
    private long lastModifiedTime;

    public FileMetadata(String fileName, int fileSize, LinkedList<Integer> blockIndices) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.blockIndices = blockIndices;
        this.creationTime = System.currentTimeMillis();
        this.lastModifiedTime = this.creationTime;
    }
    
    public String getFileName() { return this.fileName; }
    public int getFileSize() { return this.fileSize; }
    public LinkedList<Integer> getBlockIndices() { return this.blockIndices; }
    public long getCreationTime() { return this.creationTime; }
    public long getLastModifiedTime() { return this.lastModifiedTime; }
    
    public void setFileName(String newName) {
        this.fileName = newName;
    }
    
    public void setFileSize(int newSize) {
        this.fileSize = newSize;
    }
    
    public void setLastModifiedTime(long newTime) {
        this.lastModifiedTime = newTime;
    }
    
    public void emptyBlockIndices() {
        this.blockIndices = new LinkedList();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('(');
        
        sb.append(this.fileName);
        sb.append(", ");
        
        sb.append(this.fileSize);
        sb.append(", ");
        
        sb.append(this.creationTime);
        sb.append(", ");
        
        sb.append(this.lastModifiedTime);
        sb.append(", ");
        
        sb.append("Assigned blocks: ").append(this.blockIndices.toString());
        
        sb.append(')');
        return sb.toString();
    }
}
