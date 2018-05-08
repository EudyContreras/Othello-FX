
# Othello-FX-Framework ![JavaFX](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/javaFX.png)


> Othello game framework which can be use for adding Othello AI Agents..



This is an an Othello/Riversi game made in Java using JavaFX GUI Framework. 
The game allows you to implement and add your own AI agents which you will then be able to test and play againts. OthelloFX comes with 3 different Agent implementations excluding the usual ones such as MiniMax which you will have to implement on your own. The game can be ran in multiple modes:

* Human Player vs Human Player
* Humna Player vs AI Agent
* AI Agent vs AI Agent.

The game also .


![Image of OthelloFX](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/OthelloFX.png)


## Getting Started

Refer to the main folder in order to find the documented files you need. There you will see the [User Settings](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/UserSettings.java) file, [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) in which you will run your agent, as well as various example agents and implementations. Take a look at the [Agent Controller](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/controllers/AgentController.java) for useful methods, etc


#### Move

Implement a move if necessary or use the existing implementation. A move must extend from [Abstract Move](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/capsules/AbstractMove.java) Implements the compareTo method and the IsValid. In those methods you define what makes a move better than the other as well as how you define a move as valid. Take a look at [Move Wrapper](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/capsules/MoveWrapper.java) for an example of how a move is implemented. Only the isValid and the compareTo method are needed by the framework but as you can see you can also create your own helper methods.


> Example Move class which extends from AbstractMove
```java

public class ExampleMove extends AbstractMove{

	public ExampleMove() {	
	}

	@Override
	public int compareTo(MoveWrapper move) {
		return 0;
	}

	@Override
	public boolean isValid() {
		return false;
	}
}

```


#### AI Agent

Your agent must extend from the [Agent Move](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/controllers/AgentMove.java) which is a simple POJO containing important information used for determining how good your move algorithm performs, along with other information regarding the move search. The constructor of an *AgentMove* allows the turn/type to be specified as well as a name if you wish to give your agent a name.


> Example Agent class which extends from AgentMove
```java

public class ExampleAgent extends AgentMove{

	@Override
	public AbstractMove getMove(GameBoardState gameState) {
		return yourMove();
	}
}

```



#### Agent Manager

The [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) is the class you will use to run your agents. In order to run your agent simply create an instance of your agent and pass it through the second parametter of the instance of Othello just how it is shown below. The game manager will allow you to pass one or two agents. The first agent must always be PlayerOne while the second must always be PlayerTwo. The player type is determine by the turn. Player one always starts. 

> Agent Manager class main launcher application
```java

public class AgentManager extends Application{

	@Override
	public void start(Stage primaryStage) {
		new Othello(primaryStage, new ExampleAgent());
	}
	public static void main(String[] args) {
		launch(args);
	}
}

```


#### Notes


Take a look at the [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) to see a variety of helper methods as well as various implementations of different angents. There are other utility classes which might be of help 


#### Customization


The [User Settings](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/UserSettings.java) allows you to modify a variety of settings. Here you have a description of each setting.



| Property                 	| Values                               | Description                                  |
| ----------------------------- | ------------------------------------ | -------------------------------------------- |
| DEEPENING                	| NONE, ITERATIVE, DYNAMIC             | Sets the deepening style to use.             |
| GAME_MODE             	| P vs P, A vs A, P vs A               | Sets the game mode player vs player, etc     |
| MAX_SEARCH_TIME          	| 5000      milliseconds               | Sets the maximun allowed search time         |
| MIN_SEARCH_TIME          	| 0         milliseconds               | Sets the minimun allowed search time         |
| SEARCH_TIME              	| 0 - 5000  milliseconds               | Sets the search time to aim for              |
| PLAYER_ONE               	| Any String                           | Sets the name of the player one              |
| PLAYER_TWO               	| Any String                           | Sets the name of the player two              |
| USE_ANIMATION            	| true or false                        | Sets whether animations should be use or not |
| GAME_WINDOW_SCALE        	| 0 - 2                                | Sets the scale of of the window. Default = 1 |
| BOARD_GRID_SIZE          	| 4, 6, 8                              | Sets the size of the board to be used        |
| THEME_INDEX              	| 0 - 13                               | Sets the theme to be used at startup         |



#### Prerequisites

Things you need in order to be able to run OthelloFX

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or highter



#### Installing

A step by step series of examples that tell you have to get a development env running

1. Download and install the any [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) above 8
2. Clone this project using the console or your favorite IDE.
3. Run from the [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) and test to assure everything is working as expected
4. If you run into trouble ask google for help
5. If google cant help me you may contact me at the email at the end of this page.




### Built With


* [JavaFX](https://en.wikipedia.org/wiki/JavaFX) - The GUI framework used
* [Java](https://maven.apache.org/) - The programming language used




### Contributing


Please read [Contributing](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/CONTRIBUTING.md) for details on the code base code of conduct, and the process for submitting pull requests to *OthelloFX*



### Authors


* **Eudy Contreras** 



### Acknowledgments


* Jose Font for inspiration and education.
* Other teachers at Malmö Univertiy.



### Contact


Any questions reach me at.
Econtreras12@live.com



### License


This project is licensed under the MIT License - see the [Licence](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/LICENSE) file for details

------------
    The MIT License (MIT)
    
    Copyright (c) 2018 Eudy Contreras
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
