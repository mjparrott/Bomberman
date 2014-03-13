import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

/**
 * Player class
 * Keeps track of information for one player
 */
public class Player
{
	private int row; /** Row of the player on the board */
	private int column; /** Column of the player on the board */
	private int score; /** The score the player has */
	private boolean computer; /** Is the player a computer? */
	private int AI = 0; /** The level of the AI */
	private AITimer AITime; /** The timer for the AI */
	public BombTimer time; /** Timer for the bomb */
	private int dx; /** The X direction moved */
	private int dy; /** The Y direction moved */
	private int keyMove; /** The key press to move */
	
	/**
	 * Player constructor
	 * @param r The starting row of the player
	 * @param c The starting column of the player
	 */
	public Player( int r, int c )
	{
		/** 1. Initialize all variables */
		row = r;
		column = c;
		time = new BombTimer( new Bomb( 0, 0, -1 ) );
		AITime = new AITimer( this );
		dx = 0;
		dy = 0;
	}
	
	/**
	 * Make the player move
	 * @param direction The direction to move the player in. (Use KeyEvent constants)
	 */
	public void move( int direction ) throws Exception
	{
		/**
		 * 1. If the direction was up then
		 *     1.1. If there is an empty spot when you move up then (not a crate, brick)
		 *         1.1.1. Move the player to the empty spot
		 * 2. Repeat 1. for directions right, down, and left
		 */
		dx = dy = 0;
		if( direction == KeyEvent.VK_UP )
			dy = -1;
		else if( direction == KeyEvent.VK_DOWN )
			dy = 1;
		else if( direction == KeyEvent.VK_RIGHT )
			dx = 1;
		else if( direction == KeyEvent.VK_LEFT )
			dx = -1;
		
		//Get a copy of the board to look at
		Map.Piece[][] temp = BombermanGUI.getBoard().getBoard();
		
		//Make sure that the space the player is moving into isn't occupied
		if( row + dy >= 0 && row + dy < 13 && column + dx >= 0 && column + dx < 15 
				&& temp[row + dy][column + dx] != Map.Piece.BLOCK 
				&& temp[row + dy][column + dx] != Map.Piece.CRATE )
		{
			//If the player walked into an explosion then
			if( temp[ row + dy ][ column + dx ] == Map.Piece.EXPLOSION )
			{
				//If the player is a computer
				if( computer )
					//Tell the player they won
					JOptionPane.showMessageDialog( null, "You win!" );
				//Otherwise
				else
					//Tell the player they lost
					JOptionPane.showMessageDialog( null, "You lose player!" );
				
				//Restart the game
				BombermanGUI.resetBoard();
				return;
			}
			//Otherwise
			else
			{
				boolean set = false; //Keep track of whether the last spot of the player has been taken care of
				//Get of a copy of the the players
				Player[] p = BombermanGUI.getPlayers();
				
				//Check for all players belongings
				for( int i = 0; i < 2; i++ )
				{
					/** When moving off of a piece, make sure it is not a bomb, explosion, or player */
					
					//If the bomb hasn't exploded yet then only check one spot
					if( p[i].time.bombs.getRow() == row && p[i].time.bombs.getColumn() == column && !p[i].time.bombs.isExploding() )
					{
						temp[row][column] = Map.Piece.BOMB;
						BombermanGUI.setSquare( row, column, Map.Piece.BOMB );
						set = true;
					}
					//If the bomb is exploding, then you need to check all around it
					if( p[i].time.getBombs().isExploding() )
					{
						int[] dxx = { 0, 0, 1, 0, -1 };
						int[] dyy = { 0, -1, 0, 1, 0 };
						for( int j = 0; j < dxx.length; j++ )
						{
							if( row == ( p[i].time.bombs.getRow() + dyy[j] ) && column == p[i].time.bombs.getColumn() + dxx[j] )
							{
								temp[row][column] = Map.Piece.EXPLOSION;
								BombermanGUI.setSquare( row + dyy[j], column + dxx[j], Map.Piece.EXPLOSION );
								set = true;
							}
						}
					}
					
					//Only check if a player is on the square too is it isn't the same player
					if( p[i] != this ) //Different player
					{
						if( p[i].row == row && p[i].column == column )
						{
							if( p[i].isComputer() )
							{
								temp[row][column] = Map.Piece.COMPUTER;
								BombermanGUI.setSquare( row, column, Map.Piece.COMPUTER );
							}
							else
							{
								temp[row][column] = Map.Piece.PLAYER;
								BombermanGUI.setSquare( row, column, Map.Piece.PLAYER );
							}
							set = true;
						}
					}
				}
				
				//If there was nothing on the square before, then show nothing
				if( !set )
				{
					temp[row][column] = Map.Piece.NOTHING;
					BombermanGUI.setSquare( row, column, Map.Piece.NOTHING );
				}
			}
			
			//Adjust the player's position
			row += dy;
			column += dx;
		}
		
		//If the player is a computer then
		if( computer )
		{
			//Set a computer image at the player's new position
			BombermanGUI.setSquare( row, column, Map.Piece.COMPUTER );
			temp[row][column] = Map.Piece.COMPUTER;
		}
		//Otherwise
		else
		{
			//Set a user image at the player's new position
			BombermanGUI.setSquare( row, column, Map.Piece.PLAYER );
			temp[row][column] = Map.Piece.PLAYER;
		}
	}
	
