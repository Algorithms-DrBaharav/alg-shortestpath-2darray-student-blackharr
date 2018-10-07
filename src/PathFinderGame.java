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
    
    
    // -- Start of Place2
    // These are used also by Display, to disaply all possible options
    // These are set in setPattern() below
    // Give a new name to your shape, and put it here.
    public final static String[] modelNames = new String[]
			{"Clear","One diag line","Two diag lines","2 Start points"};    
    
    // -- End of Place2
    
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

    public void step() {


        
        /* 
        Main part: Go over all elements, and apply the neighbours rule.
        */

        for (int ii = 0; ii < rows; ii++) {
            for (int jj = 0; jj < cols; jj++) {
                // Four directions, and take care of wrap-around

                cellsNext[ii][jj] = cellsNow[ii][jj];
                
                if (cellsNow[ii][jj] !=0 ) continue ;
                
                int val = 0;
                for (int ns=-1; ns<=1 && val==0 ; ++ns) {
                    int r = ii+ns;
                    for (int ew=-1; ew<=1 && val==0 ; ++ew) {
                        int c = jj+ew;
                        if (r>=0 && r<rows && c>=0 && c<cols) {
                            if (cellsNow[r][c]>0) 
                                val=cellsNow[r][c] ; 
                        }
                    }
                }
                if (val >0)
                    cellsNext[ii][jj] = val+1;
                
            }
        }

        
        // Flip the arrays now.
        stepCounter++;
	tmp = cellsNow;
        cellsNow = cellsNext;
	cellsNext  = tmp;

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

    }

}
