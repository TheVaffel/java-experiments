package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.ColoredSquaresFilter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.main.Screen;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Painter;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;
import priv.hkon.theseq.structures.Atelier;

public class PainterGuidesPlayerCutscene extends Cutscene {

	Painter pa;
	Player pl;
	
	ColoredSquaresFilter csf = new ColoredSquaresFilter(0, Screen.W, Screen.H);
	
	int warpTime = (int) 10000;
	
	public PainterGuidesPlayerCutscene(Painter p1, Player p2, Core core) {
		super(core);
		pa = p1;
		pl = p2;
		
		pl.isPartOfCutscene = true;
		pa.isPartOfCutscene = true;
		
		Atelier at = pa.getAtelier();
		
		happenings.add(new Happening(pa, 0){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Hey!!", 2*60);
				((Movable)sprite).turnTowards(Movable.UP);
			}
		});
		
		happenings.add(new Happening(pa, 2*60){
			public void happen(){
				((TalkativeSprite)sprite).addSentence(new String[]{
					"What excactly do you think you're doing?",
					"This place is sacred, I tell you, SACRED!!",
					"I don't let anybody into my palace of colors!",
					"Should probably lock my door, then...",
					"",
					"Probably",
					"But, hey, do you like art?",
					"Do you feel the intense sensation of the rainbow?",
					"",
					"You don't need to look so shocked.",
					"I am not irrational, despite what others think of me",
					"I don't pretend that life is about trees or cutting them",
					"YOU HEAR ME??",
					"I AM A NORMAL HUMEN BEING!",
					"",
					"Oh, anyways.. The colors lured you into my house, yes?",
					"Then I'm sure you will love this!"
				});
				((TalkativeSprite)sprite).showDialog = true;
			}
		});
		
		happenings.add(new Happening(pa, warpTime + 60){
			public void happen(){
				core.village.moveSpriteTo(sprite, at.getX() + 1, at.getY() + at.getH()/2);
			}
		});
		
		happenings.add(new Happening(pl, warpTime + 60){
			public void happen(){
				core.village.moveSpriteTo(sprite, at.getX() + 2, at.getY() + at.getH()/2);
			}
		});
		
		happenings.add(new Happening(pa, warpTime + 2*60){
			public void happen(){
				((Movable)sprite).turnTowards(Movable.RIGHT);
				((TalkativeSprite)sprite).addSentence(new String[]{
					"Here! My personal Atelier!",
					"This is where most of the magic happens!",
					"Let's play a game! Copycat!",
					"First, I will draw something",
					"I will only cover the left half of the floor.",
					"Then you copy it!",
					"At the end, I want to see two equal images!",
					"Now, what do you say?",
					"Sounds like a cool challenge?",
					"Then, let's go!"
				});
				((TalkativeSprite)sprite).showDialog = true;
			}
		});
		
		happenings.add(new Happening(pl, warpTime + 2*60){
			public void happen(){
				((Movable)sprite).turnTowards(Movable.LEFT);
				((Player)sprite).carryColor = 0;
				
			}
		});
	}
	
	public void tick(){
		if(!pa.showDialog && tickCount > 2*60 && tickCount < warpTime){
			tickCount = warpTime;
		}
		if(tickCount > warpTime && tickCount < warpTime + 60){
			csf.dist = tickCount - warpTime;
			core.setCutsceneFilter(csf);
		}else if(tickCount > warpTime && tickCount < warpTime + 2*60){
			csf.out = true;
			csf.dist = tickCount - warpTime - 60;
			core.setCutsceneFilter(csf);
		}
		super.tick();
	}

	@Override
	public boolean isFinished() {
		return tickCount > warpTime + 60*3 && !pa.showDialog;
	}
	
	public void close(){
		super.close();
		pl.isPartOfCutscene = false;
		pa.isPartOfCutscene = false;
		pa.applyChallengePattern(0);
		pa.setMode(Painter.MODE_AWAIT_PUZZLE_FINISH, Villager.IMPORTANT_VERY, pl);
	}

}
