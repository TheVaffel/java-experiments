package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.DarkenFilter;
import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;

public class OpeningScene extends Cutscene {
	
	Player player;
	Villager prophet;
	
	DarkenFilter darkenFilter;
	
	boolean finished = false;

	public OpeningScene(Player player, Villager prophet, Core core) {
		super(core);
		this.player = player;
		this.prophet = prophet;
		
		player.isPartOfCutscene = true;
		prophet.isPartOfCutscene = true;
		darkenFilter = new DarkenFilter(0);
		
		int px = player.getX();
		int py = player.getY();
		
		happenings.add(new Happening(prophet, 0){
			public void happen(){
				((Movable)(sprite)).startPathTo(px, py - 2);
			}
		});
		
		
		happenings.add(new Happening(prophet, 120){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Well, hello there!", 90);
			}
		});
		
		happenings.add(new Happening(player, 120){
			public void happen(){
				((Movable)(sprite)).turnTowards(Movable.UP);
			}
		});
		
		happenings.add(new Happening(prophet, 240){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Can't say I've seen your face here before!", 150);
			}
		});
		
		happenings.add(new Happening(prophet, 420){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Who are you?", 120);
			}
		});
		
		happenings.add(new Happening(prophet, 600){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("...", 60);
			}
		});
		
		happenings.add(new Happening(prophet, 720){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("By the look on your face," ,120);
			}
		});
		
		happenings.add(new Happening(prophet, 840){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("I guess you are just as confused as I am", 120);
			}
		});
		
		happenings.add(new Happening(prophet, 1020){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Don't worry, we'll find out soon enough", 180);
			}
		});
		
		happenings.add(new Happening(prophet, 1260){
			public void happen(){
				((TalkativeSprite)sprite).showDialog("Come with me!", 180);
			}
		});
		
		
	}

	@Override
	public void tick() {
		if(tickCount < 120){
			darkenFilter.darkness = 255 - 2*tickCount;
			core.setCutsceneFilter(darkenFilter);
		}else if(tickCount == 120){
			core.setCutsceneFilter(Filter.NO_FILTER);
			
		}else if(tickCount == 1320){
			finished = true;
			
			player.isPartOfCutscene = false;
			prophet.isPartOfCutscene = false;
		}
		super.tick();
		tickCount++;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	public void close(){
		core.setCutsceneFilter(Filter.NO_FILTER);
	}
}
