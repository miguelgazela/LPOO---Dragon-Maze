package maze.logic;

/**
 * Classe que descende de Builder e constroi um Labirinto pre-definido
 * @author migueloliveira
 */
public class PreDefBuilder extends Builder {
	
	/**
	 * Constructor
	 * @param tactica que ira ser utilizada no jogo
	 */
	public PreDefBuilder (int tactica) {
		super();
		maze.tactica = tactica;
	}
	
	@Override
	public PreDefBuilder colocarDragao() {
		
		// instantia um Dragao na posicao pre-definida
		Dragao dragao = new Dragao(1, 3, false);
		// adiciona-o ao labirinto
		maze.dragoes.add(dragao);
		maze.labirinto[dragao.getPos().y][dragao.getPos().x] = Labirinto.DRAGAO;
		if (maze.tactica == 2) // dragao adormecido
			maze.dragoes.elementAt(0).adormece();
		return this;
	}

	@Override
	public PreDefBuilder colocarEspada() {
		// coloca a Espada na posicao pre-definida
		maze.espada = new Espada(1,8);
		maze.labirinto[maze.espada.getPos().y][maze.espada.getPos().x] = Labirinto.ESPADA;
		return this;
	}
	
	@Override
	public PreDefBuilder colocarHeroi() {
		// coloca o Heroi na posicao pre-definida
		maze.heroi = new Heroi(1,1);
		maze.labirinto[maze.heroi.getPos().y][maze.heroi.getPos().x] = Labirinto.HEROI;
		return this;
	}
	
	public PreDefBuilder colocarSaida() {
		// coloca a Saida na posicao pre-definida
		maze.Saida.x = 9; maze.Saida.y = 5;
		maze.labirinto[maze.Saida.y][maze.Saida.x] = Labirinto.SAIDA;
		return this;
	}

	@Override
	public PreDefBuilder construirLabirinto() {
		
		// inicializa o labirinto com o tamanho pre-definido
		maze.size = 10;
		maze.labirinto = new int[maze.size][maze.size];

		// chao

		for (int i = 1; i < 10; i++)
			for (int j = 1; j < 10; j++)
				maze.labirinto [i][j] = Labirinto.CHAO;

		// paredes externas

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < maze.size; j++)
				maze.labirinto[i*(maze.size-1)][j] = Labirinto.PAREDE;

		for (int i = 0; i < maze.size; i++)
			for (int j = 0; j < 2; j++)
				maze.labirinto[i][j*(maze.size-1)] = Labirinto.PAREDE;

		// paredes internas

		for (int i = 2; i < 8;i++)
		{
			maze.labirinto[i][2] = Labirinto.PAREDE;
			maze.labirinto[i][3] = Labirinto.PAREDE;
			maze.labirinto[i][5] = Labirinto.PAREDE;
			maze.labirinto[i][7] = Labirinto.PAREDE;
		}
		maze.labirinto[8][2] = Labirinto.PAREDE;
		maze.labirinto[8][3] = Labirinto.PAREDE;
		maze.labirinto[5][2] = Labirinto.CHAO;
		maze.labirinto[5][3] = Labirinto.CHAO;
		maze.labirinto[5][5] = Labirinto.CHAO;
		return this;
	}
}
