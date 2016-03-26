package priv.hkon.theseq.filters;

public class CombinedFilter extends Filter {
	
	Filter f1, f2;

	public CombinedFilter(Filter f1, Filter f2) {
		this.f1 = f1;
		this.f2 = f2;
	}
	
	public void apply(int[] data){
		f1.apply(data);
		f2.apply(data);
	}
	
	

}
