package priv.hkon.theseq.world;

import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.util.Random;

import priv.hkon.theseq.blocks.Tree;
import priv.hkon.theseq.blocks.Wall;
import priv.hkon.theseq.cutscenes.Cutscene;
import priv.hkon.theseq.cutscenes.OpeningScene;
import priv.hkon.theseq.cutscenes.WakeUpCutscene;
import priv.hkon.theseq.main.Controller;
import priv.hkon.theseq.main.Core;
import priv.hkon.theseq.main.Screen;
import priv.hkon.theseq.misc.DialogBubble;
import priv.hkon.theseq.misc.Sentence;
import priv.hkon.theseq.nonblocks.Door;
import priv.hkon.theseq.nonblocks.Flowers;
import priv.hkon.theseq.nonblocks.NonBlock;
import priv.hkon.theseq.nonblocks.TileCover;
import priv.hkon.theseq.nonblocks.WoodTileCover;
import priv.hkon.theseq.sprites.Citizen;
import priv.hkon.theseq.sprites.Gardener;
import priv.hkon.theseq.sprites.Mayor;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.Nobody;
import priv.hkon.theseq.sprites.Painter;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.Prophet;
import priv.hkon.theseq.sprites.Shadow;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.sprites.TalkativeSprite;
import priv.hkon.theseq.sprites.Villager;
import priv.hkon.theseq.sprites.Woodcutter;
import priv.hkon.theseq.structures.Building;
import priv.hkon.theseq.structures.GardenerHouse;
import priv.hkon.theseq.structures.House;
import priv.hkon.theseq.structures.MayorHouse;
import priv.hkon.theseq.structures.PainterHouse;
import priv.hkon.theseq.structures.Structure;
import priv.hkon.theseq.structures.WoodcutterHouse;

public class Village implements Serializable{

	private static final long serialVersionUID = 21014336336090563L;
	public Player player;
	public static final int W = 1000;
	public static final int H = 1000;
	
	Random random;
	
	public boolean woodcutterQuestCompleted = false; 
	public boolean painterQuestCompleted = false;
	
	public boolean nightBoost = false;
	
	public Cutscene currScene;
	public boolean inCutscene = false;
	
	public int timesSeenShadow = 0;
	
	int[][] tiles = new int[H][W];
	Sprite[][] sprites = new Sprite[H][W];
	NonBlock[][] nonBlocks = new NonBlock[H][W];
	TileCover[][] tileCovers = new TileCover[H][W];
	Building[][] ownedBy = new Building[H][W];
	
	Citizen[] citizenList;
	
	public int[] villagerPermutation;
	
	int timesSlept = 0;
	
	boolean[][] closed = new boolean[H][W];
	boolean shouldDrawInside = false;
	Building currBuilding = null; 
	
	int numVillagers = 48;
	Villager[] villagers = new Villager[numVillagers];
	
	
	int houseSide = 10;
	int houseSpread = 15;
	int townGridMiddleX = W/2;
	int townGridMiddleY = H/2;
	
	int numHouses = (int)(Math.pow(Math.ceil(Math.sqrt(numVillagers)), 2));
	int townGridSide = (int)(Math.ceil(Math.sqrt(numHouses)));
	
	House[][] townGrid = new House[townGridSide][townGridSide];
	
	public double camX= -100, camY = -100;
	
	Shadow shadow;
	
	public double camSpeed = 0.1;
	double tilesPerPixelX = 1.0/Tile.WIDTH;
	double tilesPerPixelY = 1.0/Tile.HEIGHT;
	
	transient Core core;
	
	public static final int DAYCYCLE_DURATION = 60*60*10;
	
	long time = DAYCYCLE_DURATION +  30*60;
	
	public long lastSave;
	
	public Village(Core core){
		random = new Random();
		Villager.init();
		Sentence.village = this;
		this.core = core;
		
		shadow = new Shadow(0,0, this);
		buildVillage();
		
	}
	
