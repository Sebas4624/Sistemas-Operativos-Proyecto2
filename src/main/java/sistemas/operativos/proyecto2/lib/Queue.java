package sistemas.operativos.proyecto2.lib;

import java.util.Random;
import sistemas.operativos.proyecto2.simulator.process.Process;

/**
 *
 * @author Sebastián
 * @param <T>
 */
public class Queue<T> implements Iterable<T> {
    private final LinkedList<T> list = new LinkedList<>();

    public Queue() { }

    public void enqueue(T value) { list.addLast(value); }

    public T dequeue() { return list.removeFirst(); }

    public T peek() { return list.size() == 0 ? null : list.get(0); }

    public int size() { return list.size(); }

    public boolean isEmpty() { return list.isEmpty(); }
    
    // Reverse
    
    public T dequeueReverse() { return list.removeLast(); }
    
    // Priority

    public Process peekPriority() {
        if (list.size() == 0) return null;
        
        Process best = null;
        
        for(T item : list) {
            if (item instanceof Process p) {
                if (best == null || p.priority() > best.priority()) {
                    best = p;
                }
            }
        }
        return best;
    }
    
    public Process pollPriority() {
        if (list.size() == 0) return null;
        
        Process best = null;
        T removal = null;
        
        for(T item : list) {
            if (item instanceof Process p) {
                if (best == null || p.priority() > best.priority()) {
                    best = p;
                    removal = item;
                }
            }
        }
        list.remove(removal);
        return best;
    }
    
    // Shortest Path
    
    public Process peekPath() {
        if (list.size() == 0) return null;
        
        Process best = null;
        
        for(T item : list) {
            if (item instanceof Process p) {
                if (best == null || p.getElementPath().length < best.getElementPath().length) {
                    best = p;
                }
            }
        }
        return best;
    }
    
    public Process pollPath() {
        if (list.size() == 0) return null;
        
        Process best = null;
        T removal = null;
        
        for(T item : list) {
            if (item instanceof Process p) {
                if (best == null || p.getElementPath().length < best.getElementPath().length) {
                    best = p;
                    removal = item;
                }
            }
        }
        list.remove(removal);
        return best;
    }
    
    // RANDOM
    
    public Process peekRand() {
        Random rand = new Random();
        if (list.size() == 0) return null;
        
        int randomValue = rand.nextInt(list.size());
        
        Process best = null;
        
        for(int i = 0; i < list.size(); i++) {
            if (i == randomValue && list.get(i) instanceof Process p) {
                if (best == null || p.getElementPath().length < best.getElementPath().length) {
                    best = p;
                }
            }
        }
        return best;
    }
    
    public Process pollRand() {
        Random rand = new Random();
        if (list.size() == 0) return null;
        
        int randomValue = rand.nextInt(list.size());
        
        Process best = null;
        T removal = null;
        
        for(int i = 0; i < list.size(); i++) {
            if (i == randomValue && list.get(i) instanceof Process p) {
                if (best == null || p.getElementPath().length < best.getElementPath().length) {
                    best = p;
                    removal = list.get(i);
                }
            }
        }
        list.remove(removal);
        return best;
    }

    @Override
    public java.util.Iterator<T> iterator() { return list.iterator(); }
    
    public LinkedList<T> toLinkedList() { return list; }
    
    @Override
    public String toString() { return list.toString(); }
}