package priv.hkon.theseq.world;

import priv.hkon.theseq.main.Screen;

public class DialogBubble {
	
	String[] str;
	Sprite sprite;
	int[][] data;
	int width;
	int numLines;
	
	public static final int DEFAULT_WIDTH = 80;
	
	public static final int OFFSET_X = Tile.WIDTH - 2;
	public static final int BASE_OFFSET_Y = -Tile.HEIGHT;
	
	public static final int ROW_HEIGHT = Screen.FONT_HEIGHT + 2;

	public DialogBubble(Sprite sprite) {
		this.sprite = sprite;
		width = DEFAULT_WIDTH;
	}

	public void setString(String s){
		String st[] = s.split(" ");
		StringBuilder sb = new StringBuilder();
		int currrow = 0, numrows = 1;
		int i = 0;
		int charactersInWidth = DEFAULT_WIDTH/Screen.FONT_WIDTH;
		while(i < st.length){
			if(st[i].length() <= charactersInWidth - currrow){
				currrow += st[i].length() + 1;
				sb.append(st[i] + " ");
			}else{
				if(st[i].length()<= charactersInWidth){
					numrows++;
					currrow = st[i].length() + 1;
					sb.append("~" + st[i] + " ");
				}else{
					if(numrows != 1){
						numrows++;
						sb.append("~");
					}
					currrow = charactersInWidth;
					sb.append(st[i].substring(0, charactersInWidth - 1) + "-");
					st[i] = st[i].substring(charactersInWidth - 1);
					i--;
					numrows++;
				}
			}	
			i++;
		}
		
		
		
		st = sb.toString().split("~");
		numrows = st.length;
		int maxw = 0;
		for(i = 0; i< st.length; i++){
			if(maxw < st[i].length()){
				maxw = st[i].length();
			}
		}
		
		width = maxw*Screen.FONT_WIDTH + 4;
		
		data = new int[ROW_HEIGHT*numrows][width];
		
		for(i = 0; i < data.length; i++){
			for(int j = 0; j <data[0].length; j++){
				data[i][j] = Sprite.getColor(255, 255, 255);
			}
		}
		for(i = 0; i<numrows; i++){
			Screen.drawString(st[i], data, width, ROW_HEIGHT*numrows, 2, i*ROW_HEIGHT);
		}
		numLines = numrows;
	}
	
	public int getHeight(){
		return numLines * ROW_HEIGHT;
	}

	public int getWidth(){
		return width;
	}
	
	public int getOffsetX(){
		return OFFSET_X;
	}
	
	public int getOffsetY(){
		return BASE_OFFSET_Y - getHeight();
	}
	
	public int[][] getData(){
		return data;
	}
}
