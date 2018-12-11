/**
 * @author HomeletWei
 * @date Apr 9, 2018
 */
/*
 * Update Log:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 * *****************************************************
 */
package homelet.visual.JInput;

import homelet.GH.StringDrawer.StringDrawer.StringDrawer;
import homelet.GH.StringDrawer.StringDrawer.StringDrawerException;
import homelet.GH.utils.Alignment;
import homelet.GH.utils.ToolBox;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

public class JInputField extends JInput{
	
	private static final long serialVersionUID = 1L;
	JTextField   textField;
	StringDrawer drawer;
	
	public JInputField(String placeHolder, Dimension textfieldDI, boolean editable){
		super();
		drawer = new StringDrawer(placeHolder);
		drawer.setColor(Color.GRAY);
		drawer.setAlign(Alignment.LEFT);
		drawer.setTextAlign(Alignment.LEFT);
		drawer.setInsets(-3, 5, 0, 0);
		this.textField = new JTextField(){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void paint(Graphics g){
				super.paint(g);
				if(getContent().isEmpty()){
					try{
						drawer.updateGraphics(g);
						drawer.setFrame(getBounds());
						drawer.validate();
						drawer.draw();
					}catch(StringDrawerException e){
						e.printStackTrace();
					}
				}
			}
		};
		this.textField.setEditable(editable);
		setUpAssistant(editable);
		ToolBox.setPreferredSize(textField, textfieldDI);
		this.add(textField, BorderLayout.CENTER);
	}
	
	@Override
	public JTextComponent getTextComponent(){
		return textField;
	}
}
