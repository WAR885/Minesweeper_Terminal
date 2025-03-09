import java.util.ArrayList;

public class MinesweeperMechanics
{
    private int width;
    private int height;
    private int mineNum;
    private int[][] mineBoard;
    private char[][] discoveryBoard;
    private ArrayList<int[]> emptyTiles;

    public MinesweeperMechanics(int width, int height, int mineNum)
    {
        this.width = width;
        this.height = height;
        this.mineNum = mineNum;
        mineBoard = new int[height][width];
        discoveryBoard = new char[height][width];
        emptyTiles = new ArrayList<>();
        populateBoard();
    }
    private void populateBoard()
    {
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                int[] temp = {i,j};
                emptyTiles.add(temp);
                discoveryBoard[i][j] = '.';
            }
        }
        int minesLeft = mineNum;
        while(minesLeft > 0)
        {
            int mineIndex = (int)(Math.random()*emptyTiles.size());
            int[] tile = emptyTiles.get(mineIndex);
            mineBoard[tile[0]][tile[1]] = -1;
            emptyTiles.remove(mineIndex);
            minesLeft--;
        }
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(mineBoard[i][j] != -1)
                {
                    mineBoard[i][j] = getMinesAroundTile(j, i);
                }
            }
        }
    }
    private int getMinesAroundTile(int x, int y)
    {
        int numOfMines = 0;
        int[][] pos = 
        {
            {x-1,y},
            {x+1,y},
            {x,y-1},
            {x,y+1},
            {x+1,y+1},
            {x-1,y-1},
            {x-1,y+1},
            {x+1,y-1}
        };
        for (int[] p : pos) 
        {
            if((p[0] >= 0 && p[0] < width) && (p[1] >= 0 && p[1] < height) && mineBoard[p[1]][p[0]] == -1)
            {
                numOfMines++;
            }
        }
        return numOfMines;
    }
    public int getHeight()
    {
        return height;
    }
    public int getWidth()
    {
        return width;
    }
    public int getNumMines()
    {
        return mineNum;
    }
}