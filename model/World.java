package model;

import java.util.ArrayList;
import java.lang.reflect.Field;
import view.View;

import javax.swing.*;
import java.util.Random;

/**
 * The world is our model. It saves the bare minimum of information required to
 * accurately reflect the state of the game. Note how this does not know
 * anything about graphics.
 */
public class World {

	/** The world's width. */
	private final int width;
	/** The world's height. */
	private final int height;
	/** The player's x position in the world. */
	private int playerX;
	/** The player's y position in the world. */
	private int playerY;

	private int startX;
	private int startY;
	private final FieldType[][] fields;
	/** Set of views registered to be notified of world updates. */
	private final ArrayList<View> views = new ArrayList<>();
	private final ArrayList<Enemy> enemies = new ArrayList<>();


	/**
	 * Creates a new world with the given size.t
	 */
	public World(int width, int height) {
		// Normally, we would check the arguments for proper values
		this.width = width;
		this.height = height;
		this.fields = new FieldType[width][height];
		// Build the field
		for (int x = 1; x<width-1; x++) { // geändert weil sonst empty unfd wall ? probleme beim setzen von start und goal
			for (int y = 1; y<height-1; y++) {
				fields[x][y] = FieldType.EMPTY;

			}
		}
		// Build the walls
		for (int x = 0; x<width; x++) {
			fields[x][0] = FieldType.WALL;
			fields[x][height-1] = FieldType.WALL;
		}
		for (int y = 0; y<height; y++) {
			fields[0][y] = FieldType.WALL;
			fields[width-1][y] = FieldType.WALL;
		}

		// Your labyrinth walls here
		walls_inside(10,5);

		randomStartGoal();
		playerX= startX;
		playerY= startY;
		//Place player in the world
		fields[playerX][playerY]= FieldType.PLAYER;
		setEnemies(5);

	}

	///////////////////////////////////////////////////////////////////////////
	// Getters and Setters

	/**
	 * Returns the width of the world.
	 * 
	 * @return the width of the world.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the world.
	 * 
	 * @return the height of the world.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the player's x position.
	 * 
	 * @return the player's x position.
	 */
	public int getPlayerX() {
		return playerX;
	}

	/**
	 * Sets the player's x position.
	 * 
	 * @param playerX the player's x position.
	 */
	public void setPlayerX(int playerX) {
		playerX = Math.max(0, playerX);
		playerX = Math.min(getWidth() - 1, playerX);
		this.playerX = playerX;
		
		//updateViews();
	}

	/**
	 * Returns the player's y position.
	 * 
	 * @return the player's y position.
	 */


	public int getPlayerY() {
		return playerY;
	}
	/**
	 * Returns the field.
	 *
	 * @return the field.
	 */

	public FieldType[][] getFields(){
		return fields;
	}

	/**
	 * Sets the player's y position.
	 * 
	 * @param playerY the player's y position.
	 */
	public void setPlayerY(int playerY) {
		playerY = Math.max(0, playerY);
		playerY = Math.min(getHeight() - 1, playerY);
		this.playerY = playerY;
		
		updateViews();
	}
	/**
	 * Returns the player's x position at the start.
	 *
	 * @return the player's x position at the start.
	 */
	public int getStartX() {
		return startX;
	}
	/**
	 * Returns the player's y position at the start.
	 *
	 * @return the player's y position at the start.
	 */
	public int getStartY() {
		return startY;
	}
	/**
	 * Sets the start x position.
	 *
	 * @return the start x position.
	 */
	public void setStartX(int startX) {
		this.startX = startX;
	}
	/**
	 * Sets the start y position.
	 *
	 * @return the start y position.
	 */
	public void setStartY(int startY) {
		this.startY = startY;
	}
	
	public ArrayList<Enemy> getEnemies() {
    return enemies;
}

	public void randomStartGoal() {
		Random rand = new Random();

		int startX_1, startY_1, goalX, goalY;

		do {
			startX_1 = rand.nextInt(width);
			startY_1 = rand.nextInt(height);
		} while (fields[startX_1][startY_1] != FieldType.EMPTY);

		do {
			goalX = rand.nextInt(width);
			goalY = rand.nextInt(height);
		} while (fields[goalX][goalY] != FieldType.EMPTY  && Math.abs(goalX-startX_1)+Math.abs(goalY-startY_1)<50); //warum 50);



		//setStartX(startX_1);
		//setStartY(startY_1);
		startX = startX_1;
		startY = startY_1;
		fields[startX][startY] = FieldType.START;
		fields[goalX][goalY] = FieldType.GOAL;
		updateViews();
	}

