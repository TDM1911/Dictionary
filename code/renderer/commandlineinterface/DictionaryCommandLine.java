package renderer.commandlineinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import database.Dictionary;
import database.Word;
import management.DictionaryManagement;

public class DictionaryCommandLine {
	
	public void showAllWords () {
		int wordNo=0;
		String format = "%-6d |%-45s |%s \n";
		System.out.println("No    |Word                  |Meaning");
		for (Word word : Dictionary.words) {
			wordNo++;
			System.out.format(format,wordNo, word.getWord(), word.getWordMeaning());
		}
	}
	
	public void showByPage (int page) {
		int wordNo=0;
		String format = "%-6d |%-45s |%s \n";
		System.out.println("No    |Word                  |Meaning");
		for (Word word : Dictionary.words) {
			wordNo++;
			if ((wordNo>page*100) && (wordNo<=page*100+109)) {
				System.out.format(format,wordNo, word.getWord(), word.getWordMeaning());
			}
			if (wordNo>page*100+100) {
				break;
			}
		}
	}
	
	public void instructionCommandLineInterface() {
		System.out.println("============================================");
		System.out.println("|                Command line              |");
		System.out.println("| show_all_words: hien thi cac tu          |");
		System.out.println("| show_by_page: hien thi theo trang 100 tu |");
		System.out.println("| ?, help: hien thi tro giup               |");
		System.out.println("| search: tim kiem tu                      |");
		System.out.println("| exit, x: thoat chuong trinh              |");
		System.out.println("============================================");
	}
	
	public void dictionaryLookup (String finding) {
		DictionaryManagement management = new DictionaryManagement();
		management.dictionarySearch(finding);
		try {
			management.dictionaryExportToFile(finding);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void dictionaryAdvance() throws Exception{
		
		BufferedReader s = new BufferedReader(new InputStreamReader(System.in));
		String commandin = new String();
		
		boolean repeat=true;
		instructionCommandLineInterface();
		System.out.print("Nhap cau lenh:");
		DictionaryManagement management = new DictionaryManagement();
		try {
			management.insertFromFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
			commandin= s.readLine();
		do {
			switch (commandin)
			{
				case "show_all_words": 
				{
					showAllWords();
					System.out.print("Nhap cau lenh:");
					commandin = s.readLine();
					break;
				}
				case "show_by_page": 
				{
					System.out.println("enter page number: (0~" + (int)Dictionary.words.size()/100 + ")");
					int page = Integer.parseInt(s.readLine());
					if (page<0 || page>(int)Dictionary.words.size()/100) {
						System.out.println("Error page number: No such page.");
					}
					showByPage(page);
					System.out.print("Nhap cau lenh:");
					commandin = s.readLine();
					break;
				}
				case "?": 
				{
					instructionCommandLineInterface();
					System.out.print("Nhap cau lenh:");
					commandin = s.readLine();
					break;
				}
				case "help":
				{
					instructionCommandLineInterface();
					System.out.print("Nhap cau lenh:");
					commandin = s.readLine();
					break;
				}
				case "search":
				{
					System.out.println("enter searching word:");
					String finding = s.readLine();
					dictionaryLookup(finding);
					System.out.print("Nhap cau lenh:");
					commandin = s.readLine();
					break;
				}
				case "exit":
				{
					break;
				}
				case "x" :
				{
					break;
				}
				default:
				{
					System.out.println("Khong ton tai cau lenh.");
					System.out.print("Nhap lai cau lenh:");
					commandin = s.readLine();
					break;
				}
			}
			if (commandin.equals("exit") || commandin.equals("x")) {
				repeat=false;
			}
		}
		while (repeat);
		s.close();
	}
}
