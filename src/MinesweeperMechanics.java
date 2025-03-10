import java.util.ArrayList;

public class MinesweeperMechanics
{
    private final int width;
    private final int height;
    private final int mineNum;
    private boolean isPresolved;
    private int[][] mineBoard;
    private char[][] discoveryBoard;
    private ArrayList<int[]> emptyTiles;

    public MinesweeperMechanics(int width, int height, int mineNum)
    {
        this.width = width;
        this.height = height;
        this.mineNum = mineNum;
        this.isPresolved = false;
        mineBoard = new int[height][width];
        discoveryBoard = new char[height][width];
        emptyTiles = new ArrayList<>();
        populateBoard();
        openInitialTiles();
        if(emptyTiles.isEmpty())
        {
            System.out.println("Board is pre-solved. Make a new board");
            isPresolved = true;
        }
    }
    private void populateBoard()
    {
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                int[] temp = {j,i};
                emptyTiles.add(temp);
                discoveryBoard[i][j] = '.';
            }
        }
        int minesLeft = mineNum;
        while(minesLeft > 0)
        {
            int mineIndex = (int)(Math.random()*emptyTiles.size());
            int[] tile = emptyTiles.get(mineIndex);
            mineBoard[tile[1]][tile[0]] = -1;
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
    private boolean containsTile(int x, int y, ArrayList<int[]> tiles)
    {
        for(int[] tile : tiles)
        {
            if(tile[0] == x && tile[1] == y)
                return true;
        }
        return false;
    }
    private void openInitialTiles()
    {
        ArrayList<int[]> zeroTiles = new ArrayList<>();
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(mineBoard[i][j] == 0 && !containsTile(j, i, zeroTiles))
                {
                    
                    ArrayList<int[]> tempZeroTiles = new ArrayList<>();
                    int[] sumArr = {1,-1};
                    int[] startingPos = {j,i};
                    tempZeroTiles.add(startingPos);
                    findZeroTileAmount(j, i, tempZeroTiles);
                    int sum = tempZeroTiles.size();
                    for(int[] tile : tempZeroTiles)
                    {
                        int[] newTile = {tile[0],tile[1],sum};
                        zeroTiles.add(newTile);
                    }
                }
            }
        }
        int max = 0;
        int maxIndex = 0;
        for(int i = 0; i < zeroTiles.size(); i++)
        {
            if(zeroTiles.get(i)[2] > max)
            {
                max = zeroTiles.get(i)[2];
                maxIndex = i;
            }
        }
        if(zeroTiles.isEmpty())
            return;
        int startingHeight = zeroTiles.get(maxIndex)[1];
        int startingWidth = zeroTiles.get(maxIndex)[0];
        discoveryBoard[startingHeight][startingWidth] = '0';
        removeFromUncheckedList(startingWidth, startingHeight);
        openOtherOpenTiles(startingWidth, startingHeight);
    }
    private void removeFromUncheckedList(int x, int y)
    {
        for(int j = 0; j < emptyTiles.size(); j++)
        {
            if(emptyTiles.get(j)[1] == y && emptyTiles.get(j)[0] == x)
                {
                    emptyTiles.remove(j);
                    break;
                }
        }
    }
    private void openOtherOpenTiles(int x, int y)
    {
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
            if((p[0] >= 0 && p[0] < width) && (p[1] >= 0 && p[1] < height) && discoveryBoard[p[1]][p[0]] == '.')
            {
                discoveryBoard[p[1]][p[0]] = Character.forDigit(mineBoard[p[1]][p[0]],10);
                removeFromUncheckedList(p[0], p[1]);
                if(mineBoard[p[1]][p[0]] == 0)
                {
                    openOtherOpenTiles(p[0], p[1]);
                }
            }
        }
    }
    private void findZeroTileAmount(int x, int y, ArrayList<int[]> zeroTiles)
    {
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
            if((p[0] >= 0 && p[0] < width) && (p[1] >= 0 && p[1] < height) && mineBoard[p[1]][p[0]] == 0 && !containsTile(p[0], p[1], zeroTiles))
            {
                int[] temp = {p[0],p[1]};
                if(mineBoard[p[1]][p[0]] == 0)
                {
                    zeroTiles.add(temp);
                    findZeroTileAmount(p[0], p[1], zeroTiles);
                }
            }
        }
    }
    
    public boolean uncoverTile(int x, int y)
    {
        if(mineBoard[y][x] == -1)
        {
            System.out.println("\nKABOOM!\n");
            System.out.println("\nGAME OVER!\n");
            revealAllMines();
            printDiscoveredBoard();
            return true;
        }
        else if(mineBoard[y][x] == 0)
        {
            removeFromUncheckedList(x, y);
            discoveryBoard[y][x] = '0';
            openOtherOpenTiles(x, y);
            printDiscoveredBoard();
        }
        else
        {
            removeFromUncheckedList(x, y);
            discoveryBoard[y][x] = Character.forDigit(mineBoard[y][x], 10);
            printDiscoveredBoard();
        }
        if(emptyTiles.isEmpty())
        {
            System.out.println("\nCongratulations! You won!\n");
            return true;
        }
        return false;
    }
    private void revealAllMines()
    {
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(mineBoard[i][j] == -1)
                {
                    discoveryBoard[i][j] = 'B';
                }
            }
        }
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
    public boolean getIsPresolved()
    {
        return isPresolved;
    }
    public void printDiscoveredBoard()
    {
        System.out.print("     ");
        for(int i = 0; i < width; i++)
        {
            System.out.print(i%10 + " ");
        }
        System.out.println();
        for(int i = 0; i < height; i++)
        {
            if(i < 10)
                System.out.print(i + "   |");
            else
                System.out.print(i + "  |");
            for(int j = 0; j < width; j++)
            {
                System.out.print(discoveryBoard[i][j] + "|");
            }
            System.out.println();
        }
    }
    public void printFullBoard()
    {
        System.out.print("     ");
        for(int i = 0; i < width; i++)
        {
            System.out.print(i%10 + " ");
        }
        System.out.println();
        for(int i = 0; i < height; i++)
        {
            if(i < 10)
                System.out.print(i + "   |");
            else
                System.out.print(i + "  |");
            for(int j = 0; j < width; j++)
            {
                if(mineBoard[i][j] == -1)
                    System.out.print("+" + "|");
                else
                    System.out.print(mineBoard[i][j] + "|");
            }
            System.out.println();
        }
    }
}