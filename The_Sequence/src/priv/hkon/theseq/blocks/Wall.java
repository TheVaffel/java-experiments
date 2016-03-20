package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Wall extends Block {

	private static final long serialVersionUID = -2762765267384958235L;

	public Wall(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public Wall(int nx, int ny, Structure s, Village v){
		super(nx, ny, s, v);
	}
	
	public void makeData(){
		for(int i = 0; i < H ; i++){
			for(int j = 0; j < W ; j++){
				int d  = (i/2+j/2) % 2;
				data[i][j] = getColor(64 + d * 64, 64 + d * 64, 64 + d*64);
				
				
				if(i < Tile.HEIGHT){
					int c = 100;
					float px = ((float)j)/W;
					float py = ((float)i+1)/Tile.HEIGHT;
					c += (int)(14*(6*px*(1 - px) +6*py*(1 - py)));
					int e = RAND.nextInt(16);
					data[i][j] = getColor(c + e, c + e, c + e);
				}
				else 
				{
					int e = (int)(Sprite.RAND.nextInt(28)*((float)(Math.abs(W/2 - j))/(W/2)));
					data[i][j] = getColor(128 - e, 128 - e, 128 - e);
				}
			}
		}
	}
	
	public String getName(){
		return "wall";
	}
	
}
