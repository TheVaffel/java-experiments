package priv.hkon.theseq.misc;

import priv.hkon.theseq.sprites.Sprite;

public class Notification {
	
	public static final int TYPE_INVITATION_TO_CONVERSATION = 0;
	public static final int TYPE_UNFRIENDLY = 1;
	
	int type, importance;
	Sprite creator;
	
	public Notification(int type, int importance, Sprite creator){
		this.type = type;
		this.importance = importance;
		this.creator = creator;
	}
	
	public Sprite getCreator(){
		return creator;
	}
	
	public int getType(){
		return type;
	}
	
	public int getImportance(){
		return importance;
	}
}	