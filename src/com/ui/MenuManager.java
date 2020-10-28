package com.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import com.common.Strings;

public class MenuManager implements ActionListener {
	private JMenuBar mJMenuBar;
	private JFrame mFrame;
	
	public MenuManager(JFrame frame) {
		mFrame = frame;
		init();
	}
	
	private void init() {
		mJMenuBar = new JMenuBar();
		
		JMenu menu = new JMenu(Strings.MENU_HELP);
		menu.add(createMenuItem(Strings.MENU_ABOUT));
	    mJMenuBar.add(menu);
	}
	
	private JMenuItem createMenuItem(String strMenu)
	{
	    JMenuItem menuItem = new JMenuItem(strMenu);
	    menuItem.addActionListener(this);
	    
	    return menuItem;
	}
	
	public JMenuBar getMenuBar() {
		return mJMenuBar;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		new AboutDialog(mFrame);
	}
}
