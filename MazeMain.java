package maze;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.awt.*;
import java.util.Scanner;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class MazeMain extends JPanel implements KeyListener, MouseListener {
	JFrame frame;

	private String[][] maze;
	private ArrayList<String[]> mazeRow = new ArrayList<String[]>(); // to split the maze
	private String[] row = null;

	private int frameWidth = 1350;
	private int frameHeight = 700;

	private int characterRow = 0, characterCol = 2;
	private int startRow = 0, startCol = 2; // starting position
	private int endRow = 25, endCol = 48; // ending position

	private int moveCount = 0; // moveCount;
	private int rot = 1; // rotation value starts down
	private String direction = "Down";

	private int scale2 = 15; //scale of entire maze
	private int circleMea2 = 10; //size of circle
	private int offset2 = 2; //distance from borders

	private int scale3 = 8; // scale of entire maze
	private int circleMea3 = 4; // size of circle
	private int offset3 = 2; // distance from borders

	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private ArrayList<Wall> front = new ArrayList<Wall>();
	private ArrayList<Wall> ceilings = new ArrayList<Wall>();
	private ArrayList<Wall> floors = new ArrayList<Wall>();
	private ArrayList<BreadCrumb> crumbs = new ArrayList<BreadCrumb>();

	private int rectWidth = 140;
	private int distance = 60;
	private int numberCount = 4;
	private boolean flashlight = false;
	private int breadCrumbs = 50;

	private String[][] mazeCrumbs;

	Color darkGray = Color.DARK_GRAY;
	Color mediumGray = Color.GRAY;
	Color lightGray = Color.LIGHT_GRAY;
	Color black = Color.BLACK;
	Color white = Color.WHITE;

	private Font small = new Font ("Times New Roman", Font.PLAIN, 12);
	private Font medium = new Font("Times New Roman", Font.PLAIN, 24);
	private Font large = new Font("Times New Roman", Font.PLAIN, 50);
	private int textRow = 430;

	public MazeMain() {
		setBoard();
		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
		frame.addKeyListener(this);
		// this.addMouseListener(this);
	}

	public void paintComponent(Graphics graphic) {
		super.paintComponent(graphic);
		Graphics2D g = (Graphics2D) graphic;
		g.setColor(lightGray); // this will set the background color
		g.fillRect(0, 0, frameWidth, frameHeight); // dimensions of the board
		g.setColor(darkGray); // for what we fill

		/* 3D Version 

		for (Wall w : ceilings) {
			g.setColor(lightGray);
			g.fill(w.getPolygon());
			g.setColor(black);
			g.drawPolygon(w.getPolygon());
		}

		for (Wall w : floors) {
			g.setColor(lightGray);
			g.fill(w.getPolygon());
			g.setColor(black);
			g.drawPolygon(w.getPolygon());
		}

		for (Wall w : walls) {
			g.setColor(mediumGray);
			g.fill(w.getPolygon());
			g.setColor(black);
			g.drawPolygon(w.getPolygon());
		}

		for (BreadCrumb b : crumbs) {
			g.setColor(Color.YELLOW);
			g.fillOval(b.getY() - b.getSize(), b.getX() - b.getSize(), b.getSize() / 2, b.getSize() / 2);
		}
		
		for (int i = front.size() - 1; i >= 0; i--) {
			Wall w = front.get(i);
			g.setColor(mediumGray);
			g.fill(w.getPolygon());
			g.setColor(black);
			g.drawPolygon(w.getPolygon());
		}


		g.setColor(black);
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j].equals("*")) {
					g.fillRect(j * scale3 - offset3, i * scale3 - offset3, scale3, scale3);
				}
			}
		}
		g.setColor(Color.RED);
		g.fillOval(characterCol * scale3, characterRow * scale3, circleMea3, circleMea3); // setting the circle
		g.setColor(Color.BLACK);
		g.setFont(small);
		g.drawString("Move Count: " + moveCount, 10, 225);
		g.setFont(large);
		if (characterRow == endRow && characterCol == endCol) {
			g.drawString("Great Job!", 575, frameHeight / 2);
		}
		
		*/
		
		/* 2D Version 
		
		g.setFont(medium);

		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze[0].length; j++) {
				if (maze[i][j].equals("*")) {
					g.fillRect(j * scale2 - offset2, i * scale2 - offset2, scale2, scale2);
				}
			}
		}
		g.setColor(Color.RED);
		g.fillOval(characterCol * scale2, characterRow * scale2, circleMea2, circleMea2); //setting the circle
		g.setColor(Color.BLACK);
		g.drawString("Move Count: " + moveCount, 50, textRow);
		if (characterRow == endRow && characterCol == endCol) {
			g.drawString("Great Job!",500, textRow);
		}
		
		*/
		
	}

	public void setBoard() {
		File name = new File("Design.txt");
		try {
			BufferedReader input = new BufferedReader(new FileReader(name));
			String text;
			while ((text = input.readLine()) != null) {
				row = text.split("");
				mazeRow.add(row);
			}
			maze = new String[mazeRow.size()][row.length - 1];
			for (int i = 0; i < maze.length; i++) {
				for (int j = 0; j < maze[0].length; j++) {
					maze[i][j] = mazeRow.get(i)[j];
				}
			}

		} catch (IOException io) {
			System.err.println("File error");
		}
		setMazeCrumbs();
		music();
		setLeftWall(numberCount);
		setRightWall(numberCount);
		setFrontWall(numberCount);
		setCeiling(numberCount);
		setFloor(numberCount);
	}

	public void setMazeCrumbs() {
		mazeCrumbs = new String[maze.length][maze[0].length];
		for (int i = 0; i < mazeCrumbs.length; i++) {
			for (int j = 0; j < mazeCrumbs[0].length; j++) {
				mazeCrumbs[i][j] = maze[i][j];
			}
		}

	}

	public void setLeftWall(int num) {
		for (int j = 0; j < num; j++) {
			int[] rectY = { distance, distance, frameHeight - distance, frameHeight - distance };
			int[] rectXL = { 0, rectWidth, rectWidth, 0 };
			for (int i = 0; i < 4; i++) {
				rectXL[i] += rectWidth * j;
				if (i < 2)
					rectY[i] += distance * j;
				else
					rectY[i] -= distance * j;
			}
			Wall wallLeft = new Wall(rectY, rectXL);
			walls.add(wallLeft);

			int[] trapY = { distance * j, distance + distance * j, frameHeight - distance - distance * j,
					frameHeight - distance * j };
			Wall trapLeft = new Wall(trapY, rectXL);

			if (characterCol + j < maze[0].length && characterRow - 1 >= 0
					&& maze[characterRow - 1][characterCol + j].equals("*") && rot == 0) {
				walls.add(trapLeft);
			}
			if (characterRow + j < maze.length && characterCol + 1 < maze[0].length
					&& maze[characterRow + j][characterCol + 1].equals("*") && rot == 1) {
				walls.add(trapLeft);
			}
			if (characterCol - j >= 0 && characterRow + 1 < maze.length
					&& maze[characterRow + 1][characterCol - j].equals("*") && rot == 2) {
				walls.add(trapLeft);
			}
			if (characterRow - j >= 0 && characterCol - 1 >= 0 && maze[characterRow - j][characterCol - 1].equals("*")
					&& rot == 3) {
				walls.add(trapLeft);
			}
		}
	}

	public void setRightWall(int num) {
		for (int j = 0; j < num; j++) {
			int[] rectY = { distance, distance, frameHeight - distance, frameHeight - distance };
			int[] rectXR = { frameWidth, frameWidth - rectWidth, frameWidth - rectWidth, frameWidth };
			for (int i = 0; i < 4; i++) {
				rectXR[i] -= rectWidth * j;
				if (i < 2)
					rectY[i] += distance * j;
				else
					rectY[i] -= distance * j;
			}
			Wall wallRight = new Wall(rectY, rectXR);
			walls.add(wallRight);

			int[] trapY = { distance * j, distance + distance * j, frameHeight - distance - distance * j,
					frameHeight - distance * j };
			Wall trapRight = new Wall(trapY, rectXR);

			if (characterCol + j < maze[0].length && characterRow + 1 < maze.length
					&& maze[characterRow + 1][characterCol + j].equals("*") && rot == 0) {
				walls.add(trapRight);
			}
			if (characterRow + j < maze.length && characterCol - 1 >= 0
					&& maze[characterRow + j][characterCol - 1].equals("*") && rot == 1) {
				walls.add(trapRight);
			}
			if (characterCol - j >= 0 && characterRow - 1 >= 0 && maze[characterRow - 1][characterCol - j].equals("*")
					&& rot == 2) {
				walls.add(trapRight);
			}
			if (characterRow - j >= 0 && characterCol + 1 < maze[0].length
					&& maze[characterRow - j][characterCol - 1].equals("*") && rot == 3) {
				walls.add(trapRight);
			}
		}
	}

	public void setFrontWall(int num) {
		for (int j = 0; j < num; j++) {
			int midpoint = (rectWidth * j + frameWidth - rectWidth * j) / 2;
			int[] frontXR = { rectWidth * j, midpoint, midpoint, rectWidth * j };
			int[] frontXL = { midpoint, frameWidth - rectWidth * j, frameWidth - rectWidth * j, midpoint };
			int[] frontY = { distance * j, distance * j, frameHeight - distance * j, frameHeight - distance * j };
			Wall frontWallR = new Wall(frontY, frontXR);
			Wall frontWallL = new Wall(frontY, frontXL);
			if (rot == 0 && characterCol + j < maze[0].length && maze[characterRow][characterCol + j].equals("*")) {
				front.add(frontWallR);
				front.add(frontWallL);
			}
			if (rot == 1 && characterRow + j < maze.length && maze[characterRow + j][characterCol].equals("*")) {
				front.add(frontWallR);
				front.add(frontWallL);
			}
			if (rot == 2 && characterCol - j >= 0 && maze[characterRow][characterCol - j].equals("*")) {
				front.add(frontWallR);
				front.add(frontWallL);
			}
			if (rot == 3 && characterRow - j >= 0 && maze[characterRow - j][characterCol].equals("*")) {
				front.add(frontWallR);
				front.add(frontWallL);
			}
		}
	}

	public void setCeiling(int num) {
		for (int j = 0; j < num; j++) {
			int[] ceilingY = { distance * j, distance * j, distance + distance * j, distance + distance * j };
			int[] ceilingX = { rectWidth * j, frameWidth - rectWidth * j, frameWidth - rectWidth * j, rectWidth * j };
			Wall ceiling = new Wall(ceilingY, ceilingX);
			ceilings.add(ceiling);
		}
	}

	public void setFloor(int num) {
		for (int j = 0; j < num; j++) {
			int[] floorY = { frameHeight - distance * j, frameHeight - distance * j,
					frameHeight - distance - distance * j, frameHeight - distance - distance * j };
			int[] floorX = { rectWidth * j, frameWidth - rectWidth * j, frameWidth - rectWidth * j, rectWidth * j };
			Wall floor = new Wall(floorY, floorX);
			floors.add(floor);
		}
	}

	public void setCrumbs(int num) {
		int size = 50;
		for (int j = 0; j < num; j++) {
			int y = (frameHeight - distance * j + frameHeight - distance - distance * j) / 2 + 28;
			int x = 710;
			BreadCrumb crumb = new BreadCrumb(y, x, size);
			if (rot == 0 && characterCol + j < maze[0].length
					&& mazeCrumbs[characterRow][characterCol + j].equals("+")) {
				crumbs.add(crumb);
			}
			if (rot == 1 && characterRow + j < maze.length && mazeCrumbs[characterRow + j][characterCol].equals("+")) {
				crumbs.add(crumb);
			}
			if (rot == 2 && characterCol - j >= 0 && mazeCrumbs[characterRow][characterCol - j].equals("+")) {
				crumbs.add(crumb);
			}
			if (rot == 3 && characterRow - j >= 0 && mazeCrumbs[characterRow - j][characterCol].equals("+")) {
				crumbs.add(crumb);
			}
		}
	}

	public void keyPressed(KeyEvent e) {
		/*
		 * ROT = 0: RIGHT ROT = 1: DOWN ROT = 2: LEFT ROT = 3: UP
		 */
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_UP:
			if (characterCol + 1 < maze[0].length && maze[characterRow][characterCol + 1].equals(" ") && rot == 0) {
				characterCol++;
				direction = "Right";
				moveCount++;
			}
			if (characterRow + 1 < maze.length && maze[characterRow + 1][characterCol].equals(" ") && rot == 1) {
				characterRow++;
				direction = "Down";
				moveCount++;
			}
			if (characterCol - 1 >= 0 && maze[characterRow][characterCol - 1].equals(" ") && rot == 2) {
				characterCol--;
				direction = "Left";
				moveCount++;
			}
			if (characterRow - 1 >= 0 && maze[characterRow - 1][characterCol].equals(" ") && rot == 3) {
				characterRow--;
				direction = "Up";
				moveCount++;
			}
			break;
		case KeyEvent.VK_LEFT:
			if (rot == 0)
				rot = 3;
			else
				rot--;
			break;
		case KeyEvent.VK_RIGHT:
			rot++;
			rot = rot % 4;
			break;
		case KeyEvent.VK_F:
			if (flashlight == false) {
				numberCount = 5;
				rectWidth = 125;
				flashlight = true;
			} else {
				numberCount = 4;
				rectWidth = 140;
				flashlight = false;
			}
		case KeyEvent.VK_B:
			if ((!(mazeCrumbs[characterRow][characterCol].equals("+"))) && breadCrumbs > 0) {
				mazeCrumbs[characterRow][characterCol] = "+";
				breadCrumbs--;
			}
		}

		walls = new ArrayList<Wall>();
		ceilings = new ArrayList<Wall>();
		floors = new ArrayList<Wall>();
		front = new ArrayList<Wall>();
		crumbs = new ArrayList<BreadCrumb>();

		setLeftWall(numberCount);
		setRightWall(numberCount);
		setFrontWall(numberCount);
		setCeiling(numberCount);
		setFloor(numberCount);
		setCrumbs(numberCount);

		repaint();

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void music() {
		try {
			File audioFile = new File("MazeRunnerWav.wav");
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			Clip audioClip = AudioSystem.getClip();
			audioClip.open(audioInputStream);
			audioClip.start();
			audioClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		MazeMain app = new MazeMain();
	}
}