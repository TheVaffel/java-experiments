package priv.hkon.theseq.world;

import java.awt.event.KeyEvent;
import java.util.Random;

import priv.hkon.theseq.main.Controller;
import priv.hkon.theseq.main.Screen;
import priv.hkon.theseq.sprites.Citizen;
import priv.hkon.theseq.sprites.DialogBubble;
import priv.hkon.theseq.sprites.Mayor;
import priv.hkon.theseq.sprites.Movable;
import priv.hkon.theseq.sprites.NonBlock;
import priv.hkon.theseq.sprites.Player;
import priv.hkon.theseq.sprites.Sprite;
import priv.hkon.theseq.sprites.Villager;

public class Village {
	
	public Player player;
	public static final int W = 1000;
	public static final int H = 1000;
	
	Random random;
	
	int[][] tiles = new int[H][W];
	Sprite[][] sprites = new Sprite[H][W];
	NonBlock[][] nonBlocks = new NonBlock[H][W];
	Building[][] ownedBy = new Building[H][W];
	
	Citizen[] citizenList;
	
	boolean[][] closed = new boolean[H][W];
	boolean shouldDrawInside = false;
	Building currBuilding = null; 
	
	int numVillagers = 40;
	Villager[] villagers = new Villager[numVillagers];
	
	
	int houseSide = 10;
	int houseSpread = 15;
	int townGridMiddleX = W/2;
	int townGridMiddleY = H/2;
	
	int numHouses = (int)(Math.pow(Math.ceil(Math.sqrt(numVillagers)), 2));
	int townGridSide = (int)(Math.ceil(Math.sqrt(numHouses)));
	
	House[][] townGrid = new House[townGridSide][townGridSide];
	
	public double camX= -100, camY = -100;
	
	public double camSpeed = 0.1;
	double tilesPerPixelX = 1.0/Tile.WIDTH;
	double tilesPerPixelY = 1.0/Tile.HEIGHT;
	
	long time = 0;
	
	int dayCycleDuration = 60*60*20;
	
	public Village(){
		random = new Random();
		
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
			Road r = new Road(townGridStartX - houseSpread + houseSide + 1, townGridStartY+ houseSpread*i +houseSide + 1, (townGridSide-1)*houseSpread + houseSide, 3, this);
			addArea(r);
			r = new Road(townGridStartX + houseSpread*(i - 1) + houseSide + 1, townGridStartY, 3, (townGridSide - 1)*houseSpread + houseSide, this);
			addArea(r);
		}
		
		citizenList = new Citizen[numVillagers + 1];
		
		player = new Player(townGridStartX + houseSpread - 1, townGridStartY + houseSpread - 1, this, numVillagers);
		addSprite(player); 
		for(int i = 0; i< numVillagers - 1; i++){
			villagers[i]= new Villager(townGridStartX + i, townGridStartY - 5, this, townGrid[i/townGridSide][i%townGridSide], i);
			addSprite(villagers[i]);
		}
		
