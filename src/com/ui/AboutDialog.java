package com.ui;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;

import com.common.Strings;
import com.common.Utils;

public class AboutDialog extends javax.swing.JDialog implements ActionListener {
	private JLabel mLabelAppName, mLabelVersion, mLabelCopyRight, mLabelURI;
	private JButton mButtonOK;

	public AboutDialog(JFrame frame) {
		super(frame, true);
		initGUI();

		double width = Utils.getScreenWidth();
		double height = Utils.getScreenHeight();
		int aboutDlgWidth = getWidth();
		int aboutDlgHeight = getHeight();
		setLocation((int) (width - aboutDlgWidth) / 2,
				(int) (height - aboutDlgHeight) / 2);
		setResizable(false);
		setVisible(true);
	}

	private void initGUI() {

		setTitle(Strings.MENU_ABOUT);
		getContentPane().setLayout(null);

		mLabelAppName = new JLabel();
		getContentPane().add(mLabelAppName);
		mLabelAppName.setText(Strings.APP_NAME);
		mLabelAppName.setBounds(50, 19, 200, 17);

		mLabelVersion = new JLabel();
		getContentPane().add(mLabelVersion);
		mLabelVersion.setText(Strings.ABOUT_VERSION);
		mLabelVersion.setBounds(50, 39, 200, 17);

		mLabelCopyRight = new JLabel();
		getContentPane().add(mLabelCopyRight);
		mLabelCopyRight.setText(Strings.ABOUT_AUTHOR);
		mLabelCopyRight.setBounds(50, 59, 300, 17);

		mLabelURI = new JLabel();
		getContentPane().add(mLabelURI);
		mLabelURI.setText("<html><a href=\"\">" + Strings.ABOUT_SITE
				+ "</a></html>");
		mLabelURI.setBounds(50, 79, 142, 17);
		mLabelURI.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				mLabelURI.setCursor(new Cursor(Cursor.HAND_CURSOR));
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				try {
					Utils.openURI(new URI(Strings.ABOUT_SITE));
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		mButtonOK = new JButton();
		getContentPane().add(mButtonOK);
		mButtonOK.setText("Ok");
		mButtonOK.setActionCommand(Strings.OK);
		mButtonOK.addActionListener(this);
		mButtonOK.setBounds(255, 70, 60, 30);

		setSize(380, 138);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(Strings.OK)) {
			dispose();
		}
	}

}
