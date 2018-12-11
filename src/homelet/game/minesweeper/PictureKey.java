package homelet.game.minesweeper;

import homelet.GH.utils.Pictures;

import javax.swing.*;

public enum PictureKey{
	BLOCK,
	FLAG,
	MINE_CORRECT,
	MINE_WRONG,
	L_0,
	L_1,
	L_2,
	L_3,
	L_4,
	L_5,
	L_6,
	L_7,
	L_8,
	press_clear,
	press_dead,
	press_normal,
	raise_clear,
	raise_dead,
	raise_normal,
	ICON;
	
	static{
		// init picture lib
		try{
			Pictures.put(PictureKey.BLOCK, "resource/game/block.png");
			Pictures.put(PictureKey.FLAG, "resource/game/flag.png");
			Pictures.put(PictureKey.MINE_CORRECT, "resource/game/mine_correct.png");
			Pictures.put(PictureKey.MINE_WRONG, "resource/game/mine_wrong.png");
			Pictures.put(PictureKey.L_0, "resource/game/0.png");
			Pictures.put(PictureKey.L_1, "resource/game/1.png");
			Pictures.put(PictureKey.L_2, "resource/game/2.png");
			Pictures.put(PictureKey.L_3, "resource/game/3.png");
			Pictures.put(PictureKey.L_4, "resource/game/4.png");
			Pictures.put(PictureKey.L_5, "resource/game/5.png");
			Pictures.put(PictureKey.L_6, "resource/game/6.png");
			Pictures.put(PictureKey.L_7, "resource/game/7.png");
			Pictures.put(PictureKey.L_8, "resource/game/8.png");
			Pictures.put(PictureKey.press_clear, "resource/icon/press_clear.png");
			Pictures.put(PictureKey.press_dead, "resource/icon/press_dead.png");
			Pictures.put(PictureKey.press_normal, "resource/icon/press_normal.png");
			Pictures.put(PictureKey.raise_clear, "resource/icon/raise_clear.png");
			Pictures.put(PictureKey.raise_dead, "resource/icon/raise_dead.png");
			Pictures.put(PictureKey.raise_normal, "resource/icon/raise_normal.png");
			Pictures.put(PictureKey.ICON, "resource/icon/icon.png");
		}catch(IllegalArgumentException e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Initialization Exceptions - Broken Resource Pack", "Initialization Exceptions - File Not Found", JOptionPane.ERROR_MESSAGE, null);
		}
	}
	
	public Pictures get(){
		return Pictures.get(this);
	}
	
	public static PictureKey get(int index){
		assert index <= 8;
		switch(index){
			case 0:
				return PictureKey.L_0;
			case 1:
				return PictureKey.L_1;
			case 2:
				return PictureKey.L_2;
			case 3:
				return PictureKey.L_3;
			case 4:
				return PictureKey.L_4;
			case 5:
				return PictureKey.L_5;
			case 6:
				return PictureKey.L_6;
			case 7:
				return PictureKey.L_7;
			case 8:
				return PictureKey.L_8;
			default:
				return PictureKey.L_0;
		}
	}
}
