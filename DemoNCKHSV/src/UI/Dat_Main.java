package UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import GenerateRule.TransFactAlloyToKodKod;
import Parser.Alloy2JavaParser;

/**
 * 
 * @author DatTran
 *
 */
@SuppressWarnings({ "serial", "unused" })
public class Dat_Main extends JFrame {

	final StyleContext cont = StyleContext.getDefaultStyleContext();
	// final AttributeSet attr = cont.addAttribute(cont.getEmptySet(),
	// StyleConstants.Foreground, Color.RED);
	final AttributeSet attr1 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
	final AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
	private JFrame mainFrame;
	JTextPane editor; /* , editor1; */
	// JFrame result;
	JPanel rightPanel;
	JToolBar toolbar;
	JButton btnNew, btnOpen, btnSave, btnCopy, btnCut, btnSetting, btnAbout, btnIncrease, btnDecrease, btnParser,
			btnPast;
	JButton btnFileChooser;
	Font font;
	String inputFile = null;
	String outputFolder = null;

	// This is methob draw UI
	public Dat_Main() {
		mainFrame = new JFrame("Java Swing Examples");
		mainFrame.setTitle("Alloy2Java Editor");
		mainFrame.setSize(500, 500);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setUndecorated(false);
		mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		ImageIcon img = new ImageIcon("C:\\Users\\DatTran\\workspace\\UI_Domain\\src\\download.jpg");
		mainFrame.setIconImage(img.getImage());
		mainFrame.setLayout(new BorderLayout());

		// Editor
		DefaultStyledDocument doc = createColorPattern();
		editor = new JTextPane(doc);
		JScrollPane scrollPane = new JScrollPane(editor);
		TextLineNumber tln = new TextLineNumber(editor);
		scrollPane.setRowHeaderView(tln);

		// ToolBar
		toolbar = new JToolBar("Applications");
		btnNew = new JButton(new ImageIcon("1478208162_document-new.png"));
		btnNew.setToolTipText("New");
		btnOpen = new JButton(new ImageIcon("1478205860_Open_file.png"));
		btnOpen.setToolTipText("Open");
		btnSave = new JButton(new ImageIcon("1478205921_Save.png"));
		btnSave.setToolTipText("Save");
		btnCopy = new JButton(new ImageIcon("1478205958_Copy.png"));
		btnCopy.setToolTipText("Copy");
		btnCut = new JButton(new ImageIcon("1478205993_Cut.png"));
		btnCut.setToolTipText("Cut");
		btnSetting = new JButton(new ImageIcon("1478206091_Settings_2.png"));
		btnSetting.setToolTipText("Setting");
		btnAbout = new JButton(new ImageIcon("1478206134_19.png"));
		btnAbout.setToolTipText("About");
		btnIncrease = new JButton(new ImageIcon("1478206402_stock_increase-font.png"));
		btnIncrease.setToolTipText("Increase");
		btnDecrease = new JButton(new ImageIcon("1478206454_stock_decrease-font.png"));
		btnDecrease.setToolTipText("Decrease");
		btnParser = new JButton(new ImageIcon("1478556632_Parser.png"));
		btnParser.setToolTipText("Parse");
		btnPast = new JButton(new ImageIcon("1478559800_Past.png"));
		btnPast.setToolTipText("Paste");
		btnFileChooser = new JButton(new ImageIcon("1478205860_Open_file.png"));
		btnFileChooser.setToolTipText("Write Folder");

		toolbar.add(btnNew);
		toolbar.add(btnOpen);
		toolbar.add(btnSave);
		toolbar.addSeparator();
		toolbar.add(btnCopy);
		toolbar.add(btnCut);
		toolbar.add(btnPast);
		toolbar.addSeparator();
		toolbar.add(btnSetting);
		toolbar.add(btnAbout);
		toolbar.addSeparator();
		toolbar.add(btnIncrease);
		toolbar.add(btnDecrease);
		Dimension d = new Dimension(30, 30);
		toolbar.addSeparator(d);
		toolbar.add(btnParser);
		toolbar.add(btnFileChooser);

		mainFrame.getContentPane().add(toolbar, BorderLayout.PAGE_START);

		// Menu
		mainFrame.add(scrollPane, BorderLayout.CENTER);

		// DefaultStyledDocument doc1 = ResultFrame.createColorPattern();
		// editor1 = new JTextPane(doc1);
		// editor1.setEditable(false);
		// JScrollPane scrollPane1 = new JScrollPane(editor1);
		// TextLineNumber tln1 = new TextLineNumber(editor1);
		// scrollPane1.setRowHeaderView(tln1);
		// scrollPane1.setPreferredSize(new Dimension(700, 700));
		// mainFrame.add(scrollPane1, BorderLayout.EAST);
		// result = new JFrame();
		// rightPanel = new JPanel();
		// rightPanel.add(result);
		// rightPanel.setPreferredSize(new Dimension(700, 700));
		// mainFrame.add(rightPanel, BorderLayout.EAST);
		// // mainFrame.add(result);
		// mainFrame.setVisible(true);
	}

