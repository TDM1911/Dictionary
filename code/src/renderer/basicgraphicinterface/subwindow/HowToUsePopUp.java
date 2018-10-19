package renderer.basicgraphicinterface.subwindow;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import renderer.basicgraphicinterface.subwindow.subtab.BaseTab;
import renderer.basicgraphicinterface.subwindow.subtab.Summary;

@SuppressWarnings("serial")
public class HowToUsePopUp extends JFrame{ //create how to use window
	
	Summary overview = new Summary(); //summary tab for help window
	BaseTab searchhelp = new BaseTab("How to search:", // search help tab
			"res/resources/images/howtosearch.png",
			"res/resources/text/searcheng.txt",
			"res/resources/text/searchvie.txt");
	BaseTab addnedit = new BaseTab("Add & Edit", //add n edit tab
			"res/resources/images/e.gif",
			"res/resources/text/addnediteng.txt",
			"res/resources/text/addneditvie.txt");
	BaseTab soundplaying = new BaseTab("Read a word", // speak a word help tab
			"res/resources/images/speak.png",
			"res/resources/text/playeng.txt",
			"res/resources/text/playvie.txt");
	BaseTab remove = new BaseTab("Remove a word", //how to remove tab
			"res/resources/images/button.png",
			"res/resources/text/removeeng.txt",
			"res/resources/text/removevie.txt");
	BaseTab barguide = new BaseTab("MenuBar overview", // guide about menubar tab
			"res/resources/images/menu.png",
			"res/resources/text/bareng.txt",
			"res/resources/text/barvie.txt");
	BaseTab bm = new BaseTab("Bookmark & History", // help about bookmark & history
			"res/resources/images/bm.gif",
			"res/resources/text/bmeng.txt",
			"res/resources/text/bmvie.txt");
	JTabbedPane help = new JTabbedPane();
	
	public HowToUsePopUp() { //create how to use window
		add(help);
		ImageIcon ii =  new ImageIcon("res/resources/images/helptab.png");
		help.addTab("Start", ii, overview, "Welcome!");
		help.addTab("Search", ii, searchhelp, "how to search a word");
		help.addTab("Add & Edit", ii, addnedit, "adding or editing a word");
		help.addTab("Remove", ii, remove, "remove a word from the dictionary");
		help.addTab("Pronoun", ii, soundplaying, "pronoun a word");
		help.addTab("Menu", ii, barguide, "a guide about menu bar");
		help.addTab("Bookmark & History", ii, bm, "a guide about using bookmark and history");
		pack();
		setSize(600, 300);
		setResizable(false);
		setTitle("How to use");
		setCursorandIcon();
		setVisible(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private void setCursorandIcon() {
		Image icon = loadImage("res/resources/images/helpicon.png");
		setIconImage(icon);
		Image cursorImage = new ImageIcon("res/resources/images/help.png").getImage();
	    Point point = new Point(0, 0);
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,point,"cursor"));
	}

	private Image loadImage(String path) {
		ImageIcon ii =  new ImageIcon(path);
		Image image = ii.getImage();
		return image;
	}
}
