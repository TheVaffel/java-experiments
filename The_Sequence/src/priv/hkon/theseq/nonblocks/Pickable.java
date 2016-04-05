package priv.hkon.theseq.nonblocks;

import priv.hkon.theseq.items.Item;
import priv.hkon.theseq.world.Village;

public abstract class Pickable extends TileCover {

	private static final long serialVersionUID = 4848655994832487586L;

	public Pickable(int nx, int ny, Village v) {
		super(nx, ny, v);
	}
	
	public String getInteractMessage(){
		return "Press P to pick up";
	}
	
	public abstract Item getItem();

}
