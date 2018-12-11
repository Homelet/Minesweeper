package homelet.game.minesweeper;

import homelet.GH.handlers.GH;
import homelet.GH.utils.Border;
import homelet.GH.visual.ActionsManager;
import homelet.GH.visual.interfaces.LocatableRender;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Blocks extends ActionsManager implements LocatableRender{
	
	// info
	private final Board board;
	private final int   rowIndex, colIndex;
	private static final Point   ver = new Point(0, 0);
	private final        Point   vertex;
	final                boolean isMine;
	// var
	PictureKey current;
	boolean    flagged;
	boolean    revealed;
	
	Blocks(Board board, int rowIndex, int colIndex, boolean isMine){
		this(board, rowIndex, colIndex, isMine, PictureKey.BLOCK, false, false);
	}
	
	// load a block from a known from
	Blocks(Board board, int rowIndex, int colIndex, boolean isMine, PictureKey current, boolean flagged, boolean revealed){
		this.board = board;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.isMine = isMine;
		this.vertex = new Point(colIndex * MineSweeper.PICTURE_WIDTH, rowIndex * MineSweeper.PICTURE_WIDTH);
		this.current = current;
		this.flagged = flagged;
		this.revealed = revealed;
	}
	
	@Override
	public Dimension getSize(){
		return board.getUnitSize();
	}
	
	@Override
	public Point getVertex(Rectangle rectangle){
		return vertex;
	}
	
	@Override
	public void tick(){}
	
	@Override
	public void render(Graphics2D g){
		if(board.pausing())
			return;
		if(board.isFinished() && isMine){
			if(isFlagged())
				GH.draw(g, PictureKey.MINE_CORRECT.get().image(), ver, null);
			else
				GH.draw(g, PictureKey.MINE_WRONG.get().image(), ver, null);
		}else{
			GH.draw(g, current.get().image(), ver, null);
		}
		if(isHovering())
			Border.drawBorder(g, Border.RECTANGULAR, g.getClipBounds(), Color.RED, 2, 0);
	}
	
	int search(){
		int sum = 0;
		for(int[] step : MineSweeper.steps){
			int newRow = rowIndex + step[0];
			int newCol = colIndex + step[1];
			if(board.checkAvailable(newRow, newCol)){
				if(board.getBlocks(newRow, newCol).isMine)
					sum++;
			}
		}
		return sum;
	}
	
	void fill(int val){
		if(isFlagged())
			unflag();
		this.current = PictureKey.get(val);
	}
	
	boolean isFilled(){
		return this.current == PictureKey.L_0;
	}
	
	private void flag(){
		if(!board.canFlag())
			return;
		board.plusFlag();
		current = PictureKey.FLAG;
		flagged = true;
	}
	
	private void unflag(){
		if(!board.canUnFlag())
			return;
		board.removeFlag();
		current = PictureKey.BLOCK;
		flagged = false;
	}
	
	boolean isFlagged(){
		return flagged;
	}
	
	@Override
	public void onMousePress(MouseEvent e){
		if(board.isFinished() || board.pausing())
			return;
		if(e.getClickCount() == 1){
			switch(e.getButton()){
				// left check
				case MouseEvent.BUTTON1:
					onClick();
					break;
				// right mark
				case MouseEvent.BUTTON3:
					toggleFlag();
					break;
			}
		}else if(e.getClickCount() == 2){
			onDoubleClick();
		}
		board.checkIsGameFinished();
	}
	
	private void toggleFlag(){
		if(revealed)
			return;
		if(current == PictureKey.BLOCK)
			flag();
		else if(current == PictureKey.FLAG)
			unflag();
	}
	
	private void onClick(){
		if(flagged || revealed)
			return;
		if(isMine){
			board.finish(false);
		}else{
			board.floodFill(rowIndex, colIndex);
		}
		revealed = true;
	}
	
	private void onDoubleClick(){
		if(!board.display.getMenuBarPane().isDoubleClickEnable())
			return;
		for(int[] step : MineSweeper.steps){
			int newRow = rowIndex + step[0];
			int newCol = colIndex + step[1];
			if(!board.checkAvailable(newRow, newCol))
				continue;
			board.getBlocks(newRow, newCol).onClick();
		}
	}
}
