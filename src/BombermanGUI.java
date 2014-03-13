import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * GUI class
 * Holds information about the game board and the interface
 */
public class BombermanGUI
{
	private JFrame frame; /** What is on the screen */
	private static JLabel[][] squares; /** The labels to show on the screen */
	private static Map board; /** Keeping track of the map */
	private static Player[] players; /** The human and computer players */
	private static int AIDifficulty; /** The difficulty level of the AI */
	
	/**
	 * Constructor for BombermanGUI
	 * Initializes players based on what the user asked for. (Number of human players, computer players,
	 * difficulty of the computers)
	 * Sets up the interface for Bomberman
	 */
	public BombermanGUI() throws Exception
	{
		/** Create the frame to play in */
		frame = new JFrame( "Bomberman" );
		JPanel panel = (JPanel) frame.getContentPane();
		
		/** Set the layout of the frame. 13 rows by 15 columns */
		panel.setLayout( new GridLayout( 13, 15 ) );
		
		/** Create the board arrays */
		squares = new JLabel[13][15];
		
		/** Create all of the squares */
		for( int row = 0; row < 13; row++ )
		{
			for( int column = 0; column < 15; column++ )
			{
				squares[row][column] = new JLabel();
				
				/** Add the square to the interface */
				panel.add( squares[row][column] );
			}
		}
		
		frame.setContentPane( panel );
		
		/** Set the size of the board to fit a 13x15 board, where squares are 50x50 */
		frame.setSize( 750, 650 );
		
		/** Show the board */
		frame.setVisible( true );
		
		/** Exit the program when the [X] is pressed */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//Let the user choose the difficulty of the AI
		String[] choices = { "1", "2", "3", "4" };
		AIDifficulty = JOptionPane.showOptionDialog(
		                               null                     
		                             , "What level do you want the AI to be?"     
		                             , "Choose the difficulty"             
		                             , JOptionPane.YES_NO_OPTION
		                             , JOptionPane.PLAIN_MESSAGE 
		                             , null                     
		                             , choices                    
		                             , null
		                           );
		//Make it not a zero based number
		AIDifficulty++;
		
		/**
		 * 1. Create the map
		 * 2. Create all of the players necessary
		 * 3. Start the game
		 */
		//Only create the players once!!
		players = new Player[4];
		players[0] = new Player( 0, 0 );
		players[1] = new Player( 0, 0 );
		
		board = new Map();
		playerSetup();
	}
	
	/**
	 * Adds the listener to look at events from
	 * @param listener The KeyboardListener to check for events from
	 */
	public void addListener( BombermanListener listener )
	{
		frame.addKeyListener( listener );
	}
	
	public static void setSquare( int row, int column, Map.Piece type )
	{
		String s = "Images/player-down.jpg";
		
		//Show the image based on what type it is
		if( type == Map.Piece.BLOCK )
			s = "Images/block.jpg";
		else if( type == Map.Piece.BOMB )
			s = "Images/bomb.jpg";
		else if( type == Map.Piece.BLOCK )
			s = "Images/block.jpg";
		else if ( type == Map.Piece.COMPUTER )
		{
			for( int i = 0; i < 2; i++ )
			{
				//If the computer was found then
				if( row == players[i].getRow() && column == players[i].getColumn() && players[i].isComputer() )
				{
					//Show the image depending on what direction they are facing
					if( players[i].getDx() == 1 )
						s = "Images/computer-right.jpg";
					else if( players[i].getDx() == -1 )
						s = "Images/computer-left.jpg";
					else if( players[i].getDy() == 1 )
						s = "Images/computer-down.jpg";
					else
						s = "Images/computer-up.jpg";
				}
			}
		}
		else if( type == Map.Piece.CRATE )
			s = "Images/crate.jpg";
		else if( type == Map.Piece.EXPLOSION )
			s = "Images/explosion.jpg";
		else if( type == Map.Piece.NOTHING )
			s = "Images/nothing.jpg";
		else if( type == Map.Piece.PLAYER )
		{
			//2 players
			for( int i = 0; i < 2; i++ )
			{
				//If a player was found then
				if( row == players[i].getRow() && column == players[i].getColumn() && !players[i].isComputer() )
				{
					//Show the image depending on what direction they are facing
					if( players[i].getDx() == 1 )
						s = "Images/player-right.jpg";
					else if( players[i].getDx() == -1 )
						s = "Images/player-left.jpg";
					else if( players[i].getDy() == 1 )
						s = "Images/player-down.jpg";
					else
						s = "Images/player-up.jpg";
				}
			}
		}
		
		//Show the image
		squares[row][column].setIcon( new ImageIcon( s ) );
	}
	
	public static void playerSetup()
	{
		int playerCount = 0; //Keep track of how many players/computers have been found
		
		//Look on the board for any players or computers
		for( int r = 0; r < 13; r++ )
		{
			for( int c = 0; c < 15; c++ )
			{
				//Set up all of the necessary variables for a player
				if( board.getBoard()[r][c] == Map.Piece.PLAYER )
				{
					players[playerCount].setRow( r );
					players[playerCount].setColumn( c );
					players[playerCount].setComputer( false );
					
					players[playerCount].time.bombs.makeNew();
					
					playerCount++;
				}
				else if( board.getBoard()[r][c] == Map.Piece.COMPUTER )
				{
					players[playerCount].setRow( r );
					players[playerCount].setColumn( c );
					players[playerCount].setComputer( true );
					
					players[playerCount].time.bombs.makeNew();
					
					//Set the AI difficulty
					players[playerCount].setAI( AIDifficulty );
					AITimer ai = players[playerCount].getAITime();
					ai.setPlayer( players[playerCount] );
					players[playerCount].setAITime( ai );
					
					playerCount++;
				}
			}
		}
	}
	
	/**
	 * Get ready to play another game
	 * @throws Exception From Map
	 */
	public static void resetBoard() throws Exception
	{
		board = new Map();
		playerSetup();
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Map board) {
		this.board = board;
	}

	/**
	 * @return the board
	 */
	public static Map getBoard() {
		return board;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}

	/**
	 * @return the players
	 */
	public static Player[] getPlayers() {
		return players;
	}
}