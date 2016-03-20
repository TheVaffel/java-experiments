package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public class SubNonBlock extends NonBlock { // Nonblock equivalent to SubBlock - Structure support

	private static final long serialVersionUID = -6131583218385670661L;

	public SubNonBlock(int nx, int ny, int[][] data, Structure s, Village v) {
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
