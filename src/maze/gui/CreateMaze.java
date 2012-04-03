package maze.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;
import maze.logic.*;


public class CreateMaze extends JFrame implements MouseListener, WindowListener, ActionListener {
	
	private static final long serialVersionUID = 3981003364342962426L;
	
	private ConfigurationMaze configs;
	private JFrame parent;
	public Labirinto mazeFinished;
	private int[][] maze;
	private int[][] visited;
	
	private JPanel labirinto;
	private JPanel icons;
	private JLabel currentIconLabel;
	
	private static final int CHAO_CONSTRUIR = 9;
	private static final int OUT_OF_BOUNDS = 20;
	private static final int NOT_VISITED = 0;
	private static final int VISITED = 1;
	
	private boolean foundTheExit;
	
	private Heroi hero;
	private boolean heroPlaced, exitPlaced, swordPlaced;
	private Coord exit;
	private Vector<Dragao> dragons;
	private Espada sword;
	
	private JRadioButton lWall, lHero, lDragon, lClosedExit, lSword;
	private JButton useThis;
	private JButton clearMaze;
	private JButton saveMaze;
	
	private JMenuBar menuBar;
	private JMenuItem barInstructions;
	private static boolean instructionsIsON = true;

	private Icon currentIcon;
	private String currentBlockName;
	private Icon wall20, groundBuild20, dragon20, dragonSleep20, hero20, closedExit20, sword20;
	
	public CreateMaze (JFrame parent, ConfigurationMaze configs) {
		
		super("Creation of new maze");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setLayout(new BorderLayout());
		
		this.configs = configs;
		this.parent = parent;
		parent.setVisible(false);
		
		dragons = new Vector<Dragao>();
		
		initIcons();
		initMaze();
		initOuterWalls();
		loadMazeLabels();
		initIconBar();
		initMenuBar();
		
		// mostrar janela
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		this.addWindowListener(this);
		if (instructionsIsON)
			new CreateMazeInstructions(this);
		setVisible(true);
	}

	private void initMenuBar() {
		
		menuBar = new JMenuBar();		
		JMenu menu = new JMenu("Options");
		
		if (instructionsIsON)
			barInstructions = new JMenuItem("Turn instructions OFF");
		else
			barInstructions = new JMenuItem("Turn instructions ON");
		barInstructions.setActionCommand("instructionsSwitch");
		barInstructions.addActionListener(this);
		
		menu.add(barInstructions);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}

	private class CreateMazeInstructions extends JDialog implements MouseListener
	{
		private static final long serialVersionUID = -6595176294244544775L;
		private JPanel instructions;

