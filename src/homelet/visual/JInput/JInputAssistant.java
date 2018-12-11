package homelet.visual.JInput;
/*
 * Update Log:
 * ****************************************************
 * Name:
 * Date:
 * Description:
 * *****************************************************
 */

import homelet.GH.utils.ToolBox;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * TODO
 *
 * @author HomeletWei
 * @date Apr 25, 2018
 */
public class JInputAssistant{
	
	private static final int combinationStarter = KeyEvent.META_DOWN_MASK;
	JPopupMenu     leftClickPopUp;
	JMenuItem      cut;
	JMenuItem      copy;
	JMenuItem      paste;
	JMenuItem      clearAll;
	JMenuItem      selectAll;
	JMenuItem      cutAll;
	JMenuItem      copyAll;
	JMenuItem      replaceAll;
	JMenuItem      undo;
	JMenuItem      redo;
	JMenuItem      properties;
	JTextComponent parent;
	UndoManager    undoManager;
	
	/**
	 * constructor for JInputPopupMenu
	 * <ul>
	 * <li>undo -> CKS + VK_Z</li>
	 * <li>redo -> CKS + VK_R</li>
	 * <li>cut -> CKS + VK_X</li>
	 * <li>copy -> CKS + VK_C</li>
	 * <li>paste -> CKS + VK_V</li>
	 * <li>cut all -> CKS + shift_mask + VK_X</li>
	 * <li>copy all -> CKS + shift_mask + VK_C</li>
	 * <li>replace all -> CKS + shift_mask + VK_V</li>
	 * <li>clear all -> CKS + shift_mask + VK_DELETE</li>
	 * <li>select all -> CKS + VK_A</li>
	 * </ul>
	 *
	 * @param parent
	 * @author HomeletWei
	 */
	public JInputAssistant(JTextComponent parent){
		this.undoManager = new UndoManager();
		this.parent = parent;
		createLeftClickDropDown();
		addAction();
		setUndoOrRedoEnable();
	}
	
