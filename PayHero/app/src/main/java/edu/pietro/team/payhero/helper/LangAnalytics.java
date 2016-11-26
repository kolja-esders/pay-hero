package edu.pietro.team.payhero.helper;

/**
 * Created by david on 26.11.16.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.lang3.*;



public class LangAnalytics {


    static final String regExp1 = "[0-9]+([,.][0-9]{2})[€]*?";
    static final Pattern pattern1 = Pattern.compile(regExp1);

    static final String regExp2 = "[0-9]+(([,.][0-9][0-9]){0,1})[€]+?";
    static final Pattern pattern2 = Pattern.compile(regExp2);


    public static void main(String args[]){

        System.out.println(getAmount("4.00 5€"));

    }


    public static String getIBAN(String text){

        String[] splitText = text.split(" ");

        for(String subText : splitText){
            if(subText.length() > 10){
                if(IBANValidator.getInstance().isValid(subText)){
                    return subText;
                }
            }
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

        return "";
    }




}
