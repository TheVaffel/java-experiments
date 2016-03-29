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
				120,
				120,
				120,
				180,
				120,
				120
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

}
