package com.common;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class Utils {
	/**
	 * Regular expression
	 */
	public static final String REGEX = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
	
	/**
	 * Launches the associated application to open the file.
	 */
	public static boolean openFile(File file) {
		try {
			Desktop.getDesktop().open(file);
			return true;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}
	
	public static final void startProcessing(Component component) {
		component.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	public static final void stopProcessing(Component component) {
		component.setCursor(Cursor.getDefaultCursor());
	}
	
	public static boolean openURI(URI uri) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(uri);
				return true;
			} catch (IOException e) { 
				e.printStackTrace();
			}
		} 
		
		return false;
	}
	
	public static final double getScreenWidth() {
		return Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
	public static final double getScreenHeight() {
		return Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
}
