package simplechess;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class CheckForDuplicates {

	static ArrayList<AugmentedMove> moves = new ArrayList<AugmentedMove>();

	public static void main(String[] args) throws FileNotFoundException {
		moves = LosingMovesImporter.Import("5x5L", 5, 5);
		boolean foundDupes = false;
		while (checkAndRemoveDupes(moves)) {
			System.out.println("Removed duplicate");
			foundDupes = true;
		}
		System.out.println("Done");
		if (foundDupes)
			exportMoves(moves, "5x5L");
	}

	private static void exportMoves(ArrayList<AugmentedMove> moves, String fileName) throws FileNotFoundException {
		PrintWriter w = new PrintWriter(fileName);
		w.close();
		for (int i = 0; i < moves.size(); i++)
			LosingMovesImporter.Export(moves.get(i), fileName, false);
	}

	private static boolean checkAndRemoveDupes(ArrayList<AugmentedMove> moves) {
		for (int i = 0; i < moves.size()-1; i++)
			for (int j = i+1; j < moves.size(); j++)
				if (moves.get(i).equals(moves.get(j))) {
					moves.remove(j);
					return true;
				}
		return false;
	}

}
