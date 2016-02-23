package priv.hkon.theseq.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Screen extends JFrame{

	private static final long serialVersionUID = 1L;
	
	public static final int W = 200;
	public static final int H = 200;
	public static final int SCALE = 3;
	Core core;
	
	BufferedImage mainImage;
	int[] data;
	Controller controller;
	public int count = 0;
	public static int[] charPositions;
	public static int[][][] charData;
	BufferedImage bu;
	
	public Font font;
	
	public static final int FONT_WIDTH = 5;
	public static final int FONT_HEIGHT = 8;
	
	public static final int FW = FONT_WIDTH;
	public static final int FH = FONT_HEIGHT;
	
	public Screen(Core c){
		core = c;
		setSize(W*SCALE, H*SCALE);
		initCharacters();
		setResizable(false);
		mainImage = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
		data = ((DataBufferInt)(mainImage.getRaster().getDataBuffer())).getData();
		controller = new Controller();
		addKeyListener(controller);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle(Core.TITLE);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		/*for(int i = 0; i<W*H; i++){
			data[i] = ((255<<24) - count*i)  + (255<<24);
		}*/
		g.drawImage(mainImage, 0, 0, SCALE*W, SCALE*H, null);
	}
	
	public void update(){
		paint(getGraphics());
	}
	
	public static void draw(int[][] baseData, int bw, int bh, int[][] drawData, int dw, int dh, int offX, int offY){
		int offDX = Math.max(0, -offX); int offDY = Math.max(0,  -offY); //Offset in drawbuffer
		int restrictDX = Math.min(dw, bw - offX); int restrictDY = Math.min(dh, bh - offY);
		for(int i = offDY; i<restrictDY; i++){
			for(int j = offDX; j<restrictDX; j++){
				if((drawData[i][j] &(255<<24)) != 0)
					baseData[offY + i][offX + j] = drawData[i][j];
			}
		}
	}
	
	public static void draw(int[] baseData, int bw, int bh, int[][] drawData, int dw, int dh, int offX, int offY){
		int offDX = Math.max(0, -offX); int offDY = Math.max(0,  -offY); //Offset in drawbuffer
		int restrictDX = Math.min(dw, bw - offX); int restrictDY = Math.min(dh, bh - offY);
		for(int i = offDY; i<restrictDY; i++){
			for(int j = offDX; j<restrictDX; j++){
				if((drawData[i][j] &(255<<24)) != 0)
					baseData[(offY + i)*bw + offX + j] = drawData[i][j];
			}
		}
	}
	
	public void setData(int[][] ndata){
		draw(data, W, H, ndata, ndata[0].length, ndata.length, 0, 0);
	}
	
	public void initCharacters(){
		charPositions = new int[256];
		font = new Font("Monospaced", Font.PLAIN, FH);
		String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz,.!? -'";
		for(int i = 0; i < str.length(); i++){
			charPositions[str.charAt(i)] = i;
		}
		
		charData = new int[str.length()][FH + 5][FW];
		
		bu = new BufferedImage(FW*str.length(), FH+5, BufferedImage.TYPE_INT_ARGB);
		int[] bdata = ((DataBufferInt)(bu.getRaster().getDataBuffer())).getData();
		for(int i = 0; i < str.length(); i++){
			bdata[i] = 0;
		}
		
		Graphics g = bu.getGraphics();
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawString(str, 0, FH);
		
		for(int i = 0; i < str.length(); i++){
			for(int j = 0; j < FH + 5; j++){
				for(int k = 0; k < FW; k++){
					charData[i][j][k] = bdata[(j)*str.length()*FW + i*FW + k];
				}
			}
		}
	}
	
	public static void drawString(String str, int[][] baseData, int bw, int bh, int offX, int offY){
		for(int i = 0; i < str.length(); i++){
			draw(baseData, bw, bh, charData[charPositions[str.charAt(i)]], FW, FH + 5, offX + i*FW, offY);
		}
	}
	
	public static int nightFilter(int col, float factor){ // Does not preserve alpha channel
		int r = (col >>16)&255;
		int g = (col >>8)&255;
		int b = (col)&255;
		
		int av = (r +b + g)/3;
		
		r = (int)Math.max(r*factor, r*0.2);
		g = (int)Math.max(g*factor, g*0.3);
		b = (int)Math.max(b*factor, av*0.3);
		
		return (r<<16) | (g<<8) | b | (255 << 24);
	}
}
