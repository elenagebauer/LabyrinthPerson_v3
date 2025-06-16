package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JPanel;

import model.Enemy;
import model.World;

/**
 * A graphical view of the world.
 */
public class GraphicView extends JPanel implements View {

	/** The view's width. */
	private final int WIDTH;
	/** The view's height. */
	private final int HEIGHT;

	private Dimension fieldDimension;
	private World world;
	private int powerup = 0;

	public GraphicView(int width, int height, Dimension fieldDimension) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.fieldDimension = fieldDimension;
		this.bg = new Rectangle(WIDTH, HEIGHT);
	}

	/** The background rectangle. */
	private final Rectangle bg;
	/** The rectangle we're moving. */
	private final Rectangle player = new Rectangle(1, 1);

	/**
	 * Creates a new instance.
	 */
	@Override
	public void paint(Graphics g) {
		// Paint background
		g.setColor(Color.BLACK);
		g.fillRect(bg.x, bg.y, bg.width, bg.height);
		// Paint world
		var fields = world.getFields();
		for (int x = 0; x<fields.length; x++) {
			for (int y = 0; y<fields[0].length;y++) {
				if (fields[x][y] == model.FieldType.WALL) {
					g.setColor(Color.BLUE);
					g.fillRect(
							x * fieldDimension.width,
							y * fieldDimension.height,
							fieldDimension.width,
							fieldDimension.height);
				} else if (fields[x][y] == model.FieldType.GOAL) {
					g.setColor(Color.RED);
					g.fillRect(
							x * fieldDimension.width,
							y * fieldDimension.height,
							fieldDimension.width,
							fieldDimension.height);
				} 
			}
		}
		// Paint Start
		g.setColor(Color.WHITE);
		g.fillRect(
				world.getStartX() * fieldDimension.width,
				world.getStartY() * fieldDimension.height,
				fieldDimension.width,
				fieldDimension.height);
		// Paint Player
		g.setColor(Color.YELLOW);
		g.fillOval(player.x, player.y, player.width, player.height);
		// Paint Enemy
		g.setColor(Color.MAGENTA); // z.B. für Gegner
		for (Enemy enemy : world.getEnemies()) {
    		g.fillOval(
        	enemy.getX() * fieldDimension.width,
        	enemy.getY() * fieldDimension.height,
        	fieldDimension.width,
        	fieldDimension.height
    		);
		}
							
	}
	

	@Override
	public void update(World world) {
		this.world = world;

		// Update players size and location
		player.setSize(fieldDimension);
		player.setLocation(
			(int) (world.getPlayerX() * fieldDimension.width),
			(int) (world.getPlayerY() * fieldDimension.height)
		);
		repaint();
	}

}
