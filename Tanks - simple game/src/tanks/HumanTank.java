package tanks;

/*
 * class for human player
 * 
 * 
 */

import java.awt.*;
import java.util.Random;

public class HumanTank {

	double y, yRuch;
	double x, xRuch;
	int kier;

	boolean upAccel, downAccel, leftAccel, rightAccel; // for keys control

	boolean firing; // shots
	private long firingTimer; // time measuring
	private long firingDelay; // distance between shots

	double Przyspieszenie = 0.001;
	double speed = 0.1;

	Random random = new Random();
	int health;

	private boolean dead;
	private double r;
	private Color color1;

	public HumanTank() {
		upAccel = false;
		downAccel = false;
		leftAccel = false;
		rightAccel = false;
		kier = 0;

		health = 10;
		dead = false;
		r = 5;
		color1 = Color.DARK_GRAY;

		x = random.nextInt(tanks.WIDTH - 20) + 20;
		y = random.nextInt(tanks.Height - 20) + 20;

		xRuch = 0;
		yRuch = 0;
		// y=15; //rigidly localization
		// x=15;

		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 200; // 5 shots per second

	}

	public void draw(Graphics g) {
		g.setColor(color1);
		g.fillRect((int) x, (int) y, 7, 7); // rectangle

		if (kier == 1) // up
		{
			g.setColor(color1);
			g.fillRect((int) x + 3, (int) y - 3, 1, 7);

			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, 1, 7);
			g.setColor(Color.black);
			g.fillRect((int) x + 7, (int) y, 1, 7);
		} else if (kier == 2) // down
		{
			g.setColor(color1); // barrel :)
			g.fillRect((int) x + 3, (int) y + 3, 1, 7);

			g.setColor(Color.black); // tracks
			g.fillRect((int) x, (int) y, 1, 7);
			g.setColor(Color.black);
			g.fillRect((int) x + 7, (int) y, 1, 7);
		} else if (kier == 3) // left
		{
			g.setColor(color1);
			g.fillRect((int) x - 3, (int) y + 3, 7, 1);

			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, 7, 1);
			g.setColor(Color.black);
			g.fillRect((int) x, (int) y + 7, 7, 1);

		} else if (kier == 4) // right
		{
			g.setColor(color1);
			g.fillRect((int) x + 3, (int) y + 3, 7, 1);

			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, 7, 1);
			g.setColor(Color.black);
			g.fillRect((int) x, (int) y + 7, 7, 1);

		}
	}

	public int getX() {
		return ((int) x);
	}

	public int getY() {
		return ((int) y);
	}

	public int getR() {
		return (int) r;
	}

	public void setUpAccel(boolean input) {
		upAccel = input; // information - if key pressed
	}

	public void setDownAccel(boolean input) {
		downAccel = input;
	}

	public void setLeftAccel(boolean input) {
		leftAccel = input;
	}

	public void setRightAccel(boolean input) {
		rightAccel = input;
	}

	public void move() {
		if (upAccel) {
			yRuch -= speed;
			kier = 1;
		} else if (downAccel) {
			yRuch += speed;
			kier = 2;
		} else if (!upAccel && !downAccel) {
			yRuch *= Przyspieszenie; // acceleration
			// yVel *= 0.5;
		}

		y += yRuch;

		if (leftAccel) {
			xRuch -= speed;
			kier = 3;
		} else if (rightAccel) {
			xRuch += speed;
			kier = 4;
		} else if (!leftAccel && !rightAccel) {
			xRuch *= Przyspieszenie;
		}

		x += xRuch; // position actualisation

		if (yRuch >= 0.3)
			yRuch = 0.3;
		else if (yRuch <= -0.3)
			yRuch = -0.3; // move speed
		if (xRuch >= 0.3)
			xRuch = 0.3;
		else if (xRuch <= -0.3)
			xRuch = -0.3;
		/* ====================firing======== */

		if (y < 20)
			y = 20; // collision with up wall
		if (y > 500)
			y = 500;// collision with down wall - (520-20).
		if (x < 20)
			x = 20; // collision with up wall
		if (x > 700)
			x = 700;// collision with down wall - (720-20).

		// if (firing)fnShots((int)x,(int)y,kier);
		if (firing) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000; // how much time was from the last shot
			if (elapsed > firingDelay) // firingDelay=200
			{
				switch (kier) {
				case 1: {
					tanks.fireG.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 2: {
					tanks.fireD.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 3: {
					tanks.fireL.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 4: {
					tanks.fireP.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				}

			}
		}
	}

	public void setFire(boolean b) {
		firing = b;
	}

	public int getHealth() {
		return health;
	}

	public void hit() {
		health--;
		if (health < 2)
			color1 = Color.RED; // change colour after hit
		// speed=0;
		if (health <= 0) {
			dead = true;
		}
	}

	public boolean isDead() {
		return dead;
	}

}
