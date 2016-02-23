package priv.hkon.theseq.world;

public class Player extends Movable {
	
	public Player(int x, int y, Village v){
		super(x, y, v);
		moveSpeed = 0.5f;
	}
	
	@Override
	public void makeData() {
		for(int i = 0; i < H; i++){
			for(int j = 0; j< W; j++){
				data[i][j] = 0;
			}
		}
		
		for(int i = 0; i < H; i++){
			for(int j = Tile.WIDTH/3; j< 2*Tile.WIDTH/3; j++){
				data[i][j] = Sprite.getColor(255, 0, 0);
			}
		}
	}
	
	public void makeAnimationFrames(){
		numFrames = 8;
		numAnimations = 1;
		animationFrames = new int[numAnimations][numFrames][H][W];
		
		for(int i = 0; i < numFrames; i++){
			for(int py = H - (int)(2*H/3 + H*Math.sin(((float)(i))/numFrames*Math.PI*2)/3 - 2); py < H; py++){
				for(int x = W/3; x < 2*W/3; x++){
					animationFrames[0][i][py][x] = Sprite.getColor(255, 0, 0);
				}
			}
		}
	}
	
	public boolean tick(){
		boolean b = super.tick();
		
		data = animationFrames[0][Math.min((int)(getMovedFraction()*numFrames), numFrames - 1)];
		return b;
	}

}