	public void buildVillage(){
		for(int i = 0 ; i < H ; i++){
			for(int j = 0 ; j < W; j++){
				tiles[i][j] = Tile.TYPE_GRASS;
			}
		}
		
		int townGridStartX = townGridMiddleX - houseSpread*townGridSide/2;
		int townGridStartY = townGridMiddleY - houseSpread*townGridSide/2;
		
		townGrid = new House[townGridSide][townGridSide];
		for(int i = 0; i < townGridSide; i++){
			for(int j = 0; j < townGridSide; j++){
				townGrid[i][j] = new House(townGridStartX + houseSpread*j, townGridStartY + houseSpread*i, houseSide, houseSide, this);
				addBuilding(townGrid[i][j]);
			}
		}
		
		
		for(int i = 0; i < townGridSide; i++){
			Road r = new Road(townGridStartX - houseSpread + houseSide + 1, townGridStartY+ houseSpread*i +houseSide + 1, (townGridSide-1)*houseSpread + houseSpread - 1, 3, this);
			addArea(r);
			r = new Road(townGridStartX + houseSpread*(i - 1) + houseSide + 1, townGridStartY, 3, (townGridSide - 1)*houseSpread + houseSpread - 1, this);
			addArea(r);
		}
		
		for(int i = 0; i< H; i++){
			for(int j = 0; j < W ; j++){
				if(i == 0 || j == 0 || i == H-1|| j == W-1){
					addSprite(new Wall(j, i, this));
				}
				if(nonBlocks[i][j] instanceof Door){
					setTileAt(Tile.TYPE_REFINED_ROCK, j, i +1);
				}
				if(tiles[i][j] == Tile.TYPE_GRASS &&sprites[i][j] == null){
					if(NonBlock.RAND.nextInt(10) == 0){
						addTileCover(new Flowers(j, i, this));
					}else if(NonBlock.RAND.nextInt(8) == 0){
						addSprite(new Tree(j, i, this));
					}
				}
			}
		}
		
		
		citizenList = new Citizen[numVillagers + 1];
		villagerPermutation = getPermutation(numVillagers);
		
		
		int i;
		for(i = 0; i< numVillagers - 5; i++){
			villagers[villagerPermutation[i]]= new Nobody(townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getX() + houseSide/2, 
					townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getY() + houseSide/2, 
					this, townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide], i);
			addSprite(villagers[villagerPermutation[i]]);
		}
		
		int vi = villagerPermutation[i];
		
		villagers[vi] = new Painter(townGrid[vi/townGridSide][vi%townGridSide].getX() + houseSide/2,
				townGrid[vi/townGridSide][vi%townGridSide].getY() + houseSide/2,
				/*this.townGridMiddleX + 100, this.townGridMiddleY + 10,*/ this, townGrid[vi/townGridSide][vi%townGridSide], i);
		addSprite(villagers[vi]);
		
		
		i++;
		vi = villagerPermutation[i];
		
		//System.out.println("Making woodcutter");
		villagers[vi] = new Woodcutter(townGrid[vi/townGridSide][vi%townGridSide].getX() + houseSide/2,
				townGrid[vi/townGridSide][vi%townGridSide].getY() + houseSide/2,
				/*this.townGridMiddleX + 100, this.townGridMiddleY + 10,*/ this, townGrid[vi/townGridSide][vi%townGridSide], i);
		addSprite(villagers[vi]);
		//villagers[vi].setMode(Woodcutter.MODE_WAIT_FOR_PLAYER_FILLING_CRATE, 3, null);
		/*villagers[vi].setToWaitMode(new VillageEvent(this, villagers[vi], Woodcutter.EVENT_WAIT_TO_GUIDE_PLAYER){
			public boolean isHappening(){
				return subject.distTo(getPlayer()) <= 3;
			}
		}, Villager.IMPORTANT_VERY);*/
		villagers[vi].debug = true;
		
		i++;
		i++;
		
