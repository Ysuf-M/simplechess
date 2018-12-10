package simplechess;

import java.util.ArrayList;
import java.util.Scanner;

//version 0.02

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

	public enum Mode {
		noPlayer, onePlayerW, onePlayerB, twoPlayer, trainingW, trainingB
	};

	public static Mode mode = Mode.noPlayer;

	public static Team turn = Team.w;

	public static Result forfeit = Result.n;

	public static AugmentedMove lastAugMove = new AugmentedMove(new Move(), new Tile[5][5]);

	public static ArrayList<AugmentedMove> losingMoves;

	public static int turns = 0;

	public static boolean training = false;

	public static void main(String[] args) {
		boolean done = false;
		while (!done) {
			lastAugMove = new AugmentedMove(new Move(),
					new Tile[lastAugMove.board.length][lastAugMove.board[0].length]);
			Tile[][] board = new Tile[lastAugMove.board.length][lastAugMove.board[0].length];
			losingMoves = LosingMovesImporter.Import(board.length + "x" + board[0].length, board.length,
					board[0].length);
			System.out.println("Total losing moves: " + losingMoves.size());
			turn = Team.w;
			turns = 0;
			forfeit = Result.n;
			startGame(board, Mode.trainingW);
			if (!exportMoves(board))
				done = true;
		}
	}

	private static void startGame(Tile[][] board, Mode m) {
		mode = m;
		switch (m) {
		case noPlayer:
			noPlayer(board);
			break;
		case trainingW:
			training = true;
			training(board, Team.w);
			break;
		case trainingB:
			training = true;
			training(board, Team.b);
			break;
		case onePlayerW:
			onePlayerW(board);
			break;
		case onePlayerB:
			onePlayerB(board);
			break;
		case twoPlayer:
			twoPlayer(board);
			break;
		}
	}

	private static void noPlayer(Tile[][] board) {
		resetBoard(board);
		resetBoard(lastAugMove.board);
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			playMove(board, turn);
			nextTurn();
		}
		endGame(board);
	}

	private static void twoPlayer(Tile[][] board) {
		resetBoard(board);
		resetBoard(lastAugMove.board);
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			getMove(board, turn);
			nextTurn();
		}
		endGame(board);
	}

	private static void onePlayerW(Tile[][] board) {
		resetBoard(board);
		resetBoard(lastAugMove.board);
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			getMove(board, Team.w);
			nextTurn();
			if (checkGame(board) == Result.n) {
				displayBoard(board);
				if (playMove(board, Team.b))
					nextTurn();
			}
		}
		endGame(board);
	}

	private static void onePlayerB(Tile[][] board) {
		resetBoard(board);
		resetBoard(lastAugMove.board);
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			playMove(board, Team.w);
			if (checkGame(board) == Result.n) {
				displayBoard(board);
				getMove(board, Team.b);
			}
		}
		endGame(board);
	}

	private static void training(Tile[][] board, Team t) {
		resetBoard(board);
		resetBoard(lastAugMove.board);
		while (checkGame(board) == Result.n) {
			displayBoard(board);
			if (t == Team.b)
				playRandomMove(board, Team.w);
			else
				playMove(board, t);
			nextTurn();
			if (checkGame(board) == Result.n) {
				displayBoard(board);
				if (t == Team.w)
					playRandomMove(board, Team.b);
				else
					playMove(board, t);
				nextTurn();
			}
		}
		endGame(board);
	}

	private static boolean playMove(Tile[][] board, Team t) {
		for (int r1 = 0; r1 < board.length; r1++) {
			for (int r2 = 0; r2 < board.length; r2++) {
				for (int c1 = 0; c1 < board[0].length; c1++) {
					for (int c2 = 0; c2 < board[0].length; c2++) {
						Move move = new Move(r1, c1, r2, c2);
						if (checkMove(move, t, board) && !checkIfLosingMove(move, t, board)) {
							copyArray(board, lastAugMove.board);
							lastAugMove.move = move;
							attemptMove(move, t, board);
							return true;
						}
					}
				}
			}
		}
		forfeit(t);
		return false;
	}

	private static void playRandomMove(Tile[][] board, Team t) {
		Move move = new Move();
		while (!checkMove(move, t, board)) {
			move.row1 = (int) Math.round((100 * Math.random()) % (board.length - 1));
			move.col1 = (int) Math.round((100 * Math.random()) % (board[0].length - 1));
			move.row2 = (int) Math.round((100 * Math.random()) % (board.length - 1));
			move.col2 = (int) Math.round((100 * Math.random()) % (board[0].length - 1));
		}
		attemptMove(move, t, board);
	}

	private static boolean checkIfLosingMove(Move move, Team t, Tile[][] board) {
		for (int i = 0; i < losingMoves.size(); i++) {
			if (arrayEquals(board, losingMoves.get(i).board) && move.equals(losingMoves.get(i).move))
				return true;
		}
		return false;
	}

	private static void endGame(Tile[][] board) {
		switch (checkGame(board)) {
		case b:
			displayBoard(board);
			System.out.println("Team black has won after " + turns + " turns!");
			break;
		case w:
			displayBoard(board);
			System.out.println("Team white has won after " + turns + " turns!");
			break;
		case s:
			displayBoard(board);
			System.out.println("It's a stalemate.");
		default:
		}
	}

	private static boolean exportMoves(Tile[][] board) {
		boolean isTraining = false;
		if (mode == Mode.trainingB || mode == Mode.trainingW)
			isTraining = true;
		if (lastAugMove.move.equals(new Move())) {
			System.out.println("Seems the game was rigged from the start.");
			return false;
		}
		if (((mode == Mode.noPlayer && checkGame(board) != Result.s)
				|| ((mode == Mode.onePlayerW || mode == Mode.trainingB) && checkGame(board) == Result.w)
				|| ((mode == Mode.onePlayerB || mode == Mode.trainingW) && checkGame(board) == Result.b))) {
			LosingMovesImporter.Export(lastAugMove, board.length + "x" + board[0].length);
			return true;
		}
		return isTraining;
	}

	private static Result checkGame(Tile[][] board) {
		if (forfeit != Result.n)
			return forfeit;
		for (int i = 0; i < board[0].length; i++)
			if (board[0][i] == Tile.w)
				return Result.w;
		for (int i = 0; i < board[0].length; i++)
			if (board[board.length - 1][i] == Tile.b)
				return Result.b;
		boolean whiteTilesLeft = false;
		boolean blackTilesLeft = false;
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				if (board[r][c] == Tile.w)
					whiteTilesLeft = true;
				if (board[r][c] == Tile.b)
					blackTilesLeft = true;
			}
		}
		if (!whiteTilesLeft)
			return Result.b;
		if (!blackTilesLeft)
			return Result.w;
		boolean movesPossible = false;
		for (int r1 = 0; r1 < board.length; r1++) {
			for (int c1 = 0; c1 < board[0].length; c1++) {
				for (int r2 = 0; r2 < board.length; r2++) {
					for (int c2 = 0; c2 < board[0].length; c2++) {
						Move move = new Move(r1, c1, r2, c2);
						if (checkMove(move, turn, board))
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

	private static void forfeit(Team t) {
		if (t == Team.b) {
			forfeit = Result.w;
			System.out.println("Team Black has forfeited!");
		} else {
			forfeit = Result.b;
			System.out.println("Team White has forfeited!");
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

	public static char NumToABC(int c) {
		return (char) (c + 65);
	}

	private static boolean attemptMove(Move move, Team team, Tile[][] board) {
		if (!checkMove(move, team, board))
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

		int diagMove = Math.abs(c1 - c2);
		int verMove = (r2 - r1) * direction(team);
		if (verMove > 2 || verMove < 1 || diagMove > 1
				|| (verMove == 2
						&& (diagMove != 0 || r1 != startRow(team, board) || board[r2 - direction(team)][c2] != Tile.n))
				|| board[r1][c1] != teamTile(team) || (diagMove > 0 && board[r2][c2] != oppTile(team))
				|| (diagMove == 0 && board[r2][c2] != Tile.n))
			return false;
		return true;
	}

	private static int startRow(Team team, Tile[][] board) {
		if (team == Team.w)
			return board.length - 1;
		return 0;
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

	public static void resetBoard(Tile[][] board) {
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
		turns++;
		if (turn == Team.w)
			turn = Team.b;
		else
			turn = Team.w;
	}

	private static boolean arrayEquals(Tile[][] board, Tile[][] board2) {
		for (int r = 0; r < board.length; r++)
			for (int c = 0; c < board[0].length; c++)
				if (board[r][c] != board2[r][c])
					return false;
		return true;
	}

	private static void copyArray(Tile[][] from, Tile[][] to) {
		for (int r = 0; r < from.length; r++)
			for (int c = 0; c < from[0].length; c++)
				to[r][c] = from[r][c];
	}

	public static void displayBoard(Tile[][] board) {
		if (!training) {
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

}
