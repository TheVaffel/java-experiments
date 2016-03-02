package priv.hkon.theseq.sprites;

public class Conversation {
	
	Sprite owner;
	Sprite partner;
	
	boolean isOn = false;
	
	Sprite currTalker;
	int importance;
	
	boolean finished = false;
	
	int lastSentenceStart;

	public Conversation(Sprite o, Sprite p, int importance) {
		owner = o;
		partner = p;
		currTalker = owner;
		
		this.importance = importance;
		
		invite();
	}
	
	void invite(){
		partner.inviteTo(this);
	}
	
	void finishedSentence(){
		currTalker = (currTalker == owner) ? partner : owner;
	}
	
	Sprite getTalker(){
		return currTalker;
	}
	
	void disconnect(Sprite s){
		if(partner == s){
			partner = null;
		}else{
			owner = null;
		}
		
		finished = true;
	}
	
	void tick(){
		if(finished){
			return;
		}
		if(!isOn){
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
	
	Sprite getPartner(){
		return partner;
	}
	
	Sprite getOwner(){
		return owner;
	}
	
	int getImportance(){
		return importance;
	}
	
	boolean isFinished(){
		return finished;
	}
	
	boolean isOn(){
		return isOn;
	}
}
