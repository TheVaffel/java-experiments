package priv.hkon.theseq.filters;

import priv.hkon.theseq.sprites.Sprite;

public class ColoredSquaresFilter extends Filter {
	
	public int dist = 0;
	public boolean out = false;
	int w, h;

	public ColoredSquaresFilter( int i, int w, int h) {
		this.w = w;
		this.h = h;
		dist = i;
	}
	
	@Override
	public void apply(int[] data){
		for(int i = 0;i < h/5; i++){
			for(int j = 0; j < w/5; j++){
				int rgb = Sprite.getColor(Sprite.getRandomInt(i, j)%256, Sprite.getRandomInt(i)%256, Sprite.getRandomInt(j)%256);
				
				if(out ^ Math.abs(i - h/2/5) + Math.abs(j - w/2/5) < dist*5){
					for(int u = 0; u < 5; u++){
						for( int v = 0; v < 5; v++){
							data[i* w*5 + j*5 + u*w + v] = rgb;
						}
					}
				}
			}
		}
	}

}
