/**
 * <pre>
 * ****************************************************
 * Name: TODO
 * Date: TODO
 * Description: TODO
 * *****************************************************
 * </pre>
 *
 * @author HomeletWei
 * @date May 1, 2018
 */
package homelet.visual.JInput;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;

/**
 * TODO
 *
 * @author HomeletWei
 * @date May 1, 2018
 */
public abstract class JInput extends JComponent{
	
	/** @Fields <b>serialVersionUID</b> TODO */
	private static final long serialVersionUID = 1L;
	JInputAssistant assistant;
	
	/**
	 * constructor for JInput
	 * TODO
	 *
	 * @author HomeletWei
	 */
	public JInput(){
		this.setLayout(new BorderLayout());
	}
	
	protected void setUpAssistant(boolean editable){
		assistant = new JInputAssistant(getTextComponent());
		assistant.setModifyEnable(editable);
	}
	
	/**
	 * TODO
	 *
	 * @return the textComponent
	 * @author HomeletWei
	 */
	public abstract JTextComponent getTextComponent();
	
	/**
	 * indicate if the text component has changed
	 *
	 * @return true/false
	 * @author HomeletWei
	 */
	public boolean isChanged(){
		return (getContent() != null) && !getContent().isEmpty();
	}
	
	/**
	 * TODO
	 *
	 * @return
	 * @author HomeletWei
	 */
	public String getContent(){
		return getTextComponent().getText();
	}
	
	/**
	 * TODO
	 *
	 * @param content
	 * @author HomeletWei
	 */
	public void setContent(String content){
		getTextComponent().setText(content);
	}
	
	public JInputAssistant getInputAssistant(){
		return assistant;
	}
}
