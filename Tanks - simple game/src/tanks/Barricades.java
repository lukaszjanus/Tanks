package tanks;

/*
 * class for obstacles:
 * - green (tank can be run under it)
 * - grey (can be destroyed after hit from tank)
 * 
 */
import java.awt.Color;
import java.awt.Graphics;

public class Barricades {

	private int x, y;
	private int a, b;
	private int r;

	private Color tree;
	private Color beton;

	public Barricades(int x, int y) {
		this.x = x;
		this.y = y;
		r = 4; // dimension of green obstacles

		tree = Color.green;
	}

	public Barricades(int x, int y, int a, int b, int r) {
		this.x = x;
		this.y = y;
		this.a = a;
		this.b = b;
		this.r = r; // dimension of grey obstacles

		beton = Color.LIGHT_GRAY;
	}

	public boolean updateTree() {
		if (x < -r || x > tanks.WIDTH + r || y < -r || y > tanks.Height) {
			return true;
		}

		return false;
	}

	public void draw(Graphics g) {
		g.setColor(tree);
		g.fillOval((int) (x - r), (int) (y - r), 3 * r, 3 * r);
	}

	public void draw2(Graphics g) {
		g.setColor(beton);
		g.fillRect(x, y, a, b);
		g.setColor(Color.BLACK);
		g.drawLine(x, y, x + a, y + b);
		g.setColor(Color.BLACK);
		g.drawLine(x, y + b, x + a, y);
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
