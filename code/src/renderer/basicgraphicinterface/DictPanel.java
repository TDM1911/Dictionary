package renderer.basicgraphicinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import com.sun.speech.freetts.VoiceManager;

import database.Dictionary;
import management.DictionaryManagement;

@SuppressWarnings("serial")
public class DictPanel extends JPanel implements ActionListener{
	
	private JTextField searchbox, showWord,clock;
	private JTextPane meaning;
	private JLabel label = new JLabel();
	private JComboBox <String> history, bookmark;
	private JButton editbutton, soundbutton, deletebutton, mark;
	private Font fontb,fonti,titlefont;
	private static boolean unchanged=true;
	private DictionaryManagement management = new DictionaryManagement();
	private String prev="";
	private String rawInput;
	private JTextArea suggestionBox,status;
	private int selected=0;
	AudioInputStream audioIn;
	Color mainTextColor;
	
	public DictPanel(Color color) { //set base
		
		mainTextColor= color;
		try {
			management.insertFromFile(); //get dictionary
			management.importFileIndex(); //get sound index
			management.switchDict(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setLayout(new BorderLayout());
		addFont(); //import font
		initPanel(); //create elements
		setCursor(); //setCursor
		
	}
//================================================================================
	private void initPanel() { //create elements
		setPreferredSize(new Dimension(800, 550));
		setMaximumSize(new Dimension(800, 550));
		initEditButton();
		initDeleteButton();
		initVoiceButton();
		initMarkButton();
		initDropBox();
		initSearchingBox();
		initMarkedBox();
		initHistoryBox();
		initShowingBox();
		initTitle();
		initStatusBox();
		initMeaningBox();
	}

//================================================================================
	private void initDropBox() { //create suggestion drop box
		
		Border border = BorderFactory.createEtchedBorder();
		
		suggestionBox= new JTextArea(10,10);
		suggestionBox.setEditable(false);
		suggestionBox.setEnabled(false);
		suggestionBox.setLineWrap(true);
		suggestionBox.setWrapStyleWord(true);
		suggestionBox.setBounds(30,170,200,105);
		suggestionBox.setBorder(border);
		suggestionBox.setFont(fonti);
		suggestionBox.setVisible(false);
		add(suggestionBox);
	}
	
	private void initHistoryBox() { //create history box
		
		Border border = BorderFactory.createTitledBorder("History");
		
		history = new JComboBox <> ();
		history.setModel(new DefaultComboBoxModel<String>());
		history.addItem("");
		history.addActionListener(this);
		history.setBorder(border);
		history.setBounds(30,180,200,60);
		history.setEditable(false);		
		add(history);
	}
	
	private void initMarkedBox() { // create bookmark box
		
		Border border = BorderFactory.createTitledBorder("Bookmark");
		
		bookmark = new JComboBox <> ();
		bookmark.setModel(new DefaultComboBoxModel<String>());
		try {
			inputBookmark("res/resources/bm.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
		bookmark.addActionListener(this);
		bookmark.setBorder(border);
		bookmark.setBounds(30,260,200,60);
		bookmark.setEditable(false);
		add(bookmark);
	}
	
	private void initMeaningBox() { //create meaning box
		
		Border border = BorderFactory.createTitledBorder("");
		meaning = new JTextPane();
		JScrollPane scroll = new JScrollPane(meaning);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		label.add(scroll);
		scroll.setSize(500, 370);
		scroll.setLocation(new Point(270,180));
		scroll.setViewportView(meaning);		
		meaning.setFont(fontb);
		meaning.setBorder(border);
		meaning.setEditable(false);
		meaning.setToolTipText("word/phrase meaning");
		label.setVisible(true);
		add(label);
	}
	
	private void initStatusBox() { //create status box & clock
		
		Border border = BorderFactory.createTitledBorder("Status");
		status = new JTextArea();
		clock = new JTextField();
		status.setBounds(30, 350, 200, 150);
		clock.setBounds(30, 510, 200, 40);
		status.setFont(fontb);
		clock.setFont(fonti);
		status.setLineWrap(true);
		status.setWrapStyleWord(true);
		status.setBorder(border);
		status.setText("Ready");
		status.setEditable(false);
		clock.setEnabled(false);
		Timer timer = new Timer(500, new ActionListener() { //set timer
            @Override
            public void actionPerformed(ActionEvent e) {
            	clock.setText(DateFormat.getDateTimeInstance().format(new Date()));
            }
        });
        timer.setRepeats(true);
        timer.setCoalesce(true);
        timer.setInitialDelay(0);
        timer.start();
        add(clock);
		add(status);
	}
	
	private void initShowingBox() { //create show word box (show current word)
		
		Border border = BorderFactory.createTitledBorder("Word / Phrase");
		
		showWord = new JTextField(30);
		
		showWord.setFont(new Font("Ariel", Font.BOLD,12));
		showWord.setBounds(270,100,300,60);
		showWord.setBorder(border);
		showWord.setEditable(false);
		showWord.getDocument().addDocumentListener( new DocumentListener() { //check if hasWord in search
			public void changedUpdate(DocumentEvent e) {}
			public void insertUpdate(DocumentEvent e) {
				status.setText("Ready");
				activate();
			}

			public void removeUpdate(DocumentEvent e) {
				deletebutton.setEnabled(false);
				editbutton.setEnabled(false);
				mark.setEnabled(false);
				soundbutton.setEnabled(false);
				status.setText("Ready");
			}
		});
		showWord.setToolTipText("current word/phrase");
		add(showWord);
	}
	
	private void initSearchingBox() { //create searching box
		
		Border border = BorderFactory.createTitledBorder("Searching");
		
		searchbox = new JTextField(30);
		
		searchbox.addActionListener(this);	
		searchbox.getDocument().addDocumentListener( new DocumentListener() { //check if there is input
			public void changedUpdate(DocumentEvent e) {}

			public void insertUpdate(DocumentEvent e) {
				selected=0;
				suggestionBox.setText(management.dictionarySearch(searchbox.getText()));
				suggestionBox.setVisible(true && (management.getSearchMode() != 0));
			}
		
			public void removeUpdate(DocumentEvent e) {
				selected=0;
				if (!searchbox.getText().isEmpty()) {
					suggestionBox.setText(management.dictionarySearch(searchbox.getText()));
				}
				else {
					suggestionBox.setVisible(false);
				}
			}
		});
		searchbox.addKeyListener(new KeyListener() { //get key input for suggestion selection
			@Override
			public void keyPressed(KeyEvent e) {
				String[] word = suggestionBox.getText().split("\n");				
				Highlighter h = suggestionBox.getHighlighter();
				h.removeAllHighlights();
				if (e.getKeyCode() == KeyEvent.VK_DOWN 
						&& selected<word.length-1 
						&& word.length>=2) {
					if (selected<5) selected++;
					int pos1 = suggestionBox.getText().indexOf(word[selected], 0);
					int pos2 = pos1 + word[selected].length();
					if (pos1>=0) {
						try {						
							h.addHighlight(pos1+2, pos2, 
									new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA));
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				} else if (e.getKeyCode() == KeyEvent.VK_UP && selected>1 && word.length>=2) {
					if (selected>1) selected--;
					int pos1 = suggestionBox.getText().indexOf(word[selected], 0);
					int pos2 = pos1 + word[selected].length();
					if (pos1>=0) {
						try {						
							h.addHighlight(pos1+2, pos2, 
									new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA));
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}				
				else if (e.getKeyCode() == KeyEvent.VK_ENTER && selected>0) {
					searchbox.setText(word[selected].substring(2));
				}
				else if ((selected ==1 || selected==word.length-1) && word.length>=2){
					int pos1 = suggestionBox.getText().indexOf(word[selected], 0);
					int pos2 = pos1 + word[selected].length();
					if (pos1>=0) {
						try {						
							h.addHighlight(pos1+2, pos2, 
									new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA));
						} catch (BadLocationException e1) {
							e1.printStackTrace();
						}
					}
				}
				
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
			
		});
		searchbox.setBounds(30,100,200,60);
		searchbox.setBorder(border);
		searchbox.setToolTipText("enter searching word/phrase");
		add(searchbox);
	}
//================================================================================= 
	private void initEditButton() { // create edit & add button
		
		ImageIcon ii =  new ImageIcon("res/resources/images/edit.png");
		
		editbutton = new JButton(ii);
		
		editbutton.addActionListener(this);
		
		editbutton.setBounds(570,110,ii.getIconWidth(),ii.getIconHeight());
		editbutton.setToolTipText("edit meaning");
		editbutton.setEnabled(false);
		add(editbutton);
	}
	
	private void initDeleteButton() { // create delete button
		
		ImageIcon ii = new ImageIcon("res/resources/images/delete.png");
		deletebutton = new JButton(ii);
		
		deletebutton.setBounds(620, 110, ii.getIconWidth(), ii.getIconHeight());
		deletebutton.addActionListener(this);
		deletebutton.setToolTipText("delete current word/phrase");
		deletebutton.setEnabled(false);
		add(deletebutton);
	}
	
	private void initVoiceButton() { // create speak button
		 	
		ImageIcon ii = new ImageIcon("res/resources/images/audio.png");
		soundbutton = new JButton(ii);
		
		soundbutton.addActionListener(this);
		soundbutton.setBounds(670, 110, ii.getIconWidth(), ii.getIconHeight());
		soundbutton.setToolTipText("pronounce");
		soundbutton.setEnabled(false);
		soundbutton.setMultiClickThreshhold(300); //prevent spam
		add(soundbutton);
	}
	
	private void initMarkButton() { //create bookmark button
		
		ImageIcon ii = new ImageIcon("res/resources/images/marker.png");
		mark = new JButton(ii);
		
		mark.addActionListener(this);

		mark.setBounds(720, 110, ii.getIconWidth(), ii.getIconHeight());
		mark.setEnabled(false);
		mark.setToolTipText("bookmark current word/phrase");
		mark.setMultiClickThreshhold(500);
		add(mark);
	}

//===================================================================================
	private void initTitle() { // set title & logo
		JLabel title = new JLabel("Half-A-Heart Dictionary");
		JLabel nullBox = new JLabel(new ImageIcon("res/resources/images/panel.png"));
		JLabel sub = new JLabel("vietnamese-english/english-vietnamese dictionary");
		JLabel logo = new JLabel (new ImageIcon("res/resources/images/logo2.png"));
		JLabel bg = new JLabel(new ImageIcon("res/resources/images/background3.png"));
		logo.setBounds(650,0,100,100);
		bg.setBounds(0, 0, 800, 550);
		sub.setBounds(270, 45, 400, 30);
		title.setBounds(200, 7, 500, 50);
		nullBox.setBounds(25,0,750,200);
		title.setFont(titlefont);
		sub.setFont(new Font("Ariel",Font.BOLD,14));
		sub.setForeground(Color.RED);
		add(title);
		add(sub);
		add(nullBox);
		add(logo);
		add(bg);
	}
//===========================================================================================
	
	public static boolean isChange() { // check database changes
		return !unchanged;
	}
	
	public static void setChangeStatus(boolean status) { //set database changes status
		unchanged = !status;
	}	
	
	public void setMainTextColor(Color color) { //set text color suitable with current theme
		mainTextColor = color;
		showWord.setText("");
		meaning.setText("");
	}
	
	private int confirmDelete() { // create confirm delete window
		String[] options = new String[2];
		options[0] = "Delete";
		options[1] = "Abort ";
		int choice = JOptionPane.showOptionDialog(this, 
				"Do you want to delete this word/phrase?"
				+ "\nThis will permanently delete this from the dictionary.", 
				"Confirm",
				JOptionPane.YES_NO_OPTION, 
				JOptionPane.WARNING_MESSAGE, 
				null,
				options, options[1]);
		return choice;
	}
	
	private void inputBookmark(String path) throws Exception { // read bookmark
		
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
		String word = "";
		bookmark.removeAllItems();
		bookmark.addItem("");
		while ((word = in.readLine()) != null)
		{
			bookmark.addItem(word);
		}
		if (bookmark.getItemCount() >=21) mark.setEnabled(false);
		in.close();
	}
	
	private void outputBookmark(String path) throws Exception { // save bookmark to database
		
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path),"UTF8"));
		for (int i=1; i < bookmark.getItemCount(); i++)
		{
			out.write(bookmark.getItemAt(i) + "\n");
		}
		out.close();
	}
	
	private void soundRender(String path) { // render audio file
		File soundFile = new File(path);
		try {
			audioIn = AudioSystem.getAudioInputStream(soundFile);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	private void activate() { //activate buttons when there is a word in show box
		deletebutton.setEnabled(true);
		editbutton.setEnabled(true);
		boolean isExist= false;
		for (int i=0; i < history.getItemCount(); i ++) {
			if (history.getItemAt(i) == showWord.getText()) {
				isExist = true;
				break;
			}
		}
		if (isExist) {
			history.removeItem(showWord.getText());
		}
		history.insertItemAt(showWord.getText(), 1);
		if (history.getItemCount()==6) {
			history.removeItemAt(5);
		}
		mark.setIcon(new ImageIcon("res/resources/images/marker.png"));
		mark.setToolTipText("bookmark");
		mark.setEnabled(true);
		if (bookmark.getItemCount() >=11) {
			mark.setEnabled(false);
		}
		for (int i=0; i < bookmark.getItemCount(); i ++) {
			if (bookmark.getItemAt(i).equals(showWord.getText())) {
				mark.setIcon(new ImageIcon("res/resources/images/unmarker.png"));
				mark.setToolTipText("remove marked word/phrase");
				mark.setEnabled(true);
				break;
			}
		}
		if (Dictionary.words == Dictionary.EVwords) {
			soundbutton.setEnabled(true);
		}
	}
	
	private void addFont() { // import font
		File fontFileb = new File("res/resources/font/timesbd.ttf");
		File fontFilei = new File("res/resources/font/timesi.ttf");
		File fontFiletitle = new File("res/resources/font/MTO Jamai.ttf");
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			
			fontb = Font.createFont(Font.TRUETYPE_FONT, fontFileb).deriveFont(16.0f);
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFileb));
			
			fonti = Font.createFont(Font.TRUETYPE_FONT, fontFilei).deriveFont(14.0f);
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFilei));
			
			titlefont = Font.createFont(Font.TRUETYPE_FONT, fontFiletitle).deriveFont(40.0f);
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, fontFiletitle));
			
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void setCursor() { //set cursor for each objects
		Point point1 = new Point(0, 0);
		Image cursorImage = new ImageIcon("res/resources/images/select.png").getImage();
		Cursor selectCur =Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,point1,"selectCursor");
		deletebutton.setCursor(selectCur);
		soundbutton.setCursor(selectCur);
		editbutton.setCursor(selectCur);
		
