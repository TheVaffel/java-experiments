package priv.hkon.theseq.world;

public abstract class NonBlock extends Stationary {// A class for things that doesn't block, but neither lies on the ground

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
