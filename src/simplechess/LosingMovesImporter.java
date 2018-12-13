package simplechess;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import simplechess.Main.Tile;

public class LosingMovesImporter {
	private static int rows = 0;
	private static int cols = 0;

	public static ArrayList<AugmentedMove> Import(String fileName, int r, int c) {
		rows = r;
		cols = c;
		ArrayList<AugmentedMove> moves = new ArrayList<AugmentedMove>();
		try {
			Scanner reader = new Scanner(new FileReader(fileName));
			while (reader.hasNextLine())
				moves.add(StringToAugMove(reader.nextLine().trim()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return moves;
	}

	public static void Export(AugmentedMove augMove, String fileName, boolean printExport) {
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(fileName, true));
			Tile[][] board = augMove.board;
			Move move = augMove.move;
			for (int r = 0; r < board.length; r++) {
				for (int c = 0; c < board[0].length; c++) {
					wr.append(board[r][c].toString());
					wr.append(" ");
				}
				wr.append(" ");
			}
			wr.append(";");
			wr.append("" + move.row1 + " " + move.col1 + " " + move.row2 + " " + move.col2 + '\n');
			wr.close();
			if (printExport)
				System.out.println("Exported move '" + move + "'.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static AugmentedMove StringToAugMove(String input) {
		if (!(input.equals(""))) {
			Scanner scn = new Scanner(input).useDelimiter(";");
			Tile[][] board = StringToBoard(scn.next());
			Move move = StringToMove(scn.next());
			return new AugmentedMove(move, board);
		} else {
			Tile[][] board = new Tile[rows][cols];
			Main.resetBoard(board);
			return new AugmentedMove(new Move(), board);
		}
	}

	private static Tile[][] StringToBoard(String in) {
		Tile[][] board = new Tile[rows][cols];
		Scanner scn = new Scanner(in);
		for (int r = 0; r < board.length; r++) {
			for (int c = 0; c < board[0].length; c++) {
				String value = scn.next();
				board[r][c] = Tile.valueOf(value);
			}
		}
		return board;
	}

	private static Move StringToMove(String in) {
		Scanner scn = new Scanner(in);
		return new Move(scn.nextInt(), scn.nextInt(), scn.nextInt(), scn.nextInt());
	}

}
