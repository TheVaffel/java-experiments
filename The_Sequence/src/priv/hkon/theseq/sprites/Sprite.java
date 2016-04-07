package priv.hkon.theseq.sprites;

import java.io.Serializable;
import java.util.Random;

import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public abstract class Sprite implements Serializable{
	
	private static final long serialVersionUID = -1951040426546012965L;

	protected int x, y;
	
	public static int W = Tile.WIDTH;
	public static int H = (Tile.HEIGHT*7)/4;
	protected int[][] data;
	protected int[][][][] animationFrames; //Animation no., frame no., y, x
	protected int numFrames;
	protected int numAnimations;
	
	public static final Random RAND = new Random(1234);
	
	protected Village village;
	
	public boolean debug = false;
	
	public static final int DRAW_OFFSET_Y = (int)(0.0*Tile.HEIGHT);
	
	public Sprite(int nx, int ny, Village v){
		x = nx; 
		y = ny;
		village = v;
		data = new int[H][W];
		
		
		
		makeData();
		makeAnimationFrames();
	}
	
	public abstract String getName();

	
	public abstract void makeData();
	public void makeAnimationFrames(){
	}
	
	
	
	public int distTo(Sprite s){
		return Math.abs(s.getX() - getX()) + Math.abs(s.getY() - getY());
	}
	
	public int distTo(int nx, int ny){
		return Math.abs(nx - x) + Math.abs(ny - y);
	}
	

	public static int distBetween(int x1, int y1, int x2, int y2){
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}
	
	public static int getColor(int r, int g, int b){
		return (255<<24) | (r<<16) | (g<<8) | b;
	}
	
	public int[][] getData(){
		return data;
	}
	
	public boolean tick(){
		return false;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public boolean isStationary(){
		return true;
	}
	
	public static int getRandomInt(int u){
		int b =  ((u + 2211) << 2)^u *10007 ^ 7532;
		return b< 0? -b: b;
	}
	
	public static int getRandomInt(int u, int v){
		int b = ((u + 2552) << 2)^(v + 33443)*33645^5313;
		return b < 0? -b: b;
	}
	
	public static int multiplyColor(int rgb, float f){
		rgb = ((int)((rgb&(255 << 16))*f)& (255 << 16)) | 
				((int)((rgb&(255 << 8))*f)& (255 << 8)) |
				((int)((rgb&(255 ))*f)& (255 )) | (255 << 24);
		
		return rgb;
	}
	
	
	public void setX(int nx){
		x = nx;
	}
	
	public void setY(int ny){
		y = ny;
	}
}
