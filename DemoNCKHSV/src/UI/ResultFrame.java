package UI;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

@SuppressWarnings("unused")
public class ResultFrame extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static StyleContext cont = StyleContext.getDefaultStyleContext();
	final static AttributeSet attr1 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLUE);
	final static AttributeSet attr2 = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.decode("#CD00CD"));
	final static AttributeSet attrBlack = cont.addAttribute(cont.getEmptySet(), StyleConstants.Foreground, Color.BLACK);
//	private JFrame mainFrame;
	JTextPane editor;

//	public ResultFrame() {
//		mainFrame = new JFrame("Java Swing Examples");
//		mainFrame.setTitle("Result");
//		mainFrame.setSize(700, 500);
//		mainFrame.setUndecorated(false);
//		ImageIcon img = new ImageIcon("C:\\Users\\DatTran\\workspace\\UI_Domain\\src\\download.jpg");
//		mainFrame.setIconImage(img.getImage());
//
//		// Editor
//		DefaultStyledDocument doc = createColorPattern();
//		editor = new JTextPane(doc);
//		JScrollPane scrollPane = new JScrollPane(editor);
//		TextLineNumber tln = new TextLineNumber(editor);
//		scrollPane.setRowHeaderView(tln);
//		
//		mainFrame.add(scrollPane);
//		mainFrame.setVisible(true);
//	}

	private static int findLastNonWordChar(String text, int index) {
		while (--index >= 0) {
			if (String.valueOf(text.charAt(index)).matches("\\W")) {
				break;
			}
		}
		return index;
	}

	private static int findFirstNonWordChar(String text, int index) {
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
	public static DefaultStyledDocument createColorPattern() {

		@SuppressWarnings("serial")
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
						if (text.substring(wordL, wordR).matches("(\\W)*(boolean|char|byte|short|int|long|float|double|return)")){
							setCharacterAttributes(wordL, wordR - wordL, attr1, false);
						} else if(text.substring(wordL, wordR).matches("(\\W)*(String|Array|public|private|protected|void|class|break|continue|if|while|for|do|else|switch|static)")){
							setCharacterAttributes(wordL, wordR - wordL, attr2, false);
						}
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

				if (text.substring(before, after).matches("(\\W)*(boolean|char|byte|short|int|long|float|double|return)")) {
					setCharacterAttributes(before, after - before, attr1, false);
				} else if(text.substring(before, after).matches("(\\W)*(String|Array|public|private|protected|void|class|break|continue|if|while|for|do|else|switch|static)")){
					setCharacterAttributes(before, after - before, attr2, false);
				} else {
					setCharacterAttributes(before, after - before, attrBlack, false);
				}
			}
		};
		return doc;
	}
}
