/**
 * Name: Michael Parrott
 * Date: January , 2011
 * Course code: ICS3U1
 * Title: Bomberman
 * Description: A Bomberman game coded in Java
 * Notes:
 * Extra Features: 
 * 
 * @author Michael Parrott
 */

public class Bomberman
{
	public static void main( String[] args ) throws Exception
	{
		BombermanGUI gui = new BombermanGUI();
		BombermanListener listener = new BombermanListener( gui );
	}
}