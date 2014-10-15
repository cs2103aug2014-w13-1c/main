package app.controllers;

import java.util.ArrayList;

import app.helpers.Keyword;

public class CommandParser {
    private ArrayList<String> keywords = new ArrayList<String>();
    private ArrayList<String> startDateKeywords = new ArrayList<String>();
    private ArrayList<String> endDateKeywords = new ArrayList<String>();
    
    public CommandParser() {
        setKeywords();
    }
    
    private void setKeywords() {
        startDateKeywords.clear();
        startDateKeywords.add("start");
        
        endDateKeywords.clear();
        endDateKeywords.add("end");
        
        keywords.clear();
        keywords.add("add");
        keywords.add("delete");
        keywords.add("display");
        keywords.add("clear");
        keywords.add("exit");
        keywords.add("search");
        keywords.add("update");
        keywords.add("help");
        keywords.add("settings");
        keywords.addAll(startDateKeywords);
        keywords.addAll(endDateKeywords);
    }
    
    public ArrayList<Keyword> getKeywords(String inputString) {
        ArrayList<Keyword> currentKeywords = new ArrayList<Keyword>();
        String inputStringArray[] = inputString.trim().split(" ");
        int startIndex = 0, endIndex = 0;
        for (int i = 0; i < inputStringArray.length; i++) {
            endIndex = startIndex + inputStringArray[i].length() - 1;
            if (keywords.contains(inputStringArray[i])) {
                currentKeywords.add(new Keyword(startIndex, endIndex));
            }
            startIndex = endIndex + 2;
        }
//        for (int i = 0; i < currentKeywords.size(); i++) {
//            System.out.println(currentKeywords.get(i).getStartIndex() + " " + currentKeywords.get(i).getEndIndex());
//        }
        return currentKeywords;
    }
    
}
