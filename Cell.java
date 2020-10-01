//Catarina Monteiro dos Ramos i6165818 and Maciej Nowaczyk i6134027 
//group 23

import java.util.ArrayList;

public class Cell {
  private ArrayList<Integer> listOfCandidates;
  private int[] place;
  private int temporary;
  
  // creates an object of type Cell with the given arguments
  public Cell(ArrayList<Integer> candidates, int[] places) {
    
    this.listOfCandidates = new ArrayList<Integer>();
    
    this.listOfCandidates.addAll(candidates);
    
    this.place = new int[2];
    this.place[0] = places[0];
    this.place[1] = places[1];
  }
  
  
  /*constructor made solely for deepcopy*/
  public Cell(Cell another) {
    this.listOfCandidates = another.listOfCandidates;
    this.place = another.place;
    this.temporary = another.temporary;
  }
  
  public Cell() {
    listOfCandidates = new ArrayList<>();
    place = new int[2];
    temporary = -1;
  }
  
  
  // Returns the list of candidates that we have now of the cell
  public ArrayList<Integer> candidatesNow() {
    return this.listOfCandidates; 
  }
  
  public int[] place() {
    return this.place;
  }
  
  public int getRow() {
    return place()[0];
  }
  
  public int getColumn() {
    return place()[1];
  }
  
  //Returns the size of the list which is the number of candidates
  public int size() {
    return this.listOfCandidates.size();
  }
  
  //A cell is solved if the size of the list is 1 which means that there is only 1 candidate.
  public boolean isSolved() {
    return size() == 1;
  }
  
  //Return if the list of candidates includes the integer i
  public boolean includes(int i) {
    return this.listOfCandidates.contains(i);
  }
  
  //Returns if the list of the candidates is empty
  public boolean isEmpty() {
    return this.listOfCandidates.isEmpty();
  }
  
  //Returns the solution of the cell if it is not empty(the integer at position 0 in the list of candidates) or 0 if it is empty
  public int solutionOfCell() {
    if (!isEmpty()) return this.listOfCandidates.get(0);
    else return 0;
  }
  
  // Method that removes integer i from the list of candidates
  public void deleteInteger(int i) {
    if (includes(i)) {
      this.listOfCandidates.remove(this.listOfCandidates.indexOf(i)); // gets index of the int and removes it
    }
  }
  
  
  //Method that removes every candidate from the list and then add the candidate i
  public void deleteCandidatesExcept(int i) {
    this.listOfCandidates.clear();
    this.listOfCandidates.add(i);
  }
  
  
  //Method that removes every candidate from the list and then add the candidate with index i
  public void deleteCandidatesExceptIndex(int i) {
    this.temporary = this.listOfCandidates.get(i);
    this.listOfCandidates.clear();
    this.listOfCandidates.add(this.temporary);
  }
  
  // Returns a clone of the Cell
  @Override //overrides default, generic clone method from .Object
  public Cell clone() {
    return new Cell(candidatesNow(), place());
  }
}