# Othello FX Framework 
![OthelloFX Logo](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/logo.png)




> Othello game framework which can be used as a platform for testing Othello AI Agents as well as just a normal Othello game.
	



## Description

This is an an Othello/Riversi game made in Java using JavaFX GUI Framework. 
The game allows you to implement and add your own AI agents which you will then be able to test and play againts. OthelloFX comes with 3 different Agent implementations purposefully excluding the usual ones such as MiniMax which you will have to implement on your own. The framework can run in multiple modes:

* Human Player vs Human Player
* Humna Player vs AI Agent
* AI Agent vs AI Agent.


![Image of OthelloFX](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/OthelloFX2.png)


## Getting Started

Refer to the main folder in order to find the documented files you need. There you will see the [User Settings](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/UserSettings.java) file, the [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) in which you will run your agent/s, as well as various example agents and implementations. Take a look at the [Agent Controller](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/controllers/AgentController.java) for useful static methods, etc


#### Agent Move

Implement a move if necessary or use the existing implementation. A move must extend from [Agent Move](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/capsules/AgentMove.java) and implement the compareTo and the IsValid methods. In those methods you define what makes a move better than the other as well as how you define a move as valid. Take a look at [Move Wrapper](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/capsules/MoveWrapper.java) for an example of how a move is implemented. Only the isValid and the compareTo method are needed by the framework but as you can see you can also create your own helper methods.


> Example Move class which extends from AgentMove
```java

public class ExampleMove extends AgentMove{

	public ExampleMove() {	
	}

	@Override
	public int compareTo(AgentMove move) {
		return 0;
	}

	@Override
	public boolean isValid() {
		return false;
	}
}

```


#### AI Agent

Your agent must extend from the [Agent](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/controllers/Agent.java) which is a simple POJO containing important information used for determining how good your move algorithm performs, along with other information regarding the move search. The constructor of an *AgentMove* allows the turn/type to be specified as well as a name in case you wish to give your agent a name.


> Example Agent class which extends from Agent
```java

public class ExampleAgent extends Agent{

	@Override
	public AgentMove getMove(GameBoardState gameState) {
		return yourMove();
	}
}

```



#### Agent Manager

The [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) is the class you will use to run your agent/s. In order to run your agent/s simply create an instance of your agent and pass it through the second parametter of the of the Othello constructor just how it is shown below. The game manager will allow you to pass one or two agents. The first agent must always be PlayerOne while the second must always be PlayerTwo. The player type is determine by the turn. Player one always starts. 

> Agent Manager and main launcher application
```java

public class AgentManager extends Application{

	@Override
	public void start(Stage primaryStage) {
		new Othello(primaryStage, new ExampleAgent("Some Name", PlayerTurn.PLAYER_ONE));
	}
	public static void main(String[] args) {
		launch(args);
	}
}

```



#### Notes


Take a look at the [Agent Controller](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/com/eudycontreras/othello/controllers/AgentController.java) to see a variety of helper methods as well as various implementations of different dumb and smart agents. There are other utility classes which might be of help. Explore the utilities package!



#### Customization


The [User Settings](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/UserSettings.java) allows you to modify a variety of settings. Here you have a description of each setting.



| Property                    | Values                                   | Description                                     |
| ----------------------------| ---------------------------------------- | ------------------------------------------------|
| **DEEPENING**               | NONE, ITERATIVE, DYNAMIC                 | Sets the deepening style to use.          	   |
| **GAME_MODE**               | Ply vs Ply, Agent vs Agent, Ply vs Agent | Sets the game mode to use 	   		   | 
| **MAX_SEARCH_TIME**         | 5000      milliseconds by rules          | Sets the maximun allowed search time            |
| **MIN_SEARCH_TIME**          | 0         milliseconds               	 | Sets the minimun allowed search time            |
| **TURN_INTERVAL**            | 0         milliseconds               	 | Sets the time interval between moves            |
| **START_DELAY**              | 0         milliseconds             	 | Sets the delay before the first move is made    |
| **PLAYER_ONE**              | Any String                               | Sets the name of the player one                 |
| **PLAYER_TWO**              | Any String                               | Sets the name of the player two                 |
| **USE_ANIMATION**            | true or false                            | Sets whether animations should be used 	   |
| **GAME_WINDOW_SCALE**        | 0 - 2                                    | Sets the scale of of the window.		   |
| **BOARD_GRID_SIZE**         | 4, 6, 8                                  | Sets the size of the board to be used           |
| **THEME_INDEX**             | 0 - 13                                   | Sets the theme to be used at startup            |




#### Prerequisites

Things you need in order to be able to run *OthelloFX*

* [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or highter




#### Installation and setup

1. Download and install the any [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) above 8
2. Clone this project using the console or your favorite IDE.
3. Run from the [Agent Manager](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/Othello%20FX%20Edu/src/main/AgentManager.java) and test to assure everything is working as expected
4. If you run into trouble ask google for help ;)
5. If google cant help you!!!! feel free to contact me at the email at the end of this page.





#### Future works


There are some things which I plan to add to the framework. These things will be shown here along with popular demands

> - [ ] **More options to the main menu so that users have more control through the interface**.
> - [ ] **Maybe an online multiplayer option for users to test their agents remotely in competitions**




#### Built With

![JavaFX](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/javaFX.png)
* [JavaFX](https://en.wikipedia.org/wiki/JavaFX) - The GUI framework used
* [Java](https://maven.apache.org/) - The programming language used





#### Contributing


Please read [Contributing](https://github.com/EudyContreras/Othello-FX-Framework/blob/master/CONTRIBUTING.md) for details on the code base code of conduct, and the process for submitting pull requests to **OthelloFX**



#### Authors


* **Eudy Contreras** 




#### Acknowledgments


* **Jose Font** for inspiration and education.
* Other teachers at Malmö Univertiy.




#### Contact


If any questions reach me at.
Econtreras12@live.com







### Disclaimer

**All background images including the logo were not made by me and I do not claim ownership of these images**. I would like to thank the awesome artists and creators of the images for making them public. If there is any problem with the use of these images please contact me so we can solve it. Once again. props to the artists.




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
