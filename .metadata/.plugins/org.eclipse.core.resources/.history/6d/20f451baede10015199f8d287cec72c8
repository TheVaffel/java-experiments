package priv.hkon.theseq.sprites;

import priv.hkon.theseq.world.House;
import priv.hkon.theseq.world.Village;

public class Villager extends Citizen{
	
	House home;
	int timeSinceHome;
	int citizenNumber;

	int timeToWait= 0;
	
	public static final int HEAD_OFFSET_Y = 5;
	public static final int HEAD_OFFSET_X = W/2;
	
	public static final int RANGE_OF_VIEW = 5;
	public static final float TAN_FOV = 1.0f;
	
	
	int targetMode;
	int modeImportance;
	Sprite targetSprite;
	
	boolean hasPausedMode = false;
	int pausedMode;
	int timePaused = 0;
	int pausedModeImportance;
	Sprite pausedSprite;
	
	boolean hasCalled = false;
	
	float[] affections; //Numbers from -1 to 1 which tells how much this citizen likes the others.
	
	public static final int CONDITION_ANGER = 0;
	public static final int CONDITION_FEAR = 1;
	
	float[] conditions = new float[]{0.0f, 0.0f};
	
	public static final int MODE_RELAX_AT_HOME = 0;
	public static final int MODE_CHASE_OUT_OF_HOUSE = 1;
	public static final int MODE_SURPRISED = 2;
	public static final int MODE_CURIOUS = 3;
	public static final int MODE_TALKING = 4;
	public static final int MODE_WORKING = 5;
	
	public static final int IMPORTANT_NOT = 0;
	public static final int IMPORTANT_LITTLE = 1;
	public static final int IMPORTANT_MEDIUM  = 2;
	public static final int IMPORTANT_VERY = 3;
	
	public static final String[][] DIALOG_STRINGS = {{"Dum di dum di dum", "La la-laaah lah!"}, 
			{"Hey, get out!", "You're not welcome here!", "Eyh, you should leave now"},
			{"Hey...", "What the...","Uhm..","!!"},
			{"Hum?", "?", "!"}
	};
	
	public static final int QUESTION_SELF_CONDITION = 0;
	public static final int QUESTION_RELATION_TO_ASKER = 1;
	public static final int QUESTION_MEANING_OF_LIFE = 2;
	
	public static final String[][] GENERAL_QUESTIONS = {
			{"How are you?", "How is your life by now?", "Are you having a good time?"},
			{"Honestly, what do you think of me?", "How am I in your eyes?" , "If you were to choose between a night with me or a day with Hitler, what would you do?"},
			{"What is the meaning of our existence anyway?", "Is there more out there, or is this village all the world has got?", "WHY ARE WE HERE?"}
	};
	
	public int expectingVisit = -1;
	public boolean expectingSeveralVisits = false;
	
	int coatColor = Sprite.getColor(40, 60, 80);
	
	public static final int HEAD_SQ_RADIUS = W*W/9 - 1;
	
	//TODO: add some personality!

	public Villager(int x, int y, Village v, House h, int i) {
		super(x, y, v, i);
		home = h;
		affections = new float[village.getNumCitizens()];
		for(int j = 0; j < affections.length; j++){
			affections[j] = .0f + RAND.nextFloat()*.20f;
		}
	}

