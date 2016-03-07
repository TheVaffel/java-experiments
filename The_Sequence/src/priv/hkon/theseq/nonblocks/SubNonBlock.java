package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.world.Village;

public class SubNonBlock extends NonBlock { // Nonblock equivalent to SubBlock - Structure support

	public SubNonBlock(int nx, int ny, int[][] data, Structure s, Village v) {
		super(nx, ny, s, v);
		this.data = data;
	}

	@Override
	public void makeData() {

	}

}
