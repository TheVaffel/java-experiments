package priv.hkon.theseq.misc;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Village;

public abstract class VillageEvent {
	
	Village village;
	protected Sprite subject;
	int id;
	
	public VillageEvent(Village village, Sprite s, int i) {
		this.village = village;
		this.subject = s;
		i = id;
	}
	
	public VillageEvent(Village village, Sprite s){
		this(village, s, 0);
	}
	
	public abstract boolean isHappening();
	
	public int getID(){
		return id;
	}

}