		villagers[villagerPermutation[i]] = new Gardener(townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getX() + houseSide/2, 
				townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getY() + houseSide/2, 
				this, townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide], i);
		addSprite(villagers[villagerPermutation[i]]);
		
		i++;
		
		villagers[villagerPermutation[i]] = new Mayor(townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getX() + houseSide/2, 
				townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide].getY() + houseSide/2, 
				this, townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide], i);
		addSprite(villagers[villagerPermutation[i]]);
		
		//villagers[0].debug = true;
		
		createStartSet();
		
		for(i = 0;i < numVillagers; i++){
			citizenList[i] = villagers[villagerPermutation[i]];
		}
		
		for(i = 0; i < numVillagers; i++){
			makeSuitableHouse(citizenList[i], villagerPermutation[i]/townGridSide, villagerPermutation[i]%townGridSide);
		}
	}
	
	public void makeSuitableHouse(Sprite s, int i, int j){
		if(s instanceof Mayor){
			townGrid[i][j] = new MayorHouse(getTownStartX() + houseSpread*j, getTownStartY() + houseSpread*i, houseSide, houseSide, this);
		}else if(s instanceof Gardener){
			townGrid[i][j] = new GardenerHouse(getTownStartX() + houseSpread*j, getTownStartY() + houseSpread*i, houseSide, houseSide, this);
		}else if(s instanceof Woodcutter){
			townGrid[i][j] = new WoodcutterHouse(getTownStartX() + houseSpread*j, getTownStartY() + houseSpread*i, houseSide, houseSide, this);
		}else if(s instanceof Painter){
			townGrid[i][j] = new PainterHouse(getTownStartX() + houseSpread*j, getTownStartY() + houseSpread*i, houseSide, houseSide, this);
		}else{
			townGrid[i][j] = new House(getTownStartX() + houseSpread*j, getTownStartY() + houseSpread*i, houseSide, houseSide, this);
		}
		addBuilding(townGrid[i][j]);
	}
	
	public void createStartSet(){
		int sy = townGridMiddleY + 100;
		int sx = townGridMiddleX - 20;
		
		int w = 8;
		int h = 18;
		
		for(int i = sy; i < sy + h; i++){
			for( int j = sx ; j < sx + w; j++){
				sprites[i][j] = null;
				if(Sprite.RAND.nextInt(4) == 0){
					addTileCover(new Flowers(j, i, this));
				}
			}
		}
		
		int villageri = villagerPermutation[numVillagers-5];
		player = new Player(/*townGrid[6][6].getX() + houseSide/2,
				townGrid[6][6].getY() + houseSide/2,*/
				/*villagers[villageri].getX() - 2,
				villagers[villageri].getY() - 2,*/
				sx + w/2, sy + h/2, this, numVillagers);
		citizenList[numVillagers] = player;
		addSprite(player); 
		
		int i = numVillagers - 3;
		Prophet p = new Prophet(sx + w/2, sy, this, townGrid[villagerPermutation[i]/townGridSide][villagerPermutation[i]%townGridSide], i);
		villagers[villagerPermutation[i]] = p;
		addSprite(villagers[villagerPermutation[i]]);
		
		currScene = new OpeningScene(player, p, core);
		inCutscene = true;
	}
	
	public int[][] getScreenData(int w, int h){
		int[][] data = new int[h][w];
		if(nightBoost){
			for(int i = 0;i < h; i++){
				for(int j = 0; j < w; j++){
					data[i][j] = 255 << 24;
				}
			}
			return data;
		}
		
		int beginTileX = (int)(Math.floor(camX));
		int beginTileY = (int)(Math.floor(camY));
		
		int initY = Math.max(-beginTileY, 0);
		int initX = Math.max(-beginTileX, 0);
		
		int tilesInWidth = Math.min((int)Math.ceil(tilesPerPixelX*w) + 1, W - beginTileX);
		int tilesInHeight = Math.min((int) Math.ceil(tilesPerPixelY*h) + 1, H-beginTileY);
		
		int[][] rawdata = new int[tilesInHeight*Tile.HEIGHT][tilesInWidth*Tile.WIDTH];

		for(int i = initY; i < tilesInHeight ; i++){
			for(int j = initX; j < tilesInWidth; j++){
				if((!closed[beginTileY + i][beginTileX + j] && !shouldDrawInside) || isOwnedBy(beginTileX + j, beginTileY + i, currBuilding)){
					Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, Tile.getData(tiles[beginTileY + i][beginTileX+ j]), Tile.WIDTH, Tile.HEIGHT, j*Tile.WIDTH, i*Tile.HEIGHT);
					if(tileCovers[beginTileY + i][beginTileX + j] != null){
						Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, tileCovers[beginTileY + i][beginTileX + j].getData(), Tile.WIDTH, Tile.HEIGHT, j*Tile.WIDTH, i*Tile.HEIGHT);
					}
				}else{
					Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, Tile.getData(Tile.TYPE_EMPTY), Tile.WIDTH, Tile.HEIGHT, j*Tile.WIDTH, i*Tile.HEIGHT);
				}
			}
		}
		int boundY = Math.min(tilesInHeight  + 1, H - beginTileY);
		
		for(int i = initY; i < boundY; i++){
			for(int j = initX; j < tilesInWidth; j++){
				int dx = 0;
				int dy = 0;
				if(((!closed[beginTileY + i][beginTileX + j] && !shouldDrawInside) || (isOwnedBy(beginTileX + j, beginTileY + i, currBuilding)))){
					
					if(sprites[beginTileY + i][beginTileX + j] != null){
						if(sprites[beginTileY + i][beginTileX + j] instanceof Movable){
							dx = (int)((((Movable)(sprites[beginTileY + i][beginTileX + j])).getExactX()-sprites[beginTileY + i][beginTileX + j].getX())*(Tile.WIDTH));
							dy = (int)(((Movable)(sprites[beginTileY + i][beginTileX + j])).getMovedY()*Tile.HEIGHT);
						}
						Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, sprites[beginTileY + i][beginTileX+ j].getData(), Sprite.W, Sprite.H, j*Tile.WIDTH + dx , (i + 1)*Tile.HEIGHT - Sprite.H - Sprite.DRAW_OFFSET_Y + dy);
						if(sprites[beginTileY + i][beginTileX + j] instanceof TalkativeSprite){
							if(((TalkativeSprite)(sprites[beginTileY + i][beginTileX + j])).shouldDrawDialog()){
								DialogBubble d = ((TalkativeSprite)(sprites[beginTileY + i][beginTileX + j])).getDialog();
								Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, d.getData(), d.getWidth(), d.getHeight(), j*Tile.WIDTH + dx + d.getOffsetX(), i*Tile.HEIGHT + dy + d.getOffsetY());
							}
						}
					}
					if(nonBlocks[beginTileY + i][beginTileX + j] != null && (!closed[beginTileY + i + 1][beginTileX + j] ^ shouldDrawInside)){
						Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, nonBlocks[beginTileY + i][beginTileX+ j].getData(), Sprite.W, Sprite.H, j*Tile.WIDTH, (i + 1)*Tile.HEIGHT - Sprite.H - Sprite.DRAW_OFFSET_Y);
					}
					
					
				
				}
			}
		}
		
		for(int i = initY; i < boundY; i++){
			for(int j = initX; j < tilesInWidth; j++){
				int dx = 0;
				int dy = 0;
				if(((!closed[beginTileY + i][beginTileX + j] && !shouldDrawInside) || (isOwnedBy(beginTileX + j, beginTileY + i, currBuilding)))){
					
					if(sprites[beginTileY + i][beginTileX + j] != null&& sprites[beginTileY + i][beginTileX + j] instanceof TalkativeSprite){
						if(sprites[beginTileY + i][beginTileX + j] instanceof Movable){
							dx = (int)((((Movable)(sprites[beginTileY + i][beginTileX + j])).getExactX()-sprites[beginTileY + i][beginTileX + j].getX())*(Tile.WIDTH));
							dy = (int)(((Movable)(sprites[beginTileY + i][beginTileX + j])).getMovedY()*Tile.HEIGHT);
						}
						if(((TalkativeSprite)(sprites[beginTileY + i][beginTileX + j])).shouldDrawDialog()){
							DialogBubble d = ((TalkativeSprite)(sprites[beginTileY + i][beginTileX + j])).getDialog();
							Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, d.getData(), d.getWidth(), d.getHeight(), j*Tile.WIDTH + dx + d.getOffsetX(), i*Tile.HEIGHT + dy + d.getOffsetY());
						}
					}
				}
			}
		}
		int camTileBeginX = (int)((camX - Math.floor(camX))*Tile.WIDTH);
		
		int camTileBeginY = (int)((camY - Math.floor(camY))*Tile.HEIGHT);
		
		double stepY = 1.0/(tilesPerPixelY*Tile.HEIGHT);
		double stepX = 1.0/(tilesPerPixelX*Tile.WIDTH);
		double sumy = 0;
		double sumx = 0;
		for(int i = 0; i<h ; i++ ){
			for(int j = 0; j<w; j++){
				
				data[i][j] = rawdata[(int)(camTileBeginY + sumy)][(int)(camTileBeginX + sumx)];
				sumx+= stepX;
			}
			sumx = 0;
			sumy += stepY;
		}
		
		if(!shouldDrawInside){
			/*float f = getNightFactor();
			for(int i = 0; i < h; i++){
				for(int j = 0; j < w; j++){
					data[i][j] = Screen.nightFilter(data[i][j],f);
				}
			}*/
		}
		
		return data;
	}
	
	public void breakPoint(){
		System.out.print("");
	}
	
	public void tick(){
		handleSaveLoadInput();
		
		if(nightBoost && time % Village.DAYCYCLE_DURATION <30){
			nightBoost = false;
			setCutscene(new WakeUpCutscene(core, player));
		}
		if(Controller.input[KeyEvent.VK_B]){
			breakPoint();
		}
		
		if(!core.wavIsPlaying()&& Sprite.RAND.nextInt(Village.DAYCYCLE_DURATION*2) == 0){
			if(time%Village.DAYCYCLE_DURATION < 2*Village.DAYCYCLE_DURATION/3){
				core.playWavSound(Core.WAV_MAIN_THEME_DAY);
			}else{
				core.playWavSound(Core.WAV_MAIN_THEME_NIGHT);
			}
		}
		
		if(inCutscene){
			
			currScene.tick();
			if(currScene.isFinished() || Controller.input[KeyEvent.VK_U]){
				//System.out.println("CurrSceneFinished: " + currScene.isFinished());
				currScene.close();
				inCutscene = false;
				currScene = null;
			}
		}
		for(int i = 0; i < H; i++){
			for(int j = 0; j <W; j++){
				if(sprites[i][j] != null)
					sprites[i][j].tick();
				if(nonBlocks[i][j] != null){
					nonBlocks[i][j].tick();
				}
			}
		}
		//player.tick();
		
		handlePlayerInput();
		centerCameraOnPlayer();
		setDrawMode();
		time++;
	}
	
	public void handleSaveLoadInput(){
		if(Controller.input[KeyEvent.VK_S] && time - lastSave > 1*60){
			if(Controller.input[KeyEvent.VK_1]){
				core.saveVillage(1);
			}else if(Controller.input[KeyEvent.VK_2]){
				core.saveVillage(2);
			}else if(Controller.input[KeyEvent.VK_3]){
				core.saveVillage(3);
			}
		}else if(Controller.input[KeyEvent.VK_L]){
			if(Controller.input[KeyEvent.VK_1]){
				core.loadImage(1);
			}else if(Controller.input[KeyEvent.VK_2]){
				core.loadImage(2);
			}else if(Controller.input[KeyEvent.VK_3]){
				core.loadImage(3);
			}
		}
	}
	
	public void setDrawMode(){
		if(ownedBy[player.getY()][player.getX()] == null){
			shouldDrawInside = false;
			currBuilding = null;
		}else{
			shouldDrawInside = true;
			currBuilding = ownedBy[player.getY()][player.getX()];
		}
	}
	
	public long getTime(){
		return time;
	}
	
	public void centerCameraOnPlayer(){
		//camX = villagers[villagerPermutation[numVillagers-4]].getExactX() - Screen.W/2/Tile.WIDTH ;
		//camY = villagers[villagerPermutation[numVillagers-4]].getExactY() - Screen.H/2/Tile.HEIGHT;
		camX = player.getExactX() - Screen.W/2/Tile.WIDTH ;
		camY = player.getExactY() - Screen.H/2/Tile.HEIGHT;
		
		//System.out.println(player.getExactX() + ", " + player.getExactY());
	}
	
	public void handlePlayerInput(){
		int dir = -1;
		if(Controller.input[KeyEvent.VK_UP]){
			dir = Movable.UP;
		}
		else if(Controller.input[KeyEvent.VK_LEFT]){
			dir = Movable.LEFT;
		}
		else if(Controller.input[KeyEvent.VK_DOWN]){
			dir = Movable.DOWN;
		}
		else if(Controller.input[KeyEvent.VK_RIGHT]){
			dir = Movable.RIGHT;
		}
		if(dir != -1 && !player.isPartOfCutscene){
			player.tryStartMoving(dir);
		}
		
		/*if(getTime()% 60 == 0){
			System.out.println(player.getX() + ", " + player.getY());
		}*/
	}
	
	
	
	public void addStructure(Structure s){
		for(int i = 0; i < s.getH(); i++){
			for(int j = 0; j < s.getW(); j++){
				if(s.getBlockAt(j, i) != null){
					addSprite(s.getBlockAt(j, i));
				}else if(!(getSpriteAt(s.getX() + j, s.getY() + i) instanceof TalkativeSprite)){
					sprites[s.getY() + i][s.getX() + j] = null;
				}
				if(s.getNonBlockAt(j, i) != null){
					addNonBlock(s.getNonBlockAt(j, i));
				}else{
					nonBlocks[s.getY() + i][s.getX() + j] = null;
				}
				tileCovers[s.getY() + i][s.getX() + j] = null;
			}
		}
	}
	
	public void addArea(Area area){
		for(int i = 0; i < area.getH(); i++){
			for(int j = 0; j < area.getW(); j++){
				setTileAt(area.getTileAt(j, i), area.getX() + j, area.getY() + i);
			}
		}
	}
	
	public void addBuilding(Building b){
		addStructure(b);
		for(int i = 0; i < b.getH(); i++){
			for(int j = 0; j < b.getW(); j++){
				setTileAt(b.getTileAt(j, i), b.getX() + j, b.getY() + i);
				setNonBlockAt(b.getNonBlockAt(j, i), b.getX() + j, b.getY() + i);
				closed[b.getY() + i][b.getX() + j] = b.isClosedAt(j,i);
				ownedBy[b.getY() + i][b.getX() + j] = b;
			}
		}
	}
	
	public void setTileAt(int t, int x, int y){
		tiles[y][x] = t;
	}
	
	public void setNonBlockAt(NonBlock nb, int x, int y){
		nonBlocks[y][x] = nb;
	}
	
	public void setSpriteAt(Sprite s, int x, int y){
		sprites[y][x] = s;
	}
	public void addSprite(Sprite sprite){
		sprites[sprite.getY()][sprite.getX()] = sprite;
	}
	
	public void addNonBlock(NonBlock nb){
		nonBlocks[nb.getY()][nb.getX()] = nb;
	}
	
	public void addTileCover(TileCover tc){
		tileCovers[tc.getY()][tc.getX()] = tc;
	}
	
	public boolean isEmpty(int x, int y){
		return sprites[y][x] == null;
	}
	
	public void notifyMove(Movable m){
		sprites[m.getY()][m.getX()] = m;
		sprites[m.getY() - m.getMoveDY()][m.getX() - m.getMoveDX()] = null;
	}
	
	public Sprite getSpriteAt(int x, int y){
		return sprites[y][x];
	}
	
	public NonBlock getNonBlockAt(int x, int y){
		return nonBlocks[y][x];
	}
	
	public void switchPlaces(Movable s, Movable t){
		sprites[s.getY()][s.getX()] = null;
		t.tryStartMoving(t.getMovingDirection());
		sprites[t.getY()][t.getX()] = s;
		s.tryStartMoving(s.getMovingDirection());
		sprites[t.getY()][t.getX()] = t;
	}
	
	public void moveSpriteTo(Sprite s, int x, int y){
		if(getSpriteAt(x, y) != null && getSpriteAt(x,y ) != s){
			moveSpriteTo(getSpriteAt(x, y), x - 1, y);
		}
		sprites[s.getY()][s.getX()] = null;
		s.setX(x);
		s.setY(y);
		sprites[y][x] = s;
	}
	
	public float getNightFactor(){
		if(!shouldDrawInside){
			return Math.max( Math.min((float)(0.65+ 1*Math.sin((time%DAYCYCLE_DURATION)*2*Math.PI/DAYCYCLE_DURATION)), 1), 0);
		}else{
			return 1;
		}
	}
	
	public boolean isOwnedBy(int x, int y, Building b){
		return ownedBy[y][x] == b;
	}
	
	public Building ownedBy(int x, int y){
		return ownedBy[y][x];
	}
	
	public Citizen getCitizen(int i){
		return citizenList[i];
	}
	
	public int getNumCitizens(){
		return numVillagers + 1;
	}
	
	public int getHouseSpread(){
		return houseSpread;
	}
	
	public House getHouseAt(int x, int y){
		return townGrid[y][x];
	}
	
	public int getTownStartX(){
		return townGridMiddleX - houseSpread*townGridSide/2; 
	}
	
	public int getTownStartY(){
		return townGridMiddleY - houseSpread*townGridSide/2;
	}
	
	public int getTownWidth(){
		return houseSpread*townGridSide;
	}
	
	public int getTownMiddleX(){
		return townGridMiddleX;
	}
	
	public int getTownMiddleY(){
		return townGridMiddleY;
	}
	
	public int getTownHeight(){
		return houseSpread*townGridSide;
	}
	
	public int getHouseSide(){
		return houseSide;
	}
	
	public int getTownGridSide(){
		return townGridSide;
	}
	
	public int getTileAt(int x, int y){
		return tiles[y][x];
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public boolean contains(int x, int y){
		return x >= getTownStartX() 
				&& y >= getTownStartY() 
				&& x < getTownStartX() + getTownWidth() - houseSpread + houseSide 
				&& y < getTownStartY() + getTownHeight();
	}
	
	public void setCore(Core core){
		this.core = core;
	}
	
	public int[] getPermutation(int n){
		int[] perm = new int[n];
		
		for(int i = 0; i< n; i++){
			perm[i] = i;
		}
		
		for(int i = 0; i < n-1; i++){
			int u = Sprite.RAND.nextInt(n - i);
			int t = perm[u];
			perm[u] = perm[i];
			perm[i] = t;
		}
		
		return perm;
	}
	
	public boolean isInSleepMode(){
		return nightBoost;
	}
	
	public void turnOnSleepMode(){
		if(nightBoost == false){
			timesSlept++;
		}
		nightBoost = true;
		
		core.playWavSound(Core.WAV_SLEEP);
		time = (time + Village.DAYCYCLE_DURATION)/Village.DAYCYCLE_DURATION*Village.DAYCYCLE_DURATION - 5*60;
	}
	
	public int getTimesSlept(){
		return timesSlept;
	}
	
	public void setCutscene(Cutscene cutscene){
		this.currScene = cutscene;
		inCutscene = true;
	}
	
	public Core getCore(){
		return core;
	}
	
	public Shadow getShadow(){
		return shadow;
	}

	public void setTileCoverAt(TileCover object, int x, int y) {
		tileCovers[y][x] = object;
		
	}

	public void createAreaAroundWoodcutterCabin(House cabin) {
		int c = 0;
		for(int i = cabin.getY() - 10; i< cabin.getY() + 15; i++){
			for(int j = cabin.getX() - 10; j < cabin.getX() + 15; j++){
				if(Sprite.RAND.nextInt(10) == 0&& getSpriteAt(j, i) == null && ownedBy[i][j] == null){
					tileCovers[i][j] = new WoodTileCover(j, i, this);
					c++;
				}
			}
		}
		int xb = 0, yb = 0;
		
		for(; yb < 25; yb++){
			for(;xb < 25; xb++){
				if(c>= 10){
					break;
				}
				int j = cabin.getX() - 10 + xb;
				int i = cabin.getY() - 10 + yb;
				if(getSpriteAt(j, i) == null && ownedBy[i][j] == null){
					tileCovers[i][j] = new WoodTileCover(j, i, this);
					c++;
				}
			}
			if(c >= 10){
				break;
			}
		}
		
	}

	public TileCover getTileCoverAt(int x, int y) {
		return tileCovers[y][x];
	}
	
}
