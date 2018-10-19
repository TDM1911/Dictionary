package renderer.basicgraphicinterface;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;

import management.DictionaryManagement;
import renderer.basicgraphicinterface.subwindow.AboutUs;
import renderer.basicgraphicinterface.subwindow.ContactUs;
import renderer.basicgraphicinterface.subwindow.CreditPopUp;
import renderer.basicgraphicinterface.subwindow.HowToUsePopUp;

@SuppressWarnings("serial")
public class DictMenuBar extends JMenuBar implements ActionListener{ //create menubar
	private JMenu option, help, program; // main menu
	private JMenu display, dictType, suggestionMode; // submenu
	private JMenuItem howToUse, credit, about, contact, exit, save, startwith, have, off, switches; //item
	private JRadioButton  dictEV ,dictVE; // dictmode
	private ButtonGroup dictMode;
	private JLabel currentDictionary = new JLabel("Vietnamese-English"); //dict mode display
	private DictionaryManagement management = new DictionaryManagement(); // manage dictionary
	
	public DictMenuBar () { //setting all menu elements
		KeyStroke ctrlS = KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		KeyStroke ctrlD = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
		program = new JMenu("Dictionary");
		help = new JMenu(" Help");
		option = new JMenu("Option");
		howToUse = new JMenuItem("How To Use");
		howToUse.setIcon(new ImageIcon("res/resources/images/helptab.png"));
		credit = new JMenuItem("Credit");
		credit.setIcon(new ImageIcon("res/resources/images/credit.png"));
		about = new JMenuItem("About 'Half-a-heart' dictionary'");
		about.setIcon(new ImageIcon("res/resources/images/about.png"));
		contact = new JMenuItem("Contact us");
		contact.setIcon(new ImageIcon("res/resources/images/contact.png"));
		exit = new JMenuItem("Exit");
		exit.setIcon(new ImageIcon("res/resources/images/exit.png"));
		save =  new JMenuItem("Save");
		save.setIcon(new ImageIcon("res/resources/images/save.png"));
		switches = new JMenuItem("Switch dictionary");
		display = new JMenu("Display");
		dictType = new JMenu("Dictionary mode");
		suggestionMode = new JMenu("Suggestion mode");
		
		help.add(howToUse);
		help.addSeparator();
		help.add(credit);
		help.add(about);
		help.add(contact);
		option.add(display);
		option.add(dictType);
		option.add(suggestionMode);
		program.add(save);
		program.addSeparator();
		program.add(exit);
		dictEV = new JRadioButton("English-Vietnamese" , false);
		dictVE = new JRadioButton("Vietnamese-English" , true);
		dictType.add(switches);
		dictType.addSeparator();
		dictType.add(dictEV);
		dictType.add(dictVE);
		dictMode = new ButtonGroup();
		dictMode.add(dictEV);
		dictMode.add(dictVE);
		startwith= new JMenuItem("Start with ...");
		have = new JMenuItem("Have ...");
		off = new JMenuItem("No suggestion");
		suggestionMode.add(startwith);
		suggestionMode.add(have);
		suggestionMode.add(off);
		
		startwith.addActionListener(this);
		have.addActionListener(this);
		off.addActionListener(this);
		howToUse.addActionListener(this);
		switches.addActionListener(this);
		credit.addActionListener(this);
		about.addActionListener(this);
		contact.addActionListener(this);
		save.addActionListener(this);
		exit.addActionListener(this);
		dictEV.addActionListener(this);
		dictVE.addActionListener(this);
		
		save.setAccelerator(ctrlS);
		switches.setAccelerator(ctrlD);
		program.setMnemonic('D');
		option.setMnemonic('O');
		help.setMnemonic('H');
		
		add(program);
		add(option);
		add(help);
		add(Box.createGlue());
		currentDictionary.setFont(new Font("Ariel", Font.BOLD, 14));
		add(currentDictionary);
	}
	
	@SuppressWarnings("unused")
	public void actionPerformed(ActionEvent e) { //get action and process
		if (e.getSource() == startwith) {
			DictionaryManagement.setSearchMode(1);
		}
		if (e.getSource() == have) {
			DictionaryManagement.setSearchMode(2);
		}
		if (e.getSource() == off) {
			DictionaryManagement.setSearchMode(0);
		}
		if (e.getSource() == credit) {
			CreditPopUp popupwindow = new CreditPopUp();
		}
		if (e.getSource() == howToUse) {
			HowToUsePopUp popupwindow = new HowToUsePopUp();
		}
		if (e.getSource() == about) {
			AboutUs popupwindow = new AboutUs();
		}
		if (e.getSource() == contact) {
			ContactUs popupwindow = new ContactUs();
		}
		if (e.getSource() == exit) {
			if (DictPanel.isChange()) {
				int choice= confirmClosing();
				if (choice == JOptionPane.YES_OPTION) {
					try {
						management.exportFileDict(true);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					DictPanel.setChangeStatus(false);
				} 
				else if (choice == JOptionPane.NO_OPTION) {
				System.exit(0);
				}
			}
		}
		if (e.getSource() == switches) {
			if (currentDictionary.getText().equals("English-Vietnamese")) {
				dictVE.setSelected(true);
				setDictMode();
				currentDictionary.setText("Vietnamese-English");
			}
			else {
				dictEV.setSelected(true);
				setDictMode();
				currentDictionary.setText("English-Vietnamese");
			}
			
		}
		if (e.getSource() == dictEV) {
			setDictMode();
			currentDictionary.setText("English-Vietnamese");
		}
		if (e.getSource() == dictVE) {
			setDictMode();
			currentDictionary.setText("Vietnamese-English");
		}
		if (e.getSource() == save) {
			if (DictPanel.isChange()) {
				if (confirmSaving() == JOptionPane.YES_OPTION) {
					try {
						management.exportFileDict(!dictEV.isSelected());
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
				DictPanel.setChangeStatus(false);
			}
		}
	}
	
	private int confirmSaving() { //confirm saving when there's changes
		String[] options = new String[2];
		options[0] = "Yes";
		options[1] = "No";
		int choice = JOptionPane.showOptionDialog(this, 
				"Do you want to save the changes to the current dictionary?", 
				"Confirm saving dictionary", 
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, options[0]);
		return choice;
	}
	
	private int confirmClosing() { // confirm closing when there's changes
		String[] options = new String[3];
		options[0] = "Yes";
		options[1] = "No";
		options[2] = "Cancel";
		int choice = JOptionPane.showOptionDialog(this, 
				"You have edited your dictionary recently \n Do you want to save it?", 
				"Confirm Exit", 
				JOptionPane.YES_NO_CANCEL_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null, 
				options, options[0]);
		return choice;
	}
	
	private void setDictMode() { //set dicitonary mode (EV/VE)
		if (DictPanel.isChange()) {
			if (confirmSaving() == JOptionPane.YES_OPTION) {
				try {
					management.exportFileDict(dictEV.isSelected());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			DictPanel.setChangeStatus(false);
		}
		management.switchDict(dictVE.isSelected());
	}
	
	public boolean getDictMode() { //get current dict mode
		return dictVE.isSelected();
	}
	
	public void addFrameDisplay(JMenuItem theme) { //add display theme
		display.add(theme);
	}
}
