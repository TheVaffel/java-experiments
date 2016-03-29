package priv.hkon.theseq.sprites;

import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Village;

public class Mayor extends Villager {
	
	private static final long serialVersionUID = -1375679852859624804L;

	
	static boolean presented = false;

	public Mayor(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		targetMode = MODE_WORKING;
	}
	
	@Override
	public void makeAnimationFrames(){
		super.makeAnimationFrames();
		for(int k = 0; k < numAnimations; k++){
			for(int u = 0; u < animationFrames[k].length; u++){
				for(int i = 0; i < HEAD_OFFSET_Y- 3; i++){
					for(int j = W/4; j < 3*W/4; j++){
						animationFrames[k][u][i][j] = Sprite.getColor(20, 20,20);
					}
				}
				for(int j = 0; j < W; j++){
					animationFrames[k][u][HEAD_OFFSET_Y - 3][j] = Sprite.getColor(20, 20, 20);
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
		
		if(targetSprite == null){
			Citizen c;
			if((c = findCitizen()) != null){
				if(c == lastVictim){
					return;
				}
				
				hasPath = false;
				lastVictim = c;
				
				engageConversation(c, IMPORTANT_VERY);
			}else if(!hasPath){
				strollTownGrid();
			}
		}
		return;
	}
	
	public String getName(){
		return "The Mayor";
	}

	@Override
	public String[] getMeaningOfLife() {
		return new String[] {"What?",
			"How would I know?",
			"I am the Mayor!"
		};
	}

	@Override
	public String[] getPresentation() {
		return new String[]{
				"Hello!",
				"I am the Mayor!",
				"I am the chief of this town",
				"The boss!",
				"The president!",
				"The legally responsible!",
				"And it's hard work, I tell you",
				"Yup.. I should probably get going..",
				"..Working hard, you know"
		};
	}

	@Override
	public Integer[] getPresentationDurations() {
		return new Integer[] {
			60,
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
