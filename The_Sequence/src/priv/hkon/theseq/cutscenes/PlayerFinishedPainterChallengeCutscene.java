package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.ColoredSquaresFilter;
import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.main.Screen;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Painter;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;

public class PlayerFinishedPainterChallengeCutscene extends Cutscene {
	
	Painter pa;
	Player pl;
	
	ColoredSquaresFilter csf = new ColoredSquaresFilter(0, Screen.W, Screen.H);
	
	int warpTime = (int) 10000;

	public PlayerFinishedPainterChallengeCutscene(Painter p1, Player p2, Core core) {
		super(core);
		
		pa = p1;
		pl = p2;
		
		pl.isPartOfCutscene = true;
		pa.isPartOfCutscene = true;
		
		happenings.add(new Happening(pl, 60){
			public void happen(){
				core.village.moveSpriteTo(sprite, pa.getX() + 1, pa.getY());
				((Movable)sprite).turnTowards(Movable.LEFT);
			}
		});
		
		happenings.add(new Happening(pa, 2*60){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Oh wow!", 2*60);
			}
		});
		
		happenings.add(new Happening(pa, 4*60){
			public void happen(){
				((TalkativeSprite)sprite).addSentence(new String[]{
						"You actually did it!",
						"I must say.. The art is strong in you",
						"You know what? That was a perfect example",
						"Art is about creating patterns",
						"But REAL art breaks the rules to get it done!",
						"You should be proud"
				}
				);
				((TalkativeSprite)sprite).showDialog = true;
			}
		});
		
		happenings.add(new Happening(pa, warpTime + 60 ){
			public void happen(){
				core.village.moveSpriteTo(sprite, pa.getHome().getX() + pa.getHome().getW()/2, pa.getHome().getY() + 1);
			}
		});
		
		happenings.add(new Happening(pl, warpTime + 60 ){
			public void happen(){
				core.village.moveSpriteTo(sprite, pa.getHome().getX() + pa.getHome().getW()/2 + 1, pa.getHome().getY() + 1);
			}
		});
		
		happenings.add(new Happening(pa, warpTime + 2*60 + 30){
			public void happen(){
				((TalkativeSprite)sprite).addSentence(new String[]{
						"I hope we meet again!",
						"Wait, you look very confused..",
						"You didn't like the teleporting?",
						"Doesn't make sense?",
						"Do you think that knowledge truly is",
						"The single most meaningful in this life?",
						"Nonono, my friend, I don't think it works like that",
						"You see, knowledge makes sense, but it never makes meaning",
						"The meaning is in the art",
						"The meaning comes from our souls",
						"Oh, and to justify the teleportation...",
						"I'm an artist. I do what the f*ck I want.",
						"Goodbye!"
				}
				);
				((Villager)sprite).lastVictim = pl;
				((TalkativeSprite)sprite).showDialog = true;
			}
		});
		
		
	}
	
	public void tick(){
		if(tickCount > 4*60 + 1 && !pa.showDialog && tickCount < warpTime){
			tickCount = warpTime;
		}
		if(tickCount < 2*60){
			if(tickCount < 60){
				csf.out = false;
				csf.dist = tickCount;
			}else{
				csf.out = true;
				csf.dist = tickCount - 60;
			}
			
			core.setCutsceneFilter(csf);
		}else
		if(tickCount > warpTime){
			if(tickCount < warpTime + 60){
				csf.out = false;
				csf.dist = tickCount - warpTime;
				core.setCutsceneFilter(csf);
			}else if(tickCount < warpTime + 2*60){
				csf.out = true;
				csf.dist = tickCount - warpTime - 60;
				core.setCutsceneFilter(csf);
			}else{
				core.setCutsceneFilter(Filter.NO_FILTER);
			}
		}else{
			core.setCutsceneFilter(Filter.NO_FILTER);
		}
		
		super.tick();
	}

	@Override
	public boolean isFinished() {
		return tickCount > warpTime + 4*60 && !pa.showDialog;
	}
	
	public void close(){
		super.close();
		pl.isPartOfCutscene = false;
		pa.isPartOfCutscene = false;
		pa.setMode(Villager.MODE_RELAXING, Villager.IMPORTANT_NOT, null);
	}

}
