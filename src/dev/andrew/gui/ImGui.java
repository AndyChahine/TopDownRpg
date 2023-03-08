package dev.andrew.gui;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

import dev.andrew.input.GameInput;
import dev.andrew.input.GameKeyEvent;
import dev.andrew.input.GameMouseEvent;
import dev.andrew.input.GameMouseType;

public class ImGui {
	
	private Graphics2D g;

	public char keyChar = 0;
	public int keyEntered = 0;
	public int keyMod = 0;
	public int keyboardItem = 0;
	public int lastWidget = 0;
	
	public int mouseX = 0;
	public int mouseY = 0;
	public int hotItem = 0;
	public int activeItem = 0;
	public boolean mouseDown = false;
	public Font font;
	
	public int cursorPos = 0;
	
	public ImGui(Graphics2D g) {
		this.g = g;
		this.font = new Font("Arial", Font.PLAIN, 18);
	}
	
	public void begin() {
		hotItem = 0;
	}
	
	public void end() {
		if(mouseDown == false) {
			activeItem = 0;
		}else {
			if(activeItem == 0) {
				activeItem = -1;
			}
		}
		
		if(keyEntered == KeyEvent.VK_TAB) {
			keyboardItem = 0;
		}
		
		keyEntered = 0;
		
		keyChar = 0;
	}
	
	public boolean regionHit(int x, int y, int w, int h) {
		if(mouseX < x || mouseY < y || mouseX >= x + w || mouseY >= y + h) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean button(int id, int x, int y, String text) {
		
		return button(id, x, y, 64, 24, text);
	}
	
	public boolean button(int id, int x, int y, int width, int height, String text) {
		if(regionHit(x, y, width, height)) {
			hotItem = id;
			
			if(activeItem == 0 && mouseDown) {
				activeItem = id;
				keyboardItem = id;
			}
		}
		
		//button shadow
//		g.setColor(Color.BLACK);
//		g.fillRect(x + 8, y + 8, width, height);
//		g.setColor(new Color(Color.DARK_GRAY.darker().getRed(), Color.DARK_GRAY.darker().getGreen(), Color.DARK_GRAY.darker().getBlue(), 104));
//		g.fill(new RoundRectangle2D.Float(x, y + 1, width, height + 1, 10, 10));
		
		g.setFont(new Font("Arial", Font.BOLD, 12));
		Font f1 = new Font("Arial", Font.BOLD, 12);
		if(hotItem == id) {
			if(activeItem == id) {
//				g.setColor(Color.WHITE);
//				g.fillRect(x + 2, y + 2, width, height);
				
				
				g.setColor(new Color(45, 45, 45));
				g.fill(new RoundRectangle2D.Float(x, y, width, height, 1, 1));
			}else {
//				g.setColor(Color.WHITE);
//				g.fillRect(x, y, width, height);
				
				g.setColor(new Color(150, 150, 150));
				g.fill(new RoundRectangle2D.Float(x, y, width, height, 1, 1));
				
//				Paint oldPaint = g.getPaint();
//				GradientPaint gradient = new GradientPaint(x + width / 2, y + height / 2, new Color(107, 107, 107, 128), x - 15, y - 15, new Color(107, 107, 107, 0));
//				g.setPaint(gradient);
//				g.fill(new RoundRectangle2D.Float(x - 15, y - 15, x + 15 + width / 2, y + 15 + height / 2, 10, 10));
//				g.setPaint(oldPaint);
			}
		}else {
//			g.setColor(Color.GRAY);
//			g.fillRect(x, y, width, height);
			
			Paint oldPaint = g.getPaint();
			GradientPaint gradient = new GradientPaint(x + width / 2, y, new Color(120, 120, 120), x + width / 2, y + height, new Color(65, 65, 65));
			g.setPaint(gradient);
			g.fill(new RoundRectangle2D.Float(x, y, width, height, 1, 1));
			g.setPaint(oldPaint);
		}
		
		int centerX = width / 2;
		int centerY = height / 2;
		
		FontMetrics fm = g.getFontMetrics();
		Rectangle strBounds = fm.getStringBounds(text, g).getBounds();
		
		Font font = g.getFont();
		FontRenderContext frc = g.getFontRenderContext();
		GlyphVector gv = font.createGlyphVector(frc, text);
		Rectangle visualBounds = gv.getVisualBounds().getBounds();
		
		int textX = centerX - strBounds.width / 2;
		int textY = centerY - visualBounds.height / 2 - visualBounds.y;
		
//		if(activeItem == id) {
//			g.setColor(Color.DARK_GRAY);
//			g.drawString(label, x + textX + 2, y + textY + 2);
//		}else {
//			g.setColor(Color.DARK_GRAY);
//			g.drawString(label, x + textX, y + textY);
//		}
		
		g.setColor(Color.WHITE);
		g.drawString(text, x + textX, y + textY);
		
		if(keyboardItem == 0) {
			keyboardItem = id;
		}
		
		if(keyboardItem == id) {
			g.setColor(Color.WHITE);
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(1));
			g.draw(new RoundRectangle2D.Float(x, y, width, height, 1, 1));
			g.setStroke(oldStroke);
		}
		
		if(keyboardItem == id) {
			switch(keyEntered) {
			case KeyEvent.VK_TAB:
				{
					keyboardItem = 0;
					if((keyMod & KeyEvent.SHIFT_DOWN_MASK) != 0) {
						keyboardItem = lastWidget;
					}
					
					keyEntered = 0;
				}
				break;
				
			case KeyEvent.VK_ENTER: return true;
			}
		}
		
		lastWidget = id;
		
		if(mouseDown == false && hotItem == id && activeItem == id) {
			return true;
		}
		
		return false;
	}
	
