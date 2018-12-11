package homelet.visual;

import homelet.GH.handlers.Layouter.GridBagLayouter;
import homelet.GH.handlers.Layouter.GridBagLayouter.GridConstrain.Anchor;
import homelet.GH.handlers.Layouter.GridBagLayouter.GridConstrain.Fill;
import homelet.GH.handlers.Layouter.SpringLayouter;
import homelet.GH.handlers.Layouter.SpringLayouter.Position;
import homelet.GH.utils.ToolBox;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class ControlPane extends JPanel implements ExitAction{
	
	private final LEDPane      mineNum;
	private final LEDPane      timer;
	private final StatusButton button;
	
	public ControlPane(Display display){
		this.mineNum = new LEDPane(0);
		this.timer = new LEDPane(0);
		this.button = new StatusButton();
		button.addActionListener((e)->{
			display.getMenuBarPane().initBoardWithAsk();
		});
		ToolBox.setPreferredSize(mineNum, 85, 50);
		ToolBox.setPreferredSize(timer, 85, 50);
		ToolBox.setPreferredSize(button, 50, 50);
		this.setBorder(new BevelBorder(BevelBorder.LOWERED, Display.WHITE, Display.DARK_GRAY));
		this.setBackground(Display.LIGHT_GRAY);
		SpringLayouter layouter = new SpringLayouter(this);
		// mineNum
		layouter.put(Position.CONSTRAIN_X, mineNum, Display.DEFAULT_GAP, Position.CONSTRAIN_X, this);
		layouter.put(Position.CONSTRAIN_Y, mineNum, Display.DEFAULT_GAP, Position.CONSTRAIN_Y, this);
		layouter.put(Position.CONSTRAIN_Y_HEIGHT, mineNum, -Display.DEFAULT_GAP, Position.CONSTRAIN_Y_HEIGHT, this);
		// timer
		layouter.put(Position.CONSTRAIN_Y, timer, 0, Position.CONSTRAIN_Y, mineNum);
		layouter.put(Position.CONSTRAIN_X_WIDTH, timer, -Display.DEFAULT_GAP, Position.CONSTRAIN_X_WIDTH, this);
		layouter.put(Position.CONSTRAIN_Y_HEIGHT, timer, 0, Position.CONSTRAIN_Y_HEIGHT, mineNum);
		// button
		JPanel          panel      = new JPanel();
		GridBagLayouter layouter_1 = new GridBagLayouter(panel);
		panel.setBackground(Display.LIGHT_GRAY);
		layouter_1.put(layouter_1.instanceOf(button, 0, 0).setAnchor(Anchor.CENTER).setFill(Fill.NONE).setWeight(100, 100));
		layouter.put(Position.CONSTRAIN_X, panel, Display.DEFAULT_GAP, Position.CONSTRAIN_X_WIDTH, mineNum);
		layouter.put(Position.CONSTRAIN_Y, panel, 0, Position.CONSTRAIN_Y, mineNum);
		layouter.put(Position.CONSTRAIN_X_WIDTH, panel, -Display.DEFAULT_GAP, Position.CONSTRAIN_X, timer);
		layouter.put(Position.CONSTRAIN_Y_HEIGHT, panel, 0, Position.CONSTRAIN_Y_HEIGHT, mineNum);
	}
	
	public LEDPane getMineNum(){
		return mineNum;
	}
	
	public LEDPane getTimer(){
		return timer;
	}
	
	public StatusButton getButton(){
		return button;
	}
	
	@Override
	public void onExit(){
	}
}
