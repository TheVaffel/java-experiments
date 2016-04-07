package priv.hkon.theseq.sprites;

import java.awt.event.KeyEvent;

import priv.hkon.theseq.blocks.Crate;
import priv.hkon.theseq.items.Item;
import priv.hkon.theseq.main.Controller;
import priv.hkon.theseq.misc.Conversation;
import priv.hkon.theseq.nonblocks.BrushHolder;
import priv.hkon.theseq.nonblocks.NonBlock;
import priv.hkon.theseq.nonblocks.Pickable;
import priv.hkon.theseq.structures.Bed;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Player extends Citizen {
	
	private static final long serialVersionUID = -3488395255346822868L;
	
	Item carryItem;
	
	public int carryColor = 0;

	public Player(int x, int y, Village v, int i){
		super(x, y, v, i);
		//moveSpeed = 0.125f/2;
		moveSpeed = 0.25f;
	}
	
	@Override
	public void makeData() {
		byte[] b = new byte[3];
		RAND.nextBytes(b);
		for(int i = 0; i < 3; i++){
			b[i] = (byte) (Math.abs(b[i]));
		}
		//coatColor = Sprite.getColor(b[0], b[1], b[2]);
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int d = (j-W/2)*(j-W/2) + (i - Villager.HEAD_OFFSET_Y)*(i - Villager.HEAD_OFFSET_Y);
				int c = 255;
				if(Math.abs(j - W/2) < ((float)i - Villager.HEAD_OFFSET_Y)/3){
					int p = (int) (30*Math.exp(-0.5*(int)Math.abs((j - W/2 - (float)(i-Villager.HEAD_OFFSET_Y)/5))));
					data[i][j] = Sprite.getColor( b[0] + p, b[1] + p, b[2] + p);
				}
				if(d < Villager.HEAD_SQ_RADIUS){
					c -= Math.sqrt(d)*15;
					data[i][j] = Sprite.getColor(c, c, c);
				}
			}
		}
	}
	
	public void makeAnimationFrames(){
		numFrames = 1;
		numAnimations = 4;
		animationFrames = new int[numAnimations][numFrames][H][W];

		//coatColor = Sprite.getColor(b[0], b[1], b[2]);
		for(int i = 0; i < numAnimations; i++){
			for(int j = 0; j < H; j++){
				for( int k = 0; k < W ; k++){
					animationFrames[i][0][j][k] = data[j][k];
				}
			}
				
		}
		//int ringColor = Sprite.getColor(0, 128, 128);
		int black = Sprite.getColor(0, 0, 0);

		
		animationFrames[DOWN][0][Villager.HEAD_OFFSET_Y][W/2 - 2] = black;
		animationFrames[DOWN][0][Villager.HEAD_OFFSET_Y][W/2 + 2] = black;
		
		animationFrames[RIGHT][0][Villager.HEAD_OFFSET_Y][W/2 + 4] = black;
		animationFrames[LEFT][0][Villager.HEAD_OFFSET_Y][W/2 - 4] = black;
	}
	
	public boolean tick(){
		if(village.isInSleepMode()){
			return true;
		}
		NonBlock d;
		if(village.getTime()% Village.DAYCYCLE_DURATION > 30*60 &&(d =village.getNonBlockAt(x, y))!= null && d.getStructure() != null && d.getStructure() instanceof Bed){
			showDialog("Press S to sleep til daylight", 2);
			if(Controller.input[KeyEvent.VK_S]){
				village.turnOnSleepMode();
				return true;
			}
		}
		if(conversation != null){
			if(conversation.isFinished()){
				conversation = null;
			}
		}
		if(village.getTileCoverAt(x, y) instanceof Pickable&& conversation== null){
			if(carryItem == null){
				Pickable p = (Pickable)(village.getTileCoverAt(x, y));
			
				showDialog(p.getInteractMessage(), 2);
				if(Controller.input[KeyEvent.VK_P]){
					carryItem = p.getItem();
				}
			}else  if(Controller.input[KeyEvent.VK_P]){
				showDialog("Already carrying " + carryItem.getName(), 30);
			}
		}
		if(Tile.isCanvasTile(village.getTileAt(x, y))&& carryColor != 0){
			village.setTileAt(carryColor, x, y);
		}
		
		if(village.getSpriteAt(x + dx[movingDirection], y + dy[movingDirection]) instanceof Crate
				&& carryItem != null&& conversation == null){
			Crate c = ((Crate)(village.getSpriteAt(x + dx[movingDirection], y + dy[movingDirection])));
			showDialog("Press P to put " + carryItem.getName() + " in " + c.getName(), 2);
			if(Controller.input[KeyEvent.VK_P ]){
				if(c.addItem(carryItem)){
					carryItem = null;
				}else{
					showDialog(c.getName() + " is full", 30);
				}
				
			}
		}else if(village.getNonBlockAt(x + dx[movingDirection], y + dy[movingDirection]) instanceof BrushHolder){
			showDialog("Press B to switch brush", 3);
			if(Controller.input[KeyEvent.VK_B]){
				carryColor = ((BrushHolder)(village.getNonBlockAt(x + dx[movingDirection], y + dy[movingDirection]))).getColor();
			}
		}
		boolean b = super.tick();
		
		data = animationFrames[movingDirection][Math.min((int)(getMovedFraction()*numFrames), numFrames - 1)];
		return b;
	}
	
	public String getName(){
		return "Newcomer";
	}

	public void setConversation(Conversation c){
		conversation = c;
	}
}
