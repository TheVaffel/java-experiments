package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.Crate;
import priv.hkon.theseq.blocks.WoodenWall;
import priv.hkon.theseq.nonblocks.Door;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class WoodcutterStorage extends Building {
	private static final long serialVersionUID = -4142559862099772478L;
	
	Crate crate;

	public WoodcutterStorage(int nx, int ny, int nw, int nh, Village v) {
		super(nx, ny, nw, nh, v);
	}
	
	@Override
	public void makeBlocks(){
		for(int i = 0; i < w; i++){
			blocks[0][i] = new WoodenWall(x + i, y, village);
			blocks[h - 1][i] = new WoodenWall(x + i, y + h - 1, village);
		}
		
		for(int i = 0 ; i < h; i++){
			blocks[i][0] = new WoodenWall(x, y + i, village);
			blocks[i][w - 1] = new WoodenWall(x + w -1, y + i, village);
		}
		
		crate = new Crate(x + w/2, y + 1, village);
		blocks[1][getW()/2] = crate;
	}

	@Override
	public void makeClosedSpace(){
		for(int i = 1; i<h-1; i++){
			for(int j = 1 ; j < w - 1; j++){
				closed[i][j] = true;
			}
		}
	}

	@Override
	public void makeEntrances() {
		blocks[h - 1][w/2] = null;
		nonBlocks[h-1][w/2] = new Door(x + w/2, y + h - 1, village);
		entrances = new int[1][2];
		entrances[0][0] = w/2;
		entrances[0][1] = h-1;
	}

	@Override
	public String getName() {
		return "CabinStorage";
	}

	public void makeTiles() {
		for (int i = 0; i< h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j] = Tile.TYPE_WOODEN_FLOOR;
			}
		}
	}

	public Crate getCrate(){
		return crate;
	}
}
