import java.util.Arrays;
public class AI 
{
	//private final int MAX_DEPTH = 1;
	private final int TIMEOUT = 4000;
	private final boolean DEBUG = false;
	
	public AI()
	{
	}
	
	public int getMove(GameState gs)
	{
		long startTime = System.currentTimeMillis();
		
		int cols = gs.numCols();
		for(int i=1;i<=cols;i++)
		{
			GameState next = gs.makeMove(i);
			if(next!=null)
			{
				Integer winner = next.winner();
				//System.out.println(i+":"+next.winner()+"\n"+next);
				if(winner==null || winner==0)
				{
				}
				else if(winner==gs.nextPlayer())
				{
					return i;
				}
			}
		}
		
		Integer bestMove = 1;
		for(int depth=1;;depth++)
		{
			try
			{
				Integer maybeBest = getBestMove(gs,depth,startTime);
				long endTime = System.currentTimeMillis();
				if(endTime-startTime>=TIMEOUT || maybeBest==null)
				{
					if(DEBUG)
						System.out.println("DEPTH: "+(depth-1)+"\tTIME:"+(endTime-startTime));
					return bestMove;
				}
				else
				{
					bestMove=maybeBest;
				}
			}
			catch(TimeoutException t)
			{
				long endTime = System.currentTimeMillis();
				if(DEBUG)
					System.out.println("DEPTH: "+(depth-1)+"\tTIME:"+(endTime-startTime));
				return bestMove;
			}
		}
	}
	
	//gets move, given gamestate
	private Integer getBestMove(GameState gs, int maxDepth, long startTime) throws TimeoutException
	{
		//return getDecentLegalMove(gs);
		
		
		
		int cols = gs.numCols();
		int[] colChoices = new int[cols];
		int bestMove = -1;
		double bestValue = -1;
		int player = gs.nextPlayer();
		for(int m=1;m<=cols;m++)
		{
			int i=(m-1+cols/2)%cols+1;
			GameState next = gs.makeMove(i);
			if(next!=null)
			{
				double val = player*evaluatePosition(next, -1, 1, maxDepth, startTime);
				if(bestMove==-1 || val>bestValue)
				{
					bestValue = val;
					bestMove = i;
				}
				
				if(DEBUG && maxDepth>=1)
				{
					System.out.println(maxDepth+"::"+i+":"+bestValue);
				}
				
				long endTime = System.currentTimeMillis();
				if(endTime-startTime>=TIMEOUT)
				{
					return null;
				}
			}
		}
		return bestMove;
		//return evaluatePosition(gs,-1,1);
		//return getRandomLegalMove(gs,player);
	}
	
	class Move implements Comparable
	{
		double value;
		int mv;
		public int player;
		public Move(int mv, double value, int player)
		{
			this.mv=mv;
			this.value=value;
			this.player=player;
		}
		public int compareTo(Object other)
		{
			double diff= this.value-((Move)other).value;
			if(diff>0)
				return 1*player;
			else if(diff<0)
				return -1*player;
			else
				return 0;
		}
	}
	
