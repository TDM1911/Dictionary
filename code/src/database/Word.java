package database;

public class Word {
	private String word_target, word_explain;
	
	public Word() {
		word_target="";
		word_explain="";
	}
	
	public Word(String word, String meaning) { 
		word_target=word;
		word_explain=meaning;
	}
	
	public String getWord() {
		return word_target;
	}
	
	public String getWordMeaning() {
		return word_explain;
	}
	
	public void setWord(String newWord) {
		word_target= newWord;
	}
	
	public void setWordMeaning(String newWord) {
		word_explain= newWord;
	}
	
}
