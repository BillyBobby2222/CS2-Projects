/*  Caleb Andrews
    Sensor Range
    COP3503 Computer Science 2
    SensorRange.java
*/

import java.util.Arrays;

public class SensorRange {

  //brute force solution
  public static int[] rangeCountBF(int[] readings, int[][] queries){
    //result array for query count
    int[] results = new int[queries.length];
    
    //indendently go through each query
    for (int i = 0; i < queries.length; i++){
      int L = queries[i][0];
      int R = queries[i][1];
      int count = 0;
      
      //invalid range
      if (L > R){
        results[i] = 0;
        continue;
      }
      
      //look through readings and count the ones in range
      for (int j = 0; j < readings.length; j++){
        if (readings[j] >= L && readings[j] <= R){
          count++;
        }
      }
      results[i] = count;
    }
    return results;
  }
  
  //optimized solution
  public static int[] rangeCountFast(int[] readings, int[][] queries){
  
    //creating second array to not modify the original
    int[] sortedReadings = readings.clone();
    Arrays.sort(sortedReadings);
    
    //result array for query count
    int[] results = new int[queries.length];
    
    //go through queries using binary search
    for (int i = 0; i < queries.length; i++){
      int L = queries[i][0];
      int R = queries[i][1];
      
      //invalid range
      if (L > R){
        results[i] = 0;
        continue;
      }
      
      //first index >= L (leftmost)
      int leftIndex = findLeftmostIndex(sortedReadings, L);
      
      //last index <= R (rightmost)
      int rightIndex = findRightmostIndex(sortedReadings, R);
      
      //if no indexes
      if (leftIndex == -1 || rightIndex == -1 || leftIndex > rightIndex){
        results[i] = 0;
      } else {
        results[i] = rightIndex - leftIndex + 1;
      }
    }
    return results;
  }
  
  //helper for leftmost index arr[index] >= target
  public static int findLeftmostIndex(int[] arr, int target){
    int left = 0;
    int right = arr.length - 1;
    int result = -1;
        
    while (left <= right){
      int mid = left + (right - left) / 2;
            
      if (arr[mid] >= target){
        result = mid;  //search left for earlier occurrence
        right = mid - 1;
        } else {
          left = mid + 1;  //target is to the right
        }
      }
      return result;
    }
    
  // helper for rightmost index arr[index] <= target
  public static int findRightmostIndex(int[] arr, int target){
    int left = 0;
    int right = arr.length - 1;
    int result = -1;
        
    while (left <= right){
      int mid = left + (right - left) / 2;
            
      if (arr[mid] <= target){
        result = mid;  //search right for later occurrence
        left = mid + 1;
      } else {
        right = mid - 1;  //target is to the left
      }
    }
    return result;
  }
}














