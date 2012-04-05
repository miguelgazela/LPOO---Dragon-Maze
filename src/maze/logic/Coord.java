package maze.logic;

import java.io.Serializable;

/**
 * Classe que representa uma coordenada num array 
 * @author migueloliveira
 */
public class Coord implements Serializable {
	
	private static final long serialVersionUID = -196676866997125824L;
	
	public int x;
	public int y;
	
	/**
	 * Construtor
	 * Inicializa as coordenadas a 0
	 */
	public Coord () {
		x = 0;
		y = 0;
	}
	
	/**
	 * Construtor
	 * Inicializa as coordendas com as que recebe
	 * @param x coordenada X
	 * @param y coordenada Y
	 */
	public Coord (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Retorna uma nova coordenada depois de calcula a diferenca em relacao a atual
	 * @param x_of diferenca em x
	 * @param y_of diferenca em y
	 * @return Coord nova coordenada
	 */
	public Coord move(int x_of, int y_of) {
		return new Coord(x+x_of, y+y_of);
	}
	
	/**
	 * Verifica se 2 coordenadas apontam para a mesma posicao
	 * @param Object Coordenada para ser comparada
	 * @return boolean 
	 */
	public boolean equals (Object c2) {
		if (c2 instanceof Coord)
			return (this.x == ((Coord)c2).x && this.y == ((Coord)c2).y);
		return false;
	}
	
	/**
	 * Altera as posicoes de uma Coordenada
	 * @param x coordenada x
	 * @param y coordenada y
	 */
	public void setCoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
