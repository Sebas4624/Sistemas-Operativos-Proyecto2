package sistemas.operativos.proyecto2;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import sistemas.operativos.proyecto2.file.FileMetadata;
import sistemas.operativos.proyecto2.file.Folder;
import sistemas.operativos.proyecto2.lib.LinkedList;
import sistemas.operativos.proyecto2.simulator.Simulator;
import sistemas.operativos.proyecto2.simulator.Simulator.UserMode;

/**
 *
 * @author nicolepinto
 */

public class MainFrame extends JFrame {

    private final Simulator sim;
    
    private JRadioButton rbAdmin;
    private JRadioButton rbUser;
    private JButton btnCreateFile;
    private JButton btnDeleteFile;
    private JButton btnCreateFolder;
    private JButton btnDeleteFolder;
    private JButton btnShowState;
    private JTextField txtFileName;
    private JTextField txtFileSize;
    private JTextField txtFolderName;
    private JTextArea txtOutput;
    private Container fileExplorer;
    private JTree tree;
    private DefaultMutableTreeNode rootNode;

    public MainFrame() {
        super("Proyecto 2 - Sistema de Archivos");
        this.sim = new Simulator();      // simulador por defecto (128 bloques)
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        populateSim();
        setLocationRelativeTo(null);     
    }

