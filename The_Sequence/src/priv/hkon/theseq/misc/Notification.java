package priv.hkon.theseq.misc;

import java.io.Serializable;

import priv.hkon.theseq.sprites.Sprite;

public class Notification implements Serializable{
	
	private static final long serialVersionUID = -7083971170485753706L;
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
