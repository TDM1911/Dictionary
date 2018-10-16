package database;

public class Word {
	private String word_target, word_explain;
	
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
