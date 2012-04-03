package maze.logic;

import java.util.Vector;

public abstract class Builder {

	Labirinto maze = new Labirinto();
	
	public Labirinto getLabirinto() {
		return maze;
	}
	
	public abstract Builder colocarHeroi();
	
	public abstract Builder colocarDragao();
	
	public abstract Builder colocarEspada();
	
	public abstract Builder construirLabirinto();
}
