package ua.words;

import ua.words.controller.ProcessingFile1;

/**
 * Created by Администратор on 18.09.2017.
 */
public class App {
    public static void main(String[] args) {

        long start=System.currentTimeMillis();
        ProcessingFile1 processingFile = new ProcessingFile1();
        processingFile.readFile();

        long end=System.currentTimeMillis();
        System.out.println("time taken="+(end-start)+" ms");
    }

}
