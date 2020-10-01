//Maciej Nowaczyk

import java.util.*;

public class Sudoku {
  private Cell[][] matrixOfCells;
  private Queue<Cell> queueOfSolCell;
  
  //creates a Sudoku with the given arguments
  public Sudoku(ArrayList<Cell> listOfHints) {
    ArrayList<Integer> input = new ArrayList<Integer>();
    for (int i = 0; i <= 8; i++) {
      input.add(i + 1);
    }
    
    this.matrixOfCells = new Cell[9][9];
    int[] places = new int[2];
    
    //Makes the 9x9 matrix with 81 cells
    for (int r = 0; r <= 8; r++) {
      for (int c = 0; c <= 8; c++) {
        places[0] = r;
        places[1] = c;
        this.matrixOfCells[r][c] = new Cell(input, places);
      }
    }
    this.queueOfSolCell = new LinkedList<Cell>();
    for (int i = 0; i < listOfHints.size(); i++) {
      subtitute(listOfHints.get(i));
    }
  }
  
  public Cell[][] getMatrixOfCell() {
    return this.matrixOfCells;
  }
  
  public Queue<Cell> getQueueOfSolCell() {
    return this.queueOfSolCell;
  }
  
  
  public void subtitute(Cell cell1) {
    this.matrixOfCells[cell1.getRow()][cell1.getColumn()] = cell1;
    if (cell1.size() == 1) {
      this.queueOfSolCell.add(cell1);
    }
  }
  
  
  public void rowSimple(Cell solCell) {
    // For every column c,
    for (int c = 0; c <= 8; c++) {
      // We select a row( of the solution cell), and look at each (column)cell of that row
      Cell cellA = this.matrixOfCells[solCell.getRow()][c];
      //If we are not in the solution cell(not same column)
      if (c != solCell.getColumn()) {
        int sizeBefore = cellA.size();//get the number of candidates of cellA
        // we delete the number in the solution cell from the list of candidates of cellA
        cellA.deleteInteger(solCell.solutionOfCell());
        //if cellA is solved and the number of candidates it had, before we deleted the solution, was 2(recently solved).
        if (cellA.isSolved() && sizeBefore == 2)
          this.queueOfSolCell.add(cellA);// we add the cellA to the queue
        
      }
    }
  }
  
  public void columnSimple(Cell solCell) {
    //For every row r,
    for (int r = 0; r <= 8; r++) {
      // We select a column( of the solution cell), and look at each (row)cell of that column
      Cell cellA = this.matrixOfCells[r][solCell.getColumn()];
      //If we are not in the solution cell(not same row)
      if (r != solCell.getRow()) {
        int sizeBefore = cellA.size();//get the number of candidates of cellA
        // we delete the number in the solution cell from the list of candidates of cellA
        cellA.deleteInteger(solCell.solutionOfCell());
        //if cellA is solved and the number of candidates it had before was 2
        if (cellA.isSolved() && sizeBefore == 2)
          this.queueOfSolCell.add(cellA);// we add the cellA to the queue
        
      }
    }
  }
  
  public void boxSimple(Cell solCell) {
    // first cell of the box
    int[] firstCell = new int[2];
    firstCell[0] = solCell.getRow();
    firstCell[1] = solCell.getColumn();
    // left cell of a box:
    while (firstCell[0] != 0 && firstCell[0] != 3 && firstCell[0] != 6) 
      firstCell[0]--;
    // top cell of a box:
    while (firstCell[1] != 0 && firstCell[1] != 3 && firstCell[1] != 6)
      firstCell[1]--;
    
    // For every 3x3 box:
    for (int r = firstCell[0]; r < firstCell[0] + 3; r++)
      for (int c = firstCell[1]; c < firstCell[1] + 3; c++) {
      Cell cellA = this.matrixOfCells[r][c];
      // if we are not in the solution cell(not same row and column)
      if (!(r == solCell.getRow() && c == solCell.getColumn())) {
        int sizeBefore = cellA.size();
        // we delete the number in the solution cell from the list of candidates of cellA
        cellA.deleteInteger(solCell.solutionOfCell());
        //if cellA is solved and the number of candidates it had before was 2
        if (cellA.isSolved() && sizeBefore == 2)
          this.queueOfSolCell.add(cellA);// we add the cellA to the queue
        
      }
    }
  }
  
