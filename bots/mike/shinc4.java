import java.util.*;
/*
  * Right now, the program is going to search an even number of levels. Meaning that the pureEval will
  * always evaluate a situation right after the program has "moved". It then takes all these possible values
  *
*/
public class shinc4 {
  public static int SEARCH_DEPTH = 2;
  public static int NO_CON = 10000;
  public static int ABS_CON = 10001;
  public static double C4_VAL = 100;
  public static double C3_VAL = 1;
  public static double C2_VAL = 0.1;
  public static int myturn;
  public static boolean DEBUG = true;

  public static void main(String ... args) {
    if (args.length > 0) {
      if (args[0].equals("-d")) {
        DEBUG = true;
      } else {
        System.err.println("Only parameter that exists is -d for debug mode.");
        System.exit(0);
      }
    } else {
      DEBUG = false;
    }

    Scanner asdf = new Scanner(System.in);
    int turn = Integer.parseInt(asdf.nextLine());
    myturn = turn;
    int len = Integer.parseInt(asdf.nextLine());
    String[][] board = new String[len][len];
    for (int i = 0; i < board.length; i++) {
      String[] line = asdf.nextLine().split("");
      for (int j = 1; j < line.length; j++) {
        board[i][j - 1] = line[j];
      }
    }
    run(board, turn);
  }
  public static void test(String[][] board) {
    simTest(board);
    countTest(board);

  }
  public static void run(String[][] board, int turn) {
    int result = search(board, turn);
    if (DEBUG) {
      System.out.println(result);
    } else {
      System.out.println(result + 1);
    }

    /* PLEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEASE GUY REMEMBER TO CHANGE THIS OK?
    ?
    ?
    ?
    ?
    ?
    ?
    ?
    ?
    OKKKKKKKKKKKKKKKKKKKKKKKKK
    */

  }
  public static void simTest(String[][] board) {
    String avail = Integer.toString(availableMoves(board), 2);
    for (int i = 0; i < avail.length(); i++) {
      if (avail.charAt(i) == '1') {
        System.out.println("SIMS FOR i = " + i);
        printMatrix(simMove(board, i, 1));
        printMatrix(simMove(board, i, 2));
      }
    }
  }
  public static void countTest(String[][] board) {
    for (int i = 1; i <= 4; i++) {
      System.out.println("Count Test for i = " + i + ", turn=1: " + countChains(board, 1, i));
      System.out.println("Count Test for i = " + i + ", turn=2: " + countChains(board, 2, i));
    }
  }
  public static void printMatrix(String[][] board) {
    if (DEBUG) {
      for (String[] s : board) {
        for (String t : s) {
          System.out.print(t);
        }
        System.out.println("");
      }
    }
  }
  public static int search(String[][] board, int turn) {
    int i = availableMoves(board);
    String m = Integer.toString(i, 2);
    double[] scores = new double[board[0].length];
    for (int k = 0; k < m.length(); k++) {
      if (m.charAt(k) == '0') {
        scores[k] = NO_CON;
      } else {
        scores[k] = eval(simMove(board, k, turn), turn, SEARCH_DEPTH - 1);
        if (scores[k] == -2) {
          scores[k] = NO_CON;
        }
        if (scores[k] == -4) {
          if (DEBUG) {
            System.out.println("Absolute Victory Found");
          }
          return k;
        }
      }
      if (DEBUG) {
        System.out.println(k + ": " + scores[k]);

      }
    }
    int maxIndex = -1;
    for (int k = 0; k < scores.length; k++) {
      if (scores[k] != NO_CON) {
        maxIndex = k;
        break;
      }
    }
    for (int k = 0; k < scores.length; k++) {
      if (scores[k] != NO_CON && scores[k] > scores[maxIndex]) {
        maxIndex = k;
      }
    }
    if (maxIndex == -1) {
      return (int)(Math.random() * board.length);
    }
    ArrayList<Integer> maxes = new ArrayList<Integer>();
    for (int k = 0; k < scores.length; k++) {
      if (scores[k] == scores[maxIndex]) {
        maxes.add(k);
      }
    }
    return maxes.get((int)(Math.random() * maxes.size()));
  }

