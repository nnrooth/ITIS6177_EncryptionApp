package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class LinkFrame {

	private JFrame frmLink;
	private JTextField txtLink;
	
	public LinkFrame() {
		frmLink = new JFrame("Dropbox Link");
		frmLink.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		txtLink = new JTextField("");
		frmLink.getContentPane().add(txtLink, BorderLayout.CENTER);
	}
	
	public void setLink(String link) {
		txtLink.setEditable(false);
		txtLink.setText(link);
		
		Dimension dimXY = txtLink.getPreferredSize();
		dimXY.setSize(dimXY.getWidth() + 15.00, dimXY.getHeight() + 15.00);
		
		txtLink.setSize(20, 10);
	}
	
	public void display() {
		frmLink.pack();
		frmLink.setLocationRelativeTo(null);
		frmLink.setVisible(true);
		frmLink.setAlwaysOnTop(true);
	}
	
	public void hide() {
		frmLink.setVisible(false);
	}
}
