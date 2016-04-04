package priv.hkon.theseq.sprites;

import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Village;

public class Woodcutter extends Villager {

	private static final long serialVersionUID = 8408691764399000623L;

	static boolean presented = false;	
	
	public Woodcutter(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
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
				"Oh well, I certainly hope to see you around!"
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

}