    private void initComponents() {
        //Panel de modo (Admin / Usuario) 
        rbAdmin = new JRadioButton("Administrador");
        rbUser  = new JRadioButton("Usuario");

        ButtonGroup group = new ButtonGroup();
        group.add(rbAdmin);
        group.add(rbUser);

        rbAdmin.setSelected(true);
        sim.setMode(UserMode.ADMIN);

        rbAdmin.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sim.setMode(UserMode.ADMIN);
                updateButtonsByMode();
                showState();
            }
        });

        rbUser.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                sim.setMode(UserMode.USER);
                updateButtonsByMode();
                showState();
            }
        });

        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modePanel.setBorder(BorderFactory.createTitledBorder("Modo de uso"));
        modePanel.add(rbAdmin);
        modePanel.add(rbUser);

        //Panel de archivos 
        txtFileName = new JTextField(10);
        txtFileSize = new JTextField(5);
        btnCreateFile = new JButton("Crear archivo");
        btnDeleteFile = new JButton("Eliminar archivo");

        btnCreateFile.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String name = txtFileName.getText().trim();
                String sizeStr = txtFileSize.getText().trim();

                if (name.isEmpty() || sizeStr.isEmpty()) {
                    showMessage("Debes indicar nombre y tamaño del archivo.");
                    return;
                }

                try {
                    int size = Integer.parseInt(sizeStr);
                    sim.writeFile(name, size);
                    showState();
                } catch (NumberFormatException ex) {
                    showMessage("El tamaño debe ser un número entero (bloques).");
                }
            }
        });

        btnDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String name = txtFileName.getText().trim();
                if (name.isEmpty()) {
                    showMessage("Debes indicar el nombre del archivo a eliminar.");
                    return;
                }
                sim.deleteFile(name);
                showState();
            }
        });

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filePanel.setBorder(BorderFactory.createTitledBorder("Archivos (carpeta actual)"));
        filePanel.add(new JLabel("Nombre:"));
        filePanel.add(txtFileName);
        filePanel.add(new JLabel("Tamaño (bloques):"));
        filePanel.add(txtFileSize);
        filePanel.add(btnCreateFile);
        filePanel.add(btnDeleteFile);

        // Panel de carpetas 
        txtFolderName = new JTextField(10);
        btnCreateFolder = new JButton("Crear carpeta");
        btnDeleteFolder = new JButton("Eliminar carpeta");

        btnCreateFolder.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String name = txtFolderName.getText().trim();
                if (name.isEmpty()) {
                    showMessage("Debes indicar el nombre de la carpeta.");
                    return;
                }
                sim.createFolder(name);
                showState();
            }
        });

        btnDeleteFolder.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String name = txtFolderName.getText().trim();
                if (name.isEmpty()) {
                    showMessage("Debes indicar el nombre de la carpeta a eliminar.");
                    return;
                }
                sim.deleteFolder(name);
                showState();
            }
        });

        JPanel folderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        folderPanel.setBorder(BorderFactory.createTitledBorder("Carpetas (carpeta actual)"));
        folderPanel.add(new JLabel("Nombre:"));
        folderPanel.add(txtFolderName);
        folderPanel.add(btnCreateFolder);
        folderPanel.add(btnDeleteFolder);

        
        txtOutput = new JTextArea(15, 60);
        txtOutput.setEditable(false);
        JScrollPane scroll = new JScrollPane(txtOutput);
        rootNode = new DefaultMutableTreeNode("root");
        tree = new JTree(rootNode);
        fileExplorer = getContentPane();
        
        fileExplorer.add(new JScrollPane(tree));

        btnShowState = new JButton("Mostrar estado");
        btnShowState.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                showState();
            }
        });

        //  Layout principal 
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(modePanel);
        northPanel.add(filePanel);
        northPanel.add(folderPanel);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(fileExplorer, BorderLayout.CENTER);
        mainPanel.add(btnShowState, BorderLayout.SOUTH);

        setContentPane(mainPanel);

     
        updateButtonsByMode();
        showState();
    }

    private void updateButtonsByMode() {
        boolean admin = (sim.getMode() == UserMode.ADMIN);
        btnCreateFile.setEnabled(admin);
        btnDeleteFile.setEnabled(admin);
        btnCreateFolder.setEnabled(admin);
        btnDeleteFolder.setEnabled(admin);
    }

    private void showState() {
        StringBuilder sb = new StringBuilder();
        sb.append("Modo actual: ").append(sim.getMode()).append("\n\n");
        sb.append("Estructura de carpetas (desde root):\n");
        sb.append(sim.rootFolder.toString()).append("\n\n");
        sb.append("Bloques libres / ocupados:\n");
        sb.append(Arrays.toString(sim.blockFree)).append("\n");
        txtOutput.setText(sb.toString());
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
    
    private void addFile(DefaultMutableTreeNode node, FileMetadata reg) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(reg.getFileName());
        
        node.add(newNode);
    }
    
    private void addFolder(DefaultMutableTreeNode node, Folder reg) {
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(reg.getName());
        
        LinkedList<Folder> folders = reg.getSubfolders();
        LinkedList<FileMetadata> files = reg.getFiles();
        
        for (int i = 0; i < files.size(); i++) {
            addFile(newNode, files.get(i));
        }
        
        for (int i = 0; i < folders.size(); i++) {
            addFolder(newNode, folders.get(i));
        }
        
        node.add(newNode);
    }
    
    private void updateJTree() {
        DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
        DefaultMutableTreeNode rootFolder = (DefaultMutableTreeNode)model.getRoot();
        
        LinkedList<Folder> folders = this.sim.rootFolder.getSubfolders();
        LinkedList<FileMetadata> files = this.sim.rootFolder.getFiles();
        
        for (int i = 0; i < files.size(); i++) {
            addFile(rootFolder, files.get(i));
        }
        
        for (int i = 0; i < folders.size(); i++) {
            addFolder(rootFolder, folders.get(i));
        }
        
        model.reload(rootFolder);
    }
    
    /**
     * Test Func
     */
    private void populateSim() {
        sim.writeFile("Test1", 2);
        sim.writeFile("Test2", 5);
        sim.writeFile("Test3", 3);
        sim.createFolder("Testeos");
        sim.createFolder("Locos");
        //sim.deleteFolder("Locos");
        //sim.writeFile("Test4", 1284, "");
        
        //Printer.print(sim.rootFolder.toString());

        //sim.deleteFile("Test2");
        sim.getFolder("Testeos").writeFile("Test4", 3);
        sim.getFolder("Testeos").writeFile("Test5", 4);
        sim.getFolder("Testeos").writeFile("Test6", 1);
        
        sim.getFolder("Testeos").deleteFile("Test4");
        
        updateJTree();
    }
}