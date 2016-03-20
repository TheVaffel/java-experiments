package priv.hkon.theseq.nonblocks;

import java.util.Random;

import priv.hkon.theseq.blocks.Stationary;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public abstract class NonBlock extends Stationary {// A class for things that doesn't block, but neither lies on the ground
	
	private static final long serialVersionUID = -4727434592527069915L;
	public static Random RAND  = new Random(1337);
	
	public NonBlock(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public NonBlock(int nx, int ny, Structure s, Village v){
		super(nx, ny, s, v);
	}
	
	public Structure getStructure(){
		return structure;
	}

}
