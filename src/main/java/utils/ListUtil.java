package utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    public static List<String> makeAllStringsLowerCase(List<String> listToModify){
        List<String> listLowerCase = new ArrayList<>();
        listToModify.forEach(word -> listLowerCase.add(word.toLowerCase()));

       return listLowerCase;
    }

}
