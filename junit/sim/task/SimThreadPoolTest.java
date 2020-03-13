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

public class SimThreadPoolTest{
	
	private SimThreadPool pool;
	private SimTask[] tasks;
	private Simulation sim;
	
	@Before
	public void setUp(){
		Main.SETTINGS = new Settings();
		Main.SETTINGS.numThreads.setValue(1);
		sim = new Simulation();
		pool = new SimThreadPool(sim, 1);
		tasks = pool.getTasks();
	}
	
	@Test
	public void testReset(){
		pool.reset();
	}
	
	@Test
	public void testStart(){
		pool.start();
		assertFalse(pool.isReadyUpdate());
		for(SimTask t : tasks) assertTrue(t.isRunning());
	}
	
	@Test
	public void testDoneAction(){
		for(int i = 0; i < pool.getThreadCount(); i++){
			SimTaskAction[] actions = pool.getTasks()[i].getReadyStates();
			actions[0].setReady(false);
			assertFalse(pool.doneAction(i));
			for(SimTaskAction a : actions){
				a.action();
				a.setReady(true);
			}
			assertTrue(pool.doneAction(i));
		}
	}
	
	@Test
	public void testFinished(){
		for(SimTask t : tasks) t.setFinished(true);
		assertTrue(pool.finished());
		
		tasks[0].setFinished(false);
		assertFalse(pool.finished());
	}

	@Test
	public void testStarted(){
		for(SimTask t : tasks) t.setStarted(true);
		assertTrue(pool.started());
		
		tasks[0].setStarted(false);
		assertFalse(pool.started());
	}

	@Test
	public void testGetTasks(){
		assertFalse(pool.getTasks() == null);
	}

	@Test
	public void testGetSimulation(){
		assertTrue(pool.getSimulation() == sim);
	}

	@Test
	public void testSetSimulation(){
		Simulation s = new Simulation();
		pool.setSimulation(s);
		assertTrue(pool.getSimulation() == s);
	}

	@Test
	public void testGetThreadCount(){
		assertTrue(pool.getThreadCount() == 1);
	}

	@Test
	public void testSetThreadCount(){
		pool.setThreadCount(0);
		assertTrue(pool.getThreadCount() == 0);
		pool.setThreadCount(1);
		assertTrue(pool.getThreadCount() == 1);
	}

	@Test
	public void testIsReadyUpdate(){
		assertFalse(pool.isReadyUpdate());
	}

	@Test
	public void testSetReadyUpdate(){
		pool.setReadyUpdate(true);
		assertTrue(pool.isReadyUpdate());
	}

	@Test
	public void testLoopDone(){
		pool.start();
		pool.nextLoop();
		pool.waitLoop();
		assertTrue(pool.loopDone());
	}

	@Test
	public void testTaskFinished(){
		assertFalse(pool.taskIsFinished(0));
	}

	@Test
	public void testTaskIsFinished(){
		pool.taskFinished(0);
		assertTrue(pool.taskIsFinished(0));
	}

	@Test
	public void testNextLoop(){
		pool.start();
		pool.nextLoop();
		pool.waitLoop();
	}

	@Test
	public void testWaitLoop(){
		pool.start();
		pool.nextLoop();
		pool.waitLoop();
	}

	@Test
	public void testEnd(){
		pool.start();
		pool.nextLoop();
		pool.waitLoop();
		pool.end();
		for(int i = 0; i < pool.getThreadCount(); i++){
			SimTask t = tasks[i];
			assertFalse(t.isRunning());
			assertTrue(t.isStarted());
			assertTrue(t.isFinished());
			assertTrue(pool.taskIsFinished(i));
		}
	}
	
	@Test
	public void testJoin(){
		pool.start();
		pool.end();
		pool.join();
	}

	@After
	public void end(){
		pool.end();
		pool.join();
	}
}
