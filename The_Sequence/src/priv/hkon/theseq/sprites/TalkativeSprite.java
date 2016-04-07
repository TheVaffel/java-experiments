package priv.hkon.theseq.sprites;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.LinkedList;

import priv.hkon.theseq.main.Controller;
import priv.hkon.theseq.misc.Conversation;
import priv.hkon.theseq.misc.DialogBubble;
import priv.hkon.theseq.misc.Notification;
import priv.hkon.theseq.misc.Sentence;
import priv.hkon.theseq.world.Village;

public abstract class TalkativeSprite extends Sprite{
	
	private static final long serialVersionUID = -1535401130566740803L;
	protected DialogBubble dialog;
	public boolean showDialog = false;
	protected int dialogDuration;
	protected int timeSinceDialogReset;
	
	protected Conversation conversation;
	protected Sentence currSentence;
	
	public boolean isPartOfCutscene = false;
	
	LinkedList<String> sentence = new LinkedList<String>();
	LinkedList<Integer> dialogLengths = new LinkedList<Integer>();
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
	
	public void showDialog(String str, int dur){
		setDialogString(str);
		showDialog(dur);
	}
	
	public void hideDialog(){
		showDialog = false;
		dialogDuration = 0;
	}
	
	public boolean shouldDrawDialog(){
		return showDialog && !dialog.isEmpty();
	}
	
	public DialogBubble getDialog(){
		return dialog;
	}
	
	public boolean tick(){
		super.tick();
		if(showDialog){
			timeSinceDialogReset++;
			/*if(debug){
				System.out.println("Time since dialog reset " + timeSinceDialogReset);
			}*/
			if(timeSinceDialogReset >10&& Controller.input[KeyEvent.VK_S]){
				timeSinceDialogReset = 10000;
			}
			if(timeSinceDialogReset >= dialogDuration){
				if(!sentence.isEmpty()){
					setDialogString(sentence.poll());
					if(!dialogLengths.isEmpty()){
						dialogDuration = dialogLengths.poll();
					}else{
						dialogDuration = 4*60;
					}
					timeSinceDialogReset = 0;
				}else{
					if(conversation != null&& conversation.getTalker() == this && conversation.isOn()){
						conversation.finishedSentence();
					}
					hideDialog();
				}
			}
		}
		if(isPartOfCutscene){
			return true;
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
	
	public Sentence getCurrSentence(){
		return currSentence;
	}
	
	public void talk(){
		conversation.finishedSentence();
	}
	
	
	public void setIsPartOfCutscene(boolean b){
		isPartOfCutscene = b;
	}
	
	public Conversation getConversation(){
		return conversation;
	}
	
	public void addSentence(String[] str){
		sentence.addAll(Arrays.asList(str));
	}
	
	public void addAndShowSentence(String[] str){
		addSentence(str);
		showDialog = true;
	}

}
