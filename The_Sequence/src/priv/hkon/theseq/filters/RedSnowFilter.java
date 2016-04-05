package priv.hkon.theseq.filters;

import priv.hkon.theseq.sprites.Sprite;

public class RedSnowFilter extends Filter {

	public RedSnowFilter() {
	}
	
	public void apply(int[] data){
		for(int i = 0; i < data.length; i++){
			int r = (data[i] >> 16) & 255;
			float d = Sprite.RAND.nextFloat();
			
			r = Math.min(255, (int)((r + 20)*(1 + d)));
			
			data[i] = (data[i]& ~(255<<16))| r<< 16;
		}
	}

}
