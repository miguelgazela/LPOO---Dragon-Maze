package maze.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import maze.logic.ConfigurationMaze;

/**
 * Classe que quando instanciada apresenta uma janela para configuracao do Labirinto.
 * Permite a configuracao do tamanho do labirinto, do numero de dragoes que 
 * este possui, da tactica utilizada no jogo e as teclas utilizadas para o 
 * movimento do Heroi.
 * 
 * @author migueloliveira
 */
public class Configuration extends JDialog implements ChangeListener {
	
	private static final long serialVersionUID = 917983375861534320L;
	
	private static final int MAZE_SIZE_MIN = 10;
	private static final int MAZE_SIZE_MAX = 70;
	private static final int NUMBER_DRAGONS_MIN = 0;
	private static final int NUMBER_DRAGONS_MAX = 15;
	
	private JSlider mazeSizeSlider, numberDragonsSlider;
	private JComboBox wantedStrategy;
	private JPanel buttonPanel, changeKeys;
	private JButton save, discard, up, down, right, left;
	
	private ConfigurationMaze temp, original;
	
	/**
	 * Inicializa e apresenta uma janela, com uma JFrame como dona
	 * @param frame A JFrame "parent" da instancia criada
	 * @param configs A configuracao atual do Labirinto
	 */
	public Configuration (JFrame parent, ConfigurationMaze configs) {
		
		super(parent, "Configurations", true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
//		inicializar variaveis necessarias 
		temp = new ConfigurationMaze(15,1,1);
		original = configs;
		temp.numberDragons = original.numberDragons;
		temp.sizeMaze = original.sizeMaze;
		temp.tactica = original.tactica;
		for (int i = 0; i < 4; i++)
			temp.keys[i] = original.keys[i];
	
//		/* construir e definir componentes utilizados
		getContentPane().setLayout(new GridLayout(0,1));
		setMazeSizeSlider();
		setNumberDragonsSlider();
		setComboBox();
		setChangeKeys();
		setButtons();
		
//		apresentar janela centrada no ecra
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Constroi e adiciona 'a janela o painel com os JButton para alterar teclas direccionais.
	 */
	private void setChangeKeys() {
		
//		cria painel para os botoes
		changeKeys = new JPanel (new FlowLayout(FlowLayout.CENTER, 5, 10));
		TitledBorder changeKeysTitle = BorderFactory.createTitledBorder("change directional keys");
		changeKeys.setBorder(changeKeysTitle);
		
//		cria os botoes, adiciona-lhes KeyListeners e coloca-os no painel
		up = new JButton ("UP");
		down = new JButton ("DOWN");
		right = new JButton ("RIGHT");
		left = new JButton ("LEFT");
		up.addActionListener(new changeKeyListener(this));
		down.addActionListener(new changeKeyListener(this));
		right.addActionListener(new changeKeyListener(this));
		left.addActionListener(new changeKeyListener(this));
		changeKeys.add(up);
		changeKeys.add(down);
		changeKeys.add(right);
		changeKeys.add(left);
		
//		adiciona painel ao JDialog
		getContentPane().add(changeKeys);
	}
	
	/**
	 * Implementacao de um ActionListener para utilizar nos botoes de mudanca de teclas.
	 * @author migueloliveira
	 */
	private class changeKeyListener implements ActionListener {

		JDialog parent;
		JDialog d;
		
		/**
		 * Construtor
		 * @param parent Jdialog parent da nova JDialog que vai ser inicializada
		 */
		public changeKeyListener (JDialog parent) {
			this.parent = parent;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			
//			cria novo JDialog com titulo correspondente 'a tecla a alterar
			d = new JDialog (parent, "New " + ((JButton)event.getSource()).getText() + " directional key", true);
			d.setLayout(new GridLayout (0,1));
			
//			verifica qual a tecla a alterar
			int option = -1;
			
			if ( ((JButton)event.getSource()).getText() == "UP")
				option = ConfigurationMaze.UP;
			else if ( ((JButton)event.getSource()).getText() == "DOWN")
				option = ConfigurationMaze.DOWN;
			else if ( ((JButton)event.getSource()).getText() == "RIGHT")
				option = ConfigurationMaze.RIGHT;
			else if ( ((JButton)event.getSource()).getText() == "LEFT")
				option = ConfigurationMaze.LEFT;
			
//			carrega imagem com instrucoes e adiciona-a 'a janela
			Icon keyInstructions = new ImageIcon("images/newKey.png");
			JLabel t1 = new JLabel (keyInstructions);
			d.add(t1);
			d.addKeyListener(new keyListener(option, d));
			
//			mostra a janela centrada no ecra
			d.pack();
			d.setLocationRelativeTo(null);
			d.setResizable(false);
			d.setVisible(true);
		}
		
		/**
		 * Implementacao de KeyListener para obter a tecla introduzida pelo jogador
		 * @author migueloliveira
		 */
		private class keyListener implements KeyListener
		{
			int option;
			JDialog d;
			
			/**
			 * Construtor
			 * @param option Inteiro que corresponde 'a tecla que se quer alterar
			 * @param d Janela onde o KeyListener esta' a ser utilizado
			 */
			public keyListener (int option, JDialog d) {
				this.option = option;
				this.d = d;
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
//				verifica se a tecla e« uma das autorizadas
				if ( (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) 
						|| (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z)
							|| (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN) )
				{
//					verifica se essa tecla ja' esta a ser utilizada
					if (temp.keyAlreadyUsed(option, e))
						JOptionPane.showMessageDialog(null, "That key is already being used!", "", JOptionPane.ERROR_MESSAGE);
					else
					{
//						altera a tecla na configuracao temporaria e fecha a JDialog
						temp.keys[option] = e.getKeyCode();
						d.setVisible(false);
					}
				}
				else
					JOptionPane.showMessageDialog(null, "That key can't be used!", "", JOptionPane.ERROR_MESSAGE);
			}

			@Override
			public void keyReleased(KeyEvent e) { }
			@Override
			public void keyTyped(KeyEvent e) { }
		}
	}

	/**
	 * Constroi e adiciona 'a janela o slider para configurar tamanho do Labirinto
	 */
	private void setMazeSizeSlider() {
		
//		cria o slider com o tamanho maximo do Labirinto e com ChangeListener
		mazeSizeSlider  = new JSlider(JSlider.HORIZONTAL, MAZE_SIZE_MIN, MAZE_SIZE_MAX, original.sizeMaze);
		TitledBorder mazeSizeTitle = BorderFactory.createTitledBorder("maze size: " + original.sizeMaze);
		mazeSizeSlider.setBorder(mazeSizeTitle);
		mazeSizeSlider.addChangeListener(this);

//		configura apresentacao do slider
		mazeSizeSlider.setMajorTickSpacing(5);
		mazeSizeSlider.setMinorTickSpacing(1);
		mazeSizeSlider.setPaintTicks(true);
		mazeSizeSlider.setPaintLabels(true);
		
		getContentPane().add(mazeSizeSlider);
	}

	/**
	 * Constroi e adiciona 'a janela o slider para configurar numero de dragoes no Labirinto
	 */
	private void setNumberDragonsSlider() {
		
//		cria o slider com o numero maximo de dragoes e com ChangeListener
		numberDragonsSlider  = new JSlider(JSlider.HORIZONTAL, NUMBER_DRAGONS_MIN, NUMBER_DRAGONS_MAX, original.numberDragons);
		numberDragonsSlider.setBorder(BorderFactory.createTitledBorder("number of dragons: " + original.numberDragons));
		numberDragonsSlider.addChangeListener(this);

//		configura apresentacao do slider
		numberDragonsSlider.setMajorTickSpacing(3);
		numberDragonsSlider.setMinorTickSpacing(1);
		numberDragonsSlider.setPaintTicks(true);
		numberDragonsSlider.setPaintLabels(true);
		
		getContentPane().add(numberDragonsSlider);
	}
	
	/**
	 * Constroi e adiciona 'a janela a ComboBox com as tacticas de jogo disponiveis 
	 */
	private void setComboBox() {
		
//		array com estrategias disponiveis
		String[] strategies = { "The dragon(s) is/are always awake", "The dragon(s) is/are always sleeping", "The dragon(s) is/are awake but can also fall asleep"};

//		cria a combobox, mostra a tactica atual e adiciona um ChangeListener
		wantedStrategy = new JComboBox(strategies);
		wantedStrategy.setSelectedIndex(original.tactica-1);
		wantedStrategy.setBorder(BorderFactory.createTitledBorder("wanted strategy"));
		wantedStrategy.addActionListener(new changedStrategyListener());
		
		getContentPane().add(wantedStrategy);
	}

	/**
	 * Constroi e adiciona 'a janela o painel de botoes
	 */
	private void setButtons() {
		
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 25));
		
		/* cria os botoes e adiciona-os ao painel */
		save = new JButton("Save changes");
		discard = new JButton("Discard changes");
		save.addActionListener(new saveListener());
		discard.addActionListener(new discardListener());
		buttonPanel.add(save);
		buttonPanel.add(discard);
		
		getContentPane().add(buttonPanel);
	}

