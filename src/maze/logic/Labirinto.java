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
	
	public Labirinto(ConfigurationMaze configs, int[][] maze, Heroi hero, Espada sword, Vector<Dragao> dragoes)
	{
		this();
		labirinto = maze;
		this.tactica = configs.tactica;
		this.size = configs.sizeMaze;
		NUM_DRAGOES = configs.numberDragons;
		heroi = hero;
		espada = sword;
		this.dragoes = dragoes;
	}
	
	public Labirinto(int tactica) // labirinto pre-definido
	{ 
		this();
		this.tactica = tactica;
		
		size = 10;
		labirinto = new int[size][size];

		// chao

		for (int i = 1; i < 10; i++)
			for (int j = 1; j < 10; j++)
				labirinto [i][j] = CHAO;

		// paredes externas

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < size; j++)
				labirinto[i*(size-1)][j] = PAREDE;

		for (int i = 0; i < size; i++)
			for (int j = 0; j < 2; j++)
				labirinto[i][j*(size-1)] = PAREDE;

		// paredes internas

		for (int i = 2; i < 8;i++)
		{
			labirinto[i][2] = PAREDE;
			labirinto[i][3] = PAREDE;
			labirinto[i][5] = PAREDE;
			labirinto[i][7] = PAREDE;
		}

		labirinto[8][2] = PAREDE;
		labirinto[8][3] = PAREDE;
		labirinto[5][2] = CHAO;
		labirinto[5][3] = CHAO;
		labirinto[5][5] = CHAO;

		// posicionamento dos varios elementos

		heroi = new Heroi(1,1);
		labirinto[heroi.getPos().y][heroi.getPos().x] = HEROI;

		espada = new Espada(1,8);
		labirinto[espada.getPos().y][espada.getPos().x] = ESPADA;

		Dragao dragao = new Dragao(1, 3, false);
		dragoes.add(dragao);
		labirinto[dragao.getPos().y][dragao.getPos().x] = DRAGAO;

		Saida.x = 9; Saida.y = 5;
		labirinto[Saida.y][Saida.x] = SAIDA;
		
		if (tactica == 2) // dragao adormecido
			dragoes.elementAt(0).adormece();
	}

	public Labirinto(int tamanho, int numDragoes, int tactica) {

		this();
		this.tactica = tactica;
		size = tamanho;
		NUM_DRAGOES = numDragoes;

		// definir o tamanho do labirinto

		if ( size<5 ) size = 5; 			// tamanho minimo do labirinto
		if ( size % 2 == 0 ) size++; 		// deve ser impar para o algoritmo funcionar em condicoes
		if ( size>200 ) size = 199; 		// fica muito lento acima

		counter = (size-2)*(size-2); 		// numero de celulas que precisa visitar

		labirinto = new int[size][size];

		// limpar o lixo da superficie do labirinto

		for (int i = 1; i < (size-1); i++)
			for (int j = 1; j <(size-1); j++)
				labirinto [i][j] = -1;

		// construir as paredes do labirinto

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < size; j++)
				labirinto [i*(size-1)][j] = PAREDE;

		for (int i = 0; i < size; i++)
			for (int j = 0; j < 2; j++)
				labirinto[i][j*(size-1)] = PAREDE;

		// procurar posicao do Heroi e da Saida

		heroi = new Heroi(0,0);
		heroi.setPos(rand.nextInt(3)+1, 0);

		switch(heroi.getPos().x)
		{
		case 1: // parede esquerda
			heroi.setPos(0, 1 + 2 * rand.nextInt(((size-3)/2)+1)); // y para nao ficar nos cantos
			break;
		case 2: // parede de cima ou de baixo
			heroi.setPos(1 + 2 * rand.nextInt(((size-3)/2)+1), (size-1) * rand.nextInt(2));
			break;
		case 3: // parede direita
			heroi.setPos(size-1, 1 + 2 * rand.nextInt(((size-3)/2)+1));
			break;
		}

		Saida.x = heroi.getPos().x;

		while (Saida.x == heroi.getPos().x || Saida.y == heroi.getPos().y) // nao pode ficar na mesma parede/coluna/linha que o Heroi
		{
			Saida.x = rand.nextInt(3)+1;

			switch(Saida.x)
			{
			case 1: // parede esquerda
				Saida.x = 0;
				Saida.y = 1 + 2 * rand.nextInt(((size-3)/2)+1);
				break;
			case 2: // parede de cima ou de baixo
				Saida.x = 1 + 2 * rand.nextInt(((size-3)/2)+1);
				Saida.y = (size-1) * rand.nextInt(2);
				break;
			case 3: // parede direita
				Saida.x = (size-1);
				Saida.y = 1 + 2 * rand.nextInt(((size-3)/2)+1);
			}
		}

		Coord pos = new Coord();  	// aponta sempre para a posicao atual
		Coord next = new Coord();	// proxima posicao (especie de guia, ve se podemos ir ou nao)
		Coord back = new Coord();	// aponta sempre para o proximo movimento de retrocesso (evitar andar para a frente e para tras no mesmo sitio)
		Coord old = new Coord(); 	// guarda temporariamente o valor de back num passo do algoritmo

		pos = Saida; 				// comecar pela saida, fica mais dificil o labirinto

		next = proximoMove(pos); 	// damos o primeiro passo
		pos = next;

		labirinto[pos.y][pos.x] = CHAO;
		counter--;

		do {
			next = proximoMove (pos); 				// calculamos proxima posicao

			if (next.x == 0 && next.y == 0) 		// se nao e possivel mais nenhum movimento
			{
				old = back = pos;
				while (next.x == 0 && next.y == 0) 	// enquanto nao ha movimento possivel, 
				{									// vai voltando atras
					old = back;
					back = andarBack(pos,back);

					if (back.x == 0 && back.y == 0) // se andarBack devolveu uma coordenada nula
						back = old;
					if (back.x <= 0 || back.x >= (size-1) || back.y <= 0 || back.y >= (size-1))
						back = old; 				// evita deixar o labirinto

					pos = old;
					next = findParedeFina(pos); 	// ve se ha algum caminho do outro lado de uma parede

					if (next.x != 0 || next.y != 0)
					{
						pos = next; 				// define a posicao atual para essa parede
						labirinto[pos.y][pos.x] = 0; // torna-a caminho
					}
				}
			}
			else 									// se ha proximo movimento
			{
				construirParedes(pos,next); 		// pomos paredes a volta 
				pos = next; 			
				labirinto[pos.y][pos.x] = CHAO; 		
				counter--; 							
			}
		} while (counter > 0); 						// enquanto ainda tem celulas a visitar

		// coloca o heroi e a saida no array

		labirinto[heroi.getPos().y][heroi.getPos().x] = HEROI;
		labirinto[Saida.y][Saida.x] = SAIDA; 
		
		colocarEspada();
		colocarDragao();
		
		if (tactica == 2)		// dragoes sempre adormecidos
			for (int i = 0; i < dragoes.size(); i++)
				dragoes.elementAt(i).adormece();
	}

	/*
	 * Recebe duas coordenadas, a de onde estamos (pos) e a para onde queremos ir (next)
	 * Faz uma parede em forma de 'U' a volta da posicao onde estamos, atras da nova posicao
	 *  W W        W W W													
	 *  W P N  ou  W P W  se as posicoes estiverem todas vazias
	 *  W W          N	
	 */

	private void construirParedes(Coord pos, Coord next) {

		Coord t = new Coord();
		
		// verifica a posicao das duas coordenadas uma em relacao a outra

		if ( (next.x - pos.x) == 1) 			// proxima esta a direita		
		{
			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y); // celula acima da que estamos

			if (labirinto[t.y][t.x] == -1) 		// se a coordenada esta vazia
			{
				labirinto[t.y][t.x] = PAREDE; 	// mete uma parede
				counter--; 						// decrementa o numero de celulas que falta visitar
			}

			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);  // celula abaixo da que estamos

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y); // celula a esquerda da que estamos

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,-1).x, pos.move(-1,-1).y); // celula a esquerda e acima da que estamos

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y); // celula a esquerda e abaixo da que estamos

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}
			return;
		}

		if ( (pos.x - next.x) == 1)				// proxima esta a esquerda
		{
			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y);

			if (labirinto[t.y][t.x] == -1) 		
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);

			if (labirinto[t.y][t.x] == -1) 	
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,1).x, pos.move(1,1).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,-1).x, pos.move(1,-1).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}
			return;
		}

		if ( (next.y - pos.y) == 1) 			// proxima esta em baixo
		{
			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,-1).x, pos.move(1,-1).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}
			return;
		}

		if ( (pos.y - next.y) == 1) 			// proxima esta em cima
		{
			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (labirinto[t.y][t.x] == -1)
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}

			t.setCoord(pos.move(1,1).x, pos.move(1,1).y);

			if (labirinto[t.y][t.x] == -1) 
			{
				labirinto[t.y][t.x] = PAREDE;
				counter--;
			}
			return;
		}
	}

	/*
	 * Esta funcao ve se a partir da posicao atual existe celulas vazias a 
	 * exatamente 2 celulas de distancia. Guarda os movimentos validos e retorna
	 * um deles (random), se nao houver mov. validos retorna uma coordenada nula
	 */
	private Coord findParedeFina(Coord pos) {

		Coord moves[] = new Coord[4]; // array para movimentos validos
		int i = 0; // contador de movimentos validos

		if (pos.x > 1 && labirinto[pos.move(-2,0).y][pos.move(-2,0).x] == -1)
		{
			moves[i] = pos.move(-1,0);
			i++;
		}
		if (pos.x < (size-2) && labirinto[pos.move(2,0).y][pos.move(2,0).x] == -1)
		{
			moves[i] = pos.move(1,0);
			i++;
		}
		if(pos.y > 1 && labirinto[pos.move(0,-2).y][pos.move(0,-2).x] == -1)
		{
			moves[i] = pos.move(0,-1);
			i++;
		}
		if(pos.y < (size-2) && labirinto[pos.move(0,2).y][pos.move(0,2).x] == -1)
		{
			moves[i] = pos.move(0,1);
			i++;
		}

		if ( i == 0)
		{
			Coord nula = new Coord();
			return nula;
		}
		else
			return moves[rand.nextInt(i)];
	}

	/*
	 * Esta funcao tem duas condicoes
	 * - a posicao deve ser um caminho (0)
	 * - o novo movimento deve ser diferente do antigo para 
	 * 	 nao andarmos para tras e para a frente no mesmo sitio
	 */
	private Coord andarBack(Coord pos, Coord back) {

		Coord t;
		Coord moves[] = new Coord[4]; 	// array de movimentos validos

		int numMovimentosValidos = 0;

		t = pos.move(0, -1); 

		if (labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(0, 1);

		if (labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(1,0);

		if (labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(-1,0);

		if (labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		if ( numMovimentosValidos == 0)
		{
			Coord nula = new Coord();
			return nula;
		}
		else
			return moves[rand.nextInt(numMovimentosValidos)];
	}

	/* 
	 * Esta funcao procura nas 4 direcoes possiveis e se as condicoes
	 * sao cumpridas, ele escolhe uma dessas direcoes ao calhas.
	 * - Deve ser uma celula vazia (-1)
	 * - Deve estar dentro do labirinto
	 * - Se estamos numa coluna/linha impar ou se nao e uma mudanca da direcao atual (para nao ter escadas, ou paredes duplas)
	 */
	private Coord proximoMove(Coord pos) {

		Coord moves[] = new Coord[3]; 	// movimentos possiveis
		int numMovimentosValidos = 0; 

		if (pos.x > 0 && labirinto[pos.move(-1,0).y][pos.move(-1,0).x] == -1 && ( (pos.y % 2) != 0 || labirinto[pos.move(1,0).y][pos.move(1,0).x] == 0) )
		{
			moves[numMovimentosValidos] = pos.move(-1,0);
			numMovimentosValidos++;
		}
		if (pos.x < (size-1) && labirinto[pos.move(1,0).y][pos.move(1,0).x] == -1 && ( (pos.y % 2) != 0 || labirinto[pos.move(-1,0).y][pos.move(-1,0).x] == 0 ))
		{
			moves[numMovimentosValidos] = pos.move(1,0);
			numMovimentosValidos++;
		}
		if (pos.y > 0 && labirinto[pos.move(0,-1).y][pos.move(0,-1).x] == -1 && ( (pos.x % 2) != 0 || labirinto[pos.move(0,1).y][pos.move(0,1).x] == 0) )
		{
			moves[numMovimentosValidos] = pos.move(0,-1);
			numMovimentosValidos++;
		}
		if (pos.y < (size-1) && labirinto[pos.move(0,1).y][pos.move(0,1).x] == -1 && ( (pos.x % 2) != 0 || labirinto[pos.move(0,-1).y][pos.move(0,-1).x] == 0))
		{
			moves[numMovimentosValidos] = pos.move(0,1);
			numMovimentosValidos++;
		}

		if ( numMovimentosValidos == 0 )
		{
			Coord nula = new Coord();
			return nula; 					// retorna um com coordenadas nulas
		}
		else
			return moves[rand.nextInt(numMovimentosValidos)];  // retorna um dos movimentos validos random
	}
	
	public void colocarEspada()
	{
		boolean espadaColocada = false;
		
		while (!espadaColocada) // repete ate colocar a espada 
		{
			/* calcula uma coordenada random dentro dos valores do labirinto */
			espada = new Espada(rand.nextInt((size-2))+1, rand.nextInt((size-2))+1);
			
			/* verifica se a posicao esta livre */
			if (labirinto[espada.getPos().y][espada.getPos().x] == CHAO)
			{
				espadaColocada = true;
				labirinto[espada.getPos().y][espada.getPos().x] = ESPADA; 
			}
		}
	}
	
	public void colocarDragao()
	{
		while (NUM_DRAGOES > 0) // enquanto houver dragoes por colocar
		{
			boolean dragaoColocado = false;
			Dragao dragao = new Dragao(0, 0, false);
		
			while (!dragaoColocado)
			{
				/* calcula uma coordenada random dentro dos valores do labirinto */
				dragao.setPos(rand.nextInt((size-2))+1, rand.nextInt((size-2))+1);
				
				/* verifica se a posicao esta livre e se nao fica imediatamente ao lado de uma parede externa */
				if (labirinto[dragao.getPos().y][dragao.getPos().x] == CHAO && (dragao.getPos().y > 1) && (dragao.getPos().y < (size-2)) 
																			&& (dragao.getPos().x > 1) && (dragao.getPos().x < (size-2)) )
				{
					dragaoColocado = true;
					labirinto[dragao.getPos().y][dragao.getPos().x] = DRAGAO;
					dragoes.add(dragao);
				}
			}
			NUM_DRAGOES--;
		}
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
