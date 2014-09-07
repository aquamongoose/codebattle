import java.util.*;
public class Connect4 {

	private static Scanner scanner = new Scanner(System.in);


	public static void main(String[] args) {
		processInput();
		//PvAI();
	}	
		
	public static void processInput()
	{
		AI ai = new AI();
		int player = Integer.parseInt(scanner.nextLine());
		if(player==2)
			player=-1;
		
		int N = Integer.parseInt(scanner.nextLine());
		int[][] board = new int[N][N];
		for(int i=0;i<N;i++)
		{
			String str = scanner.nextLine();
			for(int j=0;j<N;j++)
			{
				char ch = str.charAt(j);
				if(ch=='.')
					board[i][j]=0;
				else if(ch=='1')
					board[i][j]=1;
				else if(ch=='2')
					board[i][j]=-1;
			}
		}
		GameState gs = new GameState(N,N,board, player);
		//System.out.println(gs.toString());
		System.out.println(ai.getMove(gs));
	}
	
		
	public static void PvP()
	{
		GameState gs = new GameState(6,7);
		int nextPlayer = 1;
		Integer winner = null;
		while(winner==null)
		{
			System.out.println(gs.toString());
			GameState next = null;

			do
			{
				System.out.println("Player " + nextPlayer+": ");
				int move = scanner.nextInt();
				next = gs.makeMove(move);
			}
			while(next==null);
			gs=next;
			winner = gs.winner();
			
			nextPlayer = -nextPlayer; //alternate 1 vs. -1
		}
		
		System.out.println(gs.toString());
		System.out.println("Game over: Result is " + winner);
	}
	
	public static void PvAI()
	{
		AI ai = new AI();
		int NUM_ROWS=6;
		int NUM_COLS=7;
		GameState gs = new GameState(NUM_ROWS,NUM_COLS);
		int nextPlayer = 1;
		Integer winner = null;
		while(winner==null)
		{
			System.out.println(gs.toString());
			GameState next = null;

			if(nextPlayer==1)
			{
			
				do
				{
					System.out.println("Human, enter column number (1-" + NUM_COLS+"): ");
					int move = scanner.nextInt();
					next = gs.makeMove(move);
				}
				while(next==null);
				gs=next;
			}
			else
			{
				gs = gs.makeMove(ai.getMove(gs));
			}
			winner = gs.winner();
			
			nextPlayer = -nextPlayer; //alternate 1 vs. -1
		}
		
		System.out.println(gs.toString());
		if(winner==1)
			System.out.println("Game over. First player wins.");
		else if(winner==-1)
			System.out.println("Game over. Second player wins.");
		else
			System.out.println("Game over. Draw.");
	}
}
