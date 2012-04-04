package maze.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import maze.logic.*;

/**
 * Apresenta uma janela que permite ao jogador criar o seu pro'prio labirinto
 * @author migueloliveira
 */
public class CreateMaze extends JFrame implements MouseListener, WindowListener, ActionListener {
	
	private static final long serialVersionUID = 3981003364342962426L;
	
	private ConfigurationMaze configs;
	private JFrame parent;
	
	public Labirinto mazeFinished;
	private int[][] maze, visited;
	
	private JPanel labirinto, icons;
	private JLabel currentIconLabel;
	private String currentBlockName;
	private Icon currentIcon, wall20, groundBuild20, dragon20, dragonSleep20, hero20, closedExit20, sword20;
	
	private static final int CHAO_CONSTRUIR = 9;
	private static final int OUT_OF_BOUNDS = 20;
	private static final int NOT_VISITED = 0;
	private static final int VISITED = 1;
	
	private Heroi hero;
	private boolean heroPlaced, exitPlaced, swordPlaced, foundTheExit;
	private Coord exit;
	private Vector<Dragao> dragons;
	private Espada sword;
	
	private JRadioButton lWall, lHero, lDragon, lClosedExit, lSword;
	private JButton useThis, clearMaze, saveMaze;
	
	private JMenuBar menuBar;
	private JMenuItem barInstructions;
	private static boolean instructionsIsON = true;
	
	/**
	 * Construtor
	 * @param parent JFrame "dono" da nova janela
	 * @param configs Configuracoes atuais do Labirinto
	 */
	public CreateMaze (JFrame parent, ConfigurationMaze configs) {
		
		// inicializacao da janela
		super("Creation of new maze");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		// inicializacao de variaveis
		this.configs = configs;
		this.parent = parent;
		dragons = new Vector<Dragao>();
		
		// inicializar os elementos da janela
		initIcons();
		initMaze();
		initOuterWalls();
		loadMazeLabels();
		initIconBar();
		initMenuBar();
		
		// mostrar janela e meter invisivel a parent
		parent.setVisible(false);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		this.addWindowListener(this);
		if (instructionsIsON) // mostra as instrucoes para criacao de labirintos
			new CreateMazeInstructions(this);
		setVisible(true);
	}

	/**
	 * Inicializa a barra de ferramentas da janela
	 */
	private void initMenuBar() {
		
		// criacao de novo menu bar com um item
		menuBar = new JMenuBar();		
		JMenu menu = new JMenu("Options");
		
		// determinar qual o texto a ser apresentado
		if (instructionsIsON)
			barInstructions = new JMenuItem("Turn instructions OFF");
		else
			barInstructions = new JMenuItem("Turn instructions ON");
		
		// adicionar um ActionListener e adicionar barra 'a janela
		barInstructions.setActionCommand("instructionsSwitch");
		barInstructions.addActionListener(this);
		menu.add(barInstructions);
		menuBar.add(menu);
		setJMenuBar(menuBar);
	}

	/**
	 * Classe que apresenta uma janela com as instrucoes para a criacao de labirintos
	 * @author migueloliveira
	 */
	private class CreateMazeInstructions extends JDialog implements MouseListener
	{
		private static final long serialVersionUID = -6595176294244544775L;
		private JPanel instructions;

		/**
		 * Construtor
		 * @param parent
		 */
		public CreateMazeInstructions(CreateMaze parent) {
 			// inicializacao do JDialog
			super(parent ,"How to create a custom maze", true);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			// adicao da imagem com instrucoes 'a janela
			Icon ins = new ImageIcon("images/createMazeInstructions.png");
			instructions = new JPanel();
			JLabel l = new JLabel(ins);
			l.addMouseListener(this);
			getContentPane().add(instructions.add(l));
			
			// apresentar janela
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			setVisible(false); // fica invisivel com clique do rato
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {}
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}

