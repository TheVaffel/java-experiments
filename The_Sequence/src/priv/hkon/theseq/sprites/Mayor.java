package priv.hkon.theseq.sprites;

import priv.hkon.theseq.world.House;
import priv.hkon.theseq.world.Village;

public class Mayor extends Villager {
	
	Sprite lastVictim;

	public Mayor(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		targetMode = MODE_WORKING;
	}
	
	public boolean tick(){
		if(super.tick()){
			return true;
		}
		
		
		return false;
	}
	
	public void work(){
		
		if(targetSprite == null){
			Citizen c;
			if((c = findCitizen()) != null){
				if(c == lastVictim){
					return;
				}
				hasPath = false;
				lastVictim = c;
				engageConversation(c, IMPORTANT_VERY);
			}else if(!hasPath){
				strollTownGrid();
			}
		}
		return;
	}
	
	

}
