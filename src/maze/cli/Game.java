package maze.cli;

import java.util.Scanner;

import maze.gui.*;
import maze.logic.CustomBuilder;
import maze.logic.Labirinto;
import maze.logic.PreDefBuilder;

public class Game {
	
	public static void main(String[] args)
	{
		// declaracao de variaveis e inicializacao de objetos necessarios
		int tamanho = 0, opcao = 0, tactica = 0;
		String str = new String("");
		boolean escolhaValida = false;
		Scanner input = new Scanner(System.in);
		Labirinto maze;
		
		// iniciar interface grafica
		new InitialMenu();
		
		// continuar com o modo consola
		// escolha do labirinto pre-definido ou do automatico com tamanho variavel
		do {
			System.out.println("Deseja jogar com o labirinto pre-definido, ou com um gerado automaticamente de tamanho variavel?\n(pre/auto): ");
			str = input.next();
			
			if (str.equalsIgnoreCase("pre"))
			{
				escolhaValida = true;
				opcao = 2;
			}
			else if (str.equalsIgnoreCase("auto"))
			{
				escolhaValida = true;
				opcao = 1;
			}
			else
				System.out.println("A opcao escolhida nao e valida!\n");
			
		} while (!escolhaValida);
		
		escolhaValida = false;
		
		// escolha da tactica que o jogador pretende usar
		do {
			System.out.println("\nQue tactica deseja utilizar? (digite o numero da opcao desejada)\n\n1 - Dragao sempre acordado\n2 - Dragao sempre adormecido\n3 - Misto das duas anteriores");
			str = input.next();
			
			try {
				tactica = Integer.parseInt(str);
				if (tactica > 0 && tactica < 4)
					escolhaValida = true;
				else
					System.out.println("Valor invalido\n");
			}
			catch (NumberFormatException e) {
				System.out.println("Valor invalido\n");
			}
		} while (!escolhaValida);
		
		escolhaValida = false;
		
		// escolha do tamanho do labirinto
		if (opcao == 1)
			do {
				System.out.println("\nQue tamanho deve ter o labirinto? (aconselhamos um valor superior a 15 para uma melhor experiencia)");
				str = input.next();
				try {
					tamanho = Integer.parseInt(str);
					if (tamanho > 0)
						escolhaValida = true;
				}
				catch (NumberFormatException e) {
					System.out.println("Valor invalido\n");
				}
				
			} while(!escolhaValida);
		
		if (opcao == 2) // quer jogar com labirinto pre-definido
		{
			maze = new PreDefBuilder(tactica)
					.construirLabirinto()
					.colocarDragao()
					.colocarEspada()
					.colocarHeroi()
					.colocarSaida()
					.getLabirinto();
		}
		else // quer jogar com um labirinto de tamanho variavel
		{
			maze = new CustomBuilder(tamanho, 2, tactica)
					.construirLabirinto()
					.colocarDragao()
					.colocarEspada()
					.getLabirinto();
		}
		
		// motor do jogo
		do {
			clearConsole();
			System.out.println(maze);
			
			escolhaValida = false;

			// escolha da direcao que quer seguir
			while (!escolhaValida)
			{
				System.out.println("\nQue direcao quer seguir?");
				System.out.println("W -> cima, S -> baixo, A -> esquerda, D -> direita");

				str = input.next();
				
				if(str.equalsIgnoreCase("w") || str.equalsIgnoreCase("s")
						|| str.equalsIgnoreCase("a") || str.equalsIgnoreCase("d"))
					escolhaValida = true;
				else
					System.out.println("A direcao escolhida nao e valida! Tente outra vez.");
			}
			
			// movimento do heroi e do dragao consoante tactica utilizada
			maze.moverHeroi(str);
			
			if (!maze.dragoesTodosMortos())
			{
				maze.avaliaTactica();
				if ( !maze.dragoesAdormecidos() )
					maze.moverDragao("");
				maze.pertoDragao();
			}
			escolhaValida = false;
			
		} while (!maze.getChegouFim() && !maze.getHeroi().estaMorto());
		
		// terminou o jogo
		clearConsole();
		System.out.println(maze);
		
		if (!maze.getHeroi().estaMorto())
		{
			System.out.println("\nParabens, conseguiste sair do labirinto!");
			if (maze.dragoesTodosMortos())
				System.out.println("Como conseguiste matar todos os dragoes recebes 20 moedas em ouro.");
		}
		else
			System.out.println("\nAproximaste-te demasiado do dragao, ficaste queimado!");
	}

	private static void clearConsole() {
		for(int i = 0 ; i < 50 ; i++) 
			System.out.println();
	}
}
