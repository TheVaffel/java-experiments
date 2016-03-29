package priv.hkon.theseq.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import priv.hkon.theseq.filters.CombinedFilter;
import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Core implements Runnable{ 
	Screen screen;
	public static final String TITLE = "The Sequence";
	
	public Village village;
	
	boolean worldInitiated = false;
	
	Filter cutsceneFilter = Filter.NO_FILTER;
	Filter villageFilter = Filter.NO_FILTER;
	
	boolean changedFilter = false;

	boolean playing = false;
	static final double TICKS_PER_SECOND = 60;
	
	public static void main(String[] args) {
		Core c = new Core();
		c.start();

	}
	
	public Core(){
		screen = new Screen(this);
	}
	
	long currentTime = System.nanoTime();
	long lastTime = currentTime;
	
	public void start(){
		
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
			screen.darkenImage(i);
			screen.update();
			try{
				Thread.sleep(40);
			}catch(Exception e){}
		}
		
		//screen.setData(village.getScreenData(Screen.W, Screen.H));
		currentTime = System.nanoTime();
		lastTime = currentTime;
		
		playing = true;
		
		
		double unprocessedSeconds = .02;
		
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
		village.tick();
	}
	
	public void setCutsceneFilter(Filter f){
		if(f != cutsceneFilter){
			cutsceneFilter = f;
			changedFilter = true;
		}
	}
	
	public void setVillageFilter(Filter f){
		if(f != villageFilter){
			changedFilter = true;
			villageFilter = f;
		}
		
	}
	
	void draw(){
		screen.setData(village.getScreenData(Screen.W, Screen.H));
		
		if(changedFilter){
			screen.setFilter(new CombinedFilter(villageFilter, cutsceneFilter));
		}
		screen.update();
		changedFilter = false;
	}

	@Override
	public void run() {
		Tile.init();
		village = new Village(this);
		//System.out.println("At last");
		for(int i = 0; i< 3*30; i++){
			village.tick();
			try{
				Thread.sleep(30);
			}catch(Exception e){}
		}
		
		worldInitiated = true;
	}

	public void saveVillage(int savenr){
		String filename = "saves/" + savenr + ".sav";
		ObjectOutputStream oos = null;
		try{
			oos= new ObjectOutputStream(new FileOutputStream(filename));
			oos.writeObject(village);
			village.lastSave = village.getTime();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			if(oos != null){
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void loadImage(int savenr){
		String filename = "saves/"  + savenr + ".sav";
		ObjectInputStream ois = null;
		try{
			ois = new ObjectInputStream(new FileInputStream(filename));
			village = (Village)ois.readObject();
			village.setCore(this);
			currentTime = System.nanoTime();
			lastTime = currentTime;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(ois != null){
				try {
					ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		try{
			Thread.sleep(1000);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void resyncornizeTime(){
		currentTime = System.nanoTime();
		lastTime = currentTime;
	}
}
