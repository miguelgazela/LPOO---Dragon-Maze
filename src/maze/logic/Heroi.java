package maze.logic;

import java.io.Serializable;

/**
 * Representacao do heroi no labirinto 
 * @author migueloliveira
 */
public class Heroi extends Elemento implements Serializable{

	private static final long serialVersionUID = -2066160452842392951L;
	
	private boolean heroiArmado, heroiMorto;
	
	/**
	 * Construtor
	 * @param x coluna do heroi
	 * @param y linha do heroi
	 */
	public Heroi(int x, int y) {
		super(x, y);
		
		heroiArmado = false;
		heroiMorto = false;
	}

	/**
	 * Coloca o Heroi armado
	 */
	public void apanhaEspada() {
		this.heroiArmado = true;
	}

	/**
	 * Verifica se o Heroi esta armado
	 * @return boolean
	 */
	public boolean estaArmado() {
		return heroiArmado;
	}

	/**
	 * Mata o Heroi
	 */
	public void morre() {
		heroiMorto = true;
	}

	/**
	 * Verifica se o Heroi esta morto
	 * @return
	 */
	public boolean estaMorto() {
		return heroiMorto;
	}
}
