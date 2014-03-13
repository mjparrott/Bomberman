import java.util.Timer;
import java.util.TimerTask;

/**
 * AITimer
 * Timer to control the computer AI
 */
public class AITimer extends Timer
{
	Player player; //The player to control
	Task t; //What AI to run
	
	/**
	 * Sets up the AI for the computer
	 * @param p The computer player that needs to be moved
	 */
	public AITimer( Player p )
	{
		//1. Initialize all variables
		super();
		player = p;
		t = new Task();
		//Move once every 300 milliseconds
		scheduleAtFixedRate( t, 0, 300 );
	}
	
	/**
	 * Task
	 * The task to run for the computer AI
	 */
	public class Task extends TimerTask
	{
		public void run()
		{
			try
			{
				//Use the AI that the player was given
				player.AI( player.getAI() );
			}
			catch( Exception e ) { }
		}
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
}