package priv.hkon.theseq.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Controller implements KeyListener {
	
	public static boolean[] input = new boolean[65536];

	@Override
	public void keyPressed(KeyEvent arg0) {
		int e = arg0.getKeyCode();
		if(e == KeyEvent.VK_ESCAPE)
			System.exit(0);
		input[e] = true;

	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int e = arg0.getKeyCode();
		input[e] = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {

	}

}
