package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Marble extends Block {

	private static final long serialVersionUID = 7376101173836070277L;

	public Marble(int nx, int ny, Village v) {
		super(nx, ny, v);
	}

	public Marble(int nx, int ny, Structure s, Village v) {
		super(nx, ny, s, v);
	}

	@Override
	public void makeData() {
		for(int i = 0; i < H ; i++){
			for(int j = 0; j < W ; j++){	
				if(i < Tile.HEIGHT){
					int c = 100;
					float px = ((float)j + 1)/(W + 2);
					float py = ((float)i+2)/(Tile.HEIGHT + 2);
					c += Math.min((int)(30*(6*px*(1 - px) +6*py*(1 - py))), 140);
					int e = RAND.nextInt(16);
					data[i][j] = getColor(c + e, c + e, c + e);
				}
				else 
				{
					int e = (int)(Sprite.RAND.nextInt(28)*((float)(Math.abs(W/2 - j))/(W/2)));
					data[i][j] = getColor(192 - e, 192 - e, 192 - e);
				}
			}
		}
	}

	@Override
	public String getName() {
		return "Marble Wall";
	}

}