	@Override
    public void stateChanged(ChangeEvent e) 
	{
//		se nenhum dos sliders esta a ser alterado
		if( !((JSlider)e.getSource()).getValueIsAdjusting() || !((JSlider)e.getSource()).getValueIsAdjusting() )
		{
//			guarda os valores para a variavel temporaria 
			temp.sizeMaze = mazeSizeSlider.getValue();
			temp.numberDragons = numberDragonsSlider.getValue();

//			altera os titulos dos sliders para o valor atual
			mazeSizeSlider.setBorder(BorderFactory.createTitledBorder("maze size: " + temp.sizeMaze));
			numberDragonsSlider.setBorder(BorderFactory.createTitledBorder("number of dragons: " + temp.numberDragons));

//			verifica se deve permitir alterar as tactivas
			if (temp.numberDragons == 0)
				wantedStrategy.setEnabled(false);
			else
				wantedStrategy.setEnabled(true);
		}
    }
	
	/**
	 * Implementacao de ActionListener para alterar a estrategia do jogo
	 * @author migueloliveira
	 */
	private class changedStrategyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
//			guarda a tactica nova para a variavel temporario
			String strategy = (String) (((JComboBox)e.getSource() ).getSelectedItem());
			
			if (strategy == "The dragon(s) is/are always awake")
				temp.tactica = 1;
			else if (strategy == "The dragon(s) is/are always sleeping")
				temp.tactica = 2;
			else if (strategy == "The dragon(s) is/are awake but can also fall asleep")
				temp.tactica = 3;
		}
	}
	
	/**
	 * Implementacao de ActionListener para utilizar no botao de gravar
	 * @author migueloliveira
	 */
	private class saveListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
//			verifica se o jogador quer mesmo gravar as alteracoes
			Object[] options = {"Yes", "No"};
			int option = JOptionPane.showOptionDialog(null, "Are you sure you want to save these changes?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, GraphicGame.questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION)
			{
//				carrega os novos valores para a configuracao definitiva
				original.sizeMaze = temp.sizeMaze;
				original.numberDragons = temp.numberDragons;
				original.tactica = temp.tactica;
				original.keys = temp.keys;
				setVisible(false);
			}
		}
	}
	
	/**
	 * Implementacao de ActionListener para utilizar no botao de cancelar alteracoes
	 * @author migueloliveira
	 */
	private class discardListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
//			verifica se o jogador quer mesmo descartas as alteracoes
			Object[] options = {"Yes", "No"};
			int option = JOptionPane.showOptionDialog(null, "Are you sure you want to discard these changes?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, GraphicGame.questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION)
				setVisible(false);
		}
	}

}
