package renderer.basicgraphicinterface.subwindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;


@SuppressWarnings("serial")
public class ContactUs extends JFrame{ //create contact info window
	
	private JPanel contactUs = new JPanel();
	private JTextArea contact = new JTextArea(10,35);
		
	public ContactUs() {
		add(contactUs);
		pack();
		setSize(500, 300);
		setResizable(false);
		setTitle("Contact");
		setCursorandIcon();
		init();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
	
	private void setCursorandIcon() {
		Image icon = loadImage("res/resources/images/helpicon.png");
		setIconImage(icon);
		Image cursorImage = new ImageIcon("res/resources/images/help.png").getImage();
		Point point = new Point(0, 0);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,point,"cursor"));
	}
		
	private void init() {
		JLabel title = new JLabel("Contact info:");
		JScrollPane scroll = new JScrollPane(contact);
		title.setFont(new Font("Ariel", Font.BOLD, 20));
		contact.setFont(new Font("Ariel", Font.PLAIN, 15));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contact.setEditable(false);
		try {
			contact.setText(getContactText("res/resources/text/contactlist.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		contact.setLineWrap(true);
		contact.setWrapStyleWord(true);
		contactUs.setBackground(Color.WHITE);
		contactUs.add(title);
		contactUs.add(scroll);
	}	
	
	private String getContactText(String path) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
		String text = new String();
		String line = new String();
		while ((line = in.readLine()) != null)
		{
			text += (line + "\n");
		}
		in.close();
		return text;
	}
	
	private Image loadImage(String path) {
		ImageIcon ii =  new ImageIcon(path);
		Image image = ii.getImage();
		return image;
	}

}
