package maze.logic;

import java.awt.event.KeyEvent;
import java.io.Serializable;

public class ConfigurationMaze implements Serializable {
	
	private static final long serialVersionUID = -179756147555204617L;
	
	public int sizeMaze;
	public int numberDragons;
	public int tactica;
	public int[] keys;
	public static int UP = 0;
	public static int DOWN = 1;
	public static int RIGHT = 2;
	public static int LEFT = 3;
	
	public ConfigurationMaze (int sizeMaze, int numberDragons, int tactica) {
		
		this.sizeMaze = sizeMaze;
		this.numberDragons = numberDragons;
		this.tactica = tactica;
		
		keys = new int[4];
		keys[UP] = KeyEvent.VK_UP;
		keys[DOWN] = KeyEvent.VK_DOWN;
		keys[RIGHT] = KeyEvent.VK_RIGHT;
		keys[LEFT] = KeyEvent.VK_LEFT;
	}
	
	public boolean keyAlreadyUsed (int option, KeyEvent e) {
		
		/*for (int key : keys)
			if (key == e.getKeyCode())
				return true;*/
		
		for (int i = 0; i < keys.length; i++)
			if (keys[i] == e.getKeyCode() && i != option)
				return true;
		return false;
	}
}
