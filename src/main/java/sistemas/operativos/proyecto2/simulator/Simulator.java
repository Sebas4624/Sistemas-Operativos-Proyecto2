package sistemas.operativos.proyecto2.simulator;

import java.util.Arrays;
import sistemas.operativos.proyecto2.lib.LinkedList;

/**
 *
 * @author sebas
 */
public class Simulator {
    final int BLOCK_SIZE;
    final int NUM_BLOCKS;
    byte[][] storage;
    boolean[] blockFree;
    LinkedList<Integer> fileBlock = new LinkedList<>();
    
    public Simulator() {
        this.BLOCK_SIZE = 4096;
        this.NUM_BLOCKS = 128;
        this.storage = new byte[NUM_BLOCKS][BLOCK_SIZE];
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
    }
    
    public Simulator(int block, int num) {
        this.BLOCK_SIZE = block;
        this.NUM_BLOCKS = num;
        this.storage = new byte[NUM_BLOCKS][BLOCK_SIZE];
        this.blockFree = new boolean[NUM_BLOCKS];
        Arrays.fill(blockFree, true);
    }
}
