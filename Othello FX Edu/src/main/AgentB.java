package main;

import com.eudycontreras.othello.capsules.MoveWrapper;
import com.eudycontreras.othello.controllers.AgentMove;
import com.eudycontreras.othello.controllers.IAgentMove;
import com.eudycontreras.othello.exceptions.NotImplementedException;
import com.eudycontreras.othello.models.GameBoardState;

public class AgentB extends AgentMove implements IAgentMove{

	/**
	 * Delete the content of this method and Implement your logic here!
	 */
	@Override
	public MoveWrapper getMove(GameBoardState gameState) {
		try {
			throw new NotImplementedException("This method has not yet been implemented!");
		} catch (NotImplementedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		
		return null;
	}


}

