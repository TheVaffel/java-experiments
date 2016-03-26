package priv.hkon.theseq.filters;

public class DarkenFilter extends Filter {
	
	public int darkness = 0;
	
	public DarkenFilter(int d){
		darkness = d;
	}
	
	@Override
	public void apply(int[] data) {
		float a  = 1  - ((float)darkness)/255;
		for(int i = 0; i < data.length; i++){
				int rgb = data[i];
				data[i] = (255 << 24) | ((int)((rgb & (255 << 16))*a) & (255 << 16)) |
						((int)((rgb & (255 << 8))*a) & (255 << 8)) |
						((int)((rgb & 255)*a) & 255);
		}

	}

}
