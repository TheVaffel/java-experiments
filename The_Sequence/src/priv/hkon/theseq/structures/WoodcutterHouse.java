package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.WoodenWall;
import priv.hkon.theseq.world.Village;

public class WoodcutterHouse extends House {

	private static final long serialVersionUID = 8126670548184200698L;

	public WoodcutterHouse(int nx, int ny, int nw, int nh, Village v) {
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
		
		bed = new Bed(0, 0, village);
		addStructure(bed, w-2, 1);
	}

}
