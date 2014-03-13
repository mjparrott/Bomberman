import java.io.FileReader;
import java.util.Scanner;

/**
 * Map class
 * Keeps track of all of the pieces on the board
 */
public class Map
{
	public enum Piece { BLOCK, BOMB, COMPUTER, CRATE, EXPLOSION, PLAYER, NOTHING }; /** The different types of pieces */
	private Piece[][] board; /** The pieces on the board */
	
	/**
	 * Set up the board
	 * Load the map from a text file
	 * 
	 * @param filename File to load the map from
	 */
	public Map() throws Exception
	{
		/**
		 * 1. Load the default map
		 */
		loadMap( "default.txt" );
	}
	
	/**
	 * Loads the map
	 * @param filename The filename of the map to load
	 * @throws Exception If the file doesn't exist
	 */
	public void loadMap( String filename ) throws Exception
	{
		char[][] map = new char[13][15];
		board = new Piece[13][15];
		Scanner in = new Scanner( new FileReader( filename ) );
		/** 1. Go through all of the characters in the array
		  * 1.1 If the character represents Player 1 then
		  * 1.1.1. Set the type of current piece on the board to Player 1 character, ''
		  * Repeat 1.1 for Player 2-4, enemies, crates, blocks, and power-ups
		  */
		for( int r = 0; r < 13; r++ )
		{
			map[r] = in.next().toCharArray();
			
			for( int c = 0; c < 15; c++ )
			{
				switch( map[r][c] )
				{
				case '1':
					board[r][c] = Piece.PLAYER;
					break;
				case 'C':
					board[r][c] = Piece.CRATE;
					break;
				case 'c':
					board[r][c] = Piece.COMPUTER;
					break;
				case 'B':
					board[r][c] = Piece.BLOCK;
					break;
				case 'b':
					board[r][c] = Piece.BOMB;
					break;
				case 'N':
					board[r][c] = Piece.NOTHING;
					break;
				default:
					break;
				}
				
				
			}
		}
		
		//Show the board on the screen
		for( int r = 0; r < 13; r++ )
			for( int c = 0; c < 15; c++ )
				BombermanGUI.setSquare( r, c, board[r][c] );
	}

	/**
	 * @param board the board to set
	 */
	public void setBoard(Piece[][] board) {
		this.board = board;
	}

	/**
	 * @return the board
	 */
	public Piece[][] getBoard() {
		return board;
	}
}