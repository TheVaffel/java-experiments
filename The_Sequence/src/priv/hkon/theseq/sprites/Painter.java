package priv.hkon.theseq.sprites;

import java.awt.event.KeyEvent;

import priv.hkon.theseq.cutscenes.NextPainterPuzzleCutscene;
import priv.hkon.theseq.cutscenes.PainterGuidesPlayerCutscene;
import priv.hkon.theseq.cutscenes.PlayerFinishedPainterChallengeCutscene;
import priv.hkon.theseq.main.Controller;
import priv.hkon.theseq.misc.Sentence;
import priv.hkon.theseq.misc.VillageEvent;
import priv.hkon.theseq.structures.Atelier;
import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Tile;
import priv.hkon.theseq.world.Village;

public class Painter extends Villager {

	private static final long serialVersionUID = -1407944991090206701L;
	static boolean presented = false;
	
	public static final int EVENT_WAIT_TO_GUIDE_PLAYER = 0;
	
	boolean waitForPlayer = false;
	
	public boolean isInChallengeMode = false;
	
	public int puzzleNumber= 0;
	int timeSinceChange = 0;
	
	public static final int MODE_AWAIT_PUZZLE_FINISH = 10000;
	
	public static final int[][][] PAINTER_CHALLENGES = {
			{
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5},
			},{
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5},
				{7, 7, 8, 7, 7},
				{5, 5, 6, 5, 5},
				{5, 5, 6, 5, 5}
			},{
				{5, 5, 5, 5, 5},
				{5, 5, 5, 5, 5},
				{5, 5, 7, 5, 5},
				{5, 5, 5, 5, 5},
				{5, 5, 5, 5, 5}
			}
	};
	
	Atelier atelier;

	public Painter(int x, int y, Village v, House h, int i) {
		super(x, y, v, h, i);
		
		atelier = new Atelier(200, 200, 17 , 15, v);
		village.addBuilding(atelier);
	}
	
	public void makeAnimationFrames(){
		super.makeAnimationFrames();
		for(int u = 0; u < numAnimations; u++){
			for(int k = 0; k < animationFrames[u].length; k++){
				for(int i = 0; i < 3; i++){
					for(int j = 0;j < W; j++){
						if(Math.abs(j - W/2) < (i+0.5)*3){
							animationFrames[u][k][i][j] = Sprite.getColor(180, 0, 180);
						}
					}
				}
			}
		}
	}

	@Override
	public String[] getMeaningOfLife() {
		return new String[] {"Art, my friend",
				"The pure satisfaction of colors melting together",
				"Forming a outmost comfortable juice of senses",
				"Just for the sake of living, or dreaming",
				"Art, my friend"
		};
	}

	@Override
	public String[] getPresentation() {
		return new String[] {"I am the painter!",
			"I am the lord of colors!",
			"The composer of graphical music!",
			"The conjurer of your inner soul!",
			"Anyway, I try to liven up this place.",
			"No need for there not to be colors, right?",
			"Nonono, let me tell you one important thing",
			"Colors are beautiful",
			"Colors are souls",
			"Colors are life",
			"That is truly what life is all about"
		};
	}
	
	public boolean tick(){
		int px = village.getPlayer().getX();
		int py = village.getPlayer().getY();
		if(home.contains(px, py) &&home.isClosedAtGlobal(px, py) && village.woodcutterQuestCompleted && 
				!village.painterQuestCompleted&& !waitForPlayer && !home.contains(x, y)){
			village.moveSpriteTo(this, home.getX() + home.getW()/2, home.getY() + home.getH() + 3);
			hasPath = false;
			waitForPlayer = true;
			setToWaitMode(new VillageEvent(village, this, EVENT_WAIT_TO_GUIDE_PLAYER){
				public boolean isHappening(){
					return subject.distTo(village.getPlayer()) <= 3;
				}
			}, IMPORTANT_VERY);
			return true;
		}
		
		
		
		switch(targetMode){
			case MODE_AWAIT_PUZZLE_FINISH: {
				timeSinceChange++;
				if(isAtelierCopy() && ! isPartOfCutscene){
					
					if(puzzleNumber == Painter.PAINTER_CHALLENGES.length){
						village.setCutscene(new PlayerFinishedPainterChallengeCutscene(this, village.getPlayer(), village.getCore()));
						village.painterQuestCompleted = true;
						timeSinceChange = 0;
					}else{
						timeSinceChange = 0;
						village.setCutscene(new NextPainterPuzzleCutscene(this, village.getPlayer() , village.getCore()));
						//applyChallengePattern(puzzleNumber);
						showDialog(Sentence.RESPONSES[Sentence.RESPONSE_APPLAUDE][RAND.nextInt(Sentence.RESPONSES[Sentence.RESPONSE_APPLAUDE].length)], 3*60);
					}
				}
				if(!isPartOfCutscene){
					if(timeSinceChange >= 60*20 && timeSinceChange % 60*30 == 60*20){
						showDialog("Press your inner R to reset your soul", 60*3);
					}
					if(Controller.input[KeyEvent.VK_R]){
						puzzleNumber--;
						timeSinceChange = 0;
						village.setCutscene(new NextPainterPuzzleCutscene(this, village.getPlayer() , village.getCore()));
					}
				}
			}
		}
		if(super.tick()){
			return true;
		}
		
		return false;
	}
	
	public void processEvent(VillageEvent ve){
		if(ve.getID() == Painter.EVENT_WAIT_TO_GUIDE_PLAYER){
			village.setCutscene(new PainterGuidesPlayerCutscene(this, village.player, village.getCore()));
		}
	}


	@Override
	public boolean classHasPresented() {
		return  presented;
	}

	@Override
	public boolean subclassSpeechInterrupted() {
		return false;
	}

	@Override
	public boolean subclassSpeechFinished() {
		return false;
	}
	
	public Atelier getAtelier(){
		return atelier;
	}
	
	public void applyChallengePattern(int u){
		
		for(int i = atelier.getY() + atelier.getCanvasY(); i < atelier.getY() + atelier.getCanvasY() + 5; i++){
			for(int j = atelier.getX() + atelier.getCanvasX(); j< atelier.getCanvasX() + atelier.getX() + 5; j++){
				village.setTileAt(Painter.PAINTER_CHALLENGES[u][i - atelier.getY() - atelier.getCanvasY()][j - atelier.getX() - atelier.getCanvasX()], j, i);
			}
		}
		for(int i = atelier.getY() + atelier.getCanvasY(); i < atelier.getY() + atelier.getCanvasY() + 5; i++){
			for(int j = atelier.getX() + atelier.getCanvasX() + 6; j< atelier.getCanvasX() + atelier.getX() + 11; j++){
				village.setTileAt(Tile.TYPE_CANVAS_WHITE, j, i);
			}
		}
		puzzleNumber++;
		//System.out.println("Puzzlenumber is now " + puzzleNumber);
	}
	
	public boolean isAtelierCopy(){
		for(int i = atelier.getY() + atelier.getCanvasY(); i < atelier.getY() + atelier.getCanvasY() + 5; i++){
			for(int j = atelier.getX() + atelier.getCanvasX(); j< atelier.getCanvasX() + atelier.getX() + 5; j++){
				if(village.getTileAt(j, i) != village.getTileAt(j + 6, i)){
					return false;
				}
			}
		}
		return true;
	}

}
