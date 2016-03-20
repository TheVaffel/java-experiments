package priv.hkon.theseq.world;

import java.io.Serializable;

public abstract class Area implements Serializable{
	
	private static final long serialVersionUID = 5337909119183453964L;
	protected int x, y, w, h;
	protected Village village;
	
	protected int tiles[][];
	
	public Area(int nx, int ny, int nw, int nh, Village v) {
		x = nx;
		y = ny;
		w = nw;
		h = nh;
		village = v;
		tiles = new int[h][w];
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				tiles[i][j] = -1;
			}
		}
		
		makeTiles();
	}
	
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getW(){
		return w;
	}
	
	public int getH(){
		return h;
	}
	
	public int getTileAt(int x, int y){
		return tiles[y][x];
	}
	
	public boolean contains(int x, int y){
		return x >=this.x && x < this.x + w&& y >= this.y && y < this.y + h;
	}
	
	public abstract void makeTiles();

}