	@Override
	public void makeData() {
		byte[] b = new byte[3];
		RAND.nextBytes(b);
		for(int i = 0; i < 3; i++){
			b[i] = (byte) (Math.abs(b[i]));
		}
		//coatColor = Sprite.getColor(b[0], b[1], b[2]);
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int d = (j-W/2)*(j-W/2) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y);
				int c = 255;
				if(Math.abs(j - W/2) < ((float)i - HEAD_OFFSET_Y)/3){
					int p = (int) (30*Math.exp(-0.5*(int)Math.abs((j - W/2 - (float)(i-HEAD_OFFSET_Y)/5))));
					data[i][j] = Sprite.getColor( b[0] + p, b[1] + p, b[2] + p);
				}
				if(d < HEAD_SQ_RADIUS){
					c -= Math.sqrt(d)*15;
					data[i][j] = Sprite.getColor(c, c, c);
				}
			}
		}
	}
	
	public boolean isHome(){
		return home.contains(x, y);
	}
	
	public boolean tick(){
		if(!isInsideHome()){
			timeSinceHome++;
		}
		if(timeToWait >0){
			timeToWait--;
			return false;
		}
		
		
		
		data = animationFrames[movingDirection][0];
		
		if(conversation != null&& targetMode != MODE_TALKING){
			pauseMode();
			targetMode = MODE_TALKING;
			targetSprite = conversation.getOwner();
			hasPath = false;
		}

		if(hasPausedMode){
			timePaused++;
		}
		
		switch(targetMode){
			case MODE_RELAX_AT_HOME: 
				if(!isInsideHome()){
					if(hasPath&&home.isClosedAtGlobal(targetX, targetY))
						break;
					startGoHome();
				}else{
					relaxAtHome();
				}
				break;
			case MODE_CHASE_OUT_OF_HOUSE: 
				chaseOut();
				break;
			case MODE_TALKING:
				if(!hasPath){
					processConversation();
				}
				break;
			case MODE_WORKING:
				work();
				break;
		}
		if(super.tick()){
			return true;
		}
		return false;
	}
	
	void openRandomConversation(){
		//TODO: make this some general conversation starter
	}
	
	void askQuestion(int r){
		sentence.add(GENERAL_QUESTIONS[r][RAND.nextInt(GENERAL_QUESTIONS[r].length)]);
		setDialogString(sentence.poll());
		showDialog(3*60);
	}
	
	public void processConversation(){
		if(conversation == null){
			return;
		}
		if(conversation.isFinished()){
			conversation = null;
			revertPausedMode();
			return;
		}
		turnTowards(getDirectionTo((conversation.getOwner() == this)? conversation.getPartner(): conversation.getOwner()));
		
		if(conversation.getOwner() == this){
			conversation.tick();
		}else
		if(conversation.getPartner() == this && !conversation.isOn()){
			Sprite s = conversation.getOwner();
			startPathTo(s.getX() - dx[getDirectionTo(s.getX(), s.getY())],
					s.getY()  - dy[getDirectionTo(s.getX(), s.getY())]);
		}
	}
	
	void disconnectFromTalk(){
		setDialogString("Yeah, yeah...");
		showDialog(2*60);
		conversation.disconnect(this);
		conversation = null;
		if(hasPausedMode){
			revertPausedMode();
		}else{
			targetMode = MODE_RELAX_AT_HOME;
		}
	}
	
	void revertPausedMode(){
		hasPausedMode = false;
		targetMode = pausedMode;
		targetSprite = pausedSprite;
		timePaused = 0;
		modeImportance = pausedModeImportance;
	}
	
	void pauseMode(){
		hasPausedMode = true;
		pausedMode = targetMode;
		pausedSprite = targetSprite;
		timePaused = 0;
		targetSprite = null;
		pausedModeImportance = modeImportance;
	}
	
	void reactToCall(){
		if(caller instanceof Citizen){
			startPathTo(caller.getX() - dx[getDirectionTo(caller.getX(), caller.getY())], 
					caller.getY() - dy[getDirectionTo(caller.getX(), caller.getY())]);
		}else{
			conditions[CONDITION_FEAR] += 0.1;
		}
	}
	
	void relaxAtHome(){
		if(timeSinceHome > 60*3){
			setDialogString("Aah, finally home!");
			showDialog(60*3);
		}
		timeSinceHome = 0;
		
		if(!expectingSeveralVisits){
			Citizen c;
			
			if((c = findIntruder()) != null){
				targetMode = MODE_CHASE_OUT_OF_HOUSE;
				timeToWait = 30;
				setDialogString(getRandomDialogString(MODE_SURPRISED));
				
				showDialog(30);
				targetSprite = c;
			}else if(!showDialog){
				if(RAND.nextInt(60*60) == 0){
					setDialogString(getRandomDialogString(MODE_RELAX_AT_HOME));
					showDialog(60*2);
				}
			}
		}
		
		if(RAND.nextInt(120) == 0){
			int n = RAND.nextInt(4);
			if(home.isClosedAtGlobal(x + dx[n], y + dy[n])){
				tryStartMoving(n);
			}
		}
	}
	
	public void chaseOut(){
		if(!showDialog){
			setDialogString(getRandomDialogString(MODE_CHASE_OUT_OF_HOUSE));
			showDialog(3*60);
		}
		chase(targetSprite);
		conditions[CONDITION_ANGER] += 0.005;
		if(village.ownedBy(x, y) != village.ownedBy(targetSprite.getX(), targetSprite.getY())){
			targetMode = MODE_RELAX_AT_HOME;
			setDialogString("Yes, that's right!");
			showDialog(2*60);
			timeToWait = 60;
			hasPath = false;
		}
	}
	
	String getRandomDialogString(int mode){
		return DIALOG_STRINGS[mode][RAND.nextInt(DIALOG_STRINGS[mode].length)];
	}
	
	
	
	boolean tryStepTowards(int nx, int ny){
		return tryStartMoving(getDirectionTo(nx, ny));
	}
	
	void chase(Sprite s){
		tryStepTowards(s.getX(), s.getY());
	}
	
	public boolean isInsideHome(){
		return home.isClosedAtGlobal(x, y);
	}
	
	public void startGoHome(){
		
		startPathTo(home.getX()+ home.getW()/2, home.getY() + home.getH()/2);
	}
	
	public void makeAnimationFrames(){
		numAnimations = 4;
		numFrames = 1;
		animationFrames = new int[numAnimations][numFrames][H][W];
		
		for(int i = 0; i < numAnimations; i++){
			for(int j = 0; j < H; j++){
				for( int k = 0; k < W ; k++){
					animationFrames[i][0][j][k] = data[j][k];
				}
			}
				
		}
		//int ringColor = Sprite.getColor(0, 128, 128);
		int black = Sprite.getColor(0, 0, 0);
		
		/*for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int u = (j-W/2)*(j-W/2) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y);
				if(u < HEAD_SQ_RADIUS){
					int d = (int)(2*Math.sqrt((j - W/2)*(j - W/2) + (i*2 - 3- HEAD_OFFSET_Y)*(i*2 - 3 - HEAD_OFFSET_Y)));
					if(d == 4){
						animationFrames[DOWN][0][i][j] = ringColor;
					}else if(d < 4){
						animationFrames[DOWN][0][i][j] = black;
					}
					
					d = (int)(2*Math.sqrt((j + W/2 - HEAD_SQ_RADIUS)*(j+ W/2 - HEAD_SQ_RADIUS) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y)));
					if( d == 8){
						animationFrames[LEFT][0][i][j] = ringColor;
					}else if(d < 8){
						animationFrames[LEFT][0][i][j] = black;
					}
					
					d = (int)(2*Math.sqrt((j -W)*(j - W) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y)));
					if( d == 8){
						animationFrames[RIGHT][0][i][j] = ringColor;
					}else if(d < 8){
						animationFrames[RIGHT][0][i][j] = black;
					}
				}
			}
		}*/
		
		animationFrames[DOWN][0][HEAD_OFFSET_Y][W/2 - 2] = black;
		animationFrames[DOWN][0][HEAD_OFFSET_Y][W/2 + 2] = black;
		
		animationFrames[RIGHT][0][HEAD_OFFSET_Y][W/2 + 4] = black;
		animationFrames[LEFT][0][HEAD_OFFSET_Y][W/2 - 4] = black;
	}
	
	public Citizen findCitizen(){
		Citizen c;
		for(int i = 1; i < RANGE_OF_VIEW; i++){
			for(int j = (int)(-TAN_FOV*i + 1); j <TAN_FOV*i ;j++){
				if(village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
						y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j) instanceof Citizen && 
						village.ownedBy(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
						y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j) == village.ownedBy(x, y)){
					c = (Citizen)village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
							y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j);
					if(village.isOwnedBy(c.getX(), c.getY(), village.ownedBy(x, y))){
						return c;
					}
				}
			}
		}
		
		for(int i = -1; i<=1 ;i++){
			for(int j = -1; j <= 1; j++){
				if(j == 0&& i == 0)
					continue;
				if(village.getSpriteAt(x + i,
						y + j) instanceof Citizen){
					c = (Citizen)village.getSpriteAt(x + i, y + j);
					if(c != this&& village.isOwnedBy(c.getX(), c.getY(), village.ownedBy(x, y))){
						return c;
					}
				}
			}
		}
		return null;
	}
	
	public Citizen findIntruder(){
		Citizen c;
		for(int i = 1; i < RANGE_OF_VIEW; i++){
			for(int j = (int)(-TAN_FOV*i + 1); j <TAN_FOV*i ;j++){
				if(village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
						y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j) instanceof Citizen){
					c = (Citizen)village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
							y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j);
					if(expectingVisit != c.getCitizenNumber()&& village.isOwnedBy(c.getX(), c.getY(), village.ownedBy(x, y))){
						return c;
					}
				}
			}
		}
		
		for(int i = -1; i<=1 ;i++){
			for(int j = -1; j <= 1; j++){
				if(j == 0&& i == 0)
					continue;
				if(village.getSpriteAt(x + i,
						y + j) instanceof Citizen){
					c = (Citizen)village.getSpriteAt(x + i, y + j);
					if(c != this&& expectingVisit != c.getCitizenNumber()&& village.isOwnedBy(c.getX(), c.getY(), village.ownedBy(x, y))){
						return c;
					}
				}
			}
		}
		return null;
	}
	
	
	
	protected void work(){
		
	}
	
	protected boolean importantEnough(Conversation c){
		if(c.getOwner() instanceof Citizen){
			return (1 + ((Citizen)(c.getOwner())).getCitizenNumber())*c.getImportance() > modeImportance;
		}else{
			return true;
		}
	}
	
	public void talk(){
		if(conversation.getOwner() == this){
			askQuestion(0);
		}else{
			disconnectFromTalk();
		}
	}
	
	public void deniedConversation(){
		conversation = null;
		setDialogString("Oh... Ok, then...");
		showDialog(60*2);
		revertPausedMode();
	}

}