	private void createLeftClickDropDown(){
		// <!-- LeftClickDropDown -->
		this.leftClickPopUp = new JPopupMenu("Operation"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			/**
			 * @param invoker
			 * @param x
			 * @param y
			 * @see javax.swing.JPopupMenu#show(java.awt.Component, int, int)
			 */
			@Override
			public void show(Component invoker, int x, int y){
				int letterCount = JInputUtility.getLetterCount(parent);
				int wordCount   = JInputUtility.getWordCount(parent);
				properties.setText(letterCount + "\tletter" + (letterCount > 0 ? "s" : "") + ", " + wordCount + "\tword" + (wordCount > 0 ? "s" : ""));
				super.show(invoker, x, y);
			}
		};
		undo = new JMenuItem("Undo");
		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, combinationStarter));
		redo = new JMenuItem("Redo");
		redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, combinationStarter));
		cut = new JMenuItem("Cut");
		cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, combinationStarter));
		copy = new JMenuItem("Copy");
		copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, combinationStarter));
		paste = new JMenuItem("Paste");
		paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, combinationStarter));
		cutAll = new JMenuItem("Cut All");
		cutAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, combinationStarter | KeyEvent.SHIFT_DOWN_MASK));
		copyAll = new JMenuItem("Copy All");
		copyAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, combinationStarter | KeyEvent.SHIFT_DOWN_MASK));
		replaceAll = new JMenuItem("Replace All");
		replaceAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, combinationStarter | KeyEvent.SHIFT_DOWN_MASK));
		clearAll = new JMenuItem("Clear All");
		clearAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, combinationStarter | KeyEvent.SHIFT_DOWN_MASK));
		selectAll = new JMenuItem("Select All");
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, combinationStarter));
		selectAll = new JMenuItem("Select All");
		selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, combinationStarter));
		properties = new JMenuItem("Properties");
		properties.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, combinationStarter));
		// <!-- create display -->
		leftClickPopUp.add(properties);
		leftClickPopUp.addSeparator();
		leftClickPopUp.add(undo);
		leftClickPopUp.add(redo);
		leftClickPopUp.addSeparator();
		leftClickPopUp.add(cut);
		leftClickPopUp.add(copy);
		leftClickPopUp.add(paste);
		leftClickPopUp.addSeparator();
		leftClickPopUp.add(cutAll);
		leftClickPopUp.add(copyAll);
		leftClickPopUp.add(replaceAll);
		leftClickPopUp.addSeparator();
		leftClickPopUp.add(selectAll);
		leftClickPopUp.add(clearAll);
		addLeftClickDropDownAction();
	}
	
	private void addAction(){
		addMouseListener();
		addUndoableEditListener();
		addKeyMap();
	}
	
	private void setUndoOrRedoEnable(){
		undo.setEnabled(undoManager.canUndo());
		redo.setEnabled(undoManager.canRedo());
	}
	
	private void addLeftClickDropDownAction(){
		cut.addActionListener((e)->{
			JInputUtility.cut(parent);
			closeLeftClickDropDown();
		});
		copy.addActionListener((e)->{
			JInputUtility.copy(parent);
			closeLeftClickDropDown();
		});
		paste.addActionListener((e)->{
			JInputUtility.pasta(parent);
			closeLeftClickDropDown();
		});
		cutAll.addActionListener((e)->{
			JInputUtility.cutAll(parent);
			closeLeftClickDropDown();
		});
		copyAll.addActionListener((e)->{
			JInputUtility.copyAll(parent);
			closeLeftClickDropDown();
		});
		replaceAll.addActionListener((e)->{
			JInputUtility.replaceAll(parent);
			closeLeftClickDropDown();
		});
		clearAll.addActionListener((e)->{
			JInputUtility.clearAll(parent);
			closeLeftClickDropDown();
		});
		selectAll.addActionListener((e)->{
			JInputUtility.selectAll(parent);
			closeLeftClickDropDown();
		});
		undo.addActionListener((e)->{
			if(undoManager.canUndo())
				undoManager.undo();
			closeLeftClickDropDown();
		});
		redo.addActionListener((e)->{
			if(undoManager.canRedo())
				undoManager.redo();
			closeLeftClickDropDown();
		});
		properties.addActionListener((e)->{
			int letterCount = JInputUtility.getLetterCount(parent);
			int wordCount   = JInputUtility.getWordCount(parent);
			int charCount   = JInputUtility.getCharaterCount(parent);
			JOptionPane.showMessageDialog(parent, "Letter Count : " + letterCount + "\tletter" + (letterCount > 0 ? "s" : "") + "\n" + "Word Count : " + wordCount + "\tword" + (wordCount > 0 ? "s" : "") + "\n" + "Character Count : " + charCount + "\tCharacter" + (charCount > 0 ? "s" : ""), "Properties", JOptionPane.INFORMATION_MESSAGE, null);
		});
	}
	
	private void addMouseListener(){
		parent.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 2){
					JInputUtility.selectAll(parent);
				}
				super.mouseClicked(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e){
				mayBeShowPopUp(e);
				super.mousePressed(e);
			}
			
			@Override
			public void mouseReleased(MouseEvent e){
				mayBeShowPopUp(e);
				super.mouseReleased(e);
			}
			
			void mayBeShowPopUp(MouseEvent e){
				if(e.isPopupTrigger()){
					leftClickPopUp.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
	private void addUndoableEditListener(){
		parent.getDocument().addUndoableEditListener((UndoableEditEvent e)->{
			undoManager.addEdit(e.getEdit());
			setUndoOrRedoEnable();
		});
	}
	
	private void addKeyMap(){
		parent.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_P, combinationStarter), "leftClickDropDown");
		parent.getActionMap().put("leftClickDropDown", new AbstractAction("leftClickDropDownAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				Point p = ToolBox.getMouseLocation(parent);
				leftClickPopUp.show(parent, p.x, p.y);
			}
		});
		parent.getInputMap().put(undo.getAccelerator(), "undo");
		parent.getActionMap().put("undo", new AbstractAction("undoAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				if(undoManager.canUndo())
					undoManager.undo();
				setUndoOrRedoEnable();
			}
		});
		parent.getInputMap().put(redo.getAccelerator(), "redo");
		parent.getActionMap().put("redo", new AbstractAction("redoAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				if(undoManager.canRedo())
					undoManager.redo();
				setUndoOrRedoEnable();
			}
		});
		//cut, copy, paste, seleteAll has overLayed with System Command
		parent.getInputMap().put(cutAll.getAccelerator(), "cutAll");
		parent.getActionMap().put("cutAll", new AbstractAction("cutAllAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				JInputUtility.cutAll(parent);
			}
		});
		parent.getInputMap().put(copyAll.getAccelerator(), "copyAll");
		parent.getActionMap().put("copyAll", new AbstractAction("copyAllAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				JInputUtility.copyAll(parent);
			}
		});
		parent.getInputMap().put(replaceAll.getAccelerator(), "replaceAll");
		parent.getActionMap().put("replaceAll", new AbstractAction("replaceAllAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				JInputUtility.replaceAll(parent);
			}
		});
		parent.getInputMap().put(clearAll.getAccelerator(), "clearAll");
		parent.getActionMap().put("clearAll", new AbstractAction("clearAllAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				JInputUtility.clearAll(parent);
			}
		});
		parent.getInputMap().put(properties.getAccelerator(), "properties");
		parent.getActionMap().put("properties", new AbstractAction("propertiesAction"){
			/** @Fields <b>serialVersionUID</b> TODO */
			private static final long serialVersionUID = 1L;
			
			@Override
			public void actionPerformed(ActionEvent e){
				int letterCount = JInputUtility.getLetterCount(parent);
				int wordCount   = JInputUtility.getWordCount(parent);
				int charCount   = JInputUtility.getCharaterCount(parent);
				JOptionPane.showMessageDialog(parent, "Letter Count : " + letterCount + "\tletter" + (letterCount > 0 ? "s" : "") + "\n" + "Word Count : " + wordCount + "\tword" + (wordCount > 0 ? "s" : "") + "\n" + "Character Count : " + charCount + "\tCharacter" + (charCount > 0 ? "s" : ""), "Properties", JOptionPane.INFORMATION_MESSAGE, null);
			}
		});
	}
	
	public void closeLeftClickDropDown(){
		leftClickPopUp.setVisible(false);
	}
	
	/**
	 * return the textComponent
	 *
	 * @return the textComponent
	 * @author HomeletWei
	 */
	public JTextComponent getTextComponent(){
		return parent;
	}
	
	public void setModifyEnable(boolean editEnable){
		cut.setEnabled(editEnable);
		paste.setEnabled(editEnable);
		clearAll.setEnabled(editEnable);
		replaceAll.setEnabled(editEnable);
		cutAll.setEnabled(editEnable);
		setUndoEnable(editEnable);
	}
	
	public void setUndoEnable(boolean undoEnable){
		undo.setEnabled(undoEnable);
		redo.setEnabled(undoEnable);
	}
	
	public void setCopyEnable(boolean copyEnable){
		copy.setEnabled(copyEnable);
		selectAll.setEnabled(copyEnable);
		copyAll.setEnabled(copyEnable);
	}
}
