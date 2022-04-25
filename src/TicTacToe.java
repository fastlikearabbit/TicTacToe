import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;
import java.lang.Math;

public class TicTacToe {
    private final int boardSize = 3;
    private final char[][] board = new char[boardSize][boardSize];

    public TicTacToe () {
        initializeBoard();
    }

    public void initializeBoard () {
        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                board[i][j] = '_';
    }

    public void createCustomBoard () {
        Scanner boardScanner = new Scanner(System.in);
        for(int i = 0; i < boardSize; i++)
            for(int j = 0; j < boardSize; j++)
                board[i][j] = boardScanner.next().charAt(0);
    }

    public void printBoard () {
        System.out.println("\t1\t2\t3");

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (j == 0)
                    System.out.printf("%d | ", i + 1);

                System.out.printf("%c | ", board[i][j]);
            }
            System.out.println();
        }

    }

    public boolean isHorizontalOrVerticalWin (char currentPlayer) {

        for (int i = 0; i < boardSize; i++) {
            boolean isHorizontalWin = true;
            boolean isVerticalWin = true;
            for (int j = 0; j < boardSize; j++) {

                if (board[i][j] != currentPlayer)
                    isHorizontalWin = false;

                if (board[j][i] != currentPlayer)
                    isVerticalWin = false;
            }
            if (isHorizontalWin || isVerticalWin)
                return true;
        }
        return false;
    }

    public boolean isDiagonalWin (char currentPlayer) {
        boolean isMainDiagonalWin = true;
        boolean isSecondDiagonalWin = true;

        for (int i = 0; i < boardSize; i++) {
            if (board[i][i] != currentPlayer)
                isMainDiagonalWin = false;

            if (board[i][boardSize - 1 - i] != currentPlayer)
                isSecondDiagonalWin = false;

        }

        return isMainDiagonalWin || isSecondDiagonalWin;

    }

    public boolean isWinner (char currentPlayer) {
        return isHorizontalOrVerticalWin(currentPlayer) || isDiagonalWin(currentPlayer);
    }

    public boolean isFreeCell(int row, int col) {
        return board[row][col] != 'X' && board[row][col] != 'O';
    }

    public int[] AIRandomMove() {
        int[] position = new int[2];

        Random rand = new Random();

        while (true) {
            position[0] = rand.nextInt(boardSize);
            position[1] = rand.nextInt(boardSize);

            if (isFreeCell(position[0], position[1]))
                break;
        }

        return position;
    }

    public boolean isFull() {
        for (int i = 0; i < boardSize; i++)
            for(int j = 0; j < boardSize; j++)
                if (board[i][j] == '_')
                    return false;
        return true;
    }

    public int minimax(boolean isMaximizingPlayer) {

        /*
        O is the maximizing player
        O wins = +1
        X wins = -1
        draw = 0
         */

        if (isWinner('O'))
            return 1;
        else if (isWinner('X'))
            return -1;

        if (isFull())
            return 0;


        if (isMaximizingPlayer) {
            int best = Integer.MIN_VALUE;

            for (int i = 0; i < boardSize; i++)
                for (int j = 0; j < boardSize; j++)
                    if (isFreeCell(i, j)) {
                        board[i][j] = 'O';

                        best = Math.max(minimax(!isMaximizingPlayer), best);

                        board[i][j] = '_';
                    }

            return best;

        } else {
            int best = Integer.MAX_VALUE;

            for (int i = 0; i < boardSize; i++)
                for (int j = 0; j < boardSize; j++)
                    if (isFreeCell(i, j)) {
                        board[i][j] = 'X';

                        best = Math.min(minimax(!isMaximizingPlayer), best);

                        board[i][j] = '_';
                    }

            return best;
        }

    }

    public int[] showMove(char currentPlayer) {
        int[] bestMove = {-1, -1};
        int bestScore = -Integer.MAX_VALUE;

        for (int i = 0; i < boardSize; i++)
            for (int j = 0; j < boardSize; j++)
                if (isFreeCell(i, j)) {
                    board[i][j] = currentPlayer;

                    int currentMoveValue = minimax(false);

                    board[i][j] = '_';

                    if (bestScore < currentMoveValue) {
                        bestScore = currentMoveValue;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
        return bestMove;

    }

    public void startGame () {
        int numberOfOccupiedCells = 0;
        char currentPlayer = 'X';

        while (true) {
            System.out.printf("\nIt's %c's turn to play!\n", currentPlayer);
            printBoard();
            System.out.println("Enter coordinates (row, col): ");

            boolean computerPlayed = false;

            if (currentPlayer == 'O') {
                int[] position = showMove('O');
                System.out.printf("%d %d", position[0] + 1, position[1] + 1);
                board[position[0]][position[1]] = 'O';
                computerPlayed = true;

            }

            boolean isValidInput = false;
            int row, col;
            while (!isValidInput && !computerPlayed) {
                try {
                    Scanner scanForInput = new Scanner(System.in);
                    row = scanForInput.nextInt();
                    col = scanForInput.nextInt();

                    if (row > 0 && row <= boardSize && col > 0 && col <= boardSize
                            && board[row - 1][col - 1] == '_') {
                        board[row - 1][col - 1] = currentPlayer;
                        isValidInput = true;
                    } else {
                        System.out.printf("Please enter an integer between 1 and %d inclusively or a square " +
                                "that's not occupied already\n", boardSize);
                    }

                } catch (InputMismatchException ime) {
                    System.out.println("Please enter an integer!");
                }
            }

            numberOfOccupiedCells++;

            if (isWinner(currentPlayer)) {
                System.out.printf("\nThe game has ended, %c won!", currentPlayer);
                printBoard();
                return;
            }

            if (numberOfOccupiedCells == boardSize * boardSize) {
                System.out.println("It's a draw!");
                return;
            }

            if (currentPlayer == 'X')
                currentPlayer = 'O';
            else
                currentPlayer = 'X';
        }

    }

}
