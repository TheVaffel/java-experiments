package priv.hkon.theseq.blocks;

import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public abstract class Stationary extends Sprite {
	
	protected Structure structure = null;

	public Stationary(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public Stationary(int nx, int ny, Structure s, Village v){
		super(nx, ny, v);
		
		structure = s;
		
	}
	
	public boolean isStationary(){
		return true;
	}
	
}
