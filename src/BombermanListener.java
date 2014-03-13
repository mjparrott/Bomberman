import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Keyboard listener for Bomberman
 */
public class BombermanListener implements KeyListener
{
	private BombermanGUI gui; /** The bomberman interface */
	
	/**
	 * Constructor for BombermanListener
	 * Sets up the keyboard for the game
	 * @param g The GUI to look for events in
	 */
	public BombermanListener( BombermanGUI g )
	{
		/**
		 * 1. Add the GUI to the listener
		 * 2. Add the listener to the GUI
		 */
		this.gui = g;
		gui.addListener( this );
	}
	
	/** Events that the need to implemented to make the KeyListener work */
	
	/**
	 * Handles key presses/user input
	 * @param event The KeyEvent that occurred
	 */
	public void keyPressed( KeyEvent event )
	{
		/**
		 * 1. Check to see what key was pressed
		 * 2. If the key was an arrow key then
		 * 2.1 Move the human player in the direction of the key the was pressed
		 * 3. Otherwise if the key was the spacebar then
		 * 3.1 Drop a bomb
		 */
		//Get what key was pressed
		int keycode = event.getKeyCode();

		//If the user pressed an arrow key then
		if( keycode == KeyEvent.VK_UP || keycode == KeyEvent.VK_DOWN || 
				keycode == KeyEvent.VK_RIGHT || keycode == KeyEvent.VK_LEFT )
		{
			//Move the user player in that direction
			for( int i = 0; i < 2; i++ )
				if( !gui.getPlayers()[i].isComputer() )
				{
					try
					{
						gui.getPlayers()[i].move( event.getKeyCode() );
					}
					catch( Exception e ) { }
				}
		}
		//Otherwise if the player pressed the space bar then
		else if( keycode == KeyEvent.VK_SPACE )
		{
			//Drop bomb
			for( int i = 0; i < 2; i++ )
				if( !gui.getPlayers()[i].isComputer() )
					gui.getPlayers()[i].dropBomb();
		}
	}
	
	public void keyReleased( KeyEvent event )
	{
	}
	public void keyTyped( KeyEvent event )
	{
	}
}