package priv.hkon.theseq.main;

import priv.hkon.theseq.world.*;

public class Core {
	Screen screen;
	public static final String TITLE = "The Sequence";
	
	Village village;

	boolean playing = false;
	static final double TICKS_PER_SECOND = 60;
	
	public static void main(String[] args) {
		Core c = new Core();
		c.start();

	}
	
	public Core(){
		screen = new Screen(this);
	}
	
	public void start(){
		playing = true;
		Tile.init();
		village = new Village();
		screen.setData(village.getScreenData(Screen.W, Screen.H));
		long currentTime = System.nanoTime();
		long lastTime = currentTime;
		
		double unprocessedSeconds = 0.0;
		
		int numTicks = 0;
		int fps = 0;
		
		while(playing){
			currentTime = System.nanoTime();
			unprocessedSeconds += (currentTime - lastTime)/1000000000.0;
			lastTime = currentTime;
			while(unprocessedSeconds > 1/TICKS_PER_SECOND){
				numTicks++;
				unprocessedSeconds -= 1/TICKS_PER_SECOND;
				tick();
				if(numTicks == 60){
					System.out.println("FPS: " + fps);
					fps = 0;
					numTicks = 0;
				}
			}
			
			draw();
			fps++;
			
			try{
				Thread.sleep((long)(1000/TICKS_PER_SECOND - (System.nanoTime() - lastTime)/1000000.0));
			}catch(Exception e){}
		}
	}
	
	void tick(){
		screen.count++;
		village.tick();
	}
	
	void draw(){
		screen.setData(village.getScreenData(Screen.W, Screen.H));
		screen.update();
	}

}
