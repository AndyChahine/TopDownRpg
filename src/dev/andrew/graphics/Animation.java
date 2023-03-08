package dev.andrew.graphics;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Comparator;

public class Animation {

	private ArrayList<Frame> frames;
	private int curFrame;
	private boolean stopped;
	private boolean looping;
	
	public Animation(){
		frames = new ArrayList<Frame>();
		curFrame = 0;
		stopped = true;
		looping = false;
	}
	
	public void update() {
		if(!stopped) {
			Frame temp = frames.get(curFrame);
			if(temp.update()) {
				curFrame++;
				curFrame %= frames.size();
				
				if(!looping) {
					if(curFrame % frames.size() == 0) {
						stop();
					}
				}
			}
		}
	}
	
	public void render(Graphics2D g, float x, float y){
		if(!stopped) {
			frames.get(curFrame).render(g, x, y);
		}
	}
	
	public void loop() {
		start();
		looping = true;
	}
	
	public void start() {
		if(!stopped) {
			return;
		}
		
		if(frames.size() == 0) {
			return;
		}
		
		stopped = false;
	}
	
	public void stop() {
		if(frames.size() == 0) {
			return;
		}
		
		stopped = true;
		looping = false;
	}
	
	public void restart() {
		if(frames.size() == 0) {
			return;
		}
		
		start();
		curFrame = 0;
	}
	
	public void reset() {
		stop();
		curFrame = 0;
	}
	
	public ArrayList<Frame> getFrames(){
		return frames;
	}
	
	public void addFrame(Frame frame) {
		frames.add(frame);
	}
	
	public int getCurrentFrame() {
		return curFrame;
	}
	
	public void sort() {
		frames.sort(new Comparator<Frame>() {

			@Override
			public int compare(Frame f1, Frame f2) {
				
				if(f1.getIndex() > f2.getIndex()) {
					return 1;
				}else if(f1.getIndex() < f2.getIndex()) {
					return -1;
				}else {
					return 0;
				}
			}
		});
	}
	
	public boolean isStopped() {
		return stopped;
	}
	
	public boolean isLooping() {
		return looping;
	}
}
