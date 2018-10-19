/*

This is the file where you need to change things.

Three places:

1. Logic: In the function "step".
You will need to implement '4-neighbour' and '8-neighbour'

2. Adding a new starting condition: search for the phrase "Two diag Lines".

After you are done, we will do a few modification, some of which together:
a. Variable size of array (we)
b. Reading pattern from file (<-- preparation for a maze) (we)
c. building and solving a maze (you)
d. stack for depth-first (we)
e. comparing process (you)




 */

public class PathFinderGame {

	private int stepCounter;

	private boolean n8 = true ;     // Neighbour of 8.

	// see the interplay of A, B and cellsNow and cellsNext later on below.
	// In general:
	// A and B are real memory space.
	// cellsNow and cellsNext just point at them.
	private static int[][] A;
	private static int[][] B;

	private int[][] cellsNow;
	private int[][] cellsNext;

	private int[][] tmp;

	private int rows;
	private int cols;


	private int startRow ;
	private int startCol ;

	private int endRow ;
	private int endCol ;


	// These are used also by Display, to disaply all possible options
	// These are set in setPattern() below
	public final static String[] modelNames = new String[]
			{"Clear","One diag line","Two diag lines","2 Start points", "Paul", "Shapes"};

	public PathFinderGame(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;

		A = new int[rows][cols];
		B = new int[rows][cols];

		cellsNow = A;
		cellsNext = B;

		// The idea:
			// A and B are the only ones with actual memory space.
		// cellsNow and cellsNext are only 'pointers' to which one is which,
		// and we move between them every turn.

		stepCounter = 0;

		// The below could be done in "init" function.
		startRow = 0; 
		startCol = 0;
		endRow = rows-1;
		endCol = cols-1;
		cellsNow[startRow][startCol] = 1;


	}

	public void setN8( boolean in) {
		n8 = in;
	}

	public boolean getN8() {
		return n8 ;
	}

