package priv.hkon.theseq.sprites;

import priv.hkon.theseq.world.Village;

public class Shadow extends Movable {

	private static final long serialVersionUID = 5028555876348388431L;

	public Shadow(int x, int y, Village v) {
		super(x, y, v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		return "Shadow";
	}

	@Override
	public void makeAnimationFrames() {
		
		numAnimations = 4;
		numFrames = 1;
		animationFrames = new int[numAnimations][numFrames][H][W];
		
		for(int u = 0; u < animationFrames.length; u++){
			for(int k = 0; k < animationFrames[u].length; k++){
				for(int i = 0; i < H; i++){
					for(int j = 0; j < W; j++){
						int d = (j-W/2)*(j-W/2) + (i - Villager.HEAD_OFFSET_Y)*(i - Villager.HEAD_OFFSET_Y);
						if(Math.abs(j - W/2) < ((float)i - Villager.HEAD_OFFSET_Y)/3){
							animationFrames[u][k][i][j] = Sprite.getColor( 0,0,0);
						}
						if(d < Villager.HEAD_SQ_RADIUS){
							animationFrames[u][k][i][j] = Sprite.getColor(0,0,0);
						}
					}
				}
			}
		}
		
		int red = Sprite.getColor(255, 0, 0);
		
		animationFrames[DOWN][0][Villager.HEAD_OFFSET_Y][W/2 - 2] = red;
		animationFrames[DOWN][0][Villager.HEAD_OFFSET_Y][W/2 + 2] = red;
		
		animationFrames[RIGHT][0][Villager.HEAD_OFFSET_Y][W/2 + 4] = red;
		animationFrames[LEFT][0][Villager.HEAD_OFFSET_Y][W/2 - 4] = red;
	}

	@Override
	public void makeData() {
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int d = (j-W/2)*(j-W/2) + (i - Villager.HEAD_OFFSET_Y)*(i - Villager.HEAD_OFFSET_Y);
				if(Math.abs(j - W/2) < ((float)i - Villager.HEAD_OFFSET_Y)/3){
					data[i][j] = Sprite.getColor( 0,0,0);
				}
				if(d < Villager.HEAD_SQ_RADIUS){
					data[i][j] = Sprite.getColor(0,0,0);
				}
			}
		}
		
	}

}
