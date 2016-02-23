package priv.hkon.theseq.world;

public class House extends Building{
	
	//TODO: Furniture, please!
	
	public House(int nx, int ny, int nw, int nh, Village v){
		super(nx, ny, nw, nh, v);
	}
	
	public void makeBlocks(){
		for(int i = 0; i < w; i++){
			blocks[0][i] = new Wall(x + i, y, village);
			blocks[h - 1][i] = new Wall(x + i, y + h - 1, village);
		}
		
		for(int i = 0 ; i < h; i++){
			blocks[i][0] = new Wall(x, y + i, village);
			blocks[i][w - 1] = new Wall(x + w -1, y + i, village);
		}
		
		
	}
	
	@Override
	public void makeClosedSpace(){
		for(int i = 1; i<h-1; i++){
			for(int j = 1 ; j < w - 1; j++){
				closed[i][j] = true;
			}
		}
	}

	
	public void makeTiles() {
		for (int i = 0; i< h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j] = Tile.TYPE_WOODEN_FLOOR;
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
}
