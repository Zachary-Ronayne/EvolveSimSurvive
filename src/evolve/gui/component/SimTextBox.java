package evolve.gui.component;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * A JTestField set up in the style of the simulator
 */
public class SimTextBox extends JTextField{
	private static final long serialVersionUID = 1L;
	
	/**
	 * All text is allowed in the text box
	 */
	public static final int RESTRICT_NONE = 0;
	/**
	 * Only allow integers in the text box
	 */
	public static final int RESTRICT_INTEGER = 1;
	/**
	 * Only allow decimal place numbers
	 */
	public static final int RESTRICT_DOUBLE = 2;
	/**
	 * Only allow names that are valid for save files
	 */
	public static final int RESTRICT_FILE = 3;
	
	public SimTextBox(){
		super();
		SimConstants.setSimStyle(this);
		setFontSize(20);
		setCharWidth(200);
	}
	
	/**
	 * Only allow certain kinds of input to be used.
	 * @param type the type, use constants in this class for this value
	 */
	public void restrictInput(int type){
		//only allow integers
		if(type == RESTRICT_INTEGER){
			((AbstractDocument)getDocument()).setDocumentFilter(new DocumentFilter(){
				@Override
				public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
					String edit = "";
					for(int i = 0; i < text.length(); i++){
						char c = text.charAt(i);
						if(c >= '0' && c <= '9' || c == '-') edit += c;
					}
					super.replace(fb, offset, length, edit, attrs);
				}
			});
		}
		//only allow doubles
		else if(type == RESTRICT_DOUBLE){
			((AbstractDocument)getDocument()).setDocumentFilter(new DocumentFilter(){
				@Override
				public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
					String edit = "";
					for(int i = 0; i < text.length(); i++){
						char c = text.charAt(i);
						if(c >= '0' && c <= '9' || c == '-' || c == '.') edit += c;
					}
					super.replace(fb, offset, length, edit, attrs);
				}
			});
		}
		//only allow file names
		else if(type == RESTRICT_FILE){
			((AbstractDocument)getDocument()).setDocumentFilter(new DocumentFilter(){
				@Override
				public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException{
					String edit = "";
					char[] good = new char[]{'_', ' ', '$', '#', '-', '[', ']', '{', '}', '(', ')', '&', '^', '%'};
					for(int i = 0; i < text.length(); i++){
						char c = text.charAt(i);
						
						boolean add = false;
						for(char cc : good){
							if(cc == c){
								add = true;
								break;
							}
						}
						
						if(add || Character.isDigit(c) || Character.isAlphabetic(c)) add = true;
						if(add) edit += c;
					}
					super.replace(fb, offset, length, edit, attrs);
				}
			});
		}
		//allow all
		else{
			((AbstractDocument)getDocument()).setDocumentFilter(new DocumentFilter(){});
		}
	}
	
	/**
	 * Set the font to the given size
	 * @param size the size
	 */
	public void setFontSize(int size){
		setFont(new Font(SimConstants.FONT_NAME, Font.PLAIN, size));
	}
	
	/**
	 * Set the width of the text box in pixels
	 * @param width the pixels of the text box
	 */
	public void setCharWidth(int width){
		setPreferredSize(new Dimension(width, (int)getPreferredSize().getHeight()));
	}
	
}
