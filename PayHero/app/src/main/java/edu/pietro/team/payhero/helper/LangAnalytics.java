package edu.pietro.team.payhero.helper;

/**
 * Created by david on 26.11.16.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.lang3.*;



public class LangAnalytics {

    static final String regExp0 = "[0-9]+([,.][0-9][0-9]){0,1}?";
    static final Pattern pattern0 = Pattern.compile(regExp0);

    static final String regExp1 = "[0-9]+([,.][0-9]{2})[€]*?";
    static final Pattern pattern1 = Pattern.compile(regExp1);

    static final String regExp2 = "[0-9]+(([,.][0-9][0-9]){0,1})[€]+?";
    static final Pattern pattern2 = Pattern.compile(regExp2);

    static final String regExpIb = "[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}";
    static final Pattern patternIb = Pattern.compile(regExpIb);


    public static void main(String args[]){

        System.out.println(getIBAN("4.00 Euro DE277DE27796665480003292312 34trr"));

    }


    public static String getIBAN(String text){

        text = text.replace(" ", "");

        Matcher matcher = patternIb.matcher(text);
        int start = 0;

        while(matcher.find(start)){

            for(int i = 15; i < 34; i++) {
                if(matcher.start() + i > text.length()){
                    break;
                }
                String subText = text.substring(matcher.start(), matcher.start() + i);

                if (IBANValidator.getInstance().isValid(subText)) {
                    return subText;
                }
            }
            start =  matcher.start() + 4;

        }

        return "";
    }


    public static String getAmount(String text){

        String[] splitText = text.split(" ");

        for(int i = 0; i < splitText.length ; i++){
            String subText = splitText[i];
            Matcher matcher = pattern2.matcher(subText);
            if(matcher.matches()){
                subText = subText.replace(",",".");
                subText = subText.replace("€","");
                return subText;
            }
        }

        for(int i = 0; i < splitText.length ; i++){
            String subText = splitText[i];
            Matcher matcher = pattern1.matcher(subText);
            if(matcher.matches()){
                subText = subText.replace(",",".");
                subText = subText.replace("€","");
                return subText;
            }
        }

        for(int i = 0; i < splitText.length ; i++){
            String subText = splitText[i];
            if (subText.contains("Euro") && subText.length() > 4){
                String numText = subText.replace("Euro", "");
                Matcher matcher = pattern0.matcher(numText);
                if(matcher.matches()){
                    return numText;
                }
            }
            else if(i > 0){
                String numText = splitText[i-1];
                Matcher matcher = pattern0.matcher(numText);
                if(matcher.matches()){
                    return numText;
                }
            }
        }

        return "";
    }




}