	private int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private int findFirstNonWordChar(String text, int index) {
		while (index < text.length()) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
			index++;
		}
		return index;
	}

	/**
	 * Create pattern with color
	 * 
	 * @return
	 */
	public DefaultStyledDocument createColorPattern() {

		DefaultStyledDocument doc = new DefaultStyledDocument() {
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offset);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offset + str.length());
				int wordL = before;
				int wordR = before;

				while (wordR <= after) {
					if (wordR == after || String.valueOf(text.charAt(wordR)).matches("\\W")) {
						if (text.substring(wordL, wordR).matches(
								"(\\W)*(var|module|sig|lone|pred|run|fact|no|all|some|in|abstract|extends|for|exactly|assert|and|implies|check|Int|int|one|lone|)")) {
							setCharacterAttributes(wordL, wordR - wordL, attr1, false);
						}
						// else if(text.substring(wordL,
						// wordR).matches("(\\W)*(DAT)")){
						// setCharacterAttributes(wordL, wordR - wordL, attr1,
						// false);
						// }
						else {
							setCharacterAttributes(wordL, wordR - wordL, attrBlack, false);
						}
						wordL = wordR;
					}
					wordR++;
				}
			}

			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);

				String text = getText(0, getLength());
				int before = findLastNonWordChar(text, offs);
				if (before < 0)
					before = 0;
				int after = findFirstNonWordChar(text, offs);

				if (text.substring(before, after).matches(
						"(\\W)*(module|sig|lone|pred|run|fact|no|all|some|in|abstract|extends|for|exactly|assert|and|implies|check)")) {
					setCharacterAttributes(before, after - before, attr1, false);
				}
				// else if(text.substring(before,
				// after).matches("(\\W)*(DAT)")){
				// setCharacterAttributes(before, after - before, attr1, false);
				// }
				else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};

		return doc;
	}

	private void showMenuDemo() {
		// create a menu bar
		final JMenuBar menuBar = new JMenuBar();

		// create menus
		JMenu fileMenu = new JMenu("File");
		JMenu editMenu = new JMenu("Edit");
		final JMenu aboutMenu = new JMenu("About");
		final JMenu linkMenu = new JMenu("Links");

		// create menu items
		JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setMnemonic(KeyEvent.VK_N);
		newMenuItem.setActionCommand("New");

		JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setActionCommand("Open");

		JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setActionCommand("Save");

		JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setActionCommand("Exit");

		JMenuItem cutMenuItem = new JMenuItem("Cut");
		cutMenuItem.setActionCommand("Cut");

		JMenuItem copyMenuItem = new JMenuItem("Copy");
		copyMenuItem.setActionCommand("Copy");

		JMenuItem pasteMenuItem = new JMenuItem("Paste");
		pasteMenuItem.setActionCommand("Paste");

		// add menu items to menus
		fileMenu.add(newMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.addSeparator();
		fileMenu.add(exitMenuItem);
		editMenu.add(cutMenuItem);
		editMenu.add(copyMenuItem);
		editMenu.add(pasteMenuItem);

		// add menu to menubar
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(aboutMenu);
		menuBar.add(linkMenu);

		exitMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		newMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setText("");
			}
		});

		openMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = c.getSelectedFile();
						String content = readFile(selectedFile.getAbsolutePath(), StandardCharsets.UTF_8);
						editor.setText(content);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(mainFrame, "You choose
					// Cancel");
				}
			}
		});

		saveMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						File file = c.getSelectedFile();
						PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
						writer.println(editor.getText());
						writer.close();
					} catch (Exception e1) {
						// TODO: handle exception
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(mainFrame, "Save Cancel");
				}
			}
		});

		copyMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.copy();
			}
		});

		cutMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.cut();
			}
		});

		pasteMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.paste();
			}
		});

		// add menubar to the frame
		mainFrame.setJMenuBar(menuBar);
		mainFrame.setVisible(true);
	}

	// -------------------------------------------------------------------------------------------------------------------------------------------------
	public static void main(String[] args) {
		Dat_Main dat = new Dat_Main();
		dat.showMenuDemo();
		dat.createAction();
	}

	// -----------------------------------------------------------------------
	public String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public void createAction() {

		btnParser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (inputFile == null || outputFolder == null) {
					JOptionPane.showMessageDialog(mainFrame, "Missing als file or output folder path.");
				} else {
					Alloy2JavaParser p = new Alloy2JavaParser(outputFolder, inputFile);
					//generate rule
					TransFactAlloyToKodKod transFact = new TransFactAlloyToKodKod();
					
					if (p.error == true) {
						JOptionPane.showMessageDialog(mainFrame, "There might be some errors in als files.");
					} else {
						p.createModelFromAlloy();
						transFact.execute(inputFile, outputFolder);
						if (p.error == true)
							JOptionPane.showMessageDialog(mainFrame, "Failed to create Model.");
						else if(transFact.error==true){
							JOptionPane.showMessageDialog(mainFrame, "Faild to create Rule");
						}
						else {
							SwingUtilities.invokeLater(new FileBrowser(outputFolder));
							
						}
					}
				}
			}
		});

		btnFileChooser.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				c.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int rVal = c.showOpenDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					File selectedFile = c.getSelectedFile();
					outputFolder = selectedFile.getAbsolutePath();
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(mainFrame, "You choose
					// Cancel");
				}
			}
		});

		btnNew.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.setText("");
			}
		});

		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				int rVal = c.showOpenDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						File selectedFile = c.getSelectedFile();
						String content = readFile(selectedFile.getAbsolutePath(), StandardCharsets.UTF_8);
						inputFile = selectedFile.getAbsolutePath();
						editor.setText(content);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					// JOptionPane.showMessageDialog(mainFrame, "You choose
					// Cancel");
				}
			}
		});

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser c = new JFileChooser();
				// Demonstrate "Save" dialog:
				int rVal = c.showSaveDialog(mainFrame);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					try {
						File file = c.getSelectedFile();
						PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
						writer.println(editor.getText());
						writer.close();
					} catch (Exception e1) {
						// TODO: handle exception
					}
				}
				if (rVal == JFileChooser.CANCEL_OPTION) {
					JOptionPane.showMessageDialog(mainFrame, "Save Cancel");
				}
			}
		});

		btnCopy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.copy();
			}
		});

		btnCut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.cut();
			}
		});

		btnPast.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				editor.paste();
			}
		});

		btnSetting.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		btnIncrease.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Font f = editor.getFont();
				font = new Font(f.getFontName(), f.getStyle(), f.getSize() + 3);
				editor.setFont(font);
			}
		});

		btnDecrease.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Font f = editor.getFont();
				font = new Font(f.getFontName(), f.getStyle(), f.getSize() - 3);
				editor.setFont(font);
			}
		});
	}

}