  public void generalizationRule() {
    while (!this.queueOfSolCell.isEmpty()) {
      // try to solve with simpleLogicRules
      Cell h = new Cell (this.queueOfSolCell.remove());
      rowSimple(h);
      columnSimple(h);
      boxSimple(h);
      //If simpleLogicRule does solve the Sudoku completely, do generalization rules:
      generalizationRow();
      generalizationColumn();
      generalizationBox();
    }
  }
  
  public void generalizationRow() {
    for (int r = 0; r <= 8; r++) {
      //For every row r create generalRow, a List of List of cells.
      List<List<Cell>> generalRow = new ArrayList<>();
      //For every number n create ArrayList and add it to generalRow
      for (int n = 1; n <= 9; n++) {
        generalRow.add(new ArrayList<>());
        //For every column in the row (cell),
        for (int c = 0; c <= 8; c++)
          //if the cell includes the number n, add cell to the the List of List of cells generalRow
          if (this.matrixOfCells[r][c].includes(n))
          generalRow.get(n - 1).add(this.matrixOfCells[r][c]);
        List<Cell> h = generalRow.get(n - 1);
        //If there is only one cadidate and the simple rules didn't solve it
        if (h.size() == 1 && h.get(0).size() > 1) {
          // delete all candidates except number n and get n
          h.get(0).deleteCandidatesExcept(n);
          // add n to the queue
          this.queueOfSolCell.add(h.get(0));
        }
      }
    }
  }
  
  
  public void generalizationColumn() {
    //For every column c create generalColumn, a List of List of cells.
    for (int c = 0; c <= 8; c++) {
      List<List<Cell>> generalColumn = new ArrayList<>();
      //For every number n create ArrayList and add it to generalRow
      for (int n = 1; n <= 9; n++) {
        generalColumn.add(new ArrayList<Cell>());
        //For every row in the column (cell),
        for (int r = 0; r <= 8; r++)
          //if the cell includes the number n, add the cell to the the List of List of cells generalColumn
          if (this.matrixOfCells[r][c].includes(n)) generalColumn.get(n - 1).add(this.matrixOfCells[r][c]);
        List<Cell> h = generalColumn.get(n - 1);
        //If there is only one cadidate and the simple rules didn't solve it
        if (h.size() == 1 && h.get(0).size() > 1) {
          // delete all candidates except number n and get n
          h.get(0).deleteCandidatesExcept(n);
          // add n to the queue
          this.queueOfSolCell.add(h.get(0));
        }
      }
    }
  }
  
  public void generalizationBox() {
    //3x3 Box structure with box row, r, and box column, c.
    for (int r = 0; r <= 6; r = r + 3)
      for (int c = 0; c <= 6; c = c + 3) {
      //For every box, create generalBox, which is a list of list of cells
      List<List<Cell>> generalBox = new ArrayList<>();
      //For every number n between 1 and 9
      for (int n = 1; n <= 9; n++) {
        generalBox.add(new ArrayList<Cell>());
        for (int row = r; row <= r + 2; row++)
          for (int col = c; col <= c + 2; col++)
          //if cell in row and col (in the box) includes the number n, then we add it to the List of List of cells generalBox
          if (this.matrixOfCells[row][col].includes(n))
          generalBox.get(n - 1).add(this.matrixOfCells[row][col]);
        List<Cell> h = generalBox.get(n - 1);
        //If there is only one cadidate and the simple rules didn't solve it
        if (h.size() == 1 && h.get(0).size() > 1) {
          h.get(0).deleteCandidatesExcept(n);// delete all candidates except number n and get n
          this.queueOfSolCell.add(h.get(0));// add n to the queue
        }
      }
    }
    
  }
  
