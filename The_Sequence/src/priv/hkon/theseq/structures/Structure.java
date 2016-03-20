package priv.hkon.theseq.structures;

import priv.hkon.theseq.blocks.Block;
import priv.hkon.theseq.nonblocks.NonBlock;
import priv.hkon.theseq.world.Area;
import priv.hkon.theseq.world.Village;

public abstract class Structure extends Area{

	private static final long serialVersionUID = 363205869190954961L;
	Block[][] blocks;
	NonBlock[][] nonBlocks;
	
	
	public Structure(int nx, int ny, int nw, int nh, Village v){
		super(nx, ny, nw, nh, v);
		
		blocks = new Block[nh][nw];
		nonBlocks = new NonBlock[nh][nw];
		makeBlocks();
	}
	
	

	public Block getBlockAt(int j, int i){
		return blocks[i][j];
	}
	
	public NonBlock getNonBlockAt(int j, int i){
		return nonBlocks[i][j];
	}
	
	public abstract void makeBlocks();// including NonBlocks
	
	public void setX(int nx){
		x = nx;
	}
	
	public void setY(int ny){
		y = ny;
	}
	
	public abstract String getName();
}
