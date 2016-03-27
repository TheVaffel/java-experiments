package priv.hkon.theseq.cutscenes;

import java.util.LinkedList;

import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.sprites.Sprite;

public abstract class Cutscene {
	int tickCount;
	
	LinkedList<Happening> happenings;
	Core core;
	
	Cutscene(Core core){
		this.core = core;
		tickCount = 0;
		happenings = new LinkedList<Happening>();
	}
	
	public void tick(){
		while(!happenings.isEmpty() && happenings.peek().timeStamp <= tickCount){
			happenings.poll().happen();
		}
	}
	public abstract boolean isFinished();
	
	public void close(){
		core.setCutsceneFilter(Filter.NO_FILTER);
	}
	
	public abstract class Happening{
		Sprite sprite;
		int timeStamp;
		public Happening(Sprite s, int t){
			timeStamp = t;
			sprite = s;
		}
		public abstract void happen();
	}

}