  public Sudoku enumeration() {
    
    boolean done = false;
    // If the generalization rule solves the Sudoku then the method returns the Sudoku
    Sudoku FirstSudoku = clone();
    FirstSudoku.generalizationRule();
    if (!FirstSudoku.unfeasible() && FirstSudoku.isSolved()) return FirstSudoku;

    // Else we solve it using enumeration
    // First create a listOfSudoku which is empty
    ArrayList<Sudoku> listOfSudoku = new ArrayList<>();
    //For every row and column,
    for (int r = 0; r <= 8; r++) {
      for (int c = 0; c <= 8; c++) {
        if (FirstSudoku.matrixOfCells[r][c].size() > 1) {//If a cell in FirstSudoku is not solved
          //For all index h of the list of candidates of the cell that is not solved
          for (int h = 0; h < FirstSudoku.matrixOfCells[r][c].size(); h++) {
            //we clone FirstSudoku
            Sudoku SecondSudoku = FirstSudoku.clone();
            //we clone the 1st cell of SecondSudoku that is not solved
            Cell substituteCell = SecondSudoku.matrixOfCells[r][c].clone();
            //delete all the candidates except the candidate at index h, we guess h
            substituteCell.deleteCandidatesExceptIndex(h);
            //substitute it in the copied Sudoku, put the substituteCell back in SecondSudoku 
            SecondSudoku.subtitute(substituteCell);
            //add SecondSudoku, where we made a guess, to the listOfSudoku
            listOfSudoku.add(SecondSudoku);
          }
          done = true;
          break;
        }
      }
      if (done) break;
    }
    
    
    int maximumlistOfSudoku= 0;
    int count = 0;
    
    while (!listOfSudoku.isEmpty()) {
      // performance
      if (listOfSudoku.size() > maximumlistOfSudoku) maximumlistOfSudoku = listOfSudoku.size();
      count++;
      //remove and return SecondSudoku from the listOfSudoku and make the clone of SecondSudoku, we get FirstSudoku
      FirstSudoku = listOfSudoku.get(listOfSudoku.size() - 1).clone();
      listOfSudoku.remove(listOfSudoku.size() - 1);
      FirstSudoku.generalizationRule();// try to solve FirstSudoku using generalization
      
      // If FirstSudoku is unfeasible we continue in the while loop, neglect the guess
      if (FirstSudoku.unfeasible()) continue;
      
      // If FirstSudoku is solved:
      if (FirstSudoku.isSolved()) {
        // give performance
        System.out.println("Solution found after " + count + " Iterations.");
        System.out.println("The maximum amount of listOfSudoku: " + maximumlistOfSudoku);
        // return solution of Sudoku
        return FirstSudoku;
      }
      
      // Else we do another guess
      else {
        for (int r = 0; r <= 8; r++) {
          for (int c = 0; c <= 8; c++) {
            if (FirstSudoku.matrixOfCells[r][c].size() > 1) {//if Cell not solved
              for (int i = 0; i < FirstSudoku.matrixOfCells[r][c].size(); i++) {
                Sudoku SecondSudoku = FirstSudoku.clone();
                Cell substituteCell = SecondSudoku.matrixOfCells[r][c].clone();
                substituteCell.deleteCandidatesExceptIndex(i);
                SecondSudoku.subtitute(substituteCell);
                listOfSudoku.add(SecondSudoku);
              } 
              done = true;
              break;
            }
          }
          if (done) break;
        }
      }
    }
    // if the sudoku is unsolvable
    return null;
  }
  
  //Method that returns a clone of the sudoku
  @Override //overrides default, generic clone method from .Object
  public Sudoku clone() {
    //we create withoutHints, an arraylist of type Cell
    ArrayList<Cell> withoutHints = new ArrayList<>();
    //we create a Sudoku of ArrayList<Cell> withoutHints
    Sudoku newSudoku = new Sudoku(withoutHints);
    for (int r = 0; r <= 8; r++)
      for (int c = 0; c <= 8; c++) {
      if (this.matrixOfCells[r][c].isSolved())
        newSudoku.queueOfSolCell.add(this.matrixOfCells[r][c].clone());
      newSudoku.matrixOfCells[r][c] = this.matrixOfCells[r][c].clone();
    }
    
    return newSudoku;
  }
  
  // The sudoku is unfeasible if a cell is empty
  public boolean unfeasible() {
    for (int r = 0; r <= 8; r++)
      for (int c = 0; c <= 8; c++)
      if (this.matrixOfCells[r][c].isEmpty())
      return true;
    
    return false;
  }
  
  // A cell is solved if there is only a candidate in the list of candidates (size()=1)
  public boolean isSolved() {
    for (int r = 0; r <= 8; r++)
      for (int c = 0; c <= 8; c++)
      if (this.matrixOfCells[r][c].size() != 1)
      return false;
    
    return true;
  }
  
  //Print Sudoku
  public void print() {
    for (int r = 0; r <= 8; r++) {
      for (int c = 0; c <= 8; c++) {
        if (this.matrixOfCells[r][c].isEmpty()) System.out.print("+ ");
        
        else if (this.matrixOfCells[r][c].isSolved())
          System.out.print(this.matrixOfCells[r][c].solutionOfCell() + " ");
        
        else System.out.print("0 ");
        
      }
      System.out.println();
      
    }
    System.out.println();
  }
  
  
}