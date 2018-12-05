package simplechess;

import simplechess.Main.Tile;

public class AugmentedMove {
	public Move move;
	public Tile[][] board;
	
	public AugmentedMove (Move m, Tile[][] b) {
		move = m;
		board = b;
	}
	
	public String toString() {
		return move.toString();
	}
}
