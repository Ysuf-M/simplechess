package simplechess;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import simplechess.Main.Tile;

public class LosingMovesImporter {
	private static int rows = 0;
	private static int cols = 0;
	public static AugmentedMove[] Import(String fileName, int r, int c) {
		rows = r;
		cols = c;
		AugmentedMove[] losingMoves = new AugmentedMove[100];
		try {
			int i = 0;
			Scanner reader = new Scanner(new FileReader(fileName));
			while (reader.hasNextLine() && i < 100) {
				losingMoves[i] = StringToAugMove(reader.nextLine().trim());
				i++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return losingMoves;
	}
	
	public static void Export (AugmentedMove augMove, String fileName) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(fileName));
			Tile[][] board = augMove.board;
			Move move = augMove.move;
			for (int c = 0; c < board[0].length; c++) {
				for (int r = 0; r < board.length; r++) {
					wr.write(board[r][c].toString());
				}
				wr.write(".");
			}
			wr.write(";");
			wr.write("" + move.row1 + "." + move.col1 + "." + move.row2 + "." + move.col2);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static AugmentedMove StringToAugMove(String input) {
		Scanner scn = new Scanner(input).useDelimiter(";");
		Tile[][] board = StringToBoard(scn.next());
		Move move = StringToMove(scn.next());
		return new AugmentedMove(move, board);
	}

	private static Tile[][] StringToBoard(String in) {
		Tile[][] board = new Tile[rows][cols];
		Scanner scn = new Scanner(in).useDelimiter(".");
		for (int c = 0; c < board[0].length; c++) {
			for (int r = 0; r < board.length; r++) {
				board[r][c] = Tile.valueOf(scn.next());
			}
		}
		return board;
	}

	private static Move StringToMove(String in) {
		Scanner scn = new Scanner(in).useDelimiter(".");
		return new Move(scn.nextInt(), scn.nextInt(), scn.nextInt(),
				scn.nextInt());
	}

}
