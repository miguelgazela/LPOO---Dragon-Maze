package maze.logic;

import java.io.Serializable;

public class Coord implements Serializable {
	
	private static final long serialVersionUID = -196676866997125824L;
	
	public int x;
	public int y;
	
	public Coord () {
		x = 0;
		y = 0;
	}
	
	public Coord (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Coord move(int x_of, int y_of) {  // retorna um objecto depois de mudanca de coordenadas
		//Coord nova = new Coord (x+x_of, y+y_of);
		return new Coord(x+x_of, y+y_of);
	}
	
	public boolean equals (Object c2) {
		if (c2 instanceof Coord)
			return (this.x == ((Coord)c2).x && this.y == ((Coord)c2).y);
		return false;
	}
	
	public String toString() {
		return this.x + "/" + this.y;
	}

	public void setCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