	//alpha is the highest score that p1 is guaranteed
	//beta is the lowest score that p2 is guaranteed
	public double evaluatePosition(GameState gs, double alpha, double beta, int maxDepth, long startTime) throws TimeoutException
	{
		Integer winner = gs.winner();
		if(winner==null)
		{
			if(maxDepth==0)
			{
				return evaluatePositionFinal(gs, maxDepth);
			}
			
			int cols = gs.numCols();
			//Move[] colChoices = new Move[cols];
			//int k=0;
			
			//should sort by whether they're promising
			
			
			for(int i=1;i<=cols;i++)
			{
				GameState next = gs.makeMove(i);
				if(next!=null)
				{
					Integer nextWinner = next.winner();
					if(nextWinner!=null)
					{
						return ((double)nextWinner.intValue())-((10-maxDepth)*0.001*nextWinner.intValue());
					}
					/*else
					{
						colChoices[i-1]=new Move(i, next.evaluateBoard(), gs.nextPlayer());
						k++;
					}*/
				}
			}
			
			
			//Arrays.sort(colChoices);
			
			/*for(int i=1;i<=cols;i++)
			{
				if(i<cols/2)
				{
					colChoices[
				}
			}*/
			
			//for(int mv=0;mv<k;mv++)
			for(int i=1;i<=cols;i++)
			{
				//int i=colChoices[mv].mv;
				GameState next = gs.makeMove(i);
				if(next!=null)
				{
					//legal move
					
					if(gs.nextPlayer()==1)
					{
						alpha=Math.max(alpha,evaluatePosition(next, alpha, beta, maxDepth-1, startTime));
						if(alpha>beta)
						{
							return 1;
						}
					}
					else
					{
						beta=Math.min(beta,evaluatePosition(next, alpha, beta, maxDepth-1, startTime));
						if(beta<alpha)
						{
							return -1;
						}
					}
				}
				
				long endTime = System.currentTimeMillis();
				if(endTime-startTime>=TIMEOUT)
				{
					throw new TimeoutException();
				}
			}
			if(gs.nextPlayer()==1)
			{
				return alpha;
			}
			else
			{
				return beta;
			}
		}
		else
		{
			return ((double)winner.intValue())-((10-maxDepth)*0.001*winner.intValue());
		}
	}
	
	public double evaluatePositionFinal(GameState gs, int maxDepth)
	{
		
		Integer winner = gs.winner();
		if(winner==null)
		{
			/*int NUM_RND=2;
			double sum = 0;
			for(int i=0;i<NUM_RND;i++)
			{
				sum+=finishGameRandomly(gs);
			}
			return sum/NUM_RND1*0.5;*/
			return gs.evaluateBoard();
		}
		else
			return ((double)winner.intValue())-((10-maxDepth)*0.001*winner.intValue());
	}
	
	public int finishGameRandomly(GameState gs)
	{
		Integer winner = gs.winner();
		while(winner==null)
		{
			gs = gs.makeMove(getObviousLegalMove(gs));
			winner = gs.winner();
		}
		return winner.intValue();
	}
	
	private int getDecentLegalMove(GameState gs)
	{
		int cols = gs.numCols();
		int[] colChoices = new int[cols];
		int[] goodColChoices = new int[cols];
		int k=0;
		int goodK=0;
		for(int i=1;i<=cols;i++)
		{
			GameState next = gs.makeMove(i);
			if(next!=null)
			{
				Integer winner = next.winner();
				if(winner==null || winner==0)
				{
					if(winner==null)
					{
						GameState predict = next.makeMove(getObviousLegalMove(next));
						Integer thenWinner = predict.winner();
						if(thenWinner==null || thenWinner==0)
						{
							goodColChoices[goodK]=i;
							goodK++;
						}
					}
					else
					{
						goodColChoices[goodK]=i;
						goodK++;
					}
					
					colChoices[k]=i;
					k++;
					
				}
				else if(winner==gs.nextPlayer())
				{
					return i;
				}
			}
		}
		if(goodK>0)
			return goodColChoices[(int)(Math.random()*goodK)];
		else
			return colChoices[(int)(Math.random()*k)];
	}
	
	private int getObviousLegalMove(GameState gs)
	{
		int cols = gs.numCols();
		int[] colChoices = new int[cols];
		int k=0;
		for(int i=1;i<=cols;i++)
		{
			GameState next = gs.makeMove(i);
			if(next!=null)
			{
				Integer winner = next.winner();
				if(winner==null || winner==0)
				{
					colChoices[k]=i;
					k++;
					
				}
				else if(winner==gs.nextPlayer())
				{
					return i;
				}
			}
		}
		return colChoices[(int)(Math.random()*k)];
	}
}
