package main;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.AgentController;
import com.eudycontreras.othello.controllers.AgentMove;
import com.eudycontreras.othello.controllers.IAgentMove;
import com.eudycontreras.othello.models.GameBoardState;
import com.eudycontreras.othello.threading.ThreadManager;
import com.eudycontreras.othello.threading.TimeSpan;

public class AgentA extends AgentMove implements IAgentMove{

	/**
	 * Delete the content of this method and Implement your logic here!
	 */
	@Override
	public MoveWrapper getMove(GameBoardState gameState) {
		return getExampleMove(gameState);
	}
	
	/**
	 * Default template move which serves as an example of how to implement move
	 * making logic. Note that this method does not use Alpha beta pruning and
	 * the use of this method can disqualify you
	 * 
	 * @param gameState
	 * @return
	 */
	private MoveWrapper getExampleMove(GameBoardState gameState){
		int waitTime = 500; // 1.5 seconds
		
		ThreadManager.pause(TimeSpan.millis(waitTime)); // Pauses execution for the wait time to cause delay
		
		return AgentController.getExampleMove(gameState); // returns an example AI move Note: this is not AB Pruning
	}

}
