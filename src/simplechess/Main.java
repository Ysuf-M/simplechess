package simplechess;

import java.util.Scanner;

public class Main {
	public enum Tile {
		b, w, n
	};

	public enum Team {
		b, w
	};

	public enum Result {
		b, w, s, n
	};
	
	public static Team turn = Team.w;

	public static void main(String[] args) {
		Tile[][] board = new Tile[4][3];
		resetBoard(board);
		twoPlayer(board);
	}

	public static class Move {
		public int row1;
		public int row2;
		public int col1;
		public int col2;

		public Move() {
			row1 = 0;
			row2 = 0;
			col1 = 0;
			col2 = 0;
		}

		public Move(int c1, int r1, int c2, int r2) {
			this.col1 = c1;
			this.col2 = c2;
			this.row1 = r1;
			this.row2 = r2;
		}
	}

	private static void twoPlayer(Tile[][] board) {
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			getMove(board, turn);
			nextTurn();
		}
		switch (checkGame(board)) {
		case b:
			System.out.println("Team black has won the game!");
			break;
		case w:
			System.out.println("Team white has won the game!");
			break;
		case s:
			System.out.println("It's a stalemate.");
		default:
		}
	}


	private static Result checkGame(Tile[][] board) {
		for (int i = 0; i < board[0].length; i++)
			if (board[0][i] == Tile.w)
				return Result.w;
		for (int i = 0; i < board[0].length; i++)
			if (board[board.length - 1][i] == Tile.b)
				return Result.b;
		boolean movesPossible = false;
		for (int r1 = 0; r1 < board.length; r1++) {
			for (int c1 = 0; c1 < board[0].length; c1++) {
				for (int r2 = 0; r2 < board.length; r2++) {
					for (int c2 = 0; c2 < board[0].length; c2++) {
						if (checkMove(new Move(c1, r1, c2, r2), turn, board))
							movesPossible = true;
					}
				}
			}
		}
		if (!movesPossible)
			return Result.s;
		return Result.n;
	}

	private static void getMove(Tile[][] board, Team t) {
		Scanner consoleIn = new Scanner(System.in);
		while (!attemptMove(stringToMove(consoleIn.next().trim(), consoleIn.next().trim()), t, board)) {
			System.err.println("invalid move");
		}
	}

	private static Move stringToMove(String p1, String p2) {
		Move move = new Move();
		move.row1 = ABCto012(p1.charAt(0));
		move.row2 = ABCto012(p2.charAt(0));
		move.col1 = Integer.valueOf(p1.charAt(1)) - 49;
		move.col2 = Integer.valueOf(p2.charAt(1)) - 49;
		return move;
	}

	private static int ABCto012(char rowIn) {
		return (int) rowIn - 97;
	}

	private static char NumToABC(int c) {
		return (char) (c + 65);
	}

	private static boolean attemptMove(Move move, Team team, Tile[][] board) {
		if (checkMove(move, team, board))
			return false;
		board[move.row1][move.col1] = Tile.n;
		board[move.row2][move.col2] = teamTile(team);
		return true;
	}

	private static boolean checkMove(Move move, Team team, Tile[][] board) {
		int r1 = move.row1;
		int r2 = move.row2;
		int c1 = move.col1;
		int c2 = move.col2;

		if (r1 + direction(team) != r2 || Math.abs(c1 - c2) > 1 || board[r1][c1] != teamTile(team)
				|| (c1 - c2 > 0 && board[r2][c2] != oppTile(team)) || (c1 - c2 == 0 && board[r2][c2] != Tile.n))
			return false;
		return true;
	}

	private static Tile oppTile(Team t) {
		if (t == Team.b)
			return Tile.w;
		return Tile.b;
	}

	private static Tile teamTile(Team t) {
		if (t == Team.b)
			return Tile.b;
		return Tile.w;
	}

	private static void resetBoard(Tile[][] board) {
		for (int j = 0; j < board[0].length; j++)
			board[0][j] = Tile.b;
		for (int i = 1; i < board.length - 1; i++)
			for (int j = 0; j < board[0].length; j++)
				board[i][j] = Tile.n;
		for (int j = 0; j < board[0].length; j++)
			board[board.length - 1][j] = Tile.w;
	}

	private static int direction(Team t) {
		if (t == Team.w)
			return -1;
		return 1;
	}

	private static void nextTurn() {
		if (turn == Team.w)
			turn = Team.b;
		else
			turn = Team.w;
	}

	private static void displayBoard(Tile[][] board) {
		String output = "\t";
		for (int i = 1; i <= board[0].length; i++)
			output += i + "\t";
		System.out.println(output);
		output = "";
		for (int i = 0; i < board.length; i++) {
			output += NumToABC(i) + "\t";
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == Tile.n)
					output += "\t";
				else
					output += board[i][j] + "\t";
			}
			System.out.println(output);
			output = "";
		}
	}

}
