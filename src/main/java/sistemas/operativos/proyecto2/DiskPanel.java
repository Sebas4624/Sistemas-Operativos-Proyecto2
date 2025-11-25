/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sistemas.operativos.proyecto2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import sistemas.operativos.proyecto2.simulator.Simulator;

/**
 *
 * @author nicolepinto
 */
public class DiskPanel extends JPanel {

    private final Simulator sim;

    private static final int COLS = 16;
    private static final int BLOCK_SIZE = 18;
    private static final int PADDING = 4;

    public DiskPanel(Simulator sim) {
        this.sim = sim;

        int numBlocks = sim.getNumBlocks();
        int rows = (int) Math.ceil(numBlocks / (double) COLS);

        int width  = COLS * (BLOCK_SIZE + PADDING) + PADDING;
        int height = rows * (BLOCK_SIZE + PADDING) + PADDING + 25;

        setPreferredSize(new Dimension(width, height));
        setBackground(new Color(51, 51, 51));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int numBlocks = sim.getNumBlocks();
        boolean[] free = sim.blockFree;   

        int x0 = PADDING;
        int y0 = PADDING;

        for (int i = 0; i < numBlocks; i++) {
            int row = i / COLS;
            int col = i % COLS;

            int x = x0 + col * (BLOCK_SIZE + PADDING);
            int y = y0 + row * (BLOCK_SIZE + PADDING);

            if (free[i]) {
                g.setColor(new Color(0, 170, 0));  
            } else {
                g.setColor(new Color(200, 40, 40)); 
            }
            g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);


            g.setColor(Color.BLACK);
            g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);


            g.setColor(Color.WHITE);
            g.setFont(g.getFont().deriveFont(9f));
            g.drawString(String.valueOf(i), x + 2, y + BLOCK_SIZE - 4);
        }

        int rowsUsed = (int) Math.ceil(numBlocks / (double) COLS);
        int legendY = y0 + rowsUsed * (BLOCK_SIZE + PADDING) + 10;


        g.setColor(new Color(0, 170, 0));
        g.fillRect(x0, legendY, BLOCK_SIZE, BLOCK_SIZE);
        g.setColor(Color.WHITE);
        g.drawString("Libre", x0 + BLOCK_SIZE + 5, legendY + BLOCK_SIZE - 4);


        g.setColor(new Color(200, 40, 40));
        g.fillRect(x0 + 80, legendY, BLOCK_SIZE, BLOCK_SIZE);
        g.setColor(Color.WHITE);
        g.drawString("Ocupado", x0 + 80 + BLOCK_SIZE + 5, legendY + BLOCK_SIZE - 4);
    }
}
