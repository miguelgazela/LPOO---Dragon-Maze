package maze.logic;

public class CustomBuilder extends Builder {

	public CustomBuilder (int tamanho, int numberDragons, int tactica) {
		super();
		maze.tactica = tactica;
		maze.size = tamanho;
		maze.NUM_DRAGOES = numberDragons;
	}
	
	@Override
	public CustomBuilder colocarDragao() {
	
		while (maze.NUM_DRAGOES > 0) // enquanto houver dragoes por colocar
		{
			boolean dragaoColocado = false;
			Dragao dragao = new Dragao(0, 0, false);
		
			while (!dragaoColocado)
			{
				/* calcula uma coordenada random dentro dos valores do labirinto */
				dragao.setPos(maze.rand.nextInt((maze.size-2))+1, maze.rand.nextInt((maze.size-2))+1);
				
				/* verifica se a posicao esta livre e se nao fica imediatamente ao lado de uma parede externa */
				if (maze.labirinto[dragao.getPos().y][dragao.getPos().x] == Labirinto.CHAO && (dragao.getPos().y > 1) && (dragao.getPos().y < (maze.size-2)) 
																			&& (dragao.getPos().x > 1) && (dragao.getPos().x < (maze.size-2)) )
				{
					dragaoColocado = true;
					maze.labirinto[dragao.getPos().y][dragao.getPos().x] = Labirinto.DRAGAO;
					maze.dragoes.add(dragao);
				}
			}
			maze.NUM_DRAGOES--;
		}
		
		if (maze.tactica == 2)		// dragoes sempre adormecidos
			for (int i = 0; i < maze.dragoes.size(); i++)
				maze.dragoes.elementAt(i).adormece();
		
		return this;
	}

	@Override
	public CustomBuilder colocarEspada() {

		boolean espadaColocada = false;

		while (!espadaColocada) // repete ate colocar a espada 
		{
			/* calcula uma coordenada random dentro dos valores do labirinto */
			maze.espada = new Espada(maze.rand.nextInt((maze.size-2))+1, maze.rand.nextInt((maze.size-2))+1);

			/* verifica se a posicao esta livre */
			if (maze.labirinto[maze.espada.getPos().y][maze.espada.getPos().x] == Labirinto.CHAO)
			{
				espadaColocada = true;
				maze.labirinto[maze.espada.getPos().y][maze.espada.getPos().x] = Labirinto.ESPADA; 
			}
		}

		return this;
	}
	
