package dev.andrew.input;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.event.MouseInputListener;

public class GameInput implements KeyListener, MouseInputListener{

	public int keyDownCount, keyUpCount;
	public boolean[] keyDown = new boolean[256];
	public boolean[] keyUp = new boolean[256];
	public boolean keyEventsQueue = false;
	public Queue<GameKeyEvent> keyEvents = new ConcurrentLinkedQueue<GameKeyEvent>();

	public int mouseX, mouseY, mouseDownCount, mouseUpCount, prevMouseX, prevMouseY;
	public boolean[] mouseDown = new boolean[MouseInfo.getNumberOfButtons()];
	public boolean[] mouseUp = new boolean[MouseInfo.getNumberOfButtons()];
	public boolean mouseInside = true;
	public boolean mouseDragging = false;
	public boolean mouseMoving = false;
	public boolean mouseEventsQueue = true;
	public Queue<GameMouseEvent> mouseEvents = new ConcurrentLinkedQueue<GameMouseEvent>();
	
	public AffineTransform scale = AffineTransform.getScaleInstance(1024.0f / 800.0f, 768.0f / 600.0f);

	public void clear()
	{
		keyDownCount = 0;
		keyUpCount = 0;
		keyEvents.clear();

		for (int i = 0; i < keyUp.length; i++)
		{
			keyUp[i] = false;
		}

		mouseDownCount = 0;
		mouseUpCount = 0;
		mouseDragging = false;
		mouseMoving = false;
		mouseEvents.clear();

		for (int i = 0; i < mouseUp.length; i++)
		{
			mouseUp[i] = false;
		}
	}

	@Override
	public void keyTyped( KeyEvent e )
	{
		if (keyEventsQueue)
		{
			keyEvents.offer( new GameKeyEvent( GameKeyType.Type, e ) );
		}
	}

	@Override
	public void keyPressed( KeyEvent e )
	{
		if (!keyDown[e.getKeyCode()])
		{
			keyDown[e.getKeyCode()] = true;
			keyDownCount++;

			if (keyEventsQueue)
			{
				keyEvents.offer( new GameKeyEvent( GameKeyType.Down, e ) );
			}
		}
	}

	@Override
	public void keyReleased( KeyEvent e )
	{
		keyDown[e.getKeyCode()] = false;
		keyUp[e.getKeyCode()] = true;
		keyUpCount++;

		if (keyEventsQueue)
		{
			keyEvents.offer( new GameKeyEvent( GameKeyType.Up, e ) );
		}
	}

	@Override
	public void mouseDragged( MouseEvent e )
	{
		mouseDragging = true;
		mouseMoving = true;
		prevMouseX = mouseX;
		prevMouseY = mouseY;
		Point2D mousePoint = e.getPoint();
		try {
			scale.inverseTransform(mousePoint, mousePoint);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
		mouseX = (int) mousePoint.getX();
		mouseY = (int) mousePoint.getY();
	}

	@Override
	public void mouseMoved( MouseEvent e )
	{
		mouseDragging = false;
		mouseMoving = true;
		prevMouseX = mouseX;
		prevMouseY = mouseY;
		Point2D mousePoint = e.getPoint();
		try {
			scale.inverseTransform(mousePoint, mousePoint);
		} catch (NoninvertibleTransformException e1) {
			e1.printStackTrace();
		}
		mouseX = (int) mousePoint.getX();
		mouseY = (int) mousePoint.getY();
	}

	@Override
	public void mouseClicked( MouseEvent e )
	{
		if (mouseEventsQueue)
		{
			mouseEvents.offer( new GameMouseEvent( GameMouseType.Click, e ) );
		}
	}

	@Override
	public void mousePressed( MouseEvent e )
	{
		mouseDown[e.getButton()] = true;
		mouseDownCount++;

		if (mouseEventsQueue)
		{
			mouseEvents.offer( new GameMouseEvent( GameMouseType.Press, e ) );
		}
	}

	@Override
	public void mouseReleased( MouseEvent e )
	{
		mouseDown[e.getButton()] = false;
		mouseUp[e.getButton()] = true;
		mouseUpCount++;

		if (mouseEventsQueue)
		{
			mouseEvents.offer( new GameMouseEvent( GameMouseType.Release, e ) );
		}
	}

	@Override
	public void mouseEntered( MouseEvent e )
	{
		mouseInside = true;

		if (mouseEventsQueue)
		{
			mouseEvents.offer( new GameMouseEvent( GameMouseType.Enter, e ) );
		}
	}

	@Override
	public void mouseExited( MouseEvent e )
	{
		mouseInside = false;

		if (mouseEventsQueue)
		{
			mouseEvents.offer( new GameMouseEvent( GameMouseType.Exit, e ) );
		}
	}
}
