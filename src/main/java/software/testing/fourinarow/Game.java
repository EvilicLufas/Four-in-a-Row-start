package software.testing.fourinarow;

import java.util.ArrayList;

/**
 * Represents the grid that holds the pieces that have been
 * placed into each column.
 */
public class Game {

    /** The maximum number of rows in the game grid */
    public static int MAXIMUM_ROWS = 6;

    /** The maximum number of columns in the game grid */
    public static int MAXIMUM_COLUMNS = 7;

    /** The game grid that holds information about the status of each cell */
    private CellStatus[][] gameGrid;

    /**
     * Creates a new instance, initialising the board with the
     * Maximum values for columns and rows.
     */
    public Game() {
        gameGrid = new CellStatus[MAXIMUM_COLUMNS][MAXIMUM_ROWS];
    }

    /**
     * The class manages which player is currently active.
     *
     * Player One should be the first player to take a turn.
     * After a turn is taken by Player One, Player Two becomes the active player.
     * Then, Player One and so on.
     *
     * @return
     */
    private static boolean isFirstStart = true;
    private static boolean isLastPlayerOne = false;
    public Player getActivePlayer(){

        if (isFirstStart){
            //if it is the first start, the active player should be Player ONE
            isFirstStart = false;
            isLastPlayerOne = true;
            return Player.ONE;
        }else {
            // the sign : isLastPlayerOne
            // should be changed in the take turn method
            // because taking each turn should be the only reason of changing players
            if (isLastPlayerOne){
//                isLastPlayerOne = false;
                return Player.TWO;
            }else {
//                isLastPlayerOne = true;
                return Player.ONE;
            }
        }
//        if (lastPlayer == null){
//            return starterPlayer;
//        }
//        switch (lastPlayer){
//            case ONE:
//                return Player.TWO;
//            case TWO:
//                return Player.ONE;
//            case :
//                return starterPlayer;
//        }
//
//
//        Player currentPlayer = Player.ONE;
    }

