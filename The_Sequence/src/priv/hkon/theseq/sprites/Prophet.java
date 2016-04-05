package priv.hkon.theseq.sprites;

import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Village;

public class Prophet extends Villager {

	private static final long serialVersionUID = -4204665935086265739L;
	
	static boolean presented = false;
	
	public final String[] INTRODUCTION = {
			"I wish you welcome to the Village!",
			"We humens have lived here as long as our oldest,",
			"-that would be me-",
			"can remember.",
			"We are a simple community",
			"No need to hurry",
			"No need to be afraid",
			"We do our business day in, day out",
			"It is a simple life",
			"Now, where did you come from?",
			"",
			"",
			"Huh. You really don't say much..",
			"Lots of secrets, haven't you?",
			"Doesn't matter",
			"If you want a place to stay, I have good news!",
			"House " + village.getTownGridSide() + "" + village.getTownGridSide() + " is free!",
			"That is, the house most south-east in the village",
			"It has been empty since...",
			"",
			".. Since the last owner moved out",
			"I live in House " + (village.villagerPermutation[citizenNumber] + 1)%village.getTownGridSide() + "" + (village.villagerPermutation[citizenNumber] + 1)/village.getTownGridSide(),
			"But you will probably find me somewhere in the town at daytime.",
			"You should try to get to know the other villagers!",
			"And go to house " + village.getTownGridSide() + "" + village.getTownGridSide() + " when you are tired",
			"Remember - the south-east-most house",
			"I hope you will have a pleasant stay!"
	};
	
	public static final Integer[] INTRODUCTION_DURATIONS = {
		120, 120, 90, 90, 60, 120
	};
	
	public static final String[] MEANING_OF_LIFE = {
		"The answer would surprise you.", "Be patient, it will probably be revealed soon enough.",
		"You ask as if there is an answer.", "I like potatoes."
	};

	public Prophet(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		moveSpeed/= 1.5;
	}
	
	public boolean tick(){
		if(super.tick()){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void makeAnimationFrames(){
		super.makeAnimationFrames();
		for(int u = 0; u < animationFrames[0].length; u++){
			for(int i = 9; i < 12; i++){
				for(int j = 0; j < W ; j++){
					if(Math.abs(j - W/2) + i <12){
						animationFrames[DOWN][u][i][j] = Sprite.getColor(20, 20, 20);
					}
					
					if(j == 2*W/3 + 1){
						animationFrames[RIGHT][u][i][j] = Sprite.getColor(20, 20, 20);
						if(i < 9){
							animationFrames[RIGHT][u][i][j - 1] = Sprite.getColor(20, 20, 20);
						}
					}
					
					if(j == W/3){
						animationFrames[LEFT][u][i][j] = Sprite.getColor(20, 20, 20);
						if(i < 9){
							animationFrames[LEFT][u][i][j + 1] = Sprite.getColor(20, 20, 20);
						}
					}
				}
			}
		}
	}
	
	public void work(){
		super.work();
	}
	
	public String[] getMeaningOfLife(){
		return new String[] {MEANING_OF_LIFE[RAND.nextInt(MEANING_OF_LIFE.length)]};
	}

	@Override
	public String[] getPresentation() {//TODO: Expand Prophet's presentation
		return new String[] {"Ah, I really should present myself",
				"I am the Prophet",
				"I do the thinking",
				"Not to be harsh on the other villagers...",
				"But it really doesn't seem that anyone cares about their lives here.."
		};
	}

	@Override
	public Integer[] getPresentationDurations() {
		return new Integer[] {
			120,
			120,
			120,
			120,
			180
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

	public String getName(){
		return "The Prophet";
	}
}
