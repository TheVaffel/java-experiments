package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class WoodenWall extends Block {


	private static final long serialVersionUID = 5116005960323060971L;

	public WoodenWall(int nx, int ny, Village v) {
		super(nx, ny, v);
		// TODO Auto-generated constructor stub
	}

	public WoodenWall(int nx, int ny, Structure s, Village v) {
		super(nx, ny, s, v);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void makeData() {
		for(int i = 0; i< H; i++){
			for(int j = 0; j < W; j++){
				int d = (int)(Sprite.RAND.nextInt(50) + 100);
				data[i][j] = Sprite.getColor(d, d, d-60);
				if(i%4 == 1){
					if(Sprite.RAND.nextInt(8) != 0){
						data[i][j] = Sprite.getColor(0,0, 0);
					}
				}
				if(i < Tile.HEIGHT){
					int e = (int)(Sprite.RAND.nextInt(28)*((float)(Math.abs(W/2 - j))/(W/2)));
					data[i][j] = getColor(128 - e, 128 - e, 70 - e);
				}
			}
		}

	}

	@Override
	public String getName() {
		return "Wooden Wall";
	}

}
