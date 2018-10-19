package renderer.basicgraphicinterface.subwindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class AboutUs extends JFrame implements ActionListener{ //create about window
	
	private JPanel aboutUs = new JPanel();
	private ButtonGroup mode;
	private JRadioButton readEng,readVie;
	private JTextArea about = new JTextArea(10,35);
		
	public AboutUs() {
		add(aboutUs);
		pack();
		setSize(500, 300);
		setResizable(false);
		setTitle("About");
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
		JLabel title = new JLabel("About:");
		JScrollPane scroll = new JScrollPane(about);
		readEng = new JRadioButton("English");
		readVie = new JRadioButton("Vietnamese");
		mode = new ButtonGroup();
		readEng.setSelected(true);
		mode.add(readEng);
		mode.add(readVie);
		title.setFont(new Font("Ariel", Font.BOLD, 20));
		about.setFont(new Font("Ariel", Font.PLAIN, 15));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		about.setEditable(false);
		try {
			about.setText(getIntroText("res/resources/text/abouteng.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		about.setLineWrap(true);
		about.setWrapStyleWord(true);
		aboutUs.setBackground(Color.WHITE);
		aboutUs.add(title);
		readEng.addActionListener(this);
		readVie.addActionListener(this);
		aboutUs.add(readEng);
		aboutUs.add(readVie);
		aboutUs.add(scroll);
	}	
	
	private Image loadImage(String path) {
		ImageIcon ii =  new ImageIcon(path);
		Image image = ii.getImage();
		return image;
	}
	
	private String getIntroText(String path) throws Exception {
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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == readEng) {
			try {
				about.setText(getIntroText("res/resources/text/abouteng.txt"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == readVie) {
			try {
				about.setText(getIntroText("res/resources/text/aboutvie.txt"));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