		Point point2 = new Point(0, 0);
		cursorImage = new ImageIcon("res/resources/images/wordcur.png").getImage();
		Cursor wordCur = Toolkit.getDefaultToolkit().createCustomCursor(cursorImage,point2,"wordCursor");
		meaning.setCursor(wordCur);
		suggestionBox.setCursor(wordCur);
		searchbox.setCursor(wordCur);
		showWord.setCursor(wordCur);
	}
	
	private void colorText() throws BadLocationException { //colored text in meaningbox
		StyledDocument doc = meaning.getStyledDocument();
		Style style = meaning.addStyle("Style", null);
		StyleConstants.setForeground(style, Color.BLACK);
		String[] meaningtext = meaning.getText().split("\n");
		meaning.setText("");
		for (String line: meaningtext) {
			if (line.startsWith(" *")) {
				StyleConstants.setForeground(style, Color.BLUE);
				doc.insertString(doc.getLength(), line + "\n", style);
			} else if (line.startsWith(" =")) {
				StyleConstants.setForeground(style, Color.GRAY);
				doc.insertString(doc.getLength(), line + "\n", style);
			} else if (line.startsWith(" [")){
				StyleConstants.setForeground(style, Color.RED);
				doc.insertString(doc.getLength(), line + "\n", style);
			} else {
				StyleConstants.setForeground(style, mainTextColor);
				doc.insertString(doc.getLength(), line + "\n", style);
			}
		}        
		
	}
