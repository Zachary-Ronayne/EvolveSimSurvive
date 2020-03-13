package evolve.sim.misc;

import java.awt.Color;
import java.awt.Graphics;

import evolve.sim.obj.Creature;
import evolve.util.Camera;

public class CreatureGlow{
	
	/**
	 * The creature that this glow effect is used by
	 */
	private Creature creature;
	
	/**
	 * The color of this glow
	 */
	private Color glow;
	/**
	 * The radius of the glow that shows outside the creature
	 */
	private double radius;
	
	public CreatureGlow(Creature creature, Color glow, double radius){
		this.creature = creature;
		this.glow = glow;
		this.radius = radius;
	}
	
	public void render(Camera cam){
		Graphics g = cam.getG();
		g.setColor(getGlow());
		
		double size = creature.getRadius() + getRadius();
		
		cam.fillOval(creature.getX() - size, creature.getY() - size, size * 2, size * 2);
	}
	
	public Creature getCreature(){
		return creature;
	}
	public void setCreature(Creature creature){
		this.creature = creature;
	}

	public Color getGlow(){
		return glow;
	}
	public void setGlow(Color glow){
		this.glow = glow;
	}

	public double getRadius(){
		return radius;
	}
	public void setRadius(double radius){
		this.radius = radius;
	}
	
}
