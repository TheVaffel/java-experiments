package priv.hkon.theseq.structures;

import priv.hkon.theseq.nonblocks.NonBlock;
import priv.hkon.theseq.world.Village;

public abstract class Building extends Structure{
	
	private static final long serialVersionUID = -4378685563749284722L;
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
	
	public int[][] getEntrances(){
		return entrances;
	}
	
	public void addStructure(Structure structure, int nx, int ny){
		structure.setX(getX() + nx);
		structure.setY(getY() + ny);
		for(int i = 0; i < structure.getH(); i++){
			for(int j = 0; j < structure.getW(); j++){
				if(contains(getX() + nx + j, getY() + ny + i)){
					if(structure.getBlockAt(j, i) != null){
						structure.getBlockAt(j, i).setX(getX() + nx + j);
						structure.getBlockAt(j, i).setY(getY() + ny + i);
					}
					blocks[ny + i][nx + j] = structure.getBlockAt(j, i);
					if(structure.getNonBlockAt(j, i) != null){
						structure.getNonBlockAt(j, i).setX(getX() + nx + j);
						structure.getNonBlockAt(j, i).setY(getY() + ny + i);
					}
					nonBlocks[ny + i][nx + j] = structure.getNonBlockAt(j, i);
				}
			}
		}
	}
}
