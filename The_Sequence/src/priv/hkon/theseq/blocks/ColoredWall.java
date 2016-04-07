package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class ColoredWall extends Wall {
	
	int r,g,b;
	
	private static final long serialVersionUID = 6110080773446561162L;

	public ColoredWall(int nx, int ny, Village v) {
		super(nx, ny, v);
		
	}

	public ColoredWall(int nx, int ny, Structure s, Village v) {
		super(nx, ny, s, v);
	}
	
	public void makeData(){
		
		r = RAND.nextInt(200) + 56;
		g = RAND.nextInt(200) + 56;
		b = RAND.nextInt(200) + 56;
		
		for(int i = 0; i < H ; i++){
			for(int j = 0; j < W ; j++){
				int d  = (i/2+j/2) % 2;
				data[i][j] = getColor(r/4 + d * 64, g/4 + d * 64, b/4 + d*64);
				
				
				if(i < Tile.HEIGHT){
					int c = 0;
					float px = ((float)j)/W;
					float py = ((float)i+1)/Tile.HEIGHT;
					c += (int)(14*(6*px*(1 - px) +6*py*(1 - py)));
					int e = RAND.nextInt(16);
					data[i][j] = getColor(c + r/4 + e, c + g/4 + e, c + b/4 + e);
				}
				else 
				{
					int e = (int)(Sprite.RAND.nextInt(28)*((float)(Math.abs(W/2 - j))/(W/2)));
					data[i][j] = getColor(r/2 - e, g/2 - e, b/2 - e);
				}
			}
		}
	}

}