	/**
	 * Drops a bomb that the player is currently on
	 */
	public void dropBomb()
	{
		/**
		 * 1. Add a bomb to the array of bombs
		 * 2. Set the bomb's row and column to the player's row and column
		 * 3. Set the start time to the current time
		 */
		//System.out.printf( "startTime: %d\n", time.bombs.getStartTime() );
		if( time.bombs.getStartTime() == -1 )
		{
			time.bombs.setRow( row );
			time.bombs.setColumn( column );
			//System.out.println( "Setting time" );
			time.bombs.setStartTime( System.currentTimeMillis() );
		}
	}
	
	/**
	 * Move randomly
	 */
	public void randomMove() throws Exception
	{
		/**
		 * 1. Choose a direction
		 * 2. Move in that direction
		 */
		int dir = new Random().nextInt(4);
		dx = dy = 0;
		switch( dir )
		{
		case 0:
			keyMove = KeyEvent.VK_UP;
			dy = 1;
			break;
		case 1:
			keyMove = KeyEvent.VK_DOWN;
			dy = -1;
			break;
		case 2:
			keyMove = KeyEvent.VK_RIGHT;
			dx = 1;
			break;
		case 3:
			keyMove = KeyEvent.VK_LEFT;
			dx = -1;
			break;
		default:
			break;
		}
	}
	
	public void AI( int level ) throws Exception
	{
		if( level == 1 )
			level1();
		else if( level == 2 )
			level2();
		else if( level == 3 )
			level3();
		else if( level == 4 )
			level4();
	}
	
	/**
	 * Level 1 AI for the computer players
	 * Move around randomly
	 */
	public void level1() throws Exception
	{
		randomMove();
		move( keyMove );
	}
	
	/**
	 * Level 2 AI for the computer players
	 * Make semi-intelligent decisions
	 * Don't walk into explosions
	 */
	public void level2() throws Exception
	{
		/**
		 * 1. Choose a direction
		 * 2. Move in that direction if there is not an explosive there
		 */
		randomMove();
		
		if( BombermanGUI.getBoard().getBoard()[row + dy][column + dx] != Map.Piece.EXPLOSION )
		{
			move( keyMove );
		}
	}
	
