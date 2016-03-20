package priv.hkon.theseq.blocks;

import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public class SubBlock extends Block { // A class with no particular data, to support custom Structures

	private static final long serialVersionUID = -7506624519552930567L;

	public SubBlock(int nx, int ny, int[][] data, Structure s, Village v) {
		super(nx, ny, s, v);
		this.data = data;
	}

	@Override
	public void makeData() {

	}
	
	public String getName(){
		return structure.getName();
	}

}
