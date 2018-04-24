package hw2;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.lang.Math;

public class CellGraph {

	private List<LinkedList<Cell>> adjacencyList;
	private Cell[] cellRecord;
	private final int SIZE;
	private final int TOTAL_CELLS;

	public CellGraph(int size){
		this.SIZE = size;
		this.TOTAL_CELLS = size * size;
		fillAdjacencyWithEmptyLLs();
		fillCellRecord();
	}

	private void fillCellRecord(){

		//each cell should know its position in the list
		Cell[] cr = new Cell[TOTAL_CELLS];
		for (int i = 0; i < cr.length;i++){
			Cell cell = new Cell(i);
			cell.row = i / SIZE;
			cell.column = i % SIZE;
			cr[i] = cell;
		}
		for (int i = 0; i < TOTAL_CELLS; i++ ) {
			if (i + 1 < cr.length)
				if (cr[i].row == cr[i + 1].row)
					cr[i].addNeighbor(i + 1);
			if (i - 1 >= 0) {
				if (cr[i].row == cr[i -1].row)
					cr[i].addNeighbor(i - 1);
			}
			if (i + SIZE < cr.length) {
				if (cr[i].column == cr[i + SIZE].column)
					cr[i].addNeighbor(i + SIZE);
			}
			if (i - SIZE >= 0) {
				if (cr[i].column == cr[i - SIZE].column)
					cr[i].addNeighbor(i - SIZE);
			}
		}
		cellRecord = cr;
	}

	private void fillAdjacencyWithEmptyLLs(){
		this.adjacencyList = new ArrayList<LinkedList<Cell>>();
		for (int i = TOTAL_CELLS; i > 0; i--){
			this.adjacencyList.add(new LinkedList<Cell>());
		}
	}

	public int findClosedNeighbors(int cell){
		ArrayList<Integer> cell_nbors = cellRecord[cell].neighbors;
//		sopl("number of neighbors for cell  " + cell + " is:   "+ cell_nbors.size());
		ArrayList<Integer> potentials = new ArrayList<Integer>();
		for (Integer elt: cell_nbors) {
			if(cellRecord[elt].isWhole())
				potentials.add(elt);
		}
		if (potentials.size() > 1)
			return potentials.get((int)(Math.floor((Math.random() * potentials.size()))));
		else if (potentials.size() == 1)
			return potentials.get(0);
		return 0;
	}

	private void connectCells(int cell1, int cell2) {
		adjacencyList.get(cell1).add(cellRecord[cell2]);
		Cell first = cellRecord[cell1];
		Cell second = cellRecord[cell2];

		if (first.row == second.row){
			if (first.position > second.position){
				first.west = false;
				second.east = false;
			}
			else {
				first.east = false;
				second.west = false;
			}
		}
		else if (first.column == second.column){
			if(first.position > second.position){
				first.north = false;
				second.south = false;
			}
			else{
				first.south = false;
				second.north = false;
			}
		}

	}

	public void buildMaze() {
		//make the first and last always have open and closed entrance and exit:
		Stack<Integer> cellStack = new Stack<Integer>();
		int currentCell = 0;
		int visited = 1;

		while( visited < TOTAL_CELLS){
			int neighbor = findClosedNeighbors(currentCell);
			//sopl("neighbor retrieved is:"+neighbor);
			if( neighbor > 0){
				connectCells(currentCell, neighbor);
				cellStack.push(currentCell);
				currentCell = neighbor;
				visited++;
			}
			else if (!cellStack.isEmpty())
				currentCell = cellStack.pop();
		}
		cellRecord[0].north = false;
		cellRecord[TOTAL_CELLS - 1].south = false;

	}

	public void solve_BFS(){
		return;
	}

	public void solve_DFS() {
		return;
	}

	public Cell[] getCellRecord(){
		return cellRecord;
	}

	public void printMaze(Cell[] maze){
		int mazeSize = (int)Math.sqrt(maze.length);
		String build = "";
		int count = 0;
		int index = 0;
		for (int i = 0; i < mazeSize; i++)
		{
			while(count++ < mazeSize){
				if (maze[index].row == i)
					build += maze[index++].getNorth();
			}
			build += "+\n";
			count = 0;
			index -= mazeSize;

			while(count++ < mazeSize){
				if (maze[index].row == i) {
					build += maze[index].getWest() + maze[index].getValue() ;
					index++;
				}
			}
			build += "|\n";
			count = 0;
		}

		index = maze.length - mazeSize;
		while(index < maze.length){
			build += maze[index++].getSouth();
		}
		build += "+\n";
		sopl(build);
	}

	private void sop(Object x){
		System.out.print(x);
	}

	private void sopl(Object x){
		System.out.println(x);
	}

	public Cell[] getSampleMaze(){
		Cell[] m = {
				new Cell(0, false, true, false, true),
				new Cell(1, true, false, true, false),
				new Cell(2, false, false, true, true),
				new Cell(3, true, false, false, false)
		};
		for (Cell elt: m){
			elt.setRow(2);
		}
		return m;
	}
	public Cell[] getSampleMaze1(){
		Cell[] m = { new Cell(0, true, true, true, true),
				new Cell(1, true, true, true, true),
				new Cell(2, true, true, true, true),
				new Cell(3, true, true, true, true)};
		for (Cell elt: m){
			elt.setRow(2);
		}
		return m;
	}

	//||_____________________________inner class: Cell______________________|
	public class Cell {
		boolean west;
		boolean east;
		boolean north;
		boolean south;
		int visitedOrder_bfs;
		int visitedOrder_dfs;
		ArrayList<Integer> neighbors;
		int position;
		boolean hash;
		int row;
		int column;
		boolean white;
		boolean grey;
		boolean black;

		Cell(int position){
			west = north = south = east = true;
			visitedOrder_bfs = visitedOrder_dfs =  0;
			this.position = position;
			hash = false;
			row = column = -1;
			neighbors = new ArrayList<Integer>();

		}

		Cell(int pos, boolean n, boolean e, boolean s, boolean w){
			position = pos;
			north = n;
			east = e;
			south = s;
			west = w;
			hash = false;
			row = column = -1;
		}

		boolean isWhole(){
			return (east && south && west && north);
		}
		String getNorth() {
			String n = (north) ? "+-" : "+ ";
			return n;
		}
		String getEast(){
			String e =  east ? "|" : " ";
			return e;
		}

		String getWest(){
			String w = west ? "|" : " ";
			return w;
		}

		String getSouth(){
			return south ? "+-" : "+ ";

		}
		String getValue(){
			return hash ? "#" : (" ");
		}

		void setRow(int size){
			row = position / size;
			column = position % size;
		}

		void addNeighbor(int nei) {
			if (neighbors.size() < 4) {
				neighbors.add(nei);
			}
		}
	}
	//||_________________________inner class Cell_________________







	public static void main(String[] args){
		CellGraph g = new CellGraph(5);
		g.buildMaze();
		g.printMaze(g.getCellRecord());
	}
}