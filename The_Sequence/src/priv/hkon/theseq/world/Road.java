package priv.hkon.theseq.world;

public class Road extends Area {
	
	private static final long serialVersionUID = -3333814074542750597L;
	public static final int MAX_INTERSECTIONS = 15;

	public Road(int nx, int ny, int nw, int nh, Village v) {
		super(nx, ny, nw, nh, v);
	}

	@Override
	public void makeTiles() {
		tiles = new int[h][w];
		for(int i = 0; i < h; i++){
			for(int j = 0; j< w; j++){
				tiles[i][j] = Tile.TYPE_REFINED_ROCK;
			}
		}
	}

}
