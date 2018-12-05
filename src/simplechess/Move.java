package simplechess;

public class Move {
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

		public Move(int r1, int c1, int r2, int c2) {
			this.col1 = c1;
			this.col2 = c2;
			this.row1 = r1;
			this.row2 = r2;
		}

		public String toString() {
			return "from " + Main.NumToABC(row1) + (col1 + 1) + " to "
					+ Main.NumToABC(row2) + (col2 + 1);
		}
}
