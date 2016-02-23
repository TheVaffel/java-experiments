package priv.hkon.theseq.world;

public abstract class Structure extends Area{
	
	Block[][] blocks;
	NonBlock[][] nonBlocks;
	
	
	public Structure(int nx, int ny, int nw, int nh, Village v){
		super(nx, ny, nw, nh, v);
		
		blocks = new Block[h][w];
		nonBlocks = new NonBlock[h][w];
		makeBlocks(); 
	}
	
	

	public Block getBlockAt(int j, int i){
		return blocks[i][j];
	}
	
	public abstract void makeBlocks();// including NonBlocks
	
}
