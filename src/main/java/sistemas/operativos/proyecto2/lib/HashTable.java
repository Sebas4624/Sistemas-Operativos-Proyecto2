package sistemas.operativos.proyecto2.lib;

/**
 *
 * @author sebas
 * @param <T>
 */
public class HashTable<T> {
    public LinkedList<T>[] list;
    public int tSize;
    
    public HashTable(int size) {
        this.list = new LinkedList[size];
        tSize = size;
        
        for(int i = 0; i < tSize; i++) {
            this.list[i] = new LinkedList();
        }
    }
    
    public int assign(String specStr) {
        int strSize = specStr.length();
        
        int assignation = 0;
        
        for(int i = 0; i < strSize; i++) {
            char Ch = specStr.charAt(i);
            int Ascii = Ch;
            
            assignation += Ascii;
        }
        
        int selection = assignation % tSize;
        
        return selection;
    }
    
    public void insert(T value, String key) {
        int cell = assign(key);
        
        this.list[cell].addLast(value);
    }
    
    public void remove(T value, String key) {
        int cell = assign(key);
        
        this.list[cell].remove(value);
    }
    
    public T search(T value, String key) {
        int assign = assign(key);
        T res = null;
        
        for (int i = 0; i < this.list[assign].size(); i++) {
            T temp = this.list[assign].get(i);
            if (this.list[assign].get(i) == value) {
                res = temp; 
            }
        }
        
        return res;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder().append('[');
        sb.append('\n');
        
        for (LinkedList<T> linkList : this.list) {
            sb.append("    ");
            sb.append(linkList.toString());
            sb.append('\n');
        }
        
        sb.append(']');
        return sb.toString();
    }
}
