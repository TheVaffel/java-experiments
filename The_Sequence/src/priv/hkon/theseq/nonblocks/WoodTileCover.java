package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.items.Item;
import priv.hkon.theseq.items.WoodItem;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class WoodTileCover extends Pickable{

	public WoodTileCover(int nx, int ny, Village v) {
		super(nx, ny, v);
	}

	private static final long serialVersionUID = -5244033331245440440L;

	@Override
	public String getName() {
		return "Wood";
	}
	
	public void makeData(){
		for(int i = 0; i < 5; i++){
			 float f = -RAND.nextFloat();
			 float t = Tile.HEIGHT + RAND.nextInt(Tile.HEIGHT);
			 for(int j = 2; j < Tile.WIDTH - 2; j++){
				 int u = (int)(t + j*Math.sin(f));
				 if(u > H - Tile.HEIGHT && u < H){
					 data[(int)(u)][j] = Sprite.getColor(100, 100, 50);
				 }
			 }
		}
	}

	@Override
	public Item getItem() {
		
		village.setTileCoverAt(null, x, y);
		return new WoodItem();
	}

}
