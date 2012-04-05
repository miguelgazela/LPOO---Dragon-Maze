package maze.logic;

import java.io.Serializable;

/**
 * Classe abstracta que serve como interface para elementos do Labirinto
 * @author migueloliveira
 */
public abstract class Elemento implements Serializable {

	private static final long serialVersionUID = 3805821215186947949L;
	
	private Coord posicao;
	
	/**
	 * Construtor
	 * @param x coordenada x do Elemento no labirinto
	 * @param y coordenada y do Elemento no labirinto
	 */
	public Elemento (int x, int y) {
		posicao = new Coord(x,y);
	}
	
	/**
	 * Retorna a Coordenada do Elemento
	 * @return Coord
	 */
	public Coord getPos() {
		return posicao;
	}
	
	/**
	 * Altera a Coordenada do Elemento
	 * @param x nova coordenada x
	 * @param y nova coordenada y
	 */
	public void setPos(int x, int y) {
		posicao.setCoord(x, y);
	}
}