	/**
	 * Inicializa o painel que contem os botoes para a escolha dos elementos disponiveis
	 * e do painel que contem o elemento que esta seleccionado
	 */
	private void initIconBar() {
		
		// inicializacao de paineis
		icons = new JPanel(new GridLayout(0,1));
		JPanel currentPanel = new JPanel(new FlowLayout());
		JPanel availableChoices = new JPanel(new FlowLayout());
		
		// define o icone selecionado como o de PAREDE
		currentIcon = wall20;
		currentIconLabel = new JLabel (wall20);
		currentPanel.add(new JLabel("Current icon: "));
		currentPanel.add(currentIconLabel);
		
		// criacao dos botoes de radio com os icones dos elementos disponiveis
		lWall = new JRadioButton(wall20, true);
		lHero = new JRadioButton(hero20, false);
		if (configs.tactica == 2)
			lDragon = new JRadioButton (dragonSleep20, false);
		else
			lDragon = new JRadioButton (dragon20, false);
		lClosedExit = new JRadioButton (closedExit20, false);
		lSword = new JRadioButton (sword20, false);
		useThis = new JButton ("Use this maze");
		clearMaze = new JButton ("Clear maze");
		saveMaze = new JButton ("Save maze");
		
		// define as accoes de comando para cada botao
		lWall.setActionCommand("wall20");
		lHero.setActionCommand("hero20");
		if (configs.tactica == 2)
			lDragon.setActionCommand("dragonSleep20");
		else
			lDragon.setActionCommand("dragon20");
		lClosedExit.setActionCommand("closeExit20");
		lSword.setActionCommand("sword20");
		useThis.setActionCommand("useThis");
		clearMaze.setActionCommand("clearMaze");
		saveMaze.setActionCommand("saveMaze");
		
		// future feature
		saveMaze.setEnabled(false);
		
		// adicionar ActionListener a todos os botoes
		lWall.addActionListener(this);
		lHero.addActionListener(this);
		lDragon.addActionListener(this);
		lClosedExit.addActionListener(this);
		lSword.addActionListener(this);
		useThis.addActionListener(this);
		clearMaze.addActionListener(this);
		saveMaze.addActionListener(this);
	
		// adicionar botoes dos elementos ao painel respectivo
		availableChoices.add(lWall);
		availableChoices.add(lHero);
		availableChoices.add(lSword);
		availableChoices.add(lDragon);
		availableChoices.add(lClosedExit);
		availableChoices.add(clearMaze);
		availableChoices.add(saveMaze);
		availableChoices.add(useThis);
		
		// adicionar paineis 'a janela
		icons.add(currentPanel);
		icons.add(availableChoices);
		getContentPane().add(icons, BorderLayout.SOUTH);
	}
	
	/**
	 * Percorre o array com as informacoes do labirinto
	 * e preenche um painel com os icones dos elementos
	 * nas posicoes correctas
	 */
	private void loadMazeLabels() {
		
		labirinto = new JPanel(new GridLayout(0, 1, 0, 0));	// painel com uma coluna
		
		int numLabel = 0; // indice que vai ser atribuido 'as labels
		
		for (int i = 0; i < maze.length; i++) // percorrer os elementos do array
		{
			JPanel labirintoLinha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

			// percorre os elementos da linha atual do labirinto
			for (int j = 0; j < maze[i].length; j++, numLabel++)
				if (maze[i][j] == Labirinto.PAREDE)
				{
					JLabel parede = new JLabel(wall20);	// cria label
					parede.addMouseListener(this);
					parede.setName(""+numLabel);	// da-lhe como nome o seu indice atual no labirinto
					labirintoLinha.add(parede);
				}
				else if (maze[i][j] == CHAO_CONSTRUIR)
				{
					JLabel chao = new JLabel(groundBuild20);
					chao.addMouseListener(this);
					chao.setName(""+numLabel);
					labirintoLinha.add(chao);
				}
			labirinto.add(labirintoLinha);
		}
		getContentPane().add(labirinto, BorderLayout.CENTER); // adiciona painel do labirinto 'a janela
	}

	/**
	 * Cria o array com o tamanho do labirinto e inicializa
	 * os seus elementos como chao temporario
	 */
	private void initMaze() {

		maze = new int[configs.sizeMaze][configs.sizeMaze];
		
		for (int i = 1; i < (configs.sizeMaze-1); i++)
			for (int j = 1; j < (configs.sizeMaze-1); j++)
				maze[i][j] = CHAO_CONSTRUIR;
	}

	/**
	 * Inicializa os icones que vao ser necessarios
	 */
	private void initIcons() {
		wall20 = new ImageIcon("images/wall20.png");
		groundBuild20 = new ImageIcon("images/groundBuild20.png");
		dragon20 = new ImageIcon("images/dragon20.png");
		dragonSleep20 = new ImageIcon("images/dragonSleep20.png");
		hero20 = new ImageIcon("images/hero20.png");
		closedExit20 = new ImageIcon("images/closeExit20.png");
		sword20 = new ImageIcon("images/sword20.png");
	}

