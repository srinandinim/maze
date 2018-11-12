package maze;

import java.awt.Polygon;

public class Wall {

	int[] x;
	int[] y;

	public Wall (int[] x, int [] y) {
		this.x = x;
		this.y = y;
	}

	public int[] getX()
	{
		return x;
	}

	public int[] getY()
	{
		return y;
	}

	public Polygon getPolygon() {
		return new Polygon (y, x, x.length);
	}
}