package maze.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

public class mazaFileFilter extends FileFilter {

	private final static String gameFileExtension = "mzesvgme";
	
	@Override
	public boolean accept(File f) {
		if (f.isFile())
		{
			String extension = getExtension(f);

			if (extension != null) 
				if (extension.equals(gameFileExtension) )
					return true;
				else
					return false;
		}
		return false;
	}

	private static String getExtension(File f) 
	{
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

	@Override
	public String getDescription() {
		return null;
	}

}
