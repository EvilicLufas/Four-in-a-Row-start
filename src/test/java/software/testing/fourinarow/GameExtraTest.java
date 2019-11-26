package software.testing.fourinarow;


import org.junit.jupiter.api.Test;

import java.lang.reflect.GenericArrayType;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Game class.
 *
 * This represents the initial version that is checked out
 * for the laboratory on the testing course.
 *
 * @author Vellichor  (neu)
 * @Student_ID: 20175045
 * @Student_name: Gao Ge
 * @version 1.0
 */
public class GameExtraTest {

    /**
     * Test getActivePlayer()
     * For the 1st turn, player should start with the player_ONE
     */
    @Test
    public void playerShouldStart_WithPlayer_ONE(){

        Game game = new Game();
        game.resetGame();
        assertEquals(Player.ONE,game.getActivePlayer());
    }

    /**
     * Test getActivePlayer() & takeTurn()
     * For the Even number like 2nd,4th turn, player should start with the player_ONE
     */
    @Test
    public void evenTurnShouldBe_Player_TWO(){
        Game game = new Game();
        game.resetGame();
        assertEquals(Player.ONE,game.getActivePlayer());
        game.takeTurn(0);
        assertEquals(game.getActivePlayer(),Player.TWO);
        game.takeTurn(0);
        game.takeTurn(0);
        assertEquals(Player.TWO,game.getActivePlayer());
    }

    /**
     * Test getActivePlayer() & takeTurn()
     * For the Odd Number like 3nd turn, player should start with the player_ONE
     */
    @Test
    public void oddTurnShouldBe_Player_ONE(){
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        game.takeTurn(1);
        assertEquals(game.getActivePlayer(),Player.ONE);
        game.takeTurn(0);
        game.takeTurn(0);
        assertEquals(Player.ONE,game.getActivePlayer());
    }


    /**
     * Test game() constructor
     * After construct a new game, the gameGrid cells should be null
     */
    @Test
    public void CellsShouldBeNull_NewGame() throws FourInARowException {
        int MAXIMUM_ROWS = 6;
        int MAXIMUM_COLUMNS = 7;
        Game game = new Game();
        for(int column = 0; column < MAXIMUM_COLUMNS; column++) {
            for(int row = 0; row < MAXIMUM_ROWS; row++) {
                assertNull(game.getCellStatus(column, row));
            }
        }
    }

    /**
     * Test resetGame()
     * After reset the game, the gameGrid cells should be EMPTY
     */
    @Test
    public void CellsShouldBeEmptyAfter_ResetGame() throws FourInARowException {

        Game game = new Game();
        game.resetGame();
        for(int column = 0; column < Game.MAXIMUM_COLUMNS; column++) {
            for(int row = 0; row < Game.MAXIMUM_ROWS; row++) {
                assertEquals(game.getCellStatus(column,row),CellStatus.EMPTY);
            }
        }
    }

