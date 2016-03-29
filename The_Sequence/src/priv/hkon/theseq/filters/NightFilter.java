package priv.hkon.theseq.filters;

public class NightFilter extends Filter {
	
	public float factor;

	public NightFilter(float f) {
		factor = f;
	}
	
	public void apply(int[] data){
		for(int i = 0; i < data.length; i++){
			int col = data[i];
			int r = (col >>16)&255;
			int g = (col >>8)&255;
			int b = (col)&255;
			
			int av = (r +b + g)/3;
			
			r = (int)Math.max(r*factor, r*0.2);
			g = (int)Math.max(g*factor, g*0.3);
			b = (int)Math.max(b*factor, av*0.3);
			
			data[i] = (r<<16) | (g<<8) | b | (255 << 24);
		}
	}

}
