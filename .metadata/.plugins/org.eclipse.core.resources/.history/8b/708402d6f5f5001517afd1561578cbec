package priv.hkon.theseq.sprites;

import priv.hkon.theseq.misc.Conversation;
import priv.hkon.theseq.world.Village;

public class Player extends Citizen {
	
	private static final long serialVersionUID = -3488395255346822868L;

	public Player(int x, int y, Village v, int i){
		super(x, y, v, i);
		moveSpeed = 0.125f/2;
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
		if(conversation != null){
			if(conversation.isFinished()){
				conversation = null;
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
