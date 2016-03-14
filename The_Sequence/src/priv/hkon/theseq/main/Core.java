package priv.hkon.theseq.main;

import priv.hkon.theseq.world.*;

public class Core implements Runnable{ 
	Screen screen;
	public static final String TITLE = "The Sequence";
	
	Village village;
	
	boolean worldInitiated = false;

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
		
		new Thread(this).start();
		//System.out.println("At first");
		while(!worldInitiated){
			screen.runTitleScreen();
			screen.update();
			try{
				Thread.sleep(40);
			}catch(Exception e){}
		}
		for(int i = 0; i < 255; i+=10){
			screen.runTitleScreen();
			screen.darkenTitle(i);
			screen.update();
			try{
				Thread.sleep(40);
			}catch(Exception e){}
		}
		
		
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
			while(unprocessedSeconds > 1.0/TICKS_PER_SECOND){
				numTicks++;
				unprocessedSeconds -= 1.0/TICKS_PER_SECOND;
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
				if(System.nanoTime() - lastTime < 1000000000L/TICKS_PER_SECOND)
					Thread.sleep((long)(1000.0/TICKS_PER_SECOND - (System.nanoTime() - lastTime)/1000000.0));
			}catch(Exception e){}
		}
	}
	
	void tick(){
		screen.count++;
		//System.out.println("Into tick");
		village.tick();
		//System.out.println("Out of tick");
	}
	
	void draw(){
		screen.setData(village.getScreenData(Screen.W, Screen.H));
		screen.update();
	}

	@Override
	public void run() {
		Tile.init();
		village = new Village();
		//System.out.println("At last");
		for(int i = 0; i< 3*30; i++){
			village.tick();
			
			try{
				Thread.sleep(30);
			}catch(Exception e){}
		}
		
		worldInitiated = true;
	}

}
