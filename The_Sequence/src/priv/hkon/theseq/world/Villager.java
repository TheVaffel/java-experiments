package priv.hkon.theseq.world;

public class Villager extends Movable {
	
	House home;
	int timeSinceHome;
	
	public static final int HEAD_OFFSET_Y = 5;
	public static final int HEAD_OFFSET_X = W/2;
	
	public static final int HEAD_SQ_RADIUS = W*W/9 - 1;
	
	//TODO: add some personality!

	public Villager(int x, int y, Village v, House h) {
		super(x, y, v);
		home = h;
	}

	@Override
	public void makeData() {
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int d = (j-W/2)*(j-W/2) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y);
				int c = 255;
				if(Math.abs(j - W/2) < ((float)i - HEAD_OFFSET_Y)/3){
					int p = (int) (100*Math.exp(-0.5*(int)Math.abs((j - W/2 - (float)(i-HEAD_OFFSET_Y)/5))));
					data[i][j] = Sprite.getColor(p, 20 + p, 80 + p);
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
		}else {
			
			if(timeSinceHome > 60*3){
				setDialogString("Aah, finally home!");
				showDialog(60*3);
			}
			timeSinceHome = 0;
		}
		
		if(showDialog){
			
			timeSinceDialogReset++;
			if(timeSinceDialogReset >= dialogDuration){
				hideDialog();
			}
		}
		
		data = animationFrames[movingDirection][0];
		
		if(super.tick()){
			return true;
		}
		if(!isHome()){
			startGoHome();
			return true;
		}
		return false;
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
		int ringColor = Sprite.getColor(128, 255, 128);
		
		for(int i = 0; i < H; i++){
			for(int j = 0; j < W; j++){
				int d = (int)(2*Math.sqrt((j - W/2)*(j - W/2) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y)));
				if(d == 4){
					animationFrames[DOWN][0][i][j] = ringColor;
				}
				
				d = (int)(2*Math.sqrt((j)*(j) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y)));
				if( d == 8){
					animationFrames[LEFT][0][i][j] = ringColor;
				}
				
				d = (int)(2*Math.sqrt((j -W)*(j - W) + (i - HEAD_OFFSET_Y)*(i - HEAD_OFFSET_Y)));
				if( d == 8){
					animationFrames[RIGHT][0][i][j] = ringColor;
				}
			}
		}
	}

}
