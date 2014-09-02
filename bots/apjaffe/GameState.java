
public class GameState 
{
	private int[][] board;
	private int rows;
	private int cols;
	private int nextPlayer = 1;
	
	public GameState(int rows, int cols)
	{
		this.rows=rows;
		this.cols=cols;
		board = new int[rows][cols];
	}
	
	public GameState(int rows, int cols, int[][] board, int player)
	{
		this.rows=rows;
		this.cols=cols;
		this.board=board;
		this.nextPlayer = player;
	}
	
	public int numRows()
	{
		return rows;
	}
	public int numCols()
	{
		return cols;
	}
	public int nextPlayer()
	{
		return nextPlayer;
	}
	
	//1<=col<=cols
	public GameState makeMove(int col)
	{
		if(col<1 || col>cols)
			return null;
		
		col--;
		
		if(board[0][col]!=0)
			return null;
		
		int player = nextPlayer;
		
		GameState rtn = new GameState(rows,cols);
		rtn.board = new int[rows][cols];
		
		for(int j=0;j<cols;j++)
		{
			for(int i=rows-1;i>=0;i--)
			{
				if(col==j)
				{
					if(board[i][j]==0)
					{
						rtn.board[i][j] = player;
						break;
					}
					else
					{
						rtn.board[i][j] = board[i][j];
					}
				}
				else
				{
					if(board[i][j]==0)
						break;
					else
						rtn.board[i][j]=board[i][j];
				}
				
			}
		}
		rtn.nextPlayer = -this.nextPlayer;
		return rtn;
	}
	
	//taken from game.h
	public Integer winner()
	{
		boolean draw=true;
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<cols;j++)
			{
				if(board[i][j]==0)
				{
					draw=false;
				}
				else
				{
				
					if (i+4 <= rows) 
					{
		                if (board[i][j] == board[i+1][j] &&
		                    board[i][j] == board[i+2][j] &&
		                    board[i][j] == board[i+3][j])
		                    return board[i][j];
		            }
		            if (j+4 <= cols) 
		            {
		                if (board[i][j] == board[i][j+1] &&
		                    board[i][j] == board[i][j+2] &&
		                    board[i][j] == board[i][j+3])
		                    return board[i][j];
		            }
		            if (i+4 <= rows && j+4 <= cols) 
		            {
		                if (board[i][j] == board[i+1][j+1] &&
		                    board[i][j] == board[i+2][j+2] &&
		                    board[i][j] == board[i+3][j+3])
		                    return board[i][j];
		            }
		            if (i+4 <= rows && j >= 3) 
		            {
		                if (board[i][j] == board[i+1][j-1] &&
		                    board[i][j] == board[i+2][j-2] &&
		                    board[i][j] == board[i+3][j-3])
		                    return board[i][j];
		            }
				}
			}
		}
		if(draw)
			return 0;
		else
			return null;
	}
	
	//Single-threaded
	public String toString()
	{
		StringBuilder str = new StringBuilder();
		for(int i=0;i<rows;i++)
		{
			for(int j=0;j<cols;j++)
				str.append(playerName(board[i][j]));
			str.append("\n");
		}
		return str.toString();		
	}
	
	private int playerName(int orig)
	{
		if(orig==-1)
			return 2;
		else
			return orig;
	}
}
