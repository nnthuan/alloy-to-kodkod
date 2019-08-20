package UI;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

public class TextLine extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TextLine frame = new TextLine();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TextLine() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 540, 425);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JTextArea textArea = new JTextArea();
        JScrollPane pane1 = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        TextLineNumber tln1 = new TextLineNumber(textArea);
        pane1.setRowHeaderView(tln1);
        contentPane.add(pane1, null);

        JLabel lblNewLabel = new JLabel("                 Text Line Example");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
                gl_contentPane
                        .createSequentialGroup()
                        .addContainerGap(88, Short.MAX_VALUE)
                        .addGroup(
                                gl_contentPane
                                        .createParallelGroup(Alignment.LEADING)
                                        .addGroup(
                                                Alignment.TRAILING,
                                                gl_contentPane
                                                        .createSequentialGroup()
                                                        .addComponent(pane1, GroupLayout.PREFERRED_SIZE, 383,
                                                                GroupLayout.PREFERRED_SIZE).addGap(43))
                                        .addGroup(
                                                Alignment.TRAILING,
                                                gl_contentPane
                                                        .createSequentialGroup()
                                                        .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 333,
                                                                GroupLayout.PREFERRED_SIZE).addGap(72)))));
        gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
                Alignment.TRAILING,
                gl_contentPane.createSequentialGroup()
                        .addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pane1, GroupLayout.PREFERRED_SIZE, 278, GroupLayout.PREFERRED_SIZE).addGap(42)));
        contentPane.setLayout(gl_contentPane);
    }
}
