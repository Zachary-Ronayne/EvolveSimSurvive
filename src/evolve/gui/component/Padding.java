package evolve.gui.component;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A class set up to allow for easy to use padding with components
 */
public class Padding{

	/**
	 * The padding on the left
	 */
	private int padL;
	/**
	 * The padding on the right
	 */
	private int padR;
	/**
	 * The padding on the top
	 */
	private int padT;
	/**
	 * The padding on the bottom
	 */
	private int padB;
	
	public Padding(int padL, int padR, int padT, int padB){
		super();
		this.padL = padL;
		this.padR = padR;
		this.padT = padT;
		this.padB = padB;
	}

	/**
	 * Add the padding of this object to the given component and return a JPannel object with the padding.<br>
	 * To use this padding, call this method and add the returned JPannel to the desired component.<br>
	 * @param c the component, if null then a default JPanel is returned
	 * @return
	 */
	public JPanel addPadding(Component c){
		if(c == null) return new JPanel();
		return addPadding(c, c.getBackground());
	}
	
	/**
	 * Add the padding of this object to the given component and return a JPannel object with the padding, 
	 * using the specified color<br>
	 * To use this padding, call this method and add the returned JPannel to the desired component
	 * @param c the component, if null then a default JPanel is returned
	 * @param color the color, if null then a default JPanel is returned
	 * @return
	 */
	public JPanel addPadding(Component c, Color color){
		if(c == null || color == null) return new JPanel();
		
		JPanel pad = new JPanel();
		
		pad.add(c);
		pad.setBackground(color);
		pad.setBorder(new EmptyBorder(padT, padL, padB, padR));
		
		return pad;
	}
	
	public int getPadL(){
		return padL;
	}
	public void setPadL(int padL){
		this.padL = Math.max(0, padL);
	}
	
	public int getPadR(){
		return padR;
	}
	public void setPadR(int padR){
		this.padR = Math.max(0, padR);
	}
	
	public int getPadT(){
		return padT;
	}
	public void setPadT(int padT){
		this.padT = Math.max(0, padT);
	}
	
	public int getPadB(){
		return padB;
	}
	public void setPadB(int padB){
		this.padB = Math.max(0, padB);
	}
	
}
