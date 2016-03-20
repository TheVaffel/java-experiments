package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Village;

public class Tree extends Plant {

	private static final long serialVersionUID = 1351870665040962763L;


	public Tree(int nx, int ny, Village v) {
		super(nx, ny, v);
	}

	@Override
	public void makeData() {
		for(int i = H/2; i < H - 2; i++){
			for(int j = W/4; j < 3*W/4; j++){
				int d = Sprite.getRandomInt(j) % 40;
				int c = Sprite.RAND.nextInt(20);
				data[i][j] = Sprite.getColor(120 + c + d, 90 + c + d, 20);
			}
		}
		for(int i = 0; i < 2*H/3; i++){
			for(int j = W/8; j < 7*W/8 ;j++){
				int d = Sprite.RAND.nextInt(20);
				data[i][j] = Sprite.getColor(20, 150 - d, 20);
			}
		}
	}

	
	/*public boolean tick(){
		return true;
	}*/
	
	public String getName(){
		return "tree";
	}
}
