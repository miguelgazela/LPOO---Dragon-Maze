package maze.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.*;

import maze.logic.ConfigurationMaze;
import maze.logic.Labirinto;

public class InitialMenu extends JFrame implements MouseListener {

	private static final long serialVersionUID = 2494285562264757790L;
	
	private JPanel mainMenu;
	private ConfigurationMaze configs;
	private static JFileChooser fc;

	public InitialMenu() 
	{
		super("Welcome to the MAZE!");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		configs = new ConfigurationMaze(15,1,1);

		Icon menu = new ImageIcon("images/menuPrincipal.png");
		JLabel menuPng = new JLabel(menu);
		menuPng.addMouseListener(this);
		
		mainMenu = new JPanel();
		getContentPane().add(mainMenu.add(menuPng));
		
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public InitialMenu(ConfigurationMaze config) {
		this();
		configs = config;
	}
	
	private void newGame() {
		setVisible(false);
		new GraphicGame(configs);
	
	}
	
	private void loadGame() {
		ObjectInputStream is = null;

		fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		fc.addChoosableFileFilter(new mazaFileFilter());
		
		int returnVal = fc.showOpenDialog(this);
		Labirinto maze = null;

		if (returnVal == JFileChooser.APPROVE_OPTION) 
		{
			try {
				is = new ObjectInputStream ( new FileInputStream(fc.getSelectedFile().getPath()));
				maze = (Labirinto) is.readObject();
				configs.sizeMaze = maze.getSize();
				configs.tactica = maze.getTactica();
				configs.numberDragons = maze.getNumDragoes();
				this.setVisible(false);
				new GraphicGame(maze, configs);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			finally { if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				} }
		}
	}
	
	private void createGame() {
		new CreateMaze(this, configs);
	}
	
	private void changeConfigs() {
		new Configuration(this, configs);
	}
	
	private void exitGame() {
		System.exit(-1);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if ( e.getX() >= 94 && e.getX() <= 288 )
			if ( e.getY() >= 64 && e.getY() <= 94)
				newGame();
			else if ( e.getY() >= 133 && e.getY() <= 163)
				loadGame();
			else if ( e.getY() >= 203 && e.getY() <= 233)
				createGame();
			else if ( e.getY() >= 273 && e.getY() <= 303)
				changeConfigs();
			else if ( e.getY() >= 343 && e.getY() <= 373)
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