	@Override
	public CustomBuilder colocarHeroi() {
		
		maze.heroi = new Heroi(0,0);
		maze.heroi.setPos(maze.rand.nextInt(3)+1, 0);

		switch(maze.heroi.getPos().x)
		{
		case 1: // parede esquerda
			maze.heroi.setPos(0, 1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1)); // y para nao ficar nos cantos
			break;
		case 2: // parede de cima ou de baixo
			maze.heroi.setPos(1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1), (maze.size-1) * maze.rand.nextInt(2));
			break;
		case 3: // parede direita
			maze.heroi.setPos(maze.size-1, 1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1));
			break;
		}
		
		return this;
	}
	
	public CustomBuilder colocarSaida() {
		
		maze.Saida.x = maze.heroi.getPos().x;

		while (maze.Saida.x == maze.heroi.getPos().x || maze.Saida.y == maze.heroi.getPos().y) // nao pode ficar na mesma parede/coluna/linha que o Heroi
		{
			maze.Saida.x = maze.rand.nextInt(3)+1;

			switch(maze.Saida.x)
			{
			case 1: // parede esquerda
				maze.Saida.x = 0;
				maze.Saida.y = 1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1);
				break;
			case 2: // parede de cima ou de baixo
				maze.Saida.x = 1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1);
				maze.Saida.y = (maze.size-1) * maze.rand.nextInt(2);
				break;
			case 3: // parede direita
				maze.Saida.x = (maze.size-1);
				maze.Saida.y = 1 + 2 * maze.rand.nextInt(((maze.size-3)/2)+1);
			}
		}
		
		return this;
	}

	@Override
	public CustomBuilder construirLabirinto() {
		
		if ( maze.size<5 ) maze.size = 5; 			// tamanho minimo do labirinto
		if ( maze.size % 2 == 0 ) maze.size++; 		// deve ser impar para o algoritmo funcionar em condicoes
		if ( maze.size>200 ) maze.size = 199; 		// fica muito lento acima

		maze.counter = (maze.size-2)*(maze.size-2); 		// numero de celulas que precisa visitar
		
		maze.labirinto = new int[maze.size][maze.size];

		// limpar o lixo da superficie do labirinto

		for (int i = 1; i < (maze.size-1); i++)
			for (int j = 1; j <(maze.size-1); j++)
				maze.labirinto [i][j] = -1;

		// construir as paredes do labirinto

		for (int i = 0; i < 2; i++)
			for (int j = 0; j < maze.size; j++)
				maze.labirinto [i*(maze.size-1)][j] = Labirinto.PAREDE;

		for (int i = 0; i < maze.size; i++)
			for (int j = 0; j < 2; j++)
				maze.labirinto[i][j*(maze.size-1)] = Labirinto.PAREDE;
		
		colocarHeroi();
		colocarSaida();
		
		Coord pos = new Coord();  	// aponta sempre para a posicao atual
		Coord next = new Coord();	// proxima posicao (especie de guia, ve se podemos ir ou nao)
		Coord back = new Coord();	// aponta sempre para o proximo movimento de retrocesso (evitar andar para a frente e para tras no mesmo sitio)
		Coord old = new Coord(); 	// guarda temporariamente o valor de back num passo do algoritmo

		pos = maze.Saida; 				// comecar pela saida, fica mais dificil o labirinto

		next = proximoMove(pos); 	// damos o primeiro passo
		pos = next;

		maze.labirinto[pos.y][pos.x] = Labirinto.CHAO;
		maze.counter--;

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
					if (back.x <= 0 || back.x >= (maze.size-1) || back.y <= 0 || back.y >= (maze.size-1))
						back = old; 				// evita deixar o labirinto

					pos = old;
					next = findParedeFina(pos); 	// ve se ha algum caminho do outro lado de uma parede

					if (next.x != 0 || next.y != 0)
					{
						pos = next; 				// define a posicao atual para essa parede
						maze.labirinto[pos.y][pos.x] = 0; // torna-a caminho
					}
				}
			}
			else 									// se ha proximo movimento
			{
				construirParedes(pos,next); 		// pomos paredes a volta 
				pos = next; 			
				maze.labirinto[pos.y][pos.x] = Labirinto.CHAO; 		
				maze.counter--; 							
			}
		} while (maze.counter > 0); 						// enquanto ainda tem celulas a visitar

		// coloca o heroi e a saida no array

		maze.labirinto[maze.heroi.getPos().y][maze.heroi.getPos().x] = Labirinto.HEROI;
		maze.labirinto[maze.Saida.y][maze.Saida.x] = Labirinto.SAIDA; 
		
		return this;
	}
	
	private void construirParedes(Coord pos, Coord next) {

		Coord t = new Coord();
		
		// verifica a posicao das duas coordenadas uma em relacao a outra

		if ( (next.x - pos.x) == 1) 			// proxima esta a direita		
		{
			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y); // celula acima da que estamos

			if (maze.labirinto[t.y][t.x] == -1) 		// se a coordenada esta vazia
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE; 	// mete uma Labirinto.PAREDE
				maze.counter--; 						// decrementa o numero de celulas que falta visitar
			}

			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);  // celula abaixo da que estamos

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y); // celula a esquerda da que estamos

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,-1).x, pos.move(-1,-1).y); // celula a esquerda e acima da que estamos

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y); // celula a esquerda e abaixo da que estamos

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}
			return;
		}

		if ( (pos.x - next.x) == 1)				// proxima esta a esquerda
		{
			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y);

			if (maze.labirinto[t.y][t.x] == -1) 		
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);

			if (maze.labirinto[t.y][t.x] == -1) 	
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,1).x, pos.move(1,1).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,-1).x, pos.move(1,-1).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}
			return;
		}

		if ( (next.y - pos.y) == 1) 			// proxima esta em baixo
		{
			t.setCoord(pos.move(0,-1).x, pos.move(0,-1).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,-1).x, pos.move(1,-1).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}
			return;
		}

		if ( (pos.y - next.y) == 1) 			// proxima esta em cima
		{
			t.setCoord(pos.move(0,1).x, pos.move(0,1).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,0).x, pos.move(-1,0).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,0).x, pos.move(1,0).y);

			if (maze.labirinto[t.y][t.x] == -1)
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(-1,1).x, pos.move(-1,1).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
			}

			t.setCoord(pos.move(1,1).x, pos.move(1,1).y);

			if (maze.labirinto[t.y][t.x] == -1) 
			{
				maze.labirinto[t.y][t.x] = Labirinto.PAREDE;
				maze.counter--;
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

		if (pos.x > 1 && maze.labirinto[pos.move(-2,0).y][pos.move(-2,0).x] == -1)
		{
			moves[i] = pos.move(-1,0);
			i++;
		}
		if (pos.x < (maze.size-2) && maze.labirinto[pos.move(2,0).y][pos.move(2,0).x] == -1)
		{
			moves[i] = pos.move(1,0);
			i++;
		}
		if(pos.y > 1 && maze.labirinto[pos.move(0,-2).y][pos.move(0,-2).x] == -1)
		{
			moves[i] = pos.move(0,-1);
			i++;
		}
		if(pos.y < (maze.size-2) && maze.labirinto[pos.move(0,2).y][pos.move(0,2).x] == -1)
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
			return moves[maze.rand.nextInt(i)];
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

		if (maze.labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(0, 1);

		if (maze.labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(1,0);

		if (maze.labirinto[t.y][t.x] == 0 && t != pos)
		{
			moves[numMovimentosValidos] = t;
			numMovimentosValidos++;
		}

		t = pos.move(-1,0);

		if (maze.labirinto[t.y][t.x] == 0 && t != pos)
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
			return moves[maze.rand.nextInt(numMovimentosValidos)];
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

		if (pos.x > 0 && maze.labirinto[pos.move(-1,0).y][pos.move(-1,0).x] == -1 && ( (pos.y % 2) != 0 || maze.labirinto[pos.move(1,0).y][pos.move(1,0).x] == 0) )
		{
			moves[numMovimentosValidos] = pos.move(-1,0);
			numMovimentosValidos++;
		}
		if (pos.x < (maze.size-1) && maze.labirinto[pos.move(1,0).y][pos.move(1,0).x] == -1 && ( (pos.y % 2) != 0 || maze.labirinto[pos.move(-1,0).y][pos.move(-1,0).x] == 0 ))
		{
			moves[numMovimentosValidos] = pos.move(1,0);
			numMovimentosValidos++;
		}
		if (pos.y > 0 && maze.labirinto[pos.move(0,-1).y][pos.move(0,-1).x] == -1 && ( (pos.x % 2) != 0 || maze.labirinto[pos.move(0,1).y][pos.move(0,1).x] == 0) )
		{
			moves[numMovimentosValidos] = pos.move(0,-1);
			numMovimentosValidos++;
		}
		if (pos.y < (maze.size-1) && maze.labirinto[pos.move(0,1).y][pos.move(0,1).x] == -1 && ( (pos.x % 2) != 0 || maze.labirinto[pos.move(0,-1).y][pos.move(0,-1).x] == 0))
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
			return moves[maze.rand.nextInt(numMovimentosValidos)];  // retorna um dos movimentos validos random
	}
}
