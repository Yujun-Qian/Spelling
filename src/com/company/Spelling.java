package com.company;


import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Spelling {
    private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();

    public Spelling(String file) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(file));
        Pattern p = Pattern.compile("\\w+");
        for(String temp = ""; temp != null; temp = in.readLine()){
            Matcher m = p.matcher(temp.toLowerCase());
            while(m.find()) nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
        }
        in.close();
    }

    private final ArrayList<String> edits(String word) {
        ArrayList<String> result = new ArrayList<String>();
        // remove a letter
        for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
        // swap adjacent letters
        for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
        // change a letter
        for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
        // insert a letter
        for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
        return result;
    }

    public final String correct(String word) {
        if(nWords.containsKey(word)) return word;
        ArrayList<String> list = edits(word);
        HashMap<Integer, String> candidates = new HashMap<Integer, String>();
        for(String s : list) {
            if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
        }
        if(candidates.size() > 0)
            return candidates.get(Collections.max(candidates.keySet()));

        for(String s : list) {
            for(String w : edits(s)) {
                if(nWords.containsKey(w)) {
                    candidates.put(nWords.get(w),w);
                }
            }
        }
        return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : word;
    }

    public static void main(String args[]) throws IOException {
        Spelling spelling = new Spelling("big.txt");
        if(args.length > 0) System.out.println(spelling.correct(args[0]));

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = null;

            while ((input = br.readLine()) != null) {
                System.out.println(spelling.correct(input));
                System.out.println();
            }
        } catch (Exception e) {

        }
    }
}
