package priv.hkon.theseq.filters;

public class CombinedFilter extends Filter {
	
	Filter[] fs;

	public CombinedFilter(Filter f1, Filter f2) {
		fs = new Filter[2];
		fs[0] = f1;
		fs[1] = f2;
	}
	
	public CombinedFilter(Filter f1, Filter f2, Filter f3){
		fs = new Filter[3];
		fs[0] = f1;
		fs[1] = f2;
		fs[2] = f3;
	}
	
	public void apply(int[] data){
		for(int i = 0; i< fs.length; i++){
			fs[i].apply(data);
		}
	}
	
	

}
