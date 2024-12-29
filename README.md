# Snake Game in Java

Welcome to the Snake Game project! This classic arcade game has been implemented in Java, featuring engaging gameplay and simple graphics. Below, you'll find details on how to set up, run, and modify the game.

Features

Classic Gameplay: Control the snake to eat food and grow, avoiding collisions with walls and itself.

Simple Graphics: Basic 2D graphics for a nostalgic arcade feel.

Customizable: Modify game parameters like speed, grid size, and snake color.

⭐ Requirements

Java Development Kit (JDK) 8 or higher

A text editor or Integrated Development Environment (IDE) like IntelliJ IDEA or Eclipse

Installation

Clone this repository or download the source code as a ZIP file:

git clone https://github.com/Mendes27/SnakeGame.git

Open the project in your preferred IDE.

Build the project to ensure all dependencies are correctly set up.

Run the Main class to start the game.

How to Play

Use the arrow keys to control the direction of the snake:

Up Arrow: Move up

Down Arrow: Move down

Left Arrow: Move left

Right Arrow: Move right

Your objective is to eat the food that appears on the screen. Each piece of food increases your score and the snake's length.

Avoid running into the walls or the snake's own body. If you do, the game ends.

Customization

You can customize the game by modifying the following variables in the source code:

Grid Size: Adjust the size of the game board.

Snake Speed: Change the delay between moves to make the game faster or slower.

Colors: Update the color scheme for the snake, food, and background.

For example, in the GameSettings class:
public static final int GRID_SIZE = 20; // Size of each grid cell
public static final int GAME_SPEED = 100; // Delay in milliseconds
public static final Color SNAKE_COLOR = Color.GREEN; // Snake color
public static final Color FOOD_COLOR = Color.RED; // Food color

Enjoy playing and modifying the Snake Game! If you have any questions or feedback, feel free to reach out via the Issues section of the repository.
