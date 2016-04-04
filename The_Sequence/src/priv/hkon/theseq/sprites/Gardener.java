package priv.hkon.theseq.sprites;

import priv.hkon.theseq.blocks.Tree;
import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Gardener extends Villager {

	private static final long serialVersionUID = 5261540125190585088L;

	boolean isPlanting = false;
	
	public static final String[] TREE_ECSTASY_QUOTES = {"Yay! More trees!",
			"I sure love trees!", "Trees for life!" , "Trees!! <3", "A tree a day keeps the doctor away!"
	};
	
	static boolean presented = false;

	public Gardener(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		targetMode  = MODE_WORKING;
	}
	
	@Override
	public void makeAnimationFrames(){
		super.makeAnimationFrames();
		int si = (int)(2*Math.sqrt(HEAD_SQ_RADIUS));
		for(int k = 0; k < numAnimations; k++){
			for(int u = 0; u < animationFrames[k].length; u++){
				for(int i = si + 1; i <si + 6; i++){
					for(int j = W/2 - (i - si); j< W/2 + (i - si) + 1; j++){
						animationFrames[k][u][i][j] = Sprite.getColor(0, 128, 0);
					}
				}
			}
		}
	}
	
	public boolean tick(){
		if(super.tick()){
			return true;
		}
		
		return false;
	}
	
	public void work(){

		if(isPlanting){
			plantPlant();
			return;
		}
		
		if(RAND.nextInt(5000) == 0){//Be careful, or we might get a lot of vegetation!
			for(int i = 0; i < fovDeltaCoordinates[movingDirection].length; i++){
				int nx = x + fovDeltaCoordinates[movingDirection][i][0];
				int ny = y + fovDeltaCoordinates[movingDirection][i][1];
				if(village.getTileAt(nx, ny) == Tile.TYPE_GRASS
						&& village.getSpriteAt(nx, ny) == null
						&& village.ownedBy(nx, ny) == null){
					pauseMode();
					isPlanting = true;
					modeTargetX = nx;
					modeTargetY = ny;
					setDialogString(getRandomDialogString(MODE_THINKING));
					showDialog(60);
					timeToWait = 50;
					
					return;
				}
			}
		}
		super.work();
	}
	
	public void plantPlant(){
		if(moving){
			return;
		}
		if( distTo(modeTargetX, modeTargetY)== 1){
			turnTowards(getDirectionTo(modeTargetX, modeTargetY));
			if(village.getSpriteAt(modeTargetX, modeTargetY) == null){
				Tree t = new Tree(modeTargetX, modeTargetY, village);
				village.addSprite(t);
				isPlanting = false;
				setDialogString(TREE_ECSTASY_QUOTES[RAND.nextInt(TREE_ECSTASY_QUOTES.length)]);
				showDialog(2*60);
				timeToWait = 1*60;
				return;
			}
			if(village.getSpriteAt(modeTargetX, modeTargetY) instanceof Movable){
				return;
			}else{
				isPlanting = false;
				return;
			}
			
		}else{
			if(hasPath && Sprite.distBetween(pathTargetX, pathTargetY, modeTargetX, modeTargetY) == 1){
				return;
			}else{
				int d = (getDirectionTo(modeTargetX, modeTargetY) + 2) % 4;
				for( int i = 0; i < 4; i++){
					if(village.getSpriteAt(modeTargetX + dx[(d + i)%4], modeTargetY + dy[(d+i)%4]) == null){
						startPathTo(modeTargetX + dx[(d+ i)% 4], modeTargetY + dy[(d + i)%4]);
						return;
					}
				}
			}
		}
	}
	
	public String getName(){
		return "The Gardener";
	}

	@Override
	public String[] getMeaningOfLife() {
		return new String[] {"Well, isn't that obvious?",
			"Trees, my friend!",
			"Trees and trees and trees!!"
		};
	}

	@Override
	public String[] getPresentation() {
		return new String[] {"Hello!",
				"I am a Gardener",
				"I.. I like trees",
				"I.. I mean",
				"REALLY like trees",
				"How can one not?",
				"Their fluffy greenness",
				"And green fluffyness!",
				"If we could all be more like trees...",
				"",
				"That would be AWESOME!!"
		};
	}

	@Override
	public Integer[] getPresentationDurations() {
		return new Integer[]{
				120,
				120,
				120,
				120,
				120,
				120,
				120,
				120,
				120,
				120
		};
	}

	@Override
	public boolean classHasPresented() {
		return presented;
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

}
