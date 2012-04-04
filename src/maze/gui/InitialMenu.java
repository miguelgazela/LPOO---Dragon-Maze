package maze.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import maze.logic.ConfigurationMaze;
import maze.logic.Labirinto;

/**
 * Classe que quando instanciada apresenta uma janela com menu inicial
 * @author migueloliveira
 */
public class InitialMenu extends JFrame implements MouseListener {

	private static final long serialVersionUID = 2494285562264757790L;
	
	private JPanel mainMenu;
	private ConfigurationMaze configs;
	private static JFileChooser fc;
	
	private static final int MIN_X_BUTTONS = 94;
	private static final int MAX_X_BUTTONS = 288;
	private static final int MIN_Y_NEWGAME = 64;
	private static final int MAX_Y_NEWGAME = 94;
	private static final int MIN_Y_LOADGAME = 133;
	private static final int MAX_Y_LOADGAME = 163;
	private static final int MIN_Y_CREATEGAME = 203;
	private static final int MAX_Y_CREATEGAME = 233;
	private static final int MIN_Y_CONFIGS = 273;
	private static final int MAX_Y_CONFIGS = 303;
	private static final int MIN_Y_EXIT = 343;
	private static final int MAX_Y_EXIT = 373;
	
	/**
	 * Construtor
	 * Inicializa os elementos e apresenta a janela
	 */
	public InitialMenu() 
	{
		super("Welcome to the MAZE!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
//		cria uma configuracao default
		configs = new ConfigurationMaze(15,1,1);

//		cria a JLabel com a imagem do menu principal com MouseListener e adiciona-a 'a JFrame
		Icon menu = new ImageIcon("images/menuPrincipal.png");
		JLabel menuPng = new JLabel(menu);
		menuPng.addMouseListener(this);
		mainMenu = new JPanel();
		getContentPane().add(mainMenu.add(menuPng));
		
//		apresenta a janela
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Construtor
	 * Utilizado para criar um novo menu inicial quando o jogo ja foi corrido
	 * @param config
	 */
	public InitialMenu(ConfigurationMaze config) {
		this();
		configs = config;
	}
	
	/**
	 * Fecha o menu inicial e cria um novo jogo com interface gr‡fica utilizando as configuracoes atuais
	 */
	private void newGame() {
		setVisible(false);
		new GraphicGame(configs);
	
	}
	
	/**
	 * Carrega um savegame do jogo
	 */
	private void loadGame() {
		ObjectInputStream is = null;

//		cria um novo JFileChooser, aplica-lhe um filtro para apenas permitir abrir ficheiros com a extensao correcta
		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new mazaFileFilter());
		
		int returnVal = fc.showOpenDialog(this);
		Labirinto maze = null;

		if (returnVal == JFileChooser.APPROVE_OPTION) // se o jogador abriu um ficheiro
		{
//			tenta carregar as informacoes do ficheiro
			try {
				is = new ObjectInputStream ( new FileInputStream(fc.getSelectedFile().getPath()));
				maze = (Labirinto) is.readObject();
				configs.sizeMaze = maze.getSize();
				configs.tactica = maze.getTactica();
				configs.numberDragons = maze.getNumDragoes();
//				fecha o menu inicial e cria um novo jogo com interface grafica com o Labirinto carregado e as suas configuracoes
				this.setVisible(false);
				new GraphicGame(maze, configs);
			}
			catch (IOException e) // se nao foi possivel carregar o ficheiro
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) // se o ficheiro desserializado nao e da classe correcta
			{
				e.printStackTrace();
			}
			finally 
			{ 
				if (is != null)
					try {
						is.close(); // fecha o ficheiro
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	/**
	 * Abre um novo criador de Labirintos pelo jogador
	 */
	private void createGame() {
		new CreateMaze(this, configs);
	}
	
	/**
	 * Abre uma janela de configuracao para alteracao das atuais
	 */
	private void changeConfigs() {
		new Configuration(this, configs);
	}
	
	/**
	 * Fecha o jogo
	 */
	private void exitGame() {
		System.exit(-1);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
//		determinar a posicao onde clicou e escolher opcao nessa coordenada
		if ( e.getX() >= MIN_X_BUTTONS && e.getX() <= MAX_X_BUTTONS )
			if ( e.getY() >= MIN_Y_NEWGAME && e.getY() <= MAX_Y_NEWGAME)
				newGame();
			else if ( e.getY() >= MIN_Y_LOADGAME && e.getY() <= MAX_Y_LOADGAME)
				loadGame();
			else if ( e.getY() >= MIN_Y_CREATEGAME && e.getY() <= MAX_Y_CREATEGAME)
				createGame();
			else if ( e.getY() >= MIN_Y_CONFIGS && e.getY() <= MAX_Y_CONFIGS)
				changeConfigs();
			else if ( e.getY() >= MIN_Y_EXIT && e.getY() <= MAX_Y_EXIT)
				exitGame();
	}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
}
