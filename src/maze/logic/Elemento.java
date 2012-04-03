package maze.logic;

import java.io.Serializable;

public abstract class Elemento implements Serializable {

	private static final long serialVersionUID = 3805821215186947949L;
	
	private Coord posicao;
	
	public Elemento (int x, int y) {
		posicao = new Coord(x,y);
	}
	
	public Coord getPos() {
		return posicao;
	}
	
	public void setPos(int x, int y) {
		posicao.setCoord(x, y);
	}
}
