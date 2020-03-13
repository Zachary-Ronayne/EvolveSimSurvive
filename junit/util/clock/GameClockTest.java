package util.clock;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import evolve.util.clock.ClockRenderEvent;
import evolve.util.clock.ClockUpdateEvent;
import evolve.util.clock.GameClock;

public class GameClockTest{
	
	private GameClock clock;
	
	@Before
	public void setUp(){
		clock = new GameClock(10, 10);
	}

	@Test
	public void testStopClock(){
		clock.stopClock();
	}
	
	@Test
	public void testStartClock(){
		clock.startClock();
	}
	
	@Test
	public void testUpdate(){
		clock.update();
	}
	
	@Test
	public void testRender(){
		clock.render();
	}

	@Test
	public void testGetSetMaxFrameRate(){
		clock.setMaxFrameRate(19);
		assertTrue(clock.getMaxFrameRate() == 19);
	}
	
	@Test
	public void testGetSetDelay(){
		clock.setDelay(10000000l);
		assertTrue(clock.getDelay() == 10000000l);
		assertTrue(clock.getDelayMilli() == 10);
	}
	
	@Test
	public void testGetSetStopUpdates(){
		clock.setStopUpdates(true);
		assertTrue(clock.getStopUpdates());
	}
	
	public void testGetSetDelayMili(){
		clock.setDelayMilli(10);
		assertTrue(clock.getDelayMilli() == 10);
		assertTrue(clock.getDelay() == 10000000l);
	}

	@Test
	public void testIsUpdating(){
		clock.isUpdating();
	}
	
	@Test
	public void testIsRendering(){
		clock.isRendering();
	}
	
	@Test
	public void testAddUpdateEvent(){
		clock.addUpdateEvent(new ClockUpdateEvent(){
			@Override
			public void event(){
			}
		});
	}
	
	@Test
	public void testAddRenderEvent(){
		clock.addRenderEvent(new ClockRenderEvent(){
			@Override
			public void event(){
			}
		});
	}
	
}
