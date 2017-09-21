package ua.words;

import ua.words.controller.ProcessingFile;

/**
 * Created by Администратор on 18.09.2017.
 */
public class App {
    public static void main(String[] args) {

        ProcessingFile processingFile = new ProcessingFile();
        processingFile.readFile();
        processingFile.findLongestWordsInFile();


    }

}
