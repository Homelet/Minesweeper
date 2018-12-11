package homelet.game.minesweeper;

import homelet.GH.visual.CanvasThread;
import homelet.visual.Display;

import java.io.File;

public class MineSweeper{
	
	public static final int PICTURE_WIDTH = 32, PICTURE_HEIGHT = 32;
	static final  int[][]      steps = new int[][]{ { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, -1 }, { 0, 1 }, { 1, -1 }, { 1, 0 }, { 1, 1 } };
	private       Board        board;
	private final CanvasThread thread;
	private final Display      display;
	
	public MineSweeper(CanvasThread thread, Display display){
		this.display = display;
		this.thread = thread;
	}
	
	public boolean loadGame(){
		Board board = Board.loadBoardFromFile(display, thread.getRenderManager(), new File("resource/data/minesweeper.history"));
		if(board == null){
			return false;
		}else{
			this.board = board;
			return true;
		}
	}
	
	public void initBoard(Levels preset){
		initBoard(preset.row, preset.col, preset.mines);
	}
	
	public void initBoard(int rowNum, int colNum, int mines){
		thread.stopRendering();
		thread.getRenderManager().removeAllRender();
		thread.getRenderManager().removeAllPost();
		board = new Board(display, thread.getRenderManager(), rowNum, colNum, mines);
		thread.startRendering();
	}
	
	public Board getBoard(){
		return board;
	}
}
