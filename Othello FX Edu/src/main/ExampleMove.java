package main;

import com.eudycontreras.othello.capsules.AgentMove;
import com.eudycontreras.othello.capsules.MoveWrapper;

/**
 * <H2>Created by</h2> Eudy Contreras
 * <h4> Mozilla Public License 2.0 </h4>
 * Licensed under the Mozilla Public License 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <a href="https://www.mozilla.org/en-US/MPL/2.0/">visit Mozilla Public Lincense Version 2.0</a>
 * <H2>Class description</H2>
 * 
 * Example move template. You are welcome to use this move 
 * template to create your own move implementation but you are
 * also welcome to use the already implemented {@link MoveWrapper} class.
 * Each implemented class must contain a moveIndex wrapper which 
 * encapsulates information about where to place the chip. A definition of 
 * what a valid move is also needs to be implemented. Please see {@link MoveWrapper}
 * for an example implementation. Also take a look at {@link AgentMove} for
 * additional details.
 * 
 * @author Eudy Contreras
 *
 */
public class ExampleMove extends AgentMove{

	public ExampleMove() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Define a comparator for comparing the 
	 * reward of a move.
	 */
	@Override
	public int compareTo(AgentMove move) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Define what classifies a move as valid.
	 */
	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

}
