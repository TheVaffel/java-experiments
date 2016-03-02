package priv.hkon.theseq.sprites;

import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Door extends NonBlock{
	
	int timeSinceUnblocked = 0;

	public Door(int nx, int ny, Village v) {
		super(nx, ny, v);
		
	}

	@Override
	public void makeData() {
		for(int i = H - Tile.HEIGHT; i < H; i++){
			for(int j = 0; j < W; j++){
				data[i][j] = Sprite.getColor(150, 120, 50);
			}
		}

		data[2*H/3][2] = Sprite.getColor(200, 200, 50);
	}
	
	public void makeAnimationFrames(){
		numAnimations = 1;
		numFrames = 2;
		animationFrames = new int[numAnimations][numFrames][H][W];
		animationFrames[0][0] = data;
		for(int i = Tile.HEIGHT/3; i < H; i++){
			animationFrames[0][1][i][W - 1] = Sprite.getColor(150, 120, 50);
		}
	}
	
	public boolean tick(){
		if(village.getSpriteAt(x, y) != null){
			data = animationFrames[0][1];
			timeSinceUnblocked = 0;
		}else{
			data = animationFrames[0][0];
			timeSinceUnblocked++;
		}
		return false;
	}
}
