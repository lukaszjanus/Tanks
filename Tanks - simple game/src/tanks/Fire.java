package tanks;

/*
 * class for missiles
 * 
 * 
 */

import java.awt.*;

public class Fire {

	private double x, y;
	private int r;

	private double speed;

	private Color color1;

	public Fire(int x, int y) {
		this.x = x;
		this.y = y;
		r = 2; // dimension of bullet (radius of ball)

		speed = 2;
		color1 = Color.YELLOW;

	}

	public boolean updateG() {
		y -= speed;

		if (x < -r || x > tanks.WIDTH + r || y < -r || y > tanks.Height) {
			return true;
		}

		return false;
	}

	public boolean updateD() {
		y += speed;

		if (x < -r || x > tanks.WIDTH + r || y < -r || y > tanks.Height) {
			return true;
		}

		return false;
	}

	public boolean updateP() {
		x += speed;

		if (x < -r || x > tanks.WIDTH + r || y < -r || y > tanks.Height) {
			return true;
		}

		return false;
	}

	public boolean updateL() {

		x -= speed;

		if (x < -r || x > tanks.WIDTH + r || y < -r || y > tanks.Height) {
			return true;
		}

		return false;
	}

	public void draw(Graphics g) {
		g.setColor(color1);
		g.fillOval((int) (x - r), (int) (y - r), 2 * r, 2 * r);
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

}
