package priv.hkon.theseq.blocks;

import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public abstract class Block extends Stationary{
	
	public abstract void makeData();

	public Block(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public Block(int nx, int ny, Structure s, Village v){
		super(nx, ny, s, v);
	}
}
