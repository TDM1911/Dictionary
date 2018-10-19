package renderer.basicgraphicinterface;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.synth.SynthLookAndFeel;

import de.javasoft.plaf.synthetica.StyleFactory;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import management.DictionaryManagement;

@SuppressWarnings("serial")
public class DictionaryApplicationGraphic extends JFrame{
	// create window
	private DictPanel dict = new DictPanel(Color.LIGHT_GRAY); 
	private ArrayList<JMenuItem> theme = new ArrayList<JMenuItem>(); 
	private DictMenuBar menu = new DictMenuBar();
	private int index=0;
//===================================================================================================	
	public DictionaryApplicationGraphic() { 
		initUI();
	}
	// change dicitonary icon & cursor
	private void setDictIcon() {
		Image icon = loadImage("res/resources/images/iconl.png");
		setIconImage(icon);
		Image cursorImage = new ImageIcon("res/resources/images/appcur.png").getImage();
	    Point point = new Point(0, 0);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,point,"cursor"));
	}
	// read system theme & add extra blackeye theme
	private void activateTheme() {
		for (LookAndFeelInfo lookandfeelinfo : UIManager.getInstalledLookAndFeels()) {
	    	theme.add(new JMenuItem(lookandfeelinfo.getName()));
	    	theme.get(index).addActionListener(new ActionListener () {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					setTheme(lookandfeelinfo.getName());
				}
	    		
	    	});
	    	index++;
	    }
		theme.add(new JMenuItem("Black Eye"));
		theme.get(index).addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setTheme("Black Eye");
			}
		});
		index++;
	}
	// init window
	private void initUI() {
		activateTheme();
		for (int i=0; i<index; i++) {
			menu.addFrameDisplay(theme.get(i)); //add theme to Display option
		}
		add(dict); 
		setJMenuBar(menu);
		setDictIcon(); 
		setBackground(Color.GRAY);
		pack();
		setTitle("Half-a-heart Dictionary"); 
		setTheme("Black Eye"); 
	    SwingUtilities.updateComponentTreeUI(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); 
		setResizable(false); 
		setLocationRelativeTo(null); 
		addWindowListener(new WindowAdapter() { // check action and display window in case of changes
		    public void windowClosing(WindowEvent e) {
		    	if (DictPanel.isChange()) {
		    		switch (confirmClosing())
		    		{
		    			case JOptionPane.YES_OPTION:
		    			{
		    				DictionaryManagement management = new DictionaryManagement();
		    				try {
		    					management.exportFileDict(menu.getDictMode());
		    				} catch (IOException e1) {
		    					e1.printStackTrace();
		    				}
		    				System.exit(0);
		    				break;
		    			}
		    			case JOptionPane.NO_OPTION:
		    			{
		    				System.exit(0);
		    				break;
		    			}
		    			case JOptionPane.CANCEL_OPTION:
		    			{
		    				break;
		    			}
		    		}
		    	}
		    	else {
		    		System.exit(0);
		    	}
		    }
		});
	}
	
	private Image loadImage(String path) { // load image from path
		ImageIcon ii =  new ImageIcon(path);
		Image image = ii.getImage();
		return image;
	}

	private int confirmClosing() { // create confirm window
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
	
	private void noTheme() { // print error message if there're no such theme
		String[] option = new String [1];
		option[0] = "OK";
		JOptionPane.showOptionDialog(this,
				"No such theme on this computer", 
				"Error",
				JOptionPane.CANCEL_OPTION,
				JOptionPane.ERROR_MESSAGE,
				null,
				option, option[0]);
	}
	
	private void setTheme(String themeName) { //set theme for window
		boolean checkTheme=false;
		if (themeName.equals("Black Eye")) {
			try 
		    {
		    	UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		    	((StyleFactory)SynthLookAndFeel.getStyleFactory()).prepareMetalLAFSwitch();
		    	dict.setMainTextColor(Color.LIGHT_GRAY);
		    	checkTheme=true;
		    } 
		    catch (Exception e) 
		    {
		      e.printStackTrace();
		    }
		}
		try {
			for (LookAndFeelInfo lookandfeelinfo : UIManager.getInstalledLookAndFeels()) {
		    	if (themeName.equals(lookandfeelinfo.getName())) {
		    		dict.setMainTextColor(Color.BLACK);
		        	UIManager.setLookAndFeel(lookandfeelinfo.getClassName());
		        	checkTheme=true;
		            break;
		        }
		    }
			
			if (!checkTheme) {
				noTheme();
			}
		} catch (Exception e) {
			
		}
		SwingUtilities.updateComponentTreeUI(this);
	}
	
}