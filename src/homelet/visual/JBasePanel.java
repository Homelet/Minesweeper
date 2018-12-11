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
 * @date May 13, 2018
 */
package homelet.visual;

import homelet.GH.utils.Border;
import homelet.GH.utils.ColorBank;

import javax.swing.*;
import java.awt.*;

public class JBasePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	boolean   showGrid = false;
	ColorBank colorBank;
	
	/**
	 * constructor for JPanel
	 * TODO
	 *
	 * @author HomeletWei
	 */
	public JBasePanel(){
		this.colorBank = new ColorBank();
	}
	
	/**
	 * @author HomeletWei
	 * @see #dismissGrid()
	 */
	public void showGrid(){
		this.showGrid = true;
	}
	
	/**
	 * @author HomeletWei
	 * @see #showGrid()
	 */
	public void dismissGrid(){
		this.showGrid = false;
	}
	
	@Override
	public void paint(Graphics g){
		super.paint(g);
		if(showGrid)
			paintInner((Graphics2D) g, this, new Point(0, 0), 0);
	}
	
	private void paint(Graphics2D g, Point vertex, Component c, int layerIndex){
		Point     innerVertex = sumPoint(vertex, c.getLocation());
		Dimension size        = c.getSize();
		Border.drawBorder(g, Border.RECTANGULAR, new Rectangle(innerVertex, size), colorBank.pollColor(layerIndex), 1, 0);
	}
	
	private void paintInner(Graphics2D g, Container c, Point vertex, int layerIndex){
		paint(g, vertex, c, layerIndex);
		layerIndex++;
		synchronized(c.getTreeLock()){
			for(Component comp : c.getComponents()){
				if(!comp.isVisible())
					continue;
				if(comp instanceof Container)
					paintInner(g, ((Container) comp), sumPoint(vertex, c.getLocation()), layerIndex);
				else
					paint(g, vertex, c, layerIndex);
			}
		}
	}
	
	private Point sumPoint(Point vertex, Point thePoint){
		return new Point(vertex.x + thePoint.x, vertex.y + thePoint.y);
	}
}
