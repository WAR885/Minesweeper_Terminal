import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class TerminalInteferace {
    public static void runMinesweeper()
    {
        try(Scanner reader = new Scanner(System.in))
        {
            MinesweeperMechanics minesweeper = null;
            System.out.println("Hello and welcome to minesweeper!");
            while(true)
            {
                System.out.println("Choose which mode you wish: easy, medium, hard, or custom");
                String ans = reader.nextLine();
                ans = ans.toLowerCase();
                boolean restart = false;
                switch(ans)
                {
                    case "easy": 
                        minesweeper = new MinesweeperMechanics(8,8,10);
                        break;
                    case "medium":
                        minesweeper = new MinesweeperMechanics(16,16,40);
                        break;
                    case "hard":
                        minesweeper = new MinesweeperMechanics(30,16,99);
                        break;
                    case "custom":
                        minesweeper = customBoard(reader);
                        if(minesweeper.getIsPresolved())
                            return;
                        break;
                    default:
                        System.out.println("\nSorry, your answer does not match the selected format, please try again\n");
                        restart = true;
                        break;
                }
                if(restart)
                    continue;
                break;

            }
            System.out.println("Starting Board: ");
            minesweeper.printDiscoveredBoard();
            while(true)
            {
                System.out.println("Please choose your move.(Choose option if you need help)");
                String move = reader.nextLine();
                if(move.equals("option"))
                {
                    System.out.println("To click on a tile, type:");
                    System.out.println("uncover [x pos,y pos]");
                    continue;
                }
                int[] pos = parseMove(move,minesweeper);
                if(pos != null)
                {
                    if(minesweeper.uncoverTile(pos[0],pos[1]))
                        break;
                }
                
            }
        }
        
    }
    private static MinesweeperMechanics customBoard(Scanner reader)
    {
        int width = 0;
        int height = 0;
        int mineNum = 0;
        while(true)
        {
            System.out.println("Type board orientation like: [width, height, number of mines]");
            String board = reader.nextLine();
            if (!board.matches("\\[\\d+,\\d+,\\d+\\]"))
            {
                System.out.println("\nThe answer does not match the selected format, please try again\n");
                continue;
            }
            board = board.substring(1,board.length()-1);
            String[] tempArr = board.split(",");
            width = Integer.parseInt(tempArr[0]);
            height = Integer.parseInt(tempArr[1]);
            mineNum = Integer.parseInt(tempArr[2]);
            if(mineNum > (width*height/2))
            {
                System.out.println("\n Board has too many mines, please try again.\n");
                continue;
            }
            else if(height <= 0 || width <= 0 || mineNum <= 0)
            {
                System.out.println("\n board invalid, please try again.\n");
                continue;
            }
            break;
        }
        return new MinesweeperMechanics(width, height, mineNum);
    }
    private static int[] parseMove(String move, MinesweeperMechanics minesweeper)
    {
        if (!move.matches("move \\[\\d+,\\d+\\]"))
        {
            System.out.println("\nThe answer does not match the selected format, please try again\n");
            return null;
        }
        Pattern pattern = Pattern.compile("\\d+,\\d+");
        Matcher matcher = pattern.matcher(move);
        if(matcher.find())
        {
            move = matcher.group();
        }
        String[] tempArr = move.split(",");
        int width = Integer.parseInt(tempArr[0]);
        int height = Integer.parseInt(tempArr[1]);
        if((width < 0 || width >= minesweeper.getWidth()) || (height < 0 || height >= minesweeper.getHeight()))
        {
            System.out.println("Move was out of bounds, please try again");
            return null;
        }
        int[] pos = {width,height};
        return pos;
    }
}
