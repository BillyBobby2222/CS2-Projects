/* Caleb Andrews
Cosmic Concordance
COP3503 Computer Science 2
CosmicConcord.java
*/

import java.util.*;

public class CosmicConcord {

  //memoization table
  static int[][][] memo;

  //problem parameters
  static int N, M, D, G, K;
  static int[] A, B;

  //initializes variables, runs approaches, prints results
  public static void solve(int[] Aarr, int[] Barr, int n, int m, int d, int g, int k) {
    A = Aarr;
    B = Barr;
    N = n;
    M = m;
    D = d;
    G = g;
    K = k;

    //initialize memo table
    memo = new int[N][M][K + 1];
    for (int[][] mat : memo) {
      for (int[] row : mat) {
        Arrays.fill(row, -1);
      }
    }
    int recAns = 0;

    //try valid starting matches
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        if (Math.abs(A[i] - B[j]) <= D) {
          recAns = Math.max(recAns, 1 + solveRecMemo(i, j, 0));
        }
      }
    }
    int tabAns = solveTab();

    //print results
    System.out.println("Maximum length using Recursion (with Memoization): " + recAns);
    System.out.println("Maximum length using Tabulation: " + tabAns);
  }

  //recursive DP with memoization
  private static int solveRecMemo(int i, int j, int dips) {
  
    if (memo[i][j][dips] != -1) {
        return memo[i][j][dips];
    }
    int best = 0;

    //try all possible pairs
    for (int ni = i + 1; ni < N; ni++) {
      for (int nj = j + 1; nj < M; nj++) {
        //check matching tolerance
        if (Math.abs(A[ni] - B[nj]) <= D) {
          int newDips = dips;
          
          //check if step is rising
          if (!(A[ni] - A[i] >= G && B[nj] - B[j] >= G)) {
            newDips++;
          }
          
          //only proceed if dips are within limit
          if (newDips <= K) {
            best = Math.max(best, 1 + solveRecMemo(ni, nj, newDips));
          }
        }
      }
    }

    memo[i][j][dips] = best;
    return best;
  }

  //bottom-up DP (tabulation) approach.
  private static int solveTab() {
    
    int[][][] dp = new int[N][M][K + 1];
    int ans = 0;
    
    //initialize valid matches as length 1
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        if (Math.abs(A[i] - B[j]) <= D) {
          for (int k = 0; k <= K; k++) {
            dp[i][j][k] = 1;
              ans = Math.max(ans, 1);
          }
        }
      }
    }

    //build DP table
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < M; j++) {
        if (Math.abs(A[i] - B[j]) > D) {
          continue;
        }
        
        for (int k = 0; k <= K; k++) {
          if (dp[i][j][k] == 0) {
            continue;
          }
          
          //try extending the sequence
          for (int ni = i + 1; ni < N; ni++) {
            for (int nj = j + 1; nj < M; nj++) {
              if (Math.abs(A[ni] - B[nj]) <= D) {
                int newDips = k;
                //check if step is rising
                if (!(A[ni] - A[i] >= G && B[nj] - B[j] >= G)) {
                  newDips++;
                }
                if (newDips <= K) {
                  dp[ni][nj][newDips] = Math.max(dp[ni][nj][newDips], dp[i][j][k] + 1);
                  ans = Math.max(ans, dp[ni][nj][newDips]);
                }
              }
            }
          }
        }
      }
    }
    return ans;
  }
}