package evolve.sim.misc;

import evolve.sim.Simulation;
import evolve.sim.misc.Eye;
import evolve.sim.misc.Gene;
import evolve.sim.obj.NeuralNetCreature;
import evolve.util.options.Settings;
import evolve.Main;
import evolve.Util;

public class SpeedTestEye{
	
	public static boolean CONDITION = false;
	
	public static void main(String[] args){
		
		Main.SETTINGS = new Settings();
		Main.SETTINGS.initialCreatures.setValue(1000);
		
		Eye e = new Eye();
		e.setMaxDistance(new Gene(10000, 0, 100000, 1));
		e.setMinDistance(new Gene(0, 0, 100000, 1));
		e.setDistance(1);
		Simulation sim = new Simulation();
		NeuralNetCreature creature = sim.getEvolvingCreature();
		
		System.out.println("initializing");
		test(e, sim, creature);
		
		for(int i = 0; i < 4; i++){
			System.out.println("Testing true");
			CONDITION = true;
			double testT = test(e, sim, creature);
			
			System.out.println("Testing false");
			CONDITION = false;
			double testF = test(e, sim, creature);

			System.out.println("Percent difference");
			System.out.println(Math.abs(testT - testF) / testF * 100);
			System.out.println();
			System.out.println("------------------------------------------");
			System.out.println();
		}
	}
	
	public static double test(Eye e, Simulation sim, NeuralNetCreature creature){
		long startTime = System.nanoTime();
		for(int i = 0; i < 100000; i++){
			e.look(sim, creature);
		}
		long endTime = System.nanoTime();
		
		double totalTime = Util.getSeconds(endTime - startTime);
		System.out.println(totalTime);
		System.out.println();
		return totalTime;
	}
	
}
