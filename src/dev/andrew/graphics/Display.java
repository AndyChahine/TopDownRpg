package dev.andrew.graphics;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import dev.andrew.input.GameInput;

public class Display {
	
	private int width;
	private int height;
	
	private JFrame frame;
	private Canvas canvas;
	private BufferStrategy bs;
	private Graphics2D g;
	private GameInput input;
	
	public Display(int width, int height, String title) {
		this.width = width;
		this.height = height;
		
		input = new GameInput();
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.addKeyListener(input);
		canvas.addMouseListener(input);
		canvas.addMouseMotionListener(input);
		
		frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(canvas);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		canvas.createBufferStrategy(3);
		bs = canvas.getBufferStrategy();
		g = (Graphics2D) bs.getDrawGraphics();
		
		g.scale(width / 800.0f, height / 600.0f);
	}
	
	public void clear(Color color) {
		g.setColor(color);
		g.fillRect(0, 0, width, height);
	}
	
	public void update() {
		bs.show();
	}
	
	public Graphics getGraphics() {
		return g;
	}
	
	public GameInput getInput() {
		return input;
	}
	
	public void setTitle(String title) {
		frame.setTitle(title);
	}
}