//===============================================================================================
	public void speech(String text, String person) { // no longer avalible (for using speach API)
		VoiceManager voiceManager = VoiceManager.getInstance();
		com.sun.speech.freetts.Voice syntheticVoice = voiceManager.getVoice(person);
		syntheticVoice.allocate();
		syntheticVoice.speak(text);
		syntheticVoice.deallocate();
	}
//=========================================================================
	@Override
	public void actionPerformed(ActionEvent event) { // procceed all action for each button and boxes
		if (event.getSource() == soundbutton) {
			String soundFile = management.getFileSound(showWord.getText());
			if (!showWord.getText().equals("") && (soundFile!=null)) {
				String sounddir=soundFile;
				soundRender(sounddir);
			}
			if (soundFile==null) {
				speech(showWord.getText(),"mbrola_us1");
				status.setText("No quality audio for this word yet.");
			}
		}
		// delete a word
		if (event.getSource() == deletebutton) {
			if (!showWord.getText().isEmpty()) {
				if (confirmDelete() == JOptionPane.YES_OPTION) {
					management.removeWord(showWord.getText());
					showWord.setText("");
					status.setText("Deleted!");
					meaning.setText("");
					unchanged=false;
				}
			}
		}
		// check condition and start edit/add mode for user
		if (event.getSource() == editbutton) {
			if (meaning.isEditable()) {
				rawInput = meaning.getText();
				String after = meaning.getText();
				try {
					colorText();
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				meaning.setEditable(false);
				deletebutton.setEnabled(true);
				searchbox.setEditable(true);
				soundbutton.setEnabled(true);
				mark.setEnabled(true);
				bookmark.setEnabled(true);
				history.setEnabled(true);
				editbutton.setIcon(new ImageIcon("res/resources/images/edit.png"));
				editbutton.setToolTipText("add/edit");
				
				if (!after.isEmpty()) {
					if (after.charAt(0)!=' ') after = ' ' + after;			
					for (int i=1; i<after.length()-1; i++) {
						if (after.charAt(i)!=' ' && after.charAt(i-1)=='\n') {
							after = new StringBuilder(after).insert(i, ' ').toString();
						}
					}
					if (!prev.equals(after) && !showWord.getText().isEmpty()) {
						unchanged=false;
						management.editWordMeaning(showWord.getText(), after);
					}
				}
			}				
			else {
				StyledDocument doc = meaning.getStyledDocument();
				Style style = meaning.addStyle("Style", null);
				StyleConstants.setForeground(style, mainTextColor);
				meaning.setText("");
				try {
					doc.insertString(0, rawInput, style);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				meaning.setEditable(true);
				searchbox.setEditable(false);
				deletebutton.setEnabled(false);
				soundbutton.setEnabled(false);
				mark.setEnabled(false);
				bookmark.setEnabled(false);
				history.setEnabled(false);
				editbutton.setIcon(new ImageIcon("res/resources/images/edit2.png"));
				editbutton.setToolTipText("done?");
				prev = meaning.getText();
			}
		}
		// marked a word
		if (event.getSource() == mark) {
			boolean isExist= false;
			for (int i=0; i < bookmark.getItemCount(); i ++) {
				if (bookmark.getItemAt(i).equals(showWord.getText())) {
					isExist = true;
					break;
				}
			}
			if (!isExist) {
				if (bookmark.getItemCount() <21) {
					bookmark.addItem(showWord.getText());
					mark.setToolTipText("remove marked word/phrase");
					mark.setIcon(new ImageIcon("res/resources/images/unmarker.png"));
				}
				else {
					mark.setEnabled(false);
				}
			}
			else {
				mark.setIcon(new ImageIcon("res/resources/images/marker.png"));
				mark.setToolTipText("bookmark");
				bookmark.removeItem(showWord.getText());
			}
			try {
				outputBookmark("res/resources/bm.txt");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// get history word
		if (event.getSource() == history) {
			meaning.setText("");
			showWord.setText(history.getItemAt(history.getSelectedIndex()));
			rawInput = management.getWordMeaningFromDict(history.getItemAt(history.getSelectedIndex()));
			if (rawInput.length()==0) {
				status.setText("Word didn't exist in database. Please add this word. \nMake sure you use the correct Dictionary mode (EV/VE).");
			}
		 	meaning.setText(rawInput);
			history.setSelectedIndex(0);
			try {
				colorText();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		// get bookmark word
		if (event.getSource() == bookmark) {
			meaning.setText("");
			showWord.setText(bookmark.getItemAt(bookmark.getSelectedIndex()));
			rawInput = management.getWordMeaningFromDict(bookmark.getItemAt(bookmark.getSelectedIndex()));
			if (rawInput.length()==0) {
				status.setText("Word didn't exist in database. Please add this word. \nMake sure you use the correct Dictionary mode (EV/VE).");
			}
			meaning.setText(rawInput);
			bookmark.setSelectedIndex(0);
			try {
				colorText();
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		// set meaning and word when enter
		if (event.getSource() == searchbox) {
			String searchword = searchbox.getText().toLowerCase();
			searchbox.setText("");
			if (!searchword.equals("")) {
				showWord.setText(searchword);
				rawInput = management.getWordMeaningFromDict(showWord.getText());
				if (rawInput.length()==0) {
					status.setText("Word didn't exist in database. Please add this word. \nMake sure you use the correct Dictionary mode (EV/VE).");
				}
					meaning.setText(rawInput);
				try {
					colorText();
				} catch (BadLocationException e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	
}
