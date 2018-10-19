package renderer.basicgraphicinterface.subwindow.subtab;

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
public class BaseTab extends JPanel implements ActionListener{ //basic form for help tab
	
	private ButtonGroup mode;
	private JRadioButton readEng,readVie;
	String path1, path2;
	private JTextArea instruction = new JTextArea(11,25);
	
	public BaseTab(String label, String pathpic, String pathinstruction1, String pathinstruction2) {
		path1 = pathinstruction1;
		path2 = pathinstruction2;
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		readEng = new JRadioButton("English");
		readVie = new JRadioButton("Vietnamese");
		mode = new ButtonGroup();
		readEng.setSelected(true);
		mode.add(readEng);
		mode.add(readVie);
		JLabel title = new JLabel(label);
		JLabel picture = new JLabel(new ImageIcon(pathpic));
		JScrollPane scroll= new JScrollPane(instruction);
		
		layout.putConstraint(SpringLayout.WEST, title,
                40,
                SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, title,
                5,
                SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.WEST, scroll,
                30,
                SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.NORTH, scroll,
                30,
                SpringLayout.NORTH, title);
		layout.putConstraint(SpringLayout.EAST, picture,
                540,
                SpringLayout.NORTH, scroll);
		layout.putConstraint(SpringLayout.WEST, readEng,
                20,
                SpringLayout.EAST, title);
		layout.putConstraint(SpringLayout.WEST, readVie,
                20,
                SpringLayout.EAST, readEng);
		title.setFont(new Font("Ariel", Font.BOLD, 15));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		instruction.setEditable(false);
		instruction.setLineWrap(true);
		instruction.setWrapStyleWord(true);
		readEng.addActionListener(this);
		readVie.addActionListener(this);
		try {
			instruction.setText(getInstructionText(pathinstruction1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		setBackground(Color.WHITE);
		add(title);
		add(readEng);
		add(readVie);
		add(scroll);
		add(picture);
	}
	
	private String getInstructionText(String path) throws Exception {
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
				instruction.setText(getInstructionText(path1));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == readVie) {
			try {
				instruction.setText(getInstructionText(path2));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
