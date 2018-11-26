package study.AttributeAnalyzer;

import model.Decision;
import utils.RegexUtil;

import java.util.List;

public class DocketNumberAnalyzer {

    private static DocketNumberAnalyzer docketNumberAnalyzer;

    public static DocketNumberAnalyzer getInstance(){
        if(docketNumberAnalyzer == null){
            docketNumberAnalyzer = new DocketNumberAnalyzer();
        }

        return docketNumberAnalyzer;
    }

    private DocketNumberAnalyzer(){}

    /**
     * Convenience Method to find all Docket Numbers in a given String.
     *
     * @param documentText The text to analyze
     * @return A list of all Strings that match the Regex
     */
    public Decision analyzeDocumentForDocketNumber(String documentText, Decision decision) {

        String docketNumberRegex = "((VGS|RiZ\\s?s?\\(R\\)|KZR|VRG|RiZ|EnRB|StbSt\\s?\\(B\\)|AnwZ\\s?\\(Brfg\\)|RiSt|PatAnwSt\\s?\\(R\\)|AnwZ\\s?\\(B\\)|PatAnwZ|EnVZ|AnwSt\\s?\\(B\\)|NotSt\\s?\\(Brfg\\)|KVZ|KZB|AR\\s?\\(Ri\\)|NotZ\\s?\\(Brfg\\)|RiSt\\s?\\(B\\)|AnwZ\\s?\\(P\\)|EnZB|RiSt\\s?\\(R\\)|NotSt\\s?\\(B\\)|AnwSt|WpSt\\s?\\(R\\)|KVR|AR\\s?\\(Kart\\)|EnZR|StbSt\\s?\\(R\\)|WpSt\\s?\\(B\\)|KZA|AR\\s?\\(Enw\\)|AnwSt\\s?\\(R\\)|KRB|RiZ\\s?\\(B\\)|PatAnwSt\\s?\\(B\\)|EnVR|AnwZ|NotZ|EnZA|AR)\\s\\d+/\\d+)|" +
                "((GSZ|LwZB|WpSt\\s?\\(B\\)|AnwZ|LwZR|KVZ|EnRB|PatAnwSt\\s?\\(B\\)|ARP|VGS|WpSt\\s?\\(R\\)|RiSt\\s?\\(B\\)|EnZA|KRB|AnwSt\\s?\\(R\\)|NotSt\\s?\\(Brfg\\)|EnVR|LwZA|ZB|AR\\s?\\(Vollz\\)|StB|ZR|AR\\s?\\(VS\\)|BJs|BLw|NotZ\\s?\\(Brfg\\)|RiZ\\s?\\(B\\)|PatAnwSt\\s?\\(R\\)|AK|RiZ|PatAnwZ|ARs|StbSt\\s?\\(R\\)|VRG|NotSt\\s?\\(B\\)|AR\\s?\\(Enw\\)|AR\\s?\\(VZ\\)|StE|KVR|AR\\s?\\(Ri\\)|AR|AnwSt|NotZ|StbSt\\s?\\(B\\)|StR|ZA|AnwZ\\s?\\(B\\)|EnZR|AR\\s?\\(Kart\\)|GSSt|AnwZ\\s?\\(P\\)|ZR\\s?\\(Ü\\)|AnwZ\\s?\\(Brfg\\)|KZB|BGns|KZR|RiSt|KZA|BAusl|AnwSt\\s?\\(B\\)|BGs|RiZ\\s?\\(R\\)|EnZB|RiSt\\s?\\(R\\)|ARZ|EnVZ)\\s\\d+/\\d+)|" +
                "([I+|IV|V|VI|VII|VIII|IX|X|XI|XII|1-6]+[a-z]?\\s[A-Za-z\\(\\)]{2,20}\\s\\d+/\\d\\d)";


        List<String> docketNumberList = RegexUtil.findAllMatches(docketNumberRegex, documentText);


        if (!docketNumberList.isEmpty()) {
            decision.setDocketNumber(docketNumberList.get(0));
        }



        return decision;
    }


}
