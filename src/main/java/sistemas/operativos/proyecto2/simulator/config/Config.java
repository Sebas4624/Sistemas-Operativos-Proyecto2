package sistemas.operativos.proyecto2.simulator.config;

/**
 *
 * @author sebas
 */
public class Config {
    final int NUM_BLOCKS;
    private Policy policy;
    
    private final String configFile = "system_config.json";
    
    public Config() {
        this.NUM_BLOCKS = 256;
        this.policy = Policy.FIFO;
    }
    
    public Config(int NUM_BLOCKS) {
        this.NUM_BLOCKS = NUM_BLOCKS;
        this.policy = Policy.FIFO;
    }
    
    public int getNumBlocks() { return this.NUM_BLOCKS; }
    
    public Policy getPolicy() { return this.policy; }
    public void setPolicy(Policy newPolicy) {
        if (newPolicy != null) this.policy = newPolicy;
    } 
}
