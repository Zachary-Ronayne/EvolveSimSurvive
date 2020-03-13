package sim.misc;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.misc.CreatureGlow;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.options.Settings;

public class CreatureGlowTest{
	
	private CreatureGlow glow;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		
		sim = new Simulation();
		glow = new CreatureGlow(new NeuralNetCreature(sim), Color.RED, 7);
	}
	
	@Test
	public void testRender(){
		BufferedImage buff = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
		Graphics2D g = (Graphics2D)buff.getGraphics();
		sim.getCamera().setG(g);
		glow.render(sim.getCamera());
	}
	
	@Test
	public void testGetSetCreature(){
		Creature creature = new NeuralNetCreature(sim);
		glow.setCreature(creature);
		assertTrue(glow.getCreature().equals(creature));
	}
	
	@Test
	public void testGetSetGlow(){
		glow.setGlow(Color.BLACK);
		assertTrue(glow.getGlow().equals(Color.BLACK));
	}
	
	@Test
	public void testGetSetRadius(){
		glow.setRadius(2);
		assertTrue(glow.getRadius() == 2);
	}
	
}
