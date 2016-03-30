package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.Marble;
import priv.hkon.theseq.world.Village;

public class MayorHouse extends House {

	private static final long serialVersionUID = -7425188677041090416L;

	public MayorHouse(int nx, int ny, int nw, int nh, Village v) {
		super(nx, ny, nw, nh, v);
	}
	
	public void makeBlocks(){
		for(int i = 0; i < w; i++){
			blocks[0][i] = new Marble(x + i, y, village);
			blocks[h - 1][i] = new Marble(x + i, y + h - 1, village);
		}
		
		for(int i = 0 ; i < h; i++){
			blocks[i][0] = new Marble(x, y + i, village);
			blocks[i][w - 1] = new Marble(x + w -1, y + i, village);
		}
		
		bed = new Bed(0, 0, village);
		addStructure(bed, w-2, 1);
	}
	

}