		public CreateMazeInstructions(CreateMaze parent) {
			super(parent ,"How to create a custom maze", true);
			setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
			
			Icon ins = new ImageIcon("images/createMazeInstructions.png");
			instructions = new JPanel();

			JLabel l = new JLabel(ins);
			l.addMouseListener(this);
			getContentPane().add(instructions.add(l));
			
			pack();
			setResizable(false);
			setLocationRelativeTo(null);
			setVisible(true);
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			setVisible(false);
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

	private void initIconBar() {
		
		icons = new JPanel(new GridLayout(0,1));
		
		JPanel currentPanel = new JPanel(new FlowLayout());
		currentIcon = wall20;
		currentIconLabel = new JLabel (wall20);
		currentPanel.add(new JLabel("Current icon: "));
		currentPanel.add(currentIconLabel);
		
		JPanel availableChoices = new JPanel(new FlowLayout());
		
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
		
		// TEMPORARIO
		saveMaze.setEnabled(false);
		
		lWall.addActionListener(this);
		lHero.addActionListener(this);
		lDragon.addActionListener(this);
		lClosedExit.addActionListener(this);
		lSword.addActionListener(this);
		useThis.addActionListener(this);
		clearMaze.addActionListener(this);
		saveMaze.addActionListener(this);
		
		availableChoices.add(lWall);
		availableChoices.add(lHero);
		availableChoices.add(lSword);
		availableChoices.add(lDragon);
		availableChoices.add(lClosedExit);
		availableChoices.add(clearMaze);
		availableChoices.add(saveMaze);
		availableChoices.add(useThis);
		
		icons.add(currentPanel);
		icons.add(availableChoices);
		
		getContentPane().add(icons, BorderLayout.SOUTH);
	}
	
private void loadMazeLabels() {
		
		labirinto = new JPanel(new GridLayout(0, 1, 0, 0));	// painel com uma coluna
		
		int numLabel = 0;
		
		for (int i = 0; i < maze.length; i++) // percorrer os elementos do array
		{
			JPanel labirintoLinha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

			for (int j = 0; j < maze[i].length; j++, numLabel++)
				if (maze[i][j] == Labirinto.PAREDE)
				{
					JLabel parede = new JLabel(wall20);
					parede.addMouseListener(this);
					parede.setName(""+numLabel);
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
		getContentPane().add(labirinto, BorderLayout.CENTER);
	}

	private void initMaze() {

		maze = new int[configs.sizeMaze][configs.sizeMaze];
		
		for (int i = 1; i < (configs.sizeMaze-1); i++)
			for (int j = 1; j < (configs.sizeMaze-1); j++)
				maze[i][j] = CHAO_CONSTRUIR;
	}

	private void initIcons() {
		// inicializar os icones 20x20
		wall20 = new ImageIcon("images/wall20.png");
		groundBuild20 = new ImageIcon("images/groundBuild20.png");
		dragon20 = new ImageIcon("images/dragon20.png");
		dragonSleep20 = new ImageIcon("images/dragonSleep20.png");
		hero20 = new ImageIcon("images/hero20.png");
		closedExit20 = new ImageIcon("images/closeExit20.png");
		sword20 = new ImageIcon("images/sword20.png");
	}

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

		int numCell = Integer.parseInt(((JLabel)e.getSource()).getName());
		int xCell = numCell % configs.sizeMaze;
		int yCell = numCell / configs.sizeMaze;

		boolean okToPlace = false, errorDisplayed = false;

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (currentBlockName == "hero20") // se esta a meter o heroi
			{
				if (exitPlaced)
				{
					if (xCell == exit.x || yCell == exit.y)
					{
						errorDisplayed = true;
						JOptionPane.showMessageDialog(null, "You can't place the hero on the same wall as the exit!", "", JOptionPane.ERROR_MESSAGE);
					}
					else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
						okToPlace = true;
				}
				else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
					okToPlace = true;

				if(okToPlace)
				{
					hero = new Heroi(xCell, yCell);
					heroPlaced = true;
					lHero.setEnabled(false);
					maze[yCell][xCell] = Labirinto.HEROI;
					((JLabel)e.getSource()).setIcon(currentIcon);
					resetToWall();
				}
				else if(!errorDisplayed)
					JOptionPane.showMessageDialog(null, "You can't place the hero there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else if (currentBlockName == "sword20")
			{
				if (!isExteriorWall(xCell,yCell))
				{
					if (maze[yCell][xCell] == Labirinto.PAREDE || maze[yCell][xCell] == CHAO_CONSTRUIR)
					{
						sword = new Espada(xCell,yCell);
						swordPlaced = true;
						lSword.setEnabled(false);
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
				if (heroPlaced)
				{
					if (xCell == hero.getPos().x || yCell == hero.getPos().y)
					{
						errorDisplayed = true;
						JOptionPane.showMessageDialog(null, "You can't place the exit on the same wall as the hero!", "", JOptionPane.ERROR_MESSAGE);
					}
					else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
						okToPlace = true;
				}
				else if (!isCorner(xCell,yCell) && isExteriorWall(xCell,yCell))
					okToPlace = true;

				if (okToPlace)
				{
					exit = new Coord(xCell, yCell);
					exitPlaced = true;
					lClosedExit.setEnabled(false);
					maze[yCell][xCell] = Labirinto.SAIDA;
					((JLabel)e.getSource()).setIcon(currentIcon);
					resetToWall();
				}
				else if (!errorDisplayed)
					JOptionPane.showMessageDialog(null, "You can't place the exit there!", "", JOptionPane.ERROR_MESSAGE);
			}
			else if (currentBlockName == "dragon20" || currentBlockName == "dragonSleep20")
			{
				// ver se posicao e valida
				if (!isExteriorWall(xCell,yCell))
				{
					if ( ((xCell > 1) && (yCell < (configs.sizeMaze-2))) && ((yCell > 1) && (xCell < (configs.sizeMaze-2))) )
					{
						if (maze[yCell][xCell] == Labirinto.PAREDE || maze[yCell][xCell] == CHAO_CONSTRUIR)
						{
							Dragao d;
							okToPlace = true;

							if (configs.tactica == 2)
								d = new Dragao(xCell,yCell,true);
							else
								d = new Dragao (xCell,yCell,false);

							dragons.add(d);
							maze[yCell][xCell] = Labirinto.DRAGAO;
							((JLabel)e.getSource()).setIcon(currentIcon);

							if (dragons.size() == configs.numberDragons)
							{
								lDragon.setEnabled(false);
								resetToWall();
							}
						}
						else
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
				if (heroPlaced && hero.getPos().x == xCell && hero.getPos().y == yCell)
				{
					heroPlaced = false;
					lHero.setEnabled(true);
				}
				else if (exitPlaced && exit.x == xCell && exit.y == yCell)
				{
					exitPlaced = false;
					lClosedExit.setEnabled(true);
				}
				else if (swordPlaced && sword.getPos().x == xCell && sword.getPos().y == yCell)
				{
					swordPlaced = false;
					lSword.setEnabled(true);
				}
				else
				{
					for (int i = 0; i < dragons.size(); i++)
						if (dragons.get(i).getPos().x == xCell && dragons.get(i).getPos().y == yCell)
						{
							dragons.remove(i);
							lDragon.setEnabled(true);
						}
				}
					
				((JLabel)e.getSource()).setIcon(currentIcon);
				maze[yCell][xCell] = Labirinto.PAREDE;
			}
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			if (isExteriorWall(xCell, yCell))
			{
				if (heroPlaced && hero.getPos().x == xCell && hero.getPos().y == yCell)
				{
					heroPlaced = false;
					lHero.setEnabled(true);
					maze[yCell][xCell] = Labirinto.PAREDE;
				}
				else if (exitPlaced && exit.x == xCell && exit.y == yCell)
				{
					exitPlaced = false;
					lClosedExit.setEnabled(true);
					maze[yCell][xCell] = Labirinto.PAREDE;
				}

				((JLabel)e.getSource()).setIcon(wall20);
			}
			else
			{
				for (int i = 0; i < dragons.size(); i++)
					if (dragons.get(i).getPos().x == xCell && dragons.get(i).getPos().y == yCell)
					{
						dragons.remove(i);
						lDragon.setEnabled(true);
						maze[yCell][xCell] = CHAO_CONSTRUIR;
					}

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
	
	private boolean isExteriorWall(int xCell, int yCell) {
		if ((xCell == 0 || xCell == (configs.sizeMaze-1)) || (yCell == 0 || yCell == (configs.sizeMaze-1)))
			return true;
		return false;
	}

	private boolean isCorner(int x, int y) {
		if ( (x == 0 && y == 0)
				|| (x == (configs.sizeMaze-1) && y == 0)
					|| (x == 0 && y == (configs.sizeMaze-1))
						|| (x == (configs.sizeMaze-1) && y == (configs.sizeMaze-1)) )
			return true;
		return false;
	}
	
	private void resetToWall () {
		currentIconLabel.setIcon(wall20);
		currentIcon = currentIconLabel.getIcon();
		currentBlockName = "wall20";
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getActionCommand() == "useThis")
		{
			initializeVisited();
			if (findExit(hero.getPos().x, hero.getPos().y))
			{
				if (heroPlaced && swordPlaced && exitPlaced && (dragons.size() == configs.numberDragons))
					finishMaze();
				else
					JOptionPane.showMessageDialog(null, "There are elements missing from the maze. Complete it!", "", JOptionPane.ERROR_MESSAGE);
			}
			else
				JOptionPane.showMessageDialog(null, "This maze is impossible to complete! Try a different one.", "", JOptionPane.ERROR_MESSAGE);
		}
		else if (e.getActionCommand() == "clearMaze")
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
		else if (e.getActionCommand() == "instructionsSwitch")
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
		else
		{
			currentIconLabel.setIcon(new ImageIcon("images/" + e.getActionCommand() + ".png"));
			currentIcon = currentIconLabel.getIcon();
			currentBlockName = e.getActionCommand();
		}
	}
	
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

	private void finishMaze() {
		
		for (int i = 1; i < (configs.sizeMaze-1); i++)
			for (int j = 1; j < (configs.sizeMaze-1); j++)
				if (maze[i][j] == CHAO_CONSTRUIR)
					maze[i][j] = Labirinto.CHAO;
		
		mazeFinished = new Labirinto(configs, maze, hero, sword, dragons);
		setVisible(false);
		new GraphicGame(mazeFinished, configs);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		parent.setVisible(true);
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
	
	int getMazeValue(int x, int y) {
		return maze[y][x];
	}

	int goRight(int x, int y) {
		if ( x == (configs.sizeMaze-1) )
			return OUT_OF_BOUNDS;
		return (getMazeValue(x + 1, y));
	}

	int goLeft(int x, int y) {
		if ( x == 0)
			return OUT_OF_BOUNDS;
		return (getMazeValue(x - 1, y));
	}

	int goUp(int x, int y) {
		if (y == 0)
			return OUT_OF_BOUNDS;
		return (getMazeValue(x, y - 1));
	}

	int goDown(int x, int y) {
		if ( y == (configs.sizeMaze-1))
			return OUT_OF_BOUNDS;
		return (getMazeValue(x, y + 1));
	}

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
	
	private void initializeVisited() {
		visited = new int[configs.sizeMaze][configs.sizeMaze];

		for (int i = 0; i < configs.sizeMaze; i++)
			for (int j = 0; j < configs.sizeMaze; j++)
				visited[i][j] = NOT_VISITED;
	}
}
