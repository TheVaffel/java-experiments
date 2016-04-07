package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class BrushHolder extends NonBlock {

	private static final long serialVersionUID = -2911968725062332758L;
	int rgb;
	int ci;

	public BrushHolder(int nx, int ny, Village v, int colorIndex) {
		super(nx, ny, v);
		ci = colorIndex;
		makeData();
	}

	public BrushHolder(int nx, int ny, Structure s, Village v, int colorIndex) {
		super(nx, ny, s, v);
		ci = colorIndex;
		makeData();
	}

	@Override
	public String getName() {
		return "BrushHolder";
	}

	@Override
	public void makeData() {
		rgb = Tile.getData(ci)[0][0];
		rgb = Sprite.multiplyColor(rgb, 0.5f);

		for(int i = 2 + Tile.HEIGHT; i < H/3 + Tile.HEIGHT; i++){
			for(int j = 2; j < W-2; j++){
				data[i][j] = rgb;
			}
		}
		
		for(int i = 3; i < W-3; i++){
			data[2 + Tile.HEIGHT][i] = Sprite.getColor(0, 0, 0);
		}
	}
	
	public int getColor(){
		return ci;
	}

}
