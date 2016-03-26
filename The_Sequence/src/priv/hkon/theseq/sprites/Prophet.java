package priv.hkon.theseq.sprites;

import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Village;

public class Prophet extends Villager {

	private static final long serialVersionUID = -4204665935086265739L;
	
	public static final String[] MEANING_OF_LIFE = {
		"The answer would surprise you.", "Be patient, it will probably be revealed soon enough.",
		"You ask as if you know there is an answer.", "I like potatoes."
	};

	public Prophet(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
	}
	
	public boolean tick(){
		return super.tick();
	}
	
	public void work(){
		if(!hasPath){
			strollTownGrid();
		}
	}
	
	public String getMeaningOfLife(){
		return MEANING_OF_LIFE[RAND.nextInt(MEANING_OF_LIFE.length)];
	}

}
