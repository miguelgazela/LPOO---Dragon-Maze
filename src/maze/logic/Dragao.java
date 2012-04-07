package maze.logic;

import java.io.Serializable;

/**
 * Classe derivada de Elemento.
 * Representa um dragao presente no labirinto.
 * @author migueloliveira
 */
public class Dragao extends Elemento implements Serializable{

	private static final long serialVersionUID = 6056390907308360604L;
	
	private boolean dragaoMorto;
	private boolean adormecido;
	
	/**
	 * Construtor
	 * @param x coordenada x do dragao no labirinto
	 * @param y coordenda y do dragao no labirinto
	 * @param estado adormecido ou nao
	 */
	public Dragao(int x, int y, boolean estado) {
		super(x, y);
		dragaoMorto = false;
		adormecido = estado;
	}
	
	/**
	 * Mata o Dragao
	 */
	public void morre() {
		dragaoMorto = true;
	}

	/**
	 * Retorna se o Dragao esta morto
	 * @return boolean 
	 */
	public boolean estaMorto() {
		return dragaoMorto;
	}
	
	/**
	 * Retorna se o Dragao esta adormecido
	 * @return boolean
	 */
	public boolean estaAdormecido() {
		return adormecido;
	}
	
	/**
	 * Adormece o Dragao
	 */
	public void adormece() {
		adormecido = true;
	}

	/**
	 * Acorda o Dragao
	 */
	public void acorda() {
		adormecido = false;
	}
}
