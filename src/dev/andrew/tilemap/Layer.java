package dev.andrew.tilemap;

public class Layer {

	public int id;
	public String name;
	public int[][] tiles;
	public int rows;
	public int cols;
	
	public Layer(int id, String name, int rows, int cols, String[] data) {
		this.id = id;
		this.name = name;
		this.rows = rows;
		this.cols = cols;
		tiles = new int[rows][cols];
		
		for(int row = 0; row < rows; row++) {
			for(int col = 0; col < cols; col++) {
				tiles[row][col] = Integer.parseInt(data[col + row * cols]);
			}
		}
	}
}
