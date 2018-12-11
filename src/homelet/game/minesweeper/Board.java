package homelet.game.minesweeper;

import homelet.GH.StringDrawer.StringDrawer.StringDrawer;
import homelet.GH.StringDrawer.StringDrawer.StringDrawerException;
import homelet.GH.utils.Alignment;
import homelet.GH.utils.ToolBox;
import homelet.GH.visual.RenderManager;
import homelet.GH.visual.interfaces.Renderable;
import homelet.visual.Display;
import homelet.visual.LEDPane;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Board implements Renderable{
	
	private static       Font      LED      = LEDPane.LED.deriveFont(40.f);
	private static final Dimension unitSize = new Dimension(MineSweeper.PICTURE_WIDTH, MineSweeper.PICTURE_HEIGHT);
	private              long      lastNano = System.nanoTime();
	private final        int       row, col, mines;
	private final Blocks[][]   blocks;
	final         Display      display;
	// util
	private       boolean      finished   = false;
	private       long         time       = 0;
	private       int          flaggedNum = 0;
	private       boolean      pausing    = false;
	private       StringDrawer drawer;
	
	Board(Display display, RenderManager manager, int rowNum, int colNum, int mines){
		this.display = display;
		this.row = rowNum;
		this.col = colNum;
		this.mines = mines;
		this.blocks = new Blocks[rowNum][colNum];
		init(manager);
		this.display.getControlPane().getButton().normal();
		this.display.getControlPane().getTimer().reset();
		updateLeftMines();
		initDrawer();
	}
	
	static Board loadBoardFromFile(Display display, RenderManager renderManager, File file){
		String string = ToolBox.readTextFile(file);
		if(string == null){
			return null;
		}
		// finished
		if(Boolean.valueOf(ToolBox.findAll(string, "<finished>([\\S\\s]*)</finished>", 1))){
			return null;
		}
		int        rowNum     = Integer.parseInt(ToolBox.findAll(string, "<rowNum>([\\S\\s]*)</rowNum>", 1));
		int        colNum     = Integer.parseInt(ToolBox.findAll(string, "<colNum>([\\S\\s]*)</colNum>", 1));
		int        mines      = Integer.parseInt(ToolBox.findAll(string, "<mines>([\\S\\s]*)</mines>", 1));
		long       time       = Long.parseLong(ToolBox.findAll(string, "<time>([\\S\\s]*)</time>", 1));
		int        flaggedNum = Integer.parseInt(ToolBox.findAll(string, "<flaggedNum>([\\S\\s]*)</flaggedNum>", 1));
		boolean    pausing    = Boolean.parseBoolean(ToolBox.findAll(string, "<pausing>([\\S\\s]*)</pausing>", 1));
		Blocks[][] blocks     = new Blocks[rowNum][colNum];
		Board      board      = new Board(display, rowNum, colNum, mines, time, flaggedNum, pausing, blocks);
		renderManager.addPostTargets(board);
		for(int row = 0; row < rowNum; row++){
			for(int col = 0; col < colNum; col++){
				String str = ToolBox.findAll(string, "<block-" + row + "-" + col + ">([\\S\\s]*)</block-" + row + "-" + col + ">", 1);
				blocks[row][col] = loadBlocks(renderManager, row, col, board, str);
			}
		}
		return board;
	}
	
	private static Blocks loadBlocks(RenderManager manager, int row, int col, Board board, String string){
		boolean    isMine   = Boolean.valueOf(ToolBox.findAll(string, "<isMine>([\\S\\s]*)</isMine>", 1));
		PictureKey current  = PictureKey.valueOf(ToolBox.findAll(string, "<current>([\\S\\s]*)</current>", 1));
		boolean    flagged  = Boolean.valueOf(ToolBox.findAll(string, "<flagged>([\\S\\s]*)</flagged>", 1));
		boolean    revealed = Boolean.valueOf(ToolBox.findAll(string, "<revealed>([\\S\\s]*)</revealed>", 1));
		Blocks     blocks   = new Blocks(board, row, col, isMine, current, flagged, revealed);
		manager.addTargets(blocks);
		return blocks;
	}
	
	public void writeBoardToFile(File file){
		// exceed history will not be saved
		if(finished || row > Display.MAX_ROW || col > Display.MAX_COL){
			if(file.exists())
				file.delete();
			return;
		}
		file.setWritable(true);
		try(FileWriter writer = new FileWriter(file)){
			StringBuilder builder = new StringBuilder();
			builder.append("<finished>").append(finished).append("</finished>\n");
			builder.append("<rowNum>").append(row).append("</rowNum>\n");
			builder.append("<colNum>").append(col).append("</colNum>\n");
			builder.append("<mines>").append(mines).append("</mines>\n");
			builder.append("<flaggedNum>").append(flaggedNum).append("</flaggedNum>\n");
			builder.append("<pausing>").append(pausing).append("</pausing>\n");
			builder.append("<time>").append(time).append("</time>\n");
			for(int row = 0; row < this.row; row++){
				for(int col = 0; col < this.col; col++){
					Blocks blocks = getBlocks(row, col);
					String blocksBuilder = "<block-" + row + '-' + col + ">" +
							"<isMine>" + blocks.isMine + "</isMine>" +
							"<current>" + blocks.current.toString() + "</current>" +
							"<flagged>" + blocks.flagged + "</flagged>" +
							"<revealed>" + blocks.revealed + "</revealed>" +
							"</block-" + row + '-' + col + ">\n";
					builder.append(blocksBuilder);
				}
			}
			writer.write(builder.toString());
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Finished Writing History : " + file.setReadOnly());
	}
	
	private Board(Display display, int row, int col, int mines, long time, int flaggedNum, boolean pausing, Blocks[][] blocks){
		this.display = display;
		this.row = row;
		this.col = col;
		this.mines = mines;
		this.time = time;
		this.flaggedNum = flaggedNum;
		this.pausing = pausing;
		this.blocks = blocks;
		this.display.getControlPane().getButton().normal();
		this.display.getControlPane().getTimer().setValue(time);
		updateLeftMines();
		initDrawer();
	}
	
	private void initDrawer(){
		drawer = new StringDrawer();
		drawer.setFont(Board.LED);
		drawer.setAlign(Alignment.CENTER);
		drawer.setTextAlign(Alignment.TOP);
		drawer.initializeContents("PAUSING");
		drawer.setColor(Color.RED);
	}
	
	private void setTime(){
		if(System.nanoTime() - lastNano >= 1000000000){
			if(!pausing){
				time++;
				display.getControlPane().getTimer().setValue(time);
			}
			lastNano = System.nanoTime();
		}
	}
	
	boolean canFlag(){
		return flaggedNum < mines;
	}
	
	boolean canUnFlag(){
		return flaggedNum >= 0;
	}
	
	public boolean isFinished(){
		return finished;
	}
	
	void finish(boolean win){
		this.finished = true;
		if(win){
			System.out.println("Win");
			display.getControlPane().getButton().clear();
			display.getMenuBarPane().checkRanking(time);
		}else{
			System.out.println("Lose");
			display.getControlPane().getButton().dead();
		}
		time = 0;
	}
	
	private void updateLeftMines(){
		display.getControlPane().getMineNum().setValue(mines - flaggedNum);
	}
	
	void removeFlag(){
		flaggedNum--;
		updateLeftMines();
	}
	
	void plusFlag(){
		flaggedNum++;
		updateLeftMines();
	}
	
	public Dimension getDimension(){
		return new Dimension(MineSweeper.PICTURE_WIDTH * col, MineSweeper.PICTURE_HEIGHT * row);
	}
	
	private void init(RenderManager manager){
		manager.addPostTargets(this);
		boolean[][] mine = genMines();
		for(int row = 0; row < this.row; row++){
			for(int col = 0; col < this.col; col++){
				blocks[row][col] = new Blocks(this, row, col, mine[row][col]);
				manager.addTargets(blocks[row][col]);
			}
		}
	}
	
	private boolean[][] genMines(){
		boolean[][] mine = new boolean[row][col];
		// first put all mines into the field
		for(int i = 0; i < mines; i++){
			if(i >= row * col)
				return mine;
			int rowI = i / col;
			int colI = i % col;
			mine[rowI][colI] = true;
		}
		// fisher-yates
		final int max = row * col;
		for(int index = max - 1; index >= 0; index--){
			// from [0, index] rand fetch num
			int rand = (int) ToolBox.random(0, index + 1);
			swap(mine, rand / col, rand % col, index / col, index % col);
		}
		return mine;
	}
	
	void floodFill(int row, int col){
		if(!checkAvailable(row, col))
			return;
		Blocks block = getBlocks(row, col);
		if(block.isFilled())
			return;
		int search = block.search();
		block.fill(search);
		if(search > 0)
			return;
		for(int[] ints : MineSweeper.steps){
			floodFill(row + ints[0], col + ints[1]);
		}
	}
	
	void checkIsGameFinished(){
		for(int row = 0; row < this.row; row++){
			for(int col = 0; col < this.col; col++){
				Blocks blocks = getBlocks(row, col);
				if(blocks.isMine){
					if(!blocks.isFlagged())
						return;
				}
			}
		}
		finish(true);
	}
	
	private void swap(boolean[][] mine, int row1, int col1, int row2, int col2){
		boolean v2 = mine[row2][col2];
		mine[row2][col2] = mine[row1][col1];
		mine[row1][col1] = v2;
	}
	
	boolean pausing(){
		return pausing;
	}
	
	private static final String PAUSE  = "Pause";
	private static final String RESUME = "Resume";
	
	public String getPause_Resume_Text(){
		if(pausing)
			return RESUME;
		else
			return PAUSE;
	}
	
	public String pause_resume(){
		pausing = !pausing;
		return getPause_Resume_Text();
	}
	
	boolean checkAvailable(int row, int col){
		return row < this.row && row >= 0 && col < this.col && col >= 0;
	}
	
	Blocks getBlocks(int row, int col){
		return blocks[row][col];
	}
	
	Dimension getUnitSize(){
		return unitSize;
	}
	
	@Override
	public void tick(){
		if(!isFinished())
			setTime();
	}
	
	@Override
	public void render(Graphics2D g){
		if(pausing){
			Rectangle bounds = g.getClipBounds();
			g.setColor(Display.LIGHT_GRAY);
			g.fill(bounds);
			try{
				drawer.updateGraphics(g);
				drawer.setFrame(bounds);
				drawer.validate();
				drawer.draw();
			}catch(StringDrawerException e){
				e.printStackTrace();
			}
		}
	}
}
