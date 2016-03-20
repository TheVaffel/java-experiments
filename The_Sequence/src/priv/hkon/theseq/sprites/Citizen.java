package priv.hkon.theseq.sprites;

import priv.hkon.theseq.misc.Notification;
import priv.hkon.theseq.world.Village;

public abstract class Citizen extends Movable{
	
	private static final long serialVersionUID = -3959549715640637561L;


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
		receiveNotification(new Notification(Notification.TYPE_INVITATION_TO_CONVERSATION,
				importance, caller));
	}
	
	public void receiveNotification(Notification n){
		receivedNotifications.add(n);
	}
}
