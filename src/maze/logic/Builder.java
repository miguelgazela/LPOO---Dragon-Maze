package maze.logic;

/**
 * Builder abstracto para a construcao de Labirintos
 * @author migueloliveira
 */
public abstract class Builder {

	Labirinto maze = new Labirinto();
	
	/**
	 * Retorna o Labirinto que foi construido pelo builder
	 * @return Labirinto construido
	 */
	public Labirinto getLabirinto() {
		return maze;
	}
	
	/**
	 * Coloca o heroi no Labirinto
	 * @return Builder 
	 */
	public abstract Builder colocarHeroi();
	
	/**
	 * Coloca o/os dragao/oes no Labirinto
	 * @return Builder
	 */
	public abstract Builder colocarDragao();
	
	/**
	 * Coloca a espada no Labirinto
	 * @return Builder
	 */
	public abstract Builder colocarEspada();
	
	/**
	 * Inicializa e constroi o Labirinto;
	 * @return Builder
	 */
	public abstract Builder construirLabirinto();
}
