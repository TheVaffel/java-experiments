package priv.hkon.theseq.blocks;

import priv.hkon.theseq.world.Village;

public abstract class Plant extends Block {
	
	float health = 100.0f;

	public Plant(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public boolean tick(){
		super.tick();
		health -= 0.00001f;
		return false;
	}

}