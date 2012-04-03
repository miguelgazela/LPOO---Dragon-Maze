package maze.logic;

import java.io.Serializable;

public class Heroi extends Elemento implements Serializable{

	private static final long serialVersionUID = -2066160452842392951L;
	
	private boolean heroiArmado, heroiMorto;
	
	public Heroi(int x, int y) {
		super(x, y);
		
		heroiArmado = false;
		heroiMorto = false;
	}

	public void apanhaEspada() {
		this.heroiArmado = true;
	}

	public boolean estaArmado() {
		return heroiArmado;
	}

	public void morre() {
		heroiMorto = true;
	}

	public boolean estaMorto() {
		return heroiMorto;
	}
}
