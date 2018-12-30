package tanks;

/*
 * Lukasz Janus
 * 
 * 28-10-2017
 * 
 * Applied Informatics - fourth semester: 
 * 'Crossplatform Programming in Java'
 * 
 * 
 * Simple 2D game inspired 'Battle City' from Pegasus.
 * Rules:
 * - player has 10 lives
 * - fifth opponents has 4 lives 
 * - grey obstacle you can shot
 * - green obstacle - you can hide here (but enemy can still hit you)
 * The main objective - survive and eliminate all enemies.
 * 
 * Controls keys:
 * - move: left, right, up, down
 * - shot: space
 * - start game: enter
 * 
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

public class tanks extends Applet implements Runnable, KeyListener {

	final static int WIDTH = 720;
	final static int Height = 520;

	// global variable for human (control game)

	Thread thread;
	HumanTank t1;
	// AITank t2;

	ArrayList<AITank> t2;

	public static ArrayList<Barricades> tree;
	public static ArrayList<Barricades> beton;
	// public static ArrayList <Barricades> water; //not created

	public static ArrayList<Fire> fireG;
	public static ArrayList<Fire> fireD;
	public static ArrayList<Fire> fireL;
	public static ArrayList<Fire> fireP;

	public static ArrayList<Fire> IfireG;
	public static ArrayList<Fire> IfireD;
	public static ArrayList<Fire> IfireL;
	public static ArrayList<Fire> IfireP;

	int kier;

	boolean gameStarted;
	boolean PlayAgain;
	boolean running;

	Graphics gfx;
	Image img;
	Random random = new Random();

	public void init() {
		this.resize(WIDTH, Height);
		gameStarted = false;
		PlayAgain = true;
		running = true;

		this.addKeyListener(this); // keys
		t1 = new HumanTank();
		// t2 = new AITank(); //not used - for second human player

		/* list of enemies tanks */
		t2 = new ArrayList<AITank>();
		for (int i = 0; i < 5; i++) {
			t2.add(new AITank(1, 1));
		}

		// Random random = new Random(); //generating numbers
		tree = new ArrayList<Barricades>();
		for (int i = 15; i < WIDTH - 20; i += 10) {
			for (int j = 15; j < Height - 20; j += 10) {
				int n = random.nextInt(100);
				if (n == 1) {
					tree.add(new Barricades(i, j));
				}
			}
		}

		beton = new ArrayList<Barricades>();

		for (int i = 20; i < WIDTH - 25; i += 20) {
			for (int j = 20; j < Height - 25; j += 20) {
				int n = random.nextInt(10);
				int a = random.nextInt(15) + 5;
				int b = random.nextInt(15) + 5;
				int r = (a + b / 4) - 1;
				if (n == 1) {
					beton.add(new Barricades(i, j, a, b, r));
				}
			}
		}
		// water = new ArrayList<Barricades>();

		fireG = new ArrayList<Fire>();
		fireD = new ArrayList<Fire>();
		fireL = new ArrayList<Fire>();
		fireP = new ArrayList<Fire>();

		IfireG = new ArrayList<Fire>();
		IfireD = new ArrayList<Fire>();
		IfireL = new ArrayList<Fire>();
		IfireP = new ArrayList<Fire>();

		img = createImage(WIDTH, Height);
		gfx = img.getGraphics();

		thread = new Thread(this); // thread create
		thread.start();

	}

	public void paint(Graphics g) {
		gfx.setColor(Color.blue); // window dimensions
		gfx.fillRect(0, 0, WIDTH, Height);
		gfx.setColor(Color.gray); // background of the window
		gfx.fillRect(10, 10, WIDTH - 20, Height - 20);

		// g.drawString(" FPS: "+ averageFPS, 10,10);// check bullet
		gfx.setColor(Color.BLACK);
		gfx.drawString(" Human health: " + t1.health, 10, 20);

		t1.draw(gfx);
		// t2.draw(gfx);
		int line = 20;
		for (int i = 0; i < t2.size(); i++) {
			AITank ai = t2.get(i);
			t2.get(i).draw(gfx);
			gfx.drawString(" Enemy " + i + ":" + ai.health, line += 100, 20);
		}

		/* ====obstacles========= */
		for (int i = 0; i < tree.size(); i++) {
			tree.get(i).draw(gfx);
		}
		for (int i = 0; i < beton.size(); i++) {
			beton.get(i).draw2(gfx);
		}

		/* ====Human shots========= */

		for (int i = 0; i < fireG.size(); i++) {
			fireG.get(i).draw(gfx);
		}

		for (int i = 0; i < fireD.size(); i++) {
			fireD.get(i).draw(gfx);
		}

		for (int i = 0; i < fireL.size(); i++) {
			fireL.get(i).draw(gfx);
		}

		for (int i = 0; i < fireP.size(); i++) {
			fireP.get(i).draw(gfx);
		}
		/* ===== IA shots ========= */

		for (int i = 0; i < IfireG.size(); i++) {
			IfireG.get(i).draw(gfx);
		}

		for (int i = 0; i < IfireD.size(); i++) {
			IfireD.get(i).draw(gfx);
		}

		for (int i = 0; i < IfireL.size(); i++) {
			IfireL.get(i).draw(gfx);
		}

		for (int i = 0; i < IfireP.size(); i++) {
			IfireP.get(i).draw(gfx);
		}

		/* == First String (on start) =-======== */
		if (!gameStarted) {
			// if (!PlayAgain)
			{
				// gameStarted=false;
				// EndDraw(g);
				// t1.health=2;
			}
			gfx.setColor(Color.white);
			gfx.drawString("Tanks", 300, 110);
			gfx.drawString("Press Enter to begin...", 300, 130);
			// PlayAgain=false;

		}
		/* finish the game; */

		if (t2.size() == 0 || t1.health < 1) {
			running = false;
			EndDraw(g);
		}

		g.drawImage(img, 0, 0, this);

	}

	public void update(Graphics g) {
		paint(g);

		for (int i = 0; i < tree.size(); i++) {
			boolean removeTree = tree.get(i).updateTree();
			if (removeTree) {
				tree.remove(i);
				i--;
			}
		}
		/* collisions with grey obstacles */
		for (int i = 0; i < beton.size(); i++) {
			Barricades be = beton.get(i);
			double bx = be.getX();
			double by = be.getY();
			double br = be.getR(); //

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR() - 1; // radius - for ease of use, even though it's a rectangle
			// the distance of the missile from the tank
			double dx = bx - t1x;
			double dy = by - t1y;
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance < t1r + br) // bouncing from the gray obstacle
			{
				t1.Przyspieszenie = 0;
				t1.x -= -1;
				t1.y -= -1;
				t1.xRuch *= -0.1;
				t1.yRuch *= -0.1;
				t1.kier = 0;
				t1.Przyspieszenie = 0.001;
			}

		}
		/* ia - collision with grey obstacle */
		for (int i = 0; i < beton.size(); i++) {
			Barricades be = beton.get(i);
			double bx = be.getX();
			double by = be.getY();
			double br = be.getR(); //
			for (int j = 0; j < t2.size(); j++) {
				AITank ai = t2.get(j);
				double t2x = ai.getX();
				double t2y = ai.getY();
				double t2r = ai.getR() - 1;

				double dx = bx - t2x;
				double dy = by - t2y;
				double distance = Math.sqrt((dx * dx) + (dy * dy));
				if (distance < t2r + br) // AI bouncing from the gray obstacle
				{
					ai.Przyspieszenie = 0;
					ai.x -= 1;
					ai.y -= 1;
					ai.xRuch *= -0.1;
					ai.yRuch *= -0.1;
					ai.kier = 0;// random.nextInt(4)+1;
					ai.Przyspieszenie = 0.001;
					break;
				}
			}
		}

		/* bouncing with enemies */

		for (int i = 0; i < t2.size(); i++) {
			AITank ai = t2.get(i);
			double t2x = ai.getX();
			double t2y = ai.getY();
			double t2r = ai.getR(); //

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR();
			double dx = t2x - t1x;
			double dy = t2y - t1y;
			double distance = Math.sqrt((dx * dx) + (dy * dy));
			if (distance < t1r + t2r) // bouncing
			{
				// t1.hit(); //version with loss lives
				// ai.hit();
				// if(distance<15)
				{
					t1.Przyspieszenie = 0;
					t1.x -= -1;
					t1.y -= -1;
					t1.xRuch *= -1;
					t1.yRuch *= -1;
					t1.kier = 0;
					t1.Przyspieszenie = 0.001;

					ai.Przyspieszenie = 0;
					ai.x -= 1;
					ai.y -= 1;
					ai.xRuch *= -1;
					ai.yRuch *= -1;
					ai.kier = random.nextInt(4) + 1;
					ai.Przyspieszenie = 0.001;
					break;
				}

			}
		}

		/* collisions */
		/* ===Check before drawing, if Human missile is still active ===== */
		for (int i = 0; i < fireG.size(); i++) { // shot and collision with IA
			Fire f = fireG.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			// shot in gray obstacle
			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				double dx = fx - bx;
				double dy = fy - by;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) // after one shot obstacle is removed
				{
					beton.remove(j);
					fireG.remove(i);
					i--;
					break;
				}
			}

			for (int j = 0; j < t2.size(); j++) {
				AITank ai = t2.get(j);
				double t2x = ai.getX();
				double t2y = ai.getY();
				double t2r = ai.getR();
				double dx = fx - t2x;
				double dy = fy - t2y;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + t2r) // remove IA after shot
				{
					ai.hit();
					fireG.remove(i);
					i--;
					break;
				}

			}

			/* check - is missile still on fighting area */

			boolean removeG = fireG.get(i).updateG();
			if (removeG) // true, if missile is outside window
			{
				fireG.remove(i);
				i--;
			}
		}
		// shot to down
		for (int i = 0; i < fireD.size(); i++) {

			Fire f = fireD.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				double dx = fx - bx;
				double dy = fy - by;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					fireD.remove(i);
					i--;
					break;
				}
			}

			for (int j = 0; j < t2.size(); j++) {
				AITank ai = t2.get(j);
				double t2x = ai.getX();
				double t2y = ai.getY();
				double t2r = ai.getR();
				double dx = fx - t2x;
				double dy = fy - t2y;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + t2r) {
					ai.hit();
					fireD.remove(i);
					i--;
					break;
				}

			}

			boolean removeD = fireD.get(i).updateD();
			if (removeD) {
				fireD.remove(i);
				i--;
			}
		}
		for (int i = 0; i < fireL.size(); i++) {
			Fire f = fireL.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				double dx = fx - bx;
				double dy = fy - by;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					fireL.remove(i);
					i--;
					break;
				}
			}

			for (int j = 0; j < t2.size(); j++) {
				AITank ai = t2.get(j);
				double t2x = ai.getX();
				double t2y = ai.getY();
				double t2r = ai.getR();
				double dx = fx - t2x;
				double dy = fy - t2y;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + t2r) {
					ai.hit();
					fireL.remove(i);
					i--;
					break;
				}

			}

			boolean removeL = fireL.get(i).updateL();
			if (removeL) {
				fireL.remove(i);
				i--;
			}
		}
		for (int i = 0; i < fireP.size(); i++) {
			Fire f = fireP.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				double dx = fx - bx;
				double dy = fy - by;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					fireP.remove(i);
					i--;
					break;
				}
			}

			for (int j = 0; j < t2.size(); j++) {
				AITank ai = t2.get(j);
				double t2x = ai.getX();
				double t2y = ai.getY();
				double t2r = ai.getR();
				double dx = fx - t2x;
				double dy = fy - t2y;
				double distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + t2r) {
					ai.hit();
					fireP.remove(i);
					i--;
					break;
				}
			}

			boolean removeP = fireP.get(i).updateP();
			if (removeP) {
				fireP.remove(i);
				i--;
			}
		}

		/* IA shots */
		for (int i = 0; i < IfireG.size(); i++) {
			Fire f = IfireG.get(i); // shoting to human player
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR();

			double dx = fx - t1x;
			double dy = fy - t1y;
			double distance = Math.sqrt(dx * dx + dy * dy);

			if (distance < fr + t1r) {
				t1.hit();
				IfireG.remove(i);
				i--;
				break;
			}
			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				dx = fx - bx;
				dy = fy - by;
				distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					IfireG.remove(i);
					i--;
					break;
				}
			}

			boolean removeG = IfireG.get(i).updateG();
			if (removeG) {
				IfireG.remove(i);
				i--;
			}
		}

		for (int i = 0; i < IfireD.size(); i++) {
			Fire f = IfireD.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR();

			double dx = fx - t1x;
			double dy = fy - t1y;
			double distance = Math.sqrt(dx * dx + dy * dy);

			if (distance < fr + t1r) {
				t1.hit();
				IfireD.remove(i);
				i--;
				break;
			}

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				dx = fx - bx;
				dy = fy - by;
				distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					IfireD.remove(i);
					i--;
					break;
				}
			}
			boolean removeD = IfireD.get(i).updateD();
			if (removeD) {
				IfireD.remove(i);
				i--;
			}
		}
		for (int i = 0; i < IfireL.size(); i++) {
			Fire f = IfireL.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR();

			double dx = fx - t1x;
			double dy = fy - t1y;
			double distance = Math.sqrt(dx * dx + dy * dy);

			if (distance < fr + t1r) {
				t1.hit();
				IfireL.remove(i);
				i--;
				break;
			}

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				dx = fx - bx;
				dy = fy - by;
				distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					IfireL.remove(i);
					i--;
					break;
				}
			}

			boolean removeL = IfireL.get(i).updateL();
			if (removeL) {
				IfireL.remove(i);
				i--;
			}
		}
		for (int i = 0; i < IfireP.size(); i++) {
			Fire f = IfireP.get(i);
			double fx = f.getX();
			double fy = f.getY();
			double fr = f.getR();

			double t1x = t1.getX();
			double t1y = t1.getY();
			double t1r = t1.getR();

			double dx = fx - t1x;
			double dy = fy - t1y;
			double distance = Math.sqrt(dx * dx + dy * dy);

			if (distance < fr + t1r) {
				t1.hit();
				IfireP.remove(i);
				i--;
				break;
			}

			for (int j = 0; j < beton.size(); j++) {
				Barricades be = beton.get(j);
				double bx = be.getX();
				double by = be.getY();
				double br = be.getR(); //

				dx = fx - bx;
				dy = fy - by;
				distance = Math.sqrt(dx * dx + dy * dy);

				if (distance < fr + br) {
					beton.remove(j);
					IfireP.remove(i);
					i--;
					break;
				}
			}

			boolean removeP = IfireP.get(i).updateP();
			if (removeP) {
				IfireP.remove(i);
				i--;
			}
		}

		/* checking death IA */
		for (int i = 0; i < t2.size(); i++) {
			if (t2.get(i).isDead()) {
				t2.remove(i);
				i--;
			}
		}
	}

	/* keys */

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			t1.setUpAccel(true);
			kier = 1;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			t1.setDownAccel(true);
			kier = 2;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			t1.setLeftAccel(true);
			kier = 3;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			t1.setRightAccel(true);
			kier = 4;
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			gameStarted = true;
		} else if (e.getKeyCode() == KeyEvent.VK_A) {
			PlayAgain = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			t1.setFire(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			t1.setUpAccel(false);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			t1.setDownAccel(false);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			t1.setLeftAccel(false);
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			t1.setRightAccel(false);
		}

		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			t1.setFire(false);
		}

	}

	public void keyTyped(KeyEvent e) {

	}

	/* the main window */

	public void EndDraw(Graphics g) {
		if (t1.health < 1) {
			gfx.setColor(Color.blue);
			gfx.fillRect(0, 0, WIDTH, Height);
			gfx.setColor(Color.gray);
			gfx.fillRect(10, 10, WIDTH - 20, Height - 20);

			gfx.setColor(Color.BLACK);
			gfx.drawString("Game over!!", 100, 100);
		} else {
			gfx.setColor(Color.blue);
			gfx.fillRect(0, 0, WIDTH, Height);
			gfx.setColor(Color.gray);
			gfx.fillRect(10, 10, WIDTH - 20, Height - 20);

			gfx.setColor(Color.BLACK);
			gfx.drawString("You win!!", 100, 100);

		}

	}

	/* the main loop */

	public void run() {
		do // final loop
		{
			if (gameStarted == true) {
				t1.move();
				// t2.move();
				for (int i = 0; i < t2.size(); i++) {
					t2.get(i).move(); // moving players
				}
			}
			repaint(); // refresh window every 10 miliseconds - draw only objects, which were changed
			try {
				Thread.sleep(10); // sleep from C++
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (running == true);
	}
}