    /**
     * By specifying the column that the player has selected.
     * The code will then find the lowest row in that column that is empty and insert the piece.
     * If the column is full, the value false should be returned.
     * True is returned if the piece can be placed.
     *
     * @param column
     * @return
     */
//    private static ArrayList<Integer> lastPositionList;
    private static int[] lastPositionList = new int[2];
    //This ArrayList is used to record the last Modified Cell
    public boolean takeTurn(int column) {

        hasJustUndo = false;
        //Each turn's beginning, the last turn's Player won't be able to redo
        Player currentPlayer = getActivePlayer();

        if (currentPlayer == Player.TWO) {
            isLastPlayerOne = false;
        } else if (currentPlayer == Player.ONE) {
            isLastPlayerOne = true;
        }
        //Maybe it is also OK to ignore the code below
        //because in logic we can equal null to CellStatus.EMPTY
        //And for taking each turn it is meaningless to initialize all cell status
        //Though for all of these, I still chose to initialize all of the cell status to EMPTY
        //Because for the original game, if you don't use reset() function
        //the gameGrid will be null but not EMPTY which may effect other logic
        for (int i = 0; i < MAXIMUM_COLUMNS; i++) {
            for (int k = 0; k < MAXIMUM_ROWS; k++) {
                if (gameGrid[i][k] == null) {
                    gameGrid[i][k] = CellStatus.EMPTY;
                }
            }
        }

        try {
            //see if the chosen column still got empty cells
            //if not, return false
            if (numberOfEmptyCellsInColumn(column)>0){
                lastPositionList[0] = column;//Set the lastPositionCell's Column
                //Because of a design error in the GUI in the original code
                //That is, the top and bottom indexes of the same column
                //are in the reverse order of normal
                //So traversal is done here using the opposite logic
                for (int i = MAXIMUM_ROWS - 1; i >=0 ; i--) {
                    //the traversal begin at the bottom of the selected column
                    //Once an empty cell is founded, change it status to the current player
                    //and return true, if could not found one, return false
                    if (gameGrid[column][i] == CellStatus.EMPTY) {
                        lastPositionList[1] = i;//Set the lastPositionCell's row
                        if (currentPlayer == Player.ONE) {
                            gameGrid[column][i] = CellStatus.PLAYER_ONE;
                            return true;
                        } else if (currentPlayer == Player.TWO) {
                            gameGrid[column][i] = CellStatus.PLAYER_TWO;
                            return true;
                        }
                    }
                }
            }
        } catch (FourInARowException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * stop the current game and reset the game to be started again.
     * The player turns will be reset.
     * The Undo and Redo facility will be reset.
     */
    public void resetGame(){

        //set all cell status to EMPTY
        reset();
        //set the game to the start
        isFirstStart = true;
        isLastPlayerOne = false;

    }

    /**
     * After a player has taken a turn,
     * check if the player has won
     *
     * @return
     */
    public boolean hasWon(){
        Player currentPlayer = getActivePlayer();
        boolean isVertical = false;
        boolean isDiagonal = false;
        boolean isHorizontal = false;
//        boolean winPlayerOne = false;
//        boolean winPlayerTwo = false;

        //Situation 1 : wins vertically
        for (int i=0;i<MAXIMUM_COLUMNS;i++){
            for (int k=0;k<MAXIMUM_ROWS-3;k++){
                if ((gameGrid[i][k] == gameGrid[i][k+1] && gameGrid[i][k] == gameGrid[i][k+2])){
                    isVertical = true;
                }
            }
        }

        //Situation 2 : wins horizontally
        for (int i=0;i<MAXIMUM_COLUMNS-3;i++){
            for (int k=0;k<MAXIMUM_ROWS;k++){
                if ((gameGrid[i][k] == gameGrid[i+1][k] && gameGrid[i][k] == gameGrid[i+2][k])){
                    isHorizontal = true;
                }
            }
        }

        //Situation 3 : wins diagonally
        for (int i=0;i<MAXIMUM_COLUMNS-3;i++){
            for (int k=0;k<MAXIMUM_ROWS-3;k++){
                if ((gameGrid[i][k] == gameGrid[i+1][k+1] && gameGrid[i][k] == gameGrid[i+2][k+2])){
                    isDiagonal = true;
                }
            }
        }
        // Once one condition is satisfied, return true
        return isDiagonal || isVertical || isHorizontal;
    }


    //The way to do this is to record the last filled Cell
    //Then set the condition

    /**
     * The undo must come before the next player takes a turn.
     * Once the other player takes a turn, the undo operation should do nothing.
     * The method returns true for success
     * or false if the move cannot be made
     * @return
     */

    private static boolean hasJustUndo = false;
    public boolean undo(){
        int lastCellColumn = lastPositionList[0];
        int lastCellRow = lastPositionList[1];
//        if (gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_ONE
//                && getActivePlayer() == Player.ONE){
//
//            return true;
//        }else if (gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_TWO
//                && getActivePlayer() == Player.TWO) {
//            return true;
//        }

        if ((gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_ONE
                && getActivePlayer() == Player.ONE) ||
                (gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_ONE
                && getActivePlayer() == Player.ONE)){
            //Set the last modified Cell to empty status

            gameGrid[lastCellColumn][lastCellRow] = CellStatus.EMPTY;

            hasJustUndo = true;
            //After undo, the signal hasJustUndo will be set to true
            //While in the each beginning of the turn, the signal will be set to false
            //To prevent that the player use redo in different turn to cause chaos
            return true;
        }

        return false;
    }

    /**
     * If a player has undone their last turn, the player can redo that move.
     * The redo must come before the next player takes a turn.
     * Once the other player takes a turn, the redo operation should do nothing.
     * The method returns true for success or false if the move cannot be made.
     * @return
     */
    public boolean redo(){
        int lastCellColumn = lastPositionList[0];
        int lastCellRow = lastPositionList[1];
        if (hasJustUndo = true){
            if (getActivePlayer() == Player.ONE){
                gameGrid[lastCellColumn][lastCellRow] = CellStatus.PLAYER_ONE;
                return true;
            }else if (getActivePlayer() == Player.TWO){
                gameGrid[lastCellColumn][lastCellRow] = CellStatus.PLAYER_TWO;
                return true;
            }

        }
        return false;
    }

    /**
     * Returns the number of cells in a column that have the status
     * GridCellStatus.EMPTY.
     *
     * @param column The column to check. This is 0 indexed, so 0 represents column 1.
     * @return A count of the number of empty cells.
     * @throws FourInARowException if the column value is outside the range
     * 0 to MAXIMUM_COLUMNS.
     */
    public int numberOfEmptyCellsInColumn(int column) throws FourInARowException {

        int emptyCount = 0;

        for(int row = 0; row < MAXIMUM_ROWS; row++) {
            if(getCellStatus(column, row) == CellStatus.EMPTY) {
                emptyCount++;
            }
        }

        return emptyCount;
    }

    /**
     * Gets the GridCellStatus for a cell at the given grid position.
     * @param row The number of the row, with 0 representing the first row.
     * @param column The number of the column, with 0 representing the first column.
     * @return The status value for the grid at the specified position.
     * @throws FourInARowException if the row or column number is out of bounds for the
     * number of rows and columns.
     */
    public CellStatus getCellStatus(int column, int row)
           throws FourInARowException {

        if(row < 0) {
            throw new FourInARowException("Row cannot be negative");
        }

        if(row > MAXIMUM_ROWS - 1) {
            throw new FourInARowException("Row cannot be greater than the number of rows");
        }

        if(column < 0) {
            throw new FourInARowException("Column cannot be negative");
        }

        if(column > MAXIMUM_COLUMNS - 1) {
            throw new FourInARowException("Column cannot be greater than the number of columns");
        }

        return gameGrid[column][row];
    }

    /**
     * Resets the board so that every position contains the value
     * GridCellStatus.EMPTY.
     */
    public void reset() {
        for(int column = 0; column < MAXIMUM_COLUMNS; column++) {
            for(int row = 0; row < MAXIMUM_ROWS; row++) {
                gameGrid[column][row] = CellStatus.EMPTY;
            }
        }

    }
}
