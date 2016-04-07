package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.ColoredWall;
import priv.hkon.theseq.nonblocks.BrushHolder;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Atelier extends Building {

	private static final long serialVersionUID = -1049541145092829028L;

	public Atelier(int x, int y, int w, int h, Village v) {
		super(x, y, w, h, v);
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

	}

	@Override
	public void makeBlocks() {
		for(int i = 0; i < h; i++){
			blocks[i][0] = new ColoredWall(x, y + i, village);
			blocks[i][w-1] = new ColoredWall(x + w-1, y + i, village);
		}
		
		for(int i = 0; i < w; i++){
			blocks[0][i] = new ColoredWall(x + i, y, village);
			blocks[h-1][i] = new ColoredWall(x + i, y + h-1, village);
		}
		
		int x = 2*w/3 - 1;
		nonBlocks[0][x] = (new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_RED));
		x++;
		nonBlocks[0][x] = (new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_GREEN));
		x++;
		nonBlocks[0][x] = (new BrushHolder(this.x + x, this.y, this, village, Tile.TYPE_CANVAS_BLUE));
	}

	@Override
	public String getName() {
		return "Atelier";	
	}

	@Override
	public void makeTiles() {
		for(int i = 0; i < h; i++){
			for( int j = 0; j < w; j++){
				tiles[i][j] = Tile.TYPE_WOODEN_FLOOR;
			}
		}
		
		for(int i = 4; i < 9; i++){
			for( int j = 3; j < 8; j++){
				tiles[i][j] = Tile.TYPE_CANVAS_WHITE;
			}
			
			for( int j = 9; j < 14; j++){
				tiles[i][j] = Tile.TYPE_CANVAS_WHITE;
			}
		}

	}
	
	public int getCanvasX(){
		return 3;
	}
	
	public int getCanvasY(){
		return 4;
	}

}
