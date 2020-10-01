//Maciej Nowaczyk

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolve {
  public static void main(String[] args) throws FileNotFoundException{
    String filename = args[0];
    Sudoku FirstSudoku = null;
    
    FirstSudoku = readSudoku(filename);
    
    
    
    FirstSudoku.print();
    Sudoku SecondSudoku = FirstSudoku.enumeration();
    System.out.println();
    SecondSudoku.print();
  }
  
  // Method that reads the sudoku from a file
  public static Sudoku readSudoku(String filename)
    throws java.io.FileNotFoundException {
    FileReader file = new FileReader (filename);
    Scanner scan = new Scanner(file);
    
    int numberOfHints = scan.nextInt();
    
    int[] places = new int[2];
    ArrayList<Cell> allHint = new ArrayList<Cell>();
    
    // reads the hints
    for (int i = 0; i < numberOfHints; i++) {
      places[0] = scan.nextInt();
      places[1] = scan.nextInt();
      ArrayList<Integer> hintReader = new ArrayList<Integer>();
      hintReader.add(scan.nextInt());
      
      // create a object cell and add the cell to the arraylist of cells allHint
      allHint.add(new Cell(hintReader, places));
    }
    // constructor of Sudoku.java
    return new Sudoku(allHint);
  }
}