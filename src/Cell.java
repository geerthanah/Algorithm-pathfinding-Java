public class Cell {
    //  Coordinates
    public int i, j;                // Row and Column index

    //  Parent cell for path
    public Cell parent;

    //  Heuristic cost of the current cell
    public int heuristicCost;

    //  Final cost
    public int finalCost;           //  G(n) + H(n)
                                    //  G(n) the cost of the path from the start node to n
                                    //  H(n) the heuristic that estimates the cost of the cheapest path from n to the goal

    public boolean solution;        //  Indicates if the cell is part of the solution path

    /**
     * Constructs a new Cell with the given coordinates.
     * @param i The row index of the cell.
     * @param j The column index of the cell.
     */
    public Cell(int i, int j) {
        this.i = i;
        this.j = j;
    }

    /**
     * Returns a string representation of the cell.
     * @return A string in the format "[i, j]" representing the cell's coordinates.
     */
    @Override
    public String toString() {
        return "[" + i + ", " + j + "]";
    }
}
