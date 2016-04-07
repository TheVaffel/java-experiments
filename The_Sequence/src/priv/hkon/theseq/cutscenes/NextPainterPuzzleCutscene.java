package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.ColoredSquaresFilter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.main.Screen;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Painter;
import priv.hkon.theseq.sprites.Player;

public class NextPainterPuzzleCutscene extends Cutscene {
	
	Player pl;
	Painter pa;
	
	ColoredSquaresFilter csf = new ColoredSquaresFilter(0, Screen.W, Screen.H);

	public NextPainterPuzzleCutscene(Painter p1, Player p2, Core c) {
		super(c);
		
		pa = p1;
		pl = p2;
		
		pl.isPartOfCutscene = true;
		pa.isPartOfCutscene = true;
		
		happenings.add(new Happening(pl, 60){
			public void happen(){
				core.village.moveSpriteTo(sprite, pa.getAtelier().getX() + pa.getAtelier().getW()/2, pa.getAtelier().getY() + 1);
				((Movable)sprite).turnTowards(Movable.DOWN);
			}
		});
		
		happenings.add(new Happening(pa, 60){
			public void happen(){
				pa.applyChallengePattern(pa.puzzleNumber);
			}
		});
	}
	
	public void tick(){
		if(tickCount < 60){
			csf.out = false;
			csf.dist = tickCount;
			
		}else{
			csf.out = true;
			csf.dist = tickCount - 60;
		}
		
		core.setCutsceneFilter(csf);
		
		super.tick();
	}

	@Override
	public boolean isFinished() {
		return tickCount > 2*60;
	}
	
	public void close(){
		super.close();
		pl.isPartOfCutscene = false;
		pa.isPartOfCutscene = false;
		pl.carryColor = 0;
	}

}
