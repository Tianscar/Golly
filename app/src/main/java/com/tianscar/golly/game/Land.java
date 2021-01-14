package com.tianscar.golly.game;

public class Land {

	private final int width;
	private final int height;

	private Cell[][] cellMap;
	private int dayCount;
	private int aliveCellCount;

	public static final double ALIVE_PROBABILITY_DEFAULT = 0.5;
	public static final double ALIVE_PROBABILITY_ALL_DEAD = 0;
	public static final double ALIVE_PROBABILITY_ALL_ALIVE = 1;

	public Land (int size) {
		this(size, size);
	}

	public Land (int width, int height) {
		this(width, height, ALIVE_PROBABILITY_DEFAULT);
	}

	public Land (int size, double aliveProbability) {
		this(size, size, aliveProbability);
	}

	public Land (int width, int height, double aliveProbability) {
		if (width < 0) {
			throw new IllegalArgumentException("Width must be > 0");
		}
		if (height < 0) {
			throw new IllegalArgumentException("Height must be > 0");
		}
		this.width = width;
		this.height = height;
		cellMap = new Cell[width][height];
		dayCount = 0;
		aliveCellCount = 0;
		init(aliveProbability);
	}

	public void init () {
		init(ALIVE_PROBABILITY_DEFAULT);
	}

	public void init (double aliveProbability) {
		for (int x = 0; x < width; x ++) {
			for (int y = 0; y < height; y ++) {
				cellMap[x][y] = new Cell(deadOrAlive(aliveProbability));
			}
		}
	}

	public void invalidate () {
		dayCount ++;
		aliveCellCount = 0;
		Land stepLand = new Land(width, height, ALIVE_PROBABILITY_ALL_DEAD);
		for (int x = 0; x < width; x ++)
		{
			for (int y = 0; y < height; y ++)
			{
				Cell cell = getCell(x, y);
				Cell stepCell = stepLand.getCell(x, y);
				int n = getPosCellCount(x, y);
				if (cell.getState() == 1)
				{
					aliveCellCount ++;
					if (n < 2) {
						stepCell.die();
					}
					if (n == 2 || n == 3) {
						stepCell.alive();
					}
					if (n > 3) {
						stepCell.die();
					}
				}
				else {
					if (stepCell.getState() == 0) {
						if (n == 3)
						{
							stepCell.alive();
						}
					}
				}
			}
		}
		this.cellMap = stepLand.cellMap;
		stepLand = null;
	}

	public void clear () {
		init(ALIVE_PROBABILITY_ALL_DEAD);
	}

	public int getPosCellCount(int posX, int posY) {
		int count = 0;
		for (int x = posX - 1; x <= posX + 1; x ++) {
			for (int y = posY - 1; y <= posY + 1; y ++) {
				if (x >= 0 && x < width && y >= 0 && y < height) {
					Cell cell = getCell(x, y);
					if (x == posX && y == posY) {
						count += 0;
					}
					else {
						count += cell.getState();
					}
				}
			}
		}
		return count;
	}

	public Cell getCell(int posX, int posY) {
		return cellMap[posX][posY];
	}

	private byte deadOrAlive (double aliveProbability) {
		if (aliveProbability < 0 || aliveProbability > 1) {
			throw new IllegalArgumentException("Invalid probability, the range is 0 <= probability <= 1");
		}
		if (Math.random() < aliveProbability) {
			return Cell.STATE_ALIVE;
		}
		else {
			return Cell.STATE_DEAD;
		}
	}

	public int getAliveCellCount() {
		return aliveCellCount;
	}

	public int getDeadCellCount() {
		return getCellCount() - getAliveCellCount();
	}

	public int getCellCount() {
		return getWidth() * getHeight();
	}

	public int getDayCount() {
		return dayCount;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
