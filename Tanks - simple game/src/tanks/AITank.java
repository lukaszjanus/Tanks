package tanks;

/*
 * class for IA tanks:
 * - moving and shooting by pseudo-random numbers
 * 
 */
import java.awt.*;
import java.util.Random;

public class AITank {

	double y, yRuch;
	double x, xRuch;

	Random random = new Random();

	int kier;

	boolean firing;
	private long firingTimer;
	private long firingDelay;

	// boolean upAccel, downAccel, leftAccel, rightAccel;
	double Przyspieszenie = 0.001;
	double speed = 0.1;

	int health;
	private int type; // for possibly development game - new types of tanks, not used
	private int rank; // for possibly development - tank level, not used

	private boolean dead;
	private double r;
	private Color color1;

	public AITank(int type, int rank) // rank - poziom trudnosci, type - do rozbudowy - rodzaje/klasy przeciwnikow
	{
		this.type = type;
		this.rank = rank;

		if (type == 1) {
			color1 = Color.blue;
			if (rank == 1) {
				// speed=2;
				r = 5; //
				health = 4;
			}
		}

		/* ==== random start position: ===== */
		x = random.nextInt(tanks.WIDTH - 20) + 20;
		y = random.nextInt(tanks.Height - 20) + 20;

		/* ==== start position by localisation: ===== */
		/*
		 * kier=3; y=100; yRuch=0; x=100; xRuch=0;
		 */
		dead = false;

		firing = false;
		firingTimer = System.nanoTime(); // time measuring
		firingDelay = 200; // 5 shots per second
	}

	public void draw(Graphics g) {
		g.setColor(color1);
		g.fillRect((int) x, (int) y, 7, 7);

		if (kier == 1) // up
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) x + 3, (int) y - 3, 1, 7);

			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, 1, 7);
			g.setColor(Color.black);
			g.fillRect((int) x + 7, (int) y, 1, 7);
		} else if (kier == 2) // down
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) x + 3, (int) y + 3, 1, 7);

			g.setColor(Color.black); // gasiennice
			g.fillRect((int) x, (int) y, 1, 7);
			g.setColor(Color.black);
			g.fillRect((int) x + 7, (int) y, 1, 7);
		} else if (kier == 3) // left
		{
			g.setColor(Color.DARK_GRAY);
			g.fillRect((int) x - 3, (int) y + 3, 7, 1);

			g.setColor(Color.black);
			g.fillRect((int) x, (int) y, 7, 1);
			g.setColor(Color.black);
			g.fillRect((int) x, (int) y + 7, 7, 1);
		} else if (kier == 4) // right
		{
			g.setColor(Color.DARK_GRAY);
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

	public int getHealth() {
		return health;
	}

	public int getR() {
		return (int) r;
	}

	public void move() {

		// Random random = new Random();
		int ruch = random.nextInt(30) + 1; // moving generator

		if (ruch == 1) {
			kier = random.nextInt(4) + 1;
		}

		/*
		 * try { Thread.sleep(50); //sleep z c++ } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */

		if (kier == 1) {
			yRuch -= speed;
		} else if (kier == 2) {
			yRuch += speed;
		} else if (kier != 1 && kier != 2) {
			yRuch *= Przyspieszenie; // acceleration
			// yVel *= 0.5;
		}

		y += yRuch;

		if (kier == 3) {
			xRuch -= speed;
		} else if (kier == 4) {
			xRuch += speed;
		} else if (kier != 3 && kier != 4) {
			xRuch *= Przyspieszenie;
		}

		x += xRuch;

		if (yRuch >= 0.3)
			yRuch = 0.3;
		else if (yRuch <= -0.3)
			yRuch = -0.3; // move speed
		if (xRuch >= 0.3)
			xRuch = 0.3;
		else if (xRuch <= -0.3)
			xRuch = -0.3;

		if (y < 20) {
			y = 20;
			kier = 2; // if met with wall, go back
		} // up wall
		if (y > 500) {
			y = 500;
			kier = 1;
		} // down wall - (500-80).
		if (x < 20) {
			x = 20;
			kier = 4;
		} // up wall
		if (x > 700) {
			x = 700;
			kier = 3;
		} // down wall - (500-80).

		int Ifire = random.nextInt(20) + 1;

		if (Ifire == 1) {
			long elapsed = (System.nanoTime() - firingTimer) / 1000000; // how much time were from last shot
			if (elapsed > firingDelay) {
				switch (kier) {
				case 1: {
					tanks.IfireG.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 2: {
					tanks.IfireD.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 3: {
					tanks.IfireL.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				case 4: {
					tanks.IfireP.add(new Fire((int) x, (int) y));
					firingTimer = System.nanoTime();
					break;
				}
				}

			}
		}
	}

	public void hit() {
		health--;
		if (health < 2)
			color1 = Color.LIGHT_GRAY; // change colour after hit
		// speed=0;
		if (health <= 0) {
			dead = true;
		}
	}

	public boolean isDead() {
		return dead;
	}

}
