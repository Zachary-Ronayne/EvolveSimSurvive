package util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

import evolve.Main;
import evolve.util.Camera;

public class CameraTest{
	
	public static double DELTA = 0.000001;
	
	private Camera cam;
	private Graphics2D g;
	
	@Before
	public void seUp(){
		cam = new Camera(100, 100);
		
		BufferedImage buff = new BufferedImage(1000, 1000, BufferedImage.TYPE_3BYTE_BGR);
		g = (Graphics2D)buff.getGraphics();
		cam.setG(g);
	}
	
	@Test
	public void testIsAnchoredRelaseAnchor(){
		cam.releaseAnchor();
		assertFalse(cam.isAchored());
		cam.setAnchor(0, 0);
		assertTrue(cam.isAchored());
	}
	
	@Test
	public void testGetSetAnchored(){
		cam.releaseAnchor();
		cam.setAnchor(99, 10);

		assertEquals(cam.getAnchorX(), 99, DELTA);
		assertEquals(cam.getAnchorY(), 10, DELTA);
	}

	@Test
	public void testPan(){
		cam.setX(25);
		cam.setY(4);
		
		cam.releaseAnchor();
		cam.setAnchor(50, 10);
		
		cam.pan(177, 298);

		assertEquals(cam.getX(), -102, DELTA);
		assertEquals(cam.getY(), -284, DELTA);
	}
	
	@Test
	public void testCenter(){
		cam.setWidth(100);
		cam.setHeight(100);
		cam.center(20, 40);
		assertEquals(cam.getX(), -30, DELTA);
		assertEquals(cam.getY(), -10, DELTA);
	}
	
	@Test
	public void testGetSetX(){
		cam.setX(4);
		assertTrue(cam.getX() == 4);
		
		cam.setMaxX(100);
		cam.setMinX(-100);
		cam.setX(200);
		assertEquals(cam.getX(), 100, DELTA);
		cam.setX(-230);
		assertEquals(cam.getX(), -100, DELTA);
	}

	@Test
	public void testGetSetY(){
		cam.setY(6);
		assertTrue(cam.getY() == 6);
		
		cam.setMaxY(100);
		cam.setMinY(-100);
		cam.setY(200);
		assertEquals(cam.getY(), 100, DELTA);
		cam.setY(-230);
		assertEquals(cam.getY(), -100, DELTA);
	}
	
	@Test
	public void testGetSetWidth(){
		cam.setWidth(64);
		assertTrue(cam.getWidth() == 64);
	}

	@Test
	public void testGetSetHeight(){
		cam.setHeight(64);
		assertTrue(cam.getHeight() == 64);
	}

	@Test
	public void testGetSetXZoomFactor(){
		cam.setXZoomFactor(3);
		assertTrue(cam.getXZoomFactor() == 3);
	}

	@Test
	public void testGetSetYZoomFactor(){
		cam.setYZoomFactor(2);
		assertTrue(cam.getYZoomFactor() == 2);
	}
	
	@Test
	public void testGetSetG(){
		cam.setG(g);
		assertTrue(cam.getG().equals(g));
	}
	
	@Test
	public void testIsSetDrawOnlyInBounds(){
		cam.setDrawOnlyInBounds(true);
		assertTrue(cam.isDrawOnlyInBounds());
	}
	
	@Test
	public void testDrawXYWH(){
		cam.setWidth(100);
		cam.setHeight(100);
		cam.center(60, 200);
		cam.setXZoomFactor(0);
		cam.setYZoomFactor(0);
		
		assertTrue(cam.drawX(100) == 90);
		assertTrue(cam.drawY(50) == -100);
		
		cam.setXZoomFactor(10);
		cam.setYZoomFactor(-10);
		
		assertTrue(cam.drawW(3) > 3);
		assertTrue(cam.drawH(3) < 3);
	}
	
	@Test
	public void testMouseXY(){
		cam.mouseX(100);
		cam.mouseY(100);
	}
	
	@Test
	public void testZoomIn(){
		cam.setWidth(210);
		cam.setHeight(210);
		cam.center(100, 100);
		
		double x = cam.drawX(100);
		double y = cam.drawY(10);

		cam.zoomIn(110, 110, 10);
		
		assertTrue(cam.drawX(100) < x);
		assertTrue(cam.drawY(10) < y);

		x = cam.drawX(100);
		y = cam.drawY(10);
		
		cam.zoomIn(100, 100, -10);

		assertTrue(cam.drawX(100) > x);
		assertTrue(cam.drawY(10) > y);
	}
	
	@Test
	public void testZoomInX(){
		cam.setWidth(210);
		cam.setHeight(210);
		cam.center(100, 100);
		
		double x = cam.drawX(100);

		cam.zoomInX(110, 110, 10);
		
		assertTrue(cam.drawX(100) < x);

		x = cam.drawX(100);
		
		cam.zoomIn(100, 100, -10);

		assertTrue(cam.drawX(100) > x);
	}

	@Test
	public void testZoomInY(){
		cam.setHeight(210);
		cam.center(100, 100);
		
		double y = cam.drawY(10);

		cam.zoomIn(110, 110, 10);
		
		assertTrue(cam.drawY(10) < y);

		y = cam.drawY(10);
		
		cam.zoomIn(100, 100, -10);

		assertTrue(cam.drawY(10) > y);
	}
	
	@Test
	public void testFillRect(){
		cam.fillRect(1, 2, 5, 7);
	}

	@Test
	public void testFillOval(){
		cam.fillOval(1, 2, 5, 7);
	}
	
	@Test
	public void testDrawLine(){
		cam.drawLine(1, 2, 5, 7);
	}
	
	@Test
	public void testDrawString(){
		cam.drawString("test", 5, 7);
	}
	
	@Test
	public void testDrawScaleString(){
		cam.drawScaleString("test", 5, 7);
	}
	
	@Test
	public void testInBounds(){
		cam.inBounds(1, 1, 20, 20);
	}

	@Test
	public void testZoom(){
		double value = 1;
		assertTrue(Camera.zoom(value, 2) > value);
		assertTrue(Camera.zoom(value, -2) < value);
		assertTrue(Camera.zoom(value, 0) == value);
	}
	
	@Test
	public void testInverseZoom(){
		double value = 1;
		assertTrue(Camera.inverseZoom(value, 2) < value);
		assertTrue(Camera.inverseZoom(value, -2) > value);
		assertTrue(Camera.inverseZoom(value, 0) == value);
	}
	
	@Test
	public void testGetSetMinX(){
		cam.setMinX(-1000);
		assertEquals(cam.getMinX(), -1000, DELTA);
	}
	
	@Test
	public void testGetSetMinY(){
		cam.setMinY(-1001);
		assertEquals(cam.getMinY(), -1001, DELTA);
	}
	
	@Test
	public void testGetSetMaxX(){
		cam.setMaxX(1002);
		assertEquals(cam.getMaxX(), 1002, DELTA);
	}
	
	@Test
	public void testGetSetMaxY(){
		cam.setMaxY(1003);
		assertEquals(cam.getMaxY(), 1003, DELTA);
	}
	
	@Test
	public void testSaveLoad(){
		try{
			//test save
			PrintWriter write = new PrintWriter(new File(Main.DATA_PATH + "/testSave.txt"));
			assertTrue(cam.save(write));
			write.close();
			
			//test load
			Scanner read = new Scanner(new File(Main.DATA_PATH + "/testSave.txt"));
			assertTrue(cam.load(read));
			read.close();
			
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
	
}
