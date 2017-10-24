package ua.words.controller;

import java.io.*;
import java.util.*;

/**
 * Created by Администратор on 21.10.2017.
 */
public class ProcessingFile1 {

    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
                new Comparator<Map.Entry<K,V>>() {
                        public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res =  e1.getValue().compareTo(e2.getValue());
                        return res != 0 ? res : 1; // Special fix to preserve items with equal values
                    }
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }

    private HashSet t=new HashSet();
    List <String> longestWord = new ArrayList<String>();
    List <String> secondLongestWord = new ArrayList<String>();
    private int maxLength = 0;
    private int secondLongestWordLength = 0;
    private String file = "src/main/resources/words.txt";
//    private String file = getClass().getResource("/words.txt").toExternalForm();
    private int concatenatedWordsNumber = 0;
    //maintain a  lookup table to check if the word is present in test file
    private HashMap<String,Boolean> lookup=new HashMap<String,Boolean>() ;


    public void readFile() {

        try{
            FileInputStream f = new FileInputStream(file);
            byte[] buffer = new byte[(int) new File(file).length()];
            f.read(buffer);
            String text=new String(buffer);
            StringTokenizer st = new StringTokenizer(text);
            Map<String,Integer> wordList = new HashMap<String,Integer>();
            //insert tokens,token.length into hashmap
            while(st.hasMoreTokens())
            {
                String token=st.nextToken();
                wordList.put(token,token.length());
            }

            LinkedHashMap<String, Integer> lengthMap=new LinkedHashMap<String,Integer>();

            //insert the entries sorted by value into linkedhashmap
            for (Map.Entry<String, Integer> entry  : entriesSortedByValues(wordList)) {
                lengthMap.put(entry.getKey(), entry.getValue());
            }

            //Initial Trie Population
            initialHelpPopulation(lengthMap);
            //check if a concatenated word actually exists using a flag
            findLongestWordsInFile(lengthMap);

        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void initialHelpPopulation(LinkedHashMap<String,Integer> lengthMap)
    {

        for(String key:lengthMap.keySet())
        {
            t.add(key);
        }

    }



    public void findLongestWordsInFile(LinkedHashMap<String,Integer> lengthMap) {

//        String longestWord="";
//        String secondLongestWord = "";

        List<Map.Entry<String,Integer>> lengthMapList = new ArrayList(lengthMap.entrySet());

        for(int i = lengthMapList.size() -1; i >= 0 ; i --)
        {
            if(createCombinations(lengthMapList.get(i).getKey(),true))
            {

                if (lengthMapList.get(i).getKey().length()>maxLength){
                    secondLongestWordLength = maxLength;
                    maxLength = lengthMapList.get(i).getKey().length();
                    secondLongestWord.clear();
                    secondLongestWord.addAll(longestWord);
                    longestWord.clear();
                    longestWord.add(lengthMapList.get(i).getKey());

                }else if(lengthMapList.get(i).getKey().length()>secondLongestWordLength)
                {
                    secondLongestWordLength = lengthMapList.get(i).getKey().length();
                    secondLongestWord.clear();
                    secondLongestWord.add(lengthMapList.get(i).getKey());
                }else if (lengthMapList.get(i).getKey().length() == maxLength)
             {
                longestWord.add(lengthMapList.get(i).getKey());
            } else if (lengthMapList.get(i).getKey().length() == secondLongestWordLength) {
                secondLongestWord.add(lengthMapList.get(i).getKey());
            }


                concatenatedWordsNumber++;

            }
        }

        System.out.println("Concatenated words number = " + concatenatedWordsNumber);
//        System.out.println("Longest concatenated word is '" + longestWord + "'" + " with length = " + maxLength);
//        System.out.println("Second longest concatenated word is '" + secondLongestWord + "'");

        System.out.println("Longest concatenated words with length = " + maxLength);
        for (String str: longestWord){
            System.out.println(str);
        }
//        System.out.println(longestWord);


        System.out.println("Second longest concatenated words with length = " + secondLongestWordLength);
        for (String str: secondLongestWord){
            System.out.println(str);
        }

//        System.out.println(secondLongestWord);
    }



    boolean createCombinations(String word,boolean firstCall) {

    //check if its the firstCall and if the word is already present in the lookup table. return the table value for the word
        if(!firstCall && lookup.containsKey(word))
        {
            return lookup.get(word);
        }
        if (firstCall)
        {
            t.remove(word);
        }
        //from n=word.length down to 1 check if different the word is concatenated using already present words in the trie
        //if we reach end of loop then we shall conclude that the word is not a concatenated word and return false
        for(int i=word.length()-1;i>=0;i--)
        {
            if(t.contains(word.substring(0, i+1))) //if substring is in trie
            {
                //and if entire word param is in trie or (substring and) remainder is in trie
                if((i==word.length()-1)||createCombinations(word.substring(i + 1, word.length()), false))
                {
                    lookup.put(word, true);
                    if(firstCall) t.add(word);
                    return true;
                }
            }
        }

        lookup.put(word, false);
        if (firstCall) t.add(word);

        return false;
    }

}
