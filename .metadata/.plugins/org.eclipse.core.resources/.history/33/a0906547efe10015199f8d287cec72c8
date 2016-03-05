package priv.hkon.theseq.sprites;

import java.util.LinkedList;

import priv.hkon.theseq.world.Village;

public abstract class Movable extends Sprite implements Runnable{
	
	public static final int LEFT = 0;
	public static final int DOWN = 1;
	public static final int RIGHT =2;
	public static final int UP = 3;
	
	public static final int[] dx = {-1, 0, 1, 0};
	public static final int[] dy= {0, 1, 0, -1};
	
	float movedFraction = 1, moveSpeed = 0.05f;
	boolean moving = false;
	int movingDirection = 0;
	
	boolean requestingSwitch = false;
	
	boolean hasPath = false;
	boolean isPlanningPath;
	byte[] path;
	int pathIndex = 0;
	int targetX, targetY;
	
	public Movable(int x, int y, Village v){
		super(x, y, v);
	}
	
	public boolean tryStartMoving(int dir){
		if(moving){
			return false;
		}
		movingDirection = dir;
		if(!(village.isEmpty(x + dx[dir], y + dy[dir]))){
			if(village.getSpriteAt(x + dx[dir], y + dy[dir]) instanceof Movable){
				Movable m = (Movable)(village.getSpriteAt(x + dx[dir], y + dy[dir]));
				if(m.getMovingDirection() == (movingDirection + 2)% 4){
					if(m.isRequestingSwitch()){
						village.switchPlaces(this, m);
					}else{
						requestingSwitch = true;
					}
				}
			}
			return false;
		}
		requestingSwitch = false;
		movedFraction = 0;
		
		x += dx[movingDirection];
		y += dy[movingDirection];
		village.notifyMove(this);
		moving = true;
		return true;
	}
	
	public void startPathTo(int x, int y){
		targetX = x;
		targetY = y;
		try{
			new Thread(this).start();
		}catch(Exception e){}
	}
	
	public int getMoveDX(){
		return dx[movingDirection];
	}
	
	public int getMoveDY(){
		return dy[movingDirection];
	}
	
	public float getMovedX(){
		return (-getMoveDX()*(1-movedFraction));
	}
	
	public float getMovedY(){
		return (-getMoveDY()*(1-movedFraction));
	}
	
	public float getMovedFraction(){
		return movedFraction;
	}
	
	public float getExactX(){
		return x + getMovedX();
	}
	
	public float getExactY(){
		return y + getMovedY();
	}
	
	public void run(){
		
		isPlanningPath = true;
		for(int i = 0; i < 1; i++){
			hasPath = djikstra(targetX, targetY);
			if(hasPath){
				pathIndex = 0;
				break;
			}
		}
		
		isPlanningPath = false;
		try{
		Thread.currentThread().join();
		}catch(Exception e){}
	}
	
	
	public boolean tick(){ //return whether the turn should be ended or not
		super.tick();
		if(isPlanningPath){
			return true;
		}
		if(moving){
			movedFraction += moveSpeed;
			if(movedFraction >= 1){
				movedFraction = 1;
				moving = false;
				//return false;
			}
			return true;
		}
		else if (hasPath){
			followPath();
			return true;
		}
		return false;
	}
	
	public boolean followPath(){
		
		if(path.length <= pathIndex){
			hasPath = false;
			return false;
		}
		
		int nx = x + dx[path[pathIndex]];
		int ny = y + dy[path[pathIndex]];
		
		movingDirection = path[pathIndex];
		
		if(tryStartMoving(path[pathIndex])){
			pathIndex++;
			if(pathIndex == path.length - 1){
				hasPath = false;
				
			}
			return true;
		}else if(village.getSpriteAt(nx, ny).isStationary()){
			startPathTo(targetX,targetY);
		}
		return false;
	}

	public int getMovingDirection(){
		return movingDirection;
	}
	
	public boolean isRequestingSwitch(){
		return requestingSwitch;
	}
	
	public boolean isStationary(){
		return !moving&&!isRequestingSwitch();
	}
	
