package priv.hkon.theseq.misc;

import java.io.Serializable;

import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;

public class Conversation  implements Serializable{
	
	private static final long serialVersionUID = -6621616468501779179L;
	TalkativeSprite owner;
	TalkativeSprite partner;
	
	Sentence lastSentence;
	
	boolean isOn = false;
	
	TalkativeSprite currTalker;
	int importance;
	
	public boolean finished = false;
	
	int sentenceCount = 0;

	public Conversation(TalkativeSprite o, TalkativeSprite p, int importance) {
		owner = o;
		partner = p;
		currTalker = owner;
		
		this.importance = importance;
		lastSentence = null;
		
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
		if(partner instanceof Player){
			((Player)partner).setConversation(null);
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
				if(partner instanceof Player){
					((Player) partner).setConversation(this);
				}
				currTalker.talk();
				lastSentence = currTalker.getCurrSentence();
			}else
			
			if(owner.distTo(partner) >Villager.RANGE_OF_VIEW*1.2){
				owner.deniedConversation();
			}
		}else
		if(!currTalker.showDialog){
			if(isOn){
				if(owner.distTo(partner) >Villager.RANGE_OF_VIEW*1.2){
					owner.deniedConversation();
					return;
				}
				sentenceCount++;
				if(currTalker instanceof Player){
					currTalker = getOther(currTalker);
				}
				currTalker.talk();
				lastSentence = currTalker.getCurrSentence();
				
			}
		}
		
	}
	
	public Sprite getPartner(){
		return partner;
	}
	
	public Sentence getLastSentence(){
		return lastSentence;
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
	
	public int getSentenceCount(){
		return sentenceCount;
	}
	
	public TalkativeSprite getOther(TalkativeSprite ts){
		if(ts == partner){
			return owner;
		}
		
		return partner;
	}
}
