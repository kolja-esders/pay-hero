package edu.pietro.team.payhero.helper;

/**
 * Created by david on 26.11.16.
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.routines.IBANValidator;
import org.apache.commons.lang3.*;



public class LangAnalytics {


    static final String regExp = "[0-9]+([,.][0-9]{1,2})[€]*?";
    static final Pattern pattern = Pattern.compile(regExp);
    

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
            Matcher matcher = pattern.matcher(subText);
            if(matcher.matches()){
                subText = subText.replace(",",".");
                subText = subText.replace("€","");
                return subText;
            }
        }

        return "";
    }




}
