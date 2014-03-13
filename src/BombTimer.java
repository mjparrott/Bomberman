import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;

public class BombTimer extends Timer
{
	public Bomb bombs;
	Task t;
	
	/**
	 * Sets up the timer for exploding bombs
	 * @param b The bomb to be associated with the bomb timer
	 */
	public BombTimer( Bomb b )
	{
		//Initialize all variables for the class
		super();
		bombs = b;
		t = new Task();
		scheduleAtFixedRate( t, 0, 50 );
	}
	
	public class Task extends TimerTask
	{
		/**
		 * What to do do every interval
		 */
		public void run()
		{
			//If is time for the bomb to explode
			if( bombs != null ) //Not sure if this is needed, too lazy to take it out and test it
			{
				//Time for the bomb to explode
				if( bombs.checkState() == 1 )
				{
					try
					{
						bombs.explode();
					}
					catch( Exception e ) { }
					
					//Reset the bomb time for the explosion graphic
					//Check is done in case a player loses during an explosion
					if( bombs.getStartTime() != -1 )
						bombs.setStartTime( System.currentTimeMillis() );
				}
				//Remove the pictures of the explosion; times up
				else if( bombs.checkState() == 3 )
				{
					
					//Check for all explosions around the bomb
					Map.Piece[][] b = BombermanGUI.getBoard().getBoard();
					
					int[] dx = { 0, 1, 0, -1, 0 };
					int[] dy = { 0, 0, 1, 0, -1 };
					
					for( int i = 0; i < dx.length; i++ )
					{
						if( b[bombs.getRow() + dy[i]][bombs.getColumn() + dx[i]] == Map.Piece.EXPLOSION )
						{
							b[bombs.getRow() + dy[i]][bombs.getColumn() + dx[i]] = Map.Piece.NOTHING;
							BombermanGUI.setSquare(bombs.getRow() + dy[i], bombs.getColumn() + dx[i], Map.Piece.NOTHING );
						}
					}
					
					//Make the bomb disappear
					bombs.makeNew();
				}
			}
		}
	}

	/**
	 * @return the bombs
	 */
	public Bomb getBombs() {
		return bombs;
	}

	/**
	 * @param bombs the bombs to set
	 */
	public void setBombs(Bomb bombs) {
		this.bombs = bombs;
	}
}