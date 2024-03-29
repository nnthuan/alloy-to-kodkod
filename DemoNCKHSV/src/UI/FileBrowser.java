package UI;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

@SuppressWarnings("unused")
public class FileBrowser implements Runnable {

    private DefaultMutableTreeNode root;

    private DefaultTreeModel treeModel;

    private JTree tree;
    
    private String fileRootName;

    public FileBrowser(String fileRootName) {
    	this.fileRootName = fileRootName;
    }
    @Override
    public void run() {
        JFrame frame = new JFrame(fileRootName);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        File fileRoot = new File(fileRootName);
        root = new DefaultMutableTreeNode(new FileNode(fileRoot));
        treeModel = new DefaultTreeModel(root);

        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        JScrollPane scrollPane = new JScrollPane(tree);

        frame.add(scrollPane);
        //frame.setLocationByPlatform(true);
        frame.setLocationRelativeTo(null);
        frame.setSize(640, 480);
        frame.setVisible(true);

        CreateChildNodes ccn = 
                new CreateChildNodes(fileRoot, root);
        new Thread(ccn).start();
    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new FileBrowser());
//    }
}