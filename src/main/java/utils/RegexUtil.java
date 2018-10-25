package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {


    /**
     * Utillity method to get all Strings from a Text that match the given Regex.
     * @param regex The regex that shall be used for analyzing
     * @param text The text to be analyze
     * @return a List of all Strings that match the given Regex
     */
    public static List<String> findAllMatches(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        List<String> matchList = new ArrayList<>();

        while (matcher.find()) {
            String match = matcher.group();
            matchList.add(match);
        }


        return matchList;
    }





}