	public void step() {



		/* 
        Main part: Go over all elements, and apply the neighbours rule.
		 */

		if (n8) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (cellsNow[r][c] > 0) {
						cellsNext[r][c] = cellsNow[r][c];
						update8Neighbors(r, c);
					} else if (cellsNext[r][c] == 0) {
						cellsNext[r][c] = cellsNow[r][c];
					}
				}
			}
		} if (!n8) {
			for (int r = 0; r < rows; r++) {
				for (int c = 0; c < cols; c++) {
					if (cellsNow[r][c] > 0) {
						cellsNext[r][c] = cellsNow[r][c];
						update4Neighbors(r, c);
					} else if (cellsNext[r][c] == 0) {
						cellsNext[r][c] = cellsNow[r][c];
					}
				}
			}
		}
		// Flip the arrays now.
		stepCounter++;
		tmp = cellsNow;
		cellsNow = cellsNext;
		cellsNext  = tmp;
	}

	private void update8Neighbors(int r, int c) {
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (r+i < rows && c+j < cols && r+i >= 0 && c+j >= 0 && cellsNext[r+i][c+j] == 0) {
					setToLowest8Neighbor(r+i, c+j, r, c);
				}
			}
		}
	}

	private void update4Neighbors(int r, int c) {
		for (int i = -1; i < 2; i += 2) {
			if (r+i < rows && r+i >= 0 && cellsNext[r+i][c] == 0)
				setToLowest4Neighbor(r+i, c, r, c);
			if (c+i < cols && c+i >= 0 && cellsNext[r][c+i] == 0)
				setToLowest4Neighbor(r, c+i, r, c);
		}
	}

	private void setToLowest8Neighbor(int r, int c, int startr, int startc) {
		int minval = cellsNow[startr][startc];
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				if (r+i < rows && c+j < cols && r+i >= 0 && c+j >= 0)
					if (cellsNow[r+i][c+j] > 0 && cellsNow[r+i][c+j] < minval) {
						minval = cellsNow[r+i][c+j];
					}
			}
		}
		cellsNext[r][c] = minval + 1;
	}

	private void setToLowest4Neighbor(int r, int c, int startr, int startc) {
		int minval = cellsNow[startr][startc];
		for (int i = -1; i < 2; i += 2) {
			if (r+i < rows && r+i >= 0 && cellsNext[r+i][c] > 0 && cellsNext[r+i][c] < minval)
				minval = cellsNext[r+i][c];
			if (c+i < cols && c+i >= 0 && cellsNext[r][c+i] > 0 && cellsNext[r][c+i] < minval)
				minval = cellsNext[r][c+i];
		}
		cellsNext[r][c] = minval + 1;
	}

	private void clearCells(int[][] array) {
		for (int ii = 0; ii < rows; ii++) {
			for (int jj = 0; jj < cols; jj++) {
				array[ii][jj] = 0;
			}
		}
	}

	public int getCell(int row, int col) {
		return cellsNow[row][col];
	}

	public void setCell(int row, int col, int val) {
		cellsNow[row][col] = val;
	}

	public void flipCell(int row, int col) {
		cellsNow[row][col] = 1 - cellsNow[row][col];
	}

	public int getStepCounter() {
		return stepCounter;
	}

	public int getStartRow() { return startRow;}
	public int getStartCol() { return startCol;}
	public int getEndRow() { return endRow;}
	public int getEndCol() { return endCol;}

	public boolean gameEnded() {
		return (cellsNow[endRow][endCol] >0) ;
	}


	public void setPattern(String pattern) {

		// Clear everything
		clearCells(cellsNow);
		stepCounter = 0;

		if (pattern.equals("Clear")) ;

		if (pattern.equals("One diag line")) {

			for (int cc=2; cc<cols-10 ; ++cc) {
				if (cc<20 || cc>24) {
					int r = (int) Math.round(0.15*cc + 5);
					if (r >=0 && r<rows-1) {
						cellsNow[r][cc] = -1;
						cellsNow[r+1][cc] = -1;
					}
				}
			}
			startRow = 3;
			startCol = 6;
			endRow = rows-5;
			endCol = cols-2;
			cellsNow[startRow][startCol] = 1;

		}

		if (pattern.equals("Two diag lines")) {

			for (int cc=2; cc<cols-10 ; ++cc) {
				if (cc<20 || cc>24) {
					int r = (int) Math.round(0.15*cc + 5);
					if (r >=0 && r<rows-1) {
						cellsNow[r][cc] = -1;
						cellsNow[r+1][cc] = -1;
					}
				}
			}



			for (int cc=2; cc<cols-1 ; ++cc) {
				int r = (int) Math.round(-0.15*cc + 30);
				if (r >=0 && r<rows-1) {
					cellsNow[r][cc] = -1;
					cellsNow[r+1][cc] = -1;
				}
			}
			startRow = 3;
			startCol = 6;
			endRow = rows-5;
			endCol = cols-2;
			cellsNow[startRow][startCol] = 1;

		}
		
		if (pattern.equals("2 Start points")) {
			cellsNow[10][4] = 1;
			cellsNow[30][20] = 1;
		}
		
		if (pattern.equals("Paul")) {

			for (int i = 1; i < rows-1; i++)
				for (int j = 1; j < cols-1; j++) {
					if (i % 3 == 0 && j % 3 == 0)
						cellsNow[i][j] = -1;
					if (i % 2 == 0 && j % 2 == 0)
						cellsNow[i][j] = -1;
					if (i % 5 == 0 && j % 5 == 0)
						cellsNow[i][j] = -1;
				}
			startRow = 3;
			startCol = 6;
			endRow = rows-5;
			endCol = cols-2;
			cellsNow[startRow][startCol] = 1;

		}
		
		if (pattern.equals("Shapes")) {
			int radius = rows > cols ? rows/4 : cols/4;
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					double circlex = Math.pow(i-rows/2, 2);
					double circley = Math.pow(j-cols/2, 2);
					if (0.75 > Math.abs(Math.pow(circlex + circley, 0.5) - radius))
						cellsNow[i][j] = -1;
				}
			}
			
			for (int i = rows/2-rows/8; i <= rows/2+rows/8; i++) {
				cellsNow[i][cols/2-cols/8] = -1;
				cellsNow[i][cols/2+cols/8] = -1;
			}
			for (int j = cols/2-cols/8; j <= cols/2+cols/8; j++) {
				cellsNow[rows/2-rows/8][j] = -1;
				cellsNow[rows/2+rows/8][j] = -1;
			}
			
			startRow = 3;
			startCol = 6;
			endRow = rows/2;
			endCol = cols/2;
			cellsNow[startRow][startCol] = 1;
			cellsNow[rows/2][cols/2 + radius] = 0;
			cellsNow[rows/2][cols/2 - cols/8] = 0;
		}

	}

}
