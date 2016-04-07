package priv.hkon.theseq.sprites;

import java.util.Arrays;

import priv.hkon.theseq.misc.Conversation;
import priv.hkon.theseq.misc.Notification;
import priv.hkon.theseq.misc.Sentence;
import priv.hkon.theseq.misc.VillageEvent;
import priv.hkon.theseq.structures.Bed;
import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.world.Village;

public abstract class Villager extends Citizen{
	
	private static final long serialVersionUID = 6040911560799224747L;
	House home;
	int timeSinceHome;
	int citizenNumber;

	int timeToWait= 0;
	
	VillageEvent currEvent;
	
	public static final int HEAD_OFFSET_Y = 5;
	public static final int HEAD_OFFSET_X = W/2;
	
	public static final int RANGE_OF_VIEW = 5;
	public static final float TAN_FOV = 1.0f;
	
	
	int targetMode;
	int modeImportance;
	Sprite targetSprite;
	int modeTargetX;
	int modeTargetY;
	
	public Sprite lastVictim;
	
	int dayCycleShift;
	
	boolean hasPausedMode = false;
	int pausedMode;
	int timePaused = 0;
	int pausedModeImportance;
	int pausedModeTargetX;
	int pausedModeTargetY;
	Sprite pausedSprite;
	
	boolean hasCalled = false;
	
	public boolean presenting = false;
	public boolean didPresent = false;
	
	static int[][][] fovDeltaCoordinates;
	
	float[] affections; //Numbers from -1 to 1 which tells how much this citizen likes the others.
	
	public static final int CONDITION_ANGER = 0;
	public static final int CONDITION_FEAR = 1;//NB: Assumes that conditions are negative in getSelfConditionInt()
	
	float[] conditions = new float[]{0.0f, 0.0f};
	public static final String[] CONDITION_STRINGS = {
			"angry", "afraid"
	};
	
	public int nextMode = -1;
	
	public static final int MODE_RELAX_AT_HOME = 0;
	public static final int MODE_CHASE_OUT_OF_HOUSE = 1;
	public static final int MODE_SURPRISED = 2;
	public static final int MODE_CURIOUS = 3;
	public static final int MODE_TALKING = 4;
	public static final int MODE_WORKING = 5;
	public static final int MODE_FLEEING = 6;
	public static final int MODE_THINKING = 7;
	public static final int MODE_RELAXING = 8;
	public static final int MODE_SLEEPING = 9;
	public static final int MODE_SPEAKING_TO_PLAYER = 10;
	public static final int MODE_WAITING_FOR_EVENT = 11;
	
	public static final int IMPORTANT_NOT = 0;
	public static final int IMPORTANT_LITTLE = 1;
	public static final int IMPORTANT_MEDIUM  = 2;
	public static final int IMPORTANT_VERY = 3;
	
	public static final int SPEECH_PRESENTING = 10000;
	
	public static final String[][] DIALOG_STRINGS = {{"Dum di dum di dum", "La la-laaah lah!"}, 
			{"Hey, get out!", "You're not welcome here!", "Eyh, you should leave now"},
			{"Hey...", "What the...","Uhm..","!!"},
			{"Hum?", "?", "!"},
			{},
			{},
			{},
			{"Hmmmm....", "Maybe...", "Erm.. What about.."},
			{"Life is good..."},
			{"Zzz"},
			{},
			{}
	};
	
	int modeParameter; //For various use, depending on mode
	
	//TODO: Make woodcutter, with his own woodcutter-cabin
	
	public int expectingVisit = -1;
	public boolean expectingSeveralVisits = false;
	
	int coatColor = Sprite.getColor(40, 60, 80);
	
	public static final int HEAD_SQ_RADIUS = W*W/9 - 1;

	public Villager(int x, int y, Village v, House h, int i) {
		super(x, y, v, i);
		home = h;
		affections = new float[village.getNumCitizens()];
		for(int j = 0; j < affections.length; j++){
			affections[j] = .0f + RAND.nextFloat()*.20f;
		}
		targetMode = MODE_RELAXING;
		dayCycleShift = RAND.nextInt(60*60);
		
		//System.out.println("Made Villager at " + x + " " + y);
	}
	
