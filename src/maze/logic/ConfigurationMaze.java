package maze.logic;

import java.awt.event.KeyEvent;
import java.io.Serializable;

/**
 * Classe que repsenta uma configuracao para um jogo.
 * Possui o seu tamanho, numero de dragoes, tactica a utilizar
 * e teclas utilizadas para o movimento.
 * @author migueloliveira
 */
public class ConfigurationMaze implements Serializable {
	
	private static final long serialVersionUID = -179756147555204617L;
	
	public int sizeMaze;
	public int numberDragons;
	public int tactica;
	public int[] keys;
	public static int UP = 0;
	public static int DOWN = 1;
	public static int RIGHT = 2;
	public static int LEFT = 3;
	
	/**
	 * Construtor - inicializa todos os elementos da configuracao
	 * @param sizeMaze
	 * @param numberDragons
	 * @param tactica
	 */
	public ConfigurationMaze (int sizeMaze, int numberDragons, int tactica) {
		
		this.sizeMaze = sizeMaze;
		this.numberDragons = numberDragons;
		this.tactica = tactica;
		
		// teclas definidas por defeito
		keys = new int[4];
		keys[UP] = KeyEvent.VK_UP;
		keys[DOWN] = KeyEvent.VK_DOWN;
		keys[RIGHT] = KeyEvent.VK_RIGHT;
		keys[LEFT] = KeyEvent.VK_LEFT;
	}
	
	/**
	 * Verifica se a tecla escolhida ja esta' a ser utilizada
	 * @param option tecla a ser alterada
	 * @param e tecla que foi usada
	 * @return boolean se a tecla esta' a ser utilizada ou nao
	 */
	public boolean keyAlreadyUsed (int option, KeyEvent e) {
		
		for (int i = 0; i < keys.length; i++)
			if (keys[i] == e.getKeyCode() && i != option)
				return true;
		return false;
	}
}
