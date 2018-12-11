package homelet.visual;

import homelet.game.minesweeper.PictureKey;

import javax.swing.*;

public class StatusButton extends JButton{
	
	private final ImageIcon press_normal = new ImageIcon(PictureKey.press_normal.get().image());
	private final ImageIcon press_clear  = new ImageIcon(PictureKey.press_clear.get().image());
	private final ImageIcon press_dead   = new ImageIcon(PictureKey.press_dead.get().image());
	private final ImageIcon raise_normal = new ImageIcon(PictureKey.raise_normal.get().image());
	private final ImageIcon raise_clear  = new ImageIcon(PictureKey.raise_clear.get().image());
	private final ImageIcon raise_dead   = new ImageIcon(PictureKey.raise_dead.get().image());
	
	StatusButton(){
		this.setBorderPainted(false);
		this.setToolTipText("Press To Restart The Game");
		normal();
	}
	
	public void normal(){
		this.setIcon(raise_normal);
		this.setPressedIcon(press_normal);
	}
	
	public void clear(){
		this.setIcon(raise_clear);
		this.setPressedIcon(press_clear);
	}
	
	public void dead(){
		this.setIcon(raise_dead);
		this.setPressedIcon(press_dead);
	}
}
