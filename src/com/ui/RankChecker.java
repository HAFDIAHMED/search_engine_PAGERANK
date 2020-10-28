package com.ui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import main.java.google.pagerank.PageRank;

import org.json.JSONObject;
import org.json.JSONTokener;

import com.alexarank.AlexaRank;
import com.common.Strings;
import com.common.Utils;
import com.data.HistoryData;
import com.data.HistoryManager;
import com.geo.Geo;
import com.geo.GeoManager;
import com.main.ExcelOperator;
import com.main.Operator.EventListener;
import com.main.TextOperator;
 
/*
 * FileChooserDemo.java uses these files:
 * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/uiswing/examples/components/FileChooserDemoProject/src/components/FileChooserDemo.java
 */
public class RankChecker extends JPanel
                             implements ActionListener, EventListener {
    static private final String newline = "\n";
    JButton mOpenButton, mCheckButton, mHistoryButton;
    JTextArea mLog;
    JFileChooser mFileChooser;
    JEditorPane mEditText;
    private HistoryManager mHistoryManager;
 
    public RankChecker() {
        super(new BorderLayout());
        
        //Create the log first, because the action listeners
        //need to refer to it.
        mLog = new JTextArea(5,20);
        mLog.setMargin(new Insets(5,5,5,5));
        mLog.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(mLog);
 
        //Create a file chooser
        mFileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                ".xlsx", "xlsx");
        mFileChooser.setFileFilter(filter);
        //Uncomment one of the following lines to try a different
        //file selection mode.  The first allows just directories
        //to be selected (and, at least in the Java look and feel,
        //shown).  The second allows both files and directories
        //to be selected.  If you leave these lines commented out,
        //then the default mode (FILES_ONLY) will be used.
        //
        //fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
 
        //Create the open button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        mOpenButton = new JButton(Strings.BUTTON_OPEN_FILE);
        mOpenButton.addActionListener(this);

        mCheckButton = new JButton(Strings.BUTTON_CHECK_RANK);
        mCheckButton.addActionListener(this);
        
        mHistoryButton = new JButton(Strings.BUTTON_OPEN_HISTORY);
        mHistoryButton.addActionListener(this);
        
        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        mEditText = new JEditorPane();
        mEditText.setSize(200, 10);

        buttonPanel.add(mEditText);
        buttonPanel.add(mCheckButton);
        buttonPanel.add(mHistoryButton);
		buttonPanel.add(mOpenButton);
        
        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(logScrollPane, BorderLayout.CENTER);
        
        // initialize HistoryManager
        mHistoryManager = new HistoryManager(Strings.FILE_HISTORY_EXCEL);
    }
 
    public void actionPerformed(ActionEvent e) {
 
        //Handle open button action.
        if (e.getSource() == mOpenButton) {
			try {
				File config = new File(Strings.FILE_CONFIG);
				if (config.exists()) {	// read the file history
					FileReader reader = new FileReader(config);
					JSONTokener tokener = new JSONTokener(reader);
					JSONObject obj = new JSONObject(tokener);
		        	String path = obj.getString(Strings.JSON_KEY_PATH);
		        	if (path != null && new File(path).exists()) {
		        		mFileChooser.setCurrentDirectory(new File(path));
		        	}
				}
				else {
					try {
						config.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
            int returnVal = mFileChooser.showOpenDialog(RankChecker.this);
 
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = mFileChooser.getSelectedFile();
                //This is where a real application would open the file.
                String fileName = file.getName();
                mLog.append("Opening: " + fileName + "." + newline);
                
                // only text files or excel files
                if (fileName.endsWith("txt")) {
                	 TextOperator text = new TextOperator(file.toString());
            		 text.getPageRankAndAlexaRank();
                }
                else if (fileName.endsWith("xlsx")) {
                	final ExcelOperator excel = new ExcelOperator(file.toString()); 
                	excel.registerEventLister(this);
                	new Thread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							excel.getPageRankAndAlexaRank();
						}
                		
                	}).start();
                }
                else {
                	JOptionPane.showMessageDialog(null, Strings.MSG_WARNING_NOT_SUPPORTED);
                }
                
            } else {
                mLog.append("Open command cancelled by user." + newline);
            }
            mLog.setCaretPosition(mLog.getDocument().getLength());
        } 
        else if (e.getSource() == mCheckButton) {
        	Utils.startProcessing(this); // start processing
        	final String url = mEditText.getText();
        	if (url.equals(""))
        		return;
        	
        	String tmpURL = url.trim();
        	if (url != null && !url.equals("")) {
        		if (!url.startsWith("http")) {
        			tmpURL = "http://" + url;
        		}
        		final String finalURL = tmpURL;
        		final String logURL = url;
        		new Thread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Geo geo = GeoManager.cityCheck(url);
						int pageRank = PageRank.get(finalURL);
		        		int alexaRank = AlexaRank.getAlexaRank(finalURL);
		        		
		        		String finalInfo = logURL 
		        				+ ": PageRank = " + pageRank 
		        				+ ", Alexa rank = " + alexaRank 
		        				+ ", country = " + geo.country 
		        				+ ", city = " + geo.city + newline;
		        		mLog.append(finalInfo);
		        		
		        		// write result to file
//		        		mHistoryManager.saveToTextFile(new HistoryData(logURL, pageRank, alexaRank));
		        		mHistoryManager.saveToExcelFile(new HistoryData(logURL, pageRank, alexaRank, geo.country, geo.city));
		        		Utils.stopProcessing(RankChecker.this); // stop processing
					}
            		
            	}).start();
        	}
        }
        /*
    	 *  Open the history file if it exists
    	 */
        else if (e.getSource() == mHistoryButton) {
			if (!Desktop.isDesktopSupported()) {
				JOptionPane.showMessageDialog(null, Strings.MSG_WARNING_DESKTOP);
				return;
			}
			
			File file = new File(Strings.FILE_HISTORY_EXCEL);
			if (file.exists()) {
				Utils.openFile(file);
			}
			else {
				JOptionPane.showMessageDialog(null, Strings.MSG_WARNING_NO_FILE);
			}
        }
    }
 
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = RankChecker.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame(Strings.APP_NAME);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add content to the window.
        frame.add(new RankChecker());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
        
        
        double width = Utils.getScreenWidth();
        double height = Utils.getScreenHeight();
        int frameWidth = frame.getWidth();
        int frameHeight = frame.getHeight();
        frame.setLocation((int)(width - frameWidth) / 2, (int)(height - frameHeight) / 2);
        
        MenuManager menuManager = new MenuManager(frame);
        frame.setJMenuBar(menuManager.getMenuBar());
        
        frame.setSize(600, (int)height / 2);
    }
 
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });
    }

	@Override
	public void log(String log) {
		// TODO Auto-generated method stub
		if (mLog != null) {
			mLog.append(log + newline);
		}
	}
}
