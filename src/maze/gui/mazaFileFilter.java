package maze.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Classe descendete de FileFilter que representa
 * o filtro utilizado no JFileChooser para permitir 
 * apenas certos ficheiros
 * @author migueloliveira
 */
public class mazaFileFilter extends FileFilter {

	private final static String gameFileExtension = "mzesvgme";
	
	@Override
	/**
	 * Verifica se aceita o ficheiro recebido
	 * @param f
	 * @return boolean 
	 */
	public boolean accept(File f) {
		if (f.isFile()) // se for ficheiro e nao directoria
		{
			// verifica se a extensao do ficheiro e' a utilzada no jogo
			String extension = getExtension(f);

			if (extension != null) 
				if (extension.equals(gameFileExtension) )
					return true;
				else
					return false;
		}
		return false;
	}

	/**
	 * Devolve uma String com a extensao do ficheiro que recebe
	 * @param f ficheiro com a extensao a ser conhecida
	 * @return String extensao do ficheiro recebido
	 */
	private static String getExtension(File f) 
	{
		// passa o nome do ficheiro para uma String
        String ext = null;
        String s = f.getName();
        
        // calcula a posicao do ultimo ponto
        int i = s.lastIndexOf('.');

        // devolve a substring que esta apos esse ultimo ponto
        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public String getDescription() {return null;}
}
