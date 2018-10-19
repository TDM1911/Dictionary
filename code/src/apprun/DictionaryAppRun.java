package apprun;

import java.awt.EventQueue;

import renderer.basicgraphicinterface.DictionaryApplicationGraphic;


public class DictionaryAppRun { // run programe
	
	public static void main(String[] args) {
		System.setProperty("mbrola.base", "res/resources/mbrola");
        EventQueue.invokeLater(() -> {
        	DictionaryApplicationGraphic renderapp = new DictionaryApplicationGraphic();
            renderapp.setVisible(true);
        });
	}
}
