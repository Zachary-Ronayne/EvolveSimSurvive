package evolve.sim.obj.tile;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.obj.Creature;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.Camera;
import evolve.util.math.vector.AngleVector;
import evolve.util.math.vector.Vector;

public class WaterTile extends Tile{
	
	private boolean frozen;
	
	public WaterTile(int x, int y, Simulation sim){
		super(x, y, sim);
		frozen = false;
	}
	
	@Override
	public void cacheData(){
		frozen = getTemperature().getTemp() <= 0;
	}
	
	public boolean isFrozen(){
		return frozen;
	}
	
	@Override
	public void update(){
		super.update();
		
		if(!isFrozen()){
			ArrayList<NeuralNetCreature> creatures = new ArrayList<NeuralNetCreature>();
			creatures.addAll(getContainingCreatures());
			for(NeuralNetCreature c : creatures){
				if(c != null) c.addEnergy(-Main.SETTINGS.tileWaterDamage.value());
			}
		}
		super.cacheData();
	}

	@Override
	public void render(Camera cam){
		double size = Main.SETTINGS.tileSize.value();
		Graphics g = cam.getG();
		
		g.setColor(Color.BLACK);
		cam.fillRect(size * getX(), size * getY(), size, size);
		if(isFrozen()) g.setColor(new Color(70, 70, 200));
		else g.setColor(new Color(0, 0, 200));
		cam.fillRect(size * getX() + 2, size * getY() + 2, size - 4, size - 4);
		if(isFrozen())  g.setColor(new Color(100, 100, 230));
		else g.setColor(new Color(30, 30, 255));
		cam.fillRect(size * getX() + size / 4, size * getY() + size / 4, size / 2, size / 2);
	}
	
	@Override
	public double getFood(){
		return 0;
	}

	@Override
	public double eat(){
		return 0;
	}
	
	@Override
	public double eat(Creature c){
		return 0;
	}
	
	@Override
	public void giveEnergy(double energy){}

	@Override
	public boolean isHazard(){
		return true;
	}
	
	@Override
	public Vector getMovement(Creature c){
		if(c == null || !isFrozen()) return null;
		Vector v = c.getTileMovedVector();
		double speed = c.getTotalSpeed() * Main.SETTINGS.temperatureIceSlideRate.value();
		
		if(v == null){
			v = new AngleVector(c.getAngle(), speed);
		}
		else v.setMagnitude(v.getMagnitude() * Main.SETTINGS.temperatureIceSlideDecay.value());
		
		return v;
	}
	
}
