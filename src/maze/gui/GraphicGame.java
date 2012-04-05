package maze.gui;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import maze.logic.*;
import maze.sound.*;


/**
 * Cria uma interface grafica para o jogo.
 * @author migueloliveira
 */
public class GraphicGame extends JFrame implements Serializable {

	private static final long serialVersionUID = 7387239851496757254L;

	private Labirinto maze;
	
	private JPanel buttonPanel, labirinto = null;
	
	private JButton exitButton, newGame, gameConfigurations, zoomIn, zoomOut, superPower, howToPlay, sound;
	
	private JMenuBar menuBar;
	private JMenuItem barSound, barButtons;
	
	private static ConfigurationMaze configs;
	private static JFileChooser fc;
	
	private boolean superPowerHasBeenUsed, buttonPanelIsVisible = true;
	
	public static boolean soundIsOn = true;
	
	private static int iconSize = 25;
	
	public static Icon questionMark;
	private Icon wall, ground, dragon, sleepDragon, hero, heroArmed, exit, closedExit, sword, swordProtected, soundOn, soundOff;
	private Icon wall20, ground20, dragon20, sleepDragon20, hero20, heroArmed20, exit20, closedExit20, sword20, swordProtected20;
	private Icon wall15, ground15, dragon15, sleepDragon15, hero15, heroArmed15, exit15, closedExit15, sword15, swordProtected15;
	