	/**
	 * Inicializa as parades exteriores do labirinto no array
	 */
	private void initOuterWalls() {
		for (int i = 0; i < configs.sizeMaze; i++)
		{
			maze[i][0] = Labirinto.PAREDE;
			maze[i][(configs.sizeMaze-1)] = Labirinto.PAREDE;
			
			if (i == 0 || i == (configs.sizeMaze-1))
				for (int j = 1; j < (configs.sizeMaze-1); j++)
					maze[i][j] = Labirinto.PAREDE;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		// calcula a posicao da celula que recebeu o clique no array do labirinto
		int numCell = Integer.parseInt(((JLabel)e.getSource()).getName());
		int xCell = numCell % configs.sizeMaze; // coluna
		int yCell = numCell / configs.sizeMaze; // linha

		boolean okToPlace = false, errorDisplayed = false;

		if (e.getButton() == MouseEvent.BUTTON1) // botao esquerdo do rato
		{
			if (currentBlockName == "hero20") // se esta a meter o heroi
			{
				if (exitPlaced) // e se ja foi colocada a saida
				{
					if (xCell == exit.x || yCell == exit.y) // verifica se esta a meter na mesma parede ou linha/coluna
					{
						errorDisplayed = true;
						JOptionPane.showMessageDialog(null, "You can't place the hero on the same wall as the exit, or same column/line!!", "", JOptionPane.ERROR_MESSAGE);
					}
					else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell)) // se esta numa parede externa e nao e um canto
						okToPlace = true;
				}
				else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
					okToPlace = true;

				if(okToPlace) // pode colocar
				{
					hero = new Heroi(xCell, yCell); // instancia um Heroi
					heroPlaced = true;
					lHero.setEnabled(false); // desactiva o botao do heroi
					maze[yCell][xCell] = Labirinto.HEROI;
					((JLabel)e.getSource()).setIcon(currentIcon);
					resetToWall(); // volta a colocar o elemento atual como parede
				}
				else if(!errorDisplayed)
					JOptionPane.showMessageDialog(null, "You can't place the hero there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else if (currentBlockName == "sword20") // se esta a meter a espada
			{
				if (!isExteriorWall(xCell,yCell)) // nao e' uma parede externa 
				{
					if (maze[yCell][xCell] == Labirinto.PAREDE || maze[yCell][xCell] == CHAO_CONSTRUIR) // e uma parede ou chao temporaraio
					{
						sword = new Espada(xCell,yCell); // instancia uma Espada
						swordPlaced = true;
						lSword.setEnabled(false); // desactiva o botao da espada
						maze[yCell][xCell] = Labirinto.ESPADA;
						((JLabel)e.getSource()).setIcon(currentIcon);
						resetToWall();
					}
					else
						JOptionPane.showMessageDialog(null, "You can't place the sword on top of a dragon!", "", JOptionPane.ERROR_MESSAGE);
				}
				else
					JOptionPane.showMessageDialog(null, "You can't place the sword there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else if (currentBlockName == "closeExit20") // se esta a meter a saida
			{
				if (heroPlaced) // o heroi ja foi colocado
				{
					if (xCell == hero.getPos().x || yCell == hero.getPos().y) // verifica se esta a meter na mesma parede ou linha/coluna
					{
						errorDisplayed = true;
						JOptionPane.showMessageDialog(null, "You can't place the exit on the same wall as the hero, or same column/line!", "", JOptionPane.ERROR_MESSAGE);
					}
					else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell)) // se e parede externa e nao e' canto
						okToPlace = true;
				}
				else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
					okToPlace = true;

				if (okToPlace)
				{
					exit = new Coord(xCell, yCell); // instancia a saida
					exitPlaced = true;
					lClosedExit.setEnabled(false); // desactiva o botao da saida
					maze[yCell][xCell] = Labirinto.SAIDA;
					((JLabel)e.getSource()).setIcon(currentIcon);
					resetToWall();
				}
				else if (!errorDisplayed)
					JOptionPane.showMessageDialog(null, "You can't place the exit there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else if (currentBlockName == "dragon20" || currentBlockName == "dragonSleep20") // se esta a meter um dragao
			{
				if (!isExteriorWall(xCell,yCell)) // se nao e parede externa
				{
					if ( ((xCell > 1) && (yCell < (configs.sizeMaze-2))) && ((yCell > 1) && (xCell < (configs.sizeMaze-2))) ) // nao deixa colocar no primeiro anel do labirinto
					{
						if (maze[yCell][xCell] == Labirinto.PAREDE || maze[yCell][xCell] == CHAO_CONSTRUIR) // se e parede ou chao temporario
						{
							// coloca dragao, dependendo da tactiva activa
							Dragao d;
							okToPlace = true;

							if (configs.tactica == 2)
								d = new Dragao(xCell,yCell,true);
							else
								d = new Dragao (xCell,yCell,false);

							dragons.add(d);
							maze[yCell][xCell] = Labirinto.DRAGAO;
							((JLabel)e.getSource()).setIcon(currentIcon);

							if (dragons.size() == configs.numberDragons) // se ja colocou todos os dragoes disponiveis
							{
								lDragon.setEnabled(false); // desactiva o botao do dragao
								resetToWall();
							}
						}
						else // tentar colocar por cima de uma espada
						{
							errorDisplayed = true;
							JOptionPane.showMessageDialog(null, "You can't place a dragon on top of the sword!", "", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				if (!okToPlace && !errorDisplayed)
					JOptionPane.showMessageDialog(null, "You can't place the dragon there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else // currentBlock is wall
			{
				if (heroPlaced && hero.getPos().x == xCell && hero.getPos().y == yCell) // colocar por cima do heroi
				{
					heroPlaced = false;
					lHero.setEnabled(true); // activa o botao do heroi
				}
				else if (exitPlaced && exit.x == xCell && exit.y == yCell) // colocar por cima de uma saida
				{
					exitPlaced = false;
					lClosedExit.setEnabled(true); // activa o botao da saida
				}
				else if (swordPlaced && sword.getPos().x == xCell && sword.getPos().y == yCell) // colocar por cima de uma espada
				{
					swordPlaced = false;
					lSword.setEnabled(true); // activa o botao da espada
				}
				else // colocar por cima de um dragao ou de outra parede
				{
					for (int i = 0; i < dragons.size(); i++) // percorre os dragoes
						if (dragons.get(i).getPos().x == xCell && dragons.get(i).getPos().y == yCell)
						{
							dragons.remove(i);
							lDragon.setEnabled(true); // activa o botao do dragao
						}
				}
					
				((JLabel)e.getSource()).setIcon(currentIcon);
				maze[yCell][xCell] = Labirinto.PAREDE;
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3) // botao direito do rato - anular
		{
			if (isExteriorWall(xCell, yCell)) // posicao e numa parede externa
			{
				if (heroPlaced && hero.getPos().x == xCell && hero.getPos().y == yCell) // se o heroi esta nessa posicao
				{
					heroPlaced = false;
					lHero.setEnabled(true); // activa o botao do heroi
					maze[yCell][xCell] = Labirinto.PAREDE;
				}
				else if (exitPlaced && exit.x == xCell && exit.y == yCell) // se a saida esta nessa posicao
				{
					exitPlaced = false;
					lClosedExit.setEnabled(true); // activa o botao da saida
					maze[yCell][xCell] = Labirinto.PAREDE;
				}
				((JLabel)e.getSource()).setIcon(wall20);
			}
			else // se e dentro do labirinto que se esta a anular
			{
				// verifica se algum dragao esta colocado nessa posicao
				for (int i = 0; i < dragons.size(); i++)
					if (dragons.get(i).getPos().x == xCell && dragons.get(i).getPos().y == yCell)
					{
						dragons.remove(i);
						lDragon.setEnabled(true);
						maze[yCell][xCell] = CHAO_CONSTRUIR;
					}
				// ou se ha uma espada colocada nessa posicao
				if (swordPlaced && sword.getPos().x == xCell && sword.getPos().y == yCell)
				{
					swordPlaced = false;
					lSword.setEnabled(true);
					maze[yCell][xCell] = CHAO_CONSTRUIR;
				}

				((JLabel)e.getSource()).setIcon(groundBuild20);
				maze[yCell][xCell] = CHAO_CONSTRUIR;
			}
		}
	}
	
	/**
	 * Verifica se determinada posicao esta na parede externa
	 * @param xCell coordenada X
	 * @param yCell coordenada Y
	 * @return se e' parede externa ou nao
	 */
	private boolean isExteriorWall(int xCell, int yCell) {
		if ((xCell == 0 || xCell == (configs.sizeMaze-1)) || (yCell == 0 || yCell == (configs.sizeMaze-1)))
			return true;
		return false;
	}

	/**
	 * Verifica se determinada posicao e um canto do labirinto
	 * @param x coordenada X
	 * @param y coordenada Y
	 * @return se e' um canto ou nao
	 */
	private boolean isCorner(int x, int y) {
		if ( (x == 0 && y == 0)
				|| (x == (configs.sizeMaze-1) && y == 0)
					|| (x == 0 && y == (configs.sizeMaze-1))
						|| (x == (configs.sizeMaze-1) && y == (configs.sizeMaze-1)) )
			return true;
		return false;
	}
	
	/**
	 * Volta a colocar o elemento selecionado como
	 * parede
	 */
	private void resetToWall () {
		currentIconLabel.setIcon(wall20);
		currentIcon = currentIconLabel.getIcon();
		currentBlockName = "wall20";
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "useThis") // usar o labirinto construido
		{
			// verifica se existe pelo menos um caminho entre o heroi e a saida
			initializeVisited();
			if (findExit(hero.getPos().x, hero.getPos().y))
			{
				// se os eleemntos foram todos colocados
				if (heroPlaced && swordPlaced && exitPlaced && (dragons.size() == configs.numberDragons))
					finishMaze();
				else
					JOptionPane.showMessageDialog(null, "There are elements missing from the maze. Complete it!", "", JOptionPane.ERROR_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(null, "This maze is impossible to complete! Try a different one.", "", JOptionPane.ERROR_MESSAGE);
		}
		else if (e.getActionCommand() == "clearMaze") // limpar o labirinto construido
		{
			initMaze();
			initOuterWalls();
			initMazePanel();
			dragons.clear();
			heroPlaced = false;
			exitPlaced = false;
			swordPlaced = false;
			lHero.setEnabled(true);
			lDragon.setEnabled(true);
			lClosedExit.setEnabled(true);
			lSword.setEnabled(true);
		}
		else if (e.getActionCommand() == "instructionsSwitch") // ligar/desligar o aparecimento das instrucoes
		{
			if (instructionsIsON)
			{
				barInstructions.setText("Turn instructions ON");
				instructionsIsON = false;
			}
			else
			{
				barInstructions.setText("Turn instructions OFF");
				instructionsIsON = true;
			}
		}
		else // foi escolhido outro elemento para colocar
		{
			currentIconLabel.setIcon(new ImageIcon("images/" + e.getActionCommand() + ".png"));
			currentIcon = currentIconLabel.getIcon();
			currentBlockName = e.getActionCommand();
		}
	}
	
	/**
	 * Reinicializa o painel do labirinto para a situacao inicial
	 */
	private void initMazePanel() {
		
		for (int i = 1; i < (configs.sizeMaze-1); i++)
			for (int j = 1; j < (configs.sizeMaze-1); j++)
				((JLabel)((JPanel)labirinto.getComponent(i)).getComponent(j)).setIcon(groundBuild20);
		
		for (int i = 0; i < configs.sizeMaze; i++)
		{
			((JLabel)((JPanel)labirinto.getComponent(i)).getComponent(0)).setIcon(wall20);
			((JLabel)((JPanel)labirinto.getComponent(i)).getComponent(configs.sizeMaze-1)).setIcon(wall20);
			
			if (i == 0 || i == (configs.sizeMaze-1))
				for (int j = 1; j < (configs.sizeMaze-1); j++)
					((JLabel)((JPanel)labirinto.getComponent(i)).getComponent(j)).setIcon(wall20);
		}
	}

	/**
	 * Instancia um Labirinto e um GraphicGame com o mesmo
	 */
	private void finishMaze() {
		
		// troca o restante chao temporario por caminho definitivo
		for (int i = 1; i < (configs.sizeMaze-1); i++)
			for (int j = 1; j < (configs.sizeMaze-1); j++)
				if (maze[i][j] == CHAO_CONSTRUIR)
					maze[i][j] = Labirinto.CHAO;
		
		// inicia jogo com interface grafica com o labirinto criado
		mazeFinished = new Labirinto(configs, maze, hero, sword, dragons);
		setVisible(false);
		new GraphicGame(mazeFinished, configs);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// se a janela for fechada, deve meter menu inicial visivel
		parent.setVisible(true);
	}
	
	/**
	 * Retorna o elemento que esta na posicao recebida
	 * @param x coluna no labirinto
	 * @param y linha no labirinto
	 * @return int elemento da posicao no labirinto
	 */
	int getMazeValue(int x, int y) {
		return maze[y][x];
	}

	/**
	 * Retorna o elemento 'a direita da posicao atual se 
	 * esse existir
	 * @param x coluna no labirinto
	 * @param y linha no labirinto
	 * @return int
	 */
	int goRight(int x, int y) {
		if ( x == (configs.sizeMaze-1) )
			return OUT_OF_BOUNDS;
		return (getMazeValue(x + 1, y));
	}

	/**
	 * Retorna o elemento 'a esquerda da posicao atual se 
	 * esse existir
	 * @param x coluna no labirinto
	 * @param y linha no labirinto
	 * @return int
	 */
	int goLeft(int x, int y) {
		if ( x == 0)
			return OUT_OF_BOUNDS;
		return (getMazeValue(x - 1, y));
	}

	/**
	 * Retorna o elemento acima da posicao atual se 
	 * esse existir
	 * @param x coluna no labirinto
	 * @param y linha no labirinto
	 * @return int
	 */
	int goUp(int x, int y) {
		if (y == 0)
			return OUT_OF_BOUNDS;
		return (getMazeValue(x, y - 1));
	}

	/**
	 * Retorna o elemento abaixo da posicao atual se 
	 * esse existir
	 * @param x coluna no labirinto
	 * @param y linha no labirinto
	 * @return int
	 */
	int goDown(int x, int y) {
		if ( y == (configs.sizeMaze-1))
			return OUT_OF_BOUNDS;
		return (getMazeValue(x, y + 1));
	}

	/**
	 * Utiliza um algoritmo de retrocesso para verificar
	 * se a partir da posicao do heroi e' possivel chegar
	 * a saida do labirinto
	 * @param x coluna da posicao atual
	 * @param y linha da posicao atual
	 * @return boolean se existe caminho ou nao
	 */
	private boolean findExit(int x, int y)
	{
		if (getMazeValue(x,y) == Labirinto.SAIDA ) // encontrou a saida
		{
			foundTheExit = true;
			return true;
		}
		else
		{
			visited[y][x] = VISITED; // marca como visitado

			// vai para a direita
			if (goRight(x,y) != OUT_OF_BOUNDS && goRight(x,y) != Labirinto.PAREDE && visited[y][x+1] == NOT_VISITED)
				findExit(x+1,y);

			if(foundTheExit)
				return true;

			// vai para a esquerda
			if (goLeft(x,y) != OUT_OF_BOUNDS && goLeft(x,y) != Labirinto.PAREDE && visited[y][x-1] == NOT_VISITED)
				findExit(x-1,y);

			if(foundTheExit)
				return true;

			// vai para cima
			if (goUp(x,y) != OUT_OF_BOUNDS && goUp(x,y) != Labirinto.PAREDE && visited[y-1][x] == NOT_VISITED)
				findExit(x,y-1);

			if(foundTheExit)
				return true;

			// vai para baixo
			if (goDown(x,y) != OUT_OF_BOUNDS && goDown(x,y) != Labirinto.PAREDE && visited[y+1][x] == NOT_VISITED)
				findExit(x,y+1);

			if(foundTheExit)
				return true;
			return false;
		}
	}
	
	/**
	 * inicializa o array utilizado para 
	 * verificar se a celula correspondente no
	 * labirinto ja foi visitada
	 */
	private void initializeVisited() {
		visited = new int[configs.sizeMaze][configs.sizeMaze];

		for (int i = 0; i < configs.sizeMaze; i++)
			for (int j = 0; j < configs.sizeMaze; j++)
				visited[i][j] = NOT_VISITED;
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {}
	@Override
	public void mouseExited(MouseEvent arg0) {}
	@Override
	public void mousePressed(MouseEvent arg0) {}
	@Override
	public void mouseReleased(MouseEvent arg0) {}
	@Override
	public void windowActivated(WindowEvent arg0) {}
	@Override
	public void windowClosed(WindowEvent arg0) {}
	@Override
	public void windowDeactivated(WindowEvent arg0) {}
	@Override
	public void windowDeiconified(WindowEvent arg0) {}
	@Override
	public void windowIconified(WindowEvent arg0) {}
	@Override
	public void windowOpened(WindowEvent arg0) {}
}
