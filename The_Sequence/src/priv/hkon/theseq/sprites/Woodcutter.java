package priv.hkon.theseq.sprites;

import priv.hkon.theseq.cutscenes.PlayerTriesCompleteWoodcutterRequest;
import priv.hkon.theseq.cutscenes.WoodCutterGuidesPlayerCutscene;
import priv.hkon.theseq.misc.VillageEvent;
import priv.hkon.theseq.structures.Building;
import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.structures.WoodcutterHouse;
import priv.hkon.theseq.structures.WoodcutterStorage;
import priv.hkon.theseq.world.Village;

public class Woodcutter extends Villager {

	private static final long serialVersionUID = 8408691764399000623L;

	public static boolean presented = false;	
	public boolean hasGuidedPlayer = false;
	
	public House cabin;
	public WoodcutterStorage storage;
	
	public static final int EVENT_WAIT_TO_GUIDE_PLAYER = 0;
	
	public static final int MODE_WAIT_FOR_PLAYER_FILLING_CRATE = 1000;
	
	public Woodcutter(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		cabin = new WoodcutterHouse(village.getTownMiddleX() + 100, village.getTownMiddleY(), 7, 7, village);
		village.addBuilding(cabin);
		village.setSpriteAt(null, cabin.getX() + cabin.getW()/2, cabin.getY() + cabin.getH() + 1);
		storage = new WoodcutterStorage(cabin.getX() + cabin.getW() + 2, cabin.getY(), 4, 4, village);
		village.addBuilding(storage);
		village.setSpriteAt(null, storage.getX() + storage.getW()/2, storage.getY() + storage.getH() + 1);
		village.createAreaAroundWoodcutterCabin(cabin);
	}

	@Override
	public String[] getMeaningOfLife() {
		return new String[]{
				"Uhm.. ",
				"I am a woodcutter, so..",
				"Woodcutting?"
		};
	}

	@Override
	public String[] getPresentation() {
		return new String[]{
				"I am a Woodcutter!",
				"My purpose in this harsh, unfair life",
				"Is to murder green vegetation!",
				"To keep us humens warm, that is...",
				"Perhaps not a very charming description of my profession",
				"But it is a bit sexy, what I'm doing, eyh?",
				"Oh well, there is business to attend!"
		};
	}

	@Override
	public Integer[] getPresentationDurations() {
		return new Integer[] {
				120,
				180,
				180,
				180,
				180,
				180,
				180
		};
	}

	@Override
	public boolean subclassSpeechInterrupted() {
		return false;
	}

	@Override
	public boolean subclassSpeechFinished() {
		if(modeParameter == SPEECH_PRESENTING){
			presented = true;
		}
		return false;
	}

	@Override
	public boolean classHasPresented() {
		return presented;
	}
	
	@Override
	public void makeAnimationFrames(){
		super.makeAnimationFrames();
		int mustart = (int)Math.sqrt(HEAD_SQ_RADIUS) + 2;
		
		for(int u = 0; u < animationFrames[0].length; u++){
			for(int i = mustart; i < mustart + 2; i++){
				for(int j = 0; j < W ; j++){
					if(Math.abs(W/2 - j) < (i - mustart + 1)*1.5){
						animationFrames[DOWN][u][i][j] = Sprite.getColor(200, 50, 0);
					}
					if(j < (i - mustart)+ 1){
						animationFrames[LEFT][u][i][j + 4] = Sprite.getColor(200, 50, 0);
					}
					if(j > W - 1 -(i - mustart) - 1){
						animationFrames[RIGHT][u][i][j - 3] = Sprite.getColor(200, 50, 0);
					}
				}
			}
		}
	}
	
	public boolean tick(){
		
		if(village.getTimesSlept() >= 1 && !hasGuidedPlayer){
			//System.out.println("Happened!");
			Building h = village.ownedBy(village.getPlayer().getX(), village.getPlayer().getY());
			village.moveSpriteTo(this , h.getX() + village.getHouseSide()/2, h.getY() + village.getHouseSide() + 3);
			hasPath = false;
			hasGuidedPlayer = true;
			setToWaitMode(new VillageEvent(village, this, EVENT_WAIT_TO_GUIDE_PLAYER){
				public boolean isHappening(){
					return subject.distTo(village.getPlayer()) <= 3;
				}
			}, IMPORTANT_VERY);
			return true;
		}
		//System.out.println("Checking..");
		if(super.tick()){
			return true;
		}
		
		
		switch(targetMode){
		case Woodcutter.MODE_WAIT_FOR_PLAYER_FILLING_CRATE:{
				if(!hasPath&& !cabin.contains(x, y)){
					startPathTo(cabin.getX() + cabin.getW()/2, cabin.getY() + 1);
				}
				if(cabin.contains(x, y)){
					int px = village.getPlayer().getX();
					int py = village.getPlayer().getY();
					if( village.ownedBy(px, py) == cabin){
						village.setCutscene(new PlayerTriesCompleteWoodcutterRequest(this, village.getPlayer(), village.getCore()));
					}
					break;
				}
			}
		}
		
		return false;
	}
	
	public void processEvent(VillageEvent ev){
		switch(ev.getID()){
		case Woodcutter.EVENT_WAIT_TO_GUIDE_PLAYER: {
			//System.out.println("Starting guiding " + hasGuidedPlayer);
			village.setCutscene(new WoodCutterGuidesPlayerCutscene(this, village.getPlayer(), village.getCore()));
		}
		}
	}
}