	public void walls_inside(int walls, int wall_length) {
		Random rand_walls = new Random();
		int walls_inside = 0;
		int odd=0;
		while (walls_inside < walls) {
			odd +=1;
			walls_inside++;
			int x = rand_walls.nextInt(width-2)+1;
			int y = rand_walls.nextInt(height-2)+1;

			for (int i = 0; i < wall_length; i++) {
				if (odd % 2 == 0 ) {
					if (fields[x][y + i] == FieldType.WALL && y-1>0) {
						fields[x][y - i] = FieldType.WALL;
					} else if (y + i < height){
						fields[x][y + i] = FieldType.WALL;
					}
				} else {
						if (x + i < width && fields[x+i][y] == FieldType.WALL && x-1 > 0) {
							fields[x-i][y] = FieldType.WALL;
						} else if (x + i < width){
							fields[x+i][y] = FieldType.WALL;
						}
					}
				}
			}
		}

	public void setEnemies(int count_enemies){
		enemies.clear();
		int enemiesX;
		int enemiesY;
		Random rand_enemies = new Random();
		for (int i = 1; i < count_enemies; i++) {
			do { 
				enemiesX = rand_enemies.nextInt(width - 2)+1;
				enemiesY = rand_enemies.nextInt(height -2) +1 ;
			} while (fields[enemiesX][enemiesY] != FieldType.EMPTY || (enemiesX == playerX && enemiesY == playerY));
			enemies.add(new Enemy(enemiesX, enemiesY));
			fields[enemiesX][enemiesY]=FieldType.ENEMY;
		}


	}


	///////////////////////////////////////////////////////////////////////////
	// Player Management
	
	/**
	 * Moves the player along the given direction.
	 * 
	 * @param direction where to move.
	 */
	public void movePlayer(Direction direction) {	
		// The direction tells us exactly how much we need to move along
		// every direction
		fields[getPlayerX()][getPlayerY()] = FieldType.EMPTY;
		int newPositionX = getPlayerX()+direction.deltaX;
		int newPositionY = getPlayerY() + direction.deltaY;

		if (0 <= newPositionX && newPositionX < width && 0 <= newPositionY && newPositionY < height && fields[newPositionX][newPositionY] != FieldType.WALL) {
			if (fields[newPositionX][newPositionY] == FieldType.GOAL) {
				//System.exit(0);
				int choice = JOptionPane.showOptionDialog(
						null,
						" Du hast das Spiel gewonnen! \n Was möchtest du nun tun?",
						"Spiel gewonnen",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new String[] { "Neustart", "Beenden" },
						"Neustart"
				);
				if (choice == JOptionPane.YES_OPTION) {
					restart();
				} else if (choice == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
			fields[newPositionX][newPositionY] = FieldType.PLAYER;
			setPlayerX(newPositionX);
			setPlayerY(newPositionY);
			updateViews();
		}
		moveEnemies();
	}


	public void restart() {

		for (int x = 1; x < width - 1; x++) {
			for (int y = 1; y < height - 1; y++) {
				fields[x][y] = FieldType.EMPTY;
			}
		}
		walls_inside(10,8);

		randomStartGoal();
		playerX=startX;
		playerY=startY;
		fields[playerX][playerY]= FieldType.PLAYER;

		updateViews();
	}



	public void moveEnemies() {
		Random rand_enemies = new Random();
		for (Enemy enemy : enemies) {
			int dx, dy;
			int enemyX, enemyY;
			int direction_random = rand_enemies.nextInt(4);
			if (direction_random == 0 ){
				dx = 1;
				dy = 0;
			} else if (direction_random == 1) {
				dx = -1;
				dy = 0;
			} else if (direction_random == 2){
				dx = 0;
				dy = 1;
			} else {
				dx = 0;
				dy = -1;
			}
			enemyX = enemy.getX() + dx;
			enemyY = enemy.getY() + dy;

			if (enemyX >0 && enemyX < (width-1) && enemyY >0 && enemyY < (height-1) && fields[enemyX][enemyY] != FieldType.WALL && fields[enemyX][enemyY] != FieldType.ENEMY){
				enemy.setEnemyX(enemyX);
				enemy.setEnemyY(enemyY);
			}
			if (enemy.getX() == playerX && enemy.getY() == playerY) {
				int choice = JOptionPane.showOptionDialog(
						null,
						" Du hast das Spiel verloren! \n Was möchtest du nun tun?",
						"Spiel verloren",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null,
						new String[] { "Neustart", "Beenden" },
						"Neustart"
				);
				if (choice == JOptionPane.YES_OPTION) {
					restart();
				} else if (choice == JOptionPane.NO_OPTION) {
					System.exit(0);
				}
			}
			fields[enemyX][enemyY] = FieldType.ENEMY;
		}
		updateViews();


	}

	///////////////////////////////////////////////////////////////////////////
	// View Management

	/**
	 * Adds the given view of the world and updates it once. Once registered through
	 * this method, the view will receive updates whenever the world changes.
	 * 
	 * @param view the view to be registered.
	 */
	public void registerView(View view) {
		views.add(view);
		view.update(this);
	}

	/**
	 * Updates all views by calling their {@link View#update(World)} methods.
	 */
	private void updateViews() {
		for (int i = 0; i < views.size(); i++) {
			views.get(i).update(this);
		}
	}

}
