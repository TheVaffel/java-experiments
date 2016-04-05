package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public abstract class TileCover extends TalkativeSprite {
	
	private static final long serialVersionUID = 6948279460203116282L;
	public static final int W = Tile.WIDTH;
	public static final int H = Tile.HEIGHT;

	public TileCover(int nx, int ny, Village v) {
		super(nx, ny, v);
	}

	@Override
	public void makeData() {

	}
	
	public String getInteractMessage(){
		return "";
	}
	
	public abstract String getName();

}