	public boolean slider(int id, int x, int y, int max, Value value) {
		int yPos = ((256 - 16) * value.value) / max;
		
		if(regionHit(x + 8, y + 8, 16, 255)) {
			hotItem = id;
			
			if(activeItem == 0 && mouseDown) {
				activeItem = id;
			}
		}
		
		g.setColor(new Color(Color.DARK_GRAY.getRed(), Color.DARK_GRAY.getGreen(), Color.DARK_GRAY.getBlue(), 205));
		g.fill(new RoundRectangle2D.Float(x, y, 32, 256 + 16, 1, 1));
		
		if(activeItem == id || hotItem == id) {
			g.setColor(Color.WHITE);
			
			g.fill(new Ellipse2D.Float(x + 8, y + 8 + yPos, 16, 16));
		}else {
			g.setColor(Color.DARK_GRAY.brighter().brighter());
			g.fill(new Ellipse2D.Float(x + 8, y + 8 + yPos, 16, 16));
		}
		
		if(keyboardItem == 0) {
			keyboardItem = id;
		}
		
		if(keyboardItem == id) {
			g.setColor(Color.WHITE);
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(1));
			g.draw(new RoundRectangle2D.Float(x, y, 32, 272, 10, 10));
			g.setStroke(oldStroke);
		}
		
		if(keyboardItem == id) {
			switch(keyEntered) {
			case KeyEvent.VK_TAB:
				{
					keyboardItem = 0;
					if((keyMod & KeyEvent.SHIFT_DOWN_MASK) != 0) {
						keyboardItem = lastWidget;
					}
					
					keyEntered = 0;
				}
				break;
				
			case KeyEvent.VK_UP:
				{
					if(value.value > 0) {
						value.value--;
						return true;
					}
				}
				break;
				
			case KeyEvent.VK_DOWN:
				{
					if(value.value < max) {
						value.value++;
						return true;
					}
				}
				break;
			}
		}
		
		lastWidget = id;
		
		if(activeItem == id) {
			int mousePos = mouseY - (y + 8);
			if(mousePos < 0) {
				mousePos = 0;
			}
			if(mousePos > 255) {
				mousePos = 255;
			}
			
			int v = (mousePos * max) / 255;
			if(v != value.value) {
				value.value = v;
				return true;
			}
		}
		
		return false;
	}
	
	public boolean textField(int id, int x, int y, char[] buffer) {
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		int charWidth = fm.charWidth('W');
		int numChars = buffer.length;
		
		boolean changed = false;
		
		if(regionHit(x - 4, y - 4, numChars * charWidth + 8, 24 + 8)) {
			hotItem = id;
			
			if(activeItem == 0 && mouseDown) {
				activeItem = id;
			}
		}
		
		if(keyboardItem == 0) {
			keyboardItem = id;
		}
		
		if(keyboardItem == id) {
			g.setColor(Color.WHITE);
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(2));
			g.drawRect(x - 6, y - 6, numChars * charWidth + 12, 24 + 12);
			g.setStroke(oldStroke);
		}
		
		if(activeItem == id || hotItem == id || keyboardItem == id) {	
			g.setColor(new Color(91, 91, 91, 150));
			g.fillRect(x - 4, y - 4, numChars * charWidth + 8, 24 + 8);
		}else {
			g.setColor(new Color(64, 64, 64, 104));
			g.fillRect(x - 4, y - 4, numChars * charWidth + 8, 24 + 8);
		}
		
		g.setColor(Color.WHITE);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < buffer.length; i++) {
			if(buffer[i] == 0) {
				sb.append("");
			}else {
				sb.append(buffer[i]);
			}
		}
		g.drawString(sb.toString(), x, y + 16 + 1);
		
		if(keyboardItem == id) {
			fm = g.getFontMetrics();
			int w = 0;
			for(int i = 0; i < cursorPos; i++) {
				w += fm.charWidth(buffer[i]);
			}
			g.drawString("_", x + w, y + 16);
		}
		
		if(keyboardItem == id) {
			switch(keyEntered) {
			case KeyEvent.VK_TAB:
				{
					keyboardItem = 0;
					if((keyMod & KeyEvent.SHIFT_DOWN_MASK) != 0) {
						keyboardItem = lastWidget;
					}
					
					keyEntered = 0;
				}
				break;
				
			case KeyEvent.VK_BACK_SPACE:
				{
					if(cursorPos > 0) {
						cursorPos--;
						buffer[cursorPos] = 0;
						changed = true;
					}
				}
				break;
			}
			
			if(keyChar >= 32 && keyChar < 127 && cursorPos < numChars - 1) {
				buffer[cursorPos] = keyChar;
				cursorPos++;
				buffer[cursorPos] = 0;
				changed = true;
			}
		}
		
		if(mouseDown == false && hotItem == id && activeItem == id) {
			keyboardItem = id;
		}
		
		lastWidget = id;
		
		return changed;
	}
	
	public void label(int x, int y, String text) {
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(text, x, y);
	}
	
	public void input(GameInput input) {
		mouseX = input.mouseX;
		mouseY = input.mouseY;
		mouseDown = input.mouseDown[MouseEvent.BUTTON1];
	}
}
