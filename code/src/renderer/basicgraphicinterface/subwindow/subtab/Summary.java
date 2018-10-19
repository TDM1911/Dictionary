package renderer.basicgraphicinterface.subwindow.subtab;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;

@SuppressWarnings("serial")
public class Summary extends JPanel implements ActionListener{ //sumary tab
	
	private ButtonGroup mode;
	private JRadioButton readEng,readVie;
	private JTextArea instruction = new JTextArea(10,45);
	
	public Summary() {
		
		SpringLayout layout = new SpringLayout();
		this.setLayout(layout);
		JLabel title = new JLabel("Overview:");
		JScrollPane scroll = new JScrollPane(instruction);
		
		readEng = new JRadioButton("English");
		readVie = new JRadioButton("Vietnamese");
		mode = new ButtonGroup();
		readEng.setSelected(true);
		mode.add(readEng);
		mode.add(readVie);
		
		title.setFont(new Font("Ariel", Font.BOLD, 15));
		instruction.setFont(new Font("Ariel", Font.PLAIN, 12));
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
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
		layout.putConstraint(SpringLayout.WEST, readEng,
                20,
                SpringLayout.EAST, title);
		layout.putConstraint(SpringLayout.WEST, readVie,
                20,
                SpringLayout.EAST, readEng);
		instruction.setEditable(false);
		try {
			instruction.setText(getInstructionText("res/resources/text/welcomeeng.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		instruction.setLineWrap(true);
		instruction.setWrapStyleWord(true);
		readEng.addActionListener(this);
		readVie.addActionListener(this);
		setBackground(Color.WHITE);
		add(title);
		add(readEng);
		add(readVie);
		add(scroll);
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
				instruction.setText(getInstructionText("res/resources/text/welcomeeng.txt"));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		if (e.getSource() == readVie) {
			try {
				instruction.setText(getInstructionText("res/resources/text/welcomevie.txt"));
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}
}
