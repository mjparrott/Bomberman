import javax.swing.JOptionPane;

/**
 * Bomb class
 * Keep track of bomb information
 */
public class Bomb
{
	private long startTime; /** When the bomb was dropped */
	private int row; /** What row the bomb is in on the board */
	private int column; /** What column the bomb is in on the board */
	private boolean exploding; /** If the bomb is exploding; prevents it from exploding multiple times */
	
	/**
	 * 
	 * @param r The row the player is on
	 * @param c The column the player is on
	 * @param time What time the bomb was dropped
	 */
	public Bomb( int r, int c, long time )
	{
		/** 1. Initialize all the variables */
		row = r;
		column = c;
		startTime = time;
		exploding = false;
	}
	
	/**
	 * Checks to see how long the bomb hasn't exploded for
	 * @return What state the bomb is in (0 = none, 1 = exploded, 2 = exploding, 3 = finished exploding
	 */
	public int checkState()
	{
		/**
		 * 1. If the bomb has not exploded more than 4 secs then
		 * 	1.1. Return true
		 * 2. Otherwise
		 *  2.2. Return false
		 */
		long time;
		int state;
		
		if( exploding )
		{
			//How long the explosion graphic stays on the screen
			time = 1500;
			state = 3;
		}
		else
		{
			//How long the bomb graphic stays on the screen
			time = 2500;
			state = 1;
		}
		
		if( startTime != -1 && System.currentTimeMillis() - startTime > time )
			return state;
		else
			return state - 1;
	}
	
	/**
	 * Makes the bomb explode
	 */
	public void explode() throws Exception
	{
		/**
		 * 1. Check to see what is above the bomb (Any players)
		 * 2. If it is a crate or an empty spot then
		 * 2.1. Place the explosion there
		 * 3. Otherwise if it is a player
		 * 3.1. Make them lose a life
		 * 4. Otherwise
		 * 4.1 Leave the spot alone
		 * 5. Repeat 1. for directions right, down, and left
		 */
		Player[] p = BombermanGUI.getPlayers();
		Map.Piece[][] b = BombermanGUI.getBoard().getBoard();
		int[] dx = { 0, 1, 0, -1, 0 };
		int[] dy = { 0, 0, 1, 0, -1 };
		
		for( int i = 0; i < dx.length; i++ )
		{
			if( b[row + dy[i]][column + dx[i]] != Map.Piece.BLOCK )
			{
				//Check to see if the bomb exploded on a player
				if( b[row + dy[i]][column + dx[i]] == Map.Piece.PLAYER )
				{
					JOptionPane.showMessageDialog( null, "You lose bomb!" );
					BombermanGUI.resetBoard();
					return;
				}
				else if( b[row + dy[i]][column + dx[i]] == Map.Piece.COMPUTER )
				{
					JOptionPane.showMessageDialog( null, "You win!" );
					BombermanGUI.resetBoard();
					return;
				}
				b[row + dy[i]][column + dx[i]] = Map.Piece.EXPLOSION;
				BombermanGUI.setSquare(row + dy[i], column + dx[i], Map.Piece.EXPLOSION );
			}
		}
		
		exploding = true;
	}
	
	/**
	 * Sets up the bomb
	 */
	public void makeNew()
	{
		//Do this instead of creating a "new Bomb()" because of timer issues
		row = 0;
		column = 0;
		startTime = -1;
		exploding = false;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the startTime
	 */
	public long getStartTime() {
		return startTime;
	}

	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @param column the column to set
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * @param exploding the exploding to set
	 */
	public void setExploding(boolean exploding) {
		this.exploding = exploding;
	}

	/**
	 * @return the exploding
	 */
	public boolean isExploding() {
		return exploding;
	}
}