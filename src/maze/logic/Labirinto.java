package maze.logic;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import maze.gui.GraphicGame;
import maze.sound.dragonDies;
import maze.sound.soundPickSword;


public class Labirinto implements Serializable {
	
	private static final long serialVersionUID = -7227443685456744007L;

	protected int[][] labirinto;
	
	protected int counter = 0, size = 0, tactica, numJogadasDragaoAdormecido = 0;
	
	protected Random rand = new Random();
	protected Coord Saida = new Coord();
	
	protected Espada espada;
	protected Heroi heroi;
	
	protected Vector<Dragao> dragoes;
	
	public static final int CHAO = 0;
	public static final int PAREDE = 1;
	public static final int HEROI = 2;
	public static final int ARMADO = 3;
	public static final int ESPADA = 4;
	public static final int SAIDA = 5;
	public static final int DRAGAO = 6;
	public static final int DRAGAOESPADA = 7;
	
	protected int NUM_DRAGOES;
	protected int naoPodeAdormecer;
	
	protected boolean chegouFim;
	
	public Labirinto() {
		chegouFim = false;
		dragoes = new Vector<Dragao>();
	}
	
	public Labirinto(ConfigurationMaze configs, int[][] maze, Heroi hero, Espada sword, Vector<Dragao> dragoes) {
		this();
		labirinto = maze;
		this.tactica = configs.tactica;
		this.size = configs.sizeMaze;
		NUM_DRAGOES = configs.numberDragons;
		heroi = hero;
		espada = sword;
		this.dragoes = dragoes;
	}
	
