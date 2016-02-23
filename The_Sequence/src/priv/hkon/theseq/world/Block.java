package priv.hkon.theseq.world;

public abstract class Block extends Stationary{
	
	public abstract void makeData();

	public Block(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public Block(int nx, int ny, Structure s, Village v){
		super(nx, ny, s, v);
	}
}
