public class App {
    public static void main(String[] args) throws Exception {
        MinesweeperMechanics mine = new MinesweeperMechanics(25, 25, 250);
        mine.printDiscoveredBoard();
    }
}
