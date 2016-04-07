package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.ColoredWall;
import priv.hkon.theseq.nonblocks.BrushHolder;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class PainterHouse extends House {
	
	BrushHolder redHolder, greenHolder, blueHolder;

	private static final long serialVersionUID = 5463311785486218948L;

	public PainterHouse(int nx, int ny, int nw, int nh, Village v) {
		super(nx, ny, nw, nh, v);
		
		
	}
	
	public void makeBlocks(){
		for(int i = 0; i < w; i++){
			blocks[0][i] = new ColoredWall(x + i, y, village);
			blocks[h - 1][i] = new ColoredWall(x + i, y + h - 1, village);
		}
		
		for(int i = 0 ; i < h; i++){
			blocks[i][0] = new ColoredWall(x, y + i, village);
			blocks[i][w - 1] = new ColoredWall(x + w -1, y + i, village);
		}
		
		bed = new Bed(0, 0, village);
		addStructure(bed, w-2, 1);
		
		int x = w/2 - 1;
		nonBlocks[0][x] = (redHolder = new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_RED));
		x++;
		nonBlocks[0][x] = (greenHolder = new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_GREEN));
		x++;
		nonBlocks[0][x] = (blueHolder = new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_BLUE));
	}
	
	@Override
	public void makeTiles() {
		super.makeTiles();
		for (int i = 2; i< h-2; i++){
			for(int j = 2; j < w - 2; j++){
				tiles[i][j] = Tile.getRandomCanvas();
			}
		}
	}

}
