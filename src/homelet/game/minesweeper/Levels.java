package homelet.game.minesweeper;

public enum Levels{
	EASY(9, 9, 10, "       Easy:"),
	MEDIUM(16, 16, 40, "  Medium:"),
	EXPERT(16, 30, 99, "     Expert:");
	public final int row, col, mines;
	public final String rep;
	
	Levels(int row, int col, int mines, String rep){
		this.row = row;
		this.col = col;
		this.mines = mines;
		this.rep = rep;
	}
	
	public static Levels get(int index){
		switch(index){
			default:
			case 0:
				return EASY;
			case 1:
				return MEDIUM;
			case 2:
				return EXPERT;
		}
	}
}
