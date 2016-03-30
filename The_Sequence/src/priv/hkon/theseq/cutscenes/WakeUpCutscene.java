package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.DarkenFilter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Player;

public class WakeUpCutscene extends Cutscene{
	
	Player player;
	
	DarkenFilter darkenFilter;
	
	public WakeUpCutscene(Core core, Player player) {
		super(core);
		this.player = player;
		
		player.isPartOfCutscene = true;
		darkenFilter = new DarkenFilter(0);
	}

	public void tick(){
		if(tickCount < 240){
			darkenFilter.darkness = 255 - tickCount;
			core.setCutsceneFilter(darkenFilter);
		}
		super.tick();
		
	}

	@Override
	public boolean isFinished() {
		return tickCount > 240;
	}
	
	public void close(){
		super.close();
		player.isPartOfCutscene = false;
		player.tryStartMoving(Movable.LEFT);
	}

}
