package REALPROJECTS;

import java.util.Scanner;

public class TicTacToe {
    static char[][] board = {
        {' ', ' ', ' '},
        {' ', ' ', ' '},
        {' ', ' ', ' '}
    };
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char currentPlayer = 'X';
        boolean gameWon = false;

        System.out.println("Welcome to Tic Tac Toe!");
        printBoard();

        // Game loop for max 9 moves
        for (int turn = 0; turn < 9; turn++) {
            System.out.println("Player " + currentPlayer + ", enter your move (row and column: 0,1,2): ");
            int row = sc.nextInt();
            int col = sc.nextInt();

            // Check valid move
            if (row < 0 || row > 2 || col < 0 || col > 2 || board[row][col] != ' ') {
                System.out.println("Invalid move! Try again.");
                turn--; // repeat this turn
                continue;
            }

            // Place mark
            board[row][col] = currentPlayer;
            printBoard();

            // Check win condition
            if (checkWin(currentPlayer)) {
                System.out.println("Player " + currentPlayer + " wins!");
                gameWon = true;
                break;
            }

            // Switch player
            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        if (!gameWon) {
            System.out.println("It's a draw!");
        }

        sc.close();
    }

    // Print board
    static void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n-------------");
        }
    }

    // Check winning condition
    static boolean checkWin(char player) {
        // Check rows & columns
        for (int i = 0; i < 3; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) || 
                (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }

        // Check diagonals
        if ((board[0][0] == player && board[1][1] == player && board[2][2] == player) || 
            (board[0][2] == player && board[1][1] == player && board[2][0] == player)) {
            return true;
        }

        return false;
    }
}
  