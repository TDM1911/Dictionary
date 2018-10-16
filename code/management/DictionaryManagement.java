package management;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import database.Dictionary;
import database.Word;

public class DictionaryManagement {
	
	public void insertFromFile() throws IOException, Exception {
		
		InputStream input = DictionaryManagement.class.getResourceAsStream("/res/dict_vie_eng_nosign_tab.txt");
		ClassLoader loader = DictionaryManagement.class.getClassLoader();
		File file = new File(loader.getResource("res/dict_vie_eng_nosign_tab.txt").getFile());
		System.out.println(file.getPath());
		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		
		String word;
		Word newWord = new Word();
		while ((word = in.readLine()) != null)
		{
			String temp[] = word.split("\t");
			if (temp.length>1)
			{
				newWord.setWord(temp[0]);
				newWord.setWordMeaning(temp[1]);
				Dictionary.words.add(newWord);
			}
			newWord = new Word();
		}
		in.close();
	}
	
	public void dictionarySearch (String finding) {
		int wordNo=0;
		String format = "%-6d |%-45s |%s \n";
		for (Word word : Dictionary.words) {
			if (word.getWord().startsWith(finding)) {
				wordNo++;
				if (wordNo==1) {
					System.out.println("No     |Word                                              |Meaning");
				}
				System.out.format(format,wordNo, word.getWord(), word.getWordMeaning());
			}
		}
		if (wordNo==0) System.out.println("Found no word");
	}
	
	public void dictionaryExportToFile(String finding) throws IOException {
		int wordNo=0;
		PrintWriter fout = new PrintWriter("result.txt");
		for (Word word : Dictionary.words) {
			Scanner wordsearch = new Scanner(word.getWord());
			if (wordsearch.findInLine(finding) != null) {
				wordNo++;
				fout.println(wordNo + ". "  + word.getWord() + " : " + word.getWordMeaning());
			}
			wordsearch.close();
		}		
		if (wordNo==0) fout.println("Found no word");
		fout.close();
	}
	
}
