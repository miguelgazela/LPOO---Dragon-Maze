package maze.gui;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.border.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import maze.logic.ConfigurationMaze;

public class Configuration extends JDialog implements ChangeListener {
	
	private static final long serialVersionUID = 917983375861534320L;
	
	private static final int MAZE_SIZE_MIN = 10;
	private static final int MAZE_SIZE_MAX = 60;
	private static final int NUMBER_DRAGONS_MIN = 0;
	private static final int NUMBER_DRAGONS_MAX = 15;
	
	private JSlider mazeSizeSlider;
	private JSlider numberDragonsSlider;
	private JComboBox wantedStrategy;
	
	private ConfigurationMaze temp;
	private ConfigurationMaze original;
	
	private JPanel buttonPanel;
	private JPanel changeKeys;
	
	private JButton save;
	private JButton discard;
	
	private JButton up, down, right, left;
	
	public Configuration (JFrame frame, ConfigurationMaze configs) {
		
		super(frame, "Configurations", true);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		
		/* inicializar dados necessarios */
		temp = new ConfigurationMaze(15,1,1);
		original = configs;
		temp.numberDragons = original.numberDragons;
		temp.sizeMaze = original.sizeMaze;
		temp.tactica = original.tactica;
		for (int i = 0; i < 4; i++)
			temp.keys[i] = original.keys[i];
	
		/* construir e definir componentes utilizados */
		getContentPane().setLayout(new GridLayout(0,1));
		setMazeSizeSlider();
		setNumberDragonsSlider();
		setComboBox();
		setChangeKeys(frame);
		setButtons();
		
		/* apresentar janela centrada no ecra*/
		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void setChangeKeys(JFrame f) {
		
		changeKeys = new JPanel (new FlowLayout(FlowLayout.CENTER, 5, 10));
		TitledBorder changeKeysTitle = BorderFactory.createTitledBorder("change directional keys");
		changeKeys.setBorder(changeKeysTitle);
		
		up = new JButton ("UP");
		down = new JButton ("DOWN");
		right = new JButton ("RIGHT");
		left = new JButton ("LEFT");
		
		up.addActionListener(new changeKeyListener(f));
		down.addActionListener(new changeKeyListener(f));
		right.addActionListener(new changeKeyListener(f));
		left.addActionListener(new changeKeyListener(f));
		
		changeKeys.add(up);
		changeKeys.add(down);
		changeKeys.add(right);
		changeKeys.add(left);
		
		getContentPane().add(changeKeys);
	}
	
	private class changeKeyListener implements ActionListener {

		JFrame f;
		JDialog d;
		
		public changeKeyListener (JFrame f) {
			this.f = f;
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			
			d = new JDialog (f, "New " + ((JButton)event.getSource()).getText() + " directional key", true);
			d.setLayout(new GridLayout (0,1));
			
			int option = -1;
			
			if ( ((JButton)event.getSource()).getText() == "UP")
				option = 0;
			else if ( ((JButton)event.getSource()).getText() == "DOWN")
				option = 1;
			else if ( ((JButton)event.getSource()).getText() == "RIGHT")
				option = 2;
			else if ( ((JButton)event.getSource()).getText() == "LEFT")
				option = 3;
			
			Icon keyInstructions = new ImageIcon("images/newKey.png");
			JLabel t1 = new JLabel (keyInstructions);
			
			d.add(t1);
			
			d.addKeyListener(new keyListener(option, d));
			
			d.pack();
			d.setLocationRelativeTo(null);
			d.setResizable(false);
			d.setVisible(true);
		}
		
		private class keyListener implements KeyListener
		{
			int option;
			JDialog d;
			
			public keyListener (int option, JDialog d) {
				this.option = option;
				this.d = d;
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				int keyCode = e.getKeyCode();
				
				if ( (keyCode >= KeyEvent.VK_0 && keyCode <= KeyEvent.VK_9) 
						|| (keyCode >= KeyEvent.VK_A && keyCode <= KeyEvent.VK_Z)
							|| (keyCode >= KeyEvent.VK_LEFT && keyCode <= KeyEvent.VK_DOWN) )
				{
					if (temp.keyAlreadyUsed(e))
						JOptionPane.showMessageDialog(null, "That key is already being used!", "", JOptionPane.ERROR_MESSAGE);
					else
					{
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

	private void setMazeSizeSlider() {
		
		/* cria o slider, mete-lhe um border e um listener de mudancas */
		mazeSizeSlider  = new JSlider(JSlider.HORIZONTAL, MAZE_SIZE_MIN, MAZE_SIZE_MAX, original.sizeMaze);
		TitledBorder mazeSizeTitle = BorderFactory.createTitledBorder("maze size: " + original.sizeMaze);
		mazeSizeSlider.setBorder(mazeSizeTitle);
		mazeSizeSlider.addChangeListener(this);

		/* configura apresentacao do slider */
		mazeSizeSlider.setMajorTickSpacing(5);
		mazeSizeSlider.setMinorTickSpacing(1);
		mazeSizeSlider.setPaintTicks(true);
		mazeSizeSlider.setPaintLabels(true);
		
		getContentPane().add(mazeSizeSlider);
	}

	private void setNumberDragonsSlider() {
		
		/* cria o slider, mete-lhe um border e um listener de mudancas */
		numberDragonsSlider  = new JSlider(JSlider.HORIZONTAL, NUMBER_DRAGONS_MIN, NUMBER_DRAGONS_MAX, original.numberDragons);
		numberDragonsSlider.setBorder(BorderFactory.createTitledBorder("number of dragons: " + original.numberDragons));
		numberDragonsSlider.addChangeListener(this);

		// configura apresentacao do slider */
		numberDragonsSlider.setMajorTickSpacing(3);
		numberDragonsSlider.setMinorTickSpacing(1);
		numberDragonsSlider.setPaintTicks(true);
		numberDragonsSlider.setPaintLabels(true);
		
		getContentPane().add(numberDragonsSlider);
	}

	private void setComboBox() {
		
		String[] strategies = { "The dragon(s) is/are always awake", "The dragon(s) is/are always sleeping", "The dragon(s) is/are awake but can also fall asleep"};

		// cria a combobox, mostra a tactica atual e mete-lhe um border e um listener de mudancas */
		wantedStrategy = new JComboBox(strategies);
		wantedStrategy.setSelectedIndex(original.tactica-1);
		wantedStrategy.setBorder(BorderFactory.createTitledBorder("wanted strategy"));
		wantedStrategy.addActionListener(new changedStrategyListener());
		
		getContentPane().add(wantedStrategy);
	}

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
		/* se nenhum dos sliders ainda esta a ser alterado */
		if( !((JSlider)e.getSource()).getValueIsAdjusting() || !((JSlider)e.getSource()).getValueIsAdjusting() )
		{
			/* guarda os valores para a variavel temporaria */
             temp.sizeMaze = mazeSizeSlider.getValue();
             temp.numberDragons = numberDragonsSlider.getValue();
             
             /* altera os titulos dos sliders para o valor atual */
             mazeSizeSlider.setBorder(BorderFactory.createTitledBorder("maze size: " + temp.sizeMaze));
             numberDragonsSlider.setBorder(BorderFactory.createTitledBorder("number of dragons: " + temp.numberDragons));
             
             /* verifica se deve permitir alterar as tactivas */
             if (temp.numberDragons == 0)
            	 wantedStrategy.setEnabled(false);
             else
            	 wantedStrategy.setEnabled(true);
		}
    }
	
	private class changedStrategyListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* guarda a tactica nova para a variavel temporario */ 
			String strategy = (String) (((JComboBox)e.getSource() ).getSelectedItem());
			
			if (strategy == "The dragon(s) is/are always awake")
				temp.tactica = 1;
			else if (strategy == "The dragon(s) is/are always sleeping")
				temp.tactica = 2;
			else if (strategy == "The dragon(s) is/are awake but can also fall asleep")
				temp.tactica = 3;
		}
	}
	
	private class saveListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* verifica se o jogador quer mesmo gravar as alteracoes */
			Object[] options = {"Yes", "No"};
			int option = JOptionPane.showOptionDialog(null, "Are you sure you want to save these changes?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, GraphicGame.questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION)
			{
				/* carrega os valores "novos" para a configuracao definitiva */
				original.sizeMaze = temp.sizeMaze;
				original.numberDragons = temp.numberDragons;
				original.tactica = temp.tactica;
				original.keys = temp.keys;
				setVisible(false);
			}
		}
	}
	
	private class discardListener implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/* verifica se o jogador quer mesmo descartas as alteracoes */
			Object[] options = {"Yes", "No"};
			int option = JOptionPane.showOptionDialog(null, "Are you sure you want to discard these changes?", "", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, GraphicGame.questionMark, options, options[1]);
			if (option == JOptionPane.YES_OPTION)
				setVisible(false);
		}
	}

}