	public boolean djikstra(int x, int y){
		byte[][] directions = new byte[Village.H][Village.W];
		for(int i = 0; i < Village.H; i++){
			for(int j = 0; j < Village.W; j++){
				directions[i][j] = -2;
			}
		}
		int fd = 0;
		directions[this.y][this.x] = -1;
		//PriorityQueue<Pair> pq = new PriorityQueue<Pair>(10, new PairComparator(x, y));
		LinkedList<Pair> pq = new LinkedList<Pair>();
		boolean foundTarget = false;
		pq.add(new Pair(this.x, this.y));
		while(!(pq.isEmpty())&&!foundTarget){
			
			Pair p = pq.poll();
			
			if(p.x == x && p.y == y){
				foundTarget = true;
				fd = p.dist;
				break;
			}
			
			for(int i = 0; i < 4; i++){
				int nx = p.x + dx[i];
				int ny = p.y + dy[i];
				if(nx < 0 || nx >= Village.W || ny < 0 || ny >= Village.H || directions[ny][nx] != -2){
					continue;
				}
				if(village.getSpriteAt(nx, ny) != null){
					if(!(p.dist > 0&&!village.getSpriteAt(nx, ny).isStationary())){
						continue;
					}
				}
				pq.add(new Pair(nx, ny, p.dist + 1));
				directions[ny][nx] = (byte)(i);
				
			}
		}
		if(!foundTarget){
			return false;
		}
		
		path = new byte[fd];
		Pair np = new Pair(x, y);
		byte dd;
		
		for(int i = 0; i < fd; i++){
			
			dd = directions[np.y][np.x];
			
			path[fd - i - 1] = dd;
			np = new Pair(np.x - dx[dd], np.y - dy[dd]);
		}
		return true;
		
	}
	
	class Pair{
		int x, y;
		int dist;
		Pair(int nx, int ny){
			x = nx;
			y = ny;
			dist = 0;
		}
		
		Pair(int nx, int ny, int d){
			this(nx, ny);
			dist = d;
		}
	}
	
	public void turnTowards(int dir){
		if(moving == false){
			movingDirection = dir;
		}
	}
	
	int getDirectionTo(int nx, int ny){
		int dx = nx - x;
		int dy = ny - y;
		
		if(Math.abs(dx) > Math.abs(dy)){
			if(dx > 0){
				return RIGHT;
			}else {
				return LEFT;
			}
		}
		if(dy > 0){
			return DOWN;
		}else{
			return UP;
		}
	}
	
	int getDirectionTo(Sprite s){
		return getDirectionTo(s.getX(), s.getY());
	}
	
	void strollTownGrid(){
		
		if(!village.contains(x,y)){
			startPathTo(village.getTownStartX() + village.getTownWidth()/2, village.getTownStartY() + village.getTownHeight());
			return;
		}

		if((x - village.getTownStartX())%village.getHouseSpread() < village.getHouseSide()){
			if(x < village.getTownStartX() + village.getTownWidth()/2){
				startPathTo(village.getTownWidth() + (x - village.getTownWidth() + village.getHouseSpread())/village.getHouseSide()*village.getHouseSide(), y);
			}else{
				startPathTo(village.getTownWidth() + (x - village.getTownWidth())/village.getHouseSide()*village.getHouseSide(), y);
			}
			return;
		}else if((y - village.getTownStartY())% village.getHouseSpread() < village.getHouseSide()){
			if(y < village.getTownStartY() + village.getTownHeight()/2){
				startPathTo(village.getTownHeight() + (y - village.getTownHeight() + village.getHouseSpread())/village.getHouseSide()*village.getHouseSide(), y);
			}else{
				startPathTo(village.getTownHeight() + (y - village.getTownHeight())/village.getHouseSide()*village.getHouseSide(), y);
			}
			return;
		}
		int n = RAND.nextInt(4);
		int i = 0;
		while((!village.contains(x + dx[n]*village.getHouseSpread(), y + dy[n]*village.getHouseSpread())) ||
				village.ownedBy(x + dx[n]*village.getHouseSpread(), y + dy[n]*village.getHouseSpread()) != null){
			//System.out.println((x + dx[n]*village.getHouseSpread())+ ", " + (y + dy[n]*village.getHouseSpread()) + " was not ok");
			n = (n + 1)%4;
			i++;
			if(i == 4){
				//System.out.println("Couldn't stroll");
				return;
			}
			
		}
		startPathTo(x + dx[n]*village.getHouseSpread(), y + dy[n]*village.getHouseSpread());
	}
	
	/*class PairComparator implements Comparator<Pair>{
	
		int x; int y;
		PairComparator(int rx, int ry){
			x = rx;
			y = ry;
		}
		
		@Override
		public int compare(Pair o1, Pair o2) {
			if(Math.abs(o1.x - x) + Math.abs(o1.y - y) < Math.abs(o2.x - x) + Math.abs(o2.y - y)){
				return -1;
			}
			return 1;
		}
		
	}*/
	
	

}