    /**
     * Test takeTurn()
     * After player one take turn, cell should be player_one
     */
    @Test
    public void CellShouldBeOneAfter_PlayerONE_takeTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        Player currentPlayer = game.getActivePlayer();
        game.takeTurn(0);
        assertEquals(currentPlayer,Player.ONE);
        assertEquals(game.getCellStatus(0,Game.MAXIMUM_ROWS-1),CellStatus.PLAYER_ONE);

    }

    /**
     * Test takeTurn()
     * After player two take turn, cell should be player_TWO
     */
    @Test
    public void CellShouldBeTWOAfter_PlayerTWO_takeTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(1);
        Player currentPlayer = game.getActivePlayer();
        game.takeTurn(0);
        assertEquals(Player.TWO,currentPlayer);
        assertEquals(game.getCellStatus(0,Game.MAXIMUM_ROWS-1),CellStatus.PLAYER_TWO);
    }

    /**
     * Test takeTurn()
     * takeTurn() should return false when the selected column is full
     */
    @Test
    public void shouldReturnFalse_whenColumnIsFull() {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        game.takeTurn(0);
        game.takeTurn(0);
        game.takeTurn(0);
        game.takeTurn(0);
        game.takeTurn(0);
        //The column is full
        assertFalse(game.takeTurn(0));
    }

    /**
     * Test takeTurn()
     * takeTurn() should throw IllegalArgumentException when the selected column is negative
     */
    @Test
    public void shouldReturnFalse_whenColumnIsNegative() {
        Game game = new Game();
        game.resetGame();
        assertFalse(game.takeTurn(-1));
        //The column is null
//        assertThrows(FourInARowException.class, () -> {
//            game.takeTurn(-1);
//        });
    }

    /**
     * Test takeTurn()
     * takeTurn() should return false and throw IllegalArgumentException
     * \when the selected column is larger than MAXIMUM_COLUMNS index
     */
    @Test
    public void shouldReturnFalse_whenColumnIsOverIndex () {
        Game game = new Game();
        game.resetGame();
        assertFalse(game.takeTurn(Game.MAXIMUM_COLUMNS));

    }

    /**
     * Test takeTurn() & hasWon()
     * Should return hasWon = true if wins vertically
     */
    @Test
    public void hasWonShouldReturnTrue_whenWinsVertically() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(2);
        game.takeTurn(1);

        game.takeTurn(2);
        game.takeTurn(1);

        game.takeTurn(0);
        game.takeTurn(1);

        game.takeTurn(0);
        game.takeTurn(1);
        assertTrue(game.hasWon());
    }

    /**
     * Test takeTurn() & hasWon()
     * Should return hasWon = true if wins vertically
     */
    @Test
    public void hasWonShouldReturnTrue_whenWinsHorizontally() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(1);
        game.takeTurn(0);

        game.takeTurn(2);
        game.takeTurn(0);

        game.takeTurn(3);
        game.takeTurn(0);

        game.takeTurn(4);
        assertTrue(game.hasWon());
    }

    /**
     * Test takeTurn() & hasWon()
     * Should return hasWon = true if Wins Diagonally Up to Down
     */
    @Test
    public void hasWonShouldReturnTrue_whenWinsDiagonallyUp2Down() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        //Player one get 1st for column3
        game.takeTurn(3);
        game.takeTurn(2);
        //Player one get 2nd for column2
        game.takeTurn(2);
        game.takeTurn(1);

        game.takeTurn(0);
        game.takeTurn(1);
        //Player one get 3nd for column1
        game.takeTurn(1);
        game.takeTurn(0);

        game.takeTurn(1);
        game.takeTurn(0);
        //Player one get 4th for column0
        game.takeTurn(0);

        assertTrue(game.hasWon());
    }

    /**
     * Test takeTurn() & hasWon()
     * Should return hasWon = true if Wins Diagonally Down to Up
     */
    @Test
    public void hasWonShouldReturnTrue_whenWinsDiagonallyDown2Up() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        //Player one get 1st for column0
        game.takeTurn(0);
        game.takeTurn(1);
        //Player one get 2nd for column1
        game.takeTurn(1);
        game.takeTurn(2);

        game.takeTurn(3);
        game.takeTurn(2);
        //Player one get 3nd for column2
        game.takeTurn(2);
        game.takeTurn(3);

        game.takeTurn(1);
        game.takeTurn(3);
        //Player one get 4th for column3
        game.takeTurn(3);

        assertTrue(game.hasWon());
    }


    /**
     * Test takeTurn() & undo()
     * last Cell Should Be Empty after Undo
     */
    @Test
    public void lastCellShouldBeEmpty_afterUndo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);
        game.undo();
        assertEquals(game.getCellStatus(0,5),CellStatus.EMPTY);

    }

    /**
     * Test takeTurn() & undo()
     * Reselected Cell Should Still be the same player when the player takeTurn() again after undo()
     * which means the player has the right to reselect the column after using redo()
     */
    @Test
    public void selectedCellShouldBeSamePlayer_whenUndoThenTakeTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);
        game.undo();
        assertEquals(game.getCellStatus(0,5),CellStatus.EMPTY);
        //Reselect one column after using undo()
        game.takeTurn(1);
        assertEquals(game.getCellStatus(1,5),CellStatus.PLAYER_ONE);
    }

    /**
     * Test takeTurn() & undo()
     * last Cell Should Still be the same player when the player takeTurn() again after undo()
     * which means the player has the right to reselect the column after using redo()
     */
    @Test
    public void lastCellShouldBeSamePlayer_whenRedoAfterUndo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);
        game.undo();
        assertEquals(game.getCellStatus(0,5),CellStatus.EMPTY);
        //redo() after undo()
        game.redo();
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);

        game.takeTurn(2);
        game.undo();
        game.redo();
        assertEquals(game.getCellStatus(2,5),CellStatus.PLAYER_TWO);
    }

    /**
     * Test takeTurn() & undo()
     * Undo() should return false when the player did not takeTurn()
     */
    @Test
    public void shouldReturnFalse_whenUndoWithoutTakeTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        assertFalse(game.undo());
    }

    /**
     * Test takeTurn() & undo()
     * Undo() should return True when the player just use takeTurn()
     */
    @Test
    public void shouldReturnTrue_whenUndoAfterTakeTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertTrue(game.undo());
    }

    /**
     * Test takeTurn() & undo() & redo()
     * Redo() should return false when the player did not Undo()
     */
    @Test
    public void shouldReturnFalse_whenRedoWithoutUndo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertFalse(game.redo());
    }

    /**
     * Test takeTurn() & undo() & redo()
     * Redo() should return true when the player just used Undo()
     */
    @Test
    public void shouldReturnTrue_whenRedoAfterUndo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        game.takeTurn(0);
        game.undo();
        assertTrue(game.redo());
    }

    /**
     * Test takeTurn() & undo()
     * Undo() should return false if one player try to undo but not in his turn
     */
    @Test
    public void shouldNotChanged_whenUndoSkipOneTurn() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);
        //In next turn,use undo()
        game.takeTurn(0);
        game.undo();
        assertEquals(game.getCellStatus(0,5),CellStatus.PLAYER_ONE);
    }

    /**
     * Test takeTurn() & undo()
     */
    @Test
    public void shouldReturnFalse_whenUndoAfterUndo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        game.takeTurn(0);

        game.undo();
        assertFalse(game.undo());

    }

    /**
     * Test takeTurn() & undo() & redo()
     * Undo() should return false if one player try to undo but not in his turn
     */
    @Test
    public void shouldReturnFalse_whenRedoAfterRedo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();
        game.takeTurn(0);
        game.takeTurn(0);
        game.undo();
        game.redo();
        assertFalse(game.redo());

    }

    /**
     * Test takeTurn() & undo()
     * use setSign method to test all undo() conditions
     */
    @Test
    public void testCondition_undo() throws FourInARowException {
        Game game = new Game();
        game.resetGame();

        game.takeTurn(0);
        assertTrue(game.undo());
        game.setSignForNextPlayer();
        assertFalse(game.undo());
        game.redo();
        game.setSignForNextPlayer();
        assertFalse(game.undo());

        game.takeTurn(0);
        assertTrue(game.undo());
        game.setSignForNextPlayer();
        assertFalse(game.undo());
        game.redo();
        game.setSignForNextPlayer();
        assertFalse(game.undo());

    }
}











