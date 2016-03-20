package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Flowers extends TileCover {

	private static final long serialVersionUID = 3291673876930304108L;

	public Flowers(int nx, int ny, Village v) {
		super(nx, ny, v);
	}

	@Override
	public void makeData() {
		for(int i = H - Tile.HEIGHT;i  < H; i++){
			for(int j = 0; j < W ; j++){
				int d = RAND.nextInt(20);
				if(d == 0){
					data[i][j] = Sprite.getColor(200, 150, 20);
				}else if(d == 2){
					data[i][j] = Sprite.getColor(20, 20, 200);
				}else if(d == 1){
					data[i][j] = Sprite.getColor(230, 50, 20);
				}
			}
		}

	}
	
	public String getName(){
		return "flowers";
	}

}
