package maze.logic;

import java.io.Serializable;

public class Dragao extends Elemento implements Serializable{

	private static final long serialVersionUID = 6056390907308360604L;
	
	private boolean dragaoMorto;
	private boolean adormecido;
	
	public Dragao(int x, int y, boolean estado) {
		super(x, y);
		dragaoMorto = false;
		adormecido = estado;
	}
	
	public void morre() {
		dragaoMorto = true;
	}

	public boolean estaMorto() {
		return dragaoMorto;
	}
	
	public boolean estaAdormecido() {
		return adormecido;
	}
	
	public void adormece() {
		adormecido = true;
	}

	public void acorda() {
		adormecido = false;
	}
}