	public House getHome(){
		return home;
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
		data = animationFrames[movingDirection][0];
		if(!isInsideHome()){
			timeSinceHome++;
		}
		if(hasPausedMode){
			timePaused++;
		}
		if(timeToWait >0){
			timeToWait--;
			return false;
		}
		
		if(super.tick()){
			return true;
		}
		
		
		while(!receivedNotifications.isEmpty()){
			if(reactToNotification(receivedNotifications.poll())){
				return true;
			}
		}
		
		if(conversation != null&& targetMode != MODE_TALKING && targetMode != MODE_SPEAKING_TO_PLAYER){
			pauseMode();
			targetMode = MODE_TALKING;
			if(conversation.getOwner() != this){
				targetSprite = conversation.getOwner();
			}else{
				targetSprite = conversation.getPartner();
			}
			hasPath = false;
		}

		switch(targetMode){
			case MODE_RELAX_AT_HOME: 
				if(!isInsideHome()){
					if(hasPath&&home.isClosedAtGlobal(pathTargetX, pathTargetY))
						break;
					startGoHome();
				}else{
					relaxAtHome();
				}
				if(isBedTime()){
					goToBed();
				}
				if(isWorkTime()){
					setMode(MODE_WORKING, IMPORTANT_MEDIUM, null);
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
				if(!isWorkTime()){
					setMode(MODE_RELAXING, IMPORTANT_NOT, null);
				}
				if(isBedTime()){
					goToBed();
				}
				break;
			case MODE_FLEEING:
				if(distTo(targetSprite) > RANGE_OF_VIEW*1.5){
					if(hasPausedMode){
						revertPausedMode();
					}else{
						runAway();
					}
				}break;
			case MODE_RELAXING: {
				
				relax();
				
				break;
			}
			
			case MODE_SLEEPING: {
				sleep();
				break;
			}
			
			case MODE_SPEAKING_TO_PLAYER: {
				speakToPlayer();
				break;
			}
			
			case MODE_WAITING_FOR_EVENT: {
				if(currEvent == null){
					if( hasPausedMode){
						revertPausedMode();
					}else{
						setMode(MODE_RELAXING, IMPORTANT_NOT, null);
					}
				}
				if(currEvent.isHappening()){
					processEvent(currEvent);
				}
				break;
			}
		}
		
		return false;
	}
	
	public void relax(){
		
		if(isBedTime()){
			goToBed();
			return;
		}
		if(isWorkTime()){
			setMode(MODE_WORKING, IMPORTANT_MEDIUM, null);
		}

		Citizen c;
		if((c = findCitizen()) != null && c != lastVictim && c.getConversation() == null
				&& !((c instanceof Villager &&((Villager)c).getMode() == MODE_TALKING)
						|| (! (c instanceof Player) && RAND.nextInt(20)> 0))){
			
			hasPath = false;
			targetSprite = c;
			lastVictim = c;
			
			engageConversation(c, IMPORTANT_VERY);
		}else if(!hasPath){
			strollTownGrid();
		}
	}
	
	boolean reactToNotification(Notification n){
		if(n.getType() == Notification.TYPE_INVITATION_TO_CONVERSATION){
			if(n.getCreator() instanceof Citizen && affections[((Citizen)(n.getCreator())).getCitizenNumber()] * n.getImportance() > modeImportance){
				if(targetMode != MODE_RELAX_AT_HOME){
					pauseMode();
				}
				targetMode = MODE_TALKING;
				targetSprite = conversation.getOwner();
				lastVictim = conversation.getOwner();
				hasPath = false;
				return true;
			}else{
				return false;
			}
		}else if(n.getType() == Notification.TYPE_UNFRIENDLY){
			pauseMode();
			targetMode = MODE_FLEEING;
			targetSprite = n.getCreator();
			hasPath = false;
			return true;
		}
		return false;
	}
	
	boolean hasPausedPath = false;
	
	public void speakToPlayer(){
		if(sentence.isEmpty() && !showDialog && !hasPath){
			speechFinished();
			return;
		}
		if(distTo(village.getPlayer())> 14){
			speechInterrupted();
			return;
		}else if(distTo(village.getPlayer())> 6){
			if(hasPath){
				if(distBetween(getX() + dx[movingDirection], getY() + dy[movingDirection], 
						village.getPlayer().getX(), village.getPlayer().getY())
						< distTo(village.getPlayer())){
					//...
				}else{
					hasPausedPath = true;
					hasPath = false;
					turnTowards(getDirectionTo(village.getPlayer()));
				}
			}else{
				turnTowards(getDirectionTo(village.getPlayer()));
			}
		
		}else{
			if(hasPausedPath){
				hasPath = true;
				hasPausedPath = false;
			}
			if(!hasPath){
				turnTowards(getDirectionTo(village.getPlayer()));
			}
		}
	}
	
	public void speechInterrupted(){
		sentence.clear();
		if(subclassSpeechInterrupted()){
			return;
		}
		
		hasPath = false;
		if(nextMode != -1){
			targetMode = nextMode;
			nextMode = -1;
		}else if(hasPausedMode){
			revertPausedMode();
		}else{
			setMode(MODE_RELAXING, IMPORTANT_NOT, null);
		}
		showDialog("Oh.. We'll talk later, then", 120);
	}
	
	public void speechFinished(){
		if(conversation != null){
			conversation.disconnect(this);
		}
		if(subclassSpeechFinished()){
			return;
		}
		
		if(hasPausedMode && pausedMode == MODE_TALKING){
			hasPausedMode = false;
		}
		
		conversation = null;
		//System.out.println("SpeechFinished");
		
		if(nextMode != -1){
			//System.out.println("Next mode sat");
			targetMode = nextMode;
			nextMode = -1;
			return;
		}else if(hasPausedMode){
			revertPausedMode();
			
		}else{
			setMode(MODE_RELAXING, IMPORTANT_NOT, null);
		}
		lastVictim = village.getPlayer();
		showDialog(Sentence.getRandomString(Sentence.TYPE_GOODBYE, Sentence.GOODBYE_NEUTRAL) , 120);
	}
	
	boolean isBedTime(){
		return (village.getTime() - dayCycleShift)%Village.DAYCYCLE_DURATION > 2*Village.DAYCYCLE_DURATION/3;
	}
	
	boolean isWorkTime(){
		return (village.getTime() - dayCycleShift)%Village.DAYCYCLE_DURATION <Village.DAYCYCLE_DURATION/3;
	}
	
	void runAway(){
		int i = 0;
		if(hasPath){
			return;
		}
		while(village.ownedBy(getX(), getY() + 2 + i) != null){
			i++;
		}
		startPathTo(getX(), getY() + 2 + i);
	}
	
	void askQuestion(int r){
		currSentence = new Sentence(conversation.getOther(this), Sentence.TYPE_QUESTION, r, this);
		saySentence(currSentence);
	}
	
	void saySentence(Sentence s){
		currSentence = s;
		String[] sent = currSentence.getStrings();
		for(int i = 0; i < sent.length; i++){
			sentence.add(sent[i]);
		}
		setDialogString(sentence.poll());
		showDialog(2*60);
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
		modeTargetX = pausedModeTargetX;
		modeTargetY = pausedModeTargetY;
	}
	
	void pauseMode(){
		hasPausedMode = true;
		pausedMode = targetMode;
		pausedSprite = targetSprite;
		pausedModeTargetX = modeTargetX;
		pausedModeTargetY = modeTargetY;
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
	Citizen intruder;
	void relaxAtHome(){
		if(timeSinceHome > 60*3){
			setDialogString("Aah, finally home!");
			showDialog(60*3);
		}
		timeSinceHome = 0;
		
		if(!expectingSeveralVisits){
			
			
			if((intruder = findIntruder()) != null){
				intruder.receiveNotification(new Notification(Notification.TYPE_UNFRIENDLY,
						IMPORTANT_MEDIUM, this));
				targetMode = MODE_CHASE_OUT_OF_HOUSE;
				timeToWait = 30;
				setDialogString(getRandomDialogString(MODE_SURPRISED));
				
				showDialog(30);
				targetSprite = intruder;
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
	
	public void goToBed(){
		targetMode = MODE_SLEEPING;
		targetSprite = home.getBed().getBlockAt(0, 1);
		startPathTo(home.getBed().getX(), home.getBed().getY() + 1);
	}
	
	public void sleep(){//Needs some serious debugging
		if(moving){
			return;
		}
		if(hasPath && (village.getNonBlockAt(pathTargetX, pathTargetY) == null || !(village.getNonBlockAt(pathTargetX, pathTargetY).getStructure() instanceof Bed))){
			goToBed();
			return;
		}
		if(!hasPath&&(village.getNonBlockAt(x, y) == null ||!(village.getNonBlockAt(x, y).getStructure() instanceof Bed))){
			goToBed();
			return;
		}
		
		//The villager is in bed
		
		movingDirection = UP;
		
		if(Sprite.RAND.nextInt(2000) == 0){
			setDialogString(getRandomDialog(MODE_SLEEPING));
			showDialog(60*2);
		}
		
		if(!isBedTime()){
			wakeUp();
			targetMode = MODE_RELAXING;
			targetSprite = null;
		}
	}
	
	public void wakeUp(){
		movingDirection = DOWN;
		timeToWait = 60*1;
	}
	
	public String getRandomDialog(int g){
		return DIALOG_STRINGS[g][RAND.nextInt(DIALOG_STRINGS[g].length)];
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
	
	void chase(Sprite s){
		tryStepTowards(s.getX(), s.getY());
	}
	
	public boolean isInsideHome(){
		return home.isClosedAtGlobal(x, y);
	}
	
	public void startGoHome(){
		
		startPathTo(home.getX()+ home.getW()/2, home.getY() + home.getH()/2);
	}
	
	public void setMode(int m, int i, Sprite s){
		targetMode = m;
		modeImportance = i;
		targetSprite = s;
	}
	
	public void setMode(int m, int i, Sprite s, int x, int y){
		setMode(m, i , s);
		modeTargetX = x;
		modeTargetY = y;
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
	
	Citizen guest;
	
	public Citizen findIntruder(){
		
		for(int i = 1; i < RANGE_OF_VIEW; i++){
			for(int j = (int)(-TAN_FOV*i + 1); j <TAN_FOV*i ;j++){
				if(village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
						y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j) instanceof Citizen){
					guest = (Citizen)village.getSpriteAt(x + dx[movingDirection]*i + dx[(movingDirection+1)%4]*j,
							y + dy[movingDirection]*i + dy[(movingDirection+1)%4]*j);
					if(expectingVisit != guest.getCitizenNumber()&& village.isOwnedBy(guest.getX(), guest.getY(), village.ownedBy(x, y))){
						return guest;
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
					guest = (Citizen)village.getSpriteAt(x + i, y + j);
					if(guest != this&& expectingVisit != guest.getCitizenNumber()&& village.isOwnedBy(guest.getX(), guest.getY(), village.ownedBy(x, y))){
						return guest;
					}
				}
			}
		}
		return null;
	}
	
	
	
	protected void work(){
		relax();
		/*if(!hasPath){
			strollTownGrid();
		}*/
	}
	
	protected boolean importantEnough(Conversation c){
		if(c.getOwner() instanceof Citizen){
			return (1 + ((Citizen)(c.getOwner())).getCitizenNumber())*c.getImportance() > modeImportance;
		}else{
			return true;
		}
	}
	
	public void talk(){
		/*if(conversation.getOwner() == this){
			askQuestion(RAND.nextInt(Sentence.QUESTIONS.length));
		}else{
			
			
			disconnectFromTalk();
		}*/
		
		if(conversation == null){
			return;
		}
		int id = RAND.nextInt(village.getNumCitizens());
		while(id == this.citizenNumber || (conversation.getOther(this) instanceof Citizen && id == ((Citizen)(conversation.getOther(this))).getCitizenNumber())){
			id = (id + 1)% village.getNumCitizens();
		}
		
		if(conversation.getOther(this) instanceof Player && !classHasPresented()){
			setToSpeakMode(getPresentation(), getPresentationDurations(), SPEECH_PRESENTING);
			return;
		}
		
		if(conversation.getLastSentence() == null){
			saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_GREETING, Sentence.GREETING_NEUTRAL, this));
		}else{
			float aff;
			if(conversation.getOther(this) instanceof Citizen){
				aff = affections[((Citizen)(conversation.getOther(this))).getCitizenNumber()];
			}else{
				aff = 0;
			}
			int type = conversation.getLastSentence().getType();
			
			if(conversation.getOther(this) instanceof Player){
				//System.out.println("Talking to Player");
				if(type == Sentence.TYPE_GREETING){
					if(RAND.nextInt(5) == 0 || !(this instanceof Nobody)){
						saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_QUESTION, this, village.getCitizen(id)));
					}else{
						saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_RUMOR_INTRODUCTION, this));
					}
				}else if(type == Sentence.TYPE_RUMOR_INTRODUCTION){
					saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_RUMORS,0, Sentence.GENERAL_RUMORS[RAND.nextInt(Sentence.GENERAL_RUMORS.length)],this));
				}else
				if(type == Sentence.TYPE_EMPTY){
					saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_PLAYER_NOT_TALKATIVE, Math.min(Math.max((int)(aff + 1), 0), 2), this));
				}else if(type == Sentence.TYPE_PLAYER_NOT_TALKATIVE || type == Sentence.TYPE_RUMORS){
					saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_GOODBYE, Math.min(Math.max((int)(aff + 1), 0), 2), this));
					disconnectFromTalk();
				}else{
					saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_EMPTY, this));
				}
				return;
			}
			
			if(conversation.getSentenceCount() == 1&& !(conversation.getOther(this) instanceof Player)){
				saySentence(new Sentence(conversation.getOther(this),Sentence.TYPE_GREETING, Sentence.GREETING_NEUTRAL, this));
				return;
			}else if(type == Sentence.TYPE_GREETING){
				saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_QUESTION, this, village.getCitizen(id)));
				return;
			}else if(type == Sentence.TYPE_QUESTION){
				saySentence(getAnswer(conversation.getLastSentence().getArg2()));
				return;
			}else if(type == Sentence.TYPE_CUSTOM_ANSWER){
				saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_RESPONSE, Math.min(Math.max((int)(2*Sentence.meaningWeight[conversation.getLastSentence().getArg2()]*aff) + 1, 0), 2), this));
				return;
			}else if(type == Sentence.TYPE_GOODBYE ){
				saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_GOODBYE, Math.min(Math.max((int)(aff + 1), 0), 2), this));
				disconnectFromTalk();
				return;
			}else{
				saySentence(new Sentence(conversation.getOther(this), Sentence.TYPE_GOODBYE, Math.min(Math.max((int)(aff + 1), 0), 2), this));
				return;
			}
			//disconnectFromTalk();
		}
	}
	
	Sentence getAnswer(int type){
		switch(type){
		case Sentence.QUESTION_MEANING_OF_LIFE: 
			return new Sentence(conversation.getOther(this), Sentence.TYPE_CUSTOM_ANSWER, Sentence.CUSTOM_ANSWER_NEUTRAL, getMeaningOfLife(), this);
		case Sentence.QUESTION_RELATION_TO:
			return new Sentence(conversation.getOther(this), Sentence.TYPE_CUSTOM_ANSWER, getRelationToInt(conversation.getLastSentence().getTopic()), new String[] {getRelationToString(conversation.getLastSentence().getTopic())}, this);
		case Sentence.QUESTION_SELF_CONDITION:
			return new Sentence(conversation.getOther(this), Sentence.TYPE_CUSTOM_ANSWER, getOwnConditionInt(), new String[]{getOwnConditionString()}, this, this);
		}
		return new Sentence(conversation.getOther(this), Sentence.TYPE_CUSTOM_ANSWER, Sentence.CUSTOM_ANSWER_NEUTRAL, new String[]{"..."}, this);
	}
	
	public String getRelationToString(Sprite s){
		if( s instanceof Citizen){
			float aff = affections[((Citizen)s).getCitizenNumber()];
			if( Math.abs(aff) < 0.4){
				return "Ok, I guess";
			}
			if(aff < 0){
				return "Em " + Sentence.VERBS[RAND.nextInt(Sentence.VERBS.length)] + " like "
						+Sentence.ADJECTIVES[Sentence.ADJECTIVES_NEGATIVE][RAND.nextInt(Sentence.ADJECTIVES[Sentence.ADJECTIVES_NEGATIVE].length)] + 
						" " + Sentence.NOUNS[Sentence.NOUN_NEGATIVE][RAND.nextInt(Sentence.NOUNS[Sentence.NOUN_NEGATIVE].length)];
			}else{
				return "Em " + Sentence.VERBS[RAND.nextInt(Sentence.VERBS.length)] + " like "
						+Sentence.ADJECTIVES[Sentence.ADJECTIVES_POSITIVE][RAND.nextInt(Sentence.ADJECTIVES[Sentence.ADJECTIVES_POSITIVE].length)] + 
						" " + Sentence.NOUNS[Sentence.NOUN_POSITIVE][RAND.nextInt(Sentence.NOUNS[Sentence.NOUN_POSITIVE].length)];
			}
		}else{
			return "Ok... I guess?";
		}
	}
	
	public int getRelationToInt(Sprite s){
		if( s instanceof Citizen){
			float aff = affections[((Citizen)s).getCitizenNumber()];
			if( Math.abs(aff) < 0.4){
				return Sentence.CUSTOM_ANSWER_NEUTRAL;
			}
			if(aff < 0){
				return Sentence.CUSTOM_ANSWER_NEGATIVE;
				
			}else{
				return Sentence.CUSTOM_ANSWER_POSITIVE;
			}
		}else{
			return Sentence.CUSTOM_ANSWER_NEUTRAL;
		}
	}
	
	public String getOwnConditionString(){
		for(int i = 0; i < conditions.length; i++){
			if(conditions[i] > 0.7){
				return "I am actually kinda " + CONDITION_STRINGS + " now";
			}
		}
		
		return "I am totally fine";
	}
	
	public int getOwnConditionInt(){
		for(int i = 0; i < conditions.length; i++){
			if(conditions[i] > 0.7){
				return Sentence.CUSTOM_ANSWER_NEGATIVE;
			}
		}
		
		return Sentence.CUSTOM_ANSWER_NEUTRAL;
		
		
	}
	
	public abstract String[] getMeaningOfLife();
	
	public void deniedConversation(){
		conversation.finished = true;
		conversation = null;
		setDialogString("Oh... Ok, then...");
		showDialog(60*2);
		revertPausedMode();
	}
	
	public static void init(){
		int count = 0;
		fovDeltaCoordinates = new int[4][24][2];//Yeah, mystical constant, right? 24 is just the number of tiles we are inspecting
		
		for(int dir = 0; dir < 4; dir++){
			count = 0;
		
			for(int i = 1; i < RANGE_OF_VIEW; i++){
				for(int j = (int)(-TAN_FOV*i + 1); j <TAN_FOV*i ;j++){
					fovDeltaCoordinates[dir][count][0] = dx[dir]*i + dx[(dir + 1)%4]*j;
					
					fovDeltaCoordinates[dir][count][1] = dy[dir]*i + dy[(dir + 1)%4]*j;
					count ++;
				}
			}
		
		
			for(int i = -1; i<=1 ;i++){
				for(int j = -1; j <= 1; j++){
					if(j == 0&& i == 0)
						continue;
					fovDeltaCoordinates[dir][count][0] = j;
					fovDeltaCoordinates[dir][count][1] = i;
					count++;
				}
			}
		}
	}
	
	public String getName(){
		return "Number " + getCitizenNumber();
	}
	
	public boolean hasImportantMode(){
		return modeImportance < IMPORTANT_MEDIUM || (targetMode == MODE_RELAXING || targetMode == MODE_RELAX_AT_HOME);
	}
	
	public void setToSpeakMode(String[] str, Integer[] dur, int param){
		if(hasImportantMode()){
			pauseMode();
		}
		modeParameter = param;
		sentence.addAll(Arrays.asList(str));
		dialogLengths.addAll(Arrays.asList(dur));
		targetMode = MODE_SPEAKING_TO_PLAYER;
		showDialog = true;
	}
	
	
	
	public int getMode(){
		return targetMode;
	}
	

	public void processEvent(VillageEvent ve){
	}
	
	public void setToWaitMode(VillageEvent ev, int importance){
		pauseMode();
		currEvent = ev;
		targetMode = MODE_WAITING_FOR_EVENT;
		modeImportance = importance;
	}

	public abstract String[] getPresentation();
	public Integer[] getPresentationDurations(){
		return new Integer[0];
	}
	public abstract boolean classHasPresented();
	
	public abstract boolean subclassSpeechInterrupted();
	public abstract boolean subclassSpeechFinished();
}
