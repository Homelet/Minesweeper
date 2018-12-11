package homelet.visual;

import homelet.GH.handlers.Layouter.SpringLayouter;
import homelet.GH.handlers.Layouter.SpringLayouter.Position;
import homelet.GH.visual.JCanvas;
import homelet.game.minesweeper.Levels;
import homelet.game.minesweeper.MineSweeper;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.io.File;

public class GamePane extends JPanel implements ExitAction{
	
	private final JCanvas     canvas;
	private final MineSweeper sweeper;
	private final Display     display;
	
	GamePane(Display display){
		this.display = display;
		this.canvas = new JCanvas("Canvas");
		this.sweeper = new MineSweeper(canvas.getCanvasThread(), display);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Display.WHITE, Display.DARK_GRAY));
		this.setBackground(Display.LIGHT_GRAY);
		SpringLayouter layouter = new SpringLayouter(this);
		// canvas
		layouter.put(Position.CONSTRAIN_X, canvas, 0, Position.CONSTRAIN_X, this);
		layouter.put(Position.CONSTRAIN_X_WIDTH, canvas, 0, Position.CONSTRAIN_X_WIDTH, this);
		layouter.put(Position.CONSTRAIN_Y, canvas, 0, Position.CONSTRAIN_Y, this);
		layouter.put(Position.CONSTRAIN_Y_HEIGHT, canvas, 0, Position.CONSTRAIN_Y_HEIGHT, this);
	}
	
	void loadGame(){
		SwingUtilities.invokeLater(()->{
			if(sweeper.loadGame()){
				setDimension();
				canvas.startRendering();
			}else{
				display.getMenuBarPane().initBoard();
			}
			display.getMenuBarPane().pause.setText(getSweeper().getBoard().getPause_Resume_Text());
		});
	}
	
	private void setDimension(){
		display.setDisplayDimension(sweeper.getBoard().getDimension());
	}
	
	void initBoard(Levels preset){
		sweeper.initBoard(preset);
		setDimension();
	}
	
	void initBoard(int rowNum, int colNum, int mines){
		sweeper.initBoard(rowNum, colNum, mines);
		setDimension();
	}
	
	@Override
	public void onExit(){
		getCanvas().stopRendering();
		getSweeper().getBoard().writeBoardToFile(new File("resource/data/minesweeper.history"));
	}
	
	private JCanvas getCanvas(){
		return canvas;
	}
	
	MineSweeper getSweeper(){
		return sweeper;
	}
}
