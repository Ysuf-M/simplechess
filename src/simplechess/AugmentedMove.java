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
	
	public boolean equals (AugmentedMove other) {
		return (move.equals(other.move) && Main.arrayEquals(board, other.board));
	}
}
