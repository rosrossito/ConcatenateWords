package ua.words.controller;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Администратор on 18.09.2017.
 */
public class ProcessingFile {

    private HashSet<String> words = new HashSet<String>();
    private ArrayList<Integer> seq = new ArrayList();
    private ArrayList result = new ArrayList();
    private List<List> resultAll = new ArrayList<List>();
    private HashSet concatenatedWords = new HashSet();

    final int minWordsNumber = 2;
    private int wordsnumber = 1;
    private String word;
    private int maxLength = 0;
    private int secondLongestWordLength = 0;
    private String file = "src/main/resources/words.txt";
    //String file = getClass().getResource("/words.txt").toExternalForm();


    private int counter = 0;
    private int concatenatedWordsNumber = 0;

    //public List readFile() {
    public HashSet readFile() {
        String s;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while ((s = br.readLine()) != null) {
                words.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }


    public void findLongestWordsInFile() {

        List <String> longestWord = new ArrayList<String>();
        List <String> secondLongestWord = new ArrayList<String>();
//        String longestWord = "";
//        String secondLongestWord = "";
        int wordLength = 0;
        int maxWordsNumber;

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            while ((word = br.readLine()) != null) {
                System.out.println(wordsnumber++);
                wordLength = word.length();
                maxWordsNumber = wordLength / minWordsNumber;
                seq = createSeq(minWordsNumber, wordLength - minWordsNumber);

                for (int i = minWordsNumber; i < maxWordsNumber + 1; i++) {
                    createCombinations(wordLength, i, counter);

                }

                //write length if word is longer and count
                //cycle for all combinations
                for (List combination : resultAll) {

                    boolean success = checkWord(combination);
                    if (success) {

                        if (!concatenatedWords.contains(word)) {
                            concatenatedWords.add(word);
//                            System.out.println(word);

                            int l = word.length();
                            if (l > maxLength) {
                                secondLongestWordLength=maxLength;
                                maxLength = l;
                                secondLongestWord.clear();
                                secondLongestWord.addAll(longestWord);
                                longestWord.clear();
                                longestWord.add(word);
//                                secondLongestWord = longestWord;
//                                longestWord = word;
                            } else if (l == maxLength){
                                longestWord.add(word);
                            }else if (l == secondLongestWordLength){
                                secondLongestWord.add(word);
                            }
                            concatenatedWordsNumber++;
                        }
                    }
                }
                resultAll.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Concatenated words number = " + concatenatedWordsNumber);
//        System.out.println("Longest concatenated word is '" + longestWord + "'" + " with length = " + maxLength);
//        System.out.println("Second longest concatenated word is '" + secondLongestWord + "'");

        System.out.println("Longest concatenated words with length = " + maxLength);
        for (String str: longestWord){
            System.out.println(str);
        }

        System.out.println("Second longest concatenated words with length = " + secondLongestWordLength);
        for (String str: secondLongestWord){
            System.out.println(str);
        }

    }


    public ArrayList createSeq(int minWordsNumber, int maxLettersNumber) {
        seq.clear();
        for (int i = minWordsNumber; i < maxLettersNumber + 1; i++) {
            seq.add(i);
        }
        return seq;
    }


    void createCombinations(int wordLength, int wordsNumber, int counter) {

        if (wordsNumber < 0 || wordLength < 0) return;
        else if (wordLength == 0 && !(wordsNumber == 0)) return;
        else if (!(wordLength == 0) && wordsNumber == 0) return;
        else if (wordLength == 0 && wordsNumber == 0) {
            copyResult(result);
        } else {
            for (; counter < seq.size(); ++counter) {
                result.add(seq.get(counter));
                createCombinations(wordLength - seq.get(counter), wordsNumber - 1, 0);
                result.remove(result.size() - 1);
            }
        }
    }



    void copyResult(ArrayList result) {
        ArrayList temp = new ArrayList(result);
            resultAll.add(temp);
         }


    //divide word and check if it is concatenated word
    boolean checkWord(List<Integer> combination) {
        //take combination, divide word on part and check part by part if this part is word from glossary
        int startPoint = 0;

        for (Integer point : combination) {
            int endPoint = startPoint + point;

            //optimal search
            if (!words.contains(word.substring(startPoint, endPoint))){
                return false;
            }

            startPoint = endPoint;
        }
        return true;
    }
}
