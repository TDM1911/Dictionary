
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class BaseTab extends JPanel implements ActionListener{   
	
	private ButtonGroup bg;
	private JRadioButton ta,tv;
	String s1,s2;
	private JTextArea huongdan = new JTextArea(11,35);
	
	public BaseTab() {
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		ta = new JRadioButton("English");
		tv = new JRadioButton("Tieng Viet");
		bg = new ButtonGroup();
		ta.setSelected(true);
		bg.add(ta);
		bg.add(tv);
		JLabel tieude = new JLabel("tieu de");
		JLabel picture = new JLabel(new ImageIcon("C:\\picture.png"));
		JScrollPane scroll= new JScrollPane(huongdan);

		layout.putConstraint(SpringLayout.WEST, tieude,
                40,
                SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, tieude,
                5,
                SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, scroll,
                30,
                SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, scroll,
                30,
                SpringLayout.NORTH, tieude);
		layout.putConstraint(SpringLayout.EAST, picture,
                540,
                SpringLayout.NORTH, scroll);
		layout.putConstraint(SpringLayout.WEST, ta,
                20,
                SpringLayout.EAST, tieude);
		layout.putConstraint(SpringLayout.WEST, tv,
                20,
                SpringLayout.EAST, ta);
		tieude.setFont(new Font("Ariel", Font.BOLD, 15));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); 
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);  
		huongdan.setEditable(false);  
		huongdan.setLineWrap(true);
		huongdan.setWrapStyleWord(true);
		ta.addActionListener(this);
		tv.addActionListener(this);
		try {
			huongdan.setText(gethuongdanText(s1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setBackground(Color.WHITE);
		add(tieude);
		add(ta);
		add(tv);
		add(scroll);
		add(picture);
	}
	
	private String gethuongdanText(String path) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
		String vanban = new String();
		String dong = new String();
		while ((dong = in.readLine()) != null)
		{
			vanban += (dong + "\n");
		}
		in.close();
		return vanban;
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ta) {
			try {
				huongdan.setText(gethuongdanText(s1));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == tv) {
			try {
				huongdan.setText(gethuongdanText(s2));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