  public static String[][] simMove(String[][] board, int move, int turn) {
    String[][] ret = new String[board.length][];
    for (int i = 0; i < board.length; i++) {
      ret[i] = new String[board[0].length];
      System.arraycopy(board[i], 0, ret[i], 0, board[i].length);
    }
    for (int i = ret.length - 1; i >= 0; i--) {
      if (ret[i][move].equals(".")) {
        ret[i][move] = "" + turn;
        break;
      }
    }
    return ret;
  }

  public static int availableMoves(String[][] board) {
    String m = "";
    for (int i = 0; i < board[0].length; i++) {
      if (board[0][i].equals(".")) {
        m += "1";
      } else {
        m += "0";
      }
    }
    return Integer.parseInt(m, 2);
  }
  public static double pureEval(String[][] board, int turn) {
    // int c4 = countChains(board, turn, 4);

    int c3 = countChains(board, turn, 3);
    int c2 = countChains(board, turn, 2);
    return c2 * C2_VAL + c3 * C3_VAL; //+ c4 * C4_VAL;
  }

  /*
    * The parameters in the function can be a little tricky. The int turn represents the person who's move it
    * currently is. Therefore when pureeval happens, myturn != turn

    DEPRECATED ^^^^^

    For real now, turn is the turn of the person who literally just moved. Makes so much more sense

    Im going to try some array return type so that i can pass more data up the call chain.
    LOL maybe not

    If his move just made him win, return -3

    If my move just made me win, return -4
  */
  public static double eval(String[][] board, int turn, int depth) {
    int is4 = countChains(board, turn, 4);

    // printMatrix(board);
    // System.out.println(is4);

    if (is4 > 0 && turn != myturn) {
      // System.out.println("Neg");
      return -3;
    }
    if (is4 > 0 && turn == myturn) {
      // System.out.println("Negg");
      return -4;
    }
    if (depth == 0) {
      double mscore = pureEval(board, myturn);
      double hscore = -1 * pureEval(board, 3 - myturn);
      return mscore + hscore;
    }
    String sMoves = Integer.toString(availableMoves(board), 2);
    double[] scores = new double[sMoves.length()];
    int moveCount = 0;
    for (int i = 0; i < sMoves.length(); i++) {
      if (sMoves.charAt(i) == '0') {
        scores[i] = NO_CON;
      } else {
        moveCount ++;
        scores[i] = eval(simMove(board, i, 3-turn), 3 - turn, depth - 1);
      }
    }
    if (turn == myturn) {
      double sum = 0;
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] == -3) {
          return -2;
        }
        if (scores[i] != NO_CON) {
          sum += scores[i];
        }
      }
      sum /= moveCount;
      return sum;
    } else {
      double sum = 0;
      boolean quit = true;
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] != -2) {
          quit = false;
        }
      }
      if (quit) {
        return -3;
      }
      /*
        * Here, I could just remove from consideration all -2 results but I think this is a bad idea for now.
        * Definitely subject to change.
      */
      // for (int i = 0; i < scores.length; i++) {
      //   if (scores[i] == -2) {
      //     scores[i] = NO_CON;
      //   }
      // }
      for (int i = 0; i < scores.length; i++) {
        if (scores[i] != NO_CON) {
          sum += scores[i];
        }
      }
      sum /= moveCount;
      return sum;
    }







    /*
    if (depth == 0) {
      return pureEval(board, turn);
    } else {
      int moves = availableMoves(board);
      String sMoves = Integer.toString(moves, 2);
      int[] scores = new int[sMoves.length()];
      if (moves > 0) {
        for (int i = 0; i < sMoves.length(); i++) {
          if (sMoves.charAt(i) == '0') {
            scores[i] = -1;
          } else {
            scores[i] = eval(simMove(board, i, turn), 3 - turn, depth - 1);
          }
        }
        int maxIndex = 0;
        for (int k = 1; k < scores.length; k++) {
          if (scores[k] > scores[maxIndex]) {
            maxIndex = k;
          }
        }
        return scores[maxIndex];
      } else {
        return 0;
      }
    }
    */
  }
  public static int countChains(String[][] board, int turn, int clen) {
    int num = 0;
    int NUL = 500;
    Stack<String> toProcess = new Stack<String>();
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        if (board[i][j].equals("" + turn)) {
          //Format of this string is (xcor ycor current_length dx dy)
          toProcess.push(String.format("%d %d %d %d %d", i, j, 1, NUL, NUL));
        }
      }
    }
    while (!toProcess.isEmpty()) {
      String s = toProcess.pop();
      String[] params = s.split(" ");
      int x = Integer.parseInt(params[0]);
      int y = Integer.parseInt(params[1]);
      int len = Integer.parseInt(params[2]);
      int dxc = Integer.parseInt(params[3]);
      int dyc = Integer.parseInt(params[4]);
      if (len == clen) {
        num++;
        continue;
      }
      int[] _dx = new int[] {0, 1};
      int[] _dy = new int[] { -1, 0, 1};
      if (dxc == NUL && dyc == NUL) {
        for (int dx : _dx) {
          for (int dy : _dy) {
            if (!(dy == 0 && dx == 0) && !(dx == 0 && dy == 1)) {
              boolean xbounds = 0 <= x + dx && x + dx < board.length;
              boolean ybounds = 0 <= y + dy && y + dy < board[0].length;
              if (xbounds && ybounds && board[x + dx][y + dy].equals("" + turn)) {
                //System.out.println("Pushed back in with x=" + x + " y=" + y + " dx=" + dx + " dy=" + dy);
                toProcess.push(String.format("%d %d %d %d %d", x + dx, y + dy, len + 1, dx, dy));
              }
            }
          }
        }
      } else {
        boolean xbounds = 0 <= x + dxc && x + dxc < board.length;
        boolean ybounds = 0 <= y + dyc && y + dyc < board[0].length;
        if (xbounds && ybounds && board[x + dxc][y + dyc].equals("" + turn)) {
          //System.out.println("Pushed back in with x=" + x + " y=" + y + " dx=" + dx + " dy=" + dy);
          toProcess.push(String.format("%d %d %d %d %d", x + dxc, y + dyc, len + 1, dxc, dyc));
        }
      }
    }
    return num;
  }

  /*
    public static boolean chainExists(String[][] board, int turn, int clen) {
      Stack<String> toProcess = new Stack<String>();
      for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[0].length; j++) {
          if (board[i][j].equals("" + turn)) {
            toProcess.push(String.format("%d %d %d", i, j, 0));
          }
        }
      }
      while (!toProcess.isEmpty()) {
        String s = toProcess.pop();
        String[] params = s.split(" ");
        int x = Integer.parseInt(params[0]);
        int y = Integer.parseInt(params[1]);
        int len = Integer.parseInt(params[2]);
        if (len == clen) {
          return true;
        }
        if (x < board.length - 1 && board[x + 1][y].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x + 1, y, len + 1));
        } else if (x < board.length - 1 && y < board[0].length - 1 && board[x + 1][y + 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x + 1, y + 1, len + 1));
        } else if (y < board[0].length - 1 && board[x][y + 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x , y + 1, len + 1));
        } else if (x > 0 && y < board[0].length - 1 && board[x - 1][y + 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x - 1, y + 1, len + 1));
        } else if (x > 0 && board[x - 1][y].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x - 1, y, len + 1));
        } else if (x > 0 && y > 0 && board[x - 1][y - 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x - 1, y - 1, len + 1));
        } else if (y > 0 && board[x][y - 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x , y - 1, len + 1));
        } else if (x < board.length - 1 && y > 0 && board[x + 1][y - 1].equals("" + turn)) {
          toProcess.push(String.format("%d %d %d", x + 1, y - 1, len + 1));
        }
      }
      return false;
    }
    */
  /*
  public static String[][] parseInput() {
    Scanner asdf = new Scanner(System.in);
    int turn = Integer.parseInt(asdf.nextLine());
    int len = Integer.parseInt(asdf.nextLine());
    String[][] board = new String[len][len];
    for (int i = 0; i < board.length; i++) {
      String[] line = asdf.nextLine().split("");
      for (int j = 1; j < line.length; j++) {
        board[i][j - 1] = line[j];
      }
    }
    return board;
  }
  */
}