		villagers[numVillagers - 1] = new Mayor(player.getX() - 2, player.getY() - 2, this, townGrid[0][0], numVillagers - 1);
		addSprite(villagers[numVillagers - 1]);
		villagers[0].debug = true;
		
	}
	
	public int[][] getScreenData(int w, int h){
		int[][] data = new int[h][w];
		
		int tilesInWidth = (int)Math.ceil(tilesPerPixelX*w) + 1;
		int tilesInHeight = (int) Math.ceil(tilesPerPixelY*h) + 1;
		
		int[][] rawdata = new int[tilesInHeight*Tile.HEIGHT][tilesInWidth*Tile.WIDTH];
		int beginTileX = (int)(Math.floor(camX));
		int beginTileY = (int)(Math.floor(camY));
		
		int initY = Math.max(-beginTileY, 0);
		int initX = Math.max(-beginTileX, 0);

		for(int i = initY; i < tilesInHeight ; i++){
			for(int j = initX; j < tilesInWidth; j++){
				if((!closed[beginTileY + i][beginTileX + j] && !shouldDrawInside) || isOwnedBy(beginTileX + j, beginTileY + i, currBuilding)){
					Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, Tile.getData(tiles[beginTileY + i][beginTileX+ j]), Tile.WIDTH, Tile.HEIGHT, j*Tile.WIDTH, i*Tile.HEIGHT);
				}else{
					Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, Tile.getData(Tile.TYPE_EMPTY), Tile.WIDTH, Tile.HEIGHT, j*Tile.WIDTH, i*Tile.HEIGHT);
				}
			}
		}
		
		for(int i = initY; i < tilesInHeight +1; i++){
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
						if(sprites[beginTileY + i][beginTileX + j].shouldDrawDialog()){
							DialogBubble d = sprites[beginTileY + i][beginTileX + j].getDialog();
							Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, d.getData(), d.getWidth(), d.getHeight(), j*Tile.WIDTH + dx + d.getOffsetX(), i*Tile.HEIGHT + dy + d.getOffsetY());
						}
							
					}
					if(nonBlocks[beginTileY + i][beginTileX + j] != null){
						Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, nonBlocks[beginTileY + i][beginTileX+ j].getData(), Sprite.W, Sprite.H, j*Tile.WIDTH, (i + 1)*Tile.HEIGHT - Sprite.H - Sprite.DRAW_OFFSET_Y);
					}
					
					
				
				}
			}
		}
		
		for(int i = initY; i < tilesInHeight +1; i++){
			for(int j = initX; j < tilesInWidth; j++){
				int dx = 0;
				int dy = 0;
				if(((!closed[beginTileY + i][beginTileX + j] && !shouldDrawInside) || (isOwnedBy(beginTileX + j, beginTileY + i, currBuilding)))){
					
					if(sprites[beginTileY + i][beginTileX + j] != null){
						if(sprites[beginTileY + i][beginTileX + j] instanceof Movable){
							dx = (int)((((Movable)(sprites[beginTileY + i][beginTileX + j])).getExactX()-sprites[beginTileY + i][beginTileX + j].getX())*(Tile.WIDTH));
							dy = (int)(((Movable)(sprites[beginTileY + i][beginTileX + j])).getMovedY()*Tile.HEIGHT);
						}
						if(sprites[beginTileY + i][beginTileX + j].shouldDrawDialog()){
							DialogBubble d = sprites[beginTileY + i][beginTileX + j].getDialog();
							Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, d.getData(), d.getWidth(), d.getHeight(), j*Tile.WIDTH + dx + d.getOffsetX(), i*Tile.HEIGHT + dy + d.getOffsetY());
						}
					}
				}
			}
		}
		
		/*
		DialogBubble db = new DialogBubble(player);
		db.setString("Hello!!");
		Screen.draw(rawdata, tilesInWidth*Tile.WIDTH, tilesInHeight*Tile.HEIGHT, db.getData(), db.getWidth(), db.getHeight(), (int)((player.getExactX() - beginTileX)*Tile.WIDTH) + DialogBubble.OFFSET_X, (int)((player.getExactY() - beginTileY)*Tile.HEIGHT) + DialogBubble.OFFSET_Y);*/
		
		int camTileBeginX = (int)((camX - Math.floor(camX))*Tile.WIDTH);
		
		int camTileBeginY = (int)((camY - Math.floor(camY))*Tile.HEIGHT);
		
		double stepY = 1.0/(tilesPerPixelY*Tile.HEIGHT);
		double stepX = 1.0/(tilesPerPixelX*Tile.WIDTH);
		double sumy = 0;
		double sumx = 0;
		for(int i = 0; i<h ; i++ ){
			for(int j = 0; j<w; j++){
				if(camY*Tile.HEIGHT + sumy< 0 || camX*Tile.WIDTH + sumx< 0
						|| camX + sumx >= W || camY + sumy >= H){
					
					data[i][j] = Sprite.getColor(0, 0, 0);
				}else{
				
					data[i][j] = rawdata[(int)(camTileBeginY + sumy)][(int)(camTileBeginX + sumx)];
				}
				sumx+= stepX;
			}
			sumx = 0;
			sumy += stepY;
		}
		
		if(!shouldDrawInside){
			float f = getNightFactor();
			for(int i = 0; i < h; i++){
				for(int j = 0; j < w; j++){
					data[i][j] = Screen.nightFilter(data[i][j],f);
				}
			}
		}
		
		return data;
	}
	
	public void tick(){
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
		if(dir != -1)
			player.tryStartMoving(dir);
		
		/*if(getTime()% 60 == 0){
			System.out.println(player.getX() + ", " + player.getY());
		}*/
	}
	
	
	
	public void addStructure(Structure s){
		for(int i = 0; i < s.getH(); i++){
			for(int j = 0; j < s.getW(); j++){
				if(s.getBlockAt(j, i) != null)
					addSprite(s.getBlockAt(j, i));
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
				closed[b.y + i][b.x + j] = b.closed[i][j];
				ownedBy[b.y + i][b.x + j] = b;
			}
		}
	}
	
	public void setTileAt(int t, int x, int y){
		tiles[y][x] = t;
	}
	
	public void setNonBlockAt(NonBlock nb, int x, int y){
		nonBlocks[y][x] = nb;
	}
	
	public void addSprite(Sprite sprite){
		sprites[sprite.getY()][sprite.getX()] = sprite;
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
	
	public void switchPlaces(Movable s, Movable t){
		sprites[s.getY()][s.getX()] = null;
		t.tryStartMoving(t.getMovingDirection());
		sprites[t.getY()][t.getX()] = s;
		s.tryStartMoving(s.getMovingDirection());
		sprites[t.getY()][t.getX()] = t;
	}
	
	public float getNightFactor(){
		return Math.max( Math.min((float)(0.65+ 1*Math.sin((time%dayCycleDuration)*2*Math.PI/dayCycleDuration)), 1), 0);
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
	
	public int getTownStartX(){
		return townGridMiddleX - houseSpread*townGridSide/2; 
	}
	
	public int getTownStartY(){
		return townGridMiddleY - houseSpread*townGridSide/2;
	}
	
	public int getTownWidth(){
		return houseSpread*townGridSide;
	}
	
	public int getTownHeight(){
		return houseSpread*townGridSide;
	}
	
	public int getHouseSide(){
		return houseSide;
	}
	
	public boolean contains(int x, int y){
		return x >= getTownStartX() && y >= getTownStartY() && x < getTownStartX() + getTownWidth() && y < getTownStartY() + getTownWidth();
	}
	
}
