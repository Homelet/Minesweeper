package homelet.visual;

import com.apple.eawt.Application;
import homelet.GH.handlers.Layouter.SpringLayouter;
import homelet.GH.handlers.Layouter.SpringLayouter.Position;
import homelet.GH.utils.ToolBox;
import homelet.game.minesweeper.MineSweeper;
import homelet.game.minesweeper.PictureKey;

import javax.swing.*;
import java.awt.*;
import java.awt.desktop.QuitEvent;
import java.awt.desktop.QuitResponse;
import java.awt.event.WindowEvent;

public class Display extends JFrame{
	
	static final         String      VERSION     = "V1.4";
	private static final int         SAFE_VALUE  = 3;
	public static final  Color       LIGHT_GRAY  = new Color(0xc0c0c0);
	static final         Color       DARK_GRAY   = new Color(0x808080);
	static final         Color       WHITE       = new Color(0xffffff);
	static final         ImageIcon   ICON        = new ImageIcon(PictureKey.ICON.get().image());
	static final         int         DEFAULT_GAP = 10;
	public static final  int         MAX_ROW;
	public static final  int         MAX_COL;
	private final        ControlPane controlPane;
	private final        GamePane    gamePane;
	private final        MenuBarPane menuBar;
	
	static{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		MAX_ROW = (dimension.height - (50 + 2 * DEFAULT_GAP + 4) - (22 + 22 + 3 * DEFAULT_GAP) - 4) / MineSweeper.PICTURE_HEIGHT - SAFE_VALUE;
		MAX_COL = (dimension.width - (DEFAULT_GAP * 2 + 4)) / MineSweeper.PICTURE_WIDTH - SAFE_VALUE;
	}
	
	public Display() throws HeadlessException{
		super("MineSweeper XP");
		if(SystemTray.isSupported()){
			SystemTray systemTray = SystemTray.getSystemTray();
			try{
				systemTray.add(new TrayIcon(PictureKey.ICON.get().image()));
			}catch(AWTException e){
				e.printStackTrace();
			}
		}
		setIconImage(PictureKey.ICON.get().image());
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		controlPane = new ControlPane(this);
		gamePane = new GamePane(this);
		menuBar = new MenuBarPane(this);
		gamePane.loadGame();
		JBasePanel panel = new JBasePanel();
		panel.setBackground(LIGHT_GRAY);
		this.setContentPane(panel);
		this.setResizable(false);
		this.setLocationByPlatform(true);
		SpringLayouter layouter = new SpringLayouter(panel);
		// controlPane
		layouter.put(Position.CONSTRAIN_X, controlPane, DEFAULT_GAP, Position.CONSTRAIN_X, panel);
		layouter.put(Position.CONSTRAIN_Y, controlPane, DEFAULT_GAP, Position.CONSTRAIN_Y, panel);
		layouter.put(Position.CONSTRAIN_X_WIDTH, controlPane, -DEFAULT_GAP, Position.CONSTRAIN_X_WIDTH, panel);
		// gamePane
		layouter.put(Position.CONSTRAIN_X, gamePane, 0, Position.CONSTRAIN_X, controlPane);
		layouter.put(Position.CONSTRAIN_Y, gamePane, DEFAULT_GAP, Position.CONSTRAIN_Y_HEIGHT, controlPane);
		layouter.put(Position.CONSTRAIN_X_WIDTH, gamePane, -DEFAULT_GAP, Position.CONSTRAIN_X_WIDTH, panel);
		layouter.put(Position.CONSTRAIN_Y_HEIGHT, gamePane, -DEFAULT_GAP, Position.CONSTRAIN_Y_HEIGHT, panel);
		// menuBar
		setJMenuBar(menuBar);
		// init app
		String OsName = System.getProperty("os.name");
		// is mac
		if(OsName.contains("Mac")){
			Image       icon_image = PictureKey.ICON.get().image();
			Application app        = Application.getApplication();
			app.setAboutHandler(e->menuBar.showAboutGame());
			app.disableSuddenTermination();
			app.setQuitHandler((QuitEvent quitEvent, QuitResponse quitResponse)->{
				onQuitGame();
				quitResponse.performQuit();
			});
			app.setDockIconImage(icon_image);
		}
		pack();
	}
	
	void setDisplayDimension(Dimension dimension){
		Dimension gamePaneDI    = getGamePaneDimension(dimension);
		Dimension controlPaneDI = getControlPaneDimension(dimension);
		Dimension displayDI     = getDisplayDimension(gamePaneDI, controlPaneDI);
		ToolBox.setPreferredSize(this, displayDI);
		ToolBox.setPreferredSize(gamePane, gamePaneDI);
		ToolBox.setPreferredSize(controlPane, controlPaneDI);
		pack();
	}
	
	private Dimension getGamePaneDimension(Dimension dimension){
		return new Dimension(dimension.width + 4, dimension.height + 4);
	}
	
	private Dimension getControlPaneDimension(Dimension dimension){
		return new Dimension(dimension.width + 4, 50 + 2 * DEFAULT_GAP + 4);
	}
	
	private Dimension getDisplayDimension(Dimension game, Dimension control){
		return new Dimension(game.width + 2 * DEFAULT_GAP, game.height + control.height + 22 + 22 + 3 * DEFAULT_GAP);
	}
	
	public void showDisplay(){
		this.setVisible(true);
	}
	
	public ControlPane getControlPane(){
		return controlPane;
	}
	
	GamePane getGamePane(){
		return gamePane;
	}
	
	public MenuBarPane getMenuBarPane(){
		return menuBar;
	}
	
	void quitGame(){
		onQuitGame();
		System.exit(0);
	}
	
	private void onQuitGame(){
		gamePane.onExit();
		controlPane.onExit();
		menuBar.onExit();
		System.out.println("Exiting!");
	}
	
	@Override
	protected void processWindowEvent(WindowEvent e){
		if(e.getID() == WindowEvent.WINDOW_CLOSING)
			onQuitGame();
		super.processWindowEvent(e);
	}
}
