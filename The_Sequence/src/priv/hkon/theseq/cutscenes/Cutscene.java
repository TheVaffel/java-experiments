package priv.hkon.theseq.cutscenes;

import java.util.LinkedList;

import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.misc.VillageEvent;
import priv.hkon.theseq.sprites.Sprite;

public abstract class Cutscene {
	protected int tickCount;
	
	protected LinkedList<Happening> happenings;
	protected Core core;
	
	protected Cutscene(Core core){
		this.core = core;
		tickCount = 0;
		happenings = new LinkedList<Happening>();
	}
	
	public void tick(){
		while(!happenings.isEmpty()){
			//System.out.println("Huum");
			if(happenings.peek().ve != null){
				if(happenings.peek().ve.isHappening()){
					
					happenings.poll().happen();
				}else{
					break;
				}
			}else if(happenings.peek().timeStamp <= tickCount){
				happenings.poll().happen();
			}else{
				break;
			}
		}
		
		tickCount++;
	}
	
	public abstract boolean isFinished();
	
	public void close(){
		core.setCutsceneFilter(Filter.NO_FILTER);
	}
	
	public abstract class Happening{
		protected Sprite sprite;
		int timeStamp;
		VillageEvent ve = null;
		
		public Happening(Sprite s, int t){
			timeStamp = t;
			sprite = s;
		}
		
		public Happening(Sprite s, VillageEvent v){
			ve = v;
			sprite = s;
		}
		public abstract void happen();
	}

}
