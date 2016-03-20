package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.SubBlock;
import priv.hkon.theseq.nonblocks.SubNonBlock;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Bed extends Structure {

	private static final long serialVersionUID = -937898542135684735L;

	public Bed(int nx, int ny, Village v) {
		super(nx, ny, 1, 2, v);
	}

	@Override
	public void makeBlocks() {
		int[][] data1 = new int[Sprite.H][Sprite.W];//Upper Sprite
		
		for(int i = Tile.HEIGHT/2; i< Sprite.H; i++){
			for(int j = 0; j< Sprite.W; j++){
				if(Math.abs(j - Sprite.W/2 + 0.5) + Math.abs(i -Tile.HEIGHT/2)  > 6){
					data1[i][j] = Sprite.getColor(50,  50, 20);
				}
			}
		}
		
		for(int i = Sprite.H/4 + Tile.HEIGHT; i  < Sprite.H; i++){
			for(int j = 0; j < Sprite.W; j++){
				data1[i][j] = Sprite.getColor(220, 220, 220);
			}
		}
		
		blocks[0][0] = new SubBlock(x, y, data1, this, village);
		
		int[][] data2 = new int[Sprite.H][Sprite.W];
		
		
		for(int i = 2*Sprite.H/5; i < Sprite.H; i++){
			for(int j = 0; j < Sprite.W; j++){
				double d = Math.sqrt(Math.abs(j - Sprite.W/2));
				data2[i][j] = Sprite.getColor((int)(200 - 15*d), (int)(200 - 15*d), (int)(200 - 15*d));
				if( (i - Tile.HEIGHT - 8)*(i - Tile.HEIGHT - 8) + (j - Sprite.W/2)*(j - Sprite.W/2) < 10*10){
					data2[i][j] = Sprite.getColor(50,  50, 20);
				}
			}
		}
		
		nonBlocks[1][0] = new SubNonBlock(x, y + 1, data2, this, village);
	}

	@Override
	public void makeTiles() {
	}
	
	public String getName(){
		return "bed";
	}

}
