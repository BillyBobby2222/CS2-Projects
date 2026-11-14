/* Caleb Andrews
Mystic Sudoku
COP3503 Computer Science 2
SudokuSolver.java
*/

public class SudokuSolver {
  private int[][] board;
  private int[][] forbiddenPairs;
  private int solutionCount;
  private int[][] firstSolution;
    
  //knight move offsets
  private static final int[] KNIGHT_ROW_OFFSETS = {-2, -2, -1, -1, 1, 1, 2, 2};
  private static final int[] KNIGHT_COL_OFFSETS = {-1, 1, -2, 2, -2, 2, -1, 1};
  
  //up, down, left, right offsets 
  private static final int[] ORTHO_ROW_OFFSETS = {-1, 1, 0, 0};
  private static final int[] ORTHO_COL_OFFSETS = {0, 0, -1, 1};
    
  public int solve(int[][] board, int[][] forbiddenPairs) {
    this.board = board;
    this.forbiddenPairs = forbiddenPairs;
    this.solutionCount = 0;
    this.firstSolution = new int[9][9];
        
    //backtrack from first cell
    backtrack(0, 0);
        
    //if one solution, print to board
    if (solutionCount == 1) {
      for (int i = 0; i < 9; i++) {
        for (int j = 0; j < 9; j++) {
          board[i][j] = firstSolution[i][j];
        }
      }
    }
    return solutionCount;
  }
    
  private void backtrack(int row, int col) {  
    //if end of the board, found a solution
    if (row == 9) {
      solutionCount++;
      //save first solution
      if (solutionCount == 1) {
        for (int i = 0; i < 9; i++) {
          for (int j = 0; j < 9; j++) {
            firstSolution[i][j] = board[i][j];
          }
        }
      }
      return;
    }
        
    //calculate next cell position
    int nextRow = (col == 8) ? row + 1 : row;
    int nextCol = (col == 8) ? 0 : col + 1;
        
    //if current cell is filled, next cell
    if (board[row][col] != 0) {
      backtrack(nextRow, nextCol);
      return;
    }
        
    //try each digit 1 to 9
    for (int num = 1; num <= 9; num++) {
      if (isValid(row, col, num)) {
        board[row][col] = num;
        backtrack(nextRow, nextCol);
        board[row][col] = 0; //backtrack
      }
    }
  }
    
  private boolean isValid(int row, int col, int num) {
    //check row constraint
    if (!checkRow(row, num)) {
      return false;
    }
        
    //check column constraint
    if (!checkColumn(col, num)) {
      return false;
    }
        
    //check 3x3 constraint
    if (!checkSubgrid(row, col, num)) {
      return false;
    }
        
    //check knight's move constraint
    if (!checkKnightMove(row, col, num)) {
      return false;
    }
        
    //check forbidden pairs constraint
    if (!checkForbiddenPairs(row, col, num)) {
      return false;
    }
    return true;
  }
    
  private boolean checkRow(int row, int num) {
    for (int col = 0; col < 9; col++) {
      if (board[row][col] == num) {
        return false;
      }
    }
    return true;
  }
    
  private boolean checkColumn(int col, int num) {
    for (int row = 0; row < 9; row++) {
      if (board[row][col] == num) {
        return false;
      }
    }
    return true;
  }
    
  private boolean checkSubgrid(int row, int col, int num) {
    //calculate starting position of 3x3
    int startRow = (row / 3) * 3;
    int startCol = (col / 3) * 3;
        
    for (int i = startRow; i < startRow + 3; i++) {
      for (int j = startCol; j < startCol + 3; j++) {
        if (board[i][j] == num) {
          return false;
        }
      }
    }
    return true;
  }
    
  private boolean checkKnightMove(int row, int col, int num) {
    //check 8 possible moves with wrap-around
    for (int i = 0; i < 8; i++) {
      int newRow = row + KNIGHT_ROW_OFFSETS[i];
      int newCol = col + KNIGHT_COL_OFFSETS[i];
            
      //apply wrap-around
      newRow = (newRow + 9) % 9;
      newCol = (newCol + 9) % 9;
            
      //check if knight's move position has same number
      if (board[newRow][newCol] == num) {
        return false;
      }
    }
    return true;
  }
    
  private boolean checkForbiddenPairs(int row, int col, int num) {
    //check 4 adjacent cells with wrap-around
    for (int i = 0; i < 4; i++) {
      int newRow = row + ORTHO_ROW_OFFSETS[i];
      int newCol = col + ORTHO_COL_OFFSETS[i];
            
      //apply wrap-around
      newRow = (newRow + 9) % 9;
      newCol = (newCol + 9) % 9;
            
      int adjacentValue = board[newRow][newCol];
            
      //if adjacent cell empty, no constraint
      if (adjacentValue == 0) {
        continue;
      }
            
      //check for forbidden pair
      if (isForbiddenPair(num, adjacentValue)) {
        return false;
      }
    }
    return true;
  }
    
  private boolean isForbiddenPair(int a, int b) {
    if (forbiddenPairs == null) {
      return false;
    }
        
    for (int i = 0; i < forbiddenPairs.length; i++) {
      int first = forbiddenPairs[i][0];
      int second = forbiddenPairs[i][1];
            
      //check both orderings since pairs are unordered
      if ((a == first && b == second) || (a == second && b == first)) {
        return true;
      }
    }
    return false;
  }
}