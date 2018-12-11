package homelet.launcher;

import homelet.visual.Display;

import java.awt.*;

public class MinesweeperLauncher{
	
	public static void main(String[] args){
		EventQueue.invokeLater(()->{
			Display display = new Display();
			display.showDisplay();
		});
	}
}
