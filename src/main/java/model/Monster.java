package model;

import javafx.scene.layout.Pane;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Monster extends GravityObject {

	private final double speed;
	private boolean facingRight;
	private Bubble prisonBubble;
	private boolean caughtByBubble;

	/**
	 * The monster that is trying to catch the character.
	 * @param layer The level in where the game is played.
	 * @param imageLoc The path of the image of the monster.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param r The rotation.
	 * @param dx The dx of x.
	 * @param dy The dy of y.
	 * @param dr The dr of r.
	 * @param speed The speed at which the monster is travelling.
	 * @param facingRight Whether the monster is facing to the right or not.
	 */
	public Monster(Pane layer, String imageLoc, double x, double y, double r,
			double dx, double dy, double dr, double speed, boolean facingRight) {
		super(layer, imageLoc, x, y, r, dx, dy, dr);

		this.speed = speed;
		this.facingRight = facingRight;
		this.caughtByBubble = false;
	}

	/**
	 * The movement of the monster.
	 */
	public void move() {
		if (!caughtByBubble) {
			if (facingRight) {
				setDx(speed);
			} else {
				setDx(-speed);
			}

			setDy(-calculateGravity());
		} else {
			setDx(0);
			setDy(0);
			setX(prisonBubble.getX());
			setY(prisonBubble.getY());
		}

		super.move();
	}

	/**
	 * Switching the direction that the monster is facing.
	 */
	public void switchDirection() {
		facingRight = !facingRight;
	}

	/**
	 * Checks to see if the bubble has collided with the monster.
	 * @param bubble the bubble that is shot from the character.
	 */
	public void checkCollision(final Bubble bubble) {
		double bubbleX = bubble.getX();
		double bubbleY = bubble.getY();
		double bubbleX2 = bubbleX + getWidth();
		double bubbleY2 = bubbleY + getHeight();
		if ((bubble.getAbleToCatch() && !caughtByBubble 
				&& (bubbleX >= getX() && bubbleX <= getX() + getWidth())
				|| (bubbleX2 >= getX() && bubbleX2 <= getX() + getWidth()))
				&& ((bubbleY >= getY() && bubbleY <= getY() + getHeight())
				|| bubbleY2 >= getY() && bubbleY2 <= getY() + getHeight())) {
			prisonBubble = bubble;
			caughtByBubble = true;
		}
		bubble.setAbleToCatch(false);
	}


}
