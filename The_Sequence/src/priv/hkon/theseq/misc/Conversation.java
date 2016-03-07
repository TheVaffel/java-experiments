package priv.hkon.theseq.misc;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;

public class Conversation {
	
	TalkativeSprite owner;
	TalkativeSprite partner;
	
	boolean isOn = false;
	
	TalkativeSprite currTalker;
	int importance;
	
	boolean finished = false;
	
	int lastSentenceStart;

	public Conversation(TalkativeSprite o, TalkativeSprite p, int importance) {
		owner = o;
		partner = p;
		currTalker = owner;
		
		this.importance = importance;
		
		invite();
	}
	
	void invite(){
		partner.inviteTo(this);
	}
	
	public void finishedSentence(){
		currTalker = (currTalker == owner) ? partner : owner;
	}
	
	public Sprite getTalker(){
		return currTalker;
	}
	
	public void disconnect(Sprite s){
		if(partner == s){
			partner = null;
		}else{
			owner = null;
		}
		
		finished = true;
	}
	
	public void tick(){
		
		if(finished){
			return;
		}
		if(!isOn){
			//System.out.println("Conversation not on!");
			if(owner.distTo(partner) == 1){
				isOn = true;
				owner.hideDialog();
				partner.hideDialog();
				currTalker.talk();
			}else
			
			if(owner.distTo(partner) >Villager.RANGE_OF_VIEW*1.5){
				owner.deniedConversation();
			}
		}else
		if(!currTalker.showDialog){
			if(isOn){
				if(owner.distTo(partner) >Villager.RANGE_OF_VIEW*1.5){
					owner.deniedConversation();
				}
				
				currTalker.talk();
			}
		}
		
	}
	
	public Sprite getPartner(){
		return partner;
	}
	
	public Sprite getOwner(){
		return owner;
	}
	
	public int getImportance(){
		return importance;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	public boolean isOn(){
		return isOn;
	}
}