	@Override
	public String toString() {
		String str = new String ();

		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
				if (labirinto[i][j] == CHAO || labirinto[i][j] == -1) // a segunda condicao e para testes
					str += "  ";
				else if(labirinto[i][j] == PAREDE)
					str += "W ";
				else if (labirinto[i][j] == SAIDA)
					str += "S ";
				else if (labirinto[i][j] == HEROI)
					str += "H ";
				else if (labirinto[i][j] == ARMADO)
					str += "A ";
				else if (labirinto[i][j] == ESPADA)
					str += "E ";
				else if (labirinto[i][j] == DRAGAO)
					if (getDragaoAt(i,j).estaAdormecido())
						str += "d ";
					else
						str += "D ";
				else if (labirinto[i][j] == DRAGAOESPADA)
					if (getDragaoAt(i,j).estaAdormecido())
						str += "f ";
					else
						str += "F ";
			if (i != (size-1))
				str += '\n';
		}
		return str;
	}
	
	public Dragao getDragaoAt(int y, int x) 
	{	
		/* verifica se algum dragao esta na posicao desejada, se sim devolve-o */
		for (Dragao d : dragoes)
		{
			Coord posDragao = d.getPos();
			if (posDragao.x == x && posDragao.y == y)
				return d;
		}
		return null;
	}

	public void moverHeroi(String direcaoStr)
	{
		Coord temp = new Coord();
		boolean heroiArmado = heroi.estaArmado();
		
		/* calcula a nova coordenada de acordo com a direcao desejada */
		if (direcaoStr.equalsIgnoreCase("w") && heroi.getPos().y > 0)
			temp = heroi.getPos().move(0, -1);
		else if (direcaoStr.equalsIgnoreCase("s") && heroi.getPos().y < (size-1))
			temp = heroi.getPos().move(0,1);
		else if (direcaoStr.equalsIgnoreCase("a") && heroi.getPos().x > 0)
			temp = heroi.getPos().move(-1,0);
		else if (direcaoStr.equalsIgnoreCase("d") && heroi.getPos().x < (size-1))
			temp = heroi.getPos().move(1,0);
		else
			temp = heroi.getPos(); // nao deve chegar aqui
		
		// verificar o estado da proxima posicao
		
		if ( labirinto[temp.y][temp.x] != PAREDE) 					// se nao for uma parede
		{
			labirinto[heroi.getPos().y][heroi.getPos().x] = CHAO; 	// limpa a posicao antiga
			
			if (labirinto[temp.y][temp.x] == CHAO) 					// se tem espaco em branco a sua frente
			{
				if (heroiArmado)
					labirinto[temp.y][temp.x] = ARMADO;				// desenha o heroi (armado ou nao)
				else
					labirinto[temp.y][temp.x] = HEROI;
				
				heroi.setPos(temp.x,temp.y);
			}
			else if (labirinto[temp.y][temp.x] == ESPADA) 			// apanhou a espada
			{
				labirinto[temp.y][temp.x] = ARMADO;
				heroi.apanhaEspada();
				heroi.setPos(temp.x,temp.y);
				if (GraphicGame.soundIsOn)
					new soundPickSword().play();
			}
			else if (labirinto[temp.y][temp.x] == SAIDA) 			// se chegou a saida
			{
				if (heroiArmado)
					chegouFim = true;
				else 												// se nao esta armado nao pode sair
					labirinto[heroi.getPos().y][heroi.getPos().x] = HEROI;
			}
			else if ( labirinto[temp.y][temp.x] == DRAGAO && getDragaoAt(temp.y, temp.x).estaAdormecido() ) // se tem o dragao adormecido a sua frente
				labirinto[heroi.getPos().y][heroi.getPos().x] = HEROI;
		}
	}
	
	
	public void moverDragao(String direcaoStr)
	{
		Coord temp = new Coord();
		boolean movimentoValido = false;
		int movimento = 0;
		
		for (int i = 0; i < dragoes.size(); i++)
		{
			do {

				if (direcaoStr.length() == 0) // para o motor normal de jogo, com movimentos random
				{
					movimento = rand.nextInt(5);

					if (movimento == 1 && dragoes.elementAt(i).getPos().y > 1) // norte
						temp = dragoes.elementAt(i).getPos().move(0, -1);
					else if (movimento == 2 && dragoes.elementAt(i).getPos().y < (size-2)) // sul
						temp = dragoes.elementAt(i).getPos().move(0, 1);
					else if (movimento == 3 && dragoes.elementAt(i).getPos().x > 1) // oeste
						temp = dragoes.elementAt(i).getPos().move(-1, 0);
					else if (movimento == 4 && dragoes.elementAt(i).getPos().x < (size-2)) // este
						temp = dragoes.elementAt(i).getPos().move(1, 0);
					else
						temp = dragoes.elementAt(i).getPos();
				}
				else // para os testes de movimento do dragao
				{
					if (direcaoStr.equalsIgnoreCase("w") && dragoes.elementAt(i).getPos().y > 0)
						temp = dragoes.elementAt(i).getPos().move(0, -1);
					else if (direcaoStr.equalsIgnoreCase("s") && dragoes.elementAt(i).getPos().y < (size-1))
						temp = dragoes.elementAt(i).getPos().move(0,1);
					else if (direcaoStr.equalsIgnoreCase("a") && dragoes.elementAt(i).getPos().x > 0)
						temp = dragoes.elementAt(i).getPos().move(-1,0);
					else if (direcaoStr.equalsIgnoreCase("d") && dragoes.elementAt(i).getPos().x < (size-1))
						temp = dragoes.elementAt(i).getPos().move(1,0);
					else if (direcaoStr.equalsIgnoreCase("m")) // mantem
						temp = dragoes.elementAt(i).getPos();
					else
						temp = dragoes.elementAt(i).getPos(); // nao deve chegar aqui
				}

				// verificar o estado da proxima posicao
				if (movimento >= 0 && movimento <= 4)
				{
					if ( labirinto[temp.y][temp.x] != PAREDE && labirinto[temp.y][temp.x] != SAIDA)
					{
						// ate conseguir mover-se ou simplesmente manter-se, continua invalido
						if ( (temp == dragoes.elementAt(i).getPos() && movimento == 0) 
								|| (temp != dragoes.elementAt(i).getPos() && movimento != 0) 
									|| (movimento == 0 && direcaoStr.length() != 0))
							movimentoValido = true;

						if (movimentoValido)
						{
							labirinto[dragoes.elementAt(i).getPos().y][dragoes.elementAt(i).getPos().x] = CHAO; // limpa a posicao antiga

							if (labirinto[temp.y][temp.x] == ARMADO) // encontra o heroi armado
								dragoes.elementAt(i).morre();
							else if (dragoes.elementAt(i).getPos().equals(espada.getPos())) // se esta na posicao da espada
							{
								if (movimento != 0)
								{
									labirinto[temp.y][temp.x] = DRAGAO;
									labirinto[dragoes.elementAt(i).getPos().y][dragoes.elementAt(i).getPos().x] = ESPADA;
								}
								else
									labirinto[dragoes.elementAt(i).getPos().y][dragoes.elementAt(i).getPos().x] = DRAGAOESPADA;
							}
							else
							{
								if ( labirinto[temp.y][temp.x] == ESPADA ) // se vai para cima da espada
									labirinto[temp.y][temp.x] = DRAGAOESPADA;
								else
									labirinto[temp.y][temp.x] = DRAGAO;
							}
							dragoes.elementAt(i).setPos(temp.x, temp.y);
						}
					}
				}
			} while (!movimentoValido);
		}
	}
	
	
	public void pertoDragao()
	{
		/* percorre todos os dragoes existentes no labirinto */
		for (int i = 0; i < dragoes.size(); i++)
		{
			boolean heroiPerto = false, mesmaCoord = false, heroiArmado = heroi.estaArmado();
			int dragaoX = dragoes.elementAt(i).getPos().x, dragaoY = dragoes.elementAt(i).getPos().y;

			if ( labirinto[dragaoY-1][dragaoX] == HEROI || labirinto[dragaoY-1][dragaoX] == ARMADO) // a norte
				heroiPerto = true;
			else if(labirinto[dragaoY+1][dragaoX] == HEROI || labirinto[dragaoY+1][dragaoX] == ARMADO) // a sul
				heroiPerto = true;
			else if ( labirinto[dragaoY][dragaoX+1] == HEROI || labirinto[dragaoY][dragaoX+1] == ARMADO) // a este
				heroiPerto = true;
			else if ( labirinto[dragaoY][dragaoX-1] == HEROI || labirinto[dragaoY][dragaoX-1] == ARMADO) // a oeste
				heroiPerto = true;
			else if ( dragoes.elementAt(i).getPos().equals(heroi.getPos())) // quanto os dois avancam para uma esquina
			{
				heroiPerto = true;
				mesmaCoord = true;
			}

			if (heroiPerto)
				if (heroiArmado) // se o heroi estava armado, o dragao e removido
				{
					dragoes.remove(i);
					if (mesmaCoord)
						labirinto[dragaoY][dragaoX] = ARMADO;
					else
						labirinto[dragaoY][dragaoX] = CHAO;
					if (GraphicGame.soundIsOn)
						new dragonDies().play();
				}
				else // o heroi morre e o jogo atual acaba
					if (!dragoes.elementAt(i).estaAdormecido())
						heroi.morre();
		}
	}

	public boolean getChegouFim() {
		return chegouFim;
	}
	
	public Heroi getHeroi() {
		return heroi;
	}

	public void avaliaTactica()
	{
		if (tactica == 3)											// movimentacao aleatoria intercalada com dormir
			if (dragoes.elementAt(0).estaAdormecido()) 				// se ja esta adormecido
				if (numJogadasDragaoAdormecido > 0)					// e ainda tem jogadas para ficar a dormir
					numJogadasDragaoAdormecido--;
				else
					for(int i = 0; i < dragoes.size(); i++)
						dragoes.elementAt(i).acorda();
			else 													// se esta acordado
			{
				if (naoPodeAdormecer == 0)							// se ainda nao pode voltar a adormecer
				{
					int vaiAdormecer = rand.nextInt(3);

					if (vaiAdormecer == 0)								// tem 1 terco de probabilidade de adormecer						
					{	
						for(int i = 0; i < dragoes.size(); i++)
							dragoes.elementAt(i).adormece();
						numJogadasDragaoAdormecido = rand.nextInt(3)+3; // adormece entre 3 a 5 jogadas
						naoPodeAdormecer = 10;						// depois de acordar so pode voltar a adormecer passado 10 jogadas
					}
				}
				else
					naoPodeAdormecer--;								// diminui o numero de jogadas que falta ate poder adormecer
			}
	}

	public boolean dragoesTodosMortos() {
		return dragoes.isEmpty();
	}

	public boolean dragoesAdormecidos() {
		if (dragoes.elementAt(0).estaAdormecido())
			return true;
		return false;
	}

	public int getSize() {
		return size;
	}

	public int[][] getLabirinto() {
		return labirinto;
	}

	public void setPos(int type, int y, int x) {
		labirinto[y][x] = type;
	}

	public int getTactica() {
		return tactica;
	}

	public int getNumDragoes() {
		return dragoes.size();
	}
}
