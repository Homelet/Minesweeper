package homelet.visual;

import homelet.GH.handlers.Layouter;
import homelet.GH.handlers.Layouter.GridBagLayouter;
import homelet.GH.handlers.Layouter.GridBagLayouter.GridConstrain.Anchor;
import homelet.GH.handlers.Layouter.GridBagLayouter.GridConstrain.Fill;
import homelet.GH.handlers.Layouter.GroupLayouter;
import homelet.GH.utils.ToolBox;
import homelet.game.minesweeper.Levels;
import homelet.visual.JInput.JInputField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuBarPane extends JMenuBar implements ExitAction{
	
	static final         String                             DEFAULT_VALUE = "-";
	private static final String[]                           ranking_keys  = new String[]{ "easy", "medium", "expert" };
	private static final String[]                           setUp_keys    = new String[]{ "enableDoubleClick", "mode", "customData-rol", "customData-col", "customData-mines" };
	private static final String                             padder        = "    ";
	private final        HashMap<Levels, RankingPersonInfo> ranking;
	private final        Display                            display;
	private              Levels                             currentLevel;
	private              int                                customRow, customCol, customMine;
	private boolean   isDoubleClickEnable;
	private PopupMenu popupMenu;
	
	MenuBarPane(Display display){
		this.display = display;
		ranking = new HashMap<>();
		initSetup();
		initRanking();
		createGameMenu();
	}
	
	private void initRanking(){
		String string = ToolBox.readTextFile(new File("resource/data/minesweeper.ranking"));
		if(string == null){
			defaultRanking();
			return;
		}
		try(Scanner scanner = new Scanner(string)){
			for(int line = 0; line < ranking_keys.length; line++){
				RankingPersonInfo info;
				Pattern pattern = Pattern.compile("<" + ranking_keys[line] + ">[\\s\\S]*" +
						"<name>([\\s\\S]*)</name>[\\s\\S]*" +
						"<time>([\\s\\S]*)</time>[\\s\\S]*" +
						"<timeStamp>([\\s\\S]*)</timeStamp>[\\s\\S]*" +
						"</" + ranking_keys[line] + ">");
				Matcher matcher = pattern.matcher(string);
				String  name    = DEFAULT_VALUE, time = DEFAULT_VALUE, timeStamp = DEFAULT_VALUE;
				if(matcher.find()){
					name = matcher.group(1);
					time = matcher.group(2);
					timeStamp = matcher.group(3);
				}
				info = new RankingPersonInfo(name, time, timeStamp);
				ranking.put(Levels.get(line), info);
			}
		}
	}
	
	private void initSetup(){
		String string = ToolBox.readTextFile(new File("resource/data/minesweeper.setup"));
		if(string == null){
			defaultSetUp();
			return;
		}
		try(Scanner scanner = new Scanner(string)){
			for(int line = 0; line < setUp_keys.length; line++){
				String  value   = scanner.next("<" + setUp_keys[line] + ">([\\s\\S]*)</" + setUp_keys[line] + ">");
				Pattern pattern = Pattern.compile(">([\\s\\S]*)</");
				Matcher matcher = pattern.matcher(value);
				if(matcher.find()){
					value = matcher.group(1);
				}
				switch(line){
					case 0:
						this.isDoubleClickEnable = Boolean.valueOf(value);
						break;
					case 1:
						if(value.equals("CUSTOM"))
							this.currentLevel = null;
						else
							this.currentLevel = Levels.valueOf(value);
						break;
					case 2:
						this.customRow = Integer.valueOf(value);
						break;
					case 3:
						this.customCol = Integer.valueOf(value);
						break;
					case 4:
						this.customMine = Integer.valueOf(value);
						break;
				}
			}
		}
	}
	
	private void defaultSetUp(){
		this.isDoubleClickEnable = true;
		this.currentLevel = Levels.EASY;
		this.customRow = 9;
		this.customCol = 9;
		this.customMine = 10;
	}
	
	private void defaultRanking(){
		ranking.put(Levels.EASY, RankingPersonInfo.defaultInfo);
		ranking.put(Levels.MEDIUM, RankingPersonInfo.defaultInfo);
		ranking.put(Levels.EXPERT, RankingPersonInfo.defaultInfo);
	}
	
	public boolean isDoubleClickEnable(){
		return isDoubleClickEnable;
	}
	
	JMenuItem         pause;
	JCheckBoxMenuItem enableDoubleClick;
	
	private void createGameMenu(){
		this.pause = new JMenuItem("Pause");
		JMenu                game      = new JMenu("Game");
		JMenuItem            aboutThis = new JMenuItem("About Game");
		JMenuItem            newGame   = new JMenuItem("New Game");
		ButtonGroup          group     = new ButtonGroup();
		JRadioButtonMenuItem easy      = new JRadioButtonMenuItem("Easy");
		JRadioButtonMenuItem medium    = new JRadioButtonMenuItem("Medium");
		JRadioButtonMenuItem expert    = new JRadioButtonMenuItem("Expert");
		JRadioButtonMenuItem custom    = new JRadioButtonMenuItem("Custom...");
		enableDoubleClick = new JCheckBoxMenuItem("Enable Double Click");
		JMenuItem ranking = new JMenuItem("Ranking");
		JMenuItem quit    = new JMenuItem("Quit Game");
		// init accelerator
		game.setMnemonic(KeyEvent.VK_G);
		aboutThis.setMnemonic(KeyEvent.VK_I);
		aboutThis.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, KeyEvent.META_DOWN_MASK));
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.META_DOWN_MASK));
		pause.setMnemonic(KeyEvent.VK_P);
		pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.META_DOWN_MASK));
		easy.setMnemonic(KeyEvent.VK_E);
		easy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK));
		medium.setMnemonic(KeyEvent.VK_M);
		medium.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK));
		expert.setMnemonic(KeyEvent.VK_X);
		expert.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK));
		custom.setMnemonic(KeyEvent.VK_C);
		custom.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.SHIFT_DOWN_MASK | KeyEvent.META_DOWN_MASK));
		enableDoubleClick.setMnemonic(KeyEvent.VK_D);
		enableDoubleClick.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.META_DOWN_MASK));
		ranking.setMnemonic(KeyEvent.VK_R);
		ranking.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.META_DOWN_MASK));
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.META_DOWN_MASK));
		// init action
		aboutThis.addActionListener((e)->showAboutGame());
		easy.addActionListener((e)->setCurrent(Levels.EASY));
		medium.addActionListener((e)->setCurrent(Levels.MEDIUM));
		expert.addActionListener((e)->setCurrent(Levels.EXPERT));
		custom.addActionListener((e)->setCustom(customRow, customCol, customMine));
		{
			if(currentLevel == null){
				custom.setSelected(true);
			}else{
				switch(currentLevel){
					case EASY:
						easy.setSelected(true);
						break;
					case MEDIUM:
						medium.setSelected(true);
						break;
					case EXPERT:
						expert.setSelected(true);
						break;
				}
			}
		}
		enableDoubleClick.addActionListener((e)->onEnableDoubleClick(enableDoubleClick));
		enableDoubleClick.setSelected(isDoubleClickEnable);
		ranking.addActionListener((e)->showRankingList());
		newGame.addActionListener((e)->initBoardWithAsk());
		pause.addActionListener((e)->pause.setText(display.getGamePane().getSweeper().getBoard().pause_resume()));
		quit.addActionListener((e)->display.quitGame());
		group.add(easy);
		group.add(medium);
		group.add(expert);
		group.add(custom);
		game.add(aboutThis);
		game.addSeparator();
		game.add(newGame);
		game.add(pause);
		game.addSeparator();
		game.add(easy);
		game.add(medium);
		game.add(expert);
		game.add(custom);
		game.addSeparator();
		game.add(enableDoubleClick);
		game.addSeparator();
		game.add(ranking);
		game.addSeparator();
		game.add(quit);
		this.add(game);
	}
	
	private void onEnableDoubleClick(JCheckBoxMenuItem enableDoubleClick){
		isDoubleClickEnable = enableDoubleClick.isSelected();
	}
	
	private void setCurrent(Levels levels){
		currentLevel = levels;
		initBoardWithAsk();
	}
	
	private void setCustom(int row, int col, int mines){
		int[] ints   = CustomBoardInit.showCustomBoardInitializer(display, row, col, mines);
		int   result = -1;
		if(ints == null){
			result = JOptionPane.showConfirmDialog(display, "Invalid Inputs!", "Custom Initializer Exception - Invalid Inputs", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
			switch(result){
				case JOptionPane.OK_OPTION:
					setCustom(row, col, mines);
				case JOptionPane.CANCEL_OPTION:
			}
			return;
		}else if(ints[2] < 0){
			result = JOptionPane.showConfirmDialog(display, "Mine Number Can Not be Negative!", "Custom Initializer Exception - Negative Mines", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}else if(ints[0] <= 0){
			result = JOptionPane.showConfirmDialog(display, "Row Can Not be Negative or Zero!", "Custom Initializer Exception - Negative Rows", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}else if(ints[1] < 8){
			result = JOptionPane.showConfirmDialog(display, "Column Can Not be narrower than 8!", "Custom Initializer Exception - Cols Too Narrow", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}else if(ints[0] > Display.MAX_ROW){
			result = JOptionPane.showConfirmDialog(display, "Row is Greater than the Maximum Row (" + Display.MAX_ROW + ") the Display Can Contains!\nPlease Try again!\nIf Your Have Switched a Different Resolution Display, please Reopen the Application!", "Custom Initializer Exception - Rows Exceed Limit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}else if(ints[1] > Display.MAX_COL){
			result = JOptionPane.showConfirmDialog(display, "Column is Greater than the Maximum Column (" + Display.MAX_COL + ") the Display Can Contains!\nPlease Try again!\nIf Your Have Switched a Different Resolution Display, please Reopen the Application!", "Custom Initializer Exception - Cols Exceed Limit", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}else if(ints[0] * ints[1] < ints[2]){
			result = JOptionPane.showConfirmDialog(display, "Can Not Contain " + ints[2] + " mines in a " + ints[0] + " x " + ints[1] + " board!", "Custom Initializer Exception - Unexpected Mine Number", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
		}
		if(result != -1){
			switch(result){
				case JOptionPane.OK_OPTION:
					setCustom(ints[0], ints[1], ints[2]);
				case JOptionPane.CANCEL_OPTION:
			}
		}else{
			setCurrent(ints[0], ints[1], ints[2]);
		}
	}
	
	private void setCurrent(int row, int col, int mines){
		this.currentLevel = null;
		this.customRow = row;
		this.customCol = col;
		this.customMine = mines;
		initBoardWithAsk();
	}
	
	void initBoardWithAsk(){
		if(askSaving())
			initBoard();
	}
	
	private boolean askSaving(){
		if(!display.getGamePane().getSweeper().getBoard().isFinished()){
			int result = JOptionPane.showConfirmDialog(display, "Your Current Game State will not be saved!", "New Game", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null);
			switch(result){
				default:
				case JOptionPane.CANCEL_OPTION:
				case JOptionPane.NO_OPTION:
					return false;
				case JOptionPane.YES_OPTION:
					return true;
			}
		}
		return true;
	}
	
	void initBoard(){
		if(currentLevel != null){
			display.getGamePane().initBoard(currentLevel);
		}else{
			display.getGamePane().initBoard(customRow, customCol, customMine);
		}
	}
	
	// called when winning a game
	public void checkRanking(long time){
		// if null means using a custom mode
		if(currentLevel == null)
			return;
		RankingPersonInfo info = ranking.get(currentLevel);
		if(info.time.equals("-") || Long.compare(time, Long.parseLong(info.time)) <= 0){
			String name = (String) JOptionPane.showInputDialog(display, "Congratulations!\nYou Have Just Achieved a New High Score!\nPlease Leave Your name Below.", "Congratulations", JOptionPane.QUESTION_MESSAGE, Display.ICON, null, null);
			info = new RankingPersonInfo(name, String.valueOf(time), ToolBox.getDateInfo());
			ranking.put(currentLevel, info);
		}
	}
	
	private void showRankingList(){
		StringBuilder builder = new StringBuilder();
		for(int index = 0; index < 3; index++){
			Levels            l    = Levels.get(index);
			RankingPersonInfo info = ranking.get(l);
			builder.append(l.rep);
			builder.append(padder);
			builder.append(info.name);
			builder.append(padder);
			builder.append(info.time);
			if(!info.time.equals("-"))
				builder.append(" Seconds");
			builder.append(padder);
			builder.append(info.timeStamp);
			builder.append('\n');
		}
		JOptionPane.showMessageDialog(display, builder.toString(), "Ranking", JOptionPane.INFORMATION_MESSAGE, Display.ICON);
	}
	
	public void showAboutGame(){
		String content = "MineSweeper XP " + Display.VERSION + "\nTo Report a Bug or Provide Feedback Please Contact:\nGame Author: homeletwei@163.com (Mr. Wei)\nAll Rights Reserved.";
		JOptionPane.showMessageDialog(display, content, "About Game", JOptionPane.INFORMATION_MESSAGE, Display.ICON);
	}
	
	@Override
	public void onExit(){
		writeRanking();
		writeSetUp();
	}
	
	private void writeRanking(){
		File rankingList = new File("resource/data/minesweeper.ranking");
		rankingList.setWritable(true);
		try(FileWriter writer = new FileWriter(rankingList)){
			for(int line = 0; line < 3; line++){
				Levels            levels = Levels.get(line);
				RankingPersonInfo info   = ranking.get(levels);
				String builder = "<" + ranking_keys[line] + ">\n" +
						"<name>" + info.name + "</name>\n" +
						"<time>" + info.time + "</time>\n" +
						"<timeStamp>" + info.timeStamp + "</timeStamp>\n" +
						"</" + ranking_keys[line] + ">\n";
				writer.write(builder);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Finished Writing Ranking : " + rankingList.setReadOnly());
	}
	
	private void writeSetUp(){
		File setUp = new File("resource/data/minesweeper.setup");
		setUp.setWritable(true);
		try(FileWriter writer = new FileWriter(setUp)){
			for(int line = 0; line < 5; line++){
				StringBuilder builder = new StringBuilder();
				builder.append("<").append(setUp_keys[line]).append(">");
				switch(line){
					case 0:
						builder.append(isDoubleClickEnable);
						break;
					case 1:
						if(this.currentLevel == null)
							builder.append("CUSTOM");
						else
							builder.append(currentLevel.toString());
						break;
					case 2:
						builder.append(customRow);
						break;
					case 3:
						builder.append(customCol);
						break;
					case 4:
						builder.append(customMine);
						break;
				}
				builder.append("</").append(setUp_keys[line]).append(">\n");
				writer.write(builder.toString());
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Finished Writing SetUp : " + setUp.setReadOnly());
	}
}

class RankingPersonInfo{
	
	static final RankingPersonInfo defaultInfo = new RankingPersonInfo(MenuBarPane.DEFAULT_VALUE, MenuBarPane.DEFAULT_VALUE, MenuBarPane.DEFAULT_VALUE);
	//
	final        String            timeStamp;
	final        String            name;
	final        String            time;
	
	RankingPersonInfo(String name, String time, String timeStamp){
		this.name = name;
		this.time = time;
		this.timeStamp = timeStamp;
	}
}

class CustomBoardInit extends JDialog{
	
	private static final Dimension  fieldDimension = new Dimension(200, 50);
	private static final Dimension  labelDimension = new Dimension(100, 50);
	private static final Dimension  panelDimension = new Dimension(fieldDimension.width + labelDimension.width + 3 * Display.DEFAULT_GAP, fieldDimension.height * 3 + 4 * Display.DEFAULT_GAP + 30);
	private              NestedPane nestedPane;
	
	private CustomBoardInit(Frame owner, int row, int col, int mines){
		super(owner, "Custom Mode Initializer", true);
		nestedPane = new NestedPane(this, row, col, mines);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(owner);
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				setVisible(false);
			}
		});
		Layouter.GridBagLayouter layouter = new GridBagLayouter(this);
		layouter.put(layouter.instanceOf(nestedPane, 0, 0).setWeight(100, 100).setFill(Fill.BOTH).setAnchor(Anchor.CENTER));
		ToolBox.setPreferredSize(this, panelDimension.width, panelDimension.height + 22);
		this.pack();
	}
	
	static int[] showCustomBoardInitializer(Frame owner, int row, int col, int mines){
		CustomBoardInit init = new CustomBoardInit(owner, row, col, mines);
		init.setVisible(true);
		init.dispose();
		try{
			return init.nestedPane.getValue();
		}catch(NumberFormatException e){
			return null;
		}
	}
	
	class NestedPane extends JPanel{
		
		JInputField rowField;
		JInputField colField;
		JInputField minesField;
		
		NestedPane(CustomBoardInit parent, int row, int col, int mines){
			JLabel rowLabel = new JLabel("Row:");
			ToolBox.setPreferredSize(rowLabel, labelDimension);
			rowField = new JInputField("row", fieldDimension, true);
			rowField.setContent(String.valueOf(row));
			rowLabel.setLabelFor(rowField);
			JLabel colLabel = new JLabel("Column:");
			ToolBox.setPreferredSize(colLabel, labelDimension);
			colField = new JInputField("col", fieldDimension, true);
			colField.setContent(String.valueOf(col));
			colLabel.setLabelFor(colField);
			JLabel minesLabel = new JLabel("Mines:");
			ToolBox.setPreferredSize(minesLabel, labelDimension);
			minesField = new JInputField("mines", fieldDimension, true);
			minesField.setContent(String.valueOf(mines));
			colLabel.setLabelFor(minesField);
			JButton ok = new JButton("OK");
			ok.addActionListener((e)->parent.setVisible(false));
			ToolBox.setPreferredSize(ok, new Dimension(80, 30));
			ok.addKeyListener(new KeyAdapter(){
				@Override
				public void keyReleased(KeyEvent e){
					if(e.getKeyCode() == KeyEvent.VK_ENTER)
						ok.doClick();
				}
			});
			Layouter.GroupLayouter layouter = new GroupLayouter(this, true, true);
			layouter.add(0, 0, new Component[][]{
					{ rowLabel, rowField },
					{ colLabel, colField },
					{ minesLabel, minesField },
			});
			layouter.add(3, 1, ok);
			ToolBox.setPreferredSize(this, panelDimension);
		}
		
		int[] getValue() throws NumberFormatException{
			return new int[]{ Integer.parseInt(rowField.getTextComponent().getText()),
					Integer.parseInt(colField.getTextComponent().getText()),
					Integer.parseInt(minesField.getTextComponent().getText()) };
		}
	}
}
