package maze.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import maze.logic.ConfigurationMaze;

public class InitialMenu extends JFrame implements MouseListener {

	private static final long serialVersionUID = 2494285562264757790L;
	
	private JPanel mainMenu;
	private ConfigurationMaze configs;

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
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
