package homelet.visual;

import homelet.GH.StringDrawer.StringDrawer.StringDrawer;
import homelet.GH.StringDrawer.StringDrawer.StringDrawerException;
import homelet.GH.utils.Alignment;
import homelet.GH.utils.ToolBox;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LEDPane extends JComponent{
	
	public static Font         LED   = null;

	static{
		try{
			LED = Font.createFont(Font.TRUETYPE_FONT, new File("resource/font/LED.ttf")).deriveFont(50.F);
		}catch(FontFormatException | IOException e){
			e.printStackTrace();
		}
	}

	private final  int          default_value;
	private        String       value = "000";
	private        StringDrawer drawer;
	
	LEDPane(int default_value){
		this.default_value = default_value;
		drawer = new StringDrawer();
		drawer.setColor(Color.RED);
		drawer.setAlign(Alignment.RIGHT);
		drawer.setTextAlign(Alignment.TOP_RIGHT);
		drawer.setFont(LED);
		reset();
	}
	
	public void reset(){
		setValue(default_value);
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(long value){
		if(value > 999)
			value = 999;
		if(value < 0)
			value = 0;
		this.value = ToolBox.padString(String.valueOf(value), 3, '0', true);
		drawer.initializeContents(this.value);
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		Graphics2D g2        = (Graphics2D) g;
		Rectangle  rectangle = g2.getClipBounds();
		// fill the back ground
		g2.setColor(Color.BLACK);
		g2.fill(rectangle);
		// draw the text
		drawer.updateGraphics(g);
		drawer.setFrame(rectangle);
		try{
			drawer.validate();
			drawer.draw();
		}catch(StringDrawerException e){
			e.printStackTrace();
		}
	}
}