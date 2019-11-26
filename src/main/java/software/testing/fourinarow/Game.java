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
     * The boolean variable isFirstStart judges if the game is just going to start
     * The boolean variable isLastPlayerOne is used to judge the last player
     * so we can get the current player
     */
    private static boolean isFirstStart = true;
    private static boolean isLastPlayerOne = false;

    //This array is used to record the last Modified Cell's column & row number
    private static int[] lastPositionList = new int[2];

    //The way to implement redo & undo is to record the last filled Cell
    //Then set the condition to redo or undo
    private static boolean hasJustUndo = false;






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
     * @return current active player
     */
    public Player getActivePlayer(){

        if (isFirstStart){
            //if it is the first start, the active player should be Player ONE
            return Player.ONE;
        }else {
            // the sign : isLastPlayerOne
            // should be changed in the take turn method
            // because taking each turn should be the only reason of changing players
            if (isLastPlayerOne){
                return Player.TWO;
            }else {
                return Player.ONE;
            }
        }

    }

    /**
     * After taking one turn, which means the takeTurn() method shall return true
     * The setSignForNextPlayer() method should set two signals: isFirstStart&isLastPlayerOne
     * to an ideal situation, which ensures the toggling between PLayer_one & Player_two
     * running smoothly
     *
     */
    public void setSignForNextPlayer(){

        if (isFirstStart) {
            //if it is the first start, the active player should be Player ONE
            isFirstStart = false;
            isLastPlayerOne = true;
        }else {
            if (getActivePlayer() == Player.TWO) {
                isLastPlayerOne = false;
//            } else if (getActivePlayer() == Player.ONE) {
            }else {
                isLastPlayerOne = true;
            }
        }
    }


    /**
     * By specifying the column that the player has selected.
     * The code will then find the lowest row in that column that is empty and insert the piece.
     *
     * @param column  the column selected by current player
     * @return If the column is full, the value false should be returned.
     * True is returned if the piece can be placed.
     */

    public boolean takeTurn(int column) {

//        if (column < 0 || column >=MAXIMUM_COLUMNS){
//            throw new IllegalArgumentException("Selected column can not be negative or bigger than MAXIMUM_COLUMNS-1 !");
//        }else {
        try {
            //see if the chosen column still got empty cells
            //if not, return false
            if (numberOfEmptyCellsInColumn(column)>0){
                lastPositionList[0] = column;
                //Set the lastPositionCell's Column
                //Because of a design error in the GUI in the original code
                //That is, the top and bottom indexes of the same column
                //are in the reverse order of normal
                //So traversal is done here using the opposite logic
                for (int i = MAXIMUM_ROWS - 1; i >=0 ; i--) {
                    //the traversal begin at the bottom of the selected column
                    //Once an empty cell is founded, change it status to the current player
                    //and return true, if could not found one, return false
                    if (gameGrid[column][i] == CellStatus.EMPTY) {
                        if (hasJustUndo) {
                            setSignForNextPlayer();
                        }
                        lastPositionList[1] = i;//Set the lastPositionCell's row
                        if (getActivePlayer() == Player.ONE) {
                            gameGrid[column][i] = CellStatus.PLAYER_ONE;
                            //Once one turn the player played successful
                            //Judge if the current player has won with the hasWon() method
                            //Actually this is meaningless because of the "winner tips" written in controller
                            if (hasWon()){
                                System.out.println("The current player"+ getActivePlayer() +" has won");
                            }
                            //This turn, the player played successfully
                            //So change the sign to set the next player
                            setSignForNextPlayer();
                            //change the sign so current player can't redo() again
                            //unless the player use another undo()
                            hasJustUndo = false;
                            return true;
//                        } else if (getActivePlayer() == Player.TWO) {
                        } else{
                            gameGrid[column][i] = CellStatus.PLAYER_TWO;
                            if (hasWon()){
                                System.out.println("The current player"+ getActivePlayer() +" has won");
                            }
                            setSignForNextPlayer();
                            hasJustUndo = false;
                            return true;
                        }
                    }
                }
            }
        } catch (FourInARowException e) {
            e.printStackTrace();
        }
//        }
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
     * After a player has taken a turn, check if the player has won
     * Three boolean variables : isVertical & isDiagonal & isHorizontal
     * represent 3 ways to win the game
     * As long as one of these conditions is met, the current player wins
     *
     * @return if the current player won, return true, if not, return false
     */
    public boolean hasWon(){
//        Player currentPlayer = getActivePlayer();
        boolean isVertical = false;
        boolean isHorizontal = false;
        boolean isDiagonalUp2Down = false;
        boolean isDiagonalDown2Up = false;


        //Situation 1 : wins vertically
        for (int i=0;i<MAXIMUM_COLUMNS;i++){
            for (int k=0;k<MAXIMUM_ROWS-3;k++){
                if ((gameGrid[i][k] == gameGrid[i][k+1] && gameGrid[i][k] == gameGrid[i][k+2]
                        && gameGrid[i][k] == gameGrid[i][k+3] && gameGrid[i][k] != CellStatus.EMPTY)){
                    isVertical = true;
                }
            }
        }

        //Situation 2 : wins horizontally
        for (int i=0;i<MAXIMUM_COLUMNS-3;i++){
            for (int k=0;k<MAXIMUM_ROWS;k++){
                if ((gameGrid[i][k] == gameGrid[i+1][k] && gameGrid[i][k] == gameGrid[i+2][k]
                        && gameGrid[i][k] == gameGrid[i+3][k] && gameGrid[i][k] != CellStatus.EMPTY)){
                    isHorizontal = true;
                }
            }
        }

        //Situation 3 : wins diagonally (left to right, up to down)
        for (int i=0;i<MAXIMUM_COLUMNS-3;i++){
            for (int k=0;k<MAXIMUM_ROWS-3;k++){
                if ((gameGrid[i][k] == gameGrid[i+1][k+1] && gameGrid[i][k] == gameGrid[i+2][k+2]
                        && gameGrid[i][k] == gameGrid[i+3][k+3] && gameGrid[i][k] != CellStatus.EMPTY)){
                    isDiagonalUp2Down = true;
                }
            }
        }

        //Situation 4 : wins diagonally (left to right, down to up)
        for (int i=0;i<MAXIMUM_COLUMNS-3;i++){
            for (int k=MAXIMUM_ROWS-1;k>=3;k--){
                if ((gameGrid[i][k] == gameGrid[i+1][k-1] && gameGrid[i][k] == gameGrid[i+2][k-2]
                        && gameGrid[i][k] == gameGrid[i+3][k-3] && gameGrid[i][k] != CellStatus.EMPTY)){
                    isDiagonalDown2Up = true;
                }
            }
        }

        // Once one condition is satisfied, return true
        return isDiagonalUp2Down || isDiagonalDown2Up || isVertical || isHorizontal;
    }




    /**
     * The undo must come before the next player takes a turn.
     * Once the other player takes a turn, the undo operation should do nothing.
     *
     * @return
     * The method returns true for success
     * or false if the move cannot be made
     */
    public boolean undo(){
        int lastCellColumn = lastPositionList[0];
        int lastCellRow = lastPositionList[1];


        // In the judgment condition of this statement,
        // we set the undo() to be allowed once the "player color" of the last modified CELL is different from the result of getActivePlayer()
        // because in the takeTurn() method, once we run the takeTurn () method.
        // The two identifiers(isFirstStart & isLastPlayerOne) will be changed, so when we call the getActivePlayer() method
        // in the undo() and redo methods after using the takeTurn() method, the result will be the active player in the next turn
        // So undo() is only allowed if the current player of turn is the same as the last modified cell
        // which also means the next player differs from last modified cell, that is the logic below:
        if (((gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_ONE && getActivePlayer() == Player.TWO))
                || ((gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_TWO && getActivePlayer() == Player.ONE))){

            gameGrid[lastCellColumn][lastCellRow] = CellStatus.EMPTY;
            //Set the last modified Cell to empty status
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
     *
     * @return
     * The method returns true for success or false if the move cannot be made.
     *
     */
    public boolean redo(){
        int lastCellColumn = lastPositionList[0];
        int lastCellRow = lastPositionList[1];
        //Only after undo can the current player perform redo
        if (hasJustUndo){
            hasJustUndo = false;
            //If the last modified cell's player is current active player
            //Means it is in the same turn and the next turn did not begin
            //So the redo operation is allowed
            if (getActivePlayer() == Player.ONE){
//                gameGrid[lastCellColumn][lastCellRow] = CellStatus.PLAYER_ONE;
                gameGrid[lastCellColumn][lastCellRow] = CellStatus.PLAYER_TWO;
                return true;
//            }else if (getActivePlayer() == Player.TWO){
            }else{
                gameGrid[lastCellColumn][lastCellRow] = CellStatus.PLAYER_ONE;
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

/**
 * Vellichor: Some statement in Chinese of the logic written in undo() method
 *
 * 在这个语句的判断条件中，之所以设置为当上一个CELL的玩家颜色与当前玩家的颜色不同时才设置为允许Undo，
 * 是因为在taketurn() 方法中， 我们设置了一旦运行taketurn（）方法，
 * 两个判断当前玩家的标识符就会被改变，因此当我们在使用taketurn（）方法之后，
 * 再次在undo()和redo()方法中调用getActivePlayer()方法时，
 * 调用的结果将会是下一个turn中的活跃玩家，也即为下一个玩家，
 * 因此如此我们设置，以确定当前turn的玩家与last modified cell 色块相同时，才允许undo()
 */

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


//        if (gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_ONE
//                && getActivePlayer() == Player.ONE){
//
//            return true;
//        }else if (gameGrid[lastCellColumn][lastCellRow] == CellStatus.PLAYER_TWO
//                && getActivePlayer() == Player.TWO) {
//            return true;
//        }




//Maybe it is also OK to ignore the code below
//because in logic we can equal null to CellStatus.EMPTY
//And for taking each turn it is meaningless to initialize all cell status
//Though for all of these, I still chose to initialize all of the cell status to EMPTY
//Because for the original game, if you don't use reset() function
//the gameGrid will be null but not EMPTY which may effect other logic
//        for (int i = 0; i < MAXIMUM_COLUMNS; i++) {
//            for (int k = 0; k < MAXIMUM_ROWS; k++) {
//                if (gameGrid[i][k] == null) {
//                    gameGrid[i][k] = CellStatus.EMPTY;
//                }
//            }
//        }
