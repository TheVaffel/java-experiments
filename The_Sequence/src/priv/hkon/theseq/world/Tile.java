package priv.hkon.theseq.world;

import java.util.Random;

public class Tile {
	public static final int TYPE_EMPTY = 0;
	public static final int TYPE_GRASS = 1;
	public static final int TYPE_ROCK = 2;
	public static final int TYPE_REFINED_ROCK = 3;
	public static final int TYPE_WOODEN_FLOOR = 4;
	
	
	public static final int NUM_TYPES = 5;
	
	public static final int HEIGHT = 12;
	public static final int WIDTH = 16;
	
	public static final Random RAND = new Random(1234);
	
	//TODO: Make perlin-based field of the whole world, making textures sort-of random
	
	private static int[][][] data = new int[NUM_TYPES][HEIGHT][WIDTH];
	
	public static int[][] getData(int type){
		return data[type];
	}
	
	public static void init(){
		for(int i = 0; i < HEIGHT; i++){
			for(int j = 0 ; j < WIDTH; j++){
				data[TYPE_GRASS][i][j] = Sprite.getColor(0, 220 - RAND.nextInt(50), 0);
				
				data[TYPE_ROCK][i][j] = Sprite.getColor(128, 128, 50);
				data[TYPE_EMPTY][i][j] = Sprite.getColor(0, 0, 0);
				
				
				int d = 143 - RAND.nextInt(15);
				data[TYPE_REFINED_ROCK][i][j] = Sprite.getColor(d, d, d);
				if(i/2%2 == 0){
					d = RAND.nextInt(15)/13*80;
					data[TYPE_WOODEN_FLOOR][i][j] = Sprite.getColor(50 +d, 50 +d , 50);
				}else{
					d = RAND.nextInt(50);
					data[TYPE_WOODEN_FLOOR][i][j] = Sprite.getColor(130 + d, 130 + d, 70 + d);
				}
				
			}
		}
	}
}
