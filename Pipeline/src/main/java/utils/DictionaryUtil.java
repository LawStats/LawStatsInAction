package utils;

import model.DictionaryType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DictionaryUtil {


    public static Map<String, String> getMapFromCourtDictionary(){
        Map<String, String> dictionaryMap = new HashMap<>();
        String pathToFile = DictionaryType.courtDictionary;

        List<String> lines = FileUtil.getFileAsStringList(pathToFile);

        for(String line : lines){
            String[] rowValues = line.split(",");

            String standardizedForm = rowValues[0];

            for(int i = 2; i < rowValues.length; i++){
                dictionaryMap.put(rowValues[i], standardizedForm);
            }
        }

        return dictionaryMap;
    }


    public static List<String> getListFromJudgeDictionary(){
        List<String> dictionaryList = new ArrayList<>();
        String pathToFile = DictionaryType.judgeDictionary;

        List<String> lines = FileUtil.getFileAsStringList(pathToFile);

        for(String line : lines){
            String[] rowValues = line.split(",");

            dictionaryList.add(rowValues[0]);
        }

        return dictionaryList;
    }


    public static List<String> getVonJudgesFromDictionary(){

        String pathToFile = DictionaryType.vonJudgeDictionary;

        List<String> vonJudges = FileUtil.getFileAsStringList(pathToFile);
        vonJudges.forEach(judge -> judge = judge.trim());


        return vonJudges;
    }

}
