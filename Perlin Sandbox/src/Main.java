import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

public class Main extends JFrame implements KeyListener{
	
	private static final long serialVersionUID = 1L;

	float tileSize = 100.0f;
	
	int w = 400;
	int h = 400;
	
	int vectorsX = (int)(w / tileSize) + 1;
	int vectorsY = (int)(h / tileSize) + 1;
	
	Vector[][] vectors;
	
	int [] data;
	BufferedImage image;
	int count = 0;

	public Main(){
		setTitle("Perlin Sandbox");
		setSize(w , h);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		vectors = new Vector[vectorsY][vectorsX];
		for(int i = 0; i < vectorsY; i++){
			for(int j = 0; j < vectorsX; j++){
				vectors[i][j] = new Vector(getRandomAngle(j%(vectorsX - 1) + i%(vectorsY - 1)*vectorsX));
			}
		}
		
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		data = ((DataBufferInt)(image.getRaster().getDataBuffer())).getData();
		
		addKeyListener(this);
		setVisible(true);
	}

	public static void main(String[] args) {
		Main m = new Main();
		m.start();
	}
	
	public void start(){
		while(true){
			for(int i = 0; i < vectorsY; i++){
				for(int j = 0; j< vectorsX; j++){
					vectors[i][j].tick();
				}
			}

			update();
			try{
				Thread.sleep(16);
			}
			catch(Exception e){}
		}
	}
	
	public void update(){
		count++;
		int[] data1 = new int[h*w];
		for(int i = 0; i < h; i++){
			for(int j = 0; j < w; j++){
				float jfrac = j/tileSize;
				float ifrac = i/tileSize;
				
				int vx = (int)(jfrac);
				int vy = (int)(ifrac);
				
				float dot11 = vectors[vy][vx].x*(vx - jfrac) + vectors[vy][vx].y*(vy - ifrac);
				float dot21 = vectors[vy + 1][vx].x*(vx - jfrac) + vectors[vy + 1][vx].y*(vy + 1 - ifrac);
				
				float dot12 = vectors[vy][vx + 1].x*(vx  + 1- jfrac) + vectors[vy][vx + 1].y*(vy - ifrac);
				float dot22 = vectors[vy + 1][vx + 1].x*(vx + 1 - jfrac) + vectors[vy + 1][vx + 1].y*(vy + 1 - ifrac);
				
				float val2 = lerp(lerp( dot11, dot12, jfrac - vx), lerp(dot21, dot22, jfrac - vx), ifrac - vy);
				val2 = (val2 + 1)/2;
				//System.out.println(Math.abs(pval- val2));
				data1[i* w + j] = (int)(val2*255);//*((1 << 16) + (1 << 8) + 1) | (255<<24);
			}
		}
		
		for(int i = 0; i< h; i++){
			for(int j = 0 ; j < w; j++){
				data[i*w + j] = (data1[i*w + j] + data1[((i*2+ 5*count) % h)* w + ((j*2) % w)] + data1[((i*7/4 +3*count)% h)* w + (j*7/4 % w)]+ data1[((i*18/7 + 4*count) % h)* w + (j*18/7 % w)])/4; //*((1 << 16) + (1 << 8) + 1) | (255<<24);
				if(data[i*w + j] < 150){
					data[i*w + j] = data[i*w + j] * ((1<<16) | (255 << 24));
				}
				else{
					data[i*w + j] = (data[i*w + j] * (1<<16)) | ((data[i*w + j] - 190)*4 *(1<<8)) | (255 << 24);
				}
				
			}
		}
		
		getGraphics().drawImage(image, 0, 0, null);
	}
	
	int blobs(float val){
		return ((int)(val*255)) & ~(1 << 7);
	}
	
	int moreBlobs(float val){
		return ((int)(val*255)) & ~(7 << 5);
	}
	
	int hypno(float val){
		return (int)(Math.sin(val*100)*255);
	}
	
	
	
	float smooth(float a){
		return a*a*(3 - 2*a);
	}
	
	float lerp(float a, float b, float alpha){
		alpha = smooth(alpha);
		return b*alpha + (1-alpha)*a;
	}
	
	class Vector{
		float dt;
		float cdt;
		float sdt;
		float phi, x, y;
		Vector(float phi){
			this.phi = phi;
			x = (float) Math.cos(phi);
			y = (float) Math.sin(phi);
			
			dt = getRandomAngle((int)(phi * 144453))*0.01f;
			cdt = (float)Math.cos(dt);
			sdt = (float)Math.sin(dt);
		}
		
		Vector(float x, float y){
			phi = (float)Math.atan2(y, x);
			this.x = x;
			this.y = y;
		}
		
		void tick(){
			phi += dt;
			float nx = x*cdt - y*sdt;
			float ny = x*sdt + y*cdt;
			x = nx;
			y = ny;
		}
	}
	
	float dot(Vector v1, Vector v2){
		return v1.x * v2.x + v1.y * v2.y;
	}
	
	float getRandomAngle(int p){
		p = (33312*p ^ 13454)% 10007;
		return (float)(p/10007.0*2*Math.PI);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		System.exit(0);
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
