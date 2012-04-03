package maze.logic;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;

/*public class GameLog {

	private String filename;
	Calendar calendar;
	
	public GameLog(String filename)
	{
		calendar = new GregorianCalendar();
		this.filename = filename;
	}
	
	public void registarLog(String log)
	{
		try 
		{
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File(filename),true));
			try 
			{
				calendar = new GregorianCalendar();
				pw.println(calendar.get(Calendar.HOUR_OF_DAY) + ":" 
							+ calendar.get(Calendar.MINUTE) + ":" 
								+ calendar.get(Calendar.SECOND) + " -> " + log);
			} finally 
			{
				pw.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void abrirFicheiroNovo()
	{
		try 
		{
			PrintWriter p = new PrintWriter(new File(filename));
			try 
			{
				p.println(calendar.get(Calendar.HOUR_OF_DAY) + ":" 
							+ calendar.get(Calendar.MINUTE) + ":" 
								+ calendar.get(Calendar.SECOND) + " -> " + "A comecar um jogo novo");
			} finally 
			{
				p.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}*/
