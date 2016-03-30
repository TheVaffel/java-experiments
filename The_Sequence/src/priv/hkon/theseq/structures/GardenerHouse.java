package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.Tree;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class GardenerHouse extends House {

	private static final long serialVersionUID = 4654229535664643649L;

	public GardenerHouse(int nx, int ny, int nw, int nh, Village v) {
		super(nx, ny, nw, nh, v);
	}
	
	public void makeBlocks(){
		for(int i = 0; i < w; i++){
			blocks[0][i] = new Tree(x + i, y, village);
			blocks[h - 1][i] = new Tree(x + i, y + h - 1, village);
		}
		
		for(int i = 0 ; i < h; i++){
			blocks[i][0] = new Tree(x, y + i, village);
			blocks[i][w - 1] = new Tree(x + w -1, y + i, village);
		}
		
		bed = new Bed(0, 0, village);
		addStructure(bed, w-2, 1);
	}
	
	
	@Override
	public void makeTiles() {
		for (int i = 0; i< h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j] = Tile.TYPE_GRASS;
			}
		}
	}

}
