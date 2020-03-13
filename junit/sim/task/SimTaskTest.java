package sim.task;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.sim.Simulation;
import evolve.sim.task.SimTask;
import evolve.sim.task.SimTaskAction;
import evolve.sim.task.SimThreadPool;
import evolve.util.options.Settings;

public class SimTaskTest{
	
	private SimTask task;
	private Simulation sim;
	private SimThreadPool pool;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		sim = new Simulation();
		pool = new SimThreadPool(sim, 1);
		task = pool.getTasks()[0];
		task.setHighBound(.5);
		task.setLowBound(0);
	}
	
	@Test
	public void testRun() throws InterruptedException{
		Thread t = new Thread(task);
		t.start();
		task.setRunning(false);
		t.join();
		assertFalse(task.isRunning());
	}
	
	@Test
	public void testResetReadyStates(){
		task.resetReadyStates();
		for(SimTaskAction a : task.getReadyStates()) assertFalse(a.isReady());
	}
	
	@Test
	public void testGetReadyStates(){
		assertFalse(task.getReadyStates() == null);
	}
	
	@Test
	public void testGetSimulation(){
		assertTrue(task.getSimulation() == sim);
	}
	
	@Test
	public void testSetSimulation(){
		Simulation s = new Simulation();
		task.setSimulation(s);
		assertTrue(task.getSimulation() == s);
	}

	@Test
	public void testGetLowBound(){
		assertTrue(task.getLowBound() == 0);
	}
	
	@Test
	public void testSetLowBound(){
		task.setLowBound(.6);
		assertTrue(task.getLowBound() == .6);
	}

	@Test
	public void testGetHighBound(){
		assertTrue(task.getHighBound() == .5);
	}
	
	@Test
	public void testSetHighBound(){
		task.setHighBound(1);
		assertTrue(task.getHighBound() == 1);
	}

	@Test
	public void testIsStarted(){
		assertTrue(task.isStarted());
	}
	
	@Test
	public void testSetStarted(){
		task.setStarted(false);
		assertFalse(task.isStarted());
	}

	@Test
	public void testIsFinished(){
		assertFalse(task.isFinished());
	}
	
	@Test
	public void testSetFinished(){
		task.setFinished(true);
		assertTrue(task.isFinished());
	}

	@Test
	public void testIsRunning(){
		assertTrue(task.isRunning());
	}
	
	@Test
	public void testSetRunning(){
		task.setRunning(false);
		assertFalse(task.isRunning());
	}
	
	@Test
	public void testGetIndex(){
		assertTrue(task.getIndex() == 0);
	}

	@After
	public void end(){
		pool.end();
		pool.join();
	}

}
