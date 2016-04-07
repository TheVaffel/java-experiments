package priv.hkon.theseq.cutscenes;

import priv.hkon.theseq.filters.Filter;
import priv.hkon.theseq.filters.RedSnowFilter;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.misc.VillageEvent;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.Shadow;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;
import priv.hkon.theseq.sprites.Woodcutter;

public class PlayerTriesCompleteWoodcutterRequest extends Cutscene {
	
	Player player;
	Woodcutter woodcutter;
	Shadow shadow;
	
	RedSnowFilter snow;
	
	boolean showShadow = false;

	public PlayerTriesCompleteWoodcutterRequest(Woodcutter wc, Player p, Core core) {
		super(core);
		
		player = p;
		woodcutter = wc;
		player.isPartOfCutscene = true;
		wc.isPartOfCutscene = true;
		shadow = core.village.getShadow();
		
		snow = new RedSnowFilter();
		
		if(woodcutter.storage.getCrate().numItems() >= 10){
			if(core.village.timesSeenShadow == 0){
				showShadow = true;
				core.village.timesSeenShadow++;
				shadow.setX(woodcutter.cabin.getX() + woodcutter.cabin.getW()/2);
				shadow.setY(woodcutter.cabin.getY() + woodcutter.cabin.getH()/2);
				core.village.setSpriteAt(shadow, woodcutter.cabin.getX() + woodcutter.cabin.getW()/2,
						woodcutter.cabin.getY() + woodcutter.cabin.getH()/2);
				happenings.add(new Happening(player, 2*60){
					public void happen(){
						((Movable)sprite).startPathTo(sprite.getX(), sprite.getY() + 2);
					}
				});
				happenings.add(new Happening(shadow, 3*60){
					public void happen(){
						core.village.setSpriteAt(null, shadow.getX(), shadow.getY());
					}
				});
				return;
			}else{
				happenings.add(new Happening(player, 0){
					public void happen(){
						((Movable)sprite).startPathTo(sprite.getX(), sprite.getY() - 1);
					}
				});
				happenings.add(new Happening(woodcutter, 2*60){
					public void happen(){
						((Movable)sprite).turnTowards(woodcutter.getDirectionTo(player));
					}
				});
				
				happenings.add(new Happening(woodcutter, 2*60){
					public void happen(){
						((TalkativeSprite)sprite).addAndShowSentence(new String[] {
							"So you're done?",
							"Ok, then, I'll let you free",
							"Hum. You look a little shaken",
							"Feeling allright?",
							"",
							"Oh, anyway..",
							"You did well!",
							"Don't be surprised if I ask you again"
						});
					}
				});
				
				happenings.add(new Happening(player, new VillageEvent(core.village, woodcutter){
					public boolean isHappening(){
						return !((TalkativeSprite)subject).showDialog;
					}
				}){
					public void happen(){
						((Movable)sprite).startPathTo(sprite.getX(), woodcutter.cabin.getY() + woodcutter.cabin.getH() + 1);
					}
				});
				
				happenings.add(new Happening(woodcutter, new VillageEvent(core.village, woodcutter){
					public boolean isHappening(){
						return !((TalkativeSprite)subject).showDialog;
					}
				}){
					public void happen(){
						((Villager)sprite).setMode(Villager.MODE_RELAXING, Villager.IMPORTANT_NOT, null);
						((Villager)sprite).lastVictim = player;
						core.village.woodcutterQuestCompleted = true;
					}
				});
				return;
			}
		}
		
		happenings.add(new Happening(player, 0){
			public void happen(){
				((Movable)sprite).startPathTo(sprite.getX(), sprite.getY() - 3);
			}
		});
		
		happenings.add(new Happening(woodcutter, 2*60){
			public void happen(){
				((Movable)sprite).turnTowards(woodcutter.getDirectionTo(player));
			}
		});
		
		happenings.add(new Happening(woodcutter, 2*60){
			public void happen(){
				((TalkativeSprite)sprite).addAndShowSentence(new String[] {
					"So, you're done?",
					"NO, YOU'RE NOT!",
					"Sorry.. I will need more wood before you go",
					"Just find wood around the place, ",
					"I am sure there is plenty of it",
					"10 should suffice. Just put them in the storage crate",
					"Now, off you go"
				});
			}
		});
		
		happenings.add(new Happening(woodcutter, new VillageEvent(core.village, woodcutter){
			public boolean isHappening(){
				return !((TalkativeSprite)subject).showDialog;
			}
		}){
			public void happen(){
				((Movable)sprite).turnTowards(Movable.UP);
			}
		});
		
		happenings.add(new Happening(player, new VillageEvent(core.village, woodcutter){
			public boolean isHappening(){
				return !((TalkativeSprite)subject).showDialog;
			}
		}){
			public void happen(){
				((Movable)sprite).startPathTo(sprite.getX(), sprite.getY() + 5);
			}
		});
		
		
	}
	public void tick(){
		if(showShadow){
			core.setCutsceneFilter(snow);
			if(!core.wavIsPlaying()){
				core.playWavSoundDampened(Core.WAV_NOISE);
			}
		}else{
			core.setCutsceneFilter(Filter.NO_FILTER);
		}
		super.tick();
	}

	@Override
	public boolean isFinished() {
		//System.out.println(core.village.ownedBy(player.getX(), player.getY()));
		return core.village.ownedBy(player.getX(), player.getY()) == null;
	}
	
	public void close(){
		super.close();
		core.stopWavSound();
		core.village.setSpriteAt(null, shadow.getX(), shadow.getY());
		
		player.isPartOfCutscene = false;
		woodcutter.isPartOfCutscene = false;
	}

}