	/**
	 * Cria uma janela e apresenta uma janela, com um Labirinto construido com a configuracao recebida
	 * @param configs
	 */
	public GraphicGame (ConfigurationMaze configs) 
	{
		// inicializa janela
		super("Projeto Guiado - LPOO - by Miguel Oliveira (ei10076@fe.up.pt)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GraphicGame.configs = configs;
		
		// constroi novo Labirinto custom
		maze = new CustomBuilder(configs.sizeMaze, configs.numberDragons, configs.tactica)
		.construirLabirinto()
		.colocarDragao()
		.colocarEspada()
		.getLabirinto();

		// cria e apresenta a janela
		createShowGUI();
	}
	
	/**
	 * Cria um novo jogo com as configuracoes e o Labirinto recebidos
	 * @param customMaze Labirinto criado pelo jogador
	 * @param configs Configuracoes do labirinto recebido
	 */
	public GraphicGame (Labirinto customMaze, ConfigurationMaze configs) 
	{
		super("Projeto Guiado - LPOO - by Miguel Oliveira (ei10076@fe.up.pt)");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicGame.configs = configs;
		maze = customMaze;
		createShowGUI();
	}
	
	/**
	 * Cria e apresenta a janela de jogo
	 */
	private void createShowGUI() {
		// definir o layout da janela principal com horizontal gap de 0 e vertical gap de 5
		getContentPane().setLayout(new BorderLayout(0,0));

		// criar e adicionar elementos principais da janela
		createWidgets();
		addWidgets(getContentPane());

		// criar e configurar barra de ferramentas
		initMenuBar();

		// mostrar janela
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
		
		fc = new JFileChooser();
	}
	
	/**
	 * Inicializa a barra de ferramentas da janela
	 */
	private void initMenuBar() {

		// cria barra de ferramentas e seus menus
		menuBar = new JMenuBar();
		
        JMenu menu = new JMenu("Menu");
        JMenu tools = new JMenu("Tools");
        JMenu specials = new JMenu("Specials");
        JMenu help = new JMenu("Help");
        
        // construir os items do menu e os seus aceleradores
        JMenuItem barInitialMenu = new JMenuItem("Initial menu", KeyEvent.VK_I);
        barInitialMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
        JMenuItem barNewGame = new JMenuItem("New game",KeyEvent.VK_N);
        barNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));
       	JMenuItem barSaveGame = new JMenuItem("Save Game", KeyEvent.VK_S);
       	barSaveGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
       	JMenuItem barLoadGame = new JMenuItem("Load Game", KeyEvent.VK_L);
       	barLoadGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
       	JMenuItem barExitGame = new JMenuItem ("Exit", KeyEvent.VK_ESCAPE);
       	barExitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, ActionEvent.ALT_MASK));
       	JMenuItem barCreateMaze = new JMenuItem("Create maze", KeyEvent.VK_B);
       	barCreateMaze.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
       	JMenuItem barConfigurations = new JMenuItem("Configurations");
       	barConfigurations.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
       	JMenuItem barZoomIn = new JMenuItem("Zoom In");
       	barZoomIn.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, ActionEvent.ALT_MASK));
       	JMenuItem barZoomOut = new JMenuItem("Zoom Out");
       	barZoomOut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, ActionEvent.ALT_MASK));
       	barSound = new JMenuItem("Turn Sound OFF", KeyEvent.VK_F10);
       	barSound.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10, ActionEvent.ALT_MASK));
       	barButtons = new JMenuItem("Make Buttons Invisible", KeyEvent.VK_V);
       	barButtons.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.ALT_MASK));
       	JMenuItem barSuperPower = new JMenuItem("Super Power");
       	barSuperPower.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.ALT_MASK));
       	JMenuItem barHowToPlay = new JMenuItem("How To Play");
       	barHowToPlay.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
       	
       	// adicionar os listeners aos items respectivos
       	barInitialMenu.addActionListener(new initialMenuListener(this));
       	barNewGame.addActionListener(new newGameListener());
       	barSaveGame.addActionListener(new saveGameListener(this));
       	barLoadGame.addActionListener(new loadGameListener(this));
       	barExitGame.addActionListener(new ExitListener());
       	barCreateMaze.addActionListener(new createMazeListener(this));
       	barConfigurations.addActionListener(new configurationsListener(this));
       	barZoomIn.addActionListener(new zoomInListener(this));
       	barZoomOut.addActionListener(new zoomOutListener(this));
       	barSound.addActionListener(new soundListener());
       	barButtons.addActionListener(new buttonVisibilityListener(this));
       	barSuperPower.addActionListener(new superPowerListener());
       	barHowToPlay.addActionListener(new howToPlayListener(this));
       	
       	// adicionar todos os items ao menu apropriado
       	menu.add(barNewGame);
       	menu.add(barSaveGame);
       	menu.add(barLoadGame);
       	menu.addSeparator();
       	menu.add(barInitialMenu);
       	menu.addSeparator();
       	menu.add(barExitGame);
       	tools.add(barCreateMaze);
       	tools.add(barConfigurations);
       	tools.addSeparator();
       	tools.add(barZoomIn);
       	tools.add(barZoomOut);
       	tools.addSeparator();
       	tools.add(barSound);
       	tools.add(barButtons);
       	specials.add(barSuperPower);
       	help.add(barHowToPlay);
        menuBar.add(menu);
        menuBar.add(tools);
        menuBar.add(specials);
        menuBar.add(help);
        
        this.setJMenuBar(menuBar);
	}

	/**
	 * Cria o painel de botoes e os botoes que irao ser usados
	 */
	void createWidgets() {
		
		// criar paineis que constituem a janela principal do jogo
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 2));
		
		// criar os botoes disponiveis na janela principal
		exitButton = new JButton("Exit");
		exitButton.setToolTipText("Click this button to exit the game.");
		newGame = new JButton("New game");
		newGame.setToolTipText("Click this button to start a new game.");
		gameConfigurations = new JButton("Configurations");
		gameConfigurations.setToolTipText("Click this button to configure the next game options.");
		zoomIn = new JButton("Zoom in");
		zoomIn.setToolTipText("Click this button to make the maze bigger.");
		zoomOut = new JButton("Zoom out");
		zoomOut.setToolTipText("Click this button to make the maze smaller.");
		superPower = new JButton("Super Power");
		superPower.setToolTipText("Click this button to release your full power.");
		howToPlay = new JButton("How to play");
		howToPlay.setToolTipText("Click this button to see the game instructions.");
		
		// adicionar os listeners aos botoes
		exitButton.addActionListener(new ExitListener());
		newGame.addActionListener(new newGameListener());
		gameConfigurations.addActionListener(new configurationsListener(this));
		zoomIn.addActionListener(new zoomInListener(this));
		zoomOut.addActionListener(new zoomOutListener(this));
		superPower.addActionListener(new superPowerListener());
		howToPlay.addActionListener(new howToPlayListener(this));
		
		// inicializar os icones necessarios
		initIcons();
		
		// criar icone do som, icone usado depende se quer som activo ou nao
		if (soundIsOn)
			sound = new JButton (soundOn);
		else
			sound = new JButton (soundOff);
		sound.addActionListener(new soundListener());
		
		carregarLabirinto();
	}
	
	/**
	 * Adiciona os botoes criados 'a janela principal
	 * @param contentPane Contentor da janela principal
	 */
	private void addWidgets(Container contentPane) {
		
		// adicionar os botoes ao painel
		buttonPanel.add(howToPlay);
		buttonPanel.add(newGame);
		buttonPanel.add(gameConfigurations);
		buttonPanel.add(zoomIn);
		buttonPanel.add(zoomOut);
		buttonPanel.add(superPower);
		buttonPanel.add(exitButton);
		buttonPanel.add(sound);
		
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
	}

	/**
	 * Inicializa os icones utilizados na janela principal
	 */
	private void initIcons() {
		// inicializar os icones 25x25
		questionMark = new ImageIcon("images/questionMark.png");
		wall = new ImageIcon("images/wall.png");
		ground = new ImageIcon("images/ground.png");
		dragon = new ImageIcon("images/dragon.png");
		sleepDragon = new ImageIcon("images/dragonSleep.png");
		hero = new ImageIcon("images/hero.png");
		heroArmed = new ImageIcon("images/heroArmed.png");
		exit = new ImageIcon("images/exit.png");
		closedExit = new ImageIcon("images/closeExit.png");
		sword = new ImageIcon("images/sword.png");
		swordProtected = new ImageIcon("images/swordProtected.png");

		// inicializar os icones 20x20
		wall20 = new ImageIcon("images/wall20.png");
		ground20 = new ImageIcon("images/ground20.png");
		dragon20 = new ImageIcon("images/dragon20.png");
		sleepDragon20 = new ImageIcon("images/dragonSleep20.png");
		hero20 = new ImageIcon("images/hero20.png");
		heroArmed20 = new ImageIcon("images/heroArmed20.png");
		exit20 = new ImageIcon("images/exit20.png");
		closedExit20 = new ImageIcon("images/closeExit20.png");
		sword20 = new ImageIcon("images/sword20.png");
		swordProtected20 = new ImageIcon("images/swordProtected20.png");

		// inicializar os icones 15x15
		wall15 = new ImageIcon("images/wall15.png");
		ground15 = new ImageIcon("images/ground15.png");
		dragon15 = new ImageIcon("images/dragon15.png");
		sleepDragon15 = new ImageIcon("images/dragonSleep15.png");
		hero15 = new ImageIcon("images/hero15.png");
		heroArmed15 = new ImageIcon("images/heroArmed15.png");
		exit15 = new ImageIcon("images/exit15.png");
		closedExit15 = new ImageIcon("images/closeExit15.png");
		sword15 = new ImageIcon("images/sword15.png");
		soundOn = new ImageIcon("images/soundOn.png");
		soundOff = new ImageIcon("images/soundOff.png");
		swordProtected15 = new ImageIcon("images/swordProtected15.png");
	}

	/**
	 * Apresenta graficamente na janela principal o array que constitui o labirinto
	 */
	void carregarLabirinto() {

		if (labirinto != null) 							// se ja tem um labirinto carregado
			getContentPane().remove(labirinto);

		labirinto = new JPanel(new GridLayout(0, 1));	// painel com uma coluna

		// carregar array do labirinto
		int[][] estruturaLabirinto = maze.getLabirinto();

		for (int i = 0; i < estruturaLabirinto.length; i++) // percorrer os elementos do array
		{
			JPanel labirintoLinha = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

			// ver o tamanho atual para representacao dos icones do labirinto
			if (iconSize == 15)
			{
				for (int j = 0; j < estruturaLabirinto[i].length; j++)
					if (estruturaLabirinto[i][j] == Labirinto.PAREDE)
						labirintoLinha.add(new JLabel(wall15));
					else if (estruturaLabirinto[i][j] == Labirinto.CHAO)
						labirintoLinha.add(new JLabel(ground15));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO && maze.getDragaoAt(i, j).estaAdormecido())
						labirintoLinha.add(new JLabel(sleepDragon15));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO)
						labirintoLinha.add(new JLabel(dragon15));
					else if (estruturaLabirinto[i][j] == Labirinto.HEROI)
						labirintoLinha.add(new JLabel(hero15));
					else if (estruturaLabirinto[i][j] == Labirinto.ARMADO)
						labirintoLinha.add(new JLabel(heroArmed15));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA && maze.getHeroi().estaArmado())
						labirintoLinha.add(new JLabel(exit15));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA)
						labirintoLinha.add(new JLabel(closedExit15));
					else if (estruturaLabirinto[i][j] == Labirinto.ESPADA)
						labirintoLinha.add(new JLabel(sword15));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAOESPADA)
						labirintoLinha.add(new JLabel(swordProtected15));
			}
			else if (iconSize == 20)
			{
				for (int j = 0; j < estruturaLabirinto[i].length; j++)
					if (estruturaLabirinto[i][j] == Labirinto.PAREDE)
						labirintoLinha.add(new JLabel(wall20));
					else if (estruturaLabirinto[i][j] == Labirinto.CHAO)
						labirintoLinha.add(new JLabel(ground20));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO && maze.getDragaoAt(i, j).estaAdormecido())
						labirintoLinha.add(new JLabel(sleepDragon20));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO)
						labirintoLinha.add(new JLabel(dragon20));
					else if (estruturaLabirinto[i][j] == Labirinto.HEROI)
						labirintoLinha.add(new JLabel(hero20));
					else if (estruturaLabirinto[i][j] == Labirinto.ARMADO)
						labirintoLinha.add(new JLabel(heroArmed20));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA && maze.getHeroi().estaArmado())
						labirintoLinha.add(new JLabel(exit20));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA)
						labirintoLinha.add(new JLabel(closedExit20));
					else if (estruturaLabirinto[i][j] == Labirinto.ESPADA)
						labirintoLinha.add(new JLabel(sword20));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAOESPADA)
						labirintoLinha.add(new JLabel(swordProtected20));
			}
			else if (iconSize == 25)
			{
				for (int j = 0; j < estruturaLabirinto[i].length; j++)
					if (estruturaLabirinto[i][j] == Labirinto.PAREDE)
						labirintoLinha.add(new JLabel(wall));
					else if (estruturaLabirinto[i][j] == Labirinto.CHAO)
						labirintoLinha.add(new JLabel(ground));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO && maze.getDragaoAt(i, j).estaAdormecido())
						labirintoLinha.add(new JLabel(sleepDragon));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAO)
						labirintoLinha.add(new JLabel(dragon));
					else if (estruturaLabirinto[i][j] == Labirinto.HEROI)
						labirintoLinha.add(new JLabel(hero));
					else if (estruturaLabirinto[i][j] == Labirinto.ARMADO)
						labirintoLinha.add(new JLabel(heroArmed));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA && maze.getHeroi().estaArmado())
						labirintoLinha.add(new JLabel(exit));
					else if (estruturaLabirinto[i][j] == Labirinto.SAIDA)
						labirintoLinha.add(new JLabel(closedExit));
					else if (estruturaLabirinto[i][j] == Labirinto.ESPADA)
						labirintoLinha.add(new JLabel(sword));
					else if (estruturaLabirinto[i][j] == Labirinto.DRAGAOESPADA)
						labirintoLinha.add(new JLabel(swordProtected));
			}
			labirinto.add(labirintoLinha);
		}
		labirinto.setFocusable(true);
		labirinto.addKeyListener(new movimentListener());

		getContentPane().add(labirinto, BorderLayout.NORTH);
	}

	/**
	 * Invoca o updateUI() para todos os subcomponentes da janela principal
	 */
	private void updateUI() {
		SwingUtilities.updateComponentTreeUI(this);
	}

	/**
	 * Inicia um jogo novo utilizando as configuracoes atuais
	 */
    private void novoJogo() {
    	// cria novo Labirinto
    	maze = new CustomBuilder(configs.sizeMaze, configs.numberDragons, configs.tactica)
		.construirLabirinto()
		.colocarDragao()
		.colocarEspada()
		.getLabirinto();
    	
		superPowerHasBeenUsed = false;
		
		this.setResizable(true); // permite expandir se o tamanha do labirinto aumentou 
		carregarLabirinto(); // atualiza o painel do labirinto
		
		pack();
		this.setResizable(false); 
		labirinto.requestFocus();
	}
    
    /**
     * Termina o jogo quando o Heroi ganha ou perde.
     */
    private void acabouJogo() {

    	int option;
    	
    	/* criar sons necessarios  */
    	soundWinning win = new soundWinning();
    	heroDies heroDie = new heroDies();
    	
    	carregarLabirinto();
		updateUI();

    	if (maze.getHeroi().estaMorto()) // se o heroi morreu
    	{
    		if (soundIsOn) // se os sons estao ON
    			heroDie.play();
    		option = JOptionPane.showConfirmDialog(this,
    				"Ups, you're killed by a dragon, do you want to play again?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, questionMark);
    	}
    	else // heroi chegou ao fim
    	{
    		if (soundIsOn)
    			win.play();
    		option = JOptionPane.showConfirmDialog(this,
    				"Congrats, you've made it, do you want to play again?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, questionMark);
    	}
    	
    	if (option == JOptionPane.NO_OPTION) // se nao quer voltar a jogar
    	{
    		setVisible(false);
    		win.stop();
    		heroDie.stop();
    		new InitialMenu(configs);
		}
		else	// se quer voltar a jogar
		{
			win.stop();
    		heroDie.stop();
			novoJogo();
		}
	}
    
    /**
     * Implementa ActionListener para voltar ao menu inicial
     * @author migueloliveira
     */
    private class initialMenuListener implements ActionListener
    {
    	private JFrame f;
    	
    	public initialMenuListener (JFrame f) {
    		this.f = f;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// fecha a janela atual e inicializa novo menu inicial
			f.setVisible(false);
			new InitialMenu();
		}
    }
    
    /**
     * Implementa ActionListener para o botao das instrucoes de jogo
     * @author migueloliveira
     */
	private class howToPlayListener implements ActionListener
	{
		private JFrame f;
		
		public howToPlayListener (JFrame f) {
			this.f = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new HowToPlay();	// abre nova janela com instrucoes
			labirinto.requestFocus(); 
		}
		
		private class HowToPlay extends JDialog 
		{
			private static final long serialVersionUID = -6595176294244544775L;
			private JPanel instructions;

			public HowToPlay() 
			{
				super(f ,"How to play - Game instructions", true);
				setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

				Icon ins = new ImageIcon("images/gameInstructions.png");
				instructions = new JPanel();

				JLabel l = new JLabel(ins);
				getContentPane().add(instructions.add(l));

				pack();
				setResizable(false);
				setLocationRelativeTo(null);
				setVisible(true);
			}
		}
	}

	/**
	 * Implementa ActionListener para botao de novo jogo
	 * @author migueloliveira
	 */
	private class newGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			/* pergunta ao utilizador se tem a certeza que quer comecar novo jogo */
			Object[] options = {"Yes, please", "No way!"};
			int option = JOptionPane.showOptionDialog(null,
					"Are you sure you want to start a new game?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION)
				novoJogo();
			labirinto.requestFocus();
		}
	}
	
	/**
	 * Implementa ActionListener para botao de gravar jogo
	 * @author migueloliveira
	 */
	private class saveGameListener implements ActionListener
	{
		private JFrame parent;
		
		public saveGameListener (JFrame parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			ObjectOutputStream os = null;

			// apresenta janela para gravar ficheiro
			int returnVal = fc.showSaveDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) 
			{
				try {
					// tenta gravar o ficheiro com a extensao apropriada
					os = new ObjectOutputStream ( new FileOutputStream(fc.getSelectedFile().getPath() + ".mzesvgme"));
					os.writeObject(maze);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
				finally { 
					if (os != null)
						try {
							os.close(); // fecha o ficheiro
						} catch (IOException e) 
						{
							e.printStackTrace();
						} 
				}
			}
		}
	}

	private class loadGameListener implements ActionListener
	{
		private JFrame parent;

		public loadGameListener (JFrame parent) {
			this.parent = parent;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			ObjectInputStream is = null;

			// aplica um filtro para apenas permitir abrir ficheiros com a extensao correcta
			fc.setAcceptAllFileFilterUsed(false);
			fc.addChoosableFileFilter(new MazeFileFilter());
			
			int returnVal = fc.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) // se o jogador abriu um ficheiro
			{
				try {
					// tenta carregar as informacoes do ficheiro
					is = new ObjectInputStream ( new FileInputStream(fc.getSelectedFile().getPath()));
					maze = (Labirinto) is.readObject();
				}
				catch (IOException e) // se nao foi possivel carregar o ficheiro
				{
					e.printStackTrace();
				} catch (ClassNotFoundException e) // se o ficheiro desserializado nao e da classe correcta
				{
					e.printStackTrace();
				}
				finally { if (is != null)
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
				// inicializa as variaveis a partir das informacoes carregadas
				configs.sizeMaze = maze.getSize();
				configs.tactica = maze.getTactica();
				configs.numberDragons = maze.getNumDragoes();
				
				// carrega e volta a apresentar a janela de jogo
				carregarLabirinto();
				setResizable(true);
				updateUI();
				pack();
				setResizable(false);
				labirinto.requestFocus();
			}
		}
	}
	
	/**
	 * Devolve as configuracoes atuais
	 * @return ConfigurationMaze
	 */
	public ConfigurationMaze getConfigs() {
		return configs;
	}

	/**
	 * Devolve o Labirinto atual
	 * @return Labirinto
	 */
	public Labirinto getMaze() {
		return maze;
	}

	/**
	 * Implementa ActionListener para botao de configuracoes
	 * @author migueloliveira
	 */
	private class configurationsListener implements ActionListener
	{
		private JFrame f;
		
		public configurationsListener(JFrame f) {
			this.f = f;
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			/* apresenta nova janela de configuracoes com as configuracoes atuais */
			new Configuration(f, configs);
			labirinto.requestFocus();
		}
	}
	
	/**
	 * Implementa ActionListener para botao de criar novo Labirinto
	 * @author migueloliveira
	 */
	private class createMazeListener implements ActionListener
	{
		private JFrame f;
		
		public createMazeListener (JFrame f) {
			this.f = f;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			new CreateMaze(f, configs);
		}
		
	}
	
	/**
	 * Implementa ActionListener para botao de zoom in
	 * @author migueloliveira
	 */
	private class zoomInListener implements ActionListener
	{

		private JFrame f;

		public zoomInListener (JFrame f) {
			this.f = f;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			if (iconSize == 25) // se ja esta no tamanho maximo
				JOptionPane.showMessageDialog(null, "The game is already in the maximum size!", "Size warning", JOptionPane.WARNING_MESSAGE);
			else if (iconSize == 15 || iconSize == 20)
			{
				/* aumenta o tamanho dos icones atuais 5 pixeis e actualiza janela */
				iconSize += 5;
				f.setResizable(true);
				carregarLabirinto();
				repaint();
				pack();
				setLocationRelativeTo(null);
				f.setResizable(false);
				labirinto.requestFocus(); 
			}
		}
	}
	
	/**
	 * Implementa ActionListener para botao de zoom out
	 * @author migueloliveira
	 */
	private class zoomOutListener implements ActionListener
	{
		private JFrame f;

		public zoomOutListener (JFrame f) {
			this.f = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (iconSize == 15) // se ja esta no tamanho minimo 
				JOptionPane.showMessageDialog(null, "The game is already in the minimum size!", "Size warning", JOptionPane.WARNING_MESSAGE);
			else if (iconSize == 20 || iconSize == 25)
			{
				/* diminui o tamanho dos icones atuais 5 pixeis e actualiza janela */
				iconSize -= 5;
				f.setResizable(true);
				carregarLabirinto();
				repaint();
				pack();
				setLocationRelativeTo(null);
				f.setResizable(false);
				labirinto.requestFocus();
			}
		}
	}
	
	/**
	 * Implementa ActionListener para botao de super poder 
	 * @author migueloliveira
	 */
	private class superPowerListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* verifica se o poder ja foi usado, ou se o heroi ja esta armado */
			if (superPowerHasBeenUsed)
				JOptionPane.showMessageDialog(null, "You've already used your super power!", "", JOptionPane.ERROR_MESSAGE);
			else if (!superPowerHasBeenUsed && maze.getHeroi().estaArmado())
				JOptionPane.showMessageDialog(null, "You're already armed, so this in unnecessary, try to solve the maze!", "", JOptionPane.ERROR_MESSAGE);
			else
			{
				/* escolhe uma direccao para eliminar a parede */
				Object[] possibilities = {"up wall", "down wall", "right wall", "left wall"};
				
				String s = (String)JOptionPane.showInputDialog(null, "Choose one wall to bring down, allowing you to pass through",  
																"Super power", JOptionPane.PLAIN_MESSAGE, hero, possibilities, "up wall");

				if ((s != null) && (s.length() > 0)) // se escolheu uma das opcoes 
				{
					int[][] estruturaLabirinto = maze.getLabirinto();
					
					boolean notWall = false;
					boolean wallDestroyed = false;
					
					int yHeroi = maze.getHeroi().getPos().y;
					int xHeroi = maze.getHeroi().getPos().x;
					
					/* verifica se essa parede pode ser eliminada 
					 * (as externas sao irremoviveis) ou se nao e uma parede, e
					 * remove ou nao consoante o caso */
					if (s == "up wall")
						if ( estruturaLabirinto[yHeroi-1][xHeroi] == Labirinto.PAREDE)
							if (yHeroi == 1 || xHeroi == 0 || xHeroi == (maze.getSize()-1))
								JOptionPane.showMessageDialog(null, "You cant destroy that wall!", "", JOptionPane.ERROR_MESSAGE);
							else
							{
								maze.setPos(Labirinto.CHAO, yHeroi-1, xHeroi);
								wallDestroyed = true;
							}
						else
							notWall = true;
					else if (s == "down wall")
						if ( estruturaLabirinto[yHeroi+1][xHeroi] == Labirinto.PAREDE)
							if (yHeroi == (maze.getSize()-2) || xHeroi == 0 || xHeroi == (maze.getSize()-1))
								JOptionPane.showMessageDialog(null, "You cant destroy that wall!", "", JOptionPane.ERROR_MESSAGE);
							else
							{
								maze.setPos(Labirinto.CHAO, yHeroi+1, xHeroi);
								wallDestroyed = true;
							}
						else
							notWall = true;
					else if(s == "right wall")
					{
						if ( estruturaLabirinto[yHeroi][xHeroi+1] == Labirinto.PAREDE)
							if (xHeroi == (maze.getSize()-2) || yHeroi == 0 || yHeroi == (maze.getSize()-1))
								JOptionPane.showMessageDialog(null, "You cant destroy that wall!", "", JOptionPane.ERROR_MESSAGE);
							else
							{
								maze.setPos(Labirinto.CHAO, yHeroi, xHeroi+1);
								wallDestroyed = true;
							}
						else
							notWall = true;
					}
					else if(s == "left wall")
					{
						if ( estruturaLabirinto[yHeroi][xHeroi-1] == Labirinto.PAREDE)
							if (xHeroi == 1 || yHeroi == 0 || yHeroi == (maze.getSize()-1))
								JOptionPane.showMessageDialog(null, "You can't destroy that wall!", "", JOptionPane.ERROR_MESSAGE);
							else
							{
								maze.setPos(Labirinto.CHAO, yHeroi, xHeroi-1);
								wallDestroyed = true;
							}
						else
							notWall = true;
					}
					
					if (wallDestroyed) // se parede foi destruida
					{
						carregarLabirinto();
						updateUI();
						if (soundIsOn)
							new brickSmash().play();
						superPowerHasBeenUsed = true;
					}
					else if (notWall) // nao era parede
						JOptionPane.showMessageDialog(null, "That is not a wall, you can't destroy it.", "", JOptionPane.INFORMATION_MESSAGE);
				}
				else // o heroi nao quis usar o super poder
				{
					JOptionPane.showMessageDialog(null, "You decided not to use your super power, good for you.", "", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			labirinto.requestFocus(); 
		}
	}
	
	/**
	 * Implementa ActionListener para botao de sair do jogo
	 * @author migueloliveira
	 */
	private class ExitListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* verifica se o jogador quer mesmo sair */
			Object[] options = {"Yes, please", "No way!"};
			int option = JOptionPane.showOptionDialog(null,
					"Are you sure you want to leave the game?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION) // fecha o programa
			{
				setVisible(false);
				System.exit(-1);
			}
			labirinto.requestFocus();
		}
	}
	
	/**
	 * Implementa ActionListener para botao de som
	 * @author migueloliveira
	 */
	private class soundListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			/* desliga ou liga o som consoante o estado atual, atualiza icone e barra de ferramentas */
			if (soundIsOn)
			{
				barSound.setText("Turn sound ON");
				soundIsOn = false;
				sound.setIcon(soundOff);
				sound.repaint();
			}
			else
			{
				barSound.setText("Turn sound OFF");
				soundIsOn = true;
				sound.setIcon(soundOn);
				sound.repaint();
			}
			labirinto.requestFocus();
		}
	}
	
	/**
	 * Implementa ActionListener para botao de visibilidade dos botoes principais
	 * @author migueloliveira
	 */
	private class buttonVisibilityListener implements ActionListener
	{

		private JFrame f;
		
		public buttonVisibilityListener (JFrame f) {
			this.f = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
		
			f.setResizable(true);
			
			/* retira ou volta a colocar o painel de botoes da janela principal */
			if (buttonPanelIsVisible)
			{
				barButtons.setText("Make buttons visible");
				buttonPanel.setVisible(false);
				buttonPanelIsVisible = false;
			}
			else
			{
				barButtons.setText("Make buttons invisible");
				buttonPanel.setVisible(true);
				buttonPanelIsVisible = true;
			}
			pack();
			setLocationRelativeTo(null);
			f.setResizable(false);
		}
		
	}
	
	/**
	 * Implementa KeyListener para detectar os movimentos do Heroi
	 * @author migueloliveira
	 */
	private class movimentListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e) {
			String str = null;
			
			if (!e.isAltDown()) // se nao estamos a usar um atalho
			{
				// determina a direcao a tomar
				if (e.getKeyCode() == configs.keys[ConfigurationMaze.UP])
					str = new String("w");
				else if (e.getKeyCode() == configs.keys[ConfigurationMaze.DOWN])
					str = new String("s");
				else if (e.getKeyCode() == configs.keys[ConfigurationMaze.RIGHT])
					str = new String("d");
				else if (e.getKeyCode() == configs.keys[ConfigurationMaze.LEFT])
					str = new String("a");

				if(str != null)
				{	
					maze.moverHeroi(str);

					if (!maze.dragoesTodosMortos()) // enquanto houver dragoes vivos
					{
						maze.avaliaTactica(); // vê se eles podem adormecer
						if ( !maze.dragoesAdormecidos() )
							maze.moverDragao("");
						maze.pertoDragao(); // verifica se heroi morreu ou matou algum dragao
					}

					if (!maze.getChegouFim() && !maze.getHeroi().estaMorto())
					{
						carregarLabirinto();
						updateUI();
						labirinto.requestFocus(); 
					}
					else
						acabouJogo();
				}
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { }
		@Override
		public void keyTyped(KeyEvent e) { }
	}
}