	/**
	 * Level 3 AI for the computer players
	 * Make semi-intelligent decisions
	 * Don't walk into explosions or bombs
	 * Drops bombs at random
	 */
	public void level3() throws Exception
	{
		/**
		 * 1. Choose a direction
		 * 2. Move in that direction if there is not an explosive or a bomb there
		 * 3. Drop bombs randomly
		 */
		randomMove();
		
		if( BombermanGUI.getBoard().getBoard()[row + dy][column + dx] != Map.Piece.EXPLOSION )
		{
			move( keyMove );
		}
		else if( BombermanGUI.getBoard().getBoard()[row + dy][column + dx] == Map.Piece.BOMB )
		{
			//Move the opposite way of them bomb
			dx *= -1;
			dy *= -1;
			
			if( dx == 1 )
				move( KeyEvent.VK_RIGHT );
			else if( dx == -1 )
				move( KeyEvent.VK_LEFT );
			else if( dy == -1 )
				move( KeyEvent.VK_UP );
			else if( dy == 1 )
				move( KeyEvent.VK_DOWN );	
		}
		
		//5% chance of randomly dropping a bomb
		int bomb = new Random().nextInt(100);
		if( bomb < 5 )
			dropBomb();
	}
	
	/**
	 * Level 4 AI for the computer players
	 * Make semi-intelligent decisions
	 * Don't walk into explosions or bombs
	 * Don't walk the area where a bomb will explode
	 * Drops bombs if there is a crate to be destroyed
	 */
	public void level4() throws Exception
	{
		/**
		 * 1. Choose a direction
		 * 2. Move in that direction if there is not an explosive or a bomb there
		 * 3. Drop bombs randomly
		 */
		
		boolean foundMove = false;
		//NESW
		int[] dxArray = { 0, 1, 0, -1 };
		int[] dyArray = { -1, 0, 1, 0 };
		int[] keyMoves = { KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT };
		int[] keyOpposites = { KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_UP, KeyEvent.VK_RIGHT };
		
		//Find all of the bombs
		for( int r = 0; r < 13; r++ )
		{
			for( int c = 0; c < 15; c++ )
			{
				if( BombermanGUI.getBoard().getBoard()[r][c] == Map.Piece.BOMB )
				{
					//If you are going to walk into a bomb, don't.
					//In fact, walk the other way
					for( int i = 0; i < dxArray.length; i++ )
					{
						if( r == row + dyArray[i] && c == column + dxArray[i] )
						{
							dx = -dxArray[i];
							dy = -dyArray[i];
							keyMove = keyOpposites[i];
							foundMove = true;
							break;
						}
					}
				}
			}
		}
		
		//If there is nothing wrong with the move
		if( foundMove )
			move( keyMove );
		else
		{
			randomMove();
			if( BombermanGUI.getBoard().getBoard()[row + dy][column + dx] != Map.Piece.EXPLOSION )
			{
				move( keyMove );
			}
		}
		
		//Drop a bomb when it will destroy a crate
		for( int i = 0; i < dxArray.length; i++ )
		{
			if( BombermanGUI.getBoard().getBoard()[row + dyArray[i]][column + dxArray[i]] == Map.Piece.CRATE )
			{
				dropBomb();
				break; //Can't drop two bombs
			}
		}
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
	 * @param score the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param computer the computer to set
	 */
	public void setComputer(boolean computer) {
		this.computer = computer;
	}

	/**
	 * @return the computer
	 */
	public boolean isComputer() {
		return computer;
	}
	
	
	/**
	 * @return the aI
	 */
	public int getAI() {
		return AI;
	}

	/**
	 * @param aI the aI to set
	 */
	public void setAI(int aI) {
		AI = aI;
	}

	/**
	 * @return the aITime
	 */
	public AITimer getAITime() {
		return AITime;
	}

	/**
	 * @param aITime the aITime to set
	 */
	public void setAITime(AITimer aITime) {
		AITime = aITime;
	}

	/**
	 * @return the dx
	 */
	public int getDx() {
		return dx;
	}

	/**
	 * @param dx the dx to set
	 */
	public void setDx(int dx) {
		this.dx = dx;
	}

	/**
	 * @return the dy
	 */
	public int getDy() {
		return dy;
	}

	/**
	 * @param dy the dy to set
	 */
	public void setDy(int dy) {
		this.dy = dy;
	}
}