package com.bodicount;

public class Helpers4Nithula{

    public static String checkIfEmpty(String inString ){
        if (inString == null || inString.length() < 0){
            return "-";
        }
        return inString;
    }
}
