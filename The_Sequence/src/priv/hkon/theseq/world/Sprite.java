package priv.hkon.theseq.world;

import java.util.Random;

public abstract class Sprite {
	
	protected int x, y;
	
	public static int W = Tile.WIDTH;
	public static int H = (Tile.HEIGHT*7)/4;
	protected int[][] data;
	protected int[][][][] animationFrames; //Animation no., frame no., y, x
	protected int numFrames;
	protected int numAnimations;
	
	protected DialogBubble dialog;
	protected boolean showDialog = false;
	protected int dialogDuration;
	protected int timeSinceDialogReset;
		
	
	public static final Random RAND = new Random(1234);
	
	protected Village village;
	
	public static final int DRAW_OFFSET_Y = (int)(0.0*Tile.HEIGHT);
	
	public Sprite(int nx, int ny, Village v){
		x = nx; 
		y = ny;
		village = v;
		data = new int[H][W];
		
		dialog = new DialogBubble(this);
		
		makeData();
		makeAnimationFrames();
	}

	
	public abstract void makeData();
	public void makeAnimationFrames(){
	}
	
	public void setDialogString(String str){
		dialog.setString(str);
	}
	
	public void showDialog(int dur){
		showDialog = true;
		timeSinceDialogReset = 0;
		dialogDuration = dur;
	}
	
	public void hideDialog(){
		showDialog = false;
		dialogDuration = 0;
	}
	
	public boolean shouldDrawDialog(){
		return showDialog;
	}
	
	public DialogBubble getDialog(){
		return dialog;
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
	
}
