package priv.hkon.theseq.sprites;

import java.util.LinkedList;

import priv.hkon.theseq.misc.Conversation;
import priv.hkon.theseq.misc.DialogBubble;
import priv.hkon.theseq.misc.Notification;
import priv.hkon.theseq.world.Village;

public abstract class TalkativeSprite extends Sprite{
	
	protected DialogBubble dialog;
	public boolean showDialog = false;
	protected int dialogDuration;
	protected int timeSinceDialogReset;
	
	protected Conversation conversation;
	
	LinkedList<String> sentence = new LinkedList<String>();
	LinkedList<Notification> receivedNotifications = new LinkedList<Notification>();

	public TalkativeSprite(int nx, int ny, Village v) {
		super(nx, ny, v);
		
		dialog = new DialogBubble(this);
	}
	
	public void setDialogString(String str){
		dialog.setString(str);
	}
	
	public void showDialog(int dur){
		showDialog = true;
		timeSinceDialogReset = 0;
		dialogDuration = dur;
		
	}
	
	public void hideDialog(){
		showDialog = false;
		dialogDuration = 0;
	}
	
	public boolean shouldDrawDialog(){
		return showDialog;
	}
	
	public DialogBubble getDialog(){
		return dialog;
	}
	
	public boolean tick(){
		super.tick();
		if(showDialog){
			timeSinceDialogReset++;
			if(timeSinceDialogReset >= dialogDuration){
				if(!sentence.isEmpty()){
					setDialogString(sentence.poll());
					timeSinceDialogReset = 0;
				}else{
					if(conversation != null&& conversation.getTalker() == this && conversation.isOn()){
						conversation.finishedSentence();
					}
					hideDialog();
				}
			}
		}
		return false;
	}
	
	void engageConversation(TalkativeSprite s, int importance){
		conversation = new Conversation(this, s, importance);
		setDialogString("Hey, you!");
		showDialog(60*2);
	}
	
	public void inviteTo(Conversation c){
		if(importantEnough(c)){
			conversation = c;
		}
	}
	
	protected boolean importantEnough(Conversation c){
		return true;
	}
	
	public void deniedConversation(){
		conversation = null;
		setDialogString("Aaaah, why will nobody listen?");
		showDialog(60*2);
	}
	
	

}