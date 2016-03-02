package priv.hkon.theseq.sprites;

import priv.hkon.theseq.world.Village;

public abstract class Citizen extends Movable{
	
	int citizenNumber;
	

	boolean isCalledUpon = false;
	Sprite caller;
	
	int callImportance;

	public Citizen(int x, int y, Village v, int id) {
		super(x, y, v);
		citizenNumber = id;
	}
	
	public int getCitizenNumber(){
		return citizenNumber;
	}
	
	public void calledUpon(Sprite caller, int importance){
		this.caller = caller;
		this.callImportance = importance;
		isCalledUpon = true;
	}
}
