package priv.hkon.theseq.world;

public abstract class Stationary extends Sprite {
	
	protected Structure structure = null;

	public Stationary(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public Stationary(int nx, int ny, Structure s, Village v){
		super(nx, ny, v);
		
		structure = s;
		
		
	}
	
}
