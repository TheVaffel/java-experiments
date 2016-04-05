package priv.hkon.theseq.blocks;

import java.util.ArrayList;

import priv.hkon.theseq.items.Item;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Crate extends Block {

	private static final long serialVersionUID = -8445849179162489497L;
	
	ArrayList<Item> items;
	
	public static final int DEFSIZE = 50;

	public Crate(int nx, int ny, Village v) {
		super(nx, ny, v);
		items = new ArrayList<Item>();
	}

	public Crate(int nx, int ny, Structure s, Village v) {
		super(nx, ny, s, v);
		items = new ArrayList<Item>();
	}

	@Override
	public void makeData() {
		int offy = H/3;
		for(int i = offy; i < H; i++){
			for(int j = 0; j < W ; j++){
				int d = RAND.nextInt(30);
				if( i < offy + Tile.HEIGHT){
					if( j == 0 || j == W-1){
						data[i][j] = Sprite.getColor(200 + d, 200 + d, 150+ d);
					}else if( i +1 % 2 == 0){
						data[i][j] = Sprite.getColor(d, d, d);
					}else{
						data[i][j] = Sprite.getColor(180 + d, 180 + d, 150 + d);
					}
				}else{
					if(i % 2 == 1){
						data[i][j] = Sprite.getColor(d, d, d);
					}else{
						data[i][j] = Sprite.getColor(200 + d, 200 + d, 150 + d);
					}
				}
			}
		}


	}
	
	public boolean addItem(Item item){
		if(items.size() == DEFSIZE){
			return false;
		}
		else{
			items.add(item);
		}
		return true;
	}
	
	public int numItems(){
		return items.size();
	}

	@Override
	public String getName() {
		return "Crate";
	}

}
