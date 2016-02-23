package priv.hkon.theseq.world;

public abstract class Building extends Structure{
	
	boolean closed[][];
	int[][] entrances;
	
	public Building(int x, int y, int w, int h, Village v){
		super(x, y, w, h, v);
		closed = new boolean[h][w];
		makeClosedSpace();
		makeEntrances();
	}
	
	public int getTileAt(int x, int y){
		return tiles[y][x];
	}
	
	public NonBlock getNonBlockAt(int x, int y){
		return nonBlocks[y][x];
	}
	
	public boolean isClosedAt(int x, int y){
		if(x < 0 || x >= w || y < 0 || y >= h){
			return false;
		}
		return closed[y][x];
	}
	
	public boolean isClosedAtGlobal(int x, int y){
		return isClosedAt(x - this.x, y - this.y);
	}
	
	public abstract void makeClosedSpace();
	public abstract void makeEntrances();
}
