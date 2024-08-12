import java.util.ArrayList;
import java.util.PriorityQueue;

public class AStar {
    public static final int V_H_COST = 10;              // distance of a cell to cell
    //  Cell of our grid
    private final Cell[][] grid;

    //  We define a priority queue for open cells
    //  Open Cells : the set of nodes to be evaluated
    //  we put cells with the lowest cost in first
    private final PriorityQueue<Cell> openCells;

    //  Closed Cells : the set of nodes already evaluated
    private final boolean[][] closedCells;

    //  Start Cell coordinates
    private int startI, startJ;

    //  End Cell coordinates
    private int endI, endJ;

    //  Constructor for AStar class
    public AStar(int width, int height, int si, int sj, int ei, int ej, int[][] blocks) {
        grid = new Cell[width][height];
        closedCells = new boolean[width][height];
        openCells = new PriorityQueue<>((Cell c1, Cell c2) -> {
            return c1.finalCost < c2.finalCost ? 1 : c1.finalCost > c2.finalCost ? -1 : 0;      //  give the priority for lowest final cost -should check -1 and 1
        });

        startCell(si, sj);                  // Set start cell
        endCell(ei, ej);                    // Set end cell

        // init heuristic cost of all cells
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                grid[i][j] = new Cell(i, j);
                grid[i][j].heuristicCost = Math.abs(i - endI) + Math.abs(j - endJ);             // Manhattan distance heuristic
                grid[i][j].solution = false;
            }
        }

        grid[startI][startJ].finalCost = 0;                                                     // Set initial cost to start cell

        //  Place blocks on the grid
        for (int i = 0; i < blocks.length; i++) {
            addBlockOnCell(blocks[i][0], blocks[i][1]);
        }
    }

    /**
     * Method to set a block on a specific cell in the grid.
     * @param i The row index of the cell to block.
     * @param j The column index of the cell to block.
     */
    public void addBlockOnCell(int i, int j) {
        grid[i][j] = null;
    }

    /**
     * Method to set the start cell's coordinates.
     * @param i The row index of the start cell.
     * @param j The column index of the start cell.
     */
    public void startCell(int i, int j) {
        startI = i;
        startJ = j;
    }

    /**
     * Method to set the end cell's coordinates.
     * @param i The row index of the end cell.
     * @param j The column index of the end cell.
     */
    public void endCell(int i, int j) {
        endI = i;
        endJ = j;
    }

    /**
     * Method to update cost if it's needed for a given cell.
     * @param current The current cell being evaluated.
     * @param t       The neighboring cell to be checked.
     * @param cost    The cost to reach the neighboring cell.
     */
    public void updateCostIfNeeded(Cell current, Cell t, int cost) {
        if (t == null || closedCells[t.i][t.j])
            return;

        int tFinalCost = t.heuristicCost + cost;
        boolean isOpen = openCells.contains(t);

        if (!isOpen || tFinalCost < t.finalCost) {
            t.finalCost = tFinalCost;
            t.parent = current;

            if (!isOpen) {
                openCells.add(t);
            }
        }
    }

    /**
     * Main process method for the A* algorithm.
     * This method performs the A* search algorithm to find the path from start to end.
     * It updates the open and closed sets, calculates costs, and explores neighboring cells.
     */
    public void process() {
        openCells.add(grid[startI][startJ]);
        Cell current;

        while (true) {
            current = openCells.poll();

            if (current == null) {
                break;
            }

            closedCells[current.i][current.j] = true;

            if (current.equals(grid[endI][endJ])) {
                return;
            }

            Cell t;

            // Check neighbors
            if (current.i - numUpSlider(current) >= 0) {
                t = grid[current.i - numUpSlider(current)][current.j];
                updateCostIfNeeded(current, t, current.finalCost + (V_H_COST* numUpSlider(current)));
            }

            if (current.j - numLeftSlider(current) >= 0) {
                t = grid[current.i][current.j - numLeftSlider(current)];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST* numLeftSlider(current));
            }

            if (current.j + numRightSlider(current) < grid[0].length) {
                t = grid[current.i][current.j + numRightSlider(current)];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST* numRightSlider(current));
            }

            if (current.i + numDownSlider(current) < grid.length) {
                t = grid[current.i + numDownSlider(current)][current.j];
                updateCostIfNeeded(current, t, current.finalCost + V_H_COST* numDownSlider(current));
            }
        }
    }

    /**
     * Method to count unblocked cells upwards from the current cell.
     * @param current The current cell.
     * @return The number of unblocked cells upwards.
     */
    public int numUpSlider(Cell current){
        int ci = current.i;
        int cj = current.j;
        int count = 0;
        while(ci-1>=0 && grid[ci-1][cj]!=null){
            count++;
            ci--;
            if(ci == endI && cj == endJ){
                return count;
            }
        }

        return count;
    }

    /**
     * Method to count unblocked cells to the left from the current cell.
     * @param current The current cell.
     * @return The number of unblocked cells to the left.
     */
    public int numLeftSlider(Cell current){
        int ci = current.i;
        int cj = current.j;
        int count = 0;
        while(cj-1>=0 && grid[ci][cj-1]!=null){
            count++;
            cj--;
            if(ci == endI && cj == endJ){
                return count;
            }
        }

        return count;
    }

    /**
     * Method to count unblocked cells downwards from the current cell.
     * @param current The current cell.
     * @return The number of unblocked cells downwards.
     */
    public int numDownSlider(Cell current){
        int ci=current.i;
        int cj=current.j;
        int count=0;
        while(ci+1<grid[0].length && grid[ci+1][cj]!=null){
            count++;
            ci++;
            if(ci == endI && cj == endJ){
                return count;
            }
        }

        return count;
    }

    /**
     * Method to count unblocked cells to the right from the current cell.
     * @param current The current cell.
     * @return The number of unblocked cells to the right.
     */
    public int numRightSlider(Cell current){
        int ci=current.i;
        int cj=current.j;
        int count=0;
        while(cj+1<grid[0].length && grid[ci][cj+1]!=null){
            count++;
            cj++;
            if(ci == endI && cj == endJ){
                return count;
            }
        }

        return count;
    }

    /**
     * Method to display the correct solution path.
     * It prints the path taken from start to end, marked with 'X'.
     * It also shows the grid with 'S' for start, 'F' for end, and '.' for empty cells.
     */
    public void displaySolutionCorrect() {
        ArrayList<Cell> solutionList = new ArrayList<>();       //  cells in right order
        if (closedCells[endI][endJ]) {
            //we track back the path
            System.out.println("Path :");
            Cell current = grid[endI][endJ];
            solutionList.add(0,current);
            grid[current.i][current.j].solution = true;

            while (current.parent != null) {
                solutionList.add(0,current.parent);
                grid[current.parent.i][current.parent.j].solution = true;
                current = current.parent;
            }
            Cell temp = null;
            int stepCount = 0;               //  to count the number of steps gone to the solution
            for (Cell cell:solutionList) {
                if(temp == null) {          //  first node
                    System.out.println(++stepCount+". Start at      ("+(cell.j+1)+","+(cell.i+1)+")");      //change i and j while printing to more visualisation
                }else{
                    if(temp.i>cell.i){
                        System.out.println(++stepCount+". Move up to    ("+(cell.j+1)+","+(cell.i+1)+")");
                    }else if(temp.i<cell.i){
                        System.out.println(++stepCount+". Move down to  ("+(cell.j+1)+","+(cell.i+1)+")");
                    }else if(temp.j>cell.j){
                        System.out.println(++stepCount+". Move left to  ("+(cell.j+1)+","+(cell.i+1)+")");
                    }else if(temp.j<cell.j){
                        System.out.println(++stepCount+". Move right to ("+(cell.j+1)+","+(cell.i+1)+")");
                    }
                }
                temp=cell;

            }
            System.out.println(++stepCount+". Done!\n");

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if (i == startI && j == startJ) {
                        System.out.print("S");              //  source cell
                    } else if (i == endI && j == endJ) {
                        System.out.print("F");              //  destination cell
                    } else if (grid[i][j] != null) {
                        System.out.printf("%-1s", grid[i][j].solution ? "X" : ".");             //  cham- print x if solution true otherwise 0
                    } else {
                        System.out.print("0");              //  blocked cell
                    }
                }
                System.out.println();
            }
            System.out.println();
        } else {
            System.out.println("No possible path");
        }
    }

    /**
     * Method to display the final costs of each cell.
     * It prints the final cost of each cell in the grid.
     */
    public void displayScores() {
        System.out.println("\nScores for cells :");

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] != null) {
                    System.out.printf("%-3d ", grid[i][j].finalCost);
                } else {
                    System.out.print("BL  ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Method to display the current grid.
     * It prints the grid with 'S' for start, 'F' for end, and '.' for empty cells.
     */
    public void display() {
        System.out.println("Grid :");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == startI && j == startJ) {
                    System.out.print("S");              //  source cell
                } else if (i == endI && j == endJ) {
                    System.out.print("F");              //  destination cell
                } else if (grid[i][j] != null) {
                    System.out.print(".");
                } else {
                    System.out.print("0");              //  blocked cell
                }
            }
            System.out.println();
        }
        System.out.println();
    }

}
