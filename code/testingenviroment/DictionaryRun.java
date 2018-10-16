package testingenviroment;

import java.io.IOException;

import renderer.commandlineinterface.DictionaryCommandLine;

public class DictionaryRun {

	public static void main(String[] args) throws Exception {
		
		DictionaryCommandLine renderer = new DictionaryCommandLine();
		try {
			renderer.dictionaryAdvance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
