package management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import database.Dictionary;
import database.Word;

public class DictionaryManagement {
	
	private Map<String, String> wav = new HashMap<String, String>(); // audio & path
	private final File vndir = new File("res/resources/dict_vie_eng.dict");  
	private final File endir = new File("res/resources/dict_eng_vie.dict");
	private static int searchMode=1;
	
//======================================================================= get data & index
	public void insertFromFile() throws IOException {
		
		Dictionary.words.clear();
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(endir), "UTF8"));
		
		String word;
		Word newWord = new Word();
		while ((word = in.readLine()) != null)
		{
			if (word.startsWith(" ")) {
				newWord.setWordMeaning(newWord.getWordMeaning()+ word + "\n");
			}
			else {
				newWord = new Word();
				Dictionary.EVwords.add(newWord);
				newWord.setWord(word);
			}
		}
		in.close();
		in = new BufferedReader(new InputStreamReader(new FileInputStream(vndir), "UTF8"));
		newWord = new Word();
		while ((word = in.readLine()) != null)
		{
			if (word.startsWith(" ")) {
				newWord.setWordMeaning(newWord.getWordMeaning()+ word + "\n");
			}
			else {
				newWord = new Word();
				Dictionary.VEwords.add(newWord);
				newWord.setWord(word);
			}
		}
		in.close();
	}
	
	public void importFileIndex() throws IOException {
		
		File dir = new File("res/resources/dict_soundindex.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dir), "UTF8"));
		
		String word;
		while ((word = in.readLine()) != null)
		{
			if (word.endsWith(".wav")) {
				String wavfile=new String(word);
				word = in.readLine();
				wav.put(word, wavfile);				
			}
		}
		in.close();
	}
//======================================================================================dict switch	

	public void switchDict(boolean option) {

		if(option) {
			Dictionary.words = Dictionary.VEwords;
		}
		else {
			Dictionary.words = Dictionary.EVwords;
		}
	}

	
//================================================================================= edit data file
	
	public void exportFileDict(boolean option) throws IOException {
		
		File dir;
		if (option) {
			dir=vndir;	
		}
		else {
			dir=endir;
		}
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir),"UTF8"));
		for (Word word : Dictionary.words) {
			out.write(word.getWord() + "\n");
			out.write(word.getWordMeaning());
		}
		out.close();
	}

	public void exportSearchToFile(String result) throws IOException {
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("res/resources/searchingresult.txt"),"UTF8"));
		out.close();
		PrintWriter fout = new PrintWriter(
				new FileWriter("res/resources/searchingresult.txt",true));
		fout.println(result);
		fout.close();
	}
//=================================================================================== searching tool
	
	public String dictionarySearch (String finding) {
		ArrayList<String> words = new ArrayList<String> ();
		for (Word word : Dictionary.words) {
			words.add(new String(word.getWord()));
		}
		Collections.sort(words);
		finding = finding.toLowerCase();
		int wordNo=0;
		String result = new String();
		if (searchMode ==1 ) {
			for (String word : words) {
				if (word.startsWith(finding)) {
					wordNo++;
					result = result + "- " + word + "\n";
					if (wordNo>=5) break;
				}
			}
		}
		if (searchMode == 2) {
			for (String word : words) {
				if (word.contains(finding)) {
					wordNo++;
					result = result + "- " + word + "\n";
					if (wordNo>=5) break;
				}
			}		
		}
		if (wordNo==0) return "Found no word";
		result = "Did you mean: \n" + result;
		return result;
	}

	public String getWordMeaningFromDict (String finding) {
		
		for (Word word: Dictionary.words) {
			if (word.getWord().equals(finding))
			{
				return word.getWordMeaning();
			}
		}
		return "";		
	}
	
	public String getFileSound(String finding) {
		return wav.get(finding);
	}
	
//============================================================================ edit word
	
	public void editWordMeaning (String finding, String meaning) {
		boolean hasWord = false;
		for (Word word: Dictionary.words) {
			if (word.getWord().equals(finding))
			{
				word.setWordMeaning(meaning + "\n");
				hasWord=true;
				break;
			}
		}
		Word newWord = new Word(finding, meaning + "\n");
		if (!hasWord) Dictionary.words.add(newWord);
	}
	
	public void removeWord(String finding) {
		
		int wordNo=0;
		for (Word word: Dictionary.words) {
			wordNo++;
			if (word.getWord().equals(finding))
			{				
				Dictionary.words.remove(wordNo-1);
				break;
			}
		}
	}
//=========================================================================== sub function
	public static void setSearchMode(int mode) {
		searchMode=mode;
	}
	
	public int getSearchMode() {
		return searchMode;
	}
}
