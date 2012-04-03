package maze.logic;

import java.util.Vector;

public abstract class Builder {

	Labirinto maze = new Labirinto();
	
	public Labirinto getLabirinto() {
		maze.chegouFim = false;
		maze.dragoes = new Vector<Dragao>();
		maze.NUM_DRAGOES = 2;
		
		return maze;
	}
	
	public abstract Builder colocarDragao();
	
	public abstract Builder colocarEspada();
	
	public abstract Builder construirLabirinto();
}